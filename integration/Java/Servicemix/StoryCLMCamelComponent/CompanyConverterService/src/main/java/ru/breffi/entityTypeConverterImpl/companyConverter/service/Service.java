package ru.breffi.entityTypeConverterImpl.companyConverter.service;


import java.lang.reflect.Type;

import ru.breffi.EntityTypeConverterServicePackage.AbstarctTypeConverterService;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.entityTypeConverterImpl.companyConverter.sfdto.BF_Company__c;
import ru.breffi.entityTypeConverterImpl.companyConverter.sfdto.QueryRecordsBF_Company__c;
import ru.breffi.entityTypeConverterImpl.companyConverter.storydto.Company;

public class Service extends AbstarctTypeConverterService{

	public Type getSFType() {
		return QueryRecordsBF_Company__c.class;
	}

	public Class<? extends IStoryEntity> getStoryType() {
		return Company.class;
	}

	public IStoryEntity ConvertToStory(Object sf) {
		
		Company company = new Company();
		BF_Company__c sfcompany = (BF_Company__c)sf;
		
		company.SalesForce_Id = sfcompany.getId();
		company.Name =sfcompany.getName() ;
		company.Territory_SalesForce_Id = sfcompany.getBF_City__c();
		company.Phone1 = sfcompany.getBF_Company_tel1__c();
		company.Phone2 = sfcompany.getBF_Company_tel2__c();
		company.Facilitytype = sfcompany.getBF_Company_Facilitytype__c();
		company.ManagmentView = sfcompany.getBF_Company_ManagmentView__c();	
		company.Type = sfcompany.getBF_Company_type__c();
		company.HQ = sfcompany.getBF_Company_HQ__c();
		company.HQ_subsidiary = sfcompany.getBF_Company_HQ_subsidiary__c();
		company.County =sfcompany.getBF_Emloyee_County__c();
		company.Street = sfcompany.getBF_Emloyee_Street__c();
		return company;
		
	}


	@Override
	public Class<?> getSFEntityType() {
		return BF_Company__c.class;
	}

}
