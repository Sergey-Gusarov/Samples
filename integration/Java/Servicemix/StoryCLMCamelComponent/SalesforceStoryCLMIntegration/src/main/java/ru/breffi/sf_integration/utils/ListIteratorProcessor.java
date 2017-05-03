package ru.breffi.sf_integration.utils;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ListIteratorProcessor implements Processor{

	@Override
	public void process(Exchange exc) throws Exception {
		int storyStart = 0;
		int storyLimit = 0;
		List<Object> records = (List<Object>)exc.getIn().getHeader("SFRecords");
		
		if(exc.getIn().getHeader("storyStart").getClass() == String.class){
	
		storyStart = Integer.parseInt((String)exc.getIn().getHeader("storyStart"));
		storyLimit = Integer.parseInt((String)exc.getIn().getHeader("storyLimit"));
		exc.getIn().setHeader("storyLimit", storyLimit);
		}
		else 
		{
			storyStart = ((int)exc.getIn().getHeader("storyStart"));
			storyLimit= ((int)exc.getIn().getHeader("storyLimit"));			
		}
	
		List<Object> sublist = records.subList(storyStart, Math.min(storyLimit+storyStart,records.size()));
		exc.getIn().setBody(sublist);
		exc.getIn().setHeader("storyStart",storyLimit+storyStart);
		
	}

}
