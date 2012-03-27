package com.gu.email.exacttarget;

import com.gu.email.GuardianUser;
import com.gu.email.exacttarget.ExactTargetException;
import com.gu.email.exacttarget.ExactTargetFactory;
import com.gu.email.exacttarget.TriggeredEmailResponse;
import com.gu.email.exacttarget.TriggeredEmailService;
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
    private static URI endPoint;
    private static final HttpClient httpClient = new HttpClient();
    private static String businessUnitId = "1062022";

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
        ExactTargetFactory factory = new ExactTargetFactory( accountName, password, businessUnitId, emailTemplate, endPoint );
        TriggeredEmailService emailService = new TriggeredEmailService( factory, httpClient );
        TriggeredEmailResponse response = emailService.sendEmailRequest( new GuardianUser( "billy_bob", "james.rodgers@guardian.co.uk" ) );

        System.out.println( response.getStatusCode() );
        System.out.println( response.getOverallStatus() );
        System.out.println( response.getRequestId() );
        System.out.println( response.getStatusMessage() );
        System.out.println( response.getDocumentAsString() );


    }
}
