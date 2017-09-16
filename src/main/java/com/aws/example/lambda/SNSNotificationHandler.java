package com.aws.example.lambda;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;

public class SNSNotificationHandler implements RequestHandler<SNSEvent, String> {

	public String handleRequest(SNSEvent event, Context context) {
		List<SNSRecord> records = event.getRecords();
		SNSRecord record = records.get(0);
		SNS sns = record.getSNS();
		String message = sns.getMessage();
		String messageStr = String.format("Lambda function executed successfully with input %s!", message);
		context.getLogger().log(messageStr);
		return message;
	}
}