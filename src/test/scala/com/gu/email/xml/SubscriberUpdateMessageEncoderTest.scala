package com.gu.email.xml

import org.scalatest.FlatSpec
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.matchers.ShouldMatchers
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.PostMethod
import xml.{Utility, XML, NodeSeq}
import com.gu.email.{SubscriberResult, EmailList, Subscriber, AccountDetails}
import org.joda.time.DateTime
import io.Source

class SubscriberUpdateMessageEncoderTest extends FlatSpec with MockitoSugar with ShouldMatchers{
  val encoder = new SubscriberUpdateMessageEncoder

  "SubscriberUpdateMessageEncoder" should "encode subscriber update message" in {
    val time = new DateTime()
    val update = new SubscriberUpdateRequest(
      Some("aBusinessUnitId"),
      AccountDetails("ausername", "apassword"),
      List(
        Subscriber("anEmailAddress", Some("aFirstName"), Some("aSecondName"), None, None, Some("aStatus"),
          Some("anEmailPreference"), List(EmailList("aListId", "aStatus")))
      )
    )

    Utility.trim(encoder.encodeRequest(update)) should equal(Utility.trim(
      <soap:Envelope xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" xmlns:ns1="http://exacttarget.com/wsdl/partnerAPI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
        <soap:Header>
          <wsa:Action>Create</wsa:Action>
          <wsa:MessageID>urn:uuid:168bbf3d-394e-4656-ae57-sdfsdfb4b568ae</wsa:MessageID>
          <wsa:ReplyTo>
            <wsa:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:Address>
          </wsa:ReplyTo>
          <wsa:To>https://webservice.s4.exacttarget.com/Service.asmx</wsa:To>
          <wsse:Security soap:mustUnderstand="1">
            <wsse:UsernameToken xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
              <wsse:Username>ausername</wsse:Username>
              <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">apassword</wsse:Password>
            </wsse:UsernameToken>
          </wsse:Security>
        </soap:Header>
        <soap:Body>
          <CreateRequest xmlns="http://exacttarget.com/wsdl/partnerAPI">
            <Options>
              <SaveOptions>
                <SaveOption>
                  <PropertyName>*</PropertyName>
                  <SaveAction>UpdateAdd</SaveAction>
                </SaveOption>
              </SaveOptions>
            </Options>
            <Objects xsi:type="Subscriber">
              <Client>
                <ID>aBusinessUnitId</ID>
              </Client>
              <ObjectID xsi:nil="true">
              </ObjectID>
              <EmailAddress>anEmailAddress</EmailAddress>
              <SubscriberKey>anEmailAddress</SubscriberKey>
              <Lists>
                <ID>aListId</ID>
                <Status>aStatus</Status>
                <ObjectID xsi:nil="true">
                </ObjectID>
              </Lists>
              <Attributes>
                <Name>First Name</Name>
                <Value>aFirstName</Value>
              </Attributes>
              <Attributes>
                <Name>Last Name</Name>
                <Value>aSecondName</Value>
              </Attributes>
            </Objects>
          </CreateRequest>
        </soap:Body>
      </soap:Envelope> )
    )
  }

  "SubscriberUpdateMessageEncoder" should "decode subscriber update response" in {
    encoder.decodeResponse(
      <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
        <soap:Header>
          <wsa:Action>CreateResponse</wsa:Action>
          <wsa:MessageID>urn:uuid:23e96e00-d17f-4479-869c-097628e42d35</wsa:MessageID>
          <wsa:RelatesTo>urn:uuid:168bbf3d-394e-4656-ae57-sdfsdfb4b568ae</wsa:RelatesTo>
          <wsa:To>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:To>
          <wsse:Security>
            <wsu:Timestamp wsu:Id="Timestamp-858a2d85-68dc-48cf-979c-a219a4e5993c">
              <wsu:Created>2012-12-10T16:19:10Z</wsu:Created>
              <wsu:Expires>2012-12-10T16:24:10Z</wsu:Expires>
            </wsu:Timestamp>
          </wsse:Security>
        </soap:Header>
        <soap:Body>
          <CreateResponse xmlns="http://exacttarget.com/wsdl/partnerAPI">
            <Results>
              <StatusCode>OK</StatusCode>
              <StatusMessage>Created Subscriber.</StatusMessage>
              <OrdinalID>0</OrdinalID>
              <NewID>5797215</NewID>
              <Object xsi:type="Subscriber">
                <Client>
                  <ID>1059028</ID>
                </Client>
                <PartnerKey xsi:nil="true" />
                <ID>5797215</ID>
                <ObjectID xsi:nil="true" />
                <EmailAddress>13219403-francis@rhys-jones.com</EmailAddress>
                <SubscriberKey>13219403-francis@rhys-jones.com</SubscriberKey>
                <Lists>
                  <PartnerKey xsi:nil="true" />
                  <ID>217</ID>
                  <ObjectID xsi:nil="true" />
                  <Status>Unsubscribed</Status>
                </Lists>
              </Object>
            </Results>
            <RequestID>3cd7437f-0d28-4819-886f-91d4f385c3d2</RequestID>
            <OverallStatus>OK</OverallStatus>
          </CreateResponse>
        </soap:Body>
      </soap:Envelope>
    ) should equal( List(SubscriberResult("13219403-francis@rhys-jones.com", "OK", "Created Subscriber.", "" )) )
  }
}
