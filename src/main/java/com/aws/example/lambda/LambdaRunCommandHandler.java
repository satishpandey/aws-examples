package com.aws.example.lambda;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.SendCommandRequest;
import com.amazonaws.services.simplesystemsmanagement.model.SendCommandResult;

public class LambdaRunCommandHandler {

	public String runCommandHandler(RunCommandDetails command, Context context) {

		String message = String.format("Command Input -> %s!", command);
		context.getLogger().log(message);
		EnvironmentVariableCredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
		AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.standard().withCredentials(provider)
				.build();
		SendCommandRequest sendCommandRequest = new SendCommandRequest();
		sendCommandRequest.setDocumentName(command.getDocument());
		sendCommandRequest.setInstanceIds(command.getInstanceIds());
		SendCommandResult commandResult = ssm.sendCommand(sendCommandRequest);
		context.getLogger().log(String.format("Command response log: %s", commandResult.toString()));
		return commandResult.toString();
	}
}