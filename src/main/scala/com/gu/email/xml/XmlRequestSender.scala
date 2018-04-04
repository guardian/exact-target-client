package com.gu.email.xml

import org.apache.http.client.fluent.Request
import org.apache.http.entity.{ContentType, StringEntity}
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory

import scala.xml.{NodeSeq, Source, XML}

class XmlRequestSender() {

  private val ExactTargetServiceUrl = "https://webservice.s4.exacttarget.com/Service.asmx"
  private val OK = 200
  private val logger = LoggerFactory.getLogger(getClass)

  def sendSubscriptionRequest(request: NodeSeq, soapAction: String): (Int, NodeSeq) = {
    val requestString = request.toString()

    if (logger.isDebugEnabled) {
      logger.debug("Request xml: " + requestString)
    }

    val requestBody = new StringEntity(requestString, ContentType.create("text/xml", "UTF-8"))

    val postRequest =
      Request.Post(ExactTargetServiceUrl)
        .addHeader("Host", "webservice.s4.exacttarget.com")
        .addHeader("SOAPAction", soapAction)
        .body(requestBody)

    val response = postRequest.execute.returnResponse()
    val responseBody = EntityUtils.toString(response.getEntity)

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


