package com.aws.example.ses;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.BulkEmailDestination;
import com.amazonaws.services.simpleemail.model.CreateTemplateRequest;
import com.amazonaws.services.simpleemail.model.CreateTemplateResult;
import com.amazonaws.services.simpleemail.model.DeleteTemplateRequest;
import com.amazonaws.services.simpleemail.model.DeleteTemplateResult;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendBulkTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.SendBulkTemplatedEmailResult;
import com.amazonaws.services.simpleemail.model.Template;

public class SendBulkTemplatedEmail {

    private static String userKey = "PutYourAWSKeyHere";
    private static String userSecret = "PutYourAWSSecretHere";
    private static String senderEmail = "satishpandey.soft@gmail.com";
    private static String templateName = "ContactNameTemplate";
    private static String emailText = "Hello {{user}} - I am Satish from CloudTechPro.";
    private static List<Pair<String, String>> contacts = new ArrayList<Pair<String, String>>();
    private static String dataFormat = "{ \"user\":\"%s\" }";

    private static AmazonSimpleEmailService ses;

    private static void init() throws Exception {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(userKey, userSecret);
        AWSStaticCredentialsProvider initialCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        ses = AmazonSimpleEmailServiceClientBuilder.standard()
            .withCredentials(initialCredentialsProvider)
            .withRegion("us-east-1")
            .build();
        contacts.add(new Pair<String, String>("satish@cloudtechpro.com", "Satish-CloudTechPro"));
        contacts.add(new Pair<String, String>("satishpandey.soft@gmail.com", "Satish-Gmail"));
    }

    public static void main(String[] args) throws Exception {
        init();
        createTemplate();
        try {
            sendEmail();
        } finally {
            deleteTemplate();
        }
    }

    private static void createTemplate() {
        Template template = new Template();
        template.setTemplateName(templateName);
        template.setSubjectPart("Test mail1");
        template.setTextPart(emailText);
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest();
        createTemplateRequest.setTemplate(template);
        CreateTemplateResult result = ses.createTemplate(createTemplateRequest);
        System.out.println(result.getSdkResponseMetadata());
    }

    private static void deleteTemplate() {
        DeleteTemplateRequest deleteTemplateRequest = new DeleteTemplateRequest();
        deleteTemplateRequest.setTemplateName(templateName);
        DeleteTemplateResult result = ses.deleteTemplate(deleteTemplateRequest);
        System.out.println(result.getSdkResponseMetadata());
    }

    private static void sendEmail() {
        List<BulkEmailDestination> bulkEmailDestinations = new ArrayList<BulkEmailDestination>();
        for (Pair<String, String> contact : contacts) {
            Destination destination = new Destination();
            List<String> toAddresses = new ArrayList<String>();
            toAddresses.add(contact.getKey());
            destination.setToAddresses(toAddresses);
            BulkEmailDestination bulkEmailDestination = new BulkEmailDestination();
            bulkEmailDestination.setDestination(destination);
            bulkEmailDestination.setReplacementTemplateData(String.format(dataFormat, contact.getValue()));
            bulkEmailDestinations.add(bulkEmailDestination);
        }
        SendBulkTemplatedEmailRequest bulkTemplatedEmailRequest = new SendBulkTemplatedEmailRequest();
        bulkTemplatedEmailRequest.withDestinations(bulkEmailDestinations);
        bulkTemplatedEmailRequest.withTemplate(templateName);
        bulkTemplatedEmailRequest.withDefaultTemplateData(String.format(dataFormat, "UserNameNotAvailable"));
        bulkTemplatedEmailRequest.withSource(senderEmail);
        SendBulkTemplatedEmailResult bulkTemplatedEmailResult = ses.sendBulkTemplatedEmail(bulkTemplatedEmailRequest);
        System.out.println(bulkTemplatedEmailResult.getStatus());
    }
}
