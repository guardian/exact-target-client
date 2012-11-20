package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

// TODO: change name. This is the body of a post request. It wraps a SOAP envelope
class TriggeredEmailRequest extends ExactTargetRequest
{
    private final String emailTemplate; //Keep here
    private final String userName;     //Keep here
    private String soapAction;


    public TriggeredEmailRequest( AccountDetails account, String businessUnitId, String emailTemplate, GuardianUser user, String soapAction )
    {
        super(account, businessUnitId, user);

        this.soapAction = soapAction;
        this.userName = user.userName();
        this.emailTemplate = emailTemplate;// kee
        Document soapEnvelope = buildXmlMessage();//
        String soapEnvelopeString = xmlToString( soapEnvelope );

        try
        {
            delegate = new StringRequestEntity( soapEnvelopeString, "text/xml", "utf-8" );
        }
        catch( UnsupportedEncodingException ex )
        {
            throw new IllegalStateException( ex );
        }

    }

    public String getSoapAction()
    {
        return soapAction;
    }


    public String getEmailTemplate()
    {
        return emailTemplate;
    }


    public String getUserName()
    {
        return userName;
    }


    private Document buildXmlMessage()
    {
        return new SoapEnvelopeFactory().createMessage( this );
    }



}
