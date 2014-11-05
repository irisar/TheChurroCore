package com.lovelydeveloper.churrocore.activities;


import java.util.ArrayList;

import com.lovelydeveloper.churrocore.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.lovelydeveloper.churrocore.db.beans.Score;
import com.lovelydeveloper.churrocore.db.controllers.ScoresController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

/**
 * Actividad de puntuaciones
 * @author fmagana
 *
 */
public class ScoresActivity extends Activity {

	private TableLayout mTable;
	private Button mGoToMenu;
	private TableRow mRow;
	private TextView mPosition;
	private TextView mTime;
	private TextView mScore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createResources(); //Creamos los recursos necesarios
		getScores();
		createEvents(); //Crea los eventos
	}

    
    private void createResources() {
    	setContentView(R.layout.activity_scores);
    	mTable = (TableLayout) findViewById(R.id.table);
    	mGoToMenu = (Button) findViewById(R.id.go_to_menu);
    }
    
    private void createEvents() {
	    mGoToMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Vamos al menu 
				Intent intent = new Intent(ScoresActivity.this, MainActivity.class);
		        intent.putExtra("back", true);
		        startActivity(intent);
				finish();
			}
		});
    }
    
    private void getScores() {
    	//Obtenemos y mostramos la puntiación final
    	ScoresController scoresController = new ScoresController(getApplicationContext());
		ArrayList<Score> scores = scoresController.getMaxScores();
	
		LayoutParams paramsPosition = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 10);
		LayoutParams paramsDate = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 45);
		LayoutParams paramsScore = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 45);
		paramsPosition.gravity=Gravity.CENTER;
		paramsDate.gravity=Gravity.CENTER;
		paramsScore.gravity=Gravity.RIGHT;
		
		if (null != scores) {
			mRow = new TableRow(this);
			mRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			//Pintamos las cabeceras
			mPosition = new TextView(this);
			mPosition.setTextAppearance(getApplicationContext(), R.style.scoresHeader);
			mPosition.setLayoutParams(paramsPosition);
			mPosition.setPadding(0, 5, 0, 5);
			mPosition.setText(getText(R.string.posicion));
			mRow.addView(mPosition);
			
			mTime = new TextView(this);
			mTime.setTextAppearance(getApplicationContext(), R.style.scoresHeader);
			mTime.setLayoutParams(paramsDate);
			mTime.setPadding(0, 5, 0, 5);
			mTime.setText(getText(R.string.date));
			mRow.addView(mTime);

	    	mScore = new TextView(this);
	    	mScore.setTextAppearance(getApplicationContext(), R.style.scoresHeader);
	    	mScore.setLayoutParams(paramsScore);
	    	mScore.setPadding(0, 5, 0, 5);
			mScore.setText(getText(R.string.scores));
			mRow.addView(mScore);
			
			mTable.addView(mRow);
			
			for (int i = 0; i < scores.size(); i++) {
				mRow = new TableRow(this);
				mRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				
				Score score = scores.get(i);
				
				mPosition = new TextView(this);
				mPosition.setTextAppearance(getApplicationContext(), R.style.scores);
				mPosition.setLayoutParams(paramsPosition);
				mPosition.setPadding(0, 5, 0, 5);
				mPosition.setText("" + (i + 1));
				mRow.addView(mPosition);
				
				mTime = new TextView(this);
				mTime.setTextAppearance(getApplicationContext(), R.style.scores);
				mTime.setLayoutParams(paramsDate);
				mTime.setPadding(0, 5, 0, 5);
				mTime.setText(score.getTime());
				mRow.addView(mTime);

		    	mScore = new TextView(this);
		    	mScore.setTextAppearance(getApplicationContext(), R.style.scores);
		    	mScore.setLayoutParams(paramsScore);
		    	mScore.setPadding(0, 5, 0, 5);
		    	//TODO: Corregir alineación de las puntuaciones
		    	if (score.getScore() / 100000 == 0) {
		    		if (score.getScore() / 10000 == 0) {
			    		mScore.setText("    " + score.getScore());
			    	} else {
			    		mScore.setText("  " + score.getScore());
			    	}
		    	} else {
		    		mScore.setText("" + score.getScore());
		    	}
		    	
				
				mRow.addView(mScore);
				
				mTable.addView(mRow);
			}
		}
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
		Intent intent = new Intent(ScoresActivity.this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
		finish();
	}
}
