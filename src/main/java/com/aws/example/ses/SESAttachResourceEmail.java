package com.aws.example.ses;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;

public class SESAttachResourceEmail {

    private static String userKey = "PutYourKeyHere";
    private static String userSecret = "PutYourSecretHere";
    private static String senderEmail = "satishpandey.soft@gmail.com";
    private static String[] contact = { "satish@cloudtechpro.com", "satishpandey.soft@gmail.com" };
    private static String subject = "Test email with inline image";
    private static URL imagePath = SESAttachResourceEmail.class.getResource("/cloudtechpro-logo.png");
    private static StringBuffer body = new StringBuffer("<html><h1>Email with inline image</h1></html>");

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

    public static void sendEmail() throws Exception {

        Session session = Session.getInstance(new Properties());

        // creates a new e-mail message
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject);

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(body.toString(), "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds inline image
        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.setDisposition(MimeBodyPart.ATTACHMENT);
        imagePart.attachFile(imagePath.getFile());

        multipart.addBodyPart(imagePart);

        message.setContent(multipart);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);
        RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

        // Send the email.
        SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
        rawEmailRequest.withDestinations(contact);
        rawEmailRequest.withSource(senderEmail);
        SendRawEmailResult result = ses.sendRawEmail(rawEmailRequest);
        System.out.println(result);
    }

}