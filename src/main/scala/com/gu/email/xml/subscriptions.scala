package com.gu.email.xml

import com.gu.email.{AccountDetails, Subscriber}

private[email] object SubscriptionRequest {
    def apply(listId: String, accountDetails: AccountDetails, subscribers: Seq[Subscriber]) =
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
                {accountDetails.username}
              </wsse:Username>
              <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">
                {accountDetails.password}
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
            </Options>
            {subscribers map { ListSubscriber(listId, accountDetails) }}
          </CreateRequest>
        </soap:Body>
      </soap:Envelope>
  }

private object ListSubscriber {
    def apply(listId: String, accountDetails: AccountDetails)(subscriber: Subscriber) =
      <Objects xsi:type="Subscriber">
        <Client>
          <ID>{accountDetails.bussinessUnitId}</ID>
        </Client>
        <ObjectID xsi:nil="true">
        </ObjectID>
        <EmailAddress>{subscriber.email}</EmailAddress>
        <SubscriberKey>{subscriber.email}</SubscriberKey>
        <Lists>
          <ID>{listId}</ID>
          <ObjectID xsi:nil="true">
          </ObjectID>
        </Lists>
        <Attributes>
          <Name>First Name</Name>
          <Value>{subscriber.firstName}</Value>
        </Attributes>
        <Attributes>
          <Name>Last Name</Name>
          <Value>{subscriber.lastName}</Value>
        </Attributes>
      </Objects>
  }