package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class EmailListForUserRequest extends ExactTargetRequest {

    public EmailListForUserRequest(AccountDetails accountDetails, String businessUnitId, GuardianUser guardianUser) {
        super(accountDetails, businessUnitId, guardianUser);
        Document soapEnvelope = buildXmlMessage();//
        String soapEnvelopeString = xmlToString( soapEnvelope );
        String s = "\n======================\n";
        System.out.println(String.format("%s%s%s", s, soapEnvelopeString, s  ));
        try
        {
            delegate = new StringRequestEntity( soapEnvelopeString, "text/xml", "utf-8" );
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