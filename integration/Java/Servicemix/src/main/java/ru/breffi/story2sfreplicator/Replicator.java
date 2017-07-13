package ru.breffi.story2sfreplicator;


import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.partner.Connector;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import ru.breffi.story2sf.services.IConverterService;
import ru.breffi.story2sf.services.IStoryEntity;
//import ru.breffi.story2sf.services.visit.Visit;
import ru.breffi.storyclmsdk.*;
import ru.breffi.storyclmsdk.Exceptions.*;
import ru.breffi.storyclmsdk.Models.ApiLog;


public class Replicator {
	  public SalesForceLoginConfig sfconfig;
	  public StoryLoginConfig storyConfig;
	  
	//  public int logTableId = 73;
	//  public int tableId = 64;
	  final Logger logger = LoggerFactory.getLogger(Replicator.class);
	 
	  public void setSfconfig(SalesForceLoginConfig sFconfig){
		  this.sfconfig = sFconfig;
	  }
	  public void setStoryConfig(StoryLoginConfig storyConfig){
		  this.storyConfig = storyConfig;
	  }
	  PartnerConnection connection;
	  PartnerConnection getConnection() throws ConnectionException{
		  if (connection == null) {
			  ConnectorConfig config = new ConnectorConfig();
			  config.setUsername(sfconfig.UserName);
			  config.setPassword(sfconfig.Password);
			  config.setAuthEndpoint(sfconfig.AuthEndpoint);
			  connection = Connector.newConnection(config);
		  }
		  return connection;
	  }
	  
	  StoryCLMServiceConnector clientConnector;
	  StoryCLMServiceConnector getStoryConnector(){
		  if (clientConnector==null){
			  clientConnector = StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector(storyConfig.ClientId, storyConfig.ClientSecret , null);
		  }
		  return clientConnector;
	  }
	  
	  String getIsoDate(Date date){
			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-00:00"); // Quoted "Z" to indicate UTC, no timezone offset
			df.setTimeZone(tz);
			String ISODate = df.format(date);
			return ISODate;
	  }
	  
	  
	  public void FullReplicate(IConverterService converterService) throws AuthFaliException, AsyncResultException, ConnectionException{
		 Date thisReplicationDate = new Date();
		 StoryLog slog = new StoryLog();
		 slog.Note = "Полная репликация из-за несовпадения по количеству элементов";
		 slog.Date = thisReplicationDate;
		 StoryCLMServiceGeneric<IStoryEntity> storyService = clientConnector.GetService(converterService.getStoryType(), converterService.getTableId());
		 
		 
		 List<IStoryEntity> storyEntities = storyService.FindAll(null, 50).GetResult();
		 List<String> ids = new ArrayList<String>();
		 for(IStoryEntity v:storyEntities){
			  ids.add(v.getStoryId());
		  }
		 
 		
		 	//-------------------------------------------------- Жесткая связность на бизнес логику синхронизации визитов
		  // 	setSFUserIDOnInserted(visits);
		   	//--------------------------------------------------------

		 
			List<SObject> upsertedSFEntities = new ArrayList<SObject>(); 
	      	for(IStoryEntity v:storyEntities){
	      	//	upsertedSFVisits.add(createSobject(v));
	      		upsertedSFEntities.add(converterService.ConvertToSF(v));
	      	}
	      	List<UpsertResult> updatedResults = upsertToSF(upsertedSFEntities, converterService.getStoryIdNameInSF());// getConnection().upsert("BF_Visits_StoryCLM_Id__c", upsertedSFVisits.toArray(new SObject[upsertedSFVisits.size()]));
	    	List<IStoryEntity> insertedStoryEntities = new ArrayList<IStoryEntity>();
	    	List<SObject> failedSFEntities = new ArrayList<SObject>();
	    	int updated = 0;
	      	for(int i=0;i<updatedResults.size();i++){
	      		IStoryEntity v = storyEntities.get(i);
	    		if (updatedResults.get(i).isSuccess()){
	    			if (updatedResults.get(i).getCreated())
	    			{
	    				v.setSalesForceId(updatedResults.get(i).getId());
	    				insertedStoryEntities.add(v);
	    			}
	    			else {
	    				updated++;
	    			}
	    		}
	    		else{
	    			failedSFEntities.add(upsertedSFEntities.get(i));
	    		}
		    		
	    	}
	      	
	      	 slog.Updated = updated;
	      	 
	      
	      	 //Попробуем вставить failedVisits, для этого очистим SFId
	      	 for(SObject fv:failedSFEntities){
	      		 fv.setId(null);
	      	 }
	      	updatedResults = upsertToSF(failedSFEntities, converterService.getStoryIdNameInSF());// getConnection().upsert("BF_Visits_StoryCLM_Id__c", failedVisits.toArray(new SObject[0]));
	      	 
	      	for(int i=0;i<updatedResults.size();i++){
	    		IStoryEntity v = storyEntities.get(i);
	    		if (updatedResults.get(i).isSuccess()){
	    				v.setSalesForceId(updatedResults.get(i).getId());
	    				insertedStoryEntities.add(v);
	    			}
	    			else {
	    				slog.Failed = true;
		    			slog.Note+="\r\nstoryId " + v.getStoryId() + "\r\n";
		    			for(com.sforce.soap.partner.Error er: updatedResults.get(i).getErrors())
		    				slog.Note+= er.getMessage()+"\r\n";
	    			}
		    		
	    		}
		      	 
	     	slog.Inserted = insertedStoryEntities.size();
	    	if (slog.Inserted>0)
	    			storyService.UpdateMany(insertedStoryEntities.toArray(new IStoryEntity[0])).GetResult();
	  	
	    	
		 	//Находим элементы которых нет в Story и в цикле по queryLocator удаляем их
	      	String query = 
	      			MessageFormat.format(
	      					"SELECT Id FROM " + converterService.getSFTable()+ " " + ((ids.size()>0)? " where " + converterService.getStoryIdNameInSF() + " not in ({0}) ":""),joinByComma(ids));
	  
	    		
	    	System.out.println("query" + query);
	    	logger.info("SOQL: " + query);
	    	QueryResult queryResults = getConnection().query(query);
	    	System.out.println("queryResults size " + queryResults.getSize());
	    	
	    	int deleted=0;
	    	while(true){
	    		logger.info("queryResults party size " + queryResults.getSize());
		    	SObject[] sObjects = queryResults.getRecords();
		    
		    	List<String> removedSFIds = new ArrayList<String>();
		    	for(SObject s:sObjects){
		    		removedSFIds.add(s.getId());
		    		deleted++;
		    	}
		    	deleteFromSF(removedSFIds);
		    	
		    	
		    	String queryLocator = queryResults.getQueryLocator();
		    	if (queryLocator == null || queryLocator.isEmpty()) break;
		    	
		    	queryResults = getConnection().queryMore(queryLocator);
		      
	    	}
	    	
	    	slog.Deleted = deleted;
	    	
	    	StoryCLMServiceGeneric<StoryLog> slogService = getStoryConnector().GetService(StoryLog.class, converterService.getLogTableId());
	    	slogService.Insert(slog).GetResult();
	  }
	  
	  
	  public void Replicate(IConverterService converterService) throws AsyncResultException, ConnectionException, AuthFaliException
	  {
		  logger.info("Start replicate for StoryType: " +  converterService.getStoryType().getSimpleName());
		  	Date thisReplicationDate = new Date();
			StoryLog slog = new StoryLog();
	    	slog.Date = thisReplicationDate;
	    	
	    	//Достаем запись из журнала
	    	StoryCLMServiceGeneric<StoryLog> slogService = getStoryConnector().GetService(StoryLog.class, converterService.getLogTableId());
	   
	    	Date lastReplicationDate = new Date(0);
	    	
	    	lastReplicationDate = slogService.CountByQuery("[Failed][eq][False]").GetResult()>0? slogService.Max("Date", "[Failed][eq][False]", Date.class).GetResult():new Date(0);
	      	logger.info("Last Replication Date " + lastReplicationDate);
	    	
	      	
	      	StoryCLMServiceGeneric<IStoryEntity> storyService = clientConnector.GetService(converterService.getStoryType(), converterService.getTableId());
	      	ApiLog[] logs = null; 
	      	List<String> ups_ids = new ArrayList<String>();
	      	List<String> ins_ids = new ArrayList<String>();
	      	List<String> remove_ids = new ArrayList<String>();
	      	int skip=0;
	      	while((logs = storyService.Log(lastReplicationDate, skip, 1000).GetResult()).length>0)
	      	{
	      		skip=logs.length;
	      		for(ApiLog l:logs){
	      			if (l.entityId!=null)
	      			switch (l.operationType) {
					
					case 0:
						ups_ids.add(l.entityId);
						ins_ids.add(l.entityId);
						break;
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

	    	int ii=0;
	    	
	    	//сделаем дистинкт по апдейту
	    	ups_ids = new ArrayList<String>(new HashSet<String>(ups_ids));
	    	
	    	
	    	
	      	List<IStoryEntity> upsertedStoryEntities = new ArrayList<IStoryEntity>();
	      	for(;ii+5<ups_ids.size();ii+=5){
	      		upsertedStoryEntities.addAll(storyService.Find(ups_ids.subList(ii, ii+5).toArray(new String[0])).GetResult());
	      	}
	      	upsertedStoryEntities.addAll(storyService.Find(ups_ids.subList(ii, ups_ids.size()).toArray(new String[0])).GetResult());
	      	

	      	List<SObject> upsertedSFEntities = new ArrayList<SObject>(); 
	      	
	    	
	    	//Вот тут мы должны найти пользователей в SF -------------------------- Жесткая связность -----------------------------
	    //	setSFUserIDOnInserted(upsertedVisits);
	      	//------------------------------------------------------------------ Конец жесткой связности ----------------------------

	      	
	      	
	      	
	      	for(IStoryEntity v:upsertedStoryEntities){
	      		upsertedSFEntities.add(converterService.ConvertToSF(v));
	      	}
	      		
	      	
	      	
	      	
	      	
	    	List<UpsertResult> updatedResults =  upsertToSF(upsertedSFEntities, converterService.getStoryIdNameInSF());//getConnection().upsert("BF_Visits_StoryCLM_Id__c", upsertedSFVisits.toArray(new SObject[upsertedSFVisits.size()]));
	    	
	    	//Именно вставленные визиты вычисляем и обновляем в Story
	    	List<IStoryEntity> insertedVisits = new ArrayList<IStoryEntity>();
	    	slog.Updated=0;
	    	
	    	for(int i=0;i<updatedResults.size();i++){
	    		IStoryEntity v = upsertedStoryEntities.get(i);
	    		if (!updatedResults.get(i).isSuccess()){
	    			slog.Failed = true;
	    			slog.Note+="\r\nstoryId " + v.getStoryId() + "\r\n";
	    			for(com.sforce.soap.partner.Error er: updatedResults.get(i).getErrors())
	    				slog.Note+= er.getMessage()+"\r\n";
	    		}
	    		else
	    			if(updatedResults.get(i).getCreated())
		    		{
		    			v.setSalesForceId(updatedResults.get(i).getId());
		    			insertedVisits.add(v);
		    		}
		    		else {
		    			slog.Updated++;
		    		}
		    		
	    	}
	    	
	    	
	    	
	    	
	    	
	    	// --------------------------------------------------------- ОБновляем в стори пользователей
	    	slog.Inserted = insertedVisits.size();
	    	if (slog.Inserted>0)
	    			storyService.UpdateMany(insertedVisits.toArray(new IStoryEntity[slog.Inserted])).GetResult();
	    	
	    	
	    	
	    	
	    	//------------------------------------------------------------ Ищем чего нет в SF  и удаляем -------------------------------
	    	if (remove_ids.size()>0){
		    	//Находим элементы для удаления и в цикле по queryLocator удаляем их
		      	//String query = MessageFormat.format("SELECT Id FROM BF_Visits__c where BF_Visits_StoryCLM_Id__c in ({0}) ",joinByComma(remove_ids));
		      	String query = MessageFormat.format("SELECT Id FROM "+ converterService.getSFTable()+ " where "+ converterService.getStoryIdNameInSF()+ " in ({0}) ",joinByComma(remove_ids));
		    	System.out.println("query" + query);
		    	logger.info("SOQL: " + query);
		    	QueryResult queryResults = getConnection().query(query);
		    	
		    	System.out.println("queryResults size " + queryResults.getSize());
		    	//int totalSize = queryResults.getSize();
		    	int deleted=0;
		    	while(true){
		    		logger.info("queryResults party size " + queryResults.getSize());
			    	SObject[] sObjects = queryResults.getRecords();
			    
			    	List<String> removedSFIds = new ArrayList<String>();
			    	for(SObject s:sObjects){
			    		removedSFIds.add(s.getId());
			    		deleted++;
			    	}
			    	deleteFromSF(removedSFIds);
			    	
			    	
			    	String queryLocator = queryResults.getQueryLocator();
			    	if (queryLocator == null || queryLocator.isEmpty()) break;
			    	
			    	queryResults = getConnection().queryMore(queryLocator);
			      
		    	}
	    		slog.Deleted = deleted;
	    	}
	    	
	    	if (!slog.Failed){
		    	//Проверяем хэши 2-х копий на соответствие  
		    	long storyCount  = storyService.Count().GetResult();
		    	//SObject sfcount =getConnection().query("Select Count(id) cnt from BF_Visits__c").getRecords()[0];
		    	SObject sfcount =getConnection().query("Select Count(id) cnt from "+ converterService.getSFTable()).getRecords()[0];
		    	if (storyCount!=(Integer)sfcount.getField("cnt")) {
		    		String message = converterService.getSFTable() +  ":No concurrent counts ins tables " + storyCount + " and SF: " + (Integer)sfcount.getField("cnt") ; 
		    		logger.warn(message);
		    		slog.Note+="\r\n" + message + "\r\n FullReplicate";
		    		slogService.Insert(slog).GetResult();
		    		
		    		FullReplicate(converterService);
		    		logger.info("Finish fullreplicate for StoryType: " + converterService.getStoryType().getSimpleName());
		    		return;
		    	}
	    	}
	    	
	    	
	    	
    		StoryLog slogLast  =  slogService.LastOrDefault(null, "Date", 1,null).GetResult();
	    	if (slogLast!=null && slogLast.equals(slog))
	    		{
	    		slog._id = slogLast._id;
	    		slog.Attempts = slogLast.Attempts+1;
	    		slogService.Update(slog).GetResult();
	    		}
	    	else slogService.Insert(slog).GetResult();
	    	
	    	
	    	
	    	logger.info("Finish replicate for StoryType: " + converterService.getStoryType().getSimpleName());
	    
	  	}
	  	  
	  String joinByComma(String[] strings){
		  if (strings.length==0) return "";
		  String res =  String.join("\', \'",strings);
		  return "\'"+res+"\'";
	  }
	  
	  String joinByComma(List<String> strings){
		  return joinByComma(strings.toArray(new String[0]));
	  }
	  
	  /**
	   * Get a diff between two dates
	   * @param date1 the oldest date
	   * @param date2 the newest date
	   * @param timeUnit the unit in which you want the diff
	   * @return the diff value, in the provided unit
	   */
	  public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	      long diffInMillies = date2.getTime() - date1.getTime();
	      return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	  }
	  
	/*  void setSFUserIDOnInserted(List<IStoryEntity> insertedVisits) throws ConnectionException{
		  if (insertedVisits.size()==0) return;
			HashSet<String> emails  =  new HashSet<String>();
	      	for(IStoryEntity v:insertedVisits){
	      		emails.add(v.Username);
	      	}
	      	String bfuserSearchQuery = MessageFormat.format("SELECT Id,UB_Email__c FROM Users_BREFFI__c where UB_Email__c in ({0}) ",joinByComma(emails.toArray(new String[0])));
	      	QueryResult bfqueryResults = getConnection().query(bfuserSearchQuery);
	    	while(true){
	    		logger.info("bfuserSearchQuery: " + bfuserSearchQuery);
	    		logger.info("bfqueryResults party size " + bfqueryResults.getSize());
		    	SObject[] sObjects = bfqueryResults.getRecords();
		    	
		    	for(SObject s:sObjects){
		    		String email = (String) s.getField("UB_Email__c");
		    		insertedVisits.stream()
		    			.filter(x->x.Username.equals(email))
		    			.forEachOrdered(x->x.SFUserId =s.getId());
		    		
		    	}
		    	
		    	String queryLocator = bfqueryResults.getQueryLocator();
		    	if (queryLocator == null || queryLocator.isEmpty()) break;
		    	
		    	bfqueryResults = getConnection().queryMore(queryLocator);
		      
	    	}
	  }*/
	

	  
	  List<UpsertResult> upsertToSF(List<SObject> sfEntities, String externalIdFieldName) throws ConnectionException{
		  List<UpsertResult> result = new ArrayList<UpsertResult>();
		 for(int i=0;i<sfEntities.size();i+=200){
			 List<SObject> currentVisit = sfEntities.subList(i, Math.min(i+200, sfEntities.size()));
			 //result.addAll(Arrays.asList(getConnection().upsert("BF_Visits_StoryCLM_Id__c", currentVisit.toArray(new SObject[currentVisit.size()]))));
			 result.addAll(Arrays.asList(getConnection().upsert(externalIdFieldName, currentVisit.toArray(new SObject[currentVisit.size()]))));
		 }
		 return result;
		//return getConnection().upsert("BF_Visits_StoryCLM_Id__c", visits.toArray(new SObject[visits.size()]));
	}
	  
	 
	 void deleteFromSF(List<String> removingSFId) throws ConnectionException{
			 for(int i=0;i<removingSFId.size();i+=200){
				 List<String> removingPart = removingSFId.subList(i, Math.min(i+200, removingSFId.size()));
					 getConnection().delete(removingPart.toArray(new String[removingPart.size()]));
			 }
	 }
	  
	/*  
	  SObject createSobject(Visit visit){
		  SObject s = new SObject();
		  s.setType("BF_Visits__c");
		  s.setId(visit.SFId);
		  s.addField("BF_Visits_Checkin__c", visit.CheckIn);
		  s.addField("BF_Visits_Confirmed__c", visit.Confirmed);
		  s.addField("BF_Visits_Offset__c", visit.Offset);
		  s.addField("BF_Visits_PresentationId__c", visit.PresentationId);
		  s.addField("BF_Visits_SessionId__c", visit.SessionId);
		  
		  s.addField("BF_Visits_Slides__c", visit.Slides);
		  s.addField("BF_Visits_UserId__c", visit.UserId);
		  s.addField("BF_Visits_Address__c", visit.Address);
		  
		  s.addField("BF_Visits_Start__c", dateToCalendar(visit.Start));
		  s.addField("BF_Visits_End__c", dateToCalendar(visit.End));
		  s.addField("BF_Visits_Length__c", visit.Length);
		  s.addField("BF_Visits_Longitude__c", visit.Longitude);
		  s.addField("BF_Visits_Latitude__c", visit.Latitude);
		  
		  s.addField("BF_Visits_SFEmployeeId__c", visit.SFEmployeeId);
		  s.addField("BF_Visits_SFUser__c", visit.SFUserId);
		  s.addField("BF_Visits_Territory__c", visit.SFTerritoryId);
		  s.addField("BF_Visits_Company__c", visit.SFCompanyId);
		  s.addField("BF_Visits_Username__c", visit.Username);
		  s.addField("BF_Visits_StoryCLM_Id__c", visit._id);
		  return s;
	  }*/
}
 