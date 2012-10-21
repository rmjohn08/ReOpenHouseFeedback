package net.rmj.android.ohfeedback.model;

public class FeedbackQuestion {
	private String question;
	private int rating;
	private long questionId;
	private String type;
	
	
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public FeedbackQuestion() {}
	public FeedbackQuestion(String question, int rating) {
		this.question = question;
		this.rating = rating;
		
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
