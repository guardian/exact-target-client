package com.gu.email

import scala.xml.{NodeSeq, XML}
import org.apache.commons.httpclient.HttpClient
import org.slf4j.LoggerFactory
import org.apache.commons.httpclient.methods.{PostMethod, StringRequestEntity}

class XmlRequestSender(httpClient: HttpClient) {
  val OK = 200
  val logger = LoggerFactory.getLogger(classOf[XmlRequestSender])

  def sendSubscriptionRequest(request:NodeSeq, action: String): (Int, NodeSeq) = {
    val requestString = request.toString()

    if (logger.isDebugEnabled) {
      logger.debug("Request xml: " + requestString)
    }

    val requestBody = new StringRequestEntity(requestString, "text/xml", "UTF-8")

    val httpMethod = new PostMethod("https://webservice.s4.exacttarget.com/Service.asmx")
    httpMethod.setRequestHeader("Host", "webservice.s4.exacttarget.com")
    httpMethod.setRequestHeader("Content-Type", "text/xml; charset=utf-8")
    httpMethod.setRequestHeader("Content-Length", requestBody.getContentLength.toString)
    httpMethod.setRequestHeader("SOAPAction", action)
    httpMethod.setRequestEntity(requestBody)


    httpClient.executeMethod(httpMethod) match {
      case OK => {
        val response = XML.load(httpMethod.getResponseBodyAsStream)

        if (logger.isDebugEnabled) logger.debug("Email subscription response xml: " + response.toString())

        (OK, response)
      }
      case error => {
        logger.error("Request return error %s : %s : %s : %s".
          format(error, httpMethod.getStatusCode, httpMethod.getStatusLine, httpMethod.getStatusText))
        if (logger isDebugEnabled) logger.debug("Response: %s" format(httpMethod.getResponseBodyAsString))
        (error, Nil)
      }
    }
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


