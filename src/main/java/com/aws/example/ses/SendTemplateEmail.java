package com.aws.example.ses;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.CreateTemplateRequest;
import com.amazonaws.services.simpleemail.model.CreateTemplateResult;
import com.amazonaws.services.simpleemail.model.DeleteTemplateRequest;
import com.amazonaws.services.simpleemail.model.DeleteTemplateResult;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailResult;
import com.amazonaws.services.simpleemail.model.Template;

public class SendTemplateEmail {

    private static String userKey = "PutYourKeyHere";
    private static String userSecret = "PutYourSecretHere";
    private static String senderEmail = "satishpandey.soft@gmail.com";
    private static String templateName = "ContactNameTemplate";
    private static String emailText = "Hello {{user}} - I am Satish from CloudTechPro.";
    private static Pair<String, String> contact = new Pair<String, String>("satish@cloudtechpro.com", "{ \"user\":\"Satish\" }");

    private static AmazonSimpleEmailService ses;

    private static void loadClient() throws Exception {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(userKey, userSecret);
        AWSStaticCredentialsProvider initialCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        ses = AmazonSimpleEmailServiceClientBuilder.standard()
            .withCredentials(initialCredentialsProvider)
            .withRegion("us-east-1")
            .build();
    }

    public static void main(String[] args) throws Exception {
        loadClient();
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
        template.setSubjectPart("Test mail");
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
        Destination destination = new Destination();
        List<String> toAddresses = new ArrayList<String>();
        toAddresses.add(contact.getKey());
        destination.setToAddresses(toAddresses);
        SendTemplatedEmailRequest templatedEmailRequest = new SendTemplatedEmailRequest();
        templatedEmailRequest.withDestination(destination);
        templatedEmailRequest.withTemplate(templateName);
        templatedEmailRequest.withTemplateData(contact.getValue());
        templatedEmailRequest.withSource(senderEmail);
        SendTemplatedEmailResult templatedEmailResult = ses.sendTemplatedEmail(templatedEmailRequest);
        System.out.println(templatedEmailResult.getMessageId());
    }
}
