package com.lovelydeveloper.churrocore.db.beans;

import android.content.Context;

import com.lovelydeveloper.churrocore.R;

public class Status {
	private int id;
    private String name;
    private int level;
    private int score;
    private int difficulty;
    private int life;
    
 
    public Status(){}
 
    public Status (String name) {
    	super();
        this.name = name;
        this.level = 1;
        this.score = 0;
        this.difficulty = 2;
        this.life = 3;
    }
 
    //getters & setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
    
    public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public String getLifes(Context context) {
		return getLifes(context, life);
	}
	
	public static String getLifes(Context context, int life) {
		String lifes = context.getString(R.string.lifes3);
		if (life == 1) {
			lifes = context.getString(R.string.lifes1);
		} else if (life == 2) {
			lifes = context.getString(R.string.lifes2);
		}
		return lifes;
	}

	@Override
    public String toString() {
        return "Status [id=" + id + ", name=" + name + ", level=" + level + ", score=" + score  + ", difficulty=" + difficulty+ ", life=" + life + "]";
    }

}
