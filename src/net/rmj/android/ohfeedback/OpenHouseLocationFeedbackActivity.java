package net.rmj.android.ohfeedback;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class OpenHouseLocationFeedbackActivity extends Activity {
	
	ListView fListView;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.feedback);
        fListView = (ListView)this.findViewById(R.id.myFeedbackQuestions);
        
        long locationId = getIntent().getLongExtra(OhConstants.LOCATION_ID_NAME, 0);
		TaskReadLocationQuestions task = new TaskReadLocationQuestions(this);
		task.execute(new String[]{String.valueOf(locationId)});
		
    }
}
