package com.gu.email.exacttarget;

import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import sun.beans.editors.StringEditor;

import java.util.ArrayList;
import java.util.List;

import java.util.Iterator;

import static com.gu.email.exacttarget.Namespaces.ET;
import static com.gu.email.exacttarget.Namespaces.SOAP;

//TODO superclass
public class EmailListForUserResponse {

    private Document responseDocument;
    private  String overallStatus;
    private final List<String> emailListIds = new ArrayList<String>();
    private final static String RESULTS_ELEMENT_NAME = "Results";
    private String statusCode = "";

   // private final static String STATUS_ELEMENT_NAME = "Results";
    private static final Logger LOG = LoggerFactory.getLogger(EmailListForUserResponse.class);

    public EmailListForUserResponse(Document responseDocument) {

        this.responseDocument = responseDocument;

        try
        {

            Element rootElement = responseDocument.getRootElement();
            Element body = rootElement.getChild("Body", SOAP);
            Element retrieveResponseMessage = body.getChild("RetrieveResponseMsg", ET);
            this.overallStatus = retrieveResponseMessage.getChildText("OverallStatus", ET);
            List children = retrieveResponseMessage.getChildren();

            for ( Iterator<Element> iterator = children.iterator(); iterator.hasNext() ; ) {
                Element element = iterator.next();
                if ( element.getName().equals(RESULTS_ELEMENT_NAME)) {
                    emailListIds.add(element.getChildText("ListID", ET));//FK
                }
            }
        }
        catch ( Exception e )
        {
            LOG.error("Error parsing email list response document", e);
            this.statusCode = "ERROR";
            this.overallStatus = "ERROR";
        }
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public List<String> getEmalListIds() {
        return emailListIds;
    }

    public boolean isOverallStatusOk() {
        return overallStatus.toLowerCase().equals("ok");
    }

    public String getStatusCode() {
        return statusCode;
    }
}
