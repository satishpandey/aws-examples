package com.aws.example.lambda;

import java.util.List;

class RunCommandDetails {

	private String document;
	private List<String> instanceIds;

	@Override
	public String toString() {
		return "{ Document:" + document + ", InstanceIds:" + instanceIds + " }";
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public List<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}

}
