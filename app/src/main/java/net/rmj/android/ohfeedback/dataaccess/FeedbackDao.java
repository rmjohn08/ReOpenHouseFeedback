package net.rmj.android.ohfeedback.dataaccess;

import java.util.ArrayList;
import java.util.List;

import net.rmj.android.ohfeedback.model.Feedback;
import android.content.ContentValues;
import android.content.Context;

public class FeedbackDao extends OpenHouseFeedbackDao {

	List<String> allColumns;
	private final String FEEDBACK_TABLE = "feedback";
	
	public FeedbackDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setAllColumns();
		
	}
	
	protected void setAllColumns() {
		allColumns = new ArrayList<String>();
		allColumns.add("_id");
		allColumns.add("qn_id");
		allColumns.add("loc_id");
		allColumns.add("response_text");
		allColumns.add("response_no");
		allColumns.add("feedback_name");
		
		
	}
	
	public void addFeedback(Feedback fe) {
		ContentValues values = new ContentValues();
		setFeedbackContentValues(values, fe,false);
		long newId = db.insert(this.FEEDBACK_TABLE, null, values);
		fe.setFeedbackId(newId);
		
	}
	
	/**
	 * sets the content value
	 * @param values
	 * @param fe
	 * @param setPk
	 */
	protected void setFeedbackContentValues(ContentValues values, Feedback fe, boolean setPk) {
		int idx=0;
		if (setPk) 
			values.put(allColumns.get(idx++), fe.getFeedbackId());
		else
			idx++;
		
		values.put(allColumns.get(idx++),fe.getQuestionId());
		values.put(allColumns.get(idx++),fe.getLocationId());
		values.put(allColumns.get(idx++),fe.getResponseText());
		values.put(allColumns.get(idx++),fe.getResponseNo());
		values.put(allColumns.get(idx++),fe.getName());
		
	}
	
	

}
