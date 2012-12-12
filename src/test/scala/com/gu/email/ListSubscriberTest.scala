package com.gu.email

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.collection.Seq
import org.scalatest.mock.MockitoSugar
import scala._
import xml.{XmlRequestSender, Response, RequestSender, SubscriberUpdateRequest}
import scala.Some
import org.mockito.Mockito._

class ListSubscriberTest extends FlatSpec with ShouldMatchers with MockitoSugar {
  val listSubscriber = new ListSubscriber {
    val xmlRequestSender = mock[XmlRequestSender]
    val accountDetails = mock[AccountDetails]
    override lazy val subscriberUpdateMessageSender = mock[RequestSender[SubscriberUpdateRequest, Seq[Response[String]]]]
  }

  val subscribers = List(Subscriber("email@address.com", None, None))
  val result = List(mock[Response[String]])

  "unsubscribe" should "update subscription to Unsubscribed" in {
    when(listSubscriber.subscriberUpdateMessageSender.sendRequest(
      SubscriberUpdateRequest(Some("abusinessunit"), listSubscriber.accountDetails,
          List(Subscriber("email@address.com", None, None, subscriptions = List(EmailList("alist", "Unsubscribed"))))), "Create"))
        .thenReturn((200, result))

    listSubscriber.unsubscribeFromList("alist", Some("abusinessunit"), subscribers) should equal((200, result))
  }

  "subscribe" should "update subscription to Active" in {
    when(listSubscriber.subscriberUpdateMessageSender.sendRequest(
      SubscriberUpdateRequest(Some("abusinessunit"), listSubscriber.accountDetails,
          List(Subscriber("email@address.com", None, None, subscriptions = List(EmailList("alist", "Active"))))), "Create"))
        .thenReturn((200, result))

    listSubscriber.subscribeToList("alist", Some("abusinessunit"), subscribers) should equal((200, result))
  }
}