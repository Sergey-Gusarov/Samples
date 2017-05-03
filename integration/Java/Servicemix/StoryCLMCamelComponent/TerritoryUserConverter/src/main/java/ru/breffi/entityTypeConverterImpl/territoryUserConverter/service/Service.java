package ru.breffi.entityTypeConverterImpl.territoryUserConverter.service;

import java.lang.reflect.Type;

import ru.breffi.EntityTypeConverterServicePackage.AbstarctTypeConverterService;
import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.entityTypeConverterImpl.territoryUserConverter.dto.BF_TerritoryUser__c;
import ru.breffi.entityTypeConverterImpl.territoryUserConverter.dto.QueryRecordsBF_TerritoryUser__c;
import ru.breffi.entityTypeConverterImpl.territoryUserConverter.dto.TerritoryUser;

public class Service extends AbstarctTypeConverterService {

	public Type getSFType() {
		// TODO Auto-generated method stub
		return QueryRecordsBF_TerritoryUser__c.class;
	}

	public Class<? extends IStoryEntity> getStoryType() {
		// TODO Auto-generated method stub
		return TerritoryUser.class;
	}

	public IStoryEntity ConvertToStory(Object sf) {
		TerritoryUser storyUser = new TerritoryUser();
		BF_TerritoryUser__c sfuser = new BF_TerritoryUser__c();
		storyUser.SalesForce_Id = sfuser.getId();
		storyUser.Name = sfuser.getName();
		storyUser.User_SalesForce_Id = sfuser.getBF_User__c();
		storyUser.Territory_SalesForce_Id = sfuser.getBF_Territory__c();
		return storyUser;
	}

	@Override
	public Class<?> getSFEntityType() {
		// TODO Auto-generated method stub
		return BF_TerritoryUser__c.class;
	}

}
