package com.gu.email.xml

import scala.xml.{Source, NodeSeq, XML}
import org.apache.commons.httpclient.HttpClient
import org.slf4j.LoggerFactory
import org.apache.commons.httpclient.methods.{PostMethod, StringRequestEntity}

class XmlRequestSender(httpClient: HttpClient) {
  val OK = 200
  val logger = LoggerFactory.getLogger(classOf[XmlRequestSender])

  def sendSubscriptionRequest(request:NodeSeq, soapAction: String): (Int, NodeSeq) = {
    val requestString = request.toString()

    if (logger.isDebugEnabled) {
      logger.debug("Request xml: " + requestString)
    }

    val requestBody = new StringRequestEntity(requestString, "text/xml", "UTF-8")

    val httpMethod = getPostMethod()
    httpMethod.setRequestHeader("Host", "webservice.s4.exacttarget.com")
    httpMethod.setRequestHeader("Content-Type", "text/xml; charset=utf-8")
    httpMethod.setRequestHeader("Content-Length", requestBody.getContentLength.toString)
    httpMethod.setRequestHeader("SOAPAction", soapAction)
    httpMethod.setRequestEntity(requestBody)

    val responseCode = httpClient.executeMethod(httpMethod)
    val responseBody = httpMethod.getResponseBodyAsString;

    if (responseCode == OK) {
      if (logger.isDebugEnabled) logger.debug("Email subscription response xml: " + responseBody)
    } else {
      logger.error("Request return error %s : %s : %s".
        format(httpMethod.getStatusCode, httpMethod.getStatusLine, httpMethod.getStatusText))
      if (logger isDebugEnabled) {
        logger.debug("Response: %s" format responseBody)
      }
    }

    (responseCode, XML.load(Source.fromString(responseBody)))
  }

  def getPostMethod() = {
    new PostMethod("https://webservice.s4.exacttarget.com/Service.asmx")
  }
}



class RequestSender[RequestType, ResponseType](encoder: MessageEncoder[RequestType, ResponseType], xmlSender: XmlRequestSender) {
  def sendRequest(request: RequestType, action: String) : (Int, ResponseType) = {
    val (responseCode, xmlResponse) = xmlSender.sendSubscriptionRequest(encoder.encodeRequest(request), action)
    (responseCode, encoder.decodeResponse(xmlResponse))
  }
}

abstract class MessageEncoder[RequestType, ResponseType] {
  def encodeRequest(request: RequestType): NodeSeq
  def decodeResponse(response: NodeSeq): ResponseType
}

case class Response[ResponseObjectType](status: String, data: ResponseObjectType)


