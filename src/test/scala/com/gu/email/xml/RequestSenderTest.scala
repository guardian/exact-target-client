package com.gu.email.xml

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, stubFor}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{DoNotDiscover, FlatSpec, Matchers}

import scala.xml.NodeSeq

@DoNotDiscover
class XmlRequestSenderTest(exactTargetMockUrl: String) extends FlatSpec with MockitoSugar with Matchers {
  val requestSender = new XmlRequestSender(exactTargetMockUrl)

  "XmlRequestSender" should "should decode response and return status code" in {
    val expectedRequest = post("/wensleydale")
    stubFor(expectedRequest.willReturn(aResponse().withBody(<Response/>.toString().getBytes)))
    val response = requestSender.sendSubscriptionRequest(<Request/>, "Action")
    response should equal((200, <Response/>))
  }

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


