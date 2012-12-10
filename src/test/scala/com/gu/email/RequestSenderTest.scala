package com.gu.email

import org.scalatest.FlatSpec
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.matchers.ShouldMatchers
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.PostMethod
import scala.xml.NodeSeq


class XmlRequestSenderTest extends FlatSpec with MockitoSugar with ShouldMatchers{
  val httpClient = mock[HttpClient]
  val postMethod = mock[PostMethod]
  val requestSender = new XmlRequestSender(httpClient) {
    override def getPostMethod() = {
      postMethod
    }
  }

  "XmlRequestSender" should "should decode resposne and return status code" in {
    when(postMethod.getResponseBodyAsString).thenReturn(<Response/>.toString())
    when(httpClient.executeMethod(postMethod)).thenReturn(500)
    val response = requestSender.sendSubscriptionRequest(<Request/>, "Action")

    response should equal((500, <Response/>))
  }
}

class RequestSenderTest extends FlatSpec with MockitoSugar with ShouldMatchers{
  class SomeType

  val xmlRequestSender = mock[XmlRequestSender]
  val requestEncoder = mock[MessageEncoder[SomeType, SomeType]]

  val requestSender = new RequestSender[SomeType, SomeType](requestEncoder, xmlRequestSender)
  val request = mock[SomeType]
  val encodedRequest = mock[NodeSeq]

  val encodedResponse = mock[NodeSeq]
  val decodedResponse = mock[SomeType]

  "XmlRequestSender" should "should endcode request and decode resposne and return status code" in {
    when(requestEncoder.encodeRequest(request)).thenReturn(encodedRequest)
    when(requestEncoder.decodeResponse(encodedResponse)).thenReturn(decodedResponse)
    when(xmlRequestSender.sendSubscriptionRequest(encodedRequest, "SoapAction")).thenReturn((1234, encodedResponse))

    requestSender.sendRequest(request, "SoapAction") should be (1234, decodedResponse)
  }
}


