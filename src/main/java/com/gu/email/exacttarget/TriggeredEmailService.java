package com.gu.email.exacttarget;

import com.gu.email.GuardianUser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TriggeredEmailService
{
    private final ExactTargetFactory soapFactory;
    private HttpClient httpClient;
    private static final Logger LOG = LoggerFactory.getLogger( TriggeredEmailResponse.class );


    public TriggeredEmailService( ExactTargetFactory soapFactory, HttpClient httpClient )
    {
        this.soapFactory = soapFactory;
        this.httpClient = httpClient;
    }


    public TriggeredEmailResponse sendEmailRequest(  GuardianUser user ) throws ExactTargetException
    {
        TriggeredEmailRequest triggeredEmailRequest = soapFactory.createRequest( user );
        PostMethod postMethod = soapFactory.createPostMethod( triggeredEmailRequest );

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
            LOG.debug( "Sending triggered email request:\n" + bodyString.toString() );
        }
        catch( IOException e )
        {
            LOG.error( "Couldn't create a debug dump of the triggered email request", e );
        }
    }
}
