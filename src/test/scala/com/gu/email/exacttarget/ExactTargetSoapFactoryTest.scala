package com.gu.email.exacttarget

import java.net.URI

import com.github.tomakehurst.wiremock.client.WireMock._
import org.apache.http.entity.{ContentType, StringEntity}
import org.scalatest.{DoNotDiscover, FunSuite, Matchers}

@DoNotDiscover
class ExactTargetSoapFactoryTest(exactTargetMockUrl: String) extends FunSuite with Matchers {

  val factory = new ExactTargetFactory("", "", new URI(exactTargetMockUrl))
  val createSoapAction = "Create"
  val body = new StringEntity("hello", ContentType.create("text/xml", "UTF-8"))

  test("Should create a post request and set the body as specified, setting the content type and SOAPAction correctly") {
    val request = factory.createPostMethod(body, createSoapAction)
    request.execute()
    verify(
      postRequestedFor(urlEqualTo("/wensleydale"))
      .withHeader("Host", equalTo("localhost"))
      .withHeader("SOAPAction", equalTo("Create"))
      .withHeader("Content-Type", equalTo("text/xml; charset=UTF-8"))
      .withHeader("Content-Length", equalTo("5"))
      .withRequestBody(equalTo("hello"))
    )
  }

}
