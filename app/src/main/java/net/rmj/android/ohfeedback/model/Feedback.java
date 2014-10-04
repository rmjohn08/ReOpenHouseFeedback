package net.rmj.android.ohfeedback.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="feedback")
public class Feedback {
    @DatabaseField(generatedId=true)
    private long id;

	private long feedbackId;
    @DatabaseField(columnName = "qn_id")
    private long questionId;
    @DatabaseField(columnName = "loc_id")
    private long locationId;
    @DatabaseField(columnName = "response_text")
    private String responseText;
    @DatabaseField(columnName = "response_no")
    private double responseNo;
	private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
