package ru.breffi.story2sfreplicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;


public class PartnerConnectionUtils {
	PartnerConnection connection;
	PartnerConnection getConnection(){return connection;}
	
	public PartnerConnectionUtils(PartnerConnection connection){
		this.connection = connection;
	}
	
	  /*
	   * Апсертит записи в SF, учитывая ограничения сервера (например, не более 200 записей за раз)
	   */
	public List<UpsertResult> UpsertToSF(List<SObject> sfEntities, String externalIdFieldName) throws ConnectionException
	{
		  List<UpsertResult> result = new ArrayList<UpsertResult>();
		 for(int i=0;i<sfEntities.size();i+=200){
			 List<SObject> currentVisit = sfEntities.subList(i, Math.min(i+200, sfEntities.size()));
			 result.addAll(Arrays.asList(getConnection().upsert(externalIdFieldName, currentVisit.toArray(new SObject[currentVisit.size()]))));
		 }
		 return result;
	}
	  
/*	public Map<String,SObject> UpsertToSF(List<SObject> sfEntities, String externalIdFieldName, Consumer<UpsertResult> failedResultCallback) throws ConnectionException{
		  List<UpsertResult> result = new ArrayList<UpsertResult>();
		  Map<String,SObject> succesObjects = new HashMap();
		 for(int i=0;i<sfEntities.size();i+=200){
			 List<SObject> currentVisit = sfEntities.subList(i, Math.min(i+200, sfEntities.size()));
			 result.addAll(Arrays.asList(getConnection().upsert(externalIdFieldName, currentVisit.toArray(new SObject[currentVisit.size()]))));
		 }
		 for(int i=0;i<result.size();i++){
			 SObject v = sfEntities.get(i);
	    		if (!result.get(i).isSuccess()&& failedResultCallback!=null) 
	    			failedResultCallback.accept(result.get(i)); 
	    		else
	    			if(result.get(i).getCreated())
		    		{
		    			v.setField(updatedResults.get(i).getId());
		    			insertedVisits.add(v);
		    		}
		    		else {
		    			slog.Updated++;
		    		}
		    		
	    	}
		 return result;
	}
	*/
	
	
	 
	public void DeleteFromSF(List<String> removingSFId) throws ConnectionException{
			 for(int i=0;i<removingSFId.size();i+=200){
				 List<String> removingPart = removingSFId.subList(i, Math.min(i+200, removingSFId.size()));
					 getConnection().delete(removingPart.toArray(new String[removingPart.size()]));
			 }
	 }
	
	public void DeleteFromSF(Stream<String> removingSFId) throws ConnectionException{
			DeleteFromSF(removingSFId.collect(Collectors.toList()));
	} 
		 
	 /*
	  * Возвращает все объекты (с использованием querylocator)
	  */
	 public List<SObject> QueryAll(String query) throws ConnectionException{

	    	final List<SObject> result = new ArrayList<SObject>();
	    	QueryPartList(query, x-> result.addAll(x));
	    	return result;
	 }
	 
	 public void QueryPartList(String query,final ThrowingConsumer<List<SObject>> consumePart) 
	 {
		 QueryPartArray(query, x-> {
	    		List<SObject> result = new ArrayList<SObject>();
	    		for(SObject d : x) result.add(d);
	    		consumePart.accept(result);
	    	});
	 }
	 public void QueryPartArray(String query, ThrowingConsumer<SObject[]> consumePart) {
		 try{
			QueryResult queryResults = getConnection().query(query);

	    	while(true){
		    	SObject[] sObjects = queryResults.getRecords();
		    	consumePart.accept(sObjects);
		    	
		    	String queryLocator = queryResults.getQueryLocator();
		    	if (queryLocator == null || queryLocator.isEmpty()) break;
		    	
		    	queryResults = getConnection().queryMore(queryLocator);
		      
	    	}
	    	}
		 catch (final ConnectionException e) {
			 throw new RuntimeException(e);
		}
	}
	 
}
