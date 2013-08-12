package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public class ExactTargetFactory
{
    private final URI endPoint;
    private final AccountDetails accountDetails;

    public ExactTargetFactory( String accountName, String password, URI endPoint )
    {
        accountDetails = new AccountDetails(accountName, password);
        this.endPoint = endPoint;
    }

    public PostMethod createPostMethod( RequestEntity body, String soapAction )
    {
        PostMethod method = new PostMethod( endPoint.toString() );

        method.setRequestHeader( "Host", endPoint.getHost() );
        method.setRequestHeader( "Content-Type", "text/xml; charset=utf-8" );
        method.setRequestHeader( "Content-Length", "" + body.getContentLength() );
        method.setRequestHeader( "SOAPAction", soapAction );
        method.setRequestEntity( body );

        return method;
    }

    public TriggeredEmailRequest createRequest( GuardianUser guardianUser, String soapAction, String businessUnitId, String emailTemplate)
    {
        TriggeredEmailRequest triggeredRequest = new TriggeredEmailRequest( accountDetails, businessUnitId, emailTemplate, guardianUser, soapAction );
        return triggeredRequest;
    }


    public EmailListForUserRequest createListForUserRequest( GuardianUser guardianUser, String businessUnitId)
    {
        EmailListForUserRequest emailListForUserRequest = new EmailListForUserRequest(accountDetails, businessUnitId, guardianUser);
        return emailListForUserRequest;
    }

    public TriggeredEmailResponse createResponseDocument( PostMethod postMethod ) throws ExactTargetException
    {
        Document responseDocument = getDocumentFromPostRequest(postMethod);
        return new TriggeredEmailResponse( responseDocument );
    }

    public EmailListForUserResponse createEmailListResponseDocument( PostMethod postMethod ) throws ExactTargetException
    {
        Document responseDocument = getDocumentFromPostRequest(postMethod);
        return new EmailListForUserResponse( responseDocument );
    }

    private Document getDocumentFromPostRequest(PostMethod postMethod) throws ExactTargetException {
        String bodyAsString  = null;
        try
        {
            bodyAsString = postMethod.getResponseBodyAsString();
        }
        catch( IOException e )
        {
            throw new ExactTargetException( "Error extracting body from post response", e );
        }
        Document responseDocument = null;
        try
        {
            responseDocument = new SAXBuilder().build( new ByteArrayInputStream(bodyAsString.getBytes()));
        }
        catch( JDOMException e )
        {
            throw new ExactTargetException( "DOM Error parsing post response into xml document " + e.getMessage(), e );
        }
        catch( IOException e )
        {
            throw new ExactTargetException( "I/O Error parsing post response into xml document", e );
        }
        return responseDocument;
    }
}