package net.rmj.android.ohfeedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class AndroidOpenHouseFeedbackActivity extends Activity  {
	
	//ArrayList<FeedbackQuestion> questions;
	//FeedbackAdapter fAdapter;
	ListView fListView;
	boolean load = false;
	Activity mActivity;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
        /* ArrayList<FeedbackQuestion> questions = new ArrayList<FeedbackQuestion>();
        questions.add(new FeedbackQuestion("Interest",0));
        questions.add(new FeedbackQuestion("Price",0));
        questions.add(new FeedbackQuestion("Interior",0));
        questions.add(new FeedbackQuestion("Exterior Look",0));
        questions.add(new FeedbackQuestion("Paint",0));
        questions.add(new FeedbackQuestion("Neighborhood",0));
        questions.add(new FeedbackQuestion("Yard",0));
        */
        //... more questions.... 
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button bLoc = (Button)findViewById(R.id.btnLocation);
        bLoc.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			onSearchRequested();
        		}
        });
        Button bFeedback = (Button)findViewById(R.id.btnOhNew);
        bFeedback.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AndroidOpenHouseFeedbackActivity.this ,NewPropertyActivity.class);
	   			startActivity(intent);
	   			
			}
        	
        });
        /*mActivity = this;
        
        //this.inflate(resLayout, parent, false);
        Button bb = (Button)findViewById(R.id.btnSubmit);
        bb.setOnClickListener( new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!load) {
					long locationId = 1;
					ReadLocationQuestions task = new ReadLocationQuestions(mActivity);
					task.execute(new String[]{String.valueOf(locationId)});
				}
			}
        	
        }); */
        
        /*
        fListView = (ListView)this.findViewById(R.id.myFeedbackQuestions);
        fAdapter = new FeedbackAdapter(this,R.layout.feedback_list,questions);
        fListView.setAdapter(fAdapter);
        */
        //fListView.setI
        
        //ListView v = (ListView)this.findViewById(R.id.myFeedbackQuestions);
        
       
        
       
    }
    
}