package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.Matchers
import java.io.StringReader
import org.jdom.input.SAXBuilder

class TriggeredEmailResponseTest extends FunSuite with Matchers {

  test("Should be able to extract important fields from response doc") {

    val documentUrl = getClass.getClassLoader.getResource("com/gu/email/exacttarget/TriggeredEmailResponse.xml")
    val responseDocument = new SAXBuilder().build(documentUrl)
    val response = new TriggeredEmailResponse(responseDocument)

    response.getRequestId should be("297bc4ea-84e9-49fd-b4bc-6afe423d374a")
    response.getStatusMessage should be("Created TriggeredSend")
    response.isStatusOk should be(true)
    response.isOverallStatusOk should be(true)
    response.getStatusCode should be("OK")
    response.getOverallStatus should be("ok")

  }

  test("Should correctly parse status codes into booleans") {

    val documentUrl = getClass.getClassLoader.getResource("com/gu/email/exacttarget/TriggeredEmailBadResponse.xml")
    val responseDocument = new SAXBuilder().build(documentUrl)
    val response = new TriggeredEmailResponse(responseDocument)

    response.isStatusOk should be(false)
    response.isOverallStatusOk should be(true)

  }

  test("Should set status fields appropriately if we receive a junk document") {
    val junkDocString = "<junk>junk</junk>"
    val junkDoc = new SAXBuilder().build(new StringReader(junkDocString))
    val response = new TriggeredEmailResponse(junkDoc)

    response.isStatusOk should be(false)
    response.isOverallStatusOk should be(false)
    response.getStatusMessage should be("Unable to parse response document")
    response.getRequestId should be("")
    response.getStatusCode should be("ERROR")
    response.getOverallStatus should be("ERROR")
  }

}
