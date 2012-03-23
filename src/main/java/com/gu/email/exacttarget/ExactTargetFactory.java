package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public class ExactTargetFactory
{
    private final String emailTemplate;
    private final URI endPoint;
    private String soapAction = "Create";
    private final AccountDetails accountDetails;

    public ExactTargetFactory( String accountName, String password, String emailTemplate, URI endPoint )
    {
        accountDetails = new AccountDetails(accountName, password, "");
        this.emailTemplate = emailTemplate;
        this.endPoint = endPoint;
    }

    public PostMethod createPostMethod( RequestEntity body )
    {
        PostMethod method = new PostMethod( endPoint.toString() );

        method.setRequestHeader( "Host", endPoint.getHost() );
        method.setRequestHeader( "Content-Type", "text/xml; charset=utf-8" );
        method.setRequestHeader( "Content-Length", "" + body.getContentLength() );
        method.setRequestHeader( "SOAPAction", soapAction );
        method.setRequestEntity( body );

        return method;
    }

    public TriggeredEmailRequest createRequest( GuardianUser guardianUser )
    {
        TriggeredEmailRequest triggeredRequest = new TriggeredEmailRequest( accountDetails, emailTemplate, guardianUser, soapAction );
        return triggeredRequest;
    }

    public TriggeredEmailResponse createResponseDocument( PostMethod postMethod ) throws ExactTargetException
    {
        InputStream inputStream = null;
        try
        {
            inputStream = postMethod.getResponseBodyAsStream();
        }
        catch( IOException e )
        {
            throw new ExactTargetException( "Error extracting body from post response", e );
        }
        Document responseDocument = null;
        try
        {
            responseDocument = new SAXBuilder().build( inputStream );
        }
        catch( JDOMException e )
        {
            throw new ExactTargetException( "Error parsing post response into xml document", e );
        }
        catch( IOException e )
        {
            throw new ExactTargetException( "Error parsing post response into xml document", e );
        }

        return new TriggeredEmailResponse( responseDocument );
    }
}
