package net.rmj.android.ohfeedback;


import java.util.ArrayList;

import net.rmj.android.ohfeedback.dataaccess.DAOUtil;
import net.rmj.android.ohfeedback.dataaccess.QuestionsFeedbackDao;
import net.rmj.android.ohfeedback.model.BaseLocationDetailActivity;
import net.rmj.android.ohfeedback.model.Location;
import net.rmj.android.ohfeedback.model.LocationQuestion;
import net.rmj.android.ohfeedback.model.Questionaire;
import android.app.Activity;
import android.content.ContentValues;
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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.DatabaseConnection;

public class SetLocationQuestionsActivity extends BaseLocationDetailActivity {
	long locationId =0;
	protected ArrayList<Long> selQuestions=new ArrayList<Long>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setContentView(R.layout.questions);
        super.onCreate(savedInstanceState);

        if (getToolBar()!=null) {
            getToolBar().setNavigationIcon(R.drawable.ic_action_back);

        }

        locationId = getIntent().getLongExtra(OhConstants.LOCATION_ID_NAME, 0);
		
		//TaskQuestionsToSet task = new TaskQuestionsToSet(this);
		//task.execute(new String[]{OhConstants.ACTION_READALL,String.valueOf(locationId)});
		ReadQuestionsTask task = new ReadQuestionsTask(this);
		task.execute(new String[]{OhConstants.ACTION_READALL,String.valueOf(locationId)});
		
		Button saveQuestions = (Button)this.findViewById(R.id.btnSubmitQuestions);
		saveQuestions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (saveQuestionsChoices()) {
                    Toast.makeText(v.getContext(), "Questions saved",Toast.LENGTH_SHORT).show();
                    endActivity();
                }

			}
			
		});


	}
	
	public void endActivity() {
		this.finish();
	}

	/**
	 * this function is used to set handle the click on the checkbox button. 
	 * this is set on the XML layout file -- one technique 
	 * @param vw
	 */
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

        CheckBox chkChild = (CheckBox)vwParentRow.getChildAt(1);
        Long value = (Long)chkChild.getTag();
        if (chkChild.isChecked() ) {
            if (value !=null || !selQuestions.contains(value)) {
                selQuestions.add(value);
                Log.i(OhConstants.OH_TAG,"YOU clicked on "+value.toString());

            }

        } else if (selQuestions.contains(value)) {
            //remove from the list
            selQuestions.remove(value);
        }


	}
	
	public ArrayList<Long> getQuestionsSelected() {
		return selQuestions;
	}
	
	private boolean saveQuestionsChoices() {

        boolean result = false;
        Dao<LocationQuestion, Long> locQuestionDao = null;
        DatabaseConnection con = null;

        try {
            locQuestionDao = new DAOUtil().getInstance().getDAO(LocationQuestion.class);

            Dao<Location, Long> loc = DAOUtil.getInstance().getDAO(Location.class);
            Location propertyLocation = loc.queryForId(locationId);

            ArrayList<Questionaire> questions = transformQuestionairies();

            con = locQuestionDao.startThreadConnection();
            locQuestionDao.setAutoCommit(con,false);

            DeleteBuilder deleteBuild = locQuestionDao.deleteBuilder();
            deleteBuild.where().eq("location_id", locationId);
            locQuestionDao.delete(deleteBuild.prepare());

            for (Questionaire qn : questions) {
                LocationQuestion lq = new LocationQuestion();
                lq.setLocation(propertyLocation);
                lq.setQuestion(qn);

                locQuestionDao.createOrUpdate(lq);

            }

            locQuestionDao.commit(con);
            result =true;

        }  catch(Exception ex) {

            Log.e(OhConstants.OH_TAG, "Error in transaction for location_questions dao");

            try {
                locQuestionDao.rollBack(con);
            }catch(Exception ex1 ) {
                ex1.printStackTrace();
            }


        } finally {
            try {
                locQuestionDao.setAutoCommit(con, true);
            } catch(Exception ex2) {
                ex2.printStackTrace();
            }

            //con.closeQuietly();
            //con=null;

        }

        return result;

	}


	
	/**
	 * Class that will take care reading questions from database
	 * @author yrj0002
	 *
	 */
	class ReadQuestionsTask extends TaskQuestionsToSet {
		public ReadQuestionsTask(Activity ac) {
			super(ac);
		}
		
		
		protected String doInBackground(String... params) {
			
			String result = super.doInBackground(params);
			
			return result;
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			if (result.equalsIgnoreCase(OhConstants.SUCCESS)) {
				//at this point should get the list from DB
				super.onPostExecute(result);
				ArrayList<Questionaire> questions = this.getQuestions();
				
				if (questions==null) return ;
				
				for (Questionaire q : questions) {
					if (q.isSelected()) selQuestions.add(new Long(q.getQuestionId()));
					
				}
				
			} else {
				Log.i(OhConstants.ERROR, "An unexpected result " + result);
				
			}
		}
		
		
	}
	
	/**
	 * class that will take care of saving questions into database.
	 * @author yrj0002
	 *
	 */
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
