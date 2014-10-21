package net.rmj.android.ohfeedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.rmj.android.ohfeedback.dataaccess.DAOUtil;
import net.rmj.android.ohfeedback.dataaccess.FeedbackDao;
import net.rmj.android.ohfeedback.model.BaseLocationDetailActivity;
import net.rmj.android.ohfeedback.model.Feedback;
import net.rmj.android.ohfeedback.model.Location;
import net.rmj.android.ohfeedback.model.LocationQuestion;
import net.rmj.android.ohfeedback.model.Questionaire;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class OpenHouseLocationFeedback extends BaseLocationDetailActivity {
	
	ListView fListView;
	//ScrollView fListView;
	HashMap<Long,Feedback> ratingMap = new HashMap<Long,Feedback>();
	long locationId=0;
	
	/** Called when the activity is first created. */
   public void onCreateOLD(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.feedback);
        //fListView = (ScrollView)this.findViewById(R.id.myFeedbackQuestions); 
    	fListView = (ListView)this.findViewById(R.id.myFeedbackQuestions);
        
        locationId = getIntent().getLongExtra(OhConstants.LOCATION_ID_NAME, 0);;
        
		TaskReadLocationQuestions task = new TaskReadLocationQuestions(this);
		task.execute(new String[]{String.valueOf(locationId)});
		
		Button btnSubmit = (Button)this.findViewById(R.id.btnSubmitFeedback);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//saveFeedback();
				collectRatings();	
			}
			
		});
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.feedback);
        fListView = (ListView)this.findViewById(R.id.myFeedbackQuestions);
    	//fListView = (ScrollView)this.findViewById(R.id.myFeedbackQuestionsRework);
        
        locationId = getIntent().getLongExtra(OhConstants.LOCATION_ID_NAME, 0);
        /*
		TaskReadLocationQuestions task = new TaskReadLocationQuestions(this);
		task.execute(new String[]{String.valueOf(locationId)});
		*/
        List<Questionaire> locQuestions = readLocationQuestions();
        if (locQuestions!=null && !locQuestions.isEmpty()) {

            ListView fListView = (ListView) this.findViewById(R.id.myFeedbackQuestions);
            //ScrollView fListView = (ScrollView)activity.findViewById(R.id.myFeedbackQuestions);

            //FeedbackAdapter fAdapter = new FeedbackAdapter(context,R.layout.questions_list,questions);
            FeedbackAdapter fAdapter = new FeedbackAdapter(this, R.layout.feedback_list, (ArrayList<Questionaire>)locQuestions);
            fListView.setAdapter(fAdapter);


            Button btnSubmit = (Button) this.findViewById(R.id.btnSubmitFeedback);
            btnSubmit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {
                        collectRatings();
                        saveFeedback();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(v.getContext(), "Error saving Feedback", Toast.LENGTH_LONG);

                    }

                }

            });
        } else {
            Toast.makeText(this, "No questions found", Toast.LENGTH_LONG);

        }
    }

    private List<Questionaire> readLocationQuestions() {
        try {
            Dao lqDao = DAOUtil.getInstance().getDAO(LocationQuestion.class);
            lqDao.queryBuilder().where().eq("question_id",locationId);
            List<LocationQuestion> locQuestions = (List<LocationQuestion>)lqDao.queryBuilder().query();
            if (locQuestions==null)
                return new ArrayList<Questionaire>();

            List<Questionaire> questions = new ArrayList<Questionaire>();
            for (LocationQuestion lq : locQuestions) {

                questions.add(lq.getQuestion());
            }

            return questions;

        }catch(Exception ex) {
            Log.e(OhConstants.OH_TAG,ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
	
	public ListView activityList() {
		return fListView;
	}
    
    
    /**
     * handles the Click on the RatingBar
     * @param
     */
    public void collectRatings() {
    	
    	//LinearLayout ll = ()vw.getParent();
    	ListView lv = (ListView)this.findViewById(R.id.myFeedbackQuestions);
    	
    	int chCount = lv.getChildCount();
    	/* for (int i = 0; i<chCount; i++) {
    		//LinearLayout ll = (LinearLayout)lv.getChildAt(i);
    		RelativeLayout rl = (RelativeLayout)lv.getChildAt(i);
    		LinearLayout ll = (LinearLayout)rl.getChildAt(0);
    		TextView tv = (TextView)ll.getChildAt(1);
    		RatingBar rb = (RatingBar)ll.getChildAt(2);
    		Log.i(OhConstants.OH_TAG, "Text " + tv.getText() + " Rating Bar..."+rb.getRating());
    	} */
    	Adapter adapter = lv.getAdapter();
    	for (int i = 0; i<adapter.getCount(); i++) {
    		Questionaire q = (Questionaire)adapter.getItem(i);
    		Log.i(OhConstants.OH_TAG, "Text " + q.getQuestion() + " Rating Bar..."+q.getRating());
    		Feedback fb = new Feedback();
    		fb.setQuestionId(q.getQuestionId());
    		fb.setResponseNo(q.getRating());
    		fb.setLocationId(locationId);
    		ratingMap.put(q.getQuestionId(), fb);
    		
    	}
    	
    	/*
    	LinearLayout vwParentRow = (LinearLayout)vw.getParent();
        
        TextView child = (TextView)vwParentRow.getChildAt(0);
        RatingBar ratnChild = (RatingBar)vwParentRow.getChildAt(2);
        Long qid = (Long)ratnChild.getTag();
        Feedback fd = new Feedback();
        fd.setLocationId(locationId);
        fd.setResponseNo(ratnChild.getRating());
        fd.setQuestionId(qid);
        ratingMap.put(qid, fd);
        */
    }
    
    /**
     * at this point the feedback is saved. 
     * Several things happen.  1. Feedback is added to the database.
     * 						   2. Feedback may be sent to an external site with DB support and 
     * 							create an entry for the new open house. 
     * 						   3. An email is sent to the owner of the location, if specified
     * 							the owner will receive instant feedback on open house. 
     */
    private void saveFeedback() {
    	/* using async task
    	SaveFeedbackTask task = new SaveFeedbackTask(this);
		task.execute(new String[]{String.valueOf(OhConstants.ACTION_SAVE)});
		*/

        try {
            Dao<Feedback,Long> feedDao = DAOUtil.getInstance().getDAO(Feedback.class);
            for(Long qid : ratingMap.keySet()) {
                Feedback fd = ratingMap.get(qid);
                feedDao.createOrUpdate(fd);
            }

            this.finish();

        }catch(Exception ex){
            Log.e(OhConstants.OH_TAG,ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void doExtraActivitySteps() {
    	Toast.makeText(this, "Feedback saved", Toast.LENGTH_LONG);
    	this.finish();
    	//possible steps from here could be to fire up a service to
    	//send email to owner and send feedback to external service.
    	
    }
    
    
    /**
     * Asynchronus task that will handle Saving the Feedback to the database
     * @author yrj0002
     *
     */
    class SaveFeedbackTask extends OhAsyncTaskBase {
		
		public SaveFeedbackTask (Activity act) {
			super(act);
			
		}
		
		protected String doInBackground(String... params) {
			// 
			if (params == null) {
				return "No parameters passed.  Nothing read.  Cannot continue";
				
			}
			
			//long locId = Long.parseLong(params[1]);
			return saveFeedback(locationId);
			
			
		}	
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result.equalsIgnoreCase(OhConstants.SUCCESS)) {
				// Toast.makeText(context, "Please answer questions.", Toast.LENGTH_SHORT);
				doExtraActivitySteps();
			} else {
			
				//Toast.makeText(context, result, Toast.LENGTH_LONG);
				Log.i(OhConstants.OH_TAG, result);
				
			}
			
		}
		
		
		/**
		 * iterates thru the map of ratings 
		 * @param locId
		 * @return
		 */
		private String saveFeedback(long locId) {
//			/long locId = Long.parseLong(params[1]);
			String strResult="";
			FeedbackDao dao = new FeedbackDao(context);
			try {
				//ArrayList<Questionaire> questions = transformQuestionairies();
				
				dao.openDatabase();
				for(Long qid : ratingMap.keySet()) {
					Feedback fd = ratingMap.get(qid);
					dao.addFeedback(fd);
				}
				
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
    
}
