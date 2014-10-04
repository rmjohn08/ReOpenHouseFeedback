package net.rmj.android.ohfeedback;

import java.io.DataOutput;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.rmj.android.ohfeedback.dataaccess.DAOUtil;
import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.dataaccess.QuestionsFeedbackDao;
import net.rmj.android.ohfeedback.model.Location;
import net.rmj.android.ohfeedback.model.LocationQuestion;
import net.rmj.android.ohfeedback.model.Questionaire;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

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
	
	/**
	 * Read the questions
	 * @param locId
	 * @return
	 */
	protected String readQuestions(long locId) {
		//Log.i(OhConstants.OH_TAG, "Unknown Action " );
		//return "Unknown Action";
		//long locId = Long.parseLsong(params[0]);

        String strResult="";
        questions = new ArrayList<Questionaire>();

        try {
            Dao<Questionaire, Long> qDao = DAOUtil.getInstance().getDAO(Questionaire.class);
            ArrayList<Questionaire> allQuestions = (ArrayList<Questionaire>)qDao.queryForAll();
            if (allQuestions==null || allQuestions.isEmpty()){

                return OhConstants.SUCCESS;
            }

            Dao<Location,Long> locationDao = DAOUtil.getInstance().getDAO(Location.class);
            Location loc = locationDao.queryForId(locId);

            List<Long> selected = new ArrayList<Long>();
            if (loc.getLocationQuestions()!=null && !loc.getLocationQuestions().isEmpty()) {

                for(LocationQuestion lq : loc.getLocationQuestions()) {
                    //questions.add(lq.getQuestion());
                    lq.getQuestion().setSelected(true);
                    selected.add(lq.getQuestion().getId());
                }
            }

            questions.addAll(allQuestions);
            if (!selected.isEmpty()){
                int pos=0;
                for (Questionaire q : questions){
                    int idx = selected.indexOf(q.getId());
                    if(idx>=0) {
                        //questions.add(loc.getLocationQuestions().get(idx).getQuestion());
                        questions.set(pos,loc.getLocationQuestions().get(idx).getQuestion());
                        Log.i(OhConstants.OH_TAG, questions.get(pos).isSelected()?"selected":"no selected");
                    }
                    pos++;
                }
            }

            /* more complicated way of doing it using query but it does not get desired result
            Dao<LocationQuestion, Long> lDao = DAOUtil.getInstance().getDAO(LocationQuestion.class);

            QueryBuilder<LocationQuestion, Long> queryLocation = lDao.queryBuilder();
            QueryBuilder<Questionaire, Long> queryQuestion = qDao.queryBuilder();
            Where<LocationQuestion, Long> whereLoc = queryLocation.where();

            if (!selected.isEmpty()) {
                queryLocation.where().notIn("question_id",selected );
            }

            whereLoc.or(
                    whereLoc.eq("location_id", locId),
                    whereLoc.isNull("question_id")
            );

            queryQuestion.leftJoin(queryLocation);

            ArrayList<Questionaire> allQuestions = (ArrayList<Questionaire>)queryQuestion.query();

            */

        } catch(SQLException sqe) {
            Log.e(OhConstants.OH_TAG,sqe.getMessage());
            sqe.printStackTrace();
            strResult = OhConstants.EXCEPTION;
        }

		if (strResult.equals("")) strResult = OhConstants.SUCCESS;
		// TODO Auto-generated method stub
		return strResult;
	}
	
	/**
	 * Read All questions
	 * @return
	 */
	protected String readAllQuestions() {
		//Log.i(OhConstants.OH_TAG, "Unknown Action " );
		//return "Unknown Action";
		//long locId = Long.parseLsong(params[0]);
		String strResult="";
        try {

            Dao<Questionaire, Long> qDao = DAOUtil.getInstance().getDAO(Questionaire.class);
            //QueryBuilder<Questionaire, Long> query = qDao.queryBuilder();
            questions = (ArrayList<Questionaire>)qDao.queryForAll();

        } catch(SQLException sqe) {
            Log.e(OhConstants.OH_TAG,sqe.getMessage());
            sqe.printStackTrace();
            strResult = OhConstants.EXCEPTION;

        }

        /*
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
		*/

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
	        //Toast.makeText(context, "Please answer questions.", Toast.LENGTH_SHORT);
		
		} else {
		
			//Toast.makeText(context, result, Toast.LENGTH_LONG);
			Log.i(OhConstants.OH_TAG, result);
			
		}
		
	}
	
	
	
}
