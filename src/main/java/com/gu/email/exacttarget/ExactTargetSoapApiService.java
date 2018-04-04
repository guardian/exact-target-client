package com.gu.email.exacttarget;

import com.gu.email.GuardianUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExactTargetSoapApiService {
    private final ExactTargetFactory soapFactory;
    private static final Logger LOG = LoggerFactory.getLogger(TriggeredEmailResponse.class);
    private String createSoapAction = "Create";
    private String retrieveSoapAction = "Retrieve";


    public ExactTargetSoapApiService(ExactTargetFactory soapFactory) {
        this.soapFactory = soapFactory;
    }


    public TriggeredEmailResponse sendEmailRequest(String userName, String emailAddress, String businessUnitId, String emailTemplateId) throws ExactTargetException {
        GuardianUser user = new GuardianUser(userName, emailAddress);
        return sendEmailRequest(user, businessUnitId, emailTemplateId);
    }


    public EmailListForUserResponse getEmailRequestsForUser(String userName, String emailAddress, String businessUnitId) throws ExactTargetException {
        GuardianUser guardianUser = new GuardianUser(userName, emailAddress);
        return getEmailRequestsForUser(guardianUser, businessUnitId);
    }

    EmailListForUserResponse getEmailRequestsForUser(GuardianUser guardianUser, String businessUnitId) throws ExactTargetException {

        EmailListForUserRequest emailListForUserRequest = soapFactory.createListForUserRequest(guardianUser, businessUnitId);
        Request postRequest = soapFactory.createPostMethod(emailListForUserRequest.delegate, retrieveSoapAction);

        try {
            final HttpResponse httpResponse = postRequest.execute().returnResponse();
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode >= 300) {
                throw new ExactTargetException(String.format("Recieved non 200 response retrieving email lists for: %s", guardianUser.email()));
            }
            return soapFactory.createEmailListResponseDocument(httpResponse);
        } catch (IOException iox) {
            throw new ExactTargetException("Error sending post request for lists for user", iox);
        }


    }

    TriggeredEmailResponse sendEmailRequest(GuardianUser user, String businessUnitId, String emailTemplateId) throws ExactTargetException {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("Field_A", user.userName());

        return sendEmailRequest(user.email(), attributes, businessUnitId, emailTemplateId);
    }


    public TriggeredEmailResponse sendEmailRequest(String emailAddress, Map<String, String> parameters, String businessUnitId, String emailTemplateId) throws ExactTargetException {
        TriggeredEmailRequest triggeredEmailRequest = soapFactory.createRequest(emailAddress, parameters, createSoapAction, businessUnitId, emailTemplateId);
        Request postRequest = soapFactory.createPostMethod(triggeredEmailRequest.getDelegate(), createSoapAction);

        try {
            final HttpResponse httpResponse = postRequest.execute().returnResponse();
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode < 200 || responseCode >= 300) {
                throw new ExactTargetException("Received non 200 response: " + responseCode + "\n" + EntityUtils.toString(httpResponse.getEntity()));
            }
            LOG.debug("Triggered email response:" + EntityUtils.toString(httpResponse.getEntity()));
            return soapFactory.createResponseDocument(httpResponse);
        } catch (IOException e) {
            throw new ExactTargetException("Error sending post request", e);
        }
    }

}
