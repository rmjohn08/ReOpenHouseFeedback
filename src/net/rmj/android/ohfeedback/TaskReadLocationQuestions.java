package net.rmj.android.ohfeedback;

import java.util.ArrayList;

import net.rmj.android.ohfeedback.dataaccess.QuestionsFeedbackDao;
import net.rmj.android.ohfeedback.model.Questionaire;
import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

/**
 * AsyncTask that will take care of reading questions for location
 * @author YRJ0002
 *
 */
public class TaskReadLocationQuestions extends OhAsyncTaskBase {
	
	
	private ArrayList<Questionaire> questions;
	
	public TaskReadLocationQuestions(Activity activity) {
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
		long locId = Long.parseLong(params[0]);
		QuestionsFeedbackDao dao = new QuestionsFeedbackDao(context);
		try {
			dao.openDatabase();
			questions = dao.getLocationFeedbackQuestions(locId);
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
	
	
	/**
	 * Post execute actions
	 */
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if (result.equalsIgnoreCase(OhConstants.SUCCESS)) {
			//ListView fListView = (ListView)activity.findViewById(R.id.locationQuestions);
			ListView fListView = (ListView)activity.findViewById(R.id.myFeedbackQuestions);
			//ScrollView fListView = (ScrollView)activity.findViewById(R.id.myFeedbackQuestions);
	        
	        //FeedbackAdapter fAdapter = new FeedbackAdapter(context,R.layout.questions_list,questions);
			FeedbackAdapter fAdapter = new FeedbackAdapter(context,R.layout.feedback_list,questions);
	        fListView.setAdapter(fAdapter);
	        
	        //if (dialog.isShowing()) dialog.dismiss();
	        //Toast.makeText(context, "Please answer questions.", Toast.LENGTH_SHORT);
		
		} else {
		
			Toast.makeText(context, result, Toast.LENGTH_LONG);
		
		}
		
	}
	
	
	
}
