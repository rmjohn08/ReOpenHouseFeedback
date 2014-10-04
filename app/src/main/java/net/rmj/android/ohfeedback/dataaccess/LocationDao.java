package net.rmj.android.ohfeedback.dataaccess;

import java.util.ArrayList;
import java.util.List;

import net.rmj.android.ohfeedback.OhConstants;
import net.rmj.android.ohfeedback.model.Location;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class LocationDao extends OpenHouseFeedbackDao {

	public final String LOCATION_TABLE = "location";
	private List<String> allColumns;
	
	public LocationDao(Context context) {
		super(context);
		// TODO Auto-generated[ constructor stub
		setAllColumns();
	}
	
	private void setAllColumns() {
		allColumns =  new ArrayList<String>();
		allColumns.add("_id");
		allColumns.add("address");
		allColumns.add("city");
		allColumns.add("state");
		allColumns.add("lat");
		allColumns.add("lon");
		allColumns.add("pin_no");
		allColumns.add("agent_id");
		allColumns.add("seller_email");
		allColumns.add("zipcode");
		
	}
	
	/**
	 * list all locations
	 * @return
	 */
	public List<Location> getAllLocations() {
		List<Location> list = new ArrayList<Location>();
		String[] cols = allColumns.toArray(new String[0]);
		Cursor cursor = db.query(LOCATION_TABLE, cols, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			list.add(buildLocation(cursor));
			cursor.moveToNext();
		}
		
		return list;
	}
	
	/**
	 * finds a location by the query provided. it will attempt to make a match
	 * by address and city
	 * @param query
	 * @return
	 */
	public ArrayList<Location> findLocations(String query) {
		ArrayList<Location> list = new ArrayList<Location>();
		String[] cols = allColumns.toArray(new String[0]);
		String selection = " address like ? or city like ? ";
		String[] selectionArgs = {"%"+query+"%", "%"+query+"%"};
		
		Cursor cur = db.query(LOCATION_TABLE, cols, selection, selectionArgs, null, null,"address" );
		cur.moveToFirst();
		while(!cur.isAfterLast()) {
			list.add(buildLocation(cur));
			cur.moveToNext();
		}
		
		return list;
	}
	
	/**
	 * add a new location record
	 * @param qn
	 */
	public void addLocation(Location lo) {
		ContentValues values = new ContentValues();
		setLocContentValues(values, lo,false);
		
		long newId = db.insert(this.LOCATION_TABLE, null, values);
		lo.setLocationId(newId);
		
		
	}
	
	public void setLocContentValues(ContentValues values, Location lo, boolean setPk) {
		int idx=0;
		if (setPk) 
			values.put(allColumns.get(idx++), lo.getLocationId());
		else
			idx++;
		
		values.put(allColumns.get(idx++), lo.getAddress());
		values.put(allColumns.get(idx++), lo.getCity());
		values.put(allColumns.get(idx++), lo.getState());
		values.put(allColumns.get(idx++), lo.getLat());
		values.put(allColumns.get(idx++), lo.getLon());
		values.put(allColumns.get(idx++), lo.getPinNo());
		values.put(allColumns.get(idx++), lo.getAgentId());
		values.put(allColumns.get(idx++), lo.getSellerEmail());
		values.put(allColumns.get(idx++), lo.getZipcode());
		
	}
	
	public Location getLocation(long pk) {
		String selection = allColumns.get(0) + " = ?";
		String[] selectArgs = {String.valueOf(pk)};
		Location loc = null;
		String[] cols = (String[])allColumns.toArray(new String[0]);
		Cursor cursor = db.query(LOCATION_TABLE, cols, selection, selectArgs, null, null, null);
		cursor.moveToFirst();
		
		if (!cursor.isAfterLast()) {
			loc = buildLocation(cursor);
			cursor.moveToNext();
		}
		return loc;
		
		
	}
	
	
	/**
	 * update a question record
	 * @param qn
	 * @return
	 */
	public long updateLocation(Location loc) {
		ContentValues values = new ContentValues();
		setLocContentValues(values, loc, false);
		
		String whereClause = allColumns.get(0) + " = ?";
		String[] whereArgs = {String.valueOf(loc.getLocationId())};
		
		return db.update(LOCATION_TABLE, values, whereClause, whereArgs);
		
	}
	
	/**
	 * delete a question based on the id
	 * @param pk
	 */
	public long deleteLocation(long pk) {
		String args[] = {String.valueOf(pk)};
		return db.delete(LOCATION_TABLE, allColumns.get(0) + " = ? ", args);
		
	}
	
	/**
	 * builds a question object
	 * @param cursor
	 * @return
	 */
	protected Location buildLocation(Cursor cursor) {
		
		Location q = new Location();
		int idx = 0;
		//if (cursor.getColumnIndex(allColumns.get(idx))>0)
        q.setId(cursor.getLong(cursor.getColumnIndex(allColumns.get(idx))));

        q.setLocationId(cursor.getLong(cursor.getColumnIndex(allColumns.get(idx++))));
		
		q.setAddress(cursor.getString(cursor.getColumnIndex(allColumns.get(idx++))));
		q.setCity(cursor.getString(cursor.getColumnIndex(allColumns.get(idx++))));
		q.setState(cursor.getString(cursor.getColumnIndex(allColumns.get(idx++))));
		q.setLat(cursor.getDouble(cursor.getColumnIndex(allColumns.get(idx++))));
		q.setLon(cursor.getDouble(cursor.getColumnIndex(allColumns.get(idx++))));
		q.setPinNo(cursor.getString(cursor.getColumnIndex(allColumns.get(idx++))));
		q.setAgentId(cursor.getString(cursor.getColumnIndex(allColumns.get(idx++))));
		q.setSellerEmail(cursor.getString(cursor.getColumnIndex(allColumns.get(idx++))));
		q.setZipcode(cursor.getString(cursor.getColumnIndex(allColumns.get(idx++))));
		
		return q;
		
	}
	
	
	
	public void testLocationDao() {
		Location lo = new Location();
		lo.setAddress("1000 Quiet Maddow");
		lo.setCity("Great City");
		lo.setSellerEmail("r_john08@cox.net");
		lo.setZipcode("12020");
		
		this.addLocation(lo);
		Location l2 = this.getLocation(lo.getLocationId());
		if (l2==null) {
			Log.i(OhConstants.OH_TAG, "No Location found .... quitting");
			return;
		} else {
			Log.i(OhConstants.OH_TAG, "Location found....");
		}
		List<Location> list = this.getAllLocations();
		if (list==null) {
			Log.i(OhConstants.OH_TAG, "No Locations found.... quitting");
			return;
		} else {
			Log.i(OhConstants.OH_TAG, "Locations found....");
		}
		
		l2.setAddress("1600 Park Avenue");
		//long rows = this.updateLocation(l2);
			
		
		long deleted = this.deleteLocation(l2.getLocationId());
		
		if (deleted>0) {
			Log.i(OhConstants.OH_TAG, "No Location Deleted.... quitting");
			return;
		} else {
			Log.i(OhConstants.OH_TAG, "Location deleted ...." + l2.getLocationId());
		
		}

	}
	
		

}
