package com.fuasocialweb.thechurrocore.activities;

import com.fuasocialweb.thechurrocore.R;
import com.fuasocialweb.thechurrocore.db.beans.Status;
import com.fuasocialweb.thechurrocore.db.controllers.StatusController;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Actividad de nivel
 * @author fmagana
 *
 */
public class GameOverActivity extends Activity {

	private TextView mScore;
	private Button mGoToMenu;
	private Button mGoToScores;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createResources(); //Creamos los recursos necesarios
		getScore();
		createEvents(); //Crea los eventos
	}

    
    private void createResources() {
    	setContentView(R.layout.activity_game_over);
    	mScore = (TextView) findViewById(R.id.score);
    	mGoToMenu = (Button) findViewById(R.id.go_to_menu);
    	mGoToScores = (Button) findViewById(R.id.go_to_scores);
    }
    
    private void createEvents() {
	    mGoToMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Vamos al menu 
				Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
		        intent.putExtra("back", true);
		        startActivity(intent);
				finish();
			}
		});
	    
	    mGoToScores.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Vamos a la pagina de puntuaciones 
				Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
		        startActivity(intent);
				finish();
			}
		});
    }
    
    private void getScore() {
    	StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
		int score = status.getScore();
		mScore.setText(getString(R.string.score) + "\n" + score + " " + getString(R.string.points));
		
		//TODO: guardamos la puntuacion en la tabla de puntuaciones
		
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
		Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
		finish();
	}
}
