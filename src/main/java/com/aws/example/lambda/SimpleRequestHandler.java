package com.aws.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SimpleRequestHandler implements RequestHandler<String, String> {

	public String handleRequest(String input, Context context) {
		String message = String.format("Lambda function executed successfully with input %d!", input);
		context.getLogger().log(message);
		return message;
	}

}