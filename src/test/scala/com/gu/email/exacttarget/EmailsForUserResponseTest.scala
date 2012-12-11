package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.jdom.input.SAXBuilder

import scala.collection.JavaConverters._
import java.io.StringReader
import com.gu.email.EmailList


class EmailsForUserResponseTest extends FunSuite with ShouldMatchers {

  test("Should be able to extract important fields from response doc") {

    val documentUrl = getClass.getClassLoader.getResource("com/gu/email/exacttarget/EmailListForUserResponse.xml")
    val responseDocument = new SAXBuilder().build(documentUrl)
    print("XML: " + responseDocument.toString )
    val response = new EmailListForUserResponse(responseDocument)

    response.isOverallStatusOk should be(true)
    response.getOverallStatus should be("OK")
    val jListIds : java.util.List[EmailList] = response.getSubscriptions;
    val listIds = jListIds.asScala.toSet[EmailList]
    listIds.size should be (4)
    listIds should contain (EmailList("396", "Active"))
    listIds should contain (EmailList("397", "Active"))
    listIds should contain (EmailList("537", "Active"))
    listIds should contain (EmailList("540", "Active"))
  }

  test("Should set status fields appropriately if we receive a junk document") {
    val junkDocString = "<junk>junk</junk>"
    val junkDoc = new SAXBuilder().build(new StringReader(junkDocString))
    val response = new EmailListForUserResponse(junkDoc)

    response.isOverallStatusOk should be(false)
    response.getOverallStatus should be("ERROR")
    response.getStatusCode should be ("ERROR")
  }



}
