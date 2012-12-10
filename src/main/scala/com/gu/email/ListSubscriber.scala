package com.gu.email

import xml.{SubscriberUpdateMessageEncoder, SubscriberUpdateRequest}
import org.slf4j.LoggerFactory
import org.apache.commons.httpclient.HttpClient

trait ListSubscriber {

  val httpClient: HttpClient

  val xmlRequestSender = new XmlRequestSender(httpClient)
  val subscriberUpdateMessageSender = new RequestSender[SubscriberUpdateRequest, Seq[SubscriberResult]](new SubscriberUpdateMessageEncoder(), xmlRequestSender)

  val logger = LoggerFactory.getLogger(getClass)

  val accountDetails: AccountDetails

  def subscribeToList(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber]): (Int, Seq[SubscriberResult]) = {
    val subscribersWithList = subscribers.map(_.copy(lists = List(EmailList(listId, "Active"))))
    subscriberUpdateMessageSender.sendRequest(SubscriberUpdateRequest(businessUnitId, accountDetails, subscribersWithList), "Create")
  }

  def unsubscribeFromList(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber]): (Int, Seq[SubscriberResult]) = {
    val subscribersWithList = subscribers.map(_.copy(lists = List(EmailList(listId, "Unsubscribed"))))
    subscriberUpdateMessageSender.sendRequest(SubscriberUpdateRequest(businessUnitId, accountDetails, subscribersWithList), "Create")
  }
}

case class AccountDetails(username: String, password: String)

case class Subscriber(email: String, firstName: Option[String], lastName: Option[String],
                      createdDate: Option[String] = None, unsubscribeDate: Option[String] = None,
                      status: Option[String] = None, emailTypePreference: Option[String] = None,
                      lists: List[EmailList] = Nil)

case class GuardianUser(userName: String, email: String)

case class SubscriberResult(email: String, statusCode: String, statusMessage: String, errorCode: String ) {
  val success = statusCode == "OK"
}

case class EmailList(listId: String, status: String)
