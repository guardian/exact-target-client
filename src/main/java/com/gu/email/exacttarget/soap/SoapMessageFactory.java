package com.gu.email.exacttarget.soap;

import com.gu.email.exacttarget.TriggeredEmailRequest;
import org.jdom.Document;

public class SoapMessageFactory
{
    public Document createMessage( TriggeredEmailRequest requestData )
    {
        ExactTargetSoapHeader header = new ExactTargetSoapHeader(
                requestData.getSoapAction(),
                requestData.getAccountName(),
                requestData.getPassword() );

        TriggeredSendSoapBody body = new TriggeredSendSoapBody(
                requestData.getEmailTemplate(),
                requestData.getUserName(),
                requestData.getEmailAddress() );

        return new SoapEnvelope( header, body );
    }
}
