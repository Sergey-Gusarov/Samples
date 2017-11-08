package ru.breffi.story2sfreplicator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import ru.breffi.story2sf.services.IConverterService;
import ru.breffi.story2sf.services.IStoryEntity;
import ru.breffi.story2sf.services.visit.VisitConverterService;
//import ru.breffi.story2sf.services.visit.Visit;
import ru.breffi.storyclmsdk.*;
import ru.breffi.storyclmsdk.Exceptions.*;
import ru.breffi.storyclmsdk.Models.ApiLog;
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

	PartnerConnectionUtils _partnerConnectionUtils;

	PartnerConnectionUtils getPartnerConnectionUtils() throws ConnectionException {
		if (_partnerConnectionUtils == null) {
			ConnectorConfig config = new ConnectorConfig();
			config.setUsername(sfconfig.UserName);
			config.setPassword(sfconfig.Password);
			config.setAuthEndpoint(sfconfig.AuthEndpoint);
			_partnerConnectionUtils = new PartnerConnectionUtils(Connector.newConnection(config));
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

	/**
	 * 1. ВСе объекты стори обновляем в SF 
	 * 2. Далее все объекты SF провеярем на наличие в story, чего нет удаляем в SF
	 * 
	 * @param converterService
	 * @throws AuthFaliException
	 * @throws AsyncResultException
	 * @throws ConnectionException
	 */
	public void FullReplicate(IConverterService converterService)
			throws AuthFaliException, AsyncResultException, ConnectionException {
		Date thisReplicationDate = new Date();
		StoryLog slog = new StoryLog();
		slog.AddNote("Полная репликация из-за несовпадения по количеству элементов");
		slog.Date = thisReplicationDate;

		try {

			StoryCLMTableService<IStoryEntity> storyService = clientConnector
					.GetTableService(converterService.getStoryType(), converterService.getTableId());

			// 1. Берем все объекты стори и обновляем в SF
			List<IStoryEntity> storyEntities = storyService.FindAll(null, 50).GetResult();

			// Конвертация в SObject
			List<SObject> upsertedSFEntities = storyEntities.stream().map(x -> converterService.ConvertToSF(x))
					.collect(Collectors.toList());

			// При этом обновляются объекты с присутствующими идентификаторами
			// SalesForce
			// а вставляются с отсутствующими.
			List<UpsertResult> updatedResults = getPartnerConnectionUtils().UpsertToSF(upsertedSFEntities,
					converterService.getStoryIdNameInSF());

			// Извлекаем идентификаторы для вставленных записей и составляем
			// список ошибок
			// При сопоставлении идентификаторов расчет (надежда) идет на то что
			// список возвращенных записей соотвествует списку отправленных в
			// методе UpsertToSF
			// других вариантов (кроме как запрашивать отдельным запросом) для
			// сопоставления нет.
			List<IStoryEntity> insertedStoryEntities = new ArrayList<IStoryEntity>();
			List<SObject> failedSFEntities = new ArrayList<SObject>();
			int updated = 0;
			for (int i = 0; i < updatedResults.size(); i++) {
				IStoryEntity v = storyEntities.get(i);
				if (updatedResults.get(i).isSuccess()) {
					if (updatedResults.get(i).getCreated()) {
						v.setSalesForceId(updatedResults.get(i).getId());
						insertedStoryEntities.add(v);
					} else {
						updated++;
					}
				} else {
					failedSFEntities.add(upsertedSFEntities.get(i));
				}

			}

			slog.Updated = updated;

			// Попробуем вставить failedVisits, для этого очистим SFId
			for (SObject fv : failedSFEntities) {
				fv.setId(null);
			}
			updatedResults = getPartnerConnectionUtils().UpsertToSF(failedSFEntities,
					converterService.getStoryIdNameInSF());
			// getConnection().upsert("BF_Visits_StoryCLM_Id__c",
			// failedVisits.toArray(new SObject[0]));

			for (int i = 0; i < updatedResults.size(); i++) {
				IStoryEntity v = storyEntities.get(i);
				if (updatedResults.get(i).isSuccess()) {
					v.setSalesForceId(updatedResults.get(i).getId());
					insertedStoryEntities.add(v);
				} else {
					slog.Failed = true;
					slog.Note += "\r\nstoryId " + v.getStoryId() + "\r\n";
					for (com.sforce.soap.partner.Error er : updatedResults.get(i).getErrors())
						slog.Note += er.getMessage() + "\r\n";
				}

			}

			slog.Inserted = insertedStoryEntities.size();
			if (slog.Inserted > 0)
				storyService.UpdateMany(insertedStoryEntities.toArray(new IStoryEntity[0])).GetResult();

			// --------------------------------Проверяем что есть лишнего в SF
			// -----------------------------------
			// Выберем все элементы из SF, чтобы проверить наличие каждого в
			// Story

			String sfQueryAll = MessageFormat.format("SELECT Id, {0} FROM {1}", converterService.getStoryIdNameInSF(),
					converterService.getSFTable());

			List<SObject> sobjects = getPartnerConnectionUtils().QueryAll(sfQueryAll);
			int deleted = 0;

			for (int i = 0; i < sobjects.size(); i += 5) {
				List<SObject> sobjRange = sobjects.subList(i, Math.min(i + 5, sobjects.size()));
				String[] storyIdsRange = sobjRange.stream()
						.map(x -> (String) x.getField(converterService.getStoryIdNameInSF())).toArray(String[]::new);
				List<String> storyRange = storyService.Find(storyIdsRange).GetResult().stream().map(x -> x.getStoryId())
						.collect(Collectors.toList());
				List<String> sfdeletingIds = sobjRange.stream()
						.filter(sf -> !storyRange.contains(sf.getField(converterService.getStoryIdNameInSF())))
						.map(x -> x.getId()).collect(Collectors.toList());
				getPartnerConnectionUtils().DeleteFromSF(sfdeletingIds);
				deleted += sfdeletingIds.size();
			}
			;

			slog.Deleted = deleted;

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
	public void Replicate() throws AuthFaliException, AsyncResultException, ConnectionException{
		Replicate(new VisitConverterService(64, 73));
	}

	public void Replicate(IConverterService converterService)
			throws AsyncResultException, ConnectionException, AuthFaliException {
		// Сразу пробуем получяем сервис табличного лога, чтобы туда все ошибки
		// срать
		StoryCLMTableService<StoryLog> slogService = getStoryConnector().GetTableService(StoryLog.class,
				converterService.getLogTableId());
		Date thisReplicationDate = new Date();
		StoryLog slog = new StoryLog();
		slog.Date = thisReplicationDate;

		Boolean fullReplicateNeed = false;

		try {

			logger.info("Start replicate for StoryType: " + converterService.getStoryType().getSimpleName());

			// Достаем запись из журнала
			Date lastReplicationDate = new Date(0);
			lastReplicationDate = slogService.CountByQuery("[Failed][eq][False]").GetResult() > 0
					? slogService.Max("Date", "[Failed][eq][False]", Date.class).GetResult() : new Date(0);
			logger.info("Last Replication Date " + lastReplicationDate);

			StoryCLMTableService<IStoryEntity> storyService = clientConnector
					.GetTableService(converterService.getStoryType(), converterService.getTableId());

			// Количество записей для последующего сравнения
			long storyCount = storyService.Count().GetResult();

			// Анализ логов таблиц стори
			ApiLog[] logs = null;
			List<String> ups_ids = new ArrayList<String>();
			List<String> remove_ids = new ArrayList<String>();
			int skip = 0;
			while ((logs = storyService.Log(lastReplicationDate, skip, 1000).GetResult()).length > 0) {
				skip += logs.length;
				for (ApiLog l : logs) {
					if (l.entityId != null)
						switch (l.operationType) {

						case 0:
						case 1:
							ups_ids.add(l.entityId);
							break;
						case 2:
							remove_ids.add(l.entityId);
							break;
						default:
							break;
						}
				}
			}

			// дистинкт по апceрту
			ups_ids = new ArrayList<String>(new HashSet<String>(ups_ids));

			// то что удалено апдейтить не нужно
			ups_ids = ups_ids.stream().filter(x -> !remove_ids.contains(x)).collect(Collectors.toList());

			// Вытащим объекты из стори по id целиком
			List<IStoryEntity> upsertedStoryEntities = new ArrayList<IStoryEntity>();
			for (int ii = 0; ii < ups_ids.size(); ii += 5) {
				upsertedStoryEntities.addAll(
						storyService.Find(ups_ids.subList(ii, Math.min(ii + 5, ups_ids.size())).toArray(new String[0]))
								.GetResult());
			}

			// Конвертация в список SObject
			List<SObject> upsertedSFEntities = upsertedStoryEntities.stream().map(x -> converterService.ConvertToSF(x))
					.collect(Collectors.toList());

			// Апсерт в SF
			List<UpsertResult> updatedResults = getPartnerConnectionUtils().UpsertToSF(upsertedSFEntities,
					converterService.getStoryIdNameInSF());

			// Именно вставленные визиты вычисляем и обновляем в Story
			List<IStoryEntity> insertedVisits = new ArrayList<IStoryEntity>();
			slog.Updated = 0;

			for (int i = 0; i < updatedResults.size(); i++) {
				IStoryEntity v = upsertedStoryEntities.get(i);
				if (!updatedResults.get(i).isSuccess()) {
					slog.Failed = true;
					slog.Note += "\r\nstoryId " + v.getStoryId() + "\r\n";
					for (com.sforce.soap.partner.Error er : updatedResults.get(i).getErrors())
						slog.Note += er.getMessage() + "\r\n";
				} else if (updatedResults.get(i).getCreated()) {
					v.setSalesForceId(updatedResults.get(i).getId());
					insertedVisits.add(v);
				} else {
					slog.Updated++;
				}

			}

			// ---------------------------------------------------------
			// ОБновляем в стори пользователей
			slog.Inserted = insertedVisits.size();
			if (slog.Inserted > 0)
				storyService.UpdateMany(insertedVisits.toArray(new IStoryEntity[slog.Inserted])).GetResult();

			// ------------------------------------------------------------ Ищем
			// чего нет в SF и удаляем -------------------------------
			// порционально обрабатываем список удаляемых идентификаторов
			Utils.ListConsumePiecemeal(remove_ids, 10, removeIdsPortion -> {
				String query = MessageFormat.format(
						"SELECT Id FROM {0} where " + converterService.getStoryIdNameInSF() + " in ({0}) ",
						converterService.getSFTable(), Utils.JoinByComma(removeIdsPortion));
				logger.info("SOQL: " + query);
				getPartnerConnectionUtils().QueryPartList(query, resultPortion -> {
					getPartnerConnectionUtils().DeleteFromSF(resultPortion.stream().map(so -> {
						return so.getId();
					}));
				});
			});

			slog.Deleted = remove_ids.size();

			// Проверим соответствие двух систем по количеству записей
			Integer sfcount = (Integer) getPartnerConnectionUtils()
					.QueryAll("Select Count(id) cnt from " + converterService.getSFTable()).get(0).getField("cnt");
			fullReplicateNeed = storyCount != sfcount;
			if (fullReplicateNeed) {
				slog.AddNote(":Не совпадает количество записей в таблице " + storyCount + " и SF: " + sfcount);
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

}
