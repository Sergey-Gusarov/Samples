package ru.breffi.Salesforce2StoryReplicator;

import com.sforce.soap.partner.sobject.SObject;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.EntityTypeConverterServicePackage.NamedTableConverterService;
import ru.breffi.EntityTypeConverterServicePackage.PartnerTypeConverterService;

public class NamedTableConverterServiceAdapter implements NamedTableConverterService{

	PartnerTypeConverterService cService;
	
	
	public NamedTableConverterServiceAdapter(PartnerTypeConverterService service){
		cService = service;
	}
	
	@Override
	public Class<? extends IStoryEntity> getStoryType() {
		return cService.getStoryType();
	}

	@Override
	public IStoryEntity ConvertToStory(SObject sf) {
		return cService.ConvertToStory(sf);
	}

	@Override
	public String getTableName() {
		return cService.getStoryType().getSimpleName();
	}

	@Override
	public String getLogTableName() {
		return getTableName() +"Log";
	}

	@Override
	public String getLockTableName() {
		return getTableName() +"Lock";
	}

	@Override
	public String[] getSFQueryFields() {
		return cService.getSFQueryFields();
	}

	@Override
	public String getSFTable() {
		return cService.getSFTable();
	}

	@Override
	public String getSFIdFieldName() {
		return cService.getSFIdFieldName();
	}

}
