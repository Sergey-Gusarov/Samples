package ru.breffi.sf_integration.utils;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class TestBodyProcess implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		// TODO Auto-generated method stub
		System.out.println("Body:");
	//	System.out.println(Class.forName("ru.breffi.EntityTypeConverterService.Impl.QueryRecordsBF_Territory__c").getTypeName());
		System.out.println(exchange.getIn().getBody());
	}

}
