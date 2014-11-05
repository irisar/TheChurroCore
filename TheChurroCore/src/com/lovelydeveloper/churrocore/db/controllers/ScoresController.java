package com.lovelydeveloper.churrocore.db.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import com.lovelydeveloper.churrocore.db.DataBaseHelper;
import com.lovelydeveloper.churrocore.db.beans.Score;

public class ScoresController extends DataBaseHelper{
	
    public ScoresController(Context context) {
		super(context);
	}

	//Score table name
	private static final String TABLE_SCORE = "scores";
	
	// Score Table Columns names
	private static final String SCORE_ID = "_id";
	private static final String SCORE_TIME = "time";
	private static final String SCORE_SCORE = "score";
	
	private static final String[] COLUMNS = {SCORE_ID,SCORE_TIME,SCORE_SCORE};
    
    
	public void addScore(Score score){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(SCORE_TIME, score.getTime()); 
		values.put(SCORE_SCORE, score.getScore());
		
		db.insert(TABLE_SCORE, null, values); 
		
		db.close(); 
	}
    
	
	public Score getScore(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Score score = new Score();
		
		Cursor cursor = db.query(TABLE_SCORE, COLUMNS, " _id = ?", new String[] { String.valueOf(id) }, null, null, null, null);
		 
		if (cursor != null) {
			cursor.moveToFirst();	
			score.setId(Integer.parseInt(cursor.getString(0)));
			score.setTime(cursor.getString(1));
			score.setScore(cursor.getInt(2));
		}
		db.close();
		return score;
	}
	
	
	public List<Score> getAllScores() {
		List<Score> scorees = new LinkedList<Score>();

		String query = "SELECT * FROM " + TABLE_SCORE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Score score = null;
		if (cursor.moveToFirst()) {
			do {
				score = new Score();
				score.setId(Integer.parseInt(cursor.getString(0)));
				score.setTime(cursor.getString(1));
				score.setScore(cursor.getInt(3));
				
				scorees.add(score);
			} while (cursor.moveToNext());
		}
		db.close();
		return scorees;
	}

	public ArrayList<Score> getMaxScores() {
		ArrayList<Score> scores = new ArrayList<Score>();

		String query = "SELECT * FROM " + TABLE_SCORE + " ORDER BY " + SCORE_SCORE + " DESC LIMIT 10";
		

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Score score = null;
		if (cursor.moveToFirst()) {
			do {
				score = new Score();
				score.setId(Integer.parseInt(cursor.getString(0)));
				score.setTime(cursor.getString(1));
				score.setScore(cursor.getInt(2));
				
				scores.add(score);
			} while (cursor.moveToNext());
		}
		db.close();
		return scores;
	}
	
}
