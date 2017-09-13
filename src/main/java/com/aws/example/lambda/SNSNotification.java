package com.aws.example.lambda;

class SNSNotification {
	private String Type;
	private String Subject;
	private String Message;

	@Override
	public String toString() {
		return "{ Type:" + Type + ", Subject:" + Subject + ", Message:" + Message + " }";
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

}
