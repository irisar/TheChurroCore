package com.lovelydeveloper.churrocore.db.beans;

public class Score {
	private int id;
    private String time;
    private int score;
    
 
    public Score(){}
 
    //getters & setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
