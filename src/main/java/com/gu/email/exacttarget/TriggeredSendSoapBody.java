package com.gu.email.exacttarget;

import org.jdom.Element;

import static com.gu.email.exacttarget.Namespaces.*;


class TriggeredSendSoapBody extends Element
{
    private String userName;
    private String emailAddress;
    private String emailTemplate;

    public TriggeredSendSoapBody( String emailTemplate, String userName, String emailAddress )
    {
        super( "Body", SOAP );
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.emailTemplate = emailTemplate;

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

    private Element attributes()
    {
        Element name = new Element( "Name", ET );
        name.setText( "Field_A" );

        Element value = new Element( "Value", ET );
        value.setText( userName );

        Element attributes = new Element( "Attributes", ET );
        attributes.addContent( name );
        attributes.addContent( value );

        return attributes;
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
