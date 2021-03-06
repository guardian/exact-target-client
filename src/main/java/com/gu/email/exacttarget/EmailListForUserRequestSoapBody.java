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
        retrieveRequest.addContent(new Element("ClientIDs", ET).addContent(new Element("ID", ET).setText(businessUnitId)));
        retrieveRequest.addContent(new Element("ObjectType", ET).setText("ListSubscriber") );
        retrieveRequest.addContent(new Element("Properties", ET).setText("SubscriberKey") );
        retrieveRequest.addContent(new Element("Properties",ET).setText("ListID") );
        retrieveRequest.addContent(new Element("Properties", ET).setText("Status") );
        retrieveRequest.addContent( filter() );

        return retrieveRequest;
    }

    private Element filter() {
        Element andFilter = new Element("Filter", ET);
        andFilter.addNamespaceDeclaration(ETNS);
        andFilter.setAttribute("type", "ns1:ComplexFilterPart", XSI);


        Element subsrciberKeyFilter = new Element("LeftOperand", ET);
        subsrciberKeyFilter.setAttribute("type", "ns1:SimpleFilterPart", XSI);
        subsrciberKeyFilter.addContent(new Element("Property", ETNS).setText("SubscriberKey"));
        subsrciberKeyFilter.addContent(new Element("SimpleOperator", ETNS).setText("equals"));
        subsrciberKeyFilter.addContent(new Element("Value", ETNS).setText(emailAddress));

        Element operater = new Element("LogicalOperator", ET);
        operater.setText("AND");

        Element statusFilter = new Element("RightOperand", ET);
        statusFilter.setAttribute("type", "ns1:SimpleFilterPart", XSI);
        statusFilter.addContent(new Element("Property", ETNS).setText("Status"));
        statusFilter.addContent(new Element("SimpleOperator", ETNS).setText("equals"));
        statusFilter.addContent(new Element("Value", ETNS).setText("Active"));

        andFilter.addContent(subsrciberKeyFilter);
        andFilter.addContent(operater);
        andFilter.addContent(statusFilter);

        return andFilter;
    }

}
