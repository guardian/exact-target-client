package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import java.net.URI
import org.jdom.input.SAXBuilder
import org.jdom.output.{XMLOutputter, Format}
import java.io.ByteArrayOutputStream
import com.gu.email.GuardianUser

class TriggeredEmailRequestTest extends FunSuite with ShouldMatchers {

  test("Should generate correct soap message with variables interpolated into the right slots") {
    val factory = new ExactTargetSoapFactory("XXXaccountNameXXX", "XXXpasswordXXX", "XXXemailTemplateXXX", new URI("http://host.com/path/path"))
    val emailRequest = factory.createRequest(GuardianUser("XXXnew.userXXX", "XXXnew.user@somewhere.comXXX"))

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
