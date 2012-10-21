package net.rmj.android.ohfeedback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * AsyncTask that will take care of reading questions for location
 * @author YRJ0002
 *
 */
abstract class OhAsyncTaskBase extends AsyncTask<String, Void, String> {
	protected Context context;
	protected ProgressDialog dialog;
	protected Activity activity;
	
	
	public OhAsyncTaskBase(Activity activity) {
		this.activity = activity;
		this.context = this.activity;
		dialog = new ProgressDialog(context);
	}
	
	@Override
	protected String doInBackground(String... params) {	
		return null;
	}
	
	protected void onPreExecute() {
		dialog.setMessage("Please wait...");
		dialog.show();
		
	}
	
	protected void onPostExecute(String result) { 
		if (dialog!=null && dialog.isShowing())	dialog.dismiss();
		
	}
	
	protected void onCancelled() {
		if (dialog.isShowing())	dialog.dismiss();
		
	}
	
	
}

