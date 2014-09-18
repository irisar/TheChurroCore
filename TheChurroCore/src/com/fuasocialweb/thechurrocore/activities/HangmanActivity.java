package com.fuasocialweb.thechurrocore.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.*;

/**
 * Actividad de ahorcado
 * @author fmagana
 *
 */
public class HangmanActivity extends Activity {

	private TextView mQuestion;
	private int mLevel;
	private int mQuestionNumber;
	private int mCorrectAnswer;
	private TextView mTextLevel;
	private TextView mTextLifes;
	private TextView mTextLifesResult;
	private TextView mTextTime;
	private LinearLayout mResponse;
	private LinearLayout mLetters;
	private RelativeLayout mResult;
	private TextView mTextResult;
	private ImageView mImage;
	private Thread mThread;
	private int mNextLevel = 0;
	private CountDownTimer mTimer;
	private int mPuntuacion = 10000;
	private AdView adView;
	private int mNumLifes = 3;
	private int mNumLettersToResolve;
	private String[] mCorrectAnswerText;
	
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
    	setContentView(R.layout.activity_hangman);
    	mQuestion = (TextView) findViewById(R.id.question);
    	mTextLevel = (TextView) findViewById(R.id.level);
    	mTextLifes = (TextView) findViewById(R.id.life);
    	mTextLifesResult = (TextView) findViewById(R.id.lifesResult);
    	mTextTime = (TextView) findViewById(R.id.time);
	    mResult = (RelativeLayout) findViewById(R.id.result);
	    mTextResult = (TextView) findViewById(R.id.resultText);
	    mImage = (ImageView) findViewById(R.id.image);
	    mResponse = (LinearLayout) findViewById(R.id.response);
	    mLetters = (LinearLayout) findViewById(R.id.letters);
	    
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
    	
    	mCorrectAnswer = question.getCorrectAnswer();
    	String answer = question.getCorrectAnswerString();
    	mCorrectAnswerText = answer.split("(?!^)");
    	mNumLettersToResolve = mCorrectAnswerText.length;
    	
    	for (int i = 0; i < mCorrectAnswerText.length; i++) {
    		TextView answerChars = new TextView(this);
    		answerChars.setId(i);
    		answerChars.setTextAppearance(getApplicationContext(), R.style.result);
    		answerChars.setText(" * ");
    		answerChars.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
            mResponse.addView(answerChars);
    	}
    	
    	ArrayList<String> charsToSelect = getCharsToSelect(mCorrectAnswerText);
    	LinearLayout lettersLine = new LinearLayout(this);
    	lettersLine.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
    	lettersLine.setOrientation(LinearLayout.HORIZONTAL);
    	lettersLine.setGravity(Gravity.CENTER);
    	boolean write = false;
    	for (int i = 0; i < charsToSelect.size(); i++) {
    		
    		Button letter = new Button(this);
    		letter.setTextAppearance(getApplicationContext(), R.style.mainButton);
    		letter.setText(charsToSelect.get(i));
    		letter.setLayoutParams((new LayoutParams(90,LayoutParams.WRAP_CONTENT)));
    		letter.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				Button b = (Button)v;
    				checkLetter(b);
    				Log.d("TheChurroCore", "PULSADA LA LETRA " + b.getText().toString());
    			}
    		});
    		lettersLine.addView(letter);
    		write = false;
            if ((i+1)%7 == 0) {
            	write = true;
    			mLetters.addView(lettersLine);
    			lettersLine = new LinearLayout(this);
    			lettersLine.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
    	    	lettersLine.setOrientation(LinearLayout.HORIZONTAL);
    	    	lettersLine.setGravity(Gravity.CENTER);
    		}
    	}
    	if (!write) {
			mLetters.addView(lettersLine);
		}

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
			animateResult();
    	} else {
			//Si la respuesta es incorrecta lo indicamos, restamos vida y ocultamos la respuesta
			mTextResult.setText(R.string.incorrect);
			mTextLifesResult.setText(Status.getLifes(getApplicationContext(), mNumLifes - 1));
			mNextLevel = 0;
			animateResult();
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
			            		    		
			            		    		Intent intent = new Intent(HangmanActivity.this, GameOverActivity.class);
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

		//Si hay más preguntas de este nivel y no hemos pasado de la pregunta 10
		int nextLevel = mLevel;
		int nextQuestionNumber = mQuestionNumber;
		if (null != mQuestions && (mQuestions.size() > mQuestionNumber + 1) && (mQuestionNumber < 10)) {
			nextLevel = mLevel;
			nextQuestionNumber = mQuestionNumber + 1;
			
			saveStatus(nextLevel, nextQuestionNumber);
			
			Intent intent = new Intent(HangmanActivity.this, HangmanActivity.class);
	        intent.putExtra("level", nextLevel);
	        intent.putExtra("question", nextQuestionNumber);
	        intent.putExtra("questions", mQuestions);
	
	        startActivity(intent);
	        finish();
		} else { //No hay más preguntas de este nivel
			mQuestions = questionController.getQuestionsFromLevel(mLevel + 1);
			if (null != mQuestions && mQuestions.size() > 0) {
				nextLevel = mLevel + 1;
				nextQuestionNumber = 0;
				
				saveStatus(nextLevel, nextQuestionNumber);
				
				Intent intent = new Intent(HangmanActivity.this, LevelActivity.class);
		        intent.putExtra("level", nextLevel);
		        intent.putExtra("question", nextQuestionNumber);
		        intent.putExtra("questions", mQuestions);
		
		        startActivity(intent);
		        finish();
			} else {
	    		saveStatus(1, 0);

				Intent intent = new Intent(HangmanActivity.this, ChampionActivity.class);
		        startActivity(intent);
		        finish();
			}
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

	
	private void countDown() {
		try {
			mTimer.cancel();
		} catch (Exception e) {
			
		}
		
		mTimer = new CountDownTimer(31000, 1000) {
			public void onTick(long millisUntilFinished) {
				mTextTime.setText(getText(R.string.time1) + " " + (millisUntilFinished / 1000) + " " + getText(R.string.time2));
				mPuntuacion = mPuntuacion - 100;
			}
		 
			@Override
			public void onFinish() {
				mTextResult.setText(R.string.time_over);
	    		mNextLevel = 1;
	    		animateResult();
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
	
	private ArrayList<String> getCharsToSelect(String[] answer){
		ArrayList<String> fullChars = new ArrayList<String>() {{add("a");add("b");add("c");add("d");add("e");add("f");add("g");
																add("h");add("i");add("j");add("k");add("l");add("m");add("n");
																add("ñ");add("o");add("p");add("q");add("r");add("s");add("t");
																add("u");add("v");add("w");add("x");add("y");add("z");}};
		ArrayList<String> characters = new ArrayList<String>();
		for (int i = 0; i < fullChars.size(); i++) {
			boolean exists = false;
			int counter = 0;
			while (!exists && counter < answer.length) {
				String character = answer[counter].toLowerCase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
				if (fullChars.get(i).equals(character)) {
					exists = true;
					characters.add(fullChars.get(i));
				}
				counter++;
			}
		}

		if (characters.size() < 20) {
			long seed = System.nanoTime();
			Collections.shuffle(fullChars, new Random(seed));
			
			for (int i = 0; (i < fullChars.size() && characters.size() < 20); i++) {
				boolean exists = false;
				int counter = 0;
				while (!exists && counter < answer.length) {
					String character = answer[counter].toLowerCase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
					if (character.equals(fullChars.get(i))) {
						exists = true;
					}
					counter++;
				}
				if (!exists) {
					characters.add(fullChars.get(i));
				}
			}
		}

		long seed = System.nanoTime();
		Collections.shuffle(characters, new Random(seed));
		return characters;
	}
	
	private void checkLetter(Button b) {
		String letter =  b.getText().toString();
		boolean exists = false;
		for (int i = 0; i < mCorrectAnswerText.length; i++) {
			if (letter.equals(mCorrectAnswerText[i])) {
				exists = true;
				TextView letterView = (TextView) findViewById(i);
				letterView.setText(" " + letter + " ");
				mNumLettersToResolve--;
			}
		}
		b.setVisibility(b.INVISIBLE);
		
		//Si no existe la letra restamos una vida
		if (!exists) {
			mTextResult.setText(R.string.incorrect);
			mTextLifesResult.setText(Status.getLifes(getApplicationContext(), mNumLifes - 1));
			mNextLevel = 0;
			animateResult();
		}
		
		//Si no quedan letras por resolver pasamos al siguiente nivel
		if (mNumLettersToResolve == 0) {
			mTextResult.setText(R.string.correct);
			mTextLifesResult.setText(Status.getLifes(getApplicationContext(), mNumLifes));
			mNextLevel = 2;
			animateResult();
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
		Intent intent = new Intent(HangmanActivity.this, MainActivity.class);
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
