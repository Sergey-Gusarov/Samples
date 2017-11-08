

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
//import ru.breffi.PartnerEntityTypeConverterServiceImpl.territory.Service;

import ru.breffi.PartnerTypeConverterService.employee.service.Service;

import ru.breffi.Salesforce2StoryReplicator.Replicator;
import ru.breffi.Salesforce2StoryReplicator.SalesForceLoginConfig;
import ru.breffi.Salesforce2StoryReplicator.StoryLoginConfig;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;

/**
 * Unit test for simple App.
 */
public class ReplicatorTest extends TestCase
{
	static final String USERNAME = "vova.klyuev@breffi.ru";
	static final String PASSWORD = "08004FD8680441EFBA3FED245E95F692i6WOysylKFUMIzKtHFn6ymyn";
	static final String AuthEndpoint = "https://login.salesforce.com/services/Soap/u/29.0";
	
	
	/*static final String USERNAME = "vova.klyuev@breffi.ru";
	static final String PASSWORD = "1724841126A545B191A64AC7BA8412062hJr9tvGTJYsCIKUEhHKzU5aJ";
	static final String AuthEndpoint = "https://test.salesforce.com/services/Soap/u/29.0";
	*/
	
	static PartnerConnection connection;
	
	

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ReplicatorTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ReplicatorTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	Replicator r = new Replicator();
	  
	  	r.sfconfig = new SalesForceLoginConfig();
	  	r.sfconfig.UserName = USERNAME;
	  	r.sfconfig.Password = PASSWORD;
	  	r.sfconfig.AuthEndpoint = AuthEndpoint;
	  	r.storyConfig = new StoryLoginConfig();
	  	Service s = new Service();
	  	
	/*  TerritoryService s = new TerritoryService();
	 * 	r.storyConfig.ClientId="client_18_6";
	  	r.storyConfig.ClientSecret = "8e30ddbce1aa4558b4daf9286f0d59bc809bffef731a43dbbd96a28475c533f7";
	  	s.setLogTableId(69);
	  	s.setTableId(24);*/
	  	
		r.storyConfig.ClientId="client_30_3";
	  	r.storyConfig.ClientSecret = "1cd778d7252f477fa7db75ecdd3affbe2f67f1bfa9d64a74baf36ff29c5781bf";
	  	s.setLogTableId(76);
	  	s.setTableId(82);
	  	try{
	  		r.Replicate(s);	
	  	}
	  	
	  	catch(AuthFaliException e){
	  		e.printStackTrace();
	  	} catch (AsyncResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        assertTrue( true );
    }
}
