package com.gu.email.exacttarget;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.gu.email.exacttarget.Namespaces.ET;
import static com.gu.email.exacttarget.Namespaces.SOAP;

public class TriggeredEmailResponse
{
    private String statusCode;
    private String statusMessage;
    private String requestId;
    private String overallStatus;
    private final Document responseDocument;

    private static final Logger LOG = LoggerFactory.getLogger( TriggeredEmailResponse.class );


    TriggeredEmailResponse( Document responseDocument )
    {
        this.responseDocument = responseDocument;

        try
        {
            Element rootElement = responseDocument.getRootElement();
            Element body = rootElement.getChild( "Body", SOAP );
            Element createResponse = body.getChild( "CreateResponse", ET );
            Element results = createResponse.getChild( "Results", ET );

            this.statusCode = results.getChildText( "StatusCode", ET );
            this.statusMessage = results.getChildText( "StatusMessage", ET );
            this.requestId = createResponse.getChildText( "RequestID", ET );
            this.overallStatus = createResponse.getChildText( "OverallStatus", ET );
        }
        catch( Exception e )
        {
            LOG.error( "Error parsing triggered email response", e );

            this.statusCode = "ERROR";
            this.statusMessage = "Unable to parse response document";
            this.requestId = "";
            this.overallStatus = "ERROR";
        }
    }

    public String getRequestId()
    {
        return requestId;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public String getStatusCode()
    {
        return statusCode;
    }

    public String getOverallStatus()
    {
        return overallStatus;
    }

    public boolean isStatusOk()
    {
        return statusCode.toLowerCase().equals("ok");
    }

    public boolean isOverallStatusOk()
    {
        return overallStatus.toLowerCase().equals("ok");
    }

    public String getDocumentAsString()
    {
        XMLOutputter outputter = new XMLOutputter( Format.getPrettyFormat() );
        OutputStream outputStream = new ByteArrayOutputStream();

        try
        {
            outputter.output( responseDocument, outputStream );
        }
        catch( IOException e )
        {
            LOG.error( "Error outputting triggered email response", e );
            return "Could not parse response document: " + e.getMessage();
        }

        return outputStream.toString();
    }
}
