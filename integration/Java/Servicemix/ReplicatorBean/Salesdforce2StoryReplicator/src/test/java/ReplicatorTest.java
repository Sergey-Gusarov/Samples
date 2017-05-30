

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.breffi.PartnerEntityTypeConverterServiceImpl.territory.Service;
//import ru.breffi.PartnerTypeConverterService.employee.service.Service;
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
	  	r.storyConfig = new StoryLoginConfig();
	  	r.storyConfig.ClientId="client_18";
	  	r.storyConfig.ClientSecret = "595a2fb724604e51a1f9e43b808c76c915c2e0f74e8840b384218a0e354f6de6";
	  	r.sfconfig = new SalesForceLoginConfig();
	  	r.sfconfig.UserName = USERNAME;
	  	r.sfconfig.Password = PASSWORD;
	  	r.sfconfig.AuthEndpoint = AuthEndpoint;
	  	Service s = new Service();
	  	s.setLogTableId(42);
	  	s.setTableId(24);
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
