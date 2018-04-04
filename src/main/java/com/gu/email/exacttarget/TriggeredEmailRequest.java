package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import org.apache.http.entity.StringEntity;
import org.jdom.Document;

import java.io.UnsupportedEncodingException;
import java.util.Map;

// TODO: change name. This is the body of a post request. It wraps a SOAP envelope
public class TriggeredEmailRequest extends ExactTargetRequest
{
    private final String emailTemplate; //Keep here
    private final Map<String, String> attributes;
    private String soapAction;


    public TriggeredEmailRequest( AccountDetails account, String businessUnitId, String emailTemplate, String emailAddress, Map<String, String> attributes, String soapAction )
    {
        super(account, businessUnitId, emailAddress);

        this.soapAction = soapAction;
        this.attributes = attributes;
        this.emailTemplate = emailTemplate;

        try
        {
            delegate = new StringEntity(getSoapEnvelopeString(), "text/xml", "utf-8" );
        }
        catch( UnsupportedEncodingException ex )
        {
            throw new IllegalStateException( ex );
        }

    }

    public String getSoapEnvelopeString() {
        return xmlToString(buildXmlMessage());
    }

    public String getSoapAction()
    {
        return soapAction;
    }


    public String getEmailTemplate()
    {
        return emailTemplate;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    private Document buildXmlMessage()
    {
        return new SoapEnvelopeFactory().createMessage( this );
    }



}
