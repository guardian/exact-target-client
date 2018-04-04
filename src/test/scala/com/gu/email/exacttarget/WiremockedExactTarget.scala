package com.gu.email.exacttarget

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, stubFor}
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.gu.email.xml.XmlRequestSenderTest
import org.scalatest.{BeforeAndAfterAll, Suite, Suites}

trait WiremockedExactTarget extends BeforeAndAfterAll {
  self: Suite =>

  val wiremockPort = 6066
  val wiremock = new WireMockServer(wiremockPort)
  val exactTargetServiceUrl = s"http://localhost:$wiremockPort/wensleydale"

  override protected def beforeAll(): Unit = {
    wiremock.start()
    WireMock.configureFor(wiremockPort)
    val expectedRequest = post("/wensleydale")
    stubFor(expectedRequest.willReturn(aResponse().withBody("world")))
  }

}

class WiremockedExactTargetSuite extends Suites(
  new ExactTargetSoapFactoryTest("http://localhost:6066/wensleydale"),
  new XmlRequestSenderTest("http://localhost:6066/wensleydale")
) with WiremockedExactTarget
