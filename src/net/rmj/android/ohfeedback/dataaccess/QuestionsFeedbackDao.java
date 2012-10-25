package net.rmj.android.ohfeedback.dataaccess;

import java.util.ArrayList;
import java.util.List;

import net.rmj.android.ohfeedback.OhConstants;
import net.rmj.android.ohfeedback.model.Questionaire;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class QuestionsFeedbackDao extends OpenHouseFeedbackDao {

	public final String QUESTION_PK = "_id";
	public final String QUESTION_TEXT = "question";
	public final String QUESTION_TYPE = "qn_type";
	public final String QUESTION_TABLE = "questionaire";
	public final String QUESTION_LOC_TABLE = "location_question";
	public final String LOC_QN = "qn_id";
	public final String LOC_QN_LOC = "loc_id";
	
	public final String questionColumns[] = {QUESTION_PK, QUESTION_TEXT,QUESTION_TYPE};
	
	public QuestionsFeedbackDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * list all questions
	 * @return
	 */
	public ArrayList<Questionaire> getAllQuestions() {
		ArrayList<Questionaire> list = new ArrayList<Questionaire>();
		Cursor cursor = db.query(QUESTION_TABLE, questionColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			list.add(buildQuestion(cursor));
			cursor.moveToNext();
		}
		
		return list;
	}
	
	/**
	 * add a new question record
	 * @param qn
	 */
	public void addQuestion(Questionaire qn) {
		ContentValues values = new ContentValues();
		values.put(QUESTION_TEXT, qn.getQuestion());
		values.put(this.QUESTION_TYPE, qn.getType());
		
		long newId = db.insert(this.QUESTION_TABLE, null, values);
		qn.setQuestionId(newId);
		
		
	}
	
	public Questionaire getQuestion(long pk) {
		String selection = this.QUESTION_PK + " = ?";
		String[] selectArgs = {String.valueOf(pk)};
		Questionaire fq = null;
		Cursor cursor = db.query(QUESTION_TABLE, questionColumns, selection, selectArgs, null, null, QUESTION_TEXT);
		cursor.moveToFirst();
		
		if (!cursor.isAfterLast()) {
			fq = buildQuestion(cursor);
			cursor.moveToNext();
		}
		return fq;
		
		
	}
	/**
	 * update a question record
	 * @param qn
	 * @return
	 */
	public long updateQuestion(Questionaire qn) {
		ContentValues values = new ContentValues();
		values.put(QUESTION_TEXT, qn.getQuestion());
		values.put(this.QUESTION_TYPE, qn.getType());
		
		String whereClause = this.QUESTION_PK + " = ?";
		String[] whereArgs = {String.valueOf(qn.getQuestionId())};
		
		long howMany = db.update(this.QUESTION_TABLE, values, whereClause, whereArgs);
		
		//long newId = db.insert(this.QUESTION_TABLE, null, values);
		//qn.setQuestionId(newId);
		return howMany;
		
	}
	
	/**
	 * delete a question based on the id
	 * @param pk
	 */
	public long deleteQuestion(long pk) {
		String args[] = {String.valueOf(pk)};
		return db.delete(QUESTION_TABLE, this.QUESTION_PK + " = ? ", args);
		
	}
	
	/**
	 * builds a question object
	 * @param cursor
	 * @return
	 */
	protected Questionaire buildQuestion(Cursor cursor) {
		
		Questionaire q = new Questionaire();
		
		q.setQuestionId(cursor.getLong(cursor.getColumnIndex(QUESTION_PK)));
		q.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION_TEXT)));
		q.setType(cursor.getString(cursor.getColumnIndex(QUESTION_TYPE)));
		
		return q;
		
	}
	
	public void testQuestionLocation() {
		long locId = 1;
		List<Questionaire> list = this.getAllQuestions();
		List<Questionaire> qLocs = new ArrayList<Questionaire>();
		for (Questionaire q : list) {
			Questionaire fq = new Questionaire();
			fq.setQuestionId(q.getQuestionId());
			qLocs.add(fq);
			
			
		}
		int added = this.saveLocationQuestions(locId, qLocs);
		if (added==qLocs.size()) {
			Log.i(OhConstants.OH_TAG, "Questions added for locaiton "+locId);
			
		} else {
			Log.i(OhConstants.OH_TAG, "There may be a problem adding locations "+locId);
			return;
		}
		List<Questionaire> lAll = this.getLocationQuestions(locId);
		if (lAll==null || !lAll.isEmpty()) {
			Log.i(OhConstants.OH_TAG, "Questions found");
			
		} else {
			Log.i(OhConstants.OH_TAG, "No questions found, there is a problem.");
		}
		
	}
	
	
	public void testQuestionDao() {
		Questionaire q = new Questionaire();
		q.setQuestion("Continue search");
		q.setType("text");
		this.addQuestion(q);
		
		List<Questionaire> list = this.getAllQuestions();
		if (list==null) {
			Log.i(OhConstants.OH_TAG, "No Questions foudn.... quitting");
			return;
		} else {
			Log.i(OhConstants.OH_TAG, "Questions found....");
		}
		
		q.setQuestion("Decision in one week....");
		long rows = this.updateQuestion(q);
		
		Questionaire fq = this.getQuestion(q.getQuestionId());
		if (fq!=null) 
			Log.i(OhConstants.OH_TAG, "Question found...." + fq.getQuestion());
		else
			Log.i(OhConstants.OH_TAG, "Question not found...." + q.getQuestionId());
			
		
		long deleted = this.deleteQuestion(q.getQuestionId());
		

	}
	
	
	/**
	 * to do method that retrieve questions_location, insert new question_locations, delete questions_locations
	 */
	public ArrayList<Questionaire> getLocationQuestions(long locationId) {
		ArrayList<Questionaire> list = new ArrayList<Questionaire>();
		//String[] fields = {this.LOC_QN,this.LOC_QN_LOC};
		String sql = "select q._id, q.question, q.qn_type from " + 
				this.QUESTION_TABLE + " q inner join " + 
				this.QUESTION_LOC_TABLE + " ql on q._id = ql.qn_id where ql.loc_id = ? ";
		String[] args = {String.valueOf(locationId)};
		Cursor cursor = db.rawQuery(sql,args);
		
		//rawQuery(sql, selectionArgs).query(QUESTION_LOC_TABLE, fields, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			list.add(buildQuestion(cursor));
			cursor.moveToNext();
		}
		
		return list;
		
	}
	
	public int saveLocationQuestions(long locId, List<Questionaire> list) {
		int inserted=0;
		//first remove current then reinsert
		try {
			db.beginTransaction();
			int rows = db.delete(this.QUESTION_LOC_TABLE, this.LOC_QN_LOC + " = " +locId, null);
			for (Questionaire qn : list) {
				ContentValues values = new ContentValues();
				values.put(this.LOC_QN, qn.getQuestionId());
				values.put(this.LOC_QN_LOC, locId);
				
				db.insert(this.QUESTION_LOC_TABLE, null, values);
				inserted++;
			}
			
			db.setTransactionSuccessful();
			
		} catch(Exception ex) {
			Log.e(OhConstants.OH_TAG, "Error saving location questions");
			ex.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return inserted;
		
	}
	
	

}
