package com.aws.example;

import com.amazonaws.services.lambda.runtime.Context;

public class SimpleLambdaFunction {

	public String lambdaFunction(String input, Context context) {
		String message = String.format("Lambda function executed successfully with input %s!", input);
		context.getLogger().log(message);
		return message;
	}
}