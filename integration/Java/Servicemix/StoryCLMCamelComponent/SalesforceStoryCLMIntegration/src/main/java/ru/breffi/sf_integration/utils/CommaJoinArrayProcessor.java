
package ru.breffi.sf_integration.utils;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CommaJoinArrayProcessor implements Processor{

	public String Join(String[] array){
		return String.join(", ", array);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setHeader("SFQueryFields",Join((String[])exchange.getIn().getHeader("SFQueryFields")));
		
	}
}
