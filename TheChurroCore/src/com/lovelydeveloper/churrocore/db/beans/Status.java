package com.lovelydeveloper.churrocore.db.beans;

import android.content.Context;

import com.lovelydeveloper.churrocore.R;

public class Status {
	private int id;
    private int level;
    private int score;
    private int life;
    private int finish;
    

    public Status () {
    	super();
        this.level = 1;
        this.score = 0;
        this.life = 3;
        this.finish = 0;
    }
 
    //getters & setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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


    public int getFinish() {
		return finish;
	}

	public void setFinish(int finish) {
		this.finish = finish;
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
        return "Status [id=" + id + ", level=" + level + ", score=" + score  + ", lifes=" + life + "]";
    }

}
