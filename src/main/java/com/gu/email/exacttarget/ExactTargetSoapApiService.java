package com.gu.email.exacttarget;

import com.gu.email.GuardianUser;
import com.gu.email.exacttarget.util.ExactTargetUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ExactTargetSoapApiService
{
    private final ExactTargetFactory soapFactory;
    private HttpClient httpClient;
    private static final Logger LOG = LoggerFactory.getLogger( TriggeredEmailResponse.class );
    private String createSoapAction = "Create";
    private String retrieveSoapAction = "Retrieve";


    public ExactTargetSoapApiService(ExactTargetFactory soapFactory, HttpClient httpClient)
    {
        this.soapFactory = soapFactory;
        this.httpClient = httpClient;
    }


    public TriggeredEmailResponse sendEmailRequest( String userName, String emailAddress ) throws ExactTargetException
    {
        GuardianUser user = new GuardianUser( userName, emailAddress );
        return sendEmailRequest( user );
    }


    public EmailListForUserResponse getEmailRequestsForUser(String userName, String emailAddress) throws  ExactTargetException {
        GuardianUser guardianUser = new GuardianUser(userName, emailAddress);
        return getEmailRequestsForUser(guardianUser);
    }

    EmailListForUserResponse getEmailRequestsForUser(GuardianUser guardianUser) throws  ExactTargetException {

        EmailListForUserRequest emailListForUserRequest = soapFactory.createListForUserRequest(guardianUser);
        PostMethod postMethod = soapFactory.createPostMethod(emailListForUserRequest, retrieveSoapAction);

        if ( LOG.isDebugEnabled() ) {
            dumpRequestBody(postMethod);
        }

        try {
            int resposeCode = httpClient.executeMethod(postMethod);
            if ( resposeCode < 200 || resposeCode >= 300)  {
                throw new ExactTargetException(String.format("Recieved non 200 response retrieving email lists for: %s", guardianUser.email()));
            }
            String s = postMethod.getResponseBodyAsString();
            return soapFactory.createEmailListResponseDocument(postMethod);
        }
        catch (IOException iox )
        {
            throw new ExactTargetException("Error sending post request for lists for user", iox);
        }


    }

    TriggeredEmailResponse sendEmailRequest(  GuardianUser user ) throws ExactTargetException
    {
        TriggeredEmailRequest triggeredEmailRequest = soapFactory.createRequest( user, createSoapAction );
        PostMethod postMethod = soapFactory.createPostMethod( triggeredEmailRequest, createSoapAction );

        if( LOG.isDebugEnabled() )
        {
            dumpRequestBody( postMethod );
        }

        try
        {
            int responseCode = httpClient.executeMethod( postMethod );
            if( responseCode < 200 || responseCode >= 300 )
            {
                throw new ExactTargetException( "Received non 200 response: " + responseCode + "\n" + postMethod.getResponseBodyAsString() );
            }
            return soapFactory.createResponseDocument( postMethod );
        }
        catch( IOException e )
        {
            throw new ExactTargetException( "Error sending post request", e );
        }
    }

    private void dumpRequestBody( PostMethod postMethod )
    {
        OutputStream bodyString = new ByteArrayOutputStream();
        try
        {
            postMethod.getRequestEntity().writeRequest( bodyString );
            LOG.info("Sending triggered email request:\n" + bodyString.toString());
        }
        catch( IOException e )
        {
            LOG.error( "Couldn't create a debug dump of the triggered email request", e );
        }
    }

}