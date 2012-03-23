package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.apache.commons.httpclient.methods.RequestEntity
import java.io.OutputStream
import com.gu.email.exacttarget.ExactTargetSoapFactory
import java.net.URI

class ExactTargetSoapFactoryTest extends FunSuite with ShouldMatchers {

  val factory = new ExactTargetSoapFactory("", "", "", new URI("http://cheese.com/wensleydale"))

  val body = new RequestEntity() {
    def isRepeatable() = false;

    def writeRequest(out: OutputStream): Unit = {}

    def getContentLength() = 35

    def getContentType: String = "cheese;balls"
  }

  test("Should create a post request and set the body as specified") {
    val postBody = factory.createPostMethod(body)

    postBody.getRequestEntity should be(body)
  }

  test("Content length for the post method is copied from the body") {
    val postBody = factory.createPostMethod(body)
    postBody.getRequestHeader("Content-Length").getValue should be("35")
  }

  test("The post method's host header is determined by the factory") {
    val postBody = factory.createPostMethod(body)
    postBody.getRequestHeader("Host").getValue should be("cheese.com")
  }

  test("The SOAPAction header is a constant and should be 'Create' ") {
    val postBody = factory.createPostMethod(body)
    postBody.getRequestHeader("SOAPAction").getValue should be("Create")
  }

  test("The content-type header is a constant") {
    val postBody = factory.createPostMethod(body)
    postBody.getRequestHeader("Content-Type").getValue should be("text/xml; charset=utf-8")
  }


}
