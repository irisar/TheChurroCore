package com.lovelydeveloper.churrocore.db.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import java.util.LinkedList;

import com.lovelydeveloper.churrocore.db.DataBaseHelper;
import com.lovelydeveloper.churrocore.db.beans.Status;

public class StatusController extends DataBaseHelper{
	
    public StatusController(Context context) {
		super(context);
	}

	//Status table name
	private static final String TABLE_STATUS = "status";
	
	// Status Table Columns names
	private static final String STATUS_ID = "_id";
	private static final String STATUS_NAME = "name";
	private static final String STATUS_LEVEL = "level";
	private static final String STATUS_SCORE = "score";
	private static final String STATUS_DIFFICULTY = "difficulty";
	private static final String STATUS_LIFE = "life";
	
	private static final String[] COLUMNS = {STATUS_ID,STATUS_NAME,STATUS_LEVEL,STATUS_SCORE,STATUS_DIFFICULTY,STATUS_LIFE};
    
    
	public void addStatus(Status status){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(STATUS_NAME, status.getName()); 
		values.put(STATUS_LEVEL, status.getLevel());
		values.put(STATUS_SCORE, status.getScore());
		values.put(STATUS_DIFFICULTY, status.getDifficulty());
		values.put(STATUS_LIFE, status.getLife());
		
		db.insert(TABLE_STATUS, null, values); 
		
		db.close(); 
	}
    
	
	public Status getStatus(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Status status = new Status();
		
		Cursor cursor = db.query(TABLE_STATUS, COLUMNS, " _id = ?", new String[] { String.valueOf(id) }, null, null, null, null);
		 
		if (cursor != null) {
			cursor.moveToFirst();	
			status.setId(Integer.parseInt(cursor.getString(0)));
			status.setName(cursor.getString(1));
			status.setLevel(cursor.getInt(2));
			status.setScore(cursor.getInt(3));
			status.setDifficulty(cursor.getInt(4));
			status.setLife(cursor.getInt(5));
		}
		db.close();
		return status;
	}
	
	
	public List<Status> getAllStatus() {
		List<Status> statuses = new LinkedList<Status>();

		String query = "SELECT * FROM " + TABLE_STATUS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Status status = null;
		if (cursor.moveToFirst()) {
			do {
				status = new Status();
				status.setId(Integer.parseInt(cursor.getString(0)));
				status.setName(cursor.getString(1));
				status.setLevel(cursor.getInt(2));
				status.setScore(cursor.getInt(3));
				status.setDifficulty(cursor.getInt(4));
				status.setLife(cursor.getInt(5));
				
				statuses.add(status);
			} while (cursor.moveToNext());
		}
		db.close();
		return statuses;
	}
	
	public void update(Status status) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(STATUS_NAME, status.getName()); 
		values.put(STATUS_LEVEL, status.getLevel());
		values.put(STATUS_SCORE, status.getScore());
		values.put(STATUS_DIFFICULTY, status.getDifficulty());
		values.put(STATUS_LIFE, status.getLife());
		
		db.update(TABLE_STATUS, values, null, null); 
		db.close();
	}
	
	public void reduceLife(int life) {		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
	    values.put(STATUS_LIFE, life);
		db.update(TABLE_STATUS, values, null, null);
		db.close();
	}
}
