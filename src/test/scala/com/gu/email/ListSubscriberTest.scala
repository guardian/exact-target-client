package com.gu.email

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.collection.Seq
import org.apache.commons.httpclient.HttpClient
import org.scalatest.mock.MockitoSugar
import scala._
import xml.SubscriberUpdateRequest
import scala.Some
import org.mockito.Mockito._

class ListSubscriberTest extends FlatSpec with ShouldMatchers with MockitoSugar {
  val listSubscriber = new ListSubscriber {
    val httpClient = mock[HttpClient]
    val accountDetails = mock[AccountDetails]
    override val subscriberUpdateMessageSender = mock[RequestSender[SubscriberUpdateRequest, Seq[SubscriberResult]]]
  }

  val subscribers = List(Subscriber("email@address.com", None, None))
  val result = List(mock[SubscriberResult])

  "unsubscribe" should "update subscription to Unsubscribed" in {
    when(listSubscriber.subscriberUpdateMessageSender.sendRequest(
      SubscriberUpdateRequest(Some("abusinessunit"), listSubscriber.accountDetails,
          List(Subscriber("email@address.com", None, None, lists = List(EmailList("alist", "Unsubscribed"))))), "Create"))
        .thenReturn((200, result))

    listSubscriber.unsubscribeFromList("alist", Some("abusinessunit"), subscribers) should equal((200, result))
  }

  "subscribe" should "update subscription to Active" in {
    when(listSubscriber.subscriberUpdateMessageSender.sendRequest(
      SubscriberUpdateRequest(Some("abusinessunit"), listSubscriber.accountDetails,
          List(Subscriber("email@address.com", None, None, lists = List(EmailList("alist", "Active"))))), "Create"))
        .thenReturn((200, result))

    listSubscriber.subscribeToList("alist", Some("abusinessunit"), subscribers) should equal((200, result))
  }
}