package com.fuasocialweb.thechurrocore.activities;



import com.fuasocialweb.thechurrocore.R;
import com.fuasocialweb.thechurrocore.db.beans.Question;
import com.fuasocialweb.thechurrocore.db.beans.Status;
import com.fuasocialweb.thechurrocore.db.controllers.QuestionController;
import com.fuasocialweb.thechurrocore.db.controllers.StatusController;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Actividad de nivel
 * @author fmagana
 *
 */
public class LevelActivity extends Activity {

	private TextView mQuestion;
	private Button mAnswer1;
	private Button mAnswer2;
	private Button mAnswer3;
	private Button mAnswer4;
	private int mLevel;
	private int mQuestionNumber;
	private int mCorrectAnswer;
	private TextView mTextLevel;
	private TextView mTextLifes;
	private TextView mTextTime;
	private RelativeLayout mResult;
	private TextView mTextResult;
	private Thread mThread;
	private int mNextLevel = 0;
	private CountDownTimer mTimer;
	private int mPuntuacion = 10000;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		countDown();
		createResources(); //Creamos los recursos necesarios
		generateLevel(); //Obtenemos preguntas y respuestas
		createEvents(); //Crea los eventos
	}

    
    private void createResources() {
    	setContentView(R.layout.activity_level);
    	mQuestion = (TextView) findViewById(R.id.question);
    	mTextLevel = (TextView) findViewById(R.id.level);
    	mTextLifes = (TextView) findViewById(R.id.life);
    	mTextTime = (TextView) findViewById(R.id.time);
    	mAnswer1 = (Button) findViewById(R.id.option1);
	    mAnswer2 = (Button) findViewById(R.id.option2);
	    mAnswer3 = (Button) findViewById(R.id.option3);
	    mAnswer4 = (Button) findViewById(R.id.option4);
	    mResult = (RelativeLayout) findViewById(R.id.result);
	    mTextResult = (TextView) findViewById(R.id.resultText);
    }
    
    private void createEvents() {
	    mAnswer1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				checkAnswer(1);
			}
		});
	    
	    mAnswer2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				checkAnswer(2);
			}
		});
	    
	    mAnswer3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				checkAnswer(3);
			}
		});
	    
	    mAnswer4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				checkAnswer(4);
			}
		});
    }
    
    private void generateLevel() {
    	//Recuperamos la fase y el número de pregunta
    	Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    		if (null != extras.get("level")) {
    			mLevel = extras.getInt("level");
    		} else {
    			mLevel = 1;
    		}
    		if (null != extras.get("question")) {
    			mQuestionNumber = extras.getInt("question");
    		} else {
    			mQuestionNumber = 0;
    		}
    	}
    	
    	//Si el nivel es 1 y la pregunta 0 reiniciamos las vidas
    	if (mLevel == 1 && mQuestionNumber == 0) {
			StatusController statusController = new StatusController(getApplicationContext());
			Status status = new Status("Player1");
			statusController.update(status);
    	}
    	
    	//Obtenemos la pregunta y la respuesta
    	QuestionController questionController = new QuestionController(getApplicationContext());
    	
    	Question question = questionController.getQuestion(mLevel, mQuestionNumber);
    	mQuestion.setText(question.getQuestion());
    	mAnswer1.setText(question.getAnswer1());
    	mAnswer2.setText(question.getAnswer2());
    	mAnswer3.setText(question.getAnswer3());
    	mAnswer4.setText(question.getAnswer4());
    	
    	mTextLevel.setText(mTextLevel.getText() + " " + mLevel + "-" + (mQuestionNumber + 1));

    	//Obtenemos las vidas
    	StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
    	mTextLifes.setText(status.getLifes(getApplicationContext()));
    	
    	mCorrectAnswer = question.getCorrectAnswer();
    }
    
    
    private boolean checkAnswer(int answer) { 
    	if (mCorrectAnswer == answer) {
			//Si la respuesta es correcta lo indicamos y pasamos a la siguiente fase
			mTextResult.setText(R.string.correct);
			mNextLevel = 2;
			habilitarBotones(false);
			animateResult();
    	} else {
			//Si la respuesta es incorrecta lo indicamos, restamos vida y ocultamos la respuesta
			mTextResult.setText(R.string.incorrect);
			mNextLevel = 0;
			habilitarBotones(false);
			animateResult();
			habilitarBotones(true);
    	}
    	
    	return false;
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
	
	
	private void animateResult() {
		mThread = new Thread(){
	        @Override
	        public void run(){
	            try {
	                synchronized(this){
	                    try {
	                    	mResult.getHandler().post(new Runnable() {
		            		    public void run() {
		            		    	Animation zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
		            		    	mResult.startAnimation(zoomIn);
		            		    	mResult.setVisibility(View.VISIBLE);
		            		    }
		            		});
	                    	wait(3000);
	                    	if (mNextLevel == 2) {
	                    		nextLevel();
	                    	} else {
	                    		mResult.getHandler().post(new Runnable() {
			            		    public void run() {
			            		    	int lifes = repeatLevel();
			            		    	if (lifes > 0) {
			            		    		if (lifes == 1) {
			            		    			mPuntuacion = 4000;
			            		    		} else if (lifes == 2) {
			            		    			mPuntuacion = 7000;
			            		    		}
			            		    		mTextLifes.setText(Status.getLifes(getApplicationContext(), lifes));
			            		    		Animation zoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
				            		    	mResult.startAnimation(zoomOut);
				            		    	mResult.setVisibility(View.INVISIBLE);
				            		    	countDown();
			            		    	} else {
			            		    		//Guardamos el estado del usuario
			            		    		mPuntuacion = 0;
			            		    		saveStatus(mLevel, 0);
			            		    		
			            		    		Intent intent = new Intent(LevelActivity.this, GameOverActivity.class);
			            		            startActivity(intent);
			            		    		finish();
			            		    	}
			            		    }
			            		});
	                    	}
	                    } catch (NullPointerException e) {
	                    	//Error al girar dispositivo mientras carga
	                    }
	                }
	            }
	            catch(Exception e){  
	            	Log.d("TheChurroCore", "Hilo interrumpido: " + e.toString());
	            }            
	        }
	    };
	    mThread.start();
	}
    
	private void nextLevel() {
		
		
		QuestionController questionController = new QuestionController(getApplicationContext());
		
		int nextLevel = mLevel;
		int nextQuestionNumber = mQuestionNumber;
		
		int nextId = Integer.parseInt(mLevel + "" +(mQuestionNumber + 1));
		if (questionController.existsQuestionId(nextId)) { //Si existe hay siguiente pregunta del mismo nivel
			nextQuestionNumber++;
			saveStatus(nextLevel, nextQuestionNumber);
		} else { //No hay siguiente pregunta del mismo nivel
			nextId = Integer.parseInt((mLevel + 1) + "0");
			if (questionController.existsQuestionId(nextId)) { //Si existe hay siguiente nivel
				nextLevel++;
				nextQuestionNumber = 0;
				saveStatus(nextLevel, nextQuestionNumber);
			} else { //Si no existe no hay más niveles
				saveStatus(nextLevel, nextQuestionNumber);

				//TODO: mostrar mensaje de que se ha terminado
		        finish();
			}
			
		}
		
		
		Intent intent = new Intent(LevelActivity.this, LevelActivity.class);
        intent.putExtra("level", nextLevel);
        intent.putExtra("question", nextQuestionNumber);

        startActivity(intent);
        finish();
	}
	
	private int repeatLevel() {
		StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
		int lifes = status.getLife() - 1;
		if (lifes > 0) {
			statusController.reduceLife(lifes);
		}
		return lifes;
	}
	
	/**
	 * Habilita o deshabilita los botones de la pantalla
	 * @param habilitar
	 */
	private void habilitarBotones(boolean habilitar) {
    	mAnswer1.setEnabled(habilitar);
    	mAnswer2.setEnabled(habilitar);
    	mAnswer3.setEnabled(habilitar);
    	mAnswer4.setEnabled(habilitar);
	}
	
	
	private void countDown() {
		try {
			mTimer.cancel();
		} catch (Exception e) {
			
		}
		
		mTimer = new CountDownTimer(11000, 1000) {
			public void onTick(long millisUntilFinished) {
				mTextTime.setText(getText(R.string.time1) + " " + (millisUntilFinished / 1000) + " " + getText(R.string.time2));
				mPuntuacion = mPuntuacion - 300;
			}
		 
			@Override
			public void onFinish() {
				mTextResult.setText(R.string.time_over);
	    		mNextLevel = 1;
	    		habilitarBotones(false);
	    		animateResult();
	    		habilitarBotones(true);
			}
		}.start();
	}
	
	private void saveStatus(int level, int question) {
		StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
		int puntuacion = status.getScore() + mPuntuacion;
		status.setLevel(level);
		status.setScore(puntuacion);
		statusController.update(status);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(LevelActivity.this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
		finish();
	}
}
