package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.apache.commons.httpclient.methods.RequestEntity
import java.io.OutputStream
import java.net.URI

class ExactTargetSoapFactoryTest extends FunSuite with Matchers {

  val factory = new ExactTargetFactory("", "", new URI("http://cheese.com/wensleydale"))
  val createSoapAction = "Create"

  val body = new RequestEntity() {
    def isRepeatable() = false;

    def writeRequest(out: OutputStream): Unit = {}

    def getContentLength() = 35

    def getContentType: String = "cheese;balls"
  }

  // irrelevant comment to trigger the build
  test("Should create a post request and set the body as specified") {
    val postBody = factory.createPostMethod(body, createSoapAction)

    postBody.getRequestEntity should be(body)
  }

  test("Content length for the post method is copied from the body") {
    val postBody = factory.createPostMethod(body, createSoapAction)
    postBody.getRequestHeader("Content-Length").getValue should be("35")
  }

  test("The post method's host header is determined by the factory") {
    val postBody = factory.createPostMethod(body, createSoapAction)
    postBody.getRequestHeader("Host").getValue should be("cheese.com")
  }

  test("The SOAPAction header is a constant and should be 'Create' ") {
    val postBody = factory.createPostMethod(body, createSoapAction)
    postBody.getRequestHeader("SOAPAction").getValue should be("Create")
  }

  test("The content-type header is a constant") {
    val postBody = factory.createPostMethod(body, createSoapAction)
    postBody.getRequestHeader("Content-Type").getValue should be("text/xml; charset=utf-8")
  }


}
