package com.gu.email.xml

import scala.xml.{NodeSeq, Source, XML}
import org.slf4j.LoggerFactory
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

class XmlRequestSender(httpClient: DefaultHttpClient) {
  val OK = 200
  val logger = LoggerFactory.getLogger(classOf[XmlRequestSender])

  def sendSubscriptionRequest(request:NodeSeq, soapAction: String): (Int, NodeSeq) = {
    val requestString = request.toString()

    if (logger.isDebugEnabled) {
      logger.debug("Request xml: " + requestString)
    }

    val requestBody = new StringEntity(requestString, "text/xml", "UTF-8")

    val httpMethod = getPostMethod()
    httpMethod.setHeader("Host", "webservice.s4.exacttarget.com")
    httpMethod.setHeader("Content-Type", "text/xml; charset=utf-8")
    httpMethod.setHeader("Content-Length", requestBody.getContentLength.toString)
    httpMethod.setHeader("SOAPAction", soapAction)
    httpMethod.setEntity(requestBody)

    val response = httpClient.execute(httpMethod)
    val responseBody = EntityUtils.toString(httpMethod.getEntity)

    if (response.getStatusLine.getStatusCode == OK) {
      if (logger.isDebugEnabled) logger.debug("Email subscription response xml: " + responseBody)
    } else {
      logger.error("Request return error %s : %s : %s".
        format(response.getStatusLine.getStatusCode, response.getStatusLine, response.getStatusLine.getReasonPhrase))
      if (logger isDebugEnabled) {
        logger.debug("Response: %s" format responseBody)
      }
    }

    (response.getStatusLine.getStatusCode, XML.load(Source.fromString(responseBody)))
  }

  def getPostMethod() = new HttpPost("https://webservice.s4.exacttarget.com/Service.asmx")
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

case class Response[ResponseObjectType](status: String, statusMessage: Option[String], errorCode: Option[String], data: ResponseObjectType)


