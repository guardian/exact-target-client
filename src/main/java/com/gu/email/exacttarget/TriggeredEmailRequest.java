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
class TriggeredEmailRequest implements RequestEntity
{
    private final String password;
    private final String accountName;
    private final String emailTemplate;
    private final String emailAddress;
    private final String userName;
    private final String soapAction;

    private final RequestEntity delegate;

    public TriggeredEmailRequest( AccountDetails account, String emailTemplate, GuardianUser user, String soapAction )
    {
        this.accountName = account.username();
        this.password = account.password();

        this.userName = user.userName();
        this.emailAddress = user.email();

        this.emailTemplate = emailTemplate;

        this.soapAction = soapAction;

        Document soapEnvelope = buildXmlMessage();
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

    public String getPassword()
    {
        return password;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public String getEmailTemplate()
    {
        return emailTemplate;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getSoapAction()
    {
        return soapAction;
    }

    private Document buildXmlMessage()
    {
        return new SoapEnvelopeFactory().createMessage( this );
    }

    private String xmlToString( Document document )
    {
        Format format = Format.getCompactFormat();
        XMLOutputter outputter = new XMLOutputter( format );
        return outputter.outputString( document );
    }

    @Override
    public boolean isRepeatable()
    {
        return delegate.isRepeatable();
    }

    @Override
    public void writeRequest( OutputStream out ) throws IOException
    {
        delegate.writeRequest( out );
    }

    @Override
    public long getContentLength()
    {
        return delegate.getContentLength();
    }

    @Override
    public String getContentType()
    {
        return delegate.getContentType();
    }
}
