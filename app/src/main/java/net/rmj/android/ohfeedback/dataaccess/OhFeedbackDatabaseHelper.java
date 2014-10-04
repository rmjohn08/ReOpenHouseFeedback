package net.rmj.android.ohfeedback.dataaccess;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.rmj.android.ohfeedback.OhConstants;
import net.rmj.android.ohfeedback.model.Location;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class OhFeedbackDatabaseHelper extends SQLiteOpenHelper {

    private static String SQL_CREATE_LOCATION = "CREATE TABLE location (zipcode TEXT, _id INTEGER PRIMARY KEY, address TEXT, city TEXT, state TEXT, lat NUMERIC, lon NUMERIC, pin_no TEXT, agent_id TEXT, seller_email TEXT)";

	private static String DB_PATH = "/data/data/net.rmj.android.ohfeedback/databases/";
	private static String DB_NAME = "ohfeedback.db";
	private SQLiteDatabase db;
	private final Context myContext;

	public OhFeedbackDatabaseHelper(Context context) {
		super(context,DB_NAME,null,1);
		this.myContext = context;
	}

    /**
     * runs when database is created
     * @TODO create a script that would handle the db creation and prepopulation...
     * @param db
     */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// when creating database for first time
		Log.i(OhConstants.OH_TAG,"OnCreate called");
        runScriptCreate(db);
	}

    public void runScriptCreate(SQLiteDatabase db)
    {
        boolean isOk = false;
        try {
            Log.i(OhConstants.OH_TAG, "Executing table create script ...");
            db.execSQL(SQL_CREATE_LOCATION);
            isOk=true;
        } catch (SQLiteException se) {
            Log.e(OhConstants.OH_TAG, "Database operation failed...");

            se.printStackTrace();

        } finally {


        }



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
