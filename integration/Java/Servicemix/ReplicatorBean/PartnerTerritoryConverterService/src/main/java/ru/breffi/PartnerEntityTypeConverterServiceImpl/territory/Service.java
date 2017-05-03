package ru.breffi.PartnerEntityTypeConverterServiceImpl.territory;

import com.sforce.soap.partner.sobject.SObject;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.EntityTypeConverterServicePackage.PartnerAbstarctTypeConverterService;

public class Service extends PartnerAbstarctTypeConverterService{


	public Class<? extends IStoryEntity> getStoryType() {return Territory.class; }
	

	public IStoryEntity ConvertToStory(SObject sfTerritory) {
		Territory result = new Territory();
		result.Name = (String) sfTerritory.getField("Name");
		result.Parent_SFId = (String) sfTerritory.getField("BF_Territory_Parent__c");
		result.Type = (String) sfTerritory.getField("RecordTypeId");
		result.SalesForce_Id = sfTerritory.getId();
		return result;
	}


	public String[] getSFQueryFields() {
		return new String[]{"Id","Name", "BF_Territory_Parent__c", "RecordTypeId"};
	}


	public String getSFTable() {
		return "BF_Territory__c";
	}



}
