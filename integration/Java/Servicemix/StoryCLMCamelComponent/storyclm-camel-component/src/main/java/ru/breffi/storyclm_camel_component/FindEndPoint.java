package ru.breffi.storyclm_camel_component;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultProducer;

import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;

public class FindEndPoint extends StoryCLMEndpoint{
public FindEndPoint(String uri, StoryCLMComponent component) {
		super(uri, component);
		// TODO Auto-generated constructor stub
	}
	private String maxByField;
	public void setMaxByField(String maxByField){
		this.maxByField = maxByField;	
	}
	public String getMaxByField(){
		return this.maxByField;
	}

	   
	   public Producer createProducer() throws Exception {
	        return new DefaultProducer(this){
				@Override
				public void process(Exchange exchange) throws Exception {
					 List<Object> resultArray = new ArrayList<Object>();
							int count = getService(exchange).Count().GetResult();
							//System.out.println(count);
							for(int i=0;i<count;i+=100){
								resultArray.addAll(getService(exchange).Find(i, 100).GetResult());
							}
							//System.out.println(resultArray.get(resultArray.size()-1));
							if (maxByField!=null)
								if (resultArray.size()==0)
									exchange.getIn().setBody(null); //Заглушка просто выбирает первую запись
								else
									exchange.getIn().setBody(resultArray.get(resultArray.size()-1)); //Заглушка просто выбирает первую запись
							else 
								exchange.getIn().setBody(resultArray);
				}
	        	
	        };
	    }



	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
