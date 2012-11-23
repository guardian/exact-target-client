package com.gu.email.exacttarget;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import static com.gu.email.exacttarget.Namespaces.*;


class EmailListForUserRequestSoapBody extends Element {
    private final String businessUnitId;
    private final String emailAddress;

    public EmailListForUserRequestSoapBody(String businessUnitId, String emailAddress) {

        super("Body", SOAP );
        this.businessUnitId = businessUnitId;
        this.emailAddress = emailAddress;

        Element retrieveRequestMsg = new Element("RetrieveRequestMsg", ET);
        retrieveRequestMsg.addContent(retrieveRequest());
        addContent(retrieveRequestMsg);
    }


    private Element retrieveRequest() {
        Element retrieveRequest = new Element("RetrieveRequest", ET);
        retrieveRequest.addContent(new Element("ObjectType", ET).setText("ListSubscriber") );
        retrieveRequest.addContent(new Element("Properties", ET).setText("SubscriberKey") );
        retrieveRequest.addContent(new Element("Properties",ET).setText("ListID") );
        retrieveRequest.addContent(new Element("Properties", ET).setText("Status") );
        retrieveRequest.addContent( filter() );
        retrieveRequest.addContent(new Element("QueryAllAccounts", ET).setText("true") );

        return retrieveRequest;
    }

    private Element filter() {
        Element filter = new Element("Filter", ET);
        filter.addNamespaceDeclaration(ETNS);
        filter.setAttribute( "type", "ns1:SimpleFilterPart", XSI );

        filter.addContent(new Element("Property", ETNS).setText("SubscriberKey"));
        filter.addContent(new Element("SimpleOperator",ETNS).setText("equals"));
        filter.addContent(new Element("Value",ETNS).setText(emailAddress));

        return filter;
    }

}
