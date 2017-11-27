package ru.breffi.PartnerTypeConverterService.employee.service;


import com.sforce.soap.partner.sobject.SObject;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.EntityTypeConverterServicePackage.PartnerAbstarctTypeConverterService;
import ru.breffi.partnerEmployeeConverterService.employee.dto.Employee;

public class Service extends PartnerAbstarctTypeConverterService{


	public Class<? extends IStoryEntity> getStoryType() {
		//TODO Auto-generated method stub
		return Employee.class;
	}

	public IStoryEntity ConvertToStory(SObject sfe) {
		Employee ste = new Employee();
		ste.KeyClient = Boolean.parseBoolean((String)sfe.getField("BF_Emloyee_KC__c"));
		ste.Category =  (String)sfe.getField("BF_Emloyee_category__c");
	//	ste.Company_SalesForce_Id = (String)sfe.getField("BF_Emloyee_company__c");
		ste.Email = (String)sfe.getField("BF_Emloyee_email__c");
		ste.Name =(String) sfe.getField("BF_Emloyee_name__c");
		ste.Surname =(String) sfe.getField("Name");
		ste.Patronymic =(String) sfe.getField("BF_Emloyee_patronymic__c");
		ste.Phone = (String)sfe.getField("BF_Emloyee_tel__c");
	//	ste.Position = (String)sfe.getField("BF_Emloyee_position__c");
		ste.SalesForce_Id = sfe.getId();
		ste.SignedAgreement = Boolean.parseBoolean((String)sfe.getField("BF_Emloyee_SignedAgreement__c"));
		ste.Socialnetwork =(String) sfe.getField("BF_Emloyee_socialnetwork__c");
		ste.Speciality =(String) sfe.getField("BF_Emloyee_speciality__c");
	//	ste.TelemarketComment =(String)sfe.getField("BF_TelemarketComment__c");
	//	ste.Type = (String)sfe.getField("BF_Emloyee_typeofemployee__c");
		return ste;
	}


	public String[] getSFQueryFields() {
		return new String[]{
				"BF_Emloyee_KC__c",
				"BF_Emloyee_category__c",
			//	"BF_Emloyee_company__c",
				"BF_Emloyee_email__c",
				"BF_Emloyee_name__c",
				"Name",
				"BF_Emloyee_patronymic__c",
				"BF_Emloyee_tel__c",
			//	"BF_Emloyee_position__c",
				"Id",
				 "BF_Emloyee_SignedAgreement__c",
				 "BF_Emloyee_socialnetwork__c",
				 "BF_Emloyee_speciality__c",
				// "BF_TelemarketComment__c",
				// "BF_Emloyee_typeofemployee__c"		
		};
	}

	public String getSFTable() {
		return "BF_Employee__c";
	}



}
