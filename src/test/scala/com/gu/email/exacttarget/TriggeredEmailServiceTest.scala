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

  test("Should construct a soap envelope from the params and send it off in a post request") {

    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]

    when(mockSoapFactory.createRequest(user)).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest)).thenReturn(postMethod)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(200)
    when(postMethod.getRequestEntity()).thenReturn(requestEntity);

    val service = new TriggeredEmailService(mockSoapFactory, mockHttpClient)
    service.sendEmailRequest(user)

    verify(mockHttpClient).executeMethod(postMethod)
  }

  test("Should return a soap document constructed from the response of the post method") {

    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponseDocument = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest(user)).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponseDocument)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(200)

    val service = new TriggeredEmailService(mockSoapFactory, mockHttpClient)
    val actualResponseDocument = service.sendEmailRequest(user)

    actualResponseDocument should be(expectedResponseDocument)
  }

  test("Should throw an exception if the response code is not 200") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponse = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest(user)).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponse)
    when(mockHttpClient.executeMethod(postMethod)).thenReturn(500)

    val service = new TriggeredEmailService(mockSoapFactory, mockHttpClient)
    evaluating { service.sendEmailRequest(user) } should produce [ExactTargetException]
  }


  test("Should wrap IO exception thrown by http client") {
    val mockSoapFactory = mock[ExactTargetFactory]
    val mockHttpClient = mock[HttpClient]

    val user = GuardianUser("jon_balls", "jon.balls@test.com")
    val emailRequest = mock[TriggeredEmailRequest]
    val expectedResponseDocument = mock[TriggeredEmailResponse]

    when(mockSoapFactory.createRequest(user)).thenReturn(emailRequest)
    when(mockSoapFactory.createPostMethod(emailRequest)).thenReturn(postMethod)
    when(mockSoapFactory.createResponseDocument(postMethod)).thenReturn(expectedResponseDocument)

    val ioException = new IOException
    when(mockHttpClient.executeMethod(postMethod)).thenThrow(ioException)

    val service: TriggeredEmailService = new TriggeredEmailService(mockSoapFactory, mockHttpClient)
    evaluating { service.sendEmailRequest(user) } should produce [ExactTargetException]
  }


}
