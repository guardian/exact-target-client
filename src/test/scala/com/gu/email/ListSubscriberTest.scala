package com.gu.email

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.collection.Seq
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.SimpleHttpConnectionManager
import com.gu.email.xml.SubscriptionRequest
import org.scalatest.mock.MockitoSugar
import scala._
import com.gu.email.SubscriberResult
import com.gu.email.Subscriber
import xml.SubscriptionRequest
import scala.Some
import com.gu.email.AccountDetails
import org.mockito.Mockito._
import org.mockito.Matchers._

class ListSubscriberTest extends FlatSpec with ShouldMatchers with MockitoSugar {
  val listSubscriber = new ListSubscriber {
    val httpClient = mock[HttpClient]
    val accountDetails = mock[AccountDetails]
    override val listSubscriptionMessageSender = mock[RequestSender[SubscriptionRequest, Seq[SubscriberResult]]]
  }

  val subscribers = List(mock[Subscriber], mock[Subscriber])
  val result = List(mock[SubscriberResult])

  "Unsubscribe" should "update subscription to Unsubscribed" in {
    when(listSubscriber.listSubscriptionMessageSender.sendRequest(
        SubscriptionRequest("alist", Some("abusinessunit"), listSubscriber.accountDetails, subscribers, "Unsubscribed"), "Create"))
        .thenReturn((200, result))

    listSubscriber.unsubscribeFromList("alist", Some("abusinessunit"), subscribers) should equal((200, result))
  }
}