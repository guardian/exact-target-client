package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import java.net.URI;

public class ExactTargetSoapFactory
{
    private final String emailTemplate;
    private final String accountName;
    private final String password;
    private final URI endPoint;
    private String soapAction = "Create";

    // TODO: Are we going to need this?
    private String businessUnit = "";

    public ExactTargetSoapFactory( String accountName, String password, String emailTemplate, URI endPoint )
    {
        this.password = password;
        this.accountName = accountName;
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

    public TriggeredEmailRequest createRequest( String userName, String emailAddress )
    {
        AccountDetails accountDetails = new AccountDetails( accountName, password, businessUnit );
        GuardianUser registeredUser = new GuardianUser( userName, emailAddress );

        TriggeredEmailRequest triggeredRequest = new TriggeredEmailRequest( accountDetails, emailTemplate, registeredUser, soapAction );

        return triggeredRequest;
    }
}
