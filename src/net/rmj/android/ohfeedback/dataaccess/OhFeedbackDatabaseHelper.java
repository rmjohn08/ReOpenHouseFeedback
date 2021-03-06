package net.rmj.android.ohfeedback.dataaccess;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.rmj.android.ohfeedback.OhConstants;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class OhFeedbackDatabaseHelper extends SQLiteOpenHelper {
	
	private static String DB_PATH = "/data/data/net.rmj.android.ohfeedback/databases/";
	private static String DB_NAME = "ohfeedback";
	private SQLiteDatabase db;
	private final Context myContext;

	public OhFeedbackDatabaseHelper(Context context) {
		super(context,DB_NAME,null,1);
		this.myContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// when creating database for first time
		Log.i(OhConstants.OH_TAG,"OnCreate called");
		
		////boolean alreadyExist = checkDatabase();
		
		//if (!alreadyExist) {
		//	//this.getReadableDatabase();
		//	try {
		//		copyDataBaseFile();
		//	} catch(IOException io) {
		//		//throw new Exception(io);
		//		
		//	}
		//}
		
	}
	
	public boolean checkDatabase() {
		SQLiteDatabase db1 = null;
		boolean isOk = false;
		try {
			String fullname = DB_PATH + DB_NAME;
			db1 = SQLiteDatabase.openDatabase(fullname,null, SQLiteDatabase.OPEN_READONLY);
			Log.i(OhConstants.OH_TAG, "Successfull DB " + fullname + " check...");
			isOk=true;
		} catch (SQLiteException se) {
			Log.e(OhConstants.OH_TAG, "Database does not exist...");
			
			se.printStackTrace();
			
		} finally {
			
			db1.close();
		}
		
		return isOk;
	}
	
	private void copyDataBaseFile() throws IOException{
		 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// when upgrading
		
	}
	
	@Override
	public synchronized void close() {
		if (db!=null)
			db.close();
		
		super.close();
	}
	
	public void openDatabase() throws SQLException {
		String fullname = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(fullname,null, SQLiteDatabase.OPEN_READWRITE);		
	}
	
	

}
