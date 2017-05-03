package ru.breffi.sf_integration.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class AllInOneAgregationStrategy implements AggregationStrategy{

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		
		Object newTerr = newExchange.getIn().getBody();
		@SuppressWarnings("unchecked")
		List<Object>terrs = (oldExchange == null)?new  ArrayList<Object>():(ArrayList<Object>)oldExchange.getIn().getBody();
		terrs.add(newTerr);
		newExchange.getIn().setBody(terrs);
		return newExchange;
	}

}
