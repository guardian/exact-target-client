package com.gu.email.exacttarget;

import org.jdom.Element;
import org.jdom.Namespace;


class ExactTargetSoapHeader extends Element
{
    public ExactTargetSoapHeader( String actionName, String accountName, String password )
    {
        super( "Header", Namespaces.SOAP );

        Element action = new Element( "Action", Namespaces.WSA );
        action.setText( actionName );

        Element messageId = new Element( "MessageID", Namespaces.WSA );
        messageId.setText( "urn:uuid:bd9bc23b-42da-4c2e-b4c1-b1e822f520a6" );

        Element to = new Element( "To", Namespaces.WSA );
        to.setText( "https://webservice.s4.exacttarget.com/Service.asmx" );

        addContent( action );
        addContent( messageId );
        addContent( replyTo() );
        addContent( to );
        addContent( security( accountName, password ) );
    }

    public ExactTargetSoapHeader ( String accountName, String password )
    {
        super("Header", Namespaces.SOAP );

//
//        Element action = new Element( "Action", Namespaces.WSA );
//        action.setText( "GetSystemStatus" );

        addContent( security(accountName, password));
    }

    private Element security( String accountName, String password )
    {
        Element security = new Element( "Security", Namespaces.WSSE );
        security.setAttribute( "mustUnderstand", "1", Namespaces.SOAP );

        Element usernameToken = new Element( "UsernameToken", Namespaces.WSSE );
        usernameToken.setAttribute( "Id", "SecurityToken-884da619-59bb-4db6-834d-138322342442", Namespaces.WSU );

        Element username = new Element( "Username", Namespaces.WSSE );
        username.setText( accountName );

        Element passwordElement = new Element( "Password", Namespaces.WSSE );
        passwordElement.setAttribute( "Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText" );
        passwordElement.setText( password );

        usernameToken.addContent( username );
        usernameToken.addContent( passwordElement );

        security.addContent( usernameToken );
        return security;
    }

    private Element replyTo()
    {
        Element replyTo = new Element( "ReplyTo", Namespaces.WSA );
        Element address = new Element( "Address", Namespaces.WSA );
        address.setText( "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous" );
        replyTo.addContent( address );

        return replyTo;
    }
}
