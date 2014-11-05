package com.lovelydeveloper.churrocore.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lovelydeveloper.churrocore.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lovelydeveloper.churrocore.db.beans.Score;
import com.lovelydeveloper.churrocore.db.beans.Status;
import com.lovelydeveloper.churrocore.db.controllers.ScoresController;
import com.lovelydeveloper.churrocore.db.controllers.StatusController;
import com.lovelydeveloper.churrocore.utils.AppUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Actividad de game over
 * @author fmagana
 *
 */
public class GameOverActivity extends Activity {

	private TextView mScore;
	private Button mGoToMenu;
	private Button mGoToScores;
	private AdView adView;
	
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
    	
    	//Crear anuncio
    	
    	adView = new AdView(this);
	    adView.setAdUnitId(""+getText(R.string.add_id));
	    adView.setAdSize(AdSize.SMART_BANNER);
	    LinearLayout layout = (LinearLayout)findViewById(R.id.anuncio);
	    layout.addView(adView);
	    AdRequest adRequest = AppUtils.getAddRequest();
	    adView.loadAd(adRequest);
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
				Intent intent = new Intent(GameOverActivity.this, ScoresActivity.class);
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
	
	public void onPause() {
		adView.pause();
		super.onPause();
	}
	 
	public void onResume() {
		super.onResume();
		adView.resume();
	}

	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
		finish();
	}
}
