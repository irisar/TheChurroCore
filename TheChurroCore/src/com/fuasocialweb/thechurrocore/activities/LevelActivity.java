package com.fuasocialweb.thechurrocore.activities;

import java.util.ArrayList;

import com.fuasocialweb.thechurrocore.R;
import com.fuasocialweb.thechurrocore.db.beans.Question;
import com.fuasocialweb.thechurrocore.db.beans.Status;
import com.fuasocialweb.thechurrocore.db.controllers.QuestionController;
import com.fuasocialweb.thechurrocore.db.controllers.StatusController;
import com.fuasocialweb.thechurrocore.utils.AppUtils;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.*;

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
	private TextView mTextLifesResult;
	private TextView mTextTime;
	private RelativeLayout mResult;
	private TextView mTextResult;
	private ImageView mImage;
	private Thread mThread;
	private int mNextLevel = 0;
	private CountDownTimer mTimer;
	private int mPuntuacion = 10000;
	private AdView adView;
	private int mNumLifes = 3;
	
	private ArrayList<Question> mQuestions = null;
	
	
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
    	mTextLifesResult = (TextView) findViewById(R.id.lifesResult);
    	mTextTime = (TextView) findViewById(R.id.time);
    	mAnswer1 = (Button) findViewById(R.id.option1);
	    mAnswer2 = (Button) findViewById(R.id.option2);
	    mAnswer3 = (Button) findViewById(R.id.option3);
	    mAnswer4 = (Button) findViewById(R.id.option4);
	    mResult = (RelativeLayout) findViewById(R.id.result);
	    mTextResult = (TextView) findViewById(R.id.resultText);
	    mImage = (ImageView) findViewById(R.id.image);
	    
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
    		//Si existe el array de preguntas lo recuperamos, si no lo obtenemos
    		if (null != extras.get("questions")) {
    			try {
    				mQuestions = (ArrayList<Question>)extras.get("questions");
    			} catch (Exception e) {	
    			}
    		}
    		if (mQuestions == null) {
    			QuestionController questionController = new QuestionController(getApplicationContext());
    			mQuestions = questionController.getQuestionsFromLevel(mLevel);
    		}
    	}
    	
    	//Obtenemos la pregunta y la respuesta
    	Question question = mQuestions.get(mQuestionNumber);
    	mQuestion.setText(question.getQuestion());
    	mAnswer1.setText(question.getAnswer1());
    	mAnswer2.setText(question.getAnswer2());
    	mAnswer3.setText(question.getAnswer3());
    	mAnswer4.setText(question.getAnswer4());
    	mCorrectAnswer = question.getCorrectAnswer();
    	String imageName = question.getMultimedia();
    	try {
    		int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
    		Drawable image = getResources().getDrawable(drawableResourceId);
	        mImage.setImageDrawable(image);
    	} catch (Exception e) {
    		Log.d("TheChurroCore", "Error: " + e);
    	}
        
        
    	//Si el nivel es 1 y la pregunta 0 reiniciamos las vidas y obtenemos 
    	if (mLevel == 1 && mQuestionNumber == 0) {
			StatusController statusController = new StatusController(getApplicationContext());
			Status status = new Status("Player1");
			statusController.update(status);
    	}
    	
    	//Obtenemos las vidas
    	StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
    	mTextLifes.setText(status.getLifes(getApplicationContext()));
    	mTextLifesResult.setText(status.getLifes(getApplicationContext()));
    	mNumLifes = status.getLife();
    	
    	//Escribimos el número de pregunta
    	mTextLevel.setText(mTextLevel.getText() + " " + mLevel + "-" + (mQuestionNumber + 1));
    }
    
    
    private boolean checkAnswer(int answer) { 
    	if (mCorrectAnswer == answer) {
			//Si la respuesta es correcta lo indicamos y pasamos a la siguiente fase
			mTextResult.setText(R.string.correct);
			mTextLifesResult.setText(Status.getLifes(getApplicationContext(), mNumLifes));
			mNextLevel = 2;
			habilitarBotones(false);
			animateResult();
    	} else {
			//Si la respuesta es incorrecta lo indicamos, restamos vida y ocultamos la respuesta
			mTextResult.setText(R.string.incorrect);
			mTextLifesResult.setText(Status.getLifes(getApplicationContext(), mNumLifes - 1));
			mNextLevel = 0;
			habilitarBotones(false);
			animateResult();
			habilitarBotones(true);
    	}
    	return false;
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
			            		    	mNumLifes = repeatLevel();
			            		    	if (mNumLifes > 0) {
			            		    		if (mNumLifes == 1) {
			            		    			mPuntuacion = 4000;
			            		    		} else if (mNumLifes == 2) {
			            		    			mPuntuacion = 7000;
			            		    		}
			            		    		mTextLifes.setText(Status.getLifes(getApplicationContext(), mNumLifes));
			            		    		mTextLifesResult.setText(Status.getLifes(getApplicationContext(), mNumLifes));
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
    
	//Función para pasar al siguiente nivel
	private void nextLevel() {
		QuestionController questionController = new QuestionController(getApplicationContext());
		boolean goToNextLevel = true;
		//Si hay más preguntas de este nivel y no hemos pasado de la pregunta 10
		int nextLevel = mLevel;
		int nextQuestionNumber = mQuestionNumber;
		if (null != mQuestions && (mQuestions.size() > mQuestionNumber + 1) && (mQuestionNumber < 10)) {
			nextLevel = mLevel;
			nextQuestionNumber = mQuestionNumber + 1;
		} else { //No hay más preguntas de este nivel
			mQuestions = questionController.getQuestionsFromLevel(mLevel + 1);
			if (null != mQuestions && mQuestions.size() > 0) {
				nextLevel = mLevel + 1;
				nextQuestionNumber = 0;
			} else {
				goToNextLevel = false;
	    		saveStatus(1, 0);

				Intent intent = new Intent(LevelActivity.this, ChampionActivity.class);
		        startActivity(intent);
		        finish();
			}
		}
		if (goToNextLevel) {
			saveStatus(nextLevel, nextQuestionNumber);
	
			Intent intent = new Intent(LevelActivity.this, LevelActivity.class);
	        intent.putExtra("level", nextLevel);
	        intent.putExtra("question", nextQuestionNumber);
	        intent.putExtra("questions", mQuestions);
	
	        startActivity(intent);
	        finish();
		}
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
		Intent intent = new Intent(LevelActivity.this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
		finish();
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
}
