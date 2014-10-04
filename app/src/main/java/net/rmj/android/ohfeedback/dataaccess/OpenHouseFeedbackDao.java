package net.rmj.android.ohfeedback.dataaccess;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/*
 * Data Access Object class for accessing database (CRUD operations)
 * provides the access to the database. subclasses will provide specific
 * operations for each tables. 
 */
public class OpenHouseFeedbackDao {
	
	protected SQLiteDatabase db;
	protected OhFeedbackDatabaseHelper dbHelper;
	
	public OpenHouseFeedbackDao(Context context ) {
		dbHelper = new OhFeedbackDatabaseHelper(context);
	}
	
	public void openDatabase() {
		db = dbHelper.getWritableDatabase();
	}
	
	public void closeDatabase() {
		dbHelper.close();
		db=null;
	}
	
	public boolean testConnection() throws SQLException {	
		return dbHelper.checkDatabase();
	}
	
	public void beginTransaction() {
		db.beginTransaction();
	}
	public void setTransactionComplete() {
		db.setTransactionSuccessful();
	}
	public void endTransaction() {
		db.endTransaction();
	}
}
