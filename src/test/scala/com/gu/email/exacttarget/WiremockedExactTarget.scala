package com.gu.email.exacttarget

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, stubFor}
import org.scalatest.{BeforeAndAfterAll, Suite}

trait WiremockedExactTarget extends BeforeAndAfterAll {
  self: Suite =>

  lazy val wiremockPort = 6066
  lazy val wiremock = new WireMockServer(wiremockPort)
  lazy val exactTargetServiceUrl = s"http://localhost:$wiremockPort/wensleydale"

  override protected def beforeAll(): Unit = {
    wiremock.start()
    WireMock.configureFor(wiremockPort)
    val expectedRequest = post("/wensleydale")
    stubFor(expectedRequest.willReturn(aResponse().withBody("world")))
  }

  override protected def afterAll(): Unit = wiremock.stop()


}
