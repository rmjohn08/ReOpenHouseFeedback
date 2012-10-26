package net.rmj.android.ohfeedback;


import java.util.ArrayList;

import net.rmj.android.ohfeedback.dataaccess.QuestionsFeedbackDao;
import net.rmj.android.ohfeedback.model.Questionaire;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetLocationQuestionsActivity extends Activity {
	long locationId =0;
	protected ArrayList<Long> selQuestions=new ArrayList<Long>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.questions);
		
		locationId = getIntent().getLongExtra(OhConstants.LOCATION_ID_NAME, 0);
		
		TaskQuestionsToSet task = new TaskQuestionsToSet(this);
		task.execute(new String[]{OhConstants.ACTION_READALL,String.valueOf(locationId)});
		
		Button saveQuestions = (Button)this.findViewById(R.id.btnSubmitQuestions);
		saveQuestions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveQuestionsChoices();
			}
			
		});
		

	}
	
	public void endActivity() {
		this.finish();
	}
	
	public void chkButtonClickHandler(View vw) {
		          
		        //reset all the listView items background colours 
		        //before we set the clicked one..

		        /*ListView lvItems = getListView();
		        for (int i=0; i < lvItems.getChildCount(); i++) 
		        {
		            lvItems.getChildAt(i).setBackgroundColor(Color.BLUE);        
		        }*/
		        
		        
		        //get the row the clicked button is in
		        LinearLayout vwParentRow = (LinearLayout)vw.getParent();
		         
		        TextView child = (TextView)vwParentRow.getChildAt(0);
		        CheckBox chkChild = (CheckBox)vwParentRow.getChildAt(1);
		        Long value = (Long)chkChild.getTag();
		        if (chkChild.isChecked() ) {
		        	if (value !=null || !selQuestions.contains(value)) {
		        		selQuestions.add(value);
		        		Toast.makeText(this, "YOU clicked on "+value.toString(),Toast.LENGTH_SHORT );
		        	}
		        	
		        }
		        
		        //btnChild.setText(child.getText());
		        //btnChild.setText("I've been clicked!");
		        
		        //int c = Color.CYAN;
		        
		        //vwParentRow.setBackgroundColor(c); 
		        //vwParentRow.refreshDrawableState();       

	}
	
	public ArrayList<Long> getQuestionsSelected() {
		return selQuestions;
	}
	
	private boolean saveQuestionsChoices() {
		
		
		SaveQuestionsTask task = new SaveQuestionsTask(this);
		//task.setQuestions(questions);
		task.execute(new String[]{OhConstants.ACTION_SAVE, String.valueOf(locationId)});
		return true;
	}
	
	class SaveQuestionsTask extends OhAsyncTaskBase {
		
		public SaveQuestionsTask (Activity act) {
			super(act);
			
		}
		
		protected String doInBackground(String... params) {
			// 
			if (params == null) {
				return "No parameters passed.  Nothing read.  Cannot continue";
				
			}
			
			long locId = Long.parseLong(params[1]);
			return saveLocationQuestions(locId);
			
			
		}	
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result.equalsIgnoreCase(OhConstants.SUCCESS)) {
				
				// Toast.makeText(context, "Please answer questions.", Toast.LENGTH_SHORT);
				endActivity();
			
			} else {
			
				//Toast.makeText(context, result, Toast.LENGTH_LONG);
				Log.i(OhConstants.OH_TAG, result);
				
			}
			
		}
		
		private String saveLocationQuestions(long locId) {
//			/long locId = Long.parseLong(params[1]);
			String strResult="";
			QuestionsFeedbackDao dao = new QuestionsFeedbackDao(context);
			try {
				ArrayList<Questionaire> questions = transformQuestionairies();
				
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
		
		private ArrayList<Questionaire> transformQuestionairies() {
			ArrayList<Questionaire> questions = new ArrayList<Questionaire>();
			if (selQuestions==null || selQuestions.isEmpty()) return null;
			for (Long val : selQuestions) {
				Questionaire q = new Questionaire();
				q.setQuestionId(val.longValue());
				questions.add(q);
			}
			return questions;
		}
		
		
	}
	
}
