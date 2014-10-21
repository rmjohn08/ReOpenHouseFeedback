package net.rmj.android.ohfeedback;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import net.rmj.android.ohfeedback.dataaccess.DAOUtil;
import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.BaseSearchLocationActivity;
import net.rmj.android.ohfeedback.model.Location;

import java.util.ArrayList;
import java.util.List;

public class AndroidOpenHouseFeedbackActivity extends BaseSearchLocationActivity {

    private ProgressDialog dialog;
    private List<Location> results = null;

    // the search query
    String query = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        DAOUtil.getInstance().open(this.getApplicationContext());
        Log.i("OHFeedback", "Database should now be initialized");

    	super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        Intent intent = getIntent();
        //setContentView(R.layout.search_results);

        //this will check is a search action.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

        }

        performLocationSearch();

        //Button bFeedback = (Button)findViewById(R.id.btnOhNew);
        /* bFeedback.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AndroidOpenHouseFeedbackActivity.this ,NewPropertyActivity.class);
	   			startActivity(intent);

			}

        });
        */

    }

    private void performLocationSearch() {
        dialog = new ProgressDialog(this);
        dialog.show();

        if (doSearch(query).equalsIgnoreCase(OhConstants.SUCCESS)) {
            this.populateResultList();

        } else {

            Toast.makeText(this,"No locations found",Toast.LENGTH_SHORT).show();
        }

        dialog.dismiss();

    }

    //this is for the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            case R.id.action_refresh:
                performLocationSearch();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * performs search
     * @return
     */
    protected String doSearch(String... params) {
        DAOUtil dao = DAOUtil.getInstance();
        try {
            Dao<Location, Long> locDao = dao.getDAO(Location.class);
            QueryBuilder<Location, Long> query = locDao.queryBuilder();
            if (params[0]!=null) {
                Where<Location, Long> where = query.where();

                where.or(
                        where.like("address", "%" + params[0] + "%"),
                        where.like("city", "%" + params[0] + "%")

                );
            }

            query.orderBy("address", false);

            PreparedQuery<Location> preparedQuery = query.prepare();
            results = locDao.query(preparedQuery);
            if (results==null || results.size()<=0){
                results = new ArrayList<Location>();
                Location loc = new Location();
                loc.setAddress("No locations found");
                loc.setLocationId(0);
                results.add(loc);
            }

        } catch (Exception ex){
            ex.printStackTrace();
            return OhConstants.FAILED;
        }
        return OhConstants.SUCCESS;
    }


    /**
     * populates the result list
     */
    protected void populateResultList() {

        ListView fListView = this.getListView();

        if (fListView!=null) {
            SearchLocationAdapter fAdapter = new SearchLocationAdapter(this,R.layout.search_result_list, (ArrayList<Location>)results);
            fListView.setAdapter(fAdapter);
        } else {
            Toast.makeText(this, "Problem with List View.", Toast.LENGTH_SHORT);
        }

    }

    
}