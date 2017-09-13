package com.aws.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SNSNotificationHandler implements RequestHandler<SNSNotification, String> {

	public String handleRequest(SNSNotification input, Context context) {
		String message = String.format("Lambda function executed successfully with input %s!", input);
		context.getLogger().log(message);
		return message;
	}

}