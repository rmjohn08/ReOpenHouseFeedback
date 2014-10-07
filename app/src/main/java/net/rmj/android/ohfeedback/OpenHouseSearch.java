package net.rmj.android.ohfeedback;


import java.util.List;

import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.BaseSearchLocationActivity;
import net.rmj.android.ohfeedback.model.Location;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class OpenHouseSearch extends BaseSearchLocationActivity {

    private long locationId=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		//setContentView(R.layout.search_results);
		
		//this will check is a search action. 
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			TaskSearchLocations task = new TaskSearchLocations(this);
			task.execute(new String[]{query});


		}

		
	}


}
