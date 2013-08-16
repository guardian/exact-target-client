package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import java.net.URI
import org.jdom.input.SAXBuilder
import org.jdom.output.{XMLOutputter, Format}
import java.io.ByteArrayOutputStream
import com.gu.email.GuardianUser
import scala.collection.JavaConversions._

class TriggeredEmailRequestTest extends FunSuite with ShouldMatchers {
  test("Should generate correct soap message with variables interpolated into the right slots for a attribute map") {
    val factory = new ExactTargetFactory("XXXaccountNameXXX", "XXXpasswordXXX", new URI("http://host.com/path/path"))
    val emailRequest = factory.createRequest("XXXnew.user@somewhere.comXXX", Map(("Field_A" -> "XXXnew.userXXX")), "Create", "XXXbusinessUnitIdXXX", "XXXemailTemplateXXX")

    val byteOutStream = new ByteArrayOutputStream()
    emailRequest.writeRequest(byteOutStream)
    val actualXmlString = byteOutStream.toString

    val url = getClass.getClassLoader.getResource("com/gu/email/exacttarget/TriggeredEmailRequest.xml")
    val expectedDocument = new SAXBuilder().build(url)

    val outputter = new XMLOutputter(Format.getCompactFormat)
    val expectedXmlString = outputter.outputString(expectedDocument)

    actualXmlString should be(expectedXmlString)
  }
}
