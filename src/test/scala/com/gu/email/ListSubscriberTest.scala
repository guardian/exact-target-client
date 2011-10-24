package com.gu.email

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import collection.Seq
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.SimpleHttpConnectionManager
import xml.{SubscriptionRequest, SubscriberXml}

class ListSubscriberTest extends FlatSpec with ShouldMatchers {

  //NOTE this is an integration test that actually tries to add subscribers to the list in the test environment

  "List subscriber" should "call the Exact target API and attempt to add subscribers" in {

    val listSubscriber = new ListSubscriber {
      val httpClient = new HttpClient(new SimpleHttpConnectionManager(true))
      val accountDetails = AccountDetails("gnmtestuser", "row_4boat", "1062022")
    }


    val subscribers = Seq(Subscriber("john.smith@guardian.co.uk", "John", "Smith"),
      Subscriber("peter.jones@guardian.co.uk", "Peter", "Jones"))
    val (status, results) = listSubscriber.subscribeToList("132", subscribers)

    status should equal (200)

    results foreach {sub => sub.success should be (true) }

    results should contain (SubscriberResult("john.smith@guardian.co.uk", "OK", "Created Subscriber."))

    results should contain (SubscriberResult("peter.jones@guardian.co.uk", "OK", "Created Subscriber."))
  }

  "Xml" should "not have leading or trailing whitespace" in {

    val subscribers = List(Subscriber("john.smith@guardian.co.uk", "John", "Smith"))
    val accountDetails = AccountDetails("gnmtestuser", "row_4boat", "1062022")

    val subscriberXml = SubscriptionRequest("123", accountDetails, subscribers)

    ((subscriberXml) \\ "Username").text should equal ("gnmtestuser")
    ((subscriberXml) \\ "Password").text should equal ("row_4boat")

    ((subscriberXml) \\ "SubscriberKey").text should equal ("john.smith@guardian.co.uk")
    ((subscriberXml) \\ "Client" \\ "ID").text should equal ("1062022")
    ((subscriberXml) \\ "EmailAddress").text should equal ("john.smith@guardian.co.uk")
    ((subscriberXml) \\ "Lists" \\ "ID").text should equal ("123")

    val attributes = ((subscriberXml) \\ "Attributes" \\ "Value") map { _.text }

    attributes.size should be (2)

    attributes foreach { attribute =>
      attribute should equal  (attribute trim)
    }
  }
}