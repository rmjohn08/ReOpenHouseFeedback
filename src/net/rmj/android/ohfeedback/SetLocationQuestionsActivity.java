package net.rmj.android.ohfeedback;


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SetLocationQuestionsActivity extends Activity {
	long locationId =0;
	protected ArrayList<Long> selQuestions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.questions);
		
		locationId = getIntent().getLongExtra(OhConstants.LOCATION_ID_NAME, 0);
		
		TaskQuestionsToSet task = new TaskQuestionsToSet(this);
		task.execute(new String[]{OhConstants.ACTION_READALL});
		
		Button saveQuestions = (Button)this.findViewById(R.id.btnSubmitQuestions);
		saveQuestions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
		

	}
	
	
	public ArrayList<Long> getQuestionsSelected() {
		return selQuestions;
	}
	
}
