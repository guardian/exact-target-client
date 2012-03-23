package com.gu.email.exacttarget;

import org.apache.commons.httpclient.methods.PostMethod;

public class TriggeredEmailPostMethod extends PostMethod
{
    public TriggeredEmailPostMethod( String endPoint )
    {
        super( endPoint );
    }

    public TriggeredEmailResponseDocument getTriggeredEmailResponse()
    {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
