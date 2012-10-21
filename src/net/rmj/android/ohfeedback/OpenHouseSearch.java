package net.rmj.android.ohfeedback;


import java.util.List;

import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.Location;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class OpenHouseSearch extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		//setContentView(R.layout.search_results);
		
		//this will check is a search action. 
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			//setContentView(R.layout.search_results);
			String query = intent.getStringExtra(SearchManager.QUERY);
			
			//test results
			//String[] results = {"Loc 1 ", "Loc 2", "Loc 3"};
			
			TaskSearchLocations task = new TaskSearchLocations(this);
			task.execute(new String[]{query});
			
			//Toast.makeText(OpenHouseSearch.this, query, Toast.LENGTH_LONG);
			//sets the adpater...
			// for this to work needs the layout must have a textview and a list(id=list)
			/* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.search_results, R.id.txtResult,results);
	    	setListAdapter(adapter);
	    	*/
			
			//List<Location> locations = findLocations(query);
			

		}
		
	}
	
	private List<Location> findLocations(String query) {
		
		List<Location> results = null;
		LocationDao dao = new LocationDao(this);
		dao.openDatabase();
		results = dao.findLocations(query);
		
		return results;
		
	}
	
	
}
