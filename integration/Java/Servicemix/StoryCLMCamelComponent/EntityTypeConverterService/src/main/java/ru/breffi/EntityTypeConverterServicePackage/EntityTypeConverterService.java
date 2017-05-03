package ru.breffi.EntityTypeConverterServicePackage;

import java.lang.reflect.Type;

public interface EntityTypeConverterService {
	Type getSFType();
	Class<? extends IStoryEntity> getStoryType();
	IStoryEntity ConvertToStory(Object sf);
	String getTableId();
	String[] getSFQueryFields();
	String getSFTable();
	String getLogTableId();
}
