package net.rmj.android.ohfeedback;

import net.rmj.android.ohfeedback.dataaccess.DAOUtil;
import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.Location;
import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;

/**
 * AsyncTask that will take care of reading questions for location
 * @author YRJ0002
 *
 */
public class TaskReadLocationDetail extends OhAsyncTaskBase {
	
	
	private Location location;
	
	public TaskReadLocationDetail(Activity activity) {
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
        long locId = Long.parseLong(params[0]);

    try {

        Dao<Location, Long> locDao = DAOUtil.getInstance().getDAO(Location.class);
        QueryBuilder<Location, Long> query = locDao.queryBuilder();
        location = locDao.queryForId(locId);


    } catch(SQLException sqe) {

        strResult = OhConstants.EXCEPTION;

    }
        /* LocationDao dao = new LocationDao(context);
        try {
			dao.openDatabase();
			location = dao.getLocation(locId);
			if (location==null ) {
				strResult = "No location found.";
				Log.i(OhConstants.OH_TAG, "No location found, there is a problem.");
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
	
	
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if (result.equalsIgnoreCase(OhConstants.SUCCESS) && location !=null) {
			//EditText address = (EditText)activity.findViewById(R.id.txtAddress);
			//address.setText(location.getAddress());	
			
			setTextField((EditText)activity.findViewById(R.id.txtAddress),location.getAddress());
			setTextField((EditText)activity.findViewById(R.id.txtCity),location.getCity());
			setTextField((EditText)activity.findViewById(R.id.txtState),location.getState());
			setTextField((EditText)activity.findViewById(R.id.txtPin),location.getPinNo());
			setTextField((EditText)activity.findViewById(R.id.txtZipcode),location.getZipcode());
			setTextField((EditText)activity.findViewById(R.id.txtSellerEmail),location.getSellerEmail());
			
		} else {
		
			Toast.makeText(context, result, Toast.LENGTH_LONG);
		
		}
		
		
	}
	
	protected void setTextField(EditText txt, String string) {
		txt.setText(string);
		
	}
	
	
	
}
