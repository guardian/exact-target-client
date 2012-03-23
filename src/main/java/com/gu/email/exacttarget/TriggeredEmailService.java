package com.gu.email.exacttarget;

import com.gu.email.GuardianUser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

public class TriggeredEmailService
{
    private final ExactTargetFactory soapFactory;
    private HttpClient httpClient;

    public TriggeredEmailService( ExactTargetFactory soapFactory, HttpClient httpClient )
    {
        this.soapFactory = soapFactory;
        this.httpClient = httpClient;
    }

    public TriggeredEmailResponse sendEmailRequest(  GuardianUser user ) throws ExactTargetException
    {
        TriggeredEmailRequest triggeredEmailRequest = soapFactory.createRequest( user );
        PostMethod postMethod = soapFactory.createPostMethod( triggeredEmailRequest );

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
}
