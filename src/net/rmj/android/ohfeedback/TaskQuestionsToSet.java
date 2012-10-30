package net.rmj.android.ohfeedback;

import java.util.ArrayList;
import java.util.List;

import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.dataaccess.QuestionsFeedbackDao;
import net.rmj.android.ohfeedback.model.Location;
import net.rmj.android.ohfeedback.model.Questionaire;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

/**
 * AsyncTask that will take care of reading questions for location
 * @author YRJ0002
 *
 */
public class TaskQuestionsToSet extends OhAsyncTaskBase {
	
	
	private ArrayList<Questionaire> questions;
	
	public ArrayList<Questionaire> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Questionaire> questions) {
		this.questions = questions;
	}

	public TaskQuestionsToSet(Activity activity) {
		//this.activity = activity;
		//this.context = this.activity;
		//dialog = new ProgressDialog(context);
		super(activity);
	}
	
	@Override
	protected String doInBackground(String... params) {
		// 
		if (params == null) {
			return "No parameters passed.  Nothing read.  Cannot continue";
			
		}
		
		/* for other actions String actionType = params[0];
		if (actionType.equals(OhConstants.ACTION_SEARCH)) {
			String query = params[1];
			doSearch(query);
		} */
		
		String strResult = "";
		if (params[0].equals(OhConstants.ACTION_READALL)) {
			long locId = Long.parseLong(params[1]);
			return readQuestions(locId);
		} else if (params[0].equals(OhConstants.ACTION_SAVE)) {
			return saveLocationQuestions(Long.parseLong(params[1]));
		} else 
			return params[0]+" action not implemented";
		
		
	}
	
	protected String readQuestions(long locId) {
		//Log.i(OhConstants.OH_TAG, "Unknown Action " );
		//return "Unknown Action";
		//long locId = Long.parseLsong(params[0]);
		String strResult="";
		QuestionsFeedbackDao dao = new QuestionsFeedbackDao(context);
		try {
			dao.openDatabase();
			questions = dao.getLocationQuestions(locId); //.getAllQuestions();
			if (questions==null || questions.isEmpty()) {
				strResult = "No questions found.";
				Log.i(OhConstants.OH_TAG, "No questions found, there is a problem.");
			}
			
			
		
		}  catch(Exception ex) {
			Log.e(OhConstants.OH_TAG, "Error testing dao");
			ex.printStackTrace();
			strResult = OhConstants.EXCEPTION;
		} finally {
			dao.closeDatabase();
		}
		
		if (strResult.equals("")) strResult = OhConstants.SUCCESS;
		// TODO Auto-generated method stub
		return strResult;
	}
	
	protected String readAllQuestions() {
		//Log.i(OhConstants.OH_TAG, "Unknown Action " );
		//return "Unknown Action";
		//long locId = Long.parseLsong(params[0]);
		String strResult="";
		QuestionsFeedbackDao dao = new QuestionsFeedbackDao(context);
		try {
			dao.openDatabase();
			questions = dao.getAllQuestions();
			if (questions==null || questions.isEmpty()) {
				strResult = "No questions found.";
				Log.i(OhConstants.OH_TAG, "No questions found, there is a problem.");
			}
		
		}  catch(Exception ex) {
			Log.e(OhConstants.OH_TAG, "Error testing dao");
			ex.printStackTrace();
			strResult = OhConstants.EXCEPTION;
		} finally {
			dao.closeDatabase();
		}
		
		if (strResult.equals("")) strResult = OhConstants.SUCCESS;
		// TODO Auto-generated method stub
		return strResult;
	}
	
	/*
	 * saves the questions for the location
	 */
	protected String saveLocationQuestions(long locId) {
//		/long locId = Long.parseLong(params[1]);
		String strResult="";
		QuestionsFeedbackDao dao = new QuestionsFeedbackDao(context);
		try {
			dao.openDatabase();
			dao.saveLocationQuestions(locId, questions);
			strResult = OhConstants.SUCCESS;
			
		
		}  catch(Exception ex) {
			Log.e(OhConstants.OH_TAG, "Error testing dao");
			ex.printStackTrace();
			strResult = OhConstants.EXCEPTION;
		} finally {
			dao.closeDatabase();
		}
		return strResult; //(strResult.equals(OhConstants.SUCCESS) ? OhConstants.ACTION_SAVE : OhConstants.FAILED );
	}
	
	private List<Location> doSearch(String query) {
		
		List<Location> results = null;
		LocationDao dao = new LocationDao(context);
		dao.openDatabase();
		results = dao.findLocations(query);
		
		return results;
		
	}
	
	/**
	 * Post execute actions
	 */
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if (result.equalsIgnoreCase(OhConstants.SUCCESS)) {
			
			ListView fListView = (ListView)activity.findViewById(R.id.locationQuestions);
	        //sListView fListView = (ListView)activity.findViewById(R.id.locationQuestions); 
	        QuestionAdapter fAdapter = new QuestionAdapter(context,R.layout.questions_list,questions);
	        fListView.setAdapter(fAdapter);
	        
	        //if (dialog.isShowing()) dialog.dismiss();
	        Toast.makeText(context, "Please answer questions.", Toast.LENGTH_SHORT);
		
		} else {
		
			//Toast.makeText(context, result, Toast.LENGTH_LONG);
			Log.i(OhConstants.OH_TAG, result);
			
		}
		
	}
	
	
	
}
