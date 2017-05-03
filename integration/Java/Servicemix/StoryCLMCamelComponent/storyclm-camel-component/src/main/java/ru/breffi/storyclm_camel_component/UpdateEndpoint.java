package ru.breffi.storyclm_camel_component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultProducer;

import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;

public class UpdateEndpoint extends StoryCLMEndpoint{

	public UpdateEndpoint(String uri, StoryCLMComponent component) {
		super(uri, component);
	}

	String storyIdName;
	public void setStoryIdName(String name){
		storyIdName = name;
	}
	@Override
	public Producer createProducer() throws Exception {
		   return new DefaultProducer(this) {
				@Override
				public void process(Exchange exchange) throws Exception {
						
							List<Object> objects = null;
							try{
								objects = (List<Object>)exchange.getIn().getBody();
							}
							catch(ClassCastException ex){
								objects = new ArrayList<Object>();
								objects.add(exchange.getIn().getBody());
							}
							List<Object> updated = new ArrayList<Object>();
							List<Object> inserted = new ArrayList<Object>();
							
							for(Object record:objects){
								if (storyIdName == null) {
									inserted.add(record);
									continue;
								}
								Field field = record.getClass().getField(storyIdName);
								if (field.get(record) == null) inserted.add(record);
								else updated.add(record);
							}
							if (updated.size()!=0)	getService(exchange).UpdateMany(updated.toArray()).GetResult();
							if (inserted.size()!=0) getService(exchange).InsertMany(inserted.toArray()).GetResult();
						exchange.getIn().setHeader("Updated", updated.size());
						exchange.getIn().setHeader("Inserted", inserted.size());
				}
			};
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
