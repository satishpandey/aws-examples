package com.aws.example.sts;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class AssumeRoleInstanceProfileExample {

	private static String roleArn = "arn:aws:iam::455892055944:role/SQSFullAccessRole";
	private static String roleSessionName = "MyRoleSession";

	static AmazonSQS sqs;

	private static void init() throws Exception {
		InstanceProfileCredentialsProvider initialCredentialsProvider = new InstanceProfileCredentialsProvider(true);
		AWSSecurityTokenService sts = AWSSecurityTokenServiceClientBuilder.standard()
				.withCredentials(initialCredentialsProvider).build();
		STSAssumeRoleSessionCredentialsProvider credentialsProvider = new STSAssumeRoleSessionCredentialsProvider.Builder(
				roleArn, roleSessionName).withStsClient(sts).build();
		sqs = AmazonSQSClientBuilder.standard().withCredentials(credentialsProvider).build();
	}

	public static void main(String[] args) throws Exception {
		init();

		try {
			String queueName = "MY-TEST-QUEUE";
			System.out.println("Creating queue : " + queueName);
			CreateQueueResult createQueueResult = sqs.createQueue(queueName);
			System.out.println("Queue created successfully!");
			System.out.println("Sending message to queue...");
			sqs.sendMessage(createQueueResult.getQueueUrl(), "First message to SQS queue!");
			System.out.println("Message succesfully sent to SQS queue!");
			System.out.println("Receiving message from queue url : " + createQueueResult.getQueueUrl());
			ReceiveMessageResult messageResult = sqs.receiveMessage(createQueueResult.getQueueUrl());
			System.out.println("SQS queue message received!");
			for (Message message : messageResult.getMessages()) {
				System.out.println("Message from queue: " + message.getBody());
			}
			System.out.println("Deleting queue url : " + createQueueResult.getQueueUrl());
			sqs.deleteQueue(createQueueResult.getQueueUrl());
			System.out.println("Queue deleted!");
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

	}
}
