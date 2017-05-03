package ru.breffi.sf_integration.utils;

import java.sql.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import ru.breffi.sf_integration.dto.StoryLog;

public class CheckDateProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		if (exchange.getIn().getBody()==null)
			exchange.getIn().setBody(new StoryLog());
		
	}

}
