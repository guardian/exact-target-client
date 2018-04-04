package com.gu.email.exacttarget

import com.gu.email.GuardianUser
import org.scalatest.{BeforeAndAfterEach, FunSuite, Matchers}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import java.io.IOException

import org.apache.http.{HttpResponse, StatusLine}
import org.apache.http.client.fluent.{Request, Response}
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient

import scala.collection.JavaConversions._


class TriggeredEmailServiceTest extends FunSuite with Matchers with MockitoSugar with BeforeAndAfterEach {

  val postMethod = mock[Request]
  val requestEntity = mock[StringEntity]
  val createSoapAction = "Create"
  val retrieveSoapAction = "Retrieve"
  val response = mock[Response]
  val httpResponse = mock[HttpResponse]
  val statusLine = mock[StatusLine]
  val mockSoapFactory = mock[ExactTargetFactory]
  val responseEntity = new StringEntity("hello")
  val user = GuardianUser("jon_balls", "jon.balls@test.com")

  override protected def beforeEach(): Unit = {
    when(postMethod.execute()).thenReturn(response)
    when(response.returnResponse()).thenReturn(httpResponse)
    when(httpResponse.getStatusLine).thenReturn(statusLine)
    when(httpResponse.getEntity).thenReturn(responseEntity)
    when(statusLine.getStatusCode).thenReturn(200)
  }

  override protected def afterEach(): Unit = {
    reset(mockSoapFactory, postMethod)
  }

  test("Should construct a soap envelope from the params and send it off in a post request") {

    val mockSoapFactory = mock[ExactTargetFactory]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]

    when(emailRequest.getDelegate).thenReturn(requestEntity)
    when(mockSoapFactory.createRequest("jon.balls@test.com", Map(("Field_A" -> "jon_balls")), createSoapAction, "abusinessUnitId", "anEmailRemplate")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest.getDelegate, createSoapAction)).thenReturn(postMethod)

    val service = new ExactTargetSoapApiService(mockSoapFactory)
    service.sendEmailRequest(user, "abusinessUnitId", "anEmailRemplate")

    verify(postMethod).execute()
  }

  test("Should construct a soap envelope for an email list request and then send it off in a post request") {
    val emailListRequest = mock[EmailListForUserRequest]
    when(emailListRequest.getDelegate).thenReturn(requestEntity)
    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest.delegate, retrieveSoapAction)).thenReturn(postMethod)

    val service = new ExactTargetSoapApiService(mockSoapFactory)
    service.getEmailRequestsForUser(user, "abusinessUnitId")

    verify(postMethod).execute()
  }

  test("Should return a soap document constructed from the response of the post method") {


    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[CloseableHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponseDocument = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest("jon.balls@test.com", Map(("Field_A" -> "jon_balls")),createSoapAction, "abusinessUnitId", "anEmailRemplate")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest.delegate, createSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(httpResponse)).thenReturn(expectedResponseDocument)

    val service = new ExactTargetSoapApiService(mockSoapFactory)
    val actualResponseDocument = service.sendEmailRequest(user, "abusinessUnitId", "anEmailRemplate")

    actualResponseDocument should be(expectedResponseDocument)
  }

  test("Should return a soap document constructed from the response of the post method for get emails for user") {


    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[CloseableHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailListRequest = mock[EmailListForUserRequest]
    val expectedResponseDocument = mock[EmailListForUserResponse]

    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest.delegate, retrieveSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createEmailListResponseDocument(httpResponse)).thenReturn(expectedResponseDocument)

    val service = new ExactTargetSoapApiService(mockSoapFactory)
    val actualResponseDocument = service.getEmailRequestsForUser(user, "abusinessUnitId")

    actualResponseDocument should be(expectedResponseDocument)
  }


  test("Should throw an exception if the response code is not 200") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[CloseableHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponse = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest("jon.balls@test.com", Map(("Field_A" -> "jon_balls")), createSoapAction, "abusinessUnitId", "anEmailRemplate")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest.delegate, createSoapAction)).thenReturn(postMethod)
    when(statusLine.getStatusCode).thenReturn(500)
    when(mockSoapFactory.createResponseDocument(httpResponse)).thenReturn(expectedResponse)

    val service = new ExactTargetSoapApiService(mockSoapFactory)
    an [ExactTargetException] should be thrownBy service.sendEmailRequest(user, "abusinessUnitId", "anEmailRemplate")
  }


  test("Should throw an exception if the response code is not 200 when retrieving an emailList") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[CloseableHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailListRequest = mock[EmailListForUserRequest]
    val expectedResponseDocument = mock[EmailListForUserResponse]

    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest.delegate, retrieveSoapAction)).thenReturn(postMethod)
    when(statusLine.getStatusCode).thenReturn(500)
    val service = new ExactTargetSoapApiService(mockSoapFactory)
    an [ExactTargetException] should be thrownBy service.getEmailRequestsForUser(user, "abusinessUnitId")
  }


  test("Should wrap IO exception thrown by http client") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[CloseableHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponseDocument = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest("jon.balls@test.com", Map(("Field_A" -> "jon_balls")),createSoapAction, "abusinessUnitId", "anEmailRemplate")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest.delegate,createSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(httpResponse)).thenReturn(expectedResponseDocument)

    val ioException = new IOException
    when(postMethod.execute()).thenThrow(ioException)

    val service: ExactTargetSoapApiService = new ExactTargetSoapApiService(mockSoapFactory)
    an [ExactTargetException] should be thrownBy service.sendEmailRequest(user, "abusinessUnitId", "anEmailRemplate")
  }

}
