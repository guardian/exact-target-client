package com.gu.email

import org.apache.commons.httpclient.methods.{PostMethod, StringRequestEntity}
import xml.SubscriptionRequest
import scala.xml.{Node, XML}
import org.slf4j.LoggerFactory
import org.apache.commons.httpclient.HttpClient

trait ListSubscriber {

  val httpClient: HttpClient

  val logger = LoggerFactory.getLogger(getClass)

  val accountDetails: AccountDetails

  val OK = 200


  def subscribeToList(listId: String, subscribers: Seq[Subscriber]): (Int, Seq[SubscriberResult]) = {

    val subscriptionXml = SubscriptionRequest(listId, accountDetails, subscribers)

    val requestBody = new StringRequestEntity(subscriptionXml toString, "text/xml", "UTF-8")
    val httpMethod = prepareRequest(requestBody)

    httpClient.executeMethod(httpMethod) match {
      case OK => {
        val response = XML.load(httpMethod.getResponseBodyAsStream)
        val subscriberResults = (response \\ "CreateResponse" \\ "Results") map { SubscriberResult(_) }

        if (logger.isDebugEnabled) subscriberResults foreach { r => logger.debug(r.toString) }

        (OK, subscriberResults)
      }
      case error => {
        logger.error("Error adding subscribers to list %s : %s : %s : %s".
          format(error, httpMethod.getStatusCode, httpMethod.getStatusLine, httpMethod.getStatusText))
        logger.error("Subscriber list: " + subscribers.map(_.toString).mkString(","))
        (error, Nil)
      }
    }
  }

  private def prepareRequest(requestBody: StringRequestEntity) = {
    val method = new PostMethod("https://webservice.s4.exacttarget.com/Service.asmx")
    method.setRequestHeader("Host", "webservice.s4.exacttarget.com")
    method.setRequestHeader("Content-Type", "text/xml; charset=utf-8")
    method.setRequestHeader("Content-Length", requestBody.getContentLength.toString)
    method.setRequestHeader("SOAPAction", "Create")
    method.setRequestEntity(requestBody)
    method
  }
}

case class AccountDetails(username: String, password: String, bussinessUnitId: String)

case class Subscriber(email: String, firstName: String, lastName: String)

case class SubscriberResult(email: String, statusCode: String, statusMessage: String) {
  val success = statusCode == "OK"
}
object SubscriberResult {
  def apply(node: Node): SubscriberResult = {
    val email = (node \\ "EmailAddress").text.trim
    val statusCode = (node \\ "StatusCode").text.trim
    val statusMessage = (node \\ "StatusMessage").text.trim
    SubscriberResult(email, statusCode, statusMessage)
  }
}
