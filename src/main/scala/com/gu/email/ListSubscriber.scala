package com.gu.email

import org.apache.commons.httpclient.methods.{PostMethod, StringRequestEntity}
import xml.{ListSubscriberMessageEncoder, SubscriptionRequest}
import scala.xml.{Node, XML}
import org.slf4j.LoggerFactory
import org.apache.commons.httpclient.HttpClient

trait ListSubscriber {

  val httpClient: HttpClient

  val xmlRequestSender = new XmlRequestSender(httpClient)
  val listSubscriptionMessageSender = new RequestSender[SubscriptionRequest, Seq[SubscriberResult]](new ListSubscriberMessageEncoder(), xmlRequestSender)

  val logger = LoggerFactory.getLogger(getClass)

  val accountDetails: AccountDetails

  def subscribeToList(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber]): (Int, Seq[SubscriberResult]) = {
    sendSubscriptionRequest(listId, businessUnitId, subscribers, "Active")
  }

  def unsubscribeFromList(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber]): (Int, Seq[SubscriberResult]) = {
    sendSubscriptionRequest(listId, businessUnitId, subscribers, "Unsubscribed")
  }

  def sendSubscriptionRequest(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber], status:String): (Int, Seq[SubscriberResult]) = {
    listSubscriptionMessageSender.sendRequest(SubscriptionRequest(listId, businessUnitId, accountDetails, subscribers, status), "Create")
  }
}

case class AccountDetails(username: String, password: String)

case class Subscriber(email: String, firstName: Option[String], lastName: Option[String],
                      createdDate: Option[String] = None, unsubscribeDate: Option[String] = None,
                      status: Option[String] = None, emailTypePreference: Option[String] = None)

case class GuardianUser(userName: String, email: String)

case class SubscriberResult(email: String, statusCode: String, statusMessage: String, errorCode: String ) {
  val success = statusCode == "OK"
}
