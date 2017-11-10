package com.gu.email.xml

import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import org.scalatest.Matchers
import xml.Utility
import com.gu.email.{AccountDetails, Subscriber}
import org.joda.time.DateTime

class SubscriberRetrieveMessageEncoderTest extends FlatSpec with MockitoSugar with Matchers {
  val encoder = new SubscriberRetrieveMessageEncoder(AccountDetails("aUsername", "aPassword"))

  "SubscriberRetrieveMessageEncoder" should "encode subscriber update message" in {
    val time = new DateTime()

    Utility.trim(encoder.encodeRequest("foo@foo.com")) should equal(Utility.trim(
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        <soapenv:Header>
          <wsse:Security soapenv:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
            <wsse:UsernameToken wsu:Id="UsernameToken-24440876" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
              <wsse:Username>aUsername</wsse:Username>
              <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">aPassword</wsse:Password>
            </wsse:UsernameToken>
          </wsse:Security>
        </soapenv:Header>
        <soapenv:Body>
          <RetrieveRequestMsg xmlns="http://exacttarget.com/wsdl/partnerAPI">
            <RetrieveRequest>
              <ObjectType>Subscriber</ObjectType>
              <ObjectType>Subscriber</ObjectType>
              <Properties>ID</Properties>
              <Properties>CreatedDate</Properties>
              <Properties>Client.ID</Properties>
              <Properties>EmailAddress</Properties>
              <Properties>SubscriberKey</Properties>
              <Properties>UnsubscribedDate</Properties>
              <Properties>Status</Properties>
              <Properties>EmailTypePreference</Properties>
              <Filter xsi:type="ns1:SimpleFilterPart" xmlns:ns1="http://exacttarget.com/wsdl/partnerAPI">
                <Property>SubscriberKey</Property>
                <SimpleOperator>equals</SimpleOperator>
                <Value>foo@foo.com</Value>
              </Filter>
            </RetrieveRequest>
          </RetrieveRequestMsg>
        </soapenv:Body>
      </soapenv:Envelope>

    ))
  }

  "SubscriberRetrieveMessageEncoder" should "decode subscriber update response" in {
    encoder.decodeResponse(
      <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
        <soap:Header>
          <wsa:Action>RetrieveResponse</wsa:Action> <wsa:MessageID>urn:uuid:4b7b7024-42cb-47ee-a7d6-72b1caee8f81</wsa:MessageID> <wsa:RelatesTo>urn:uuid:d1109d06-8c91-40b6-bfd5-de22adc90032</wsa:RelatesTo> <wsa:To>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:To> <wsse:Security>
          <wsu:Timestamp wsu:Id="Timestamp-d2cb68d0-e797-435c-aad4-c47b81c20d94">
            <wsu:Created>2012-12-10T17:11:22Z</wsu:Created> <wsu:Expires>2012-12-10T17:16:22Z</wsu:Expires>
          </wsu:Timestamp>
        </wsse:Security>
        </soap:Header>
        <soap:Body>
          <RetrieveResponseMsg xmlns="http://exacttarget.com/wsdl/partnerAPI">
            <OverallStatus>OK</OverallStatus>
            <RequestID>d70b9d8e-194e-4c53-8c5f-bd9194f30bf3</RequestID>
            <Results xsi:type="Subscriber">
            <Client>
              <ID>1058742</ID>
            </Client>
            <PartnerKey xsi:nil="true"/>
            <CreatedDate>2012-12-07T06:57:00</CreatedDate>
            <ID>5797215</ID>
            <ObjectID xsi:nil="true"/>
            <EmailAddress>foo@blah.com</EmailAddress>
            <Attributes>
              <Name>First Name</Name>
              <Value>foo</Value>
            </Attributes>
            <Attributes>
              <Name>Last Name</Name>
              <Value>blah</Value>
            </Attributes>
            <SubscriberKey>foo@blah.com</SubscriberKey>
            <Status>Active</Status>
            <EmailTypePreference>HTML</EmailTypePreference>
          </Results>
        </RetrieveResponseMsg>
      </soap:Body>
      </soap:Envelope>
    ) should equal(
      Response(
        "OK",
        None,
        None,
        Subscriber("foo@blah.com",
          Some("foo"),
          Some("blah"),
          Some("2012-12-07T06:57:00"),
          None,
          Some("Active"),
          Some("HTML")
        ))
    )
  }
}
