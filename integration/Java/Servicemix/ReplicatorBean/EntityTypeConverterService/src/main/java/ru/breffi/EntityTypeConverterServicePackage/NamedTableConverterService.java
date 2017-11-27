package ru.breffi.EntityTypeConverterServicePackage;

import com.sforce.soap.partner.sobject.SObject;

public interface NamedTableConverterService {
	Class<? extends IStoryEntity> getStoryType();
	IStoryEntity ConvertToStory(SObject sf);
	String getTableName();
	String getLogTableName();
	String getLockTableName();
	String[] getSFQueryFields();
	String getSFTable();
	String getSFIdFieldName();
}
