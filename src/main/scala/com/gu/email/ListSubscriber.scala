package com.gu.email

import xml._
import org.slf4j.LoggerFactory
import xml.SubscriberUpdateRequest

//these traits are for convenience and can be mixed in to create a 'repository', you need to provide a xmlRequestSender eg:
//class ETRepo (val xmlRequestSender: XmlRequestSender, val accountDetails: AccountDetails) with ListSubscriber with SubscriberInfo
//val repo = new ETRepo(new XmlRequestSender(httpClient))

trait RequiresXmlRequestSender {
  val xmlRequestSender: XmlRequestSender
}
trait RequiresAccountDetails {
  val accountDetails: AccountDetails
}

trait ListSubscriber extends RequiresXmlRequestSender with RequiresAccountDetails{
  lazy val listSubscriberUpdateMessageSender = new RequestSender[SubscriberUpdateRequest, Seq[Response[String]]](new SubscriberUpdateMessageEncoder(), xmlRequestSender)

  val logger = LoggerFactory.getLogger(getClass)

  def subscribeToList(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber]): (Int, Seq[Response[String]]) = {
    val subscribersWithList = subscribers.map(_.copy(subscriptions = List(EmailList(listId, "Active"))))
    listSubscriberUpdateMessageSender.sendRequest(SubscriberUpdateRequest(businessUnitId, accountDetails, subscribersWithList), "Create")
  }

  def unsubscribeFromList(listId: String, businessUnitId: Option[String], subscribers: Seq[Subscriber]): (Int, Seq[Response[String]]) = {
    val subscribersWithList = subscribers.map(_.copy(subscriptions = List(EmailList(listId, "Unsubscribed"))))
    listSubscriberUpdateMessageSender.sendRequest(SubscriberUpdateRequest(businessUnitId, accountDetails, subscribersWithList), "Create")
  }
}

trait SubscriberInfo extends RequiresXmlRequestSender with RequiresAccountDetails {
  lazy val infoSubscriberRetrieveMessageSender = new RequestSender[String, Response[Subscriber]](new SubscriberRetrieveMessageEncoder(accountDetails), xmlRequestSender)
  lazy val infoSubscriberUpdateMessageSender = new RequestSender[SubscriberUpdateRequest, Seq[Response[String]]](new SubscriberUpdateMessageEncoder(), xmlRequestSender)

  def getSubscriberInfo(emailAddress: String): (Int, Response[Subscriber]) =
    infoSubscriberRetrieveMessageSender.sendRequest(emailAddress, "Retrieve")

  def updateSubscriberInfo(subscriber: Subscriber, businessUnitId: Option[String]): (Int, Seq[Response[String]]) =
    infoSubscriberUpdateMessageSender.sendRequest(
      SubscriberUpdateRequest(businessUnitId, accountDetails, List(subscriber)), "Create")

}

case class AccountDetails(username: String, password: String)

case class Subscriber(
  email: String,
  firstName: Option[String] = None,
  lastName: Option[String] = None,
  createdDate: Option[String] = None,
  unsubscribeDate: Option[String] = None,
  status: Option[String] = None,
  emailTypePreference: Option[String] = None,
  subscriptions: List[EmailList] = Nil
)

case class GuardianUser(userName: String, email: String)

case class EmailList(listId: String, status: String)
