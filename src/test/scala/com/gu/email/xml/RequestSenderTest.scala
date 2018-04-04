package com.gu.email.xml

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, stubFor}
import com.gu.email.exacttarget.WiremockedExactTarget
import org.mockito.Mockito._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.mock.MockitoSugar

import scala.xml.NodeSeq


class XmlRequestSenderTest extends FlatSpec with WiremockedExactTarget with MockitoSugar with Matchers {
  val requestSender = new XmlRequestSender(exactTargetServiceUrl)


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


