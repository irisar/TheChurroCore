package com.lovelydeveloper.churrocore.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lovelydeveloper.churrocore.db.beans.Status;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Clase encargada de gestionar la base de datos
 * @author fmagana
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper{
	 
	private static String DB_NAME = "database.db";
	private SQLiteDatabase myDataBase; 
	private final Context myContext;
	 
	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}	
 
	/**
	 * Creates a empty database on the system and rewrites it with your own database.
     * */
	public void createDataBase() {
		boolean dbExist = checkDataBase();
		Log.d("TheChurroCore", "¿DB EXISTS?: " + dbExist);
		if(dbExist){
			//do nothing - database already exist
		}else{
			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.d("TheChurroCore", "Error copiando base de datos: " + e.toString());
			}
		}
	}
 
	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(){
		boolean checkDB = false;
		try{
			String myPath = myContext.getDatabasePath("ignored").getParentFile() + "/" + DB_NAME;
			Log.d("TheChurroCore", "DATABASE PATH: " + myPath);

			File dbfile = new File(myPath);                
			checkDB = dbfile.exists();
		}catch(Exception e){
			//database does't exist yet.
		}
		
		return checkDB;
	}
 
	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException{
		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
 
		// Path to the just created empty db
		String outFileName = myContext.getDatabasePath("ignored").getParentFile() +  "/" + DB_NAME;
 
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
 
	public void openDataBase() throws SQLException{
		//Open the database
		String myPath = myContext.getDatabasePath("ignored").getParentFile() +  "/" + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}
 
    @Override
	public synchronized void close() {
    	if(myDataBase != null) {
    		myDataBase.close();
    	}
    	super.close();
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}