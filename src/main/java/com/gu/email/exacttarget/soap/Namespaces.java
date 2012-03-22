package com.gu.email.exacttarget.soap;

import org.jdom.Namespace;

import static org.jdom.Namespace.getNamespace;

class Namespaces
{
    public static final Namespace SOAP = getNamespace("soap", "http://schemas.xmlsoap.org/soap/envelope/");
    public static final Namespace XSI = getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    public static final Namespace XSD = getNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
    public static final Namespace WSA = getNamespace("wsa", "http://schemas.xmlsoap.org/ws/2004/08/addressing");
    public static final Namespace WSSE = getNamespace("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
    public static final Namespace WSU = getNamespace("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
    public static final Namespace ET = getNamespace( "http://exacttarget.com/wsdl/partnerAPI" );

}
