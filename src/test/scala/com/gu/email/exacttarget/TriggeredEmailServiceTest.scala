package com.gu.email.exacttarget

import com.gu.email.GuardianUser
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import java.io.IOException

import org.apache.http.HttpEntity
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient

import scala.collection.JavaConversions._


class TriggeredEmailServiceTest extends FunSuite with Matchers with MockitoSugar {

  val postMethod = mock[HttpPost]
  val requestEntity = mock[StringEntity]
  val createSoapAction = "Create"
  val retrieveSoapAction = "Retrieve"
  val closeableHttpResponse = mock[CloseableHttpResponse]


  test("Should construct a soap envelope from the params and send it off in a post request") {

    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[DefaultHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    when(emailRequest.delegate).thenReturn(requestEntity)

    when(mockSoapFactory.createRequest("jon.balls@test.com", Map(("Field_A" -> "jon_balls")), createSoapAction, "abusinessUnitId", "anEmailRemplate")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest.delegate,createSoapAction)).thenReturn(postMethod)
    when(mockHttpClient.execute(postMethod)).thenReturn(closeableHttpResponse)
    when(postMethod.getEntity()).thenReturn(requestEntity)

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    service.sendEmailRequest(user, "abusinessUnitId", "anEmailRemplate")

    verify(mockHttpClient).execute(postMethod)
  }

  test("Should construct a soap envelope for an email list request and then send it off in a post request") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[DefaultHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailListRequest = mock[EmailListForUserRequest]
    when(emailListRequest.delegate).thenReturn(requestEntity)

    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest.delegate, retrieveSoapAction)).thenReturn(postMethod)
    when(mockHttpClient.execute(postMethod)).thenReturn(closeableHttpResponse)
    when(postMethod.getEntity()).thenReturn(requestEntity);

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    service.getEmailRequestsForUser(user, "abusinessUnitId")

    verify(mockHttpClient).execute(postMethod)
  }

  test("Should return a soap document constructed from the response of the post method") {


    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[DefaultHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponseDocument = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest("jon.balls@test.com", Map(("Field_A" -> "jon_balls")),createSoapAction, "abusinessUnitId", "anEmailRemplate")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest.delegate, createSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponseDocument)
    when(mockHttpClient.execute(postMethod)).thenReturn(closeableHttpResponse)

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    val actualResponseDocument = service.sendEmailRequest(user, "abusinessUnitId", "anEmailRemplate")

    actualResponseDocument should be(expectedResponseDocument)
  }

  test("Should return a soap document constructed from the response of the post method for get emails for user") {


    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[DefaultHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailListRequest = mock[EmailListForUserRequest]
    val expectedResponseDocument = mock[EmailListForUserResponse]

    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest.delegate, retrieveSoapAction)).thenReturn(postMethod)
    when(mockHttpClient.execute(postMethod)).thenReturn(closeableHttpResponse)
    when(mockSoapFactory.createEmailListResponseDocument(postMethod)).thenReturn(expectedResponseDocument)
    when(postMethod.getEntity()).thenReturn(requestEntity)

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    val actualResponseDocument = service.getEmailRequestsForUser(user, "abusinessUnitId")

    actualResponseDocument should be(expectedResponseDocument)
  }


  test("Should throw an exception if the response code is not 200") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[DefaultHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponse = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest("jon.balls@test.com", Map(("Field_A" -> "jon_balls")), createSoapAction, "abusinessUnitId", "anEmailRemplate")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest.delegate, createSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponse)
    when(mockHttpClient.execute(postMethod)).thenReturn(closeableHttpResponse)

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    an [ExactTargetException] should be thrownBy service.sendEmailRequest(user, "abusinessUnitId", "anEmailRemplate")
  }


  test("Should throw an exception if the response code is not 200 when retrieving an emailList") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[DefaultHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailListRequest = mock[EmailListForUserRequest]
    val expectedResponseDocument = mock[EmailListForUserResponse]

    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest.delegate, retrieveSoapAction)).thenReturn(postMethod)
    when(mockHttpClient.execute(postMethod)).thenReturn(closeableHttpResponse)

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    an [ExactTargetException] should be thrownBy service.getEmailRequestsForUser(user, "abusinessUnitId")
  }


  test("Should wrap IO exception thrown by http client") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[DefaultHttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponseDocument = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest("jon.balls@test.com", Map(("Field_A" -> "jon_balls")),createSoapAction, "abusinessUnitId", "anEmailRemplate")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest.delegate,createSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponseDocument)

    val ioException = new IOException
    when(mockHttpClient.execute(postMethod)).thenThrow(ioException)

    val service: ExactTargetSoapApiService = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    an [ExactTargetException] should be thrownBy service.sendEmailRequest(user, "abusinessUnitId", "anEmailRemplate")
  }
}
