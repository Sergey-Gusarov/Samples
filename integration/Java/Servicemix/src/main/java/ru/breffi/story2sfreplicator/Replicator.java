package ru.breffi.story2sfreplicator;


import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.partner.Connector;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import ru.breffi.storyclmsdk.*;
import ru.breffi.storyclmsdk.Exceptions.*;
import ru.breffi.storyclmsdk.Models.ApiLog;


public class Replicator {
	  public SalesForceLoginConfig sfconfig;
	  public StoryLoginConfig storyConfig;
	  
	  public int logTableId = 73;
	  public int tableId = 64;
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
	  
	  
	  public void FullReplicate() throws AuthFaliException, AsyncResultException, ConnectionException{
		 Date thisReplicationDate = new Date();
		 StoryLog slog = new StoryLog();
		 slog.Note = "Полная репликация из-за несовпадения по количеству элементов";
		 slog.Date = thisReplicationDate;
		 StoryCLMServiceGeneric<Visit> storyService = clientConnector.GetService(Visit.class, tableId);
		 
		 
		 List<Visit> visits = storyService.FindAll(null, 50).GetResult();
		 List<String> ids = new ArrayList<String>();
		 for(Visit v:visits){
			  ids.add(v._id);
		  }
			List<SObject> upsertedSFVisits = new ArrayList<SObject>(); 
	      	for(Visit v:visits){
	      		upsertedSFVisits.add(createSobject(v));
	      	}
	      	UpsertResult[] updatedResults = getConnection().upsert("BF_Visits_StoryCLM_Id__c", upsertedSFVisits.toArray(new SObject[upsertedSFVisits.size()]));
	    	List<Visit> insertedVisits = new ArrayList<Visit>();
	    	List<SObject> failedVisits = new ArrayList<SObject>();
	    	int updated = 0;
	      	for(int i=0;i<updatedResults.length;i++){
	    		Visit v = visits.get(i);
	    		if (updatedResults[i].isSuccess()){
	    			if (updatedResults[i].getCreated())
	    			{
	    				v.SFId = updatedResults[i].getId();
	    				insertedVisits.add(v);
	    			}
	    			else {
	    				updated++;
	    			}
	    		}
	    		else{
	    			failedVisits.add(upsertedSFVisits.get(i));
	    		}
		    		
	    	}
	      	
	      	 slog.Updated = updated;
	      	 
	      
	      	 //Попробуем вставить failedVisits, для этого очистим SFId
	      	 for(SObject fv:failedVisits){
	      		 fv.setId(null);
	      	 }
	      	updatedResults = getConnection().upsert("BF_Visits_StoryCLM_Id__c", failedVisits.toArray(new SObject[0]));
	      	 
	      	for(int i=0;i<updatedResults.length;i++){
	    		Visit v = visits.get(i);
	    		if (updatedResults[i].isSuccess()){
	    				v.SFId = updatedResults[i].getId();
	    				insertedVisits.add(v);
	    			}
	    			else {
	    				slog.Failed = true;
		    			slog.Note+="\r\nstoryId " + v._id + "\r\n";
		    			for(com.sforce.soap.partner.Error er: updatedResults[i].getErrors())
		    				slog.Note+= er.getMessage()+"\r\n";
	    			}
		    		
	    		}
		    		
	    	
	      	 
	     	slog.Inserted = insertedVisits.size();
	    	if (slog.Inserted>0)
	    			storyService.UpdateMany(insertedVisits.toArray(new Visit[slog.Inserted])).GetResult();
	  	
	    	
		 	//Находим элементы которых нет в Story и в цикле по queryLocator удаляем их
	      	String query = MessageFormat.format("SELECT Id FROM BF_Visits__c" + ((ids.size()>0)? " where BF_Visits_StoryCLM_Id__c not in ({0}) ":""),joinByComma(ids));
	  
	    		
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
		    	getConnection().delete(removedSFIds.toArray(new String[removedSFIds.size()]));
		    	
		    	
		    	String queryLocator = queryResults.getQueryLocator();
		    	if (queryLocator == null || queryLocator.isEmpty()) break;
		    	
		    	queryResults = getConnection().queryMore(queryLocator);
		      
	    	}
	    	
	    	slog.Deleted = deleted;
	    	
	    	StoryCLMServiceGeneric<StoryLog> slogService = getStoryConnector().GetService(StoryLog.class, logTableId);
	    	slogService.Insert(slog).GetResult();
	  }
	  
	  
	  public void Replicate() throws AsyncResultException, ConnectionException, AuthFaliException
	  {
		  logger.info("Start replicate for StoryType: " + Visit.class.getSimpleName());
		  	Date thisReplicationDate = new Date();
			StoryLog slog = new StoryLog();
	    	slog.Date = thisReplicationDate;
	    	
	    	//Достаем запись из журнала
	    	StoryCLMServiceGeneric<StoryLog> slogService = getStoryConnector().GetService(StoryLog.class, logTableId);
	   
	    	Date lastReplicationDate = new Date(0);
	    	
	    	lastReplicationDate = slogService.Count().GetResult()>0? slogService.Max("Date", null, Date.class).GetResult():new Date(0);
	      	logger.info("Last Replication Date " + lastReplicationDate);
	    	
	      	
	      	StoryCLMServiceGeneric<Visit> storyService = clientConnector.GetService(Visit.class, tableId);
	      	ApiLog[] logs = null; 
	      	List<String> ups_ids = new ArrayList<String>();
	      	List<String> ins_ids = new ArrayList<String>();
	      	List<String> re_ids = new ArrayList<String>();
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
					
						re_ids.add(l.entityId);
						break;
					default:
						break;
					}
	      		}
	      	}

	    	int ii=0;
	    	
	    	//сделаем дистинкт по апдейту
	    	ups_ids = new ArrayList<String>(new HashSet<String>(ups_ids));
	    	
	    	
	    	
	      	List<Visit> upsertedVisits = new ArrayList<Visit>();
	      	for(;ii+5<ups_ids.size();ii+=5){
	      		upsertedVisits.addAll(storyService.Find(ups_ids.subList(ii, ii+5).toArray(new String[0])).GetResult());
	      	}
	      	upsertedVisits.addAll(storyService.Find(ups_ids.subList(ii, ups_ids.size()).toArray(new String[0])).GetResult());
	      	

	      	List<SObject> upsertedSFVisits = new ArrayList<SObject>(); 
	      	for(Visit v:upsertedVisits){
	      		upsertedSFVisits.add(createSobject(v));
	      	}
	      	
	      	/******************/
	    	String query1 = "SELECT Id, BF_Visits_SFEmployeeId__c FROM BF_Visits__c";
	    	System.out.println("query" + query1);
	    	logger.info("SOQL: " + query1);
	    	QueryResult queryResults1 = getConnection().query(query1);
	      	/************************/
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	    	UpsertResult[] updatedResults = getConnection().upsert("BF_Visits_StoryCLM_Id__c", upsertedSFVisits.toArray(new SObject[upsertedSFVisits.size()]));
	    	
	    	//Именно вставленные визиты вычисляем и обновляем в Story
	    	List<Visit> insertedVisits = new ArrayList<Visit>();
	    	slog.Updated=0;
	    	for(int i=0;i<updatedResults.length;i++){
	    		Visit v = upsertedVisits.get(i);
	    		if (!updatedResults[i].isSuccess()){
	    			slog.Failed = true;
	    			slog.Note+="\r\nstoryId " + v._id + "\r\n";
	    			for(com.sforce.soap.partner.Error er: updatedResults[i].getErrors())
	    				slog.Note+= er.getMessage()+"\r\n";
	    		}
	    		else
	    			if(updatedResults[i].getCreated())
		    		{
		    			v.SFId = updatedResults[i].getId();
		    			insertedVisits.add(v);
		    		}
		    		else {
		    			slog.Updated++;
		    		}
		    		
	    	}
	    	slog.Inserted = insertedVisits.size();
	    	if (slog.Inserted>0)
	    			storyService.UpdateMany(insertedVisits.toArray(new Visit[slog.Inserted])).GetResult();
	    	
	    	if (re_ids.size()>0){
		    	//Находим элементы для удаления и в цикле по queryLocator удаляем их
		      	String query = MessageFormat.format("SELECT Id FROM BF_Visits__c where BF_Visits_StoryCLM_Id__c in ({0}) ",joinByComma(re_ids));
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
			    	getConnection().delete(removedSFIds.toArray(new String[removedSFIds.size()]));
			    	
			    	
			    	String queryLocator = queryResults.getQueryLocator();
			    	if (queryLocator == null || queryLocator.isEmpty()) break;
			    	
			    	queryResults = getConnection().queryMore(queryLocator);
			      
		    	}
	    		slog.Deleted = deleted;
	    	}
	    	
	    	//Проверяем хэши 2-х копий на соответствие  
	    	long storyCount  = storyService.Count().GetResult();
	    	SObject sfcount =getConnection().query("Select Count(id) cnt from BF_Visits__c").getRecords()[0];
	    	if (storyCount!=(Integer)sfcount.getField("cnt")) {
	    		String message ="Visits:No concurrent counts ins tables " + storyCount + " and SF: " + (Integer)sfcount.getField("cnt") ; 
	    		logger.warn(message);
	    		slog.Note+="\r\n" + message + "\r\n FullReplicate";
	    		slogService.Insert(slog).GetResult();
	    		
	    		FullReplicate();
	    		
	    	}
	    	else{
	    		StoryLog slogLast  =  slogService.LastOrDefault(null, "Date", 1,null).GetResult();
		    	if (slogLast!=null && slogLast.equals(slog))
		    		{
		    		slog._id = slogLast._id;
		    		slog.Attempts = slogLast.Attempts+1;
		    		slogService.Update(slog).GetResult();
		    		}
		    	else slogService.Insert(slog).GetResult();
	    	}
	    	
	    	
	    	logger.info("Finish replicate for StoryType: Visits");
	    
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
	  
	boolean checkHash(){
		return false;
	}
	  
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
		  
		  s.addField("BF_Visits_Start__c", visit.Start);
		  s.addField("BF_Visits_End__c", visit.End);
		  s.addField("BF_Visits_Length__c", visit.Length);
		  s.addField("BF_Visits_Longitude__c", visit.Longitude);
		  s.addField("BF_Visits_Latitude__c", visit.Latitude);
		  
		  s.addField("BF_Visits_SFEmployeeId__c", visit.SFEmployeeId);
		  s.addField("BF_Visits_SFUserId__c", visit.SFUserId);
		  s.addField("BF_Visits_Territory__c", visit.SFTerritoryId);
		  s.addField("BF_Visits_Company__c", visit.SFCompanyId);
		  s.addField("BF_Visits_Username__c", visit.Username);
		  s.addField("BF_Visits_StoryCLM_Id__c", visit._id);
		  return s;
	  }
}
