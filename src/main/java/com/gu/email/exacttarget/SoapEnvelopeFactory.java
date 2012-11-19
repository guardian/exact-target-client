package com.gu.email.exacttarget;

import org.jdom.Document;

class SoapEnvelopeFactory
{
    public Document createMessage( TriggeredEmailRequest requestData )
    {
        ExactTargetSoapHeader header = new ExactTargetSoapHeader(
                requestData.getSoapAction(),
                requestData.getAccountName(),
                requestData.getPassword() );

        TriggeredSendSoapBody body = new TriggeredSendSoapBody(
                requestData.getBusinessUnitId(),
                requestData.getEmailTemplate(),
                requestData.getUserName(),
                requestData.getEmailAddress() );

        return new SoapEnvelope( header, body );
    }

    public Document createEmaiListMessage( EmailListForUserRequest requestData ) {

        ExactTargetSoapHeader header = new ExactTargetSoapHeader(
                requestData.getAccountName(),
                requestData.getPassword() );

        EmailListForUserRequestSoapBody body = new EmailListForUserRequestSoapBody(
                requestData.getBusinessUnitId(),
                requestData.getEmailAddress()
        );

        return new SoapEnvelope(header, body);
    }
}
