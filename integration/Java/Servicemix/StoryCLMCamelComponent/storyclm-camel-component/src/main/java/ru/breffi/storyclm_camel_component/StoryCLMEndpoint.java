package ru.breffi.storyclm_camel_component;

import java.lang.reflect.Type;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Language;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import ru.breffi.storyclmsdk.StoryCLMServiceConnector;
import ru.breffi.storyclmsdk.StoryCLMServiceGeneric;

/**
 * Represents a StoryCLM endpoint.
 */
@UriEndpoint(scheme = "StoryCLM", title = "StoryCLM", syntax="StoryCLM:name", label = "StoryCLM")
public abstract class StoryCLMEndpoint extends DefaultEndpoint {
	  @UriPath @Metadata(required = "true")
	    private String name;
	    @UriParam(defaultValue = "10")
	    private int option = 10;

	    StoryCLMServiceConnector clientConnector;
	    Expression tableId;
	    public void setTableId(String tableId){
	    	this.tableId = createSimpleExpression(tableId);
	    }
	    
	   Type entityType = null;
	   public void setEntityType(String type) throws ClassNotFoundException{
	    	if (type== null)
	    		this.entityType = Object.class;
	    	else
	    		this.entityType = Class.forName(type);
	    }
	  /*  public void setEntityType(Type type) throws ClassNotFoundException{
	    	this.entityType = type;
	    }*/
	    Type getEntityType(){
	    	return entityType==null?Object.class:entityType;
	    }
	    Type getEntityType(Exchange exchange){
	    	Object t = exchange.getIn().getHeader("StoryEntityType");
	    	return t==null?getEntityType():(Type)t;
	    	//return (Type)exchange.getIn().getHeader("StoryEntityType");
	    	//return entityType==null?Object.class:entityType;
	    }

	    public StoryCLMEndpoint(String uri, StoryCLMComponent component) {
	        super(uri, component);
	       
	        
	    }
	    public void Init(StoryCLMServiceConnector clientConnector)
	    {
	    	 this.clientConnector = clientConnector;
	    }

	    public StoryCLMEndpoint(String endpointUri) {
	        super(endpointUri);
	    }


	    public boolean isSingleton() {
	        return true;
	    }

	    /**
	     * Some description of this option, and what it does
	     */
	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getName() {
	        return name;
	    }

	    /**
	     * Some description of this option, and what it does
	     */
	    public void setOption(int option) {
	        this.option = option;
	    }

	    public int getOption() {
	        return option;
	    }
	    
	    protected  StoryCLMServiceGeneric<Object> getService(Exchange exchange){
	    	return  clientConnector.GetService(this. getEntityType(exchange),this.tableId.evaluate(exchange, Integer.class));
	    }
	    protected Expression createSimpleExpression(String expression) {
	        Language language;
	        // only use file language if the name is complex (eg. using $)
	        if (expression.contains("$")) {
	            language = getCamelContext().resolveLanguage("simple");
	        } else {
	            language = getCamelContext().resolveLanguage("constant");
	        }
	        return language.createExpression(expression);
	    }

	 

}
