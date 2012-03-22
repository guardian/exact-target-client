package com.gu.email.com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.gu.email.exacttarget.TriggeredEmailRequestFactory
import java.net.URI
import org.jdom.input.SAXBuilder
import org.jdom.output.{XMLOutputter, Format}
import java.io.{FileOutputStream, ByteArrayOutputStream, File}

class TriggeredEmailRequestTest extends FunSuite with ShouldMatchers {

  test("Should generate correct soap message with variables interpolated into the right slots") {
    val factory = new TriggeredEmailRequestFactory("XXXaccountNameXXX", "XXXpasswordXXX", "XXXemailTemplateXXX", new URI("http://host.com/path/path"))
    val emailRequest = factory.createRequest("XXXnew.userXXX", "XXXnew.user@somewhere.comXXX")

    val byteOutStream = new ByteArrayOutputStream()
    emailRequest.writeRequest(byteOutStream)
    val actualXmlString = byteOutStream.toString

    val url = getClass.getClassLoader.getResource("com/gu/email/exacttarget/TriggeredEmailRequest.xml")
    val expectedDocument = new SAXBuilder().build(url)

    val outputter = new XMLOutputter(Format.getCompactFormat)
    val expectedXmlString = outputter.outputString(expectedDocument)
    //
    // println(actualXmlString)

    actualXmlString should be(expectedXmlString)

//    val actualFile = new File("/home/jrodgers/Desktop/actual.xml")
//    val expectedFile = new File("/home/jrodgers/Desktop/expected.xml")
//
//    emailRequest.writeRequest(new FileOutputStream(actualFile))
//    outputter.output(expectedDocument, new FileOutputStream(expectedFile))

  }


}
