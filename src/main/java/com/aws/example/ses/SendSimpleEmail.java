package com.aws.example.ses;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

public class SendSimpleEmail {

    private static String userKey = "PutYourKeyHere";
    private static String userSecret = "PutYourSecretHere";
    private static String senderEmail = "satishpandey.soft@gmail.com";
    private static String contact = "satish@cloudtechpro.com";
    private static String subject = "Test email";
    private static String emailText = "Hello - I am Satish from CloudTechPro.";

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
        sendEmail();
    }

    private static void sendEmail() {
        // Prepare destination
        Destination destination = new Destination();
        List<String> toAddresses = new ArrayList<String>();
        toAddresses.add(contact);
        destination.setToAddresses(toAddresses);

        // Prepare message
        Body body = new Body();
        body.setText(new Content(emailText));
        Message message = new Message();
        message.setSubject(new Content(subject));
        message.setBody(body);

        // Sending email
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.withSource(senderEmail);
        sendEmailRequest.withDestination(destination);
        sendEmailRequest.withMessage(message);
        SendEmailResult emailResult = ses.sendEmail(sendEmailRequest);
        System.out.println(emailResult.getMessageId());
    }
}
