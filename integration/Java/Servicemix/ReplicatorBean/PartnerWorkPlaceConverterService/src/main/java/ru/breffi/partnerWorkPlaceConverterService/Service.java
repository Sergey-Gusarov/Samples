package ru.breffi.partnerWorkPlaceConverterService;


import com.sforce.soap.partner.sobject.SObject;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.EntityTypeConverterServicePackage.PartnerAbstarctTypeConverterService;

public class Service extends PartnerAbstarctTypeConverterService{


	public Class<? extends IStoryEntity> getStoryType() {
		//TODO Auto-generated method stub
		return StoryWorkPlace.class;
	}

	public IStoryEntity ConvertToStory(SObject sfe) {
		StoryWorkPlace ste = new StoryWorkPlace();
		ste.Employee_SalesForce_Id =  (String)sfe.getField("BF_PlaceofWork_Employee__c");
		ste.Company_SalesForce_Id = (String)sfe.getField("BF_PlaceofWork_Company__c");
		ste.Position = (String)sfe.getField("BF_PlaceofWork_position__c");
		ste.SalesForce_Id = sfe.getId();
		ste.PrimaryWorkPlace = Boolean.parseBoolean((String)sfe.getField("BF_PlaceOfWork_Main__c"));
		return ste;
	}


	public String[] getSFQueryFields() {
		return new String[]{
				"Id",
				"BF_PlaceofWork_Employee__c",
				"BF_PlaceofWork_Company__c",
				"BF_PlaceofWork_position__c",
				"BF_PlaceOfWork_Main__c"
		};
	}

	public String getSFTable() {
		return "BF_PlaceOfWork__c";
	}

}
