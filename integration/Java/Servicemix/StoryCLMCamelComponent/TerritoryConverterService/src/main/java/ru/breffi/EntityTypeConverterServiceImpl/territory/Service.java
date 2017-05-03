package ru.breffi.EntityTypeConverterServiceImpl.territory;

import java.lang.reflect.Type;

import ru.breffi.EntityTypeConverterServicePackage.AbstarctTypeConverterService;
import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;

public class Service extends AbstarctTypeConverterService{


	public Type getSFType() {return 	QueryRecordsBF_Territory__c.class; }

	public Class<? extends IStoryEntity> getStoryType() {return Territory.class; }

	public IStoryEntity ConvertToStory(Object sf) {
		Territory result = new Territory();
		BF_Territory__c sfTerritory = (BF_Territory__c) sf;
		result.Name = sfTerritory.getName();
		result.Parent_SFId = sfTerritory.getBF_Territory_Parent__c();
		result.Type = sfTerritory.getRecordTypeId();
		result.SalesForce_Id = sfTerritory.getId();
		return result;
	}

	@Override
	public Class<?> getSFEntityType() {
		return BF_Territory__c.class;
	}
	

}
