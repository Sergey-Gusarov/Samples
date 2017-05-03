package ru.breffi.storyclm_camel_component;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;

import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMServiceConnector;


/**
 * Represents the component that manages {@link StoryCLMEndpoint}.
 */
public class StoryCLMComponent extends UriEndpointComponent {
	  	StoryCLMServiceConnector clientConnector;
	  	AccountSettings accountSettings;
	    public void Init() {
	    	clientConnector = StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector(accountSettings.getClientId(), accountSettings.getClientSecret(),null);
	    }
	  
	    
	    public AccountSettings getAccountSettings(){
	    	return this.accountSettings;
	    }
	    
	    public void setAccountSettings(AccountSettings accountSettings){
	    	this.accountSettings=accountSettings;
	    }
	    public StoryCLMComponent() {
	    	super(StoryCLMEndpoint.class);
	    }
	    public StoryCLMComponent(AccountSettings accountSettings) {
	    	super(StoryCLMEndpoint.class);
	    	setAccountSettings(accountSettings);
	    	
	    }
	    /*public StoryCLMComponent() {
	        super(StoryCLMEndpoint.class);
	        accountSettings = new AccountSettings();
	        accountSettings.client_id = "client_18";
	        accountSettings.client_secret = "595a2fb724604e51a1f9e43b808c76c915c2e0f74e8840b384218a0e354f6de6";
	        //Init(accountSettings);
	    }*/
	    
	    @Override
	    protected void doStart() throws Exception {
	    	this.Init();
	    	super.doStart();
	    	
	    }
	    public StoryCLMComponent(CamelContext context) {
	        super(context, StoryCLMEndpoint.class);
	    }

	    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
	    	StoryCLMEndpoint endpoint = null;
	    	switch(remaining)
	    	{	    	
	    		case "Find":endpoint=new FindEndPoint(uri,this);break;
	    		case "Query": endpoint = new QueryEndpoint(uri,this);break;
	    		case "Upsert":endpoint = new UpdateEndpoint(uri, this);
	    	}
	        setProperties(endpoint, parameters);
	        
	        endpoint.Init(clientConnector);
	        return endpoint;
	    }
}
