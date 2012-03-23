package com.gu.email.exacttarget;

import org.jdom.Document;
import org.jdom.Element;

import static com.gu.email.exacttarget.Namespaces.*;


class SoapEnvelope extends Document
{
    public SoapEnvelope(Element header, Element body)
    {
        Element envelopeElement = new Element("Envelope", SOAP);
        envelopeElement.addNamespaceDeclaration( XSI );
        envelopeElement.addNamespaceDeclaration( XSD );
        envelopeElement.addNamespaceDeclaration( WSA );
        envelopeElement.addNamespaceDeclaration( WSSE );
        envelopeElement.addNamespaceDeclaration( WSU );

        envelopeElement.addContent( header );
        envelopeElement.addContent( body );

        addContent( envelopeElement );
    }


}
