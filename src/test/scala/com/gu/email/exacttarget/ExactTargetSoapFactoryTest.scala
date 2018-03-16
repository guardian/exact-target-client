package com.gu.email.exacttarget

import org.scalatest.FunSuite
import org.scalatest.Matchers
import java.io.OutputStream
import java.net.URI

import org.apache.http.Header
import org.apache.http.entity.{ContentType, StringEntity}
import org.apache.http.message.BasicHeader

class ExactTargetSoapFactoryTest extends FunSuite with Matchers {

  val factory = new ExactTargetFactory("", "", new URI("http://cheese.com/wensleydale"))
  val createSoapAction = "Create"

  val body = new StringEntity("hello", ContentType.create("text/xml", "UTF-8")) {
    override def isRepeatable() = false;

    def writeRequest(out: OutputStream): Unit = {}

    override def getContentLength() = 35

    override def getContentType: Header = new BasicHeader("Content-Type", "")
  }

  // irrelevant comment to trigger the build
  test("Should create a post request and set the body as specified") {
    val postBody = factory.createPostMethod(body, createSoapAction)

    postBody.getEntity should be(body)
  }

  test("Content length for the post method is copied from the body") {
    val postBody = factory.createPostMethod(body, createSoapAction)
    postBody.getFirstHeader("Content-Length").getValue should be("35")
  }

  test("The post method's host header is determined by the factory") {
    val postBody = factory.createPostMethod(body, createSoapAction)
    postBody.getFirstHeader("Host").getValue should be("cheese.com")
  }

  test("The SOAPAction header is a constant and should be 'Create' ") {
    val postBody = factory.createPostMethod(body, createSoapAction)
    postBody.getFirstHeader("SOAPAction").getValue should be("Create")
  }

  test("The content-type header is a constant") {
    val postBody = factory.createPostMethod(body, createSoapAction)
    postBody.getFirstHeader("Content-Type").getValue should be("text/xml; charset=utf-8")
  }


}
