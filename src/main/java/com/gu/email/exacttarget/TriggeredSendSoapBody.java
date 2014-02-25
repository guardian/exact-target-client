package com.gu.email.exacttarget;

import org.jdom.Content;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gu.email.exacttarget.Namespaces.*;


class TriggeredSendSoapBody extends Element
{
    private Map<String, String> attributes;
    private String emailAddress;
    private String emailTemplate;
    private String businessUnitId;

    public TriggeredSendSoapBody( String businessUnitId, String emailTemplate, Map<String, String> attributes, String emailAddress )
    {
        super( "Body", SOAP );
        this.attributes = attributes;
        this.emailAddress = emailAddress;
        this.emailTemplate = emailTemplate;
        this.businessUnitId = businessUnitId;

        Element createRequest = new Element( "CreateRequest", ET );

        createRequest.addContent( new Element( "Options", ET ) );
        createRequest.addContent( objects() );

        addContent( createRequest );
    }

    private Element objects()
    {
        Element objects = new Element( "Objects", ET );
        objects.setAttribute( "type", "TriggeredSend", XSI );

        objects.addContent( partnerKey() );
        objects.addContent( objectId() );
        objects.addContent( businessUnitID() );
        objects.addContent( triggeredSendDefinition() );
        objects.addContent( subscribers() );
        return objects;
    }

    private Element triggeredSendDefinition()
    {
        Element customerKeyElement = new Element( "CustomerKey", ET );
        customerKeyElement.setText( emailTemplate );

        Element triggeredSendDefinition = new Element( "TriggeredSendDefinition", ET );
        triggeredSendDefinition.addContent( partnerKey() );
        triggeredSendDefinition.addContent( objectId() );
        triggeredSendDefinition.addContent( customerKeyElement );

        return triggeredSendDefinition;
    }

    private Content businessUnitID()
    {
        Element client = new Element( "Client", ET );
        Element id = new Element( "ID", ET );
        id.setText( businessUnitId );

        client.addContent( id );

        return client;
    }

    private Element subscribers()
    {
        Element emailAddressElement = new Element( "EmailAddress", ET );
        emailAddressElement.setText( emailAddress );

        Element subscribers = new Element( "Subscribers", ET );
        subscribers.addContent( partnerKey() );
        subscribers.addContent( objectId() );
        subscribers.addContent( emailAddressElement );
        subscribers.addContent( subscriberKey() );
        subscribers.addContent( attributes() );

        return subscribers;
    }

    private List<Element> attributes()
    {
        List<Element> elements = new ArrayList<Element>();

        for (Map.Entry<String, String> stringStringEntry : this.attributes.entrySet()) {
            Element name = new Element( "Name", ET );
            name.setText( stringStringEntry.getKey());

            Element value = new Element( "Value", ET );
            value.setText( stringStringEntry.getValue() );

            Element attributesElement = new Element( "Attributes", ET );
            attributesElement.addContent( name );
            attributesElement.addContent( value );

            elements.add(attributesElement);
        }


        return elements;
    }

    private Element subscriberKey()
    {
        Element subscriberKey = new Element( "SubscriberKey", ET );
        subscriberKey.setText( emailAddress );

        return subscriberKey;
    }

    private Element partnerKey()
    {
        Element partnerKey = new Element( "PartnerKey", ET );
        partnerKey.setAttribute( "nil", "true", XSI );
        return partnerKey;
    }

    private Element objectId()
    {
        Element objectId = new Element( "ObjectID", ET );
        objectId.setAttribute( "nil", "true", XSI );
        return objectId;
    }

}
