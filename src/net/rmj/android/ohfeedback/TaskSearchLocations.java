package net.rmj.android.ohfeedback;

import java.util.ArrayList;

import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.Location;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

/**
 * AsyncTask that will take care of reading questions for location
 * @author YRJ0002
 *
 */


public class TaskSearchLocations extends OhAsyncTaskBase {
	
	ArrayList<Location> results = null;
	
	public TaskSearchLocations(Activity activity) {
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
		
		/* for other actions */
		String query = params[0];
		String strResult = "";
		
		
		//long locId = Long.parseLong(params[0]);
		LocationDao dao = new LocationDao(context);
		try {	
			dao.openDatabase();
			results = dao.findLocations(query);
			
			if (results==null || results.isEmpty()) {
				strResult = "No locations found.";
				Log.i(OhConstants.OH_TAG, "No locations found.");
			}
		
		}  catch(Exception ex) {
			Log.e(OhConstants.OH_TAG, "Error "+ex.toString());
			ex.printStackTrace();
			strResult = OhConstants.EXCEPTION;
		
		} finally {
			dao.closeDatabase();
		}
		
		if (strResult.equals("")) strResult = OhConstants.SUCCESS;
		// TODO Auto-generated method stub
		return strResult;
		
	}
		

	protected void onPostExecute(String result) {
		
		if (result.equalsIgnoreCase(OhConstants.SUCCESS)) {
			ListActivity ac =(ListActivity)this.activity;
			ListView fListView = ac.getListView();
			//ListView fListView = (ListView)this.activity.findViewById(R.id.srchResultList);
	        
			if (fListView!=null) {
				SearchLocationAdapter fAdapter = new SearchLocationAdapter(context,R.layout.search_result_list, results);
				fListView.setAdapter(fAdapter);
			} else { 
				Toast.makeText(context, "Problem with List View.", Toast.LENGTH_SHORT);
			}
		
		} else {
		
			Toast.makeText(context, result, Toast.LENGTH_LONG);
		
		}
		
		if (dialog.isShowing()) dialog.dismiss();
		
	}
	
	
	
}
