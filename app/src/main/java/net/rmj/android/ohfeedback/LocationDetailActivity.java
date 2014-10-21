package net.rmj.android.ohfeedback;

import net.rmj.android.ohfeedback.dataaccess.DAOUtil;
import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.BaseLocationDetailActivity;
import net.rmj.android.ohfeedback.model.Location;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class LocationDetailActivity extends BaseLocationDetailActivity {
	  protected long locationId =0;
	  protected Location thisLocation=new Location();
	  
	  protected Button btnFeedback;
	  protected Button btnSetQuestion;
	  
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
				saveLocationDetails((Activity)v.getContext());
			}
			 
		 });
		 
		 this.setButtonsOnClickListener();

          getActionBar().setDisplayHomeAsUpEnabled(true);

	  }
	  
	  protected void setButtonsOnClickListener() {
		  	if (btnFeedback==null) btnFeedback = (Button)this.findViewById(R.id.btnFeedback);
			
		  	btnFeedback.setOnClickListener( new OnClickListener() {

				@Override
				public void onClick(View vw) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(vw.getContext(),OpenHouseLocationFeedback.class);
					intent.putExtra(OhConstants.LOCATION_ID_NAME, locationId);
	    			vw.getContext().startActivity(intent);
				}
				 
			 });
		
			 if (btnSetQuestion==null) btnSetQuestion = (Button)this.findViewById(R.id.btnSetQuestions);
			 
			 btnSetQuestion.setOnClickListener( new OnClickListener() {

				@Override
				public void onClick(View vw) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(vw.getContext(),SetLocationQuestionsActivity.class);
					intent.putExtra(OhConstants.LOCATION_ID_NAME, locationId);
	    			vw.getContext().startActivity(intent);
				}
				 
			 });
		  
	  }
	  
	  
	  protected void saveLocationDetails(Activity theActivity) {
		  thisLocation = new Location();
		  setFields(thisLocation);
          try {
              Dao<Location, Long> dao = DAOUtil.getInstance().getDAO(Location.class);
              //dao.update(thisLocation);
              dao.createOrUpdate(thisLocation);

          } catch(Exception ex){
              ex.printStackTrace();
              Log.e("OHFeedback", ex.getMessage());
              Toast.makeText(this.getApplicationContext(), "Problem saving Location", Toast.LENGTH_SHORT);

          }
          /*
		  TaskSaveLocationDetail task = new TaskSaveLocationDetail(theActivity);
		  task.setLocation(thisLocation);
		  task.execute(new String[]{OhConstants.ACTION_SAVE});
		  */
	  }

	  protected void setFields (Location loc) {
		  	loc.setLocationId(locationId);
            loc.setId(locationId);
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
