package net.rmj.android.ohfeedback;

import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.Location;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationDetailActivity extends Activity {
	  long locationId =0;
	  Location thisLocation=new Location();
	  
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 this.setContentView(R.layout.location_detail);
		 locationId = getIntent().getLongExtra(OhConstants.LOCATION_ID_NAME, 0);
		 
		 try {
			 TaskReadLocationDetail task = new TaskReadLocationDetail(this);
			 task.execute(new String[]{String.valueOf(locationId)});
		 } catch(Exception ex) {
			 ex.printStackTrace();
		 }
		 
		 //set evet listener for button
		 Button btnSave = (Button)this.findViewById(R.id.btnSaveLocDetail);
		 btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveLocationDetails();
			}
			 
		 });
		 Button btnFeedback = (Button)this.findViewById(R.id.btnFeedback);
		 btnFeedback.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View vw) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(vw.getContext(),OpenHouseLocationFeedback.class);
				intent.putExtra(OhConstants.LOCATION_ID_NAME, locationId);
    			vw.getContext().startActivity(intent);
			}
			 
		 });
		 Button btnSetQuestion = (Button)this.findViewById(R.id.btnSetQuestions);
		 btnSetQuestion.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View vw) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(vw.getContext(),SetLocationQuestionsActivity.class);
				intent.putExtra(OhConstants.LOCATION_ID_NAME, locationId);
    			vw.getContext().startActivity(intent);
			}
			 
		 });
		 // within activity not sure should do this setLocationDetail(locId);
	  }
	  
	  /**
	   * set the location info
	   * @param locId
	   */
	  private void setLocationDetail(long locId) {
		  Location loc = findLocation(locId);
		  if (loc==null) {
			  Toast.makeText(this, "Location not Found", Toast.LENGTH_LONG);
			  return;
		  }
		  EditText txtAddress = (EditText)this.findViewById(R.id.txtAddress);
		  txtAddress.setText(loc.getAddress());
	  }
	  
	  private void saveLocationDetails() {
		  thisLocation = new Location();
		  setFields(thisLocation);
		  TaskSaveLocationDetail task = new TaskSaveLocationDetail(this);
		  task.setLocation(thisLocation);
		  task.execute(new String[]{OhConstants.ACTION_SAVE});
		  
	  }
	  
	  
	  private void setFields (Location loc) {
		  	loc.setLocationId(locationId);
		  	loc.setAddress(((EditText)this.findViewById(R.id.txtAddress)).getText().toString());
			loc.setCity(((EditText)this.findViewById(R.id.txtCity)).getText().toString());
			loc.setState(((EditText)this.findViewById(R.id.txtState)).getText().toString());
			loc.setPinNo(((EditText)this.findViewById(R.id.txtPin)).getText().toString());
			loc.setZipcode(((EditText)this.findViewById(R.id.txtZipcode)).getText().toString());
			loc.setSellerEmail(((EditText)this.findViewById(R.id.txtSellerEmail)).getText().toString());
	  }
	  
	  /**
	   * 
	   * @param locId
	   * @return
	   */
	  private Location findLocation(long locId) {
		   LocationDao dao = new LocationDao(this);
		   Location location = null;
		   
		   try {
				dao.openDatabase();
				location = dao.getLocation(locId);
				if (location==null ) {
					
					Log.i(OhConstants.OH_TAG, "No location found, there is a problem.");
				}
			
			}  catch(Exception ex) {
				Log.e(OhConstants.OH_TAG, "Error testing dao");
				ex.printStackTrace();
				
			} finally {
				dao.closeDatabase();
			}
		   
		   return location;
	  }
	
}
