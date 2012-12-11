package com.gu.email

import xml._
import org.slf4j.LoggerFactory
import xml.SubscriberUpdateRequest

//these traits are for convenience and can be mixed in to create a 'repository', you need to provide a xmlRequestSender eg:
//class ETRepo (val xmlRequestSender: XmlRequestSender) with ListSubscriber with SubscriberInfo
//val repo = new ETRepo(new XmlRequestSender(httpClient))

trait ListSubscriber {
  val xmlRequestSender: XmlRequestSender
  val subscriberUpdateMessageSender = new RequestSender[SubscriberUpdateRequest, Seq[Response[String]]](new SubscriberUpdateMessageEncoder(), xmlRequestSender)

  val logger = LoggerFactory.getLogger(getClass)

  val accountDetails: AccountDetails

  def subscribeToList(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber]): (Int, Seq[Response[String]]) = {
    val subscribersWithList = subscribers.map(_.copy(lists = List(EmailList(listId, "Active"))))
    subscriberUpdateMessageSender.sendRequest(SubscriberUpdateRequest(businessUnitId, accountDetails, subscribersWithList), "Create")
  }

  def unsubscribeFromList(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber]): (Int, Seq[Response[String]]) = {
    val subscribersWithList = subscribers.map(_.copy(lists = List(EmailList(listId, "Unsubscribed"))))
    subscriberUpdateMessageSender.sendRequest(SubscriberUpdateRequest(businessUnitId, accountDetails, subscribersWithList), "Create")
  }
}

trait SubscriberInfo {
  val xmlRequestSender: XmlRequestSender
  val subscriberRetrieveMessageSender = new RequestSender[String, Response[Subscriber]](new SubscriberRetrieveMessageEncoder(), xmlRequestSender)
  def getSubscriberInfo(userId: String) = subscriberRetrieveMessageSender.sendRequest(userId, "Retrieve")
}

case class AccountDetails(username: String, password: String)

case class Subscriber(email: String, firstName: Option[String], lastName: Option[String],
                      createdDate: Option[String] = None, unsubscribeDate: Option[String] = None,
                      status: Option[String] = None, emailTypePreference: Option[String] = None,
                      lists: List[EmailList] = Nil)

case class GuardianUser(userName: String, email: String)

case class EmailList(listId: String, status: String)
