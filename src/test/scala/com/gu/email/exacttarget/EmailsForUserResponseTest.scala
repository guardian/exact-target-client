package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.jdom.input.SAXBuilder

import scala.collection.JavaConverters._


class EmailsForUserResponseTest extends FunSuite with ShouldMatchers {

  test("Should be able to extract important fields from response doc") {

    val documentUrl = getClass.getClassLoader.getResource("com/gu/email/exacttarget/EmailListForUserResponse.xml")
    val responseDocument = new SAXBuilder().build(documentUrl)
    print("XML: " + responseDocument.toString )
    val response = new EmailListForUserResponse(responseDocument)

    response.isOverallStatusOk should be(true)
    response.getOverallStatus should be("OK")
    val jListIds : java.util.List[java.lang.String] = response.getEmalListIds;
    val listIds = jListIds.asScala.toSet[String]
    listIds.size should be (4)
    listIds should contain ("396")
    listIds should contain ("397")
    listIds should contain ("537")
    listIds should contain ("540")
  }




}
