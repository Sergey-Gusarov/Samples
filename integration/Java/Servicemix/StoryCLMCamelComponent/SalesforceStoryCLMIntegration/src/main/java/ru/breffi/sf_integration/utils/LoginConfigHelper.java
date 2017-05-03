package ru.breffi.sf_integration.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.camel.component.salesforce.SalesforceLoginConfig;

public class LoginConfigHelper  { 

    private static final String TEST_LOGIN_PROPERTIES = "src/main/resources/test-salesforce-login.properties";

    public static SalesforceLoginConfig getLoginConfig() throws IOException {

        // load test-salesforce-login properties
        Properties properties = new Properties();
        InputStream stream = null;
        try {
         //   stream = new FileInputStream(TEST_LOGIN_PROPERTIES);
           // properties.load(stream);

            /*final SalesforceLoginConfig config = new SalesforceLoginConfig(
                properties.getProperty("loginUrl", SalesforceLoginConfig.DEFAULT_LOGIN_URL),
                properties.getProperty("clientId"),
                properties.getProperty("clientSecret"),
                properties.getProperty("userName"),
                properties.getProperty("password"),
                Boolean.parseBoolean(properties.getProperty("lazyLogin", "false")));
//*/
            
            final SalesforceLoginConfig config = new SalesforceLoginConfig(
                   "https://login.salesforce.com",
                   "3MVG9RHx1QGZ7OsgjGo1cAW4h4OmG7NrECFPwHBrEKMUHTrDO60RFkSewfYdgDoRCmFeW_2Gz6rp1sLorzR4w",
                   "1119479077639771374",
                   "vova.klyuev@breffi.ru",
                   "Qwe510091#mq2rBYRxEPtryhBtMOyqjpAd",false);
                    
     /*       assertNotNull("Null loginUrl", config.getLoginUrl());
            assertNotNull("Null clientId", config.getClientId());
            assertNotNull("Null clientSecret", config.getClientSecret());
            assertNotNull("Null userName", config.getUserName());
            assertNotNull("Null password", config.getPassword());
*/
            return config;

        } 
        /*catch (FileNotFoundException e) {
            throw new FileNotFoundException("Create a properties file named "
                + TEST_LOGIN_PROPERTIES + " with clientId, clientSecret, userName, and password"
                + " for a Salesforce account with Merchandise and Invoice objects from Salesforce Guides.");
        }*/ 
        finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

}