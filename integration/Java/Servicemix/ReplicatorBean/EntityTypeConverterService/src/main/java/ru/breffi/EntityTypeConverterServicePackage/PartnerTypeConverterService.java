package ru.breffi.EntityTypeConverterServicePackage;

import com.sforce.soap.partner.sobject.SObject;

public interface PartnerTypeConverterService {
	Class<? extends IStoryEntity> getStoryType();
	IStoryEntity ConvertToStory(SObject sf);
	int getTableId();
	String[] getSFQueryFields();
	String getSFTable();
	int getLogTableId();
	String getSFIdFieldName();
}
