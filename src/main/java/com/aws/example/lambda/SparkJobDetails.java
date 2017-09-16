package com.aws.example.lambda;

class SparkJobDetails {

	private String input;
	private String output;
	private String masterInstanceId;

	@Override
	public String toString() {
		return "{ Input:" + input + ", Output:" + output + ", MasterInstanceId:" + masterInstanceId + " }";
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getMasterInstanceId() {
		return masterInstanceId;
	}

	public void setMasterInstanceId(String masterInstanceId) {
		this.masterInstanceId = masterInstanceId;
	}

}
