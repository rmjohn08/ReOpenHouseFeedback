package net.rmj.android.ohfeedback;

import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.Location;
import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * AsyncTask that will take care of reading questions for location
 * @author YRJ0002
 *
 */
public class TaskSaveLocationDetail extends OhAsyncTaskBase {
	
	
	private Location location;
	
	public TaskSaveLocationDetail(Activity activity) {
		//this.activity = activity;
		//this.context = this.activity;
		//dialog = new ProgressDialog(context);
		super(activity);
	}
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	
	@Override
	protected String doInBackground(String... params) {
		// 
		if (params == null || location==null) {
			return "No parameters passed.  Nothing read.  Cannot continue";
			
		}
		
		/* for other actions String actionType = params[0];
		if (actionType.equals(OhConstants.ACTION_SEARCH)) {
			String query = params[1];
			doSearch(query);
		} */
		
		String strResult = "";
		String action = params[0];
		// TODO Auto-generated method stub
		if (action.equals(OhConstants.ACTION_SAVE))
			return saveLocation();
		else 
			return "";
	}
	
	private String saveLocation() {
		LocationDao dao = new LocationDao(context);
		String strResult="";
		try {
			dao.openDatabase();
			if (location.getLocationId()>0)
				dao.updateLocation(location);
			else 
				dao.addLocation(location);
			
		
		}  catch(Exception ex) {
			Log.e(OhConstants.OH_TAG, "Error testing dao");
			ex.printStackTrace();
			strResult = OhConstants.EXCEPTION;
		} finally {
			dao.closeDatabase();
		}
		
		if (strResult.equals("")) strResult = OhConstants.SUCCESS;
		
		return strResult;
	}
	
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if (result.equalsIgnoreCase(OhConstants.SUCCESS)) {
			Toast.makeText(context, "Location Saved", Toast.LENGTH_LONG);
			
		} else {
		
			Toast.makeText(context, result, Toast.LENGTH_LONG);
		
		}
		
		
	}
	
	protected void setTextField(EditText txt, String string) {
		txt.setText(string);
		
	}
	
	
	
}
