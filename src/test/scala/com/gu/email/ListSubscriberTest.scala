package com.gu.email

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import collection.Seq


class ListSubscriberTest extends FlatSpec with ShouldMatchers {

  //NOTE this is an integration test that actually tries to add subscribers to the list in the test environment

  "List subscriber" should "call the Exact target API and attempt to add subscribers" in {

    val listSubscriber = new ListSubscriber {
      val accountDetails = AccountDetails("gnmtestuser", "row_4boat", "1062022")
    }


    val subscribers: Seq[Subscriber] = Seq(Subscriber("john.smith@guardian.co.uk", "John", "Smith"),
      Subscriber("peter.jones@guardian.co.uk", "Peter", "Jones"))
    val (status, results) = listSubscriber.subscribeToList("132", subscribers)

    results.foreach(println)

    status should equal (200)

    results should contain (SubscriberResult("john.smith@guardian.co.uk", "Error", "The subscriber is already on the list"))

    results should contain (SubscriberResult("peter.jones@guardian.co.uk", "Error", "The subscriber is already on the list"))
  }
}