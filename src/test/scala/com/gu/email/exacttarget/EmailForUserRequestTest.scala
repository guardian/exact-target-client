package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import java.net.URI
import com.gu.email.GuardianUser
import java.io.ByteArrayOutputStream
import org.jdom.input.SAXBuilder
import org.jdom.output.{Format, XMLOutputter}

class EmailForUserRequestTest extends FunSuite with ShouldMatchers {


  test("should generate correct soap message")
  {
    val factory = new ExactTargetFactory("XXXaccountNameXXX", "XXXpasswordXXX", new URI("http://host.com/path/path"))
    val emailListsRequest = factory.createListForUserRequest(GuardianUser("XXXnew.userXXX", "mrwibblywobbly@guardian.co.uk"), "XXXbusinessUnitIdXXX")

    val stream = new ByteArrayOutputStream()
    emailListsRequest.writeRequest(stream)
    val actualXmlString = stream.toString

    val url = getClass.getClassLoader.getResource("com/gu/email/exacttarget/EmailListForUserRequest.xml")
    val expectedDocument = new SAXBuilder().build(url)

    val outputter = new XMLOutputter(Format.getCompactFormat)
    val expectedXmlString = outputter.outputString(expectedDocument)

    actualXmlString should be(expectedXmlString)
  }
}
