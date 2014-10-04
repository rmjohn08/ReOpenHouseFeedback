package net.rmj.android.ohfeedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NewPropertyActivity extends LocationDetailActivity {

	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	    
    	super.onCreate(savedInstanceState);
    	setButtonVisibility(false);
    	
    	//set evet listener for button
		 Button btnSave = (Button)this.findViewById(R.id.btnSaveLocDetail);
		 btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveLocationDetails((Activity)v.getContext());
				resetScreen();
			}
			 
		 }); 	
    	
    	
	}
	
	private void setButtonVisibility(boolean visible) {
		
		if (btnFeedback==null)  btnFeedback = (Button)this.findViewById(R.id.btnFeedback);
		if (btnSetQuestion==null) btnSetQuestion = (Button)this.findViewById(R.id.btnSetQuestions);
		
		if (visible) {
			this.btnFeedback.setVisibility(View.VISIBLE);
			this.btnSetQuestion.setVisibility(View.VISIBLE);
			
		} else {
			this.btnFeedback.setVisibility(View.INVISIBLE);
	    	this.btnSetQuestion.setVisibility(View.INVISIBLE);
	    	
		}
	}
	
	private void resetScreen() {
		this.setButtonVisibility(true);
		this.setButtonsOnClickListener();
		
		
	}
	
	
	
}
