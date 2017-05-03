package ru.breffi.sf_integration.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import ru.breffi.EntityTypeConverterServicePackage.EntityTypeConverterService;
import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;

public class ConverterByHeaderConverter implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println(exchange.getIn().getBody());
		EntityTypeConverterService service = (EntityTypeConverterService)exchange.getIn().getHeader("EntityTypeService");
		List<Object> sfList = (List<Object>)exchange.getIn().getBody();
		List<IStoryEntity> storyList = new ArrayList<IStoryEntity>();
		for(int i=0;i<sfList.size();i++){
			storyList.add(service.ConvertToStory(sfList.get(i)));
		}
		exchange.getIn().setBody(storyList);
	}

}
