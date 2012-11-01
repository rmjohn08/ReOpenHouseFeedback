package net.rmj.android.ohfeedback.model;

public class Feedback {
	private long feedbackId;
	private long questionId;
	private long locationId;
	private String responseText;
	private double responseNo;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(long feedbackId) {
		this.feedbackId = feedbackId;
	}
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	public String getResponseText() {
		return responseText;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	public double getResponseNo() {
		return responseNo;
	}
	public void setResponseNo(double responseNo) {
		this.responseNo = responseNo;
	}
	
	
}
