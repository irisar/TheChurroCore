package com.lovelydeveloper.churrocore.db.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
	private static final String STATUS_LEVEL = "level";
	private static final String STATUS_SCORE = "score";
	private static final String STATUS_LIFE = "lifes";
	private static final String STATUS_FINISH = "finish";
	
	private static final String[] COLUMNS = {STATUS_ID, STATUS_LEVEL, STATUS_SCORE, STATUS_LIFE, STATUS_FINISH};
    
    
	public void addStatus(Status status){
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(STATUS_LEVEL, status.getLevel());
			values.put(STATUS_SCORE, status.getScore());
			values.put(STATUS_LIFE, status.getLife());
			values.put(STATUS_FINISH, status.getFinish());		
	
			db.insert(TABLE_STATUS, null, values); 
			
			Log.d("TheChurroCore", "Creado el status: " + status.getLevel());
		} catch (Exception e) {
			Log.d("TheChurroCore", "Error al crear status: " + e);
		}
		db.close(); 
	}
    
	
	public Status getStatus(int level){
		SQLiteDatabase db = this.getReadableDatabase();
		Status status = null;
		
		Cursor cursor = db.query(TABLE_STATUS, COLUMNS, " level = ?", new String[] { String.valueOf(level) }, null, null, null, null);
		try {
			if (cursor != null) {
				cursor.moveToFirst();
				status = new Status();
				status.setId(Integer.parseInt(cursor.getString(0)));
				status.setLevel(cursor.getInt(1));
				status.setScore(cursor.getInt(2));
				status.setLife(cursor.getInt(3));
				status.setFinish(cursor.getInt(4));
				Log.d("TheChurroCore", "Obtenido el status " + status.getLevel());
			}
		} catch (Exception e) {
			status = null;
			Log.d("TheChurroCore", "No hay status en el get " + level + " Error: " + e);
		}
		db.close();
		
		return status;
	}
	
	public Status getMaxStatus(){
		Status status = null;
		
		String query = "SELECT " + STATUS_ID + ", " + STATUS_LEVEL + ", " + STATUS_SCORE + ", " + STATUS_LIFE + ", " + STATUS_FINISH
						+ " FROM " + TABLE_STATUS 
						+ " WHERE " + STATUS_LEVEL + " IN (SELECT MAX(" + STATUS_LEVEL + ") FROM " + TABLE_STATUS + ")";
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(query, null);
			
			if (cursor != null) {
				cursor.moveToFirst();
				status = new Status();
				status.setId(Integer.parseInt(cursor.getString(0)));
				status.setLevel(cursor.getInt(1));
				status.setScore(cursor.getInt(2));
				status.setLife(cursor.getInt(3));
				status.setFinish(cursor.getInt(4));
				
				Log.d("TheChurroCore", "Obtenido el maxstatus " + status.getLevel());
			}
		} catch (Exception e) {
			status = null;
			Log.d("TheChurroCore", "No hay status en el maxstatus");
		}
		db.close();
		return status;
	}
	
	
	public int getScore(){
		int score = 0;
		
		String query = "SELECT SUM(" + STATUS_SCORE + ")" + " FROM " + TABLE_STATUS;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(query, null);
			
			if (cursor != null) {
				cursor.moveToFirst();
				score = Integer.parseInt(cursor.getString(0));
				Log.d("TheChurroCore", "Obtenido el score " + score);
			}
		} catch (Exception e) {
			Log.d("TheChurroCore", "No hay status en el maxstatus");
		}
		db.close();
		return score;
	}
	
	
	public List<Status> getAllStatus() {
		List<Status> statuses = new LinkedList<Status>();

		String query = "SELECT " + STATUS_ID + ", " + STATUS_LEVEL + ", " + STATUS_SCORE + ", " + STATUS_LIFE  + ", " + STATUS_FINISH
						+ " FROM " + TABLE_STATUS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Status status = null;
		if (cursor.moveToFirst()) {
			do {
				status = new Status();
				status.setId(Integer.parseInt(cursor.getString(0)));
				status.setLevel(cursor.getInt(1));
				status.setScore(cursor.getInt(2));
				status.setLife(cursor.getInt(3));
				status.setFinish(cursor.getInt(4));
				
				statuses.add(status);
			} while (cursor.moveToNext());
		}
		db.close();
		return statuses;
	}
	
	public void update(Status status) {
		
		Status existsStatus  = getStatus(status.getLevel());
		if (null != existsStatus) {
			Log.d("TheChurroCore", "Hacemos update del status " + status.getLevel());
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(STATUS_LEVEL, status.getLevel());
			values.put(STATUS_SCORE, status.getScore());
			values.put(STATUS_LIFE, status.getLife());
			if (status.getFinish() == 1) {
				values.put(STATUS_FINISH, status.getFinish());
			}
			db.update(TABLE_STATUS, values, "level = " + status.getLevel() , null); 
			db.close();
		} else {
			Log.d("TheChurroCore", "Creamos el status " + status.getLevel());
			addStatus(status);
		}
	}
	
	public void reduceLife(int level, int life) {		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
	    values.put(STATUS_LIFE, life);
		db.update(TABLE_STATUS, values, "level = " + level, null);
		db.close();
	}
}
