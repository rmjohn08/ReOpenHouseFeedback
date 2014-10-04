package net.rmj.android.ohfeedback;

import java.util.ArrayList;
import java.util.List;

import net.rmj.android.ohfeedback.dataaccess.DAOUtil;
import net.rmj.android.ohfeedback.dataaccess.LocationDao;
import net.rmj.android.ohfeedback.model.Location;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

/**
 * AsyncTask that will take care of reading questions for location
 * @author YRJ0002
 *
 */
public class TaskSearchLocations extends OhAsyncTaskBase {
	
	List<Location> results = null;
	
	public TaskSearchLocations(Activity activity) {
		//this.activity = activity;
		//this.context = this.activity;
		//dialog = new ProgressDialog(context);
		super(activity);
	}

    @Override
    protected String doInBackground(String... params) {
        DAOUtil dao = DAOUtil.getInstance();
        try {
            Dao<Location, Long> locDao = dao.getDAO(Location.class);
            QueryBuilder<Location, Long> query = locDao.queryBuilder();
            Where<Location, Long> where = query.where();

                    where.or(
                            where.like("address", "%" + params[0] + "%"),
                            where.like("city", "%" + params[0] + "%")

                    );
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

    protected String doInBackgroundOriginal(String... params) {
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

        if (dialog.isShowing()) dialog.dismiss();

        if (result == null) {
            Toast.makeText(context, "No locations found.", Toast.LENGTH_SHORT);

        }
        ListActivity ac =(ListActivity)this.activity;
        ListView fListView = ac.getListView();
        //ListView fListView = (ListView)this.activity.findViewById(R.id.srchResultList);

        if (fListView!=null) {
            SearchLocationAdapter fAdapter = new SearchLocationAdapter(context,R.layout.search_result_list, (ArrayList<Location>)results);
            fListView.setAdapter(fAdapter);
        } else {
            Toast.makeText(context, "Problem with List View.", Toast.LENGTH_SHORT);
        }

	}

}
