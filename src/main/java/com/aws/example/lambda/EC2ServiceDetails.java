package com.aws.example.lambda;

class EC2ServiceDetails {

	private String action;
	private String instanceId;

	@Override
	public String toString() {
		return "{ Action:" + action + ", InstanceId:" + instanceId + " }";
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
