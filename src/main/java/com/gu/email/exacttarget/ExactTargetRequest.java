package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


public abstract class ExactTargetRequest {

    protected final String businessUnitId;
    protected final String accountName;
    protected final String password;
    protected final String emailAddress;
    protected StringEntity delegate;

    public ExactTargetRequest(AccountDetails accountDetails, String businessUnitId, String emailAddress)
    {
        this.businessUnitId = businessUnitId;
        this.accountName = accountDetails.username();
        this.password = accountDetails.password();

        this.emailAddress = emailAddress;
    }

    protected String xmlToString( Document document )
    {
        Format format = Format.getCompactFormat();
        XMLOutputter outputter = new XMLOutputter( format );
        return outputter.outputString( document );
    }

    public String getPassword()
    {
        return password;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public String getBusinessUnitId() { return businessUnitId; }
}
