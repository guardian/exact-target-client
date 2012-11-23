package com.gu.email.exacttarget;

import org.apache.commons.httpclient.HttpClient;
import org.jdom.JDOMException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// TODO: move this to tests
public class Harness
{
    private static final String accountName = "gnmtestuser";
    private static final String password = "row_4boat";
    private static final String emailTemplate = "001WelcomeEmailTriggerET";
    private static String businessUnitId = "1062022";

    private static URI endPoint;
    private static final HttpClient httpClient = new HttpClient();

    static {
        try
        {
            endPoint = new URI( "https://webservice.s4.exacttarget.com/Service.asmx" );
        }
        catch( URISyntaxException e )
        {
            e.printStackTrace();
        }
    }

    public static void main( String args[] ) throws IOException, JDOMException, ExactTargetException
    {
        ExactTargetFactory factory = new ExactTargetFactory( accountName, password, emailTemplate, endPoint );
        ExactTargetSoapApiService exactTargetSoapApiService = new ExactTargetSoapApiService( factory, httpClient );
        TriggeredEmailResponse response = exactTargetSoapApiService.sendEmailRequest( "John Smit", "james.rodgers@guardian.co.uk", businessUnitId );

        System.out.println( response.getStatusCode() );
        System.out.println( response.getOverallStatus() );
        System.out.println( response.getRequestId() );
        System.out.println( response.getStatusMessage() );
        System.out.println( response.getDocumentAsString() );


    }
}
