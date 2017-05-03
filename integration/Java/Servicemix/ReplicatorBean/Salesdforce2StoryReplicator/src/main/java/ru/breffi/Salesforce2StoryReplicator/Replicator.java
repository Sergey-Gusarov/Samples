package ru.breffi.Salesforce2StoryReplicator;


import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.DeletedRecord;
import com.sforce.soap.partner.GetDeletedResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import  ru.breffi.EntityTypeConverterServicePackage.PartnerTypeConverterService;
import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMServiceConnector;
import ru.breffi.storyclmsdk.StoryCLMServiceGeneric;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
public class Replicator {
	  public SalesForceLoginConfig sfconfig;
	  public StoryLoginConfig storyConfig;
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
			  config.setAuthEndpoint("https://login.salesforce.com/services/Soap/u/29.0");
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
	  
	  public void Replicate(PartnerTypeConverterService converterService) throws AsyncResultException, ConnectionException, AuthFaliException
	  {
		  logger.info("Start replicate for StoryType: " + converterService.getStoryType().getSimpleName());
		  	Date thisReplicationDate = new Date();
			StoryLog slog = new StoryLog();
	    	slog.Date = thisReplicationDate;
	    	
	    	//Достаем запись из журнала
	    	StoryCLMServiceGeneric<StoryLog> slogService = getStoryConnector().GetService(StoryLog.class, converterService.getLogTableId());
	    	int count = slogService.Count().GetResult();
	    	Date lastReplicationDate = new Date(0);
	    	if (count!=0) lastReplicationDate = slogService.Find(count-1,1).GetResult().get(0).Date;
	    	System.out.println("lastReplicationDate!" + lastReplicationDate);
	    	logger.info("Last Replication Date " + lastReplicationDate);
	    	//String query = MessageFormat.format("SELECT {0} FROM {1} where LastModifiedDate > {2} and LastModifiedDate <= {3} ",
	    	String query = MessageFormat.format("SELECT {0} FROM {1} where LastModifiedDate > {2} and LastModifiedDate <= {3} ",		
	    			joinByComma(converterService.getSFQueryFields()), converterService.getSFTable(), getIsoDate(lastReplicationDate),getIsoDate(thisReplicationDate));
	    	System.out.println("query" + query);
	    	logger.info("SOQL: " + query);
	    	QueryResult queryResults = getConnection().query(query);
	    	
	    	System.out.println("queryResults size " + queryResults.getSize());
	    	//int totalSize = queryResults.getSize();
	    	StoryCLMServiceGeneric<IStoryEntity> storyService = clientConnector.GetService(converterService.getStoryType(), converterService.getTableId());
	    	while(true){
	    		logger.info("queryResults party size " + queryResults.getSize());
		    	SObject[] sObjects = queryResults.getRecords();
		    	List<IStoryEntity> tempStoryObjects = new ArrayList<IStoryEntity>();
		    	List<IStoryEntity> storyObjects = new ArrayList<IStoryEntity>();
		    	String SFIds=""; 
		    	for(int i=0;i<sObjects.length;i++){
		    		tempStoryObjects.add(converterService.ConvertToStory(sObjects[i]));
		    		SFIds+= ",\"" + sObjects[i].getId() + "\"";
		    		if ((tempStoryObjects.size()%20==0)||(i+1==sObjects.length)){
		    			SFIds = SFIds.substring(1, SFIds.length());
		    			
		    			String storyQuery = MessageFormat.format("[{0}][in][{1}]", converterService.getSFIdFieldName(),SFIds);
		    			logger.info("storyQuery: " + storyQuery);
		    			List<IStoryEntity> entities =  storyService.FindAllSync(storyQuery);
		    			logger.info("response size: " + entities.size());
		    			for(IStoryEntity story :entities)
		    				for(IStoryEntity sf :tempStoryObjects)
		    					if (sf.getSalesForceId().equals(story.getSalesForceId()))
		    					{
		    						sf.setStoryId(story.getStoryId());
		    						break;
		    					}
		    			storyObjects.addAll(tempStoryObjects);
		    			tempStoryObjects.clear();
		    			SFIds=""; 
		    		}	
		    	}
		    	
		    	upsertStoryEntities(storyObjects,storyService, slog);
		    	
		    	String queryLocator = queryResults.getQueryLocator();
		    	if (queryLocator == null || queryLocator.isEmpty()) break;
		    	
		    	queryResults = connection.queryMore(queryLocator);
		      
	    	}
	    	
	    	
	    	
	    	slog.Deleted = removeDeleted(lastReplicationDate, thisReplicationDate, converterService.getSFTable(), converterService.getSFIdFieldName(), storyService);
	    	logger.info("*****" + converterService.getStoryType().getSimpleName() + "***** " + "Insert log ");
	    	slogService.Insert(slog).GetResult();
	    	
	    	logger.info("Finish replicate for StoryType: "+converterService.getStoryType().getSimpleName());
	    
	  	}
	  	  
	  String joinByComma(String[] strings){
		  return String.join(", ",strings);
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
	  
	  int removeDeleted(Date lastReplicationDate, Date thisReplicationDate, String sObjectType, String SFIdFieldName, StoryCLMServiceGeneric<IStoryEntity> storyService) throws ConnectionException, AuthFaliException, AsyncResultException{
		
		if (getDateDiff(lastReplicationDate, thisReplicationDate, TimeUnit.DAYS)>30){
			lastReplicationDate = new Date((new Date()).getTime() - 30 * 24 * 3600 * 1000l );
			logger.warn("************* "+sObjectType+" *************** дата последней репликации превышает 30 дней - возможна потеря информации об удаленных объектах!");
		}
		  Calendar startCal = new GregorianCalendar();
		//  lastReplicationDate = new Date(2017,4,01);
	      startCal.setTime(lastReplicationDate);
		  Calendar finisshCal = new GregorianCalendar();
		  finisshCal.setTime(thisReplicationDate);
		  GetDeletedResult queryResults = getConnection().getDeleted(sObjectType, startCal, finisshCal);
		  
		  DeletedRecord[] f = queryResults.getDeletedRecords();
		  logger.info("Get Deleted size " + f.length);
		  String SFIds=""; 
		  List<String> storyIds = new ArrayList<String>();
		  for(int i=0;i<f.length;i++){
	    	//	tempStoryObjects.add(converterService.ConvertToStory(sObjects[i]));
	    		SFIds+= ",\"" + f[i].getId() + "\"";
	    		if ((SFIds.length()>100)||(i+1==f.length)){
	    			SFIds = SFIds.substring(1, SFIds.length());
	    			String storyQuery = MessageFormat.format("[{0}][in][{1}]", SFIdFieldName,SFIds);
	    			logger.info("storyQuery: " + storyQuery);
	    			List<IStoryEntity> entities =  storyService.FindAllSync(storyQuery);
	    			logger.info("response size: " + entities.size());
	    			for(IStoryEntity entity:entities)
	    				storyIds.add(entity.getStoryId());
	    			SFIds=""; 
	    		}	
	    	}
		  if (storyIds.size()>0){
			  logger.info("will delete from story size: " + storyIds.size());
			  storyService.Delete(storyIds.toArray(new String[storyIds.size()])).GetResult();  
		  }
		  return storyIds.size();
	  }
	  
	  
	  void upsertStoryEntities(List<IStoryEntity> objects, StoryCLMServiceGeneric<IStoryEntity> storyService, StoryLog slog) throws AsyncResultException, AuthFaliException{
		
			List<IStoryEntity> updated = new ArrayList<IStoryEntity>();
			List<IStoryEntity> inserted = new ArrayList<IStoryEntity>();
			
			for(IStoryEntity record:objects){
				if (record.getStoryId()==null) inserted.add(record);
				else updated.add(record);
			}
			if (updated.size()!=0)	storyService.UpdateMany(updated.toArray(new IStoryEntity[0])).GetResult();
			if (inserted.size()!=0) storyService.InsertMany(inserted.toArray(new IStoryEntity[0])).GetResult();
	
			slog.Inserted+=inserted.size();
			slog.Updated+=updated.size();	
	  }
	
}
