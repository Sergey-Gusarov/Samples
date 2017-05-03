package ru.breffi.entityTypeConverterImpl.employee.service;

import java.lang.reflect.Type;

import ru.breffi.EntityTypeConverterServicePackage.AbstarctTypeConverterService;
import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.entityTypeConverterImpl.employee.dto.Employee__c;
import ru.breffi.entityTypeConverterImpl.employee.dto.QueryRecordsEmployee__c;
import ru.breffi.entityTypeConverterImpl.employee.dto.StoryEmployee;

public class Service extends AbstarctTypeConverterService {

	public Type getSFType() {
		return QueryRecordsEmployee__c.class;
	}

	public Class<? extends IStoryEntity> getStoryType() {
		// TODO Auto-generated method stub
		return StoryEmployee.class;
	}

	public IStoryEntity ConvertToStory(Object sf) {
		Employee__c sfe = (Employee__c)sf;
		StoryEmployee ste = new StoryEmployee();
		ste.Category =  sfe.getBF_Emloyee_category__c();
		ste.Company_SalesForce_Id = sfe.getBF_Emloyee_company__c();
		ste.Email = sfe.getBF_Emloyee_email__c();
		ste.Name = sfe.getBF_Emloyee_name__c();
		ste.Surname = sfe.getName();
		ste.Patronymic = sfe.getBF_Emloyee_patronymic__c();
		ste.Phone = sfe.getBF_Emloyee_tel__c();
		ste.Position = sfe.getBF_Emloyee_position__c();
		ste.SalesForce_Id = sfe.getId();
		ste.SignedAgreement = sfe.getBF_Emloyee_SignedAgreement__c();
		ste.Socialnetwork = sfe.getBF_Emloyee_socialnetwork__c();
		ste.Speciality = sfe.getBF_Emloyee_speciality__c();
		ste.TelemarketComment = sfe.getBF_TelemarketComment__c();
		ste.Type = sfe.getBF_Emloyee_typeofemployee__c();
		return ste;
	}

	@Override
	public Class<?> getSFEntityType() {
		return Employee__c.class;
	}


	
		
	/*	return new String[]{
				"Id",
				"Name",
				"BF_Emloyee_company__c"
				,"BF_Emloyee_SignedAgreement__c"
				,"BF_Emloyee_category__c"
				,"BF_Emloyee_email__c"
				,"BF_Emloyee_name__c"
				,"BF_Emloyee_patronymic__c"
				,"BF_Emloyee_position__c"
				,"BF_Emloyee_socialnetwork__c"
				,"BF_Emloyee_speciality__c"
				,"BF_Emloyee_tel__c"
				,"BF_Emloyee_typeofemployee__c"
				,"BF_TelemarketComment__c"
	};*/


}
