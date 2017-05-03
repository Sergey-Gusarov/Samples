package ru.breffi.sf_integration.converters;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;

public class Story2IdsBean {
	public String Convert(IStoryEntity[] terrs){
		String result= "";		
		for(IStoryEntity terr:terrs){
			result+="\""+terr.getSalesForceId() +"\"" + ",";
		}
		return result.substring(0, result.lastIndexOf(","));
	}
}
