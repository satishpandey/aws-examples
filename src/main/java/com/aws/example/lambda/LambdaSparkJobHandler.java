package com.aws.example.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.SendCommandRequest;
import com.amazonaws.services.simplesystemsmanagement.model.SendCommandResult;
import com.amazonaws.util.json.Jackson;

public class LambdaSparkJobHandler {

	// Example message content
	// {"input":"s3a://spark.data.com/shakespeare-1-100.txt","output":"s3a://spark.data.com/output1","masterInstanceId":"i-0e511b28f98727523"}
	public String runCommandHandler(SNSEvent event, Context context) {
		List<SNSRecord> records = event.getRecords();
		SNSRecord record = records.get(0);
		SNS sns = record.getSNS();
		String message = sns.getMessage();
		String messageStr = String.format("SNS Message %s", message);
		context.getLogger().log(messageStr);
		SparkJobDetails sparkJobDetails = Jackson.fromJsonString(message, SparkJobDetails.class);
		EnvironmentVariableCredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
		AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.standard().withCredentials(provider)
				.build();
		SendCommandRequest sendCommandRequest = new SendCommandRequest();
		String runCommandDocument = System.getenv("document");
		context.getLogger().log(String.format("Run Command Document: %s", runCommandDocument));
		sendCommandRequest.setDocumentName(runCommandDocument);
		sendCommandRequest.setInstanceIds(stringToList(sparkJobDetails.getMasterInstanceId()));
		Map<String, List<String>> parameters = new HashMap<String, List<String>>();
		parameters.put("input", stringToList(sparkJobDetails.getInput()));
		parameters.put("output", stringToList(sparkJobDetails.getOutput()));
		sendCommandRequest.setParameters(parameters);
		SendCommandResult commandResult = ssm.sendCommand(sendCommandRequest);
		context.getLogger().log(String.format("Command response log: %s", commandResult.toString()));
		return commandResult.toString();
	}

	private List<String> stringToList(String value) {
		List<String> list = new ArrayList<String>();
		list.add(value);
		return list;
	}
}