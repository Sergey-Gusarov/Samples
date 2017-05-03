package ru.breffi.partnerEntityTypeConverterImpl.companyConverter.service;

import com.sforce.soap.partner.sobject.SObject;
import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.EntityTypeConverterServicePackage.PartnerAbstarctTypeConverterService;
import ru.breffi.entityTypeConverterImpl.companyConverter.storydto.Company;

public class Service extends PartnerAbstarctTypeConverterService{

	public Class<? extends IStoryEntity> getStoryType() {
		return Company.class;
	}



	public IStoryEntity ConvertToStory(SObject sfcompany) {
		
		Company company = new Company();
		
		company.SalesForce_Id = sfcompany.getId();
		company.Name =(String) sfcompany.getField("Name") ;
		company.Territory_SalesForce_Id = (String) sfcompany.getField("BF_City__c");
		company.Phone1 = (String) sfcompany.getField("BF_Company_tel1__c");
		company.Phone2 = (String) sfcompany.getField("BF_Company_tel2__c");
		company.Facilitytype = (String) sfcompany.getField("BF_Company_Facilitytype__c");
		company.ManagmentView = (String) sfcompany.getField("BF_Company_ManagmentView__c");	
		company.Type = (String) sfcompany.getField("BF_Company_type__c");
		company.HQ = (String) sfcompany.getField("BF_Company_HQ__c");
		company.HQ_subsidiary = (String) sfcompany.getField("BF_Company_HQ_subsidiary__c");
		company.County =(String) sfcompany.getField("BF_Emloyee_County__c");
		company.Street = (String) sfcompany.getField("BF_Emloyee_Street__c");
		return company;
	}

	public String[] getSFQueryFields() {
		return new String[]{
			"Id",
			"Name",
			"BF_City__c",
			"BF_Company_tel1__c",
			"BF_Company_tel2__c",
			"BF_Company_Facilitytype__c",
			"BF_Company_ManagmentView__c",
			"BF_Company_type__c",
			"BF_Company_HQ__c",
			"BF_Company_HQ_subsidiary__c",
			"BF_Emloyee_County__c",
			"BF_Emloyee_Street__c"
		};
	}

	public String getSFTable() {
		return "BF_Company__c";
	}

}
