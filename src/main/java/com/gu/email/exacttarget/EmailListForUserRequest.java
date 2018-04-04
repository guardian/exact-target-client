package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import org.apache.http.entity.StringEntity;
import org.jdom.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class EmailListForUserRequest extends ExactTargetRequest {

    public EmailListForUserRequest(AccountDetails accountDetails, String businessUnitId, String emailAddress) {
        super(accountDetails, businessUnitId, emailAddress);
        Document soapEnvelope = buildXmlMessage();//
        String soapEnvelopeString = xmlToString( soapEnvelope );
        try
        {
            delegate = new StringEntity( soapEnvelopeString, "text/xml", "utf-8" );
        }
        catch( UnsupportedEncodingException ex )
        {
            throw new IllegalStateException( ex );
        }
    }

    private Document buildXmlMessage() {
        return new SoapEnvelopeFactory().createEmaiListMessage(this);
    }
}
