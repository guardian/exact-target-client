package com.gu.email.exacttarget;

import com.gu.email.GuardianUser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.gu.email.exacttarget.soap.Namespaces.ET;
import static com.gu.email.exacttarget.soap.Namespaces.SOAP;

public class Harness
{
    private static final String accountName = "gnmtestuser";
    private static final String password = "row_4boat";
    private static final String emailTemplate = "001WelcomeEmailTriggerET";
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

    public static void main( String args[] ) throws IOException, JDOMException
    {
        ExactTargetSoapFactory factory = new ExactTargetSoapFactory( accountName, password, emailTemplate, endPoint );

        TriggeredEmailRequest emailRequest = factory.createRequest( new GuardianUser( "Kevin Balls", "james.rodgers@guardian.co.uk" ) );
        PostMethod postMethod = factory.createPostMethod( emailRequest );

        System.out.println( httpClient.executeMethod( postMethod ) );

        // TriggeredEmailResponse response = postMethod.getTriggeredEmailResponse();


        Document responseDocument = new SAXBuilder().build( postMethod.getResponseBodyAsStream() );

        XMLOutputter outputter = new XMLOutputter( Format.getPrettyFormat() );
        outputter.output( responseDocument, System.out );

        Element rootElement = responseDocument.getRootElement();
        Element body = rootElement.getChild( "Body", SOAP );
        Element createResponse = body.getChild( "CreateResponse", ET );
        Element results = createResponse.getChild( "Results", ET );
        String statusCode = results.getChildText( "StatusCode", ET );
        String statusMessage = results.getChildText( "StatusMessage", ET );
        String requestId = createResponse.getChildText( "RequestID", ET );
        String overallStatus = createResponse.getChildText( "OverallStatus", ET );
        System.out.println( statusCode + " " + statusMessage + " " + requestId + " " + overallStatus );
    }
}
