package net.rmj.android.ohfeedback.model;

public class Questionaire {
	private String question;
	private long questionId;
	private String type;
	//these are used to responses to questions
	private int rating;
	private String text;
	private boolean selected;
	
	
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
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
	
	public Questionaire() {}
	public Questionaire(String question, int rating) {
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
