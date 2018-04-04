package com.gu.email.exacttarget;

import com.gu.email.AccountDetails;
import com.gu.email.GuardianUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Map;


public class ExactTargetFactory {
    public final URI endPoint;
    private final AccountDetails accountDetails;

    public ExactTargetFactory(String accountName, String password, URI endPoint) {
        accountDetails = new AccountDetails(accountName, password);
        this.endPoint = endPoint;
    }

    public Request createPostMethod(StringEntity body, String soapAction) {
        return Request
                .Post(endPoint)
                .addHeader("Host", endPoint.getHost())
                .addHeader("SOAPAction", soapAction)
                .body(body);
    }

    public TriggeredEmailRequest createRequest(String emailAddress, Map<String, String> attributes, String soapAction, String businessUnitId, String emailTemplate) {
        TriggeredEmailRequest triggeredRequest = new TriggeredEmailRequest(accountDetails, businessUnitId, emailTemplate, emailAddress, attributes, soapAction);
        return triggeredRequest;
    }

    @Deprecated
    public EmailListForUserRequest createListForUserRequest(GuardianUser guardianUser, String businessUnitId) {
        EmailListForUserRequest emailListForUserRequest = new EmailListForUserRequest(accountDetails, businessUnitId, guardianUser.email());
        return emailListForUserRequest;
    }

    public EmailListForUserRequest createListForUserRequest(String emailAddress, String businessUnitId) {
        EmailListForUserRequest emailListForUserRequest = new EmailListForUserRequest(accountDetails, businessUnitId, emailAddress);
        return emailListForUserRequest;
    }

    public TriggeredEmailResponse createResponseDocument(HttpResponse response) throws ExactTargetException {
        Document responseDocument = getDocumentFromHttpResponse(response);
        return new TriggeredEmailResponse(responseDocument);
    }

    public EmailListForUserResponse createEmailListResponseDocument(HttpResponse response) throws ExactTargetException {
        Document responseDocument = getDocumentFromHttpResponse(response);
        return new EmailListForUserResponse(responseDocument);
    }

    private Document getDocumentFromHttpResponse(HttpResponse response) throws ExactTargetException {
        String bodyAsString = null;
        try {
            bodyAsString = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new ExactTargetException("Error extracting body from post response", e);
        }
        Document responseDocument = null;
        try {
            responseDocument = new SAXBuilder().build(new ByteArrayInputStream(bodyAsString.getBytes()));
        } catch (JDOMException e) {
            throw new ExactTargetException("DOM Error parsing post response into xml document " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ExactTargetException("I/O Error parsing post response into xml document", e);
        }
        return responseDocument;
    }
}