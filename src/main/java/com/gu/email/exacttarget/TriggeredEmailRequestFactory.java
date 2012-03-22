package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;

import java.net.URI;

public class TriggeredEmailRequestFactory
{
    private final String emailTemplate;
    private final String accountName;
    private final String password;
    private final URI endPoint;
    private String soapAction = "Create";

    // TODO: Are we going to need this?
    private String businessUnit = "";

    public TriggeredEmailRequestFactory( String accountName, String password, String emailTemplate, URI endPoint )
    {
        this.password = password;
        this.accountName = accountName;
        this.emailTemplate = emailTemplate;
        this.endPoint = endPoint;
    }

    public TriggeredEmailRequest createRequest( String userName, String emailAddress )
    {
        AccountDetails accountDetails = new AccountDetails(accountName, password, businessUnit);
        GuardianUser registeredUser = new GuardianUser(userName, emailAddress);

        TriggeredEmailRequest triggeredRequest = new TriggeredEmailRequest(accountDetails, emailTemplate, registeredUser, endPoint, soapAction);

//        triggeredRequest.setPassword( password );
//        triggeredRequest.setAccountName( accountName );
//        triggeredRequest.setEmailTemplate( emailTemplate );
//        triggeredRequest.setEndPoint( endPoint.toString() );
//        triggeredRequest.setHost( endPoint.getHost() );
//        triggeredRequest.setEmailAddress( emailAddress );
//        triggeredRequest.setUserName( userName );

        return triggeredRequest;
    }
}
