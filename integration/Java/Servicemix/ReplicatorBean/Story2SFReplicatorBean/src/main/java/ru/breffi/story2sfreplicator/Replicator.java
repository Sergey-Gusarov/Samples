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
	 * 1. ��� ������� ����� ��������� � SF 
	 * 2. ����� ��� ������� SF ��������� �� ������� � story, ���� ��� ������� � SF
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
		slog.AddNote("������ ���������� ��-�� ������������ �� ���������� ���������");
		slog.Date = thisReplicationDate;

		try {

			StoryCLMTableService<IStoryEntity> storyService = clientConnector
					.GetTableService(converterService.getStoryType(), converterService.getTableId());

			// 1. ����� ��� ������� ����� � ��������� � SF
			List<IStoryEntity> storyEntities = storyService.FindAll(null, 50).GetResult();

			// ����������� � SObject
			List<SObject> upsertedSFEntities = storyEntities.stream().map(x -> converterService.ConvertToSF(x))
					.collect(Collectors.toList());

			// ��� ���� ����������� ������� � ��������������� ����������������
			// SalesForce
			// � ����������� � ��������������.
			List<UpsertResult> updatedResults = getPartnerConnectionUtils().UpsertToSF(upsertedSFEntities,
					converterService.getStoryIdNameInSF());

			// ��������� �������������� ��� ����������� ������� � ����������
			// ������ ������
			// ��� ������������� ��������������� ������ (�������) ���� �� �� ���
			// ������ ������������ ������� ������������ ������ ������������ �
			// ������ UpsertToSF
			// ������ ��������� (����� ��� ����������� ��������� ��������) ���
			// ������������� ���.
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

			// ��������� �������� failedVisits, ��� ����� ������� SFId
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

			// --------------------------------��������� ��� ���� ������� � SF
			// -----------------------------------
			// ������� ��� �������� �� SF, ����� ��������� ������� ������� �
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
			// ��������� ��������� ��������� � ��� � ���
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
		// ����� ������� �������� ������ ���������� ����, ����� ���� ��� ������
		// �����
		StoryCLMTableService<StoryLog> slogService = getStoryConnector().GetTableService(StoryLog.class,
				converterService.getLogTableId());
		Date thisReplicationDate = new Date();
		StoryLog slog = new StoryLog();
		slog.Date = thisReplicationDate;

		Boolean fullReplicateNeed = false;

		try {

			logger.info("Start replicate for StoryType: " + converterService.getStoryType().getSimpleName());

			// ������� ������ �� �������
			Date lastReplicationDate = new Date(0);
			lastReplicationDate = slogService.CountByQuery("[Failed][eq][False]").GetResult() > 0
					? slogService.Max("Date", "[Failed][eq][False]", Date.class).GetResult() : new Date(0);
			logger.info("Last Replication Date " + lastReplicationDate);

			StoryCLMTableService<IStoryEntity> storyService = clientConnector
					.GetTableService(converterService.getStoryType(), converterService.getTableId());

			// ���������� ������� ��� ������������ ���������
			long storyCount = storyService.Count().GetResult();

			// ������ ����� ������ �����
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

			// �������� �� ��ce���
			ups_ids = new ArrayList<String>(new HashSet<String>(ups_ids));

			// �� ��� ������� ��������� �� �����
			ups_ids = ups_ids.stream().filter(x -> !remove_ids.contains(x)).collect(Collectors.toList());

			// ������� ������� �� ����� �� id �������
			List<IStoryEntity> upsertedStoryEntities = new ArrayList<IStoryEntity>();
			for (int ii = 0; ii < ups_ids.size(); ii += 5) {
				upsertedStoryEntities.addAll(
						storyService.Find(ups_ids.subList(ii, Math.min(ii + 5, ups_ids.size())).toArray(new String[0]))
								.GetResult());
			}

			// ����������� � ������ SObject
			List<SObject> upsertedSFEntities = upsertedStoryEntities.stream().map(x -> converterService.ConvertToSF(x))
					.collect(Collectors.toList());

			// ������ � SF
			List<UpsertResult> updatedResults = getPartnerConnectionUtils().UpsertToSF(upsertedSFEntities,
					converterService.getStoryIdNameInSF());

			// ������ ����������� ������ ��������� � ��������� � Story
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
			// ��������� � ����� �������������
			slog.Inserted = insertedVisits.size();
			if (slog.Inserted > 0)
				storyService.UpdateMany(insertedVisits.toArray(new IStoryEntity[slog.Inserted])).GetResult();

			// ------------------------------------------------------------ ����
			// ���� ��� � SF � ������� -------------------------------
			// ������������ ������������ ������ ��������� ���������������
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

			// �������� ������������ ���� ������ �� ���������� �������
			Integer sfcount = (Integer) getPartnerConnectionUtils()
					.QueryAll("Select Count(id) cnt from " + converterService.getSFTable()).get(0).getField("cnt");
			fullReplicateNeed = storyCount != sfcount;
			if (fullReplicateNeed) {
				slog.AddNote(":�� ��������� ���������� ������� � ������� " + storyCount + " � SF: " + sfcount);
				slog.AddNote("��������� ������ ����������.");
			}
		}
		// ������������� ��� ���������� � �������� ��������� ��������� � ���
		catch (Exception e) {
			// ��������� ��������� ��������� � ��� � ���
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
