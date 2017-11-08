package ru.breffi.Salesforce2StoryReplicator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.DeletedRecord;
import com.sforce.soap.partner.GetDeletedResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.EntityTypeConverterServicePackage.PartnerTypeConverterService;
import ru.breffi.storyclmsdk.*;
import ru.breffi.storyclmsdk.Exceptions.*;
import ru.breffi.storyclmsdk.connectors.StoryCLMServiceConnector;

public class Replicator {
	public SalesForceLoginConfig sfconfig;
	public StoryLoginConfig storyConfig;
	final Logger logger = LoggerFactory.getLogger(Replicator.class);

	public void setSfconfig(SalesForceLoginConfig sFconfig) {
		this.sfconfig = sFconfig;

	}

	public void setStoryConfig(StoryLoginConfig storyConfig) {
		this.storyConfig = storyConfig;
	}

	PartnerConnection connection;

	PartnerConnection getConnection() throws ConnectionException {
		if (connection == null) {
			ConnectorConfig config = new ConnectorConfig();
			config.setUsername(sfconfig.UserName);
			config.setPassword(sfconfig.Password);
			config.setAuthEndpoint(sfconfig.AuthEndpoint);
			connection = Connector.newConnection(config);
		}
		return connection;
	}

	PartnerConnectionUtils _partnerConnectionUtils;

	PartnerConnectionUtils getPartnerConnectionUtils() throws ConnectionException {
		if (_partnerConnectionUtils == null) {

			_partnerConnectionUtils = new PartnerConnectionUtils(getConnection());
		}
		return _partnerConnectionUtils;
	}

	StoryCLMServiceConnector clientConnector;

	StoryCLMServiceConnector getStoryConnector() {
		if (clientConnector == null) {
			clientConnector = StoryCLMConnectorsGenerator.CreateStoryCLMServiceConnector(storyConfig.ClientId,
					storyConfig.ClientSecret, null, null, null);
		}
		return clientConnector;
	}

	public void Replicate(PartnerTypeConverterService converterService)
			throws AsyncResultException, ConnectionException, AuthFaliException {
		logger.info("Start replicate for StoryType: " + converterService.getStoryType().getSimpleName());

		// Проверим соответствие двух систем по количеству записей
		Integer sfcountall =  getPartnerConnectionUtils().GetCount(converterService.getSFTable());
			 
		StoryCLMTableService<StoryLog> slogService = getStoryConnector().GetTableService(StoryLog.class,
				converterService.getLogTableId());
		StoryCLMTableService<IStoryEntity> storyService = clientConnector
				.GetTableService(converterService.getStoryType(), converterService.getTableId());
		Date thisReplicationDate = new Date();
		StoryLog slog = new StoryLog();
		slog.Date = thisReplicationDate;

		Boolean fullReplicateNeed = false;

		try {

			// Достаем запись из журнала
			Date lastReplicationDate = slogService.MaxOrDefault("Date", null, Date.class, new Date(0)).GetResult();

			logger.info("Last Replication Date " + lastReplicationDate);

			// Запрашиваем, что изменилось в SF
			String query = MessageFormat.format(
					"SELECT {0} FROM {1} where LastModifiedDate > {2} and LastModifiedDate <= {3} ",
					String.join(", ",converterService.getSFQueryFields()), converterService.getSFTable(),
					Utils.GetIsoDate(lastReplicationDate), Utils.GetIsoDate(thisReplicationDate));
			logger.info("SOQL: " + query);
			// По данному запросу переносим sobjects в стори
			fromsftostory(query, converterService, slog, storyService);

			// В методе удаленные из SF записи удаляются из story
			slog.Deleted = removeDeleted(lastReplicationDate, thisReplicationDate, converterService.getSFTable(),
				converterService.getSFIdFieldName(), storyService);
			logger.info("*****" + converterService.getStoryType().getSimpleName() + "***** " + "Insert log ");

			long storyCount = storyService.Count().GetResult();
			fullReplicateNeed = storyCount != sfcountall;
			if (fullReplicateNeed) {
				slog.AddNote(":Не совпадает количество записей в таблице " + storyCount + " и SF: " + sfcountall);
				slog.AddNote("Требуется полная репликация.");
			}
		}
		// Перехватываем все исключения и пытаемся запихнуть сообщение в лог
		catch (Exception e) {
			// Попробуем отправить сообщение к нам в лог
			slog.Failed = true;
			slog.AddNote("Exception happens");
			slog.AddNote(Utils.JoinStackTrace(e));
		} finally {
			StoryLog slogLast = slogService.LastOrDefault(null, "Date", 1, null).GetResult();
			if (slogLast != null && slogLast.equals(slog)) {
				slog._id = slogLast._id;
				slog.Attempts = slogLast.Attempts + 1;
				slogService.Update(slog).GetResult();
			} else
				slogService.Insert(slog).GetResult();
			logger.info("Finish replicate for StoryType: " + converterService.getStoryType().getSimpleName());
		}

		

		if (!slog.Failed && fullReplicateNeed) {
			FullReplicate(converterService);
			logger.info("Finish fullreplicate for StoryType: " + converterService.getStoryType().getSimpleName());
			return;
		}
	}

	/**
	 * 1. ВСе объекты SF обновляем в стори 
	 * 2. Далее все объекты стори провеярем на наличие в SF, чего нет удаляем в стори
	 * @param converterService
	 * @throws AuthFaliException
	 * @throws AsyncResultException
	 */
	private void FullReplicate(PartnerTypeConverterService converterService) throws AuthFaliException, AsyncResultException {
		Date thisReplicationDate = new Date();
		StoryLog slog = new StoryLog();
		logger.info("Полная репликация из-за несовпадения по количеству элементов");
		slog.AddNote("Полная репликация из-за несовпадения по количеству элементов");
		slog.Date = thisReplicationDate;

		try {

			StoryCLMTableService<IStoryEntity> storyService = clientConnector
					.GetTableService(converterService.getStoryType(), converterService.getTableId());
			
			// Запрашиваем, все что есть в SF
			String query = MessageFormat.format("SELECT {0} FROM {1} ",
					String.join(", ",converterService.getSFQueryFields()), converterService.getSFTable());
			
			List<SObject> sObjects = getPartnerConnectionUtils().QueryAll(query);
			//ОБновляем в стори
			fromsftostory(sObjects, converterService, slog, storyService);

			//Все объекты стори
			List<IStoryEntity> storyObjects = storyService.FindAll(null, 50).GetResult();
			
			// Теперь необходимо определить чего нет в SF и удалить в стори
			List<String> sfids = sObjects.stream().map(sf->sf.getId()).collect(Collectors.toList());
			List<String> storyRemovingIds = storyObjects.stream().filter(s->!sfids.contains(s.getSalesForceId())).map(s->s.getStoryId()).collect(Collectors.toList());
			if (storyRemovingIds.size()>0) storyService.Delete(storyRemovingIds).GetResult();
			slog.Deleted = storyRemovingIds.size();
			
		} catch (Exception e) {
			// Попробуем отправить сообщение к нам в лог
			slog.Failed = true;
			slog.AddNote("Exception happens");
			slog.AddNote(Utils.JoinStackTrace(e));
		} finally {
			StoryCLMTableService<StoryLog> slogService = getStoryConnector().GetTableService(StoryLog.class,
					converterService.getLogTableId());
			slogService.Insert(slog).GetResult();
		}

	}

	
	/**
	 * По запросу query из SF полученные объекты апсертятся в STORY
	 * @param query
	 * @param converterService
	 * @param slog
	 * @param storyService
	 * @return
	 * @throws AuthFaliException
	 * @throws AsyncResultException
	 * @throws ConnectionException
	 */
	List<IStoryEntity> fromsftostory(
			String query, 
			PartnerTypeConverterService converterService,
			StoryLog slog, 
			StoryCLMTableService<IStoryEntity> storyService)
					throws AuthFaliException, AsyncResultException, ConnectionException 
	{
		// Получим записи из SF для сравнения с тем что есть в Story
		List<SObject> sObjects = getPartnerConnectionUtils().QueryAll(query);
		return fromsftostory(sObjects, converterService, slog, storyService);

	}

	List<IStoryEntity> fromsftostory(
			List<SObject> sObjects, 
			PartnerTypeConverterService converterService,
			StoryLog slog, 
			StoryCLMTableService<IStoryEntity> storyService)			
					throws AuthFaliException, AsyncResultException, ConnectionException {
		logger.info("SF objects size " + sObjects.size());

		// Смотрим в стори
		
		List<String> SFIdList = sObjects.stream().map(x->x.getId()).collect(Collectors.toList());
		Map<String,String> sf2storyIds = 
				StoryServiceUtils.GetExisting(converterService.getSFIdFieldName(), SFIdList, storyService)
				.stream()
				.collect(Collectors.toMap(x->x.getSalesForceId(),x->x.getStoryId()));
		//Обновляем story идентификаторы объектов из SF 
		List<IStoryEntity> storyObjects = sObjects.stream()
				.map(x->{
					IStoryEntity s = converterService.ConvertToStory(x);
					s.setStoryId(sf2storyIds.getOrDefault(x.getId(), null));
					return s;
				})
				.collect(Collectors.toList());
		
		
		/*List<IStoryEntity> tempStoryObjects = new ArrayList<IStoryEntity>();
		String SFIds = "";
		// В Story запрос (query) передается в строке http, поэтому приходится
		// его делить
		for (int i = 0; i < sObjects.size(); i++) {
			tempStoryObjects.add(converterService.ConvertToStory(sObjects.get(i)));
			SFIds += ",\"" + sObjects.get(i).getId() + "\"";
			if ((tempStoryObjects.size() % 20 == 0) || (i + 1 == sObjects.size())) {
				SFIds = SFIds.substring(1, SFIds.length());

				String storyQuery = MessageFormat.format("[{0}][in][{1}]", converterService.getSFIdFieldName(), SFIds);
				logger.info("storyQuery: " + storyQuery);
				List<IStoryEntity> entities = storyService.FindAllSync(storyQuery);
				logger.info("response size: " + entities.size());
				for (IStoryEntity story : entities)
					for (IStoryEntity sf : tempStoryObjects)
						if (sf.getSalesForceId().equals(story.getSalesForceId())) {
							sf.setStoryId(story.getStoryId());
							break;
						}
				storyObjects.addAll(tempStoryObjects);
				tempStoryObjects.clear();
				SFIds = "";
			}
		}*/

		upsertStoryEntities(storyObjects, storyService, slog);
		return storyObjects;

	}

	int removeDeleted(Date lastReplicationDate, Date thisReplicationDate, String sObjectType, String SFIdFieldName,
			StoryCLMTableService<IStoryEntity> storyService)
			throws ConnectionException, AuthFaliException, AsyncResultException {

		if (Utils.GetDateDiff(lastReplicationDate, thisReplicationDate, TimeUnit.DAYS) > 30) {
			lastReplicationDate = new Date((new Date()).getTime() - 29 * 24 * 3600 * 1000l);
			logger.warn("************* " + sObjectType
					+ " *************** дата последней репликации превышает 30 дней - возможна потеря информации об удаленных объектах!");
		}
		Calendar startCal = new GregorianCalendar();
		// lastReplicationDate = new Date(2017,4,01);
		startCal.setTime(lastReplicationDate);
		Calendar finisshCal = new GregorianCalendar();
		finisshCal.setTime(thisReplicationDate);
		GetDeletedResult queryResults = getConnection().getDeleted(sObjectType, startCal, finisshCal);

		DeletedRecord[] deletedRecordsSF = queryResults.getDeletedRecords();
		logger.info("Get Deleted size " + deletedRecordsSF.length);
		List<String> removingSfIds = Stream.of(deletedRecordsSF).map(x->x.getId()).collect(Collectors.toList());
		List<String> removingStoryIds = StoryServiceUtils.GetExisting(SFIdFieldName, removingSfIds, storyService).stream().map(x->x.getStoryId()).collect(Collectors.toList());
		
		if (removingStoryIds.size() > 0) {
			logger.info("will delete from story size: " + removingStoryIds.size());
			storyService.Delete(removingStoryIds).GetResult();
		}
		return removingStoryIds.size();
	}

	/**
	 * Все записи с идентификаторами обновляет, без них вставляет
	 */
	void upsertStoryEntities(List<IStoryEntity> objects, StoryCLMTableService<IStoryEntity> storyService, StoryLog slog)
			throws AsyncResultException, AuthFaliException {

		List<IStoryEntity> updated = new ArrayList<IStoryEntity>();
		List<IStoryEntity> inserted = new ArrayList<IStoryEntity>();

		for (IStoryEntity record : objects) {
			if (record.getStoryId() == null)
				inserted.add(record);
			else
				updated.add(record);
		}
		if (updated.size() != 0)
			storyService.UpdateMany(updated.toArray(new IStoryEntity[0])).GetResult();
		if (inserted.size() != 0)
			storyService.InsertMany(inserted.toArray(new IStoryEntity[0])).GetResult();

		slog.Inserted += inserted.size();
		slog.Updated += updated.size();
	}

}
