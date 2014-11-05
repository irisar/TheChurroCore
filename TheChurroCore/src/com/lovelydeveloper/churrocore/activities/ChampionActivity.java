package com.lovelydeveloper.churrocore.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lovelydeveloper.churrocore.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.lovelydeveloper.churrocore.db.beans.Score;
import com.lovelydeveloper.churrocore.db.beans.Status;
import com.lovelydeveloper.churrocore.db.controllers.ScoresController;
import com.lovelydeveloper.churrocore.db.controllers.StatusController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Actividad de game over
 * @author fmagana
 *
 */
public class ChampionActivity extends Activity {

	private TextView mScore;
	private Button mGoToMenu;
	private Button mGoToScores;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
		super.onCreate(savedInstanceState);
		createResources(); //Creamos los recursos necesarios
		getScore();
		createEvents(); //Crea los eventos
		} catch (Exception e) {
			Log.d("TheChurroCore", "Error: " + e.toString());
		}
	}

    
    private void createResources() {
    	setContentView(R.layout.activity_champion);
    	mScore = (TextView) findViewById(R.id.score);
    	mGoToMenu = (Button) findViewById(R.id.go_to_menu);
    	mGoToScores = (Button) findViewById(R.id.go_to_scores);
    }
    
    private void createEvents() {
	    mGoToMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Vamos al menu 
				Intent intent = new Intent(ChampionActivity.this, MainActivity.class);
		        intent.putExtra("back", true);
		        startActivity(intent);
				finish();
			}
		});
	    
	    mGoToScores.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Vamos a la pagina de puntuaciones 
				Intent intent = new Intent(ChampionActivity.this, ScoresActivity.class);
		        startActivity(intent);
				finish();
			}
		});
    }
    
    private void getScore() {
    	//Obtenemos y mostramos la puntiación final
    	StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
		int score = status.getScore();
		mScore.setText(getString(R.string.score) + "\n" + score + " " + getString(R.string.points));
		
		//Guardamos la puntuacion en la tabla de puntuaciones
		Score scoreBean = new Score();
		scoreBean.setScore(score);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		scoreBean.setTime(dateFormat.format(date));
		ScoresController scoresController = new ScoresController(getApplicationContext());
		scoresController.addScore(scoreBean);
		
		//Reseteamos la puntuación
		status.setScore(0); //Dejamos la puntuacion a 0
		statusController.update(status);
    }

    @Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); 
	}


	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ChampionActivity.this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
		finish();
	}
}
