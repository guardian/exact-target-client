package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import com.gu.email.exacttarget.soap.SoapMessageFactory;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;


public class TriggeredEmailRequest implements RequestEntity
{
    private final String password;
    private final String accountName;
    private final String emailTemplate;
    private final String endPoint;
    private final String host;
    private final String emailAddress;
    private final String userName;
    private final String soapAction;

    private final RequestEntity delegate;

    TriggeredEmailRequest(AccountDetails account, String emailTemplate, GuardianUser user, URI endPoint, String soapAction)
    {
        this.accountName = account.username();
        this.password = account.password();

        this.userName = user.userName();
        this.emailAddress = user.email();

        this.endPoint = endPoint.toString();
        this.host = endPoint.getHost();

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

//    void setPassword( String password )
//    {
//        this.password = password;
//    }
//
//    void setAccountName( String accountName )
//    {
//        this.accountName = accountName;
//    }
//
//    void setEmailTemplate( String emailTemplate )
//    {
//        this.emailTemplate = emailTemplate;
//    }
//
//    void setEndPoint( String endPoint )
//    {
//        this.endPoint = endPoint;
//    }
//
//    void setHost( String host )
//    {
//        this.host = host;
//    }
//
//    void setEmailAddress( String emailAddress )
//    {
//        this.emailAddress = emailAddress;
//    }
//
//    void setUserName( String userName )
//    {
//        this.userName = userName;
//    }

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

    public String getEndPoint()
    {
        return endPoint;
    }

    public String getHost()
    {
        return host;
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
        return new SoapMessageFactory().createMessage( this );
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
