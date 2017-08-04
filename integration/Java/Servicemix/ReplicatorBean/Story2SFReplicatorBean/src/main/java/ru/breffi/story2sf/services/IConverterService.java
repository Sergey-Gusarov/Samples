package ru.breffi.story2sf.services;

import com.sforce.soap.partner.sobject.SObject;

public interface IConverterService {
	Class<? extends IStoryEntity> getStoryType();
	SObject ConvertToSF(IStoryEntity sf);
	
	
	/**
	 * Возвращает название поля в SF, хранящее идентификатор Story
	 * @return
	 */
	String getStoryIdNameInSF();
	
	//String[] getSFQueryFields();
	String getSFTable();
	
	int getLogTableId();
	int getTableId();
	//String getSFIdFieldName();
}