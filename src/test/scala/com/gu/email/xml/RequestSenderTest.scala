package com.gu.email.xml

import org.scalatest.FlatSpec
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.Matchers
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils

import scala.xml.NodeSeq


class XmlRequestSenderTest extends FlatSpec with MockitoSugar with Matchers{
  val httpClient = mock[CloseableHttpClient]
  val postMethod = mock[HttpPost]
  val requestSender = new XmlRequestSender() {
  }

  val closeableHttpResponse = mock[CloseableHttpResponse]
/**
  "XmlRequestSender" should "should decode response and return status code" in {
    when(EntityUtils.toString(postMethod.getEntity)).thenReturn(<Response/>.toStream toString())
    when(httpClient.execute(postMethod)).thenReturn(closeableHttpResponse)
    val response = requestSender.sendSubscriptionRequest(<Request/>, "Action")

    response should equal((closeableHttpResponse, <Response/>))
  }
  **/
}

class RequestSenderTest extends FlatSpec with MockitoSugar with Matchers{
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


