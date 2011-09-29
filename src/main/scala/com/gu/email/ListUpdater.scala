package com.gu.email

import http.Http
import org.apache.commons.httpclient.methods.{PostMethod, StringRequestEntity}
import xml.SubscriptionRequest


object ListUpdater extends Application with Http {

  val accountDetails = AccountDetails("gnmtestuser", "row_4boat", "1062022")

  def subscribeToList(listId: String, accountDetails: AccountDetails, subscribers: List[Subscriber]) = {


    SubscriptionResult(200, Nil)
  }


  val method = new PostMethod("https://webservice.s4.exacttarget.com/Service.asmx")

  val requestBody = new StringRequestEntity(SubscriptionRequest("132", accountDetails, Nil), "text/xml", "UTF-8")

  method.setRequestHeader("Host", "webservice.s4.exacttarget.com")
  method.setRequestHeader("Content-Type", "text/xml; charset=utf-8")
  method.setRequestHeader("Content-Length", requestBody.getContentLength.toString)
  method.setRequestHeader("SOAPAction", "Create")
  method.setRequestEntity(requestBody)

  println("aaaaaa")

  println(httpClient.executeMethod(method))

  println("bbbbbb")


  println(method.getStatusLine)
  println(method.getStatusText)
  println(method.getResponseBodyAsString)

}

case class AccountDetails(username: String, password: String, bussinessUnitId: String)
case class Subscriber(email: String, firstName: String, lastName: String)
case class SubscriberResult(email: String, statusCode: String, statusMessage: String)
case class SubscriptionResult(statusCode: Int, subscriberResults: Seq[SubscriberResult])
