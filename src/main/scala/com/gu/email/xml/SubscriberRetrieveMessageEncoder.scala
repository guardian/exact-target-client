package com.gu.email.xml

import com.gu.email.{SubscriberResult, Subscriber, MessageEncoder}
import xml.NodeSeq
import org.joda.time.format.ISODateTimeFormat

class SubscriberRetrieveMessageEncoder extends MessageEncoder[String, Response[Subscriber]] {
  def encodeRequest(subscriberKey: String) = {
    <s:Envelope xmlns:a="http://schemas.xmlsoap.org/ws/2004/08/addressing" xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
      <s:Header>
        <a:Action s:mustUnderstand="1">Retrieve</a:Action>
        <a:MessageID>urn:uuid:12afdce4-062d-45e9-98b6-d221ace9cc95</a:MessageID>
        <ActivityId CorrelationId="007731bc-31c3-40a2-8fbe-108de7783a57" xmlns="http://schemas.microsoft.com/2004/09/ServiceModel/Diagnostics">8d3425a9-1d88-4af3-a987-ea52e660437a</ActivityId>
        <a:ReplyTo>
          <a:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:Address>
        </a:ReplyTo>
      </s:Header>
      <s:Body xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
        <RetrieveRequestMsg xmlns="http://exacttarget.com/wsdl/partnerAPI">
          <RetrieveRequest>
            <ObjectType>Subscriber</ObjectType>
            <Properties>ID</Properties>
            <Properties>CreatedDate</Properties>
            <Properties>Client.ID</Properties>
            <Properties>EmailAddress</Properties>
            <Properties>SubscriberKey</Properties>
            <Properties>UnsubscribedDate</Properties>
            <Properties>Status</Properties>
            <Properties>EmailTypePreference</Properties>
            <Filter xmlns:q1="http://exacttarget.com/wsdl/partnerAPI" xsi:type="q1:SimpleFilterPart">
              <q1:Property>SubscriberKey</q1:Property>
              <q1:SimpleOperator>equals</q1:SimpleOperator>
              <q1:Value>{subscriberKey}</q1:Value>
            </Filter>
          </RetrieveRequest>
        </RetrieveRequestMsg>
      </s:Body>
    </s:Envelope>

  }


  val dateTimeParser = ISODateTimeFormat.dateOptionalTimeParser()
  def decodeResponse(response: NodeSeq) = {

    val results = response \\ "RetrieveResponseMsg"
    Response(results \\ "OverallStatus" text,
      Subscriber(
        results \\ "SubscriberKey" text,
        (results \\ "Attributes") find( attribute => ( attribute \\ "Name" text ) == "First Name" ) map( _ \\ "Value" text),
        (results \\ "Attributes") find( attribute => ( attribute \\ "Name" text ) == "Last Name" ) map( _ \\ "Value" text),
        results \\ "CreatedDate" map( _.text ) headOption,
        None,
        (results \\ "Status").headOption.map(_.text),
        (results \\ "EmailTypePreference").headOption.map(_.text)
      )
    )
  }
}
