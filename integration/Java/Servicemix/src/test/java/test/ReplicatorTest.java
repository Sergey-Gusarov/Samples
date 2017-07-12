package test;



import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.breffi.story2sf.services.IConverterService;
import ru.breffi.story2sf.services.breffiuser.BreffiUserConverterService;
import ru.breffi.story2sf.services.visit.VisitConverterService;
import ru.breffi.story2sfreplicator.Replicator;
import ru.breffi.story2sfreplicator.SalesForceLoginConfig;
import ru.breffi.story2sfreplicator.StoryLoginConfig;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;

/**
 * Unit test for simple App.
 */
public class ReplicatorTest extends TestCase
{


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
	  	r.sfconfig = new  SalesForceLoginConfig();
	  	//Production
	  	
	  /*		
	    r.storyConfig.ClientId="client_30_1";
	  	r.storyConfig.ClientSecret = "7537d7ca7df0468e95f1a8a96e77bc548bc48aa045c84ea2b84e857b4c5e678a";
	  	
		r.sfconfig.UserName = "vova.klyuev@breffi.ru";
		r.sfconfig.Password = "08004FD8680441EFBA3FED245E95F692i6WOysylKFUMIzKtHFn6ymyn";
		r.sfconfig.AuthEndpoint = "https://login.salesforce.com/services/Soap/u/29.0";
		VisitConverterService visitService = new VisitConverterService(64, 73);
		*/
		
		
	  	//Test
		
	  	
	  	r.storyConfig.ClientId="client_18_5";
	  	r.storyConfig.ClientSecret = "858bae5dae734410b61013fa3d64bd8fd55c93bb4e80416d9eed35c7399f3da6";
	  	
	  	r.sfconfig.UserName = "vova.klyuev@breffi.ru";
		r.sfconfig.Password = "1724841126A545B191A64AC7BA8412062hJr9tvGTJYsCIKUEhHKzU5aJ";
		r.sfconfig.AuthEndpoint = "https://test.salesforce.com/services/Soap/u/29.0";
		//VisitConverterService visitService = new VisitConverterService(77, 78);
		IConverterService visitService = new BreffiUserConverterService(84, 85);
		
	  		
	
		
		  	
	 
	 /* 	r.tableId = 77;
	  	r.logTableId = 78;*/
	  	
	  	try{
	  		r.Replicate(visitService);	
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
