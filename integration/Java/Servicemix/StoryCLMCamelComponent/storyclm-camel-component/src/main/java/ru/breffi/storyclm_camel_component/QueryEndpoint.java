package ru.breffi.storyclm_camel_component;


import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultProducer;

import org.apache.camel.spi.UriEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a StoryCLM endpoint.
 */
@UriEndpoint(scheme = "StoryCLM", title = "StoryCLM", syntax="StoryCLM:name", label = "StoryCLM")
public class QueryEndpoint extends StoryCLMEndpoint {
	private static final Logger LOG = LoggerFactory.getLogger(QueryEndpoint.class);
		
		public QueryEndpoint(String endpointUri,StoryCLMComponent component) {
		super(endpointUri,component);
		// TODO Auto-generated constructor stub
	}

		Expression query;
		public void setQuery(String query){
			this.query = createSimpleExpression(query);
		}

	    public Producer createProducer() throws Exception {
	        return new DefaultProducer(this) {
				@Override
				public void process(Exchange exchange) throws Exception {
					LOG.debug("Query = " + query.toString());
					
					 List<Object> resultArray = new ArrayList<Object>();
						try {
							int count = getService(exchange).CountByQuery(query.evaluate(exchange, String.class)).GetResult();
							for(int i=0;i<count;i+=100){
								resultArray.addAll(getService(exchange).Find(query.evaluate(exchange, String.class), "Id", 1, i,  100).GetResult());
							}
							exchange.getIn().setBody(resultArray);
						}  catch (Exception e) {
							// TODO Auto-generated catch block
							LOG.error("ОШибка при выполнении запроса StoryCLM "+ e.getMessage());
							throw e;
						}
				}
			};
	    }
	    
	   @Override
		public Consumer createConsumer(Processor processor) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	 
}
