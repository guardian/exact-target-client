package com.gu.email.exacttarget

import com.gu.email.GuardianUser
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.RequestEntity;
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import java.io.IOException


class TriggeredEmailServiceTest extends FunSuite with ShouldMatchers with MockitoSugar {

  val postMethod = mock[PostMethod]
  val requestEntity = mock[RequestEntity]
  val createSoapAction = "Create"
  val retrieveSoapAction = "Retrieve"

  test("Should construct a soap envelope from the params and send it off in a post request") {

    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]

    when(mockSoapFactory.createRequest(user, createSoapAction, "abusinessUnitId")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest,createSoapAction)).thenReturn(postMethod)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(200)
    when(postMethod.getRequestEntity()).thenReturn(requestEntity);

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    service.sendEmailRequest(user, "abusinessUnitId")

    verify(mockHttpClient).executeMethod(postMethod)
  }

  test("Should construct a soap envelope for an email list request and then send it off in a post request") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailListRequest = mock[EmailListForUserRequest]

    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest, retrieveSoapAction)).thenReturn(postMethod)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(200)
    when(postMethod.getRequestEntity()).thenReturn(requestEntity);

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    service.getEmailRequestsForUser(user, "abusinessUnitId")

    verify(mockHttpClient).executeMethod(postMethod)
  }

  test("Should return a soap document constructed from the response of the post method") {


    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponseDocument = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest(user,createSoapAction, "abusinessUnitId")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest, createSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponseDocument)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(200)

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    val actualResponseDocument = service.sendEmailRequest(user, "abusinessUnitId")

    actualResponseDocument should be(expectedResponseDocument)
  }

  test("Should return a soap document constructed from the response of the post method for get emails for user") {


    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailListRequest = mock[EmailListForUserRequest]
    val expectedResponseDocument = mock[EmailListForUserResponse]

    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest, retrieveSoapAction)).thenReturn(postMethod)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(200)
    when(mockSoapFactory.createEmailListResponseDocument(postMethod)).thenReturn(expectedResponseDocument)
    when(postMethod.getRequestEntity()).thenReturn(requestEntity);

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    val actualResponseDocument = service.getEmailRequestsForUser(user, "abusinessUnitId")

    actualResponseDocument should be(expectedResponseDocument)
  }


  test("Should throw an exception if the response code is not 200") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponse = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest(user, createSoapAction, "abusinessUnitId")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest, createSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponse)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(500)

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    evaluating { service.sendEmailRequest(user, "abusinessUnitId") } should produce [ExactTargetException]
  }


  test("Should throw an exception if the response code is not 200 when retrieving an emailList") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailListRequest = mock[EmailListForUserRequest]
    val expectedResponseDocument = mock[EmailListForUserResponse]

    when(mockSoapFactory.createListForUserRequest(user, "abusinessUnitId")).thenReturn(emailListRequest)
    when(mockSoapFactory.createPostMethod(emailListRequest, retrieveSoapAction)).thenReturn(postMethod)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(500)

    val service = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    evaluating { service.getEmailRequestsForUser(user, "abusinessUnitId") } should produce [ExactTargetException]
  }


  test("Should wrap IO exception thrown by http client") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponseDocument = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest(user,createSoapAction, "abusinessUnitId")).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest,createSoapAction)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponseDocument)

    val ioException = new IOException
    when(mockHttpClient.executeMethod(postMethod)).thenThrow(ioException)

    val service: ExactTargetSoapApiService = new ExactTargetSoapApiService(mockSoapFactory, mockHttpClient)
    evaluating { service.sendEmailRequest(user, "abusinessUnitId") } should produce [ExactTargetException]
  }
}
