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
import com.google.gson.Gson;

public class LambdaEC2ServiceController {

	// Example message content
	// {"action":"start", "instanceId":"i-0a601229eeae401d0"}
	public String runCommandHandler(SNSEvent event, Context context) {
		try {
			List<SNSRecord> records = event.getRecords();
			SNSRecord record = records.get(0);
			SNS sns = record.getSNS();
			String message = sns.getMessage();
			String messageStr = String.format("SNS Message : %s", message);
			context.getLogger().log(messageStr);
			Gson gson = new Gson();
			EC2ServiceDetails serviceDetails = gson.fromJson(message, EC2ServiceDetails.class);
			context.getLogger().log("ServiceDetails: " + serviceDetails);
			EnvironmentVariableCredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
			AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.standard()
					.withCredentials(provider).build();
			SendCommandRequest sendCommandRequest = new SendCommandRequest();
			String runCommandDocument = System.getenv("document");
			context.getLogger().log(String.format("Run Command Document: %s", runCommandDocument));
			sendCommandRequest.setDocumentName(runCommandDocument);
			sendCommandRequest.setInstanceIds(stringToList(serviceDetails.getInstanceId()));
			Map<String, List<String>> parameters = new HashMap<String, List<String>>();
			parameters.put("action", stringToList(serviceDetails.getAction()));
			sendCommandRequest.setParameters(parameters);
			SendCommandResult commandResult = ssm.sendCommand(sendCommandRequest);
			context.getLogger().log(String.format("Command response log: %s", commandResult.toString()));
			return commandResult.toString();
		} catch (Exception exception) {
			context.getLogger().log("Exception: " + exception.getMessage());
			// exception.printStackTrace();
			return null;
		}
	}

	private List<String> stringToList(String value) {
		List<String> list = new ArrayList<String>();
		list.add(value);
		return list;
	}
}