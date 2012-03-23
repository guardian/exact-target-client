package com.gu.email.exacttarget;

import org.jdom.Document;

public class TriggeredEmailResponse
{
    private final Document responseDocument;

    TriggeredEmailResponse( Document responseDocument )
    {
        this.responseDocument = responseDocument;
    }
}
