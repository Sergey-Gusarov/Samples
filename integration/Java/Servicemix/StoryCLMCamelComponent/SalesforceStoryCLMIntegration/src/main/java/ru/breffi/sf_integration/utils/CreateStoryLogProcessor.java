package ru.breffi.sf_integration.utils;

import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.sf_integration.dto.StoryLog;

public class CreateStoryLogProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		@SuppressWarnings("unchecked")
		StoryLog log = (StoryLog)exchange.getIn().getHeader("StoryLog");
		if (log == null) { 
			log = new StoryLog();	log.Date=( (Date) exchange.getIn().getHeader("LastReplicDateNew"));}
		else
		{
			List<IStoryEntity> list =(List<IStoryEntity>) exchange.getIn().getBody();
			for(int i=0;i<list.size();i++){
				//System.out.println(list.get(i).getStoryId());
			if (list.get(i).getStoryId()==null) log.Inserted++;
			else log.Updated++;
		}
		}
		exchange.getIn().setHeader("StoryLog", log);
	}

}
