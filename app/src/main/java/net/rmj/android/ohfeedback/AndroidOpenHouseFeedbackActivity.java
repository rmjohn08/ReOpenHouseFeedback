package net.rmj.android.ohfeedback;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import net.rmj.android.ohfeedback.dataaccess.DAOUtil;
import net.rmj.android.ohfeedback.model.BaseSearchLocationActivity;
import net.rmj.android.ohfeedback.model.Location;

import java.util.ArrayList;
import java.util.List;

public class AndroidOpenHouseFeedbackActivity extends BaseSearchLocationActivity
    implements LocationListFragment.OnFragmentInteractionListener
    {

    private ProgressDialog dialog;
    private List<Location> results = null;

    private DrawerLayout mDrawyerLayout;
    private String[] drawerOptions;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence title;

    // the search query
    String query = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        DAOUtil.getInstance().open(this.getApplicationContext());
        Log.i("OHFeedback", "Database should now be initialized");

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent intent = getIntent();
        //setContentView(R.layout.search_results);

        //this will check is a search action.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

        }

        performLocationSearch();

        // drawer stuff
        drawerOptions = getResources().getStringArray(R.array.drawer_options);
        Log.i("PlanetSizes", "Planet array:"+drawerOptions.length);

        mDrawyerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerOptions));

        // click events on the drawer list
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // actions when drawer opens or closes
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawyerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view){
                getActionBar().setTitle(title);

            }

            public void onDrawerOpened(View drawerView) {

                getActionBar().setTitle("Open Drawer");
                View cf = getCurrentFocus();
                if (cf!=null) {
                    cf.clearFocus();
                }
            }
        };

        mDrawyerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        //end drawer stuff

    }

        @Override
        public void onResume() {
            super.onResume();
        }

        /**
         * this is used to communicate between activity and fragment
         * @return
         */
        @Override
        public List<Location> onFragmentInteraction() {
            return results;
        }

        /**
         * listener events for the drawer
         */
        private class DrawerItemClickListener implements ListView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id )
            {
                selectItem(position);

            }

        }


    /**
     * not sure why this is needed but somewhere I read it is for drawer proper display
     * @param savedInstance
     */
    @Override
    protected void onPostCreate(Bundle savedInstance) {

        super.onPostCreate(savedInstance);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);


    }

    /* original code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /* int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        */
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

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

    /* here you can swap fragments in the main content view */
    private void selectItem(int position)
    {

        mDrawyerLayout.closeDrawer(mDrawerList);

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


    /**
     * performs search of items
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

        android.app.Fragment fragment = new LocationListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).addToBackStack("fragBack").commit();

        /* ListView fListView = this.getFragmentManager().getFragment().getActivity().getLgetListView();

        if (fListView!=null) {
            SearchLocationAdapter fAdapter = new SearchLocationAdapter(this,R.layout.search_result_list, (ArrayList<Location>)results);
            fListView.setAdapter(fAdapter);
        } else {
            Toast.makeText(this, "Problem with List View.", Toast.LENGTH_SHORT);
        }
        */
    }


    /**
     * avoid closing the app when pressing the back button
     */
     @Override
     public void onBackPressed() {

         if (mDrawyerLayout.isDrawerOpen(Gravity.LEFT)){
             mDrawyerLayout.closeDrawer(Gravity.LEFT);
         } else {
             super.onBackPressed();
         }

     }
}