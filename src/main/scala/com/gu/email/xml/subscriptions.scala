package com.gu.email.xml

import com.gu.email.{SubscriberResult, AccountDetails, Subscriber}
import xml.NodeSeq

case class SubscriberUpdateRequest(businessUnitId: Option[String], accountDetails: AccountDetails, subscribers: Seq[Subscriber])

class SubscriberUpdateMessageEncoder extends MessageEncoder[SubscriberUpdateRequest, Seq[SubscriberResult]] {
  def encodeRequest(request: SubscriberUpdateRequest) = {
    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                   xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing"
                   xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
                   xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
                   xmlns:ns1="http://exacttarget.com/wsdl/partnerAPI">
      <soap:Header>
        <wsa:Action>Create</wsa:Action>
        <wsa:MessageID>urn:uuid:168bbf3d-394e-4656-ae57-sdfsdfb4b568ae</wsa:MessageID>
        <wsa:ReplyTo>
          <wsa:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:Address>
        </wsa:ReplyTo>
        <wsa:To>https://webservice.s4.exacttarget.com/Service.asmx</wsa:To>
        <wsse:Security soap:mustUnderstand="1">
          <wsse:UsernameToken xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
            <wsse:Username>
              {request.accountDetails.username}
            </wsse:Username>
            <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">
              {request.accountDetails.password}
            </wsse:Password>
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
          </Options>{request.subscribers map {
          subscriber => encodeSubscriber(request.businessUnitId, subscriber)
        }}
        </CreateRequest>
      </soap:Body>
    </soap:Envelope>

  }

  def encodeSubscriber(businessUnitId: Option[String], subscriber: Subscriber) = {
    <Objects xsi:type="Subscriber">
      {businessUnitId map (businessUnitId =>
      <Client>
        <ID>
          {businessUnitId}
        </ID>
      </Client>
      ) flatten}<ObjectID xsi:nil="true">
    </ObjectID>
      <EmailAddress>
        {subscriber.email}
      </EmailAddress>
      <SubscriberKey>
        {subscriber.email}
      </SubscriberKey>
      <Lists>
        {subscriber.lists.map {
        emailList =>
          <ID>
            {emailList.listId}
          </ID>
            <Status>
              {emailList.status}
            </Status>
      } flatten}<ObjectID xsi:nil="true">
      </ObjectID>
      </Lists>{subscriber.firstName.map(firstName =>
      <Attributes>
        <Name>First Name</Name>
        <Value>
          {firstName}
        </Value>
      </Attributes>
    ) flatten}{subscriber.lastName.map(lastName =>
      <Attributes>
        <Name>Last Name</Name>
        <Value>
          {lastName}
        </Value>
      </Attributes>
    ) flatten}
    </Objects>
  }

  def decodeResponse(response: NodeSeq) = {
    (response \\ "CreateResponse" \\ "Results") map {
      subscriberNode =>
        val email = (subscriberNode \\ "EmailAddress").text.trim
        val statusCode = (subscriberNode \\ "StatusCode").text.trim
        val statusMessage = (subscriberNode \\ "StatusMessage").text.trim
        val errorCode = (subscriberNode \\ "ErrorCode").text.trim
        SubscriberResult(email, statusCode, statusMessage, errorCode)
    }
  }
}

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
              <q1:Value>
                {subscriberKey}
              </q1:Value>
            </Filter>
          </RetrieveRequest>
        </RetrieveRequestMsg>
      </s:Body>
    </s:Envelope>

  }

  def decodeResponse(response: NodeSeq) = {

    val results = response \\ "RetrieveResponseMsg"
    Response(results \\ "OverallStatus" text,
      Subscriber(
        results \\ "SubscriberKey" text,
        (results \\ "Attributes") find (attribute => (attribute \\ "Name" text) == "First Name") map (_ \\ "Value" text),
        (results \\ "Attributes") find (attribute => (attribute \\ "Name" text) == "Last Name") map (_ \\ "Value" text),
        results \\ "CreatedDate" map (_.text) headOption,
        None,
        (results \\ "Status").headOption.map(_.text),
        (results \\ "EmailTypePreference").headOption.map(_.text)
      )
    )
  }
}
