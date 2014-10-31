package com.fuasocialweb.thechurrocore.db.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import com.fuasocialweb.thechurrocore.db.DataBaseHelper;
import com.fuasocialweb.thechurrocore.db.beans.Question;

public class QuestionController extends DataBaseHelper{
	
    public QuestionController(Context context) {
		super(context);
	}

	//Questions table name
	private static final String TABLE_NAME = "questions";
	
	// Questions Table Columns names
	private static final String QUESTION_ID = "_id";
	private static final String QUESTION = "question";
	private static final String ANSWER1 = "answer1";
	private static final String ANSWER2 = "answer2";
	private static final String ANSWER3 = "answer3";
	private static final String ANSWER4 = "answer4";
	private static final String CORRECT_ANSWER = "correct_answer";
	private static final String MULTIMEDIA = "multimedia";
	private static final String LEVEL = "level";
	private static final String TYPE = "type";
	private static final String ANSWERED = "answered";

	
	private static final String[] COLUMNS = {QUESTION_ID,QUESTION,ANSWER1,ANSWER2,ANSWER3,ANSWER4,CORRECT_ANSWER,MULTIMEDIA,LEVEL,TYPE,ANSWERED};
    
    
	public void addQuestion(Question questions){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(QUESTION, questions.getQuestion()); 
		values.put(ANSWER1, questions.getAnswer1());
		values.put(ANSWER2, questions.getAnswer2());
		values.put(ANSWER3, questions.getAnswer3());
		values.put(ANSWER4, questions.getAnswer4());
		values.put(CORRECT_ANSWER, questions.getCorrectAnswer());
		values.put(MULTIMEDIA, questions.getMultimedia());
		values.put(LEVEL, questions.getLevel());
		values.put(TYPE, questions.getType());
		values.put(ANSWERED, questions.getAnswered());
		
		db.insert(TABLE_NAME, null, values); 
		
		db.close(); 
	}
    
	
	public Question getQuestion(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Question question = new Question();
		
		Cursor cursor = db.query(TABLE_NAME, COLUMNS, " _id = ?", new String[] { String.valueOf(id) }, null, null, null, null);
		 
		if (cursor != null) {
			cursor.moveToFirst();
			question.setId(Integer.parseInt(cursor.getString(0)));
			question.setQuestion(cursor.getString(1));
			question.setAnswer1(cursor.getString(2));
			question.setAnswer2(cursor.getString(3));
			question.setAnswer3(cursor.getString(4));
			question.setAnswer4(cursor.getString(5));
			question.setCorrectAnswer(cursor.getInt(6));
			question.setMultimedia(cursor.getString(7));
			question.setLevel(cursor.getInt(8));
			question.setType(cursor.getInt(9));
			question.setAnswered(cursor.getInt(10));
		}
		db.close(); 
		return question;
	}
	
	
	
	public Question getQuestion(int level, int questionNumber){
		Question question = new Question();
		SQLiteDatabase db = this.getReadableDatabase();
		
		String id = level + "" + questionNumber;
		Log.d("TheChurroCore", "id: " + id);
		Cursor cursor = db.query(TABLE_NAME, COLUMNS, " _id = ?", new String[] { id }, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
			
			question.setId(Integer.parseInt(cursor.getString(0)));
			question.setQuestion(cursor.getString(1));
			question.setAnswer1(cursor.getString(2));
			question.setAnswer2(cursor.getString(3));
			question.setAnswer3(cursor.getString(4));
			question.setAnswer4(cursor.getString(5));
			question.setCorrectAnswer(cursor.getInt(6));
			question.setMultimedia(cursor.getString(7));
			question.setLevel(cursor.getInt(8));
			question.setType(cursor.getInt(9));
			question.setAnswered(cursor.getInt(10));
		}
		db.close(); 
		return question;
	}
	
	
	public List<Question> getAllQuestions() {
		List<Question> questions = new LinkedList<Question>();

		String query = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Question question = null;
		if (cursor.moveToFirst()) {
			do {
				question = new Question();
				question.setId(Integer.parseInt(cursor.getString(0)));
				question.setQuestion(cursor.getString(1));
				question.setAnswer1(cursor.getString(2));
				question.setAnswer2(cursor.getString(3));
				question.setAnswer3(cursor.getString(4));
				question.setAnswer4(cursor.getString(5));
				question.setCorrectAnswer(cursor.getInt(6));
				question.setMultimedia(cursor.getString(7));
				question.setLevel(cursor.getInt(8));
				question.setType(cursor.getInt(9));
				question.setAnswered(cursor.getInt(10));

				questions.add(question);
			} while (cursor.moveToNext());
		}
		db.close(); 
		return questions;
	}
	
	
	
	public ArrayList<Question> getQuestionsFromLevel(int level) {
		ArrayList<Question> questions = new ArrayList<Question>();

		String query = "SELECT  * FROM " + TABLE_NAME + " where level = " + level;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Question question = null;
		if (cursor.moveToFirst()) {
			do {
				question = new Question();
				question.setId(Integer.parseInt(cursor.getString(0)));
				question.setQuestion(cursor.getString(1));
				question.setAnswer1(cursor.getString(2));
				question.setAnswer2(cursor.getString(3));
				question.setAnswer3(cursor.getString(4));
				question.setAnswer4(cursor.getString(5));
				question.setCorrectAnswer(cursor.getInt(6));
				question.setMultimedia(cursor.getString(7));
				question.setLevel(cursor.getInt(8));
				question.setType(cursor.getInt(9));
				question.setAnswered(cursor.getInt(10));

				questions.add(question);
			} while (cursor.moveToNext());
			
			//Randomize
			long seed = System.nanoTime();
			Collections.shuffle(questions, new Random(seed));
		}
		db.close(); 
		return questions;
	}
	
	public ArrayList<Question> getUnansweredQuestionsFromLevel(int level) {
		ArrayList<Question> questions = new ArrayList<Question>();

		String query = "SELECT  * FROM " + TABLE_NAME + " where answered = 0 and level = " + level;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Question question = null;
		if (cursor.moveToFirst()) {
			do {
				question = new Question();
				question.setId(Integer.parseInt(cursor.getString(0)));
				question.setQuestion(cursor.getString(1));
				question.setAnswer1(cursor.getString(2));
				question.setAnswer2(cursor.getString(3));
				question.setAnswer3(cursor.getString(4));
				question.setAnswer4(cursor.getString(5));
				question.setCorrectAnswer(cursor.getInt(6));
				question.setMultimedia(cursor.getString(7));
				question.setLevel(cursor.getInt(8));
				question.setType(cursor.getInt(9));
				question.setAnswered(cursor.getInt(10));

				questions.add(question);
			} while (cursor.moveToNext());
			
			//Randomize
			long seed = System.nanoTime();
			Collections.shuffle(questions, new Random(seed));
		}
		db.close(); 
		return questions;
	}
	
	public int getLastQuestionId(){
		int lastQuestionId = -1;
		String query = "SELECT max(_id) FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
			lastQuestionId = Integer.parseInt(cursor.getString(0));
		}
		db.close(); 
		return lastQuestionId;
	}
	
	public boolean existsQuestionId(int id){
		boolean exists = false;
		String query = "SELECT _id FROM " + TABLE_NAME + " where _id = " + id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (null != cursor && cursor.getCount() > 0) {
			exists = true;
		}
		db.close(); 
		return exists;
	}
	
	
	public void setAsAnswered(int id) {		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
	    values.put(ANSWERED, 1);
		int response = db.update(TABLE_NAME, values, QUESTION_ID + " = " + id, null);
		Log.d("TheChurroCore", "Response: " + response);
		db.close();
	}
	
	public void reset() {		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
	    values.put(ANSWERED, 0);
		db.update(TABLE_NAME, values, null, null);
		db.close();
	}
}
