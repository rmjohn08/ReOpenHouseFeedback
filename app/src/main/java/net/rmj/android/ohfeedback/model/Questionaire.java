package net.rmj.android.ohfeedback.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="questionaire")
public class Questionaire {
    @DatabaseField(generatedId=true)
    private long id;

    @DatabaseField(columnName = "question")
    private String question;

    @DatabaseField(columnName = "qn_type")
    private String type;

	//these are used to responses to questions
    private long questionId;
    private float rating;
	private String text;
	private boolean selected;

    public long getId() { return id;}
    public void setId(long id) { this.id= id;}

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
		return id;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
        this.id = this.questionId;
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
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	
}
