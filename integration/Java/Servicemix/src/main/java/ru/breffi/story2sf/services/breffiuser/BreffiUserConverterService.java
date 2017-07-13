package ru.breffi.story2sf.services.breffiuser;

import com.sforce.soap.partner.sobject.SObject;

import ru.breffi.story2sf.services.IStoryEntity;
import ru.breffi.story2sf.services.StandartConverterService;

public class BreffiUserConverterService extends StandartConverterService{

	public BreffiUserConverterService(int tableId, int logTableId) {
		super(tableId, logTableId, "Users_BREFFI__c", "UB_StoryCLM_Id__c");
	}
	
	@Override
	public Class<? extends IStoryEntity> getStoryType() {
		return BreffiUser.class;
	}
	
	
	  
	@Override
	public SObject ConvertToSF(IStoryEntity sf) {
		  SObject s = new SObject();
		  BreffiUser user = (BreffiUser) sf;
		  s.setType("Users_BREFFI__c");
		  s.setId(user.SFId);
		  s.addField("UB_StoryCLM_Id__c", user._id);
		  s.addField("BF_Dismissed__c", user.Active);
		  s.addField("UB_Name__c", user.Name);
		  s.addField("UB_MiddleName__c", user.PatronymicName);
		
		  s.addField("UB_Surname__c", user.Surname);
		  
		  s.addField("UB_Territory__c", user.Territory_SFId);
		  s.addField("UB_Email__c", user.UserName);
		  return s;
	}

	

	

}
