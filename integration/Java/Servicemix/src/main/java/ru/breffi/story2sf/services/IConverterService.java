package ru.breffi.story2sf.services;

import com.sforce.soap.partner.sobject.SObject;

public interface IConverterService {
	Class<? extends IStoryEntity> getStoryType();
	IStoryEntity ConvertToStory(SObject sf);
	int getTableId();
	String[] getSFQueryFields();
	String getSFTable();
	int getLogTableId();
	String getSFIdFieldName();
}