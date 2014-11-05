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
import android.os.Bundle;
import android.os.CountDownTimer;
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
 * Actividad de nivel
 * @author fmagana
 *
 */
public class LevelActivity extends Activity {

	private TextView mQuestion;
	private TextView mTextLevel;
	private TextView mTextLifes;
	private TextView mTextLifesResult;
	private TextView mTextTime;
	private TextView mTextResult;
	private Button mAnswer1;
	private Button mAnswer2;
	private Button mAnswer3;
	private Button mAnswer4;
	private RelativeLayout mResult;
	private ImageView mImage;
	private Thread mThread;
	private LinearLayout mResponse;
	private LinearLayout mLetters;
	
	private CountDownTimer mTimer;
	private AdView adView;
	private Question mQuestionBean = null;
	
	private int mLevel;
	private int mCorrectAnswer;
	private int mQuestionID;
	private int mNextLevel = 0;
	private int mScore = 10000;
	private int mNumLifes = 3;
	private int mTime = 11000;
	private int mMinus = 300;
	private int mNumLettersToResolve;
	private int mType;
	private int timeUntilFinish;
	private boolean pause = false;
	
	
	private String[] mCorrectAnswerText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getQuestion(); //Obtenemos la pregunta
		mType = mQuestionBean.getType();
		mQuestionID = mQuestionBean.getId();
		countDown();
		if (mType == 1) {
			createResourcesQuestion(); //Creamos los recursos necesarios para preguntas
			generateLevelQuestion(); //Obtenemos preguntas y respuestas
		} else {
			createResourcesHangman(); //Creamos los recursos necesarios para ahorcado
			generateLevelHangman();
		}
		generateGlobalVariables(); //Generamos los elementos comuntes
		
	}

	/**
	 * Obtiene la siguiente pregunta
	 */
	private void getQuestion() {
		//Recuperamos la fase
    	Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    		if (null != extras.get("level")) {
    			mLevel = extras.getInt("level");
    		} else {
    			mLevel = 1;
    		}
    	}
    	
    	//Obtenemos las preguntas no respondidas del nivel
    	QuestionController questionController = new QuestionController(getApplicationContext());
		ArrayList<Question> questions = questionController.getUnansweredQuestionsFromLevel(mLevel);
    	
    	//Obtenemos la pregunta actual
		mQuestionBean = questions.get(0);
	}
	
	
	
    /**
     * Crea recursos cuando estamos ante una pregunta normal
     */
    private void createResourcesQuestion() {
    	setContentView(R.layout.activity_question);
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
	    
	    crearAnuncio(); //Crea los anuncios
	    createEventsQuestion(); //Crea los eventos necesarios
    }
    
    /**
     * Crea recursos cuando estamos en un ahorcado
     */
    private void createResourcesHangman() {
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
	    crearAnuncio();
    }
    
    /**
     * Lanza los listener de los botones
     */
    private void createEventsQuestion() {
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
    
    /**
     * Generamos los elementos comunes de todos los tipos
     */
    private void generateGlobalVariables() {
    	//Pintamos la imagen
    	String imageName = mQuestionBean.getMultimedia();
    	try {
    		int drawableResourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
    		Drawable image = getResources().getDrawable(drawableResourceId);
	        mImage.setImageDrawable(image);
    	} catch (Exception e) {
    		Log.d("TheChurroCore", "Error: " + e);
    	}
    	
    	//Obtenemos las vidas
    	StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
    	mTextLifes.setText(status.getLifes(getApplicationContext()));
    	mTextLifesResult.setText(status.getLifes(getApplicationContext()));
    	mNumLifes = status.getLife();
    	
    	//Escribimos el nivel
    	mTextLevel.setText(mTextLevel.getText() + " " + mLevel);
    }
    
    
    
    /**
     * Obtiene los elementos del nivel para las preguntas normales
     */
    private void generateLevelQuestion() {
    	mQuestion.setText(mQuestionBean.getQuestion());
    	mAnswer1.setText(mQuestionBean.getAnswer1());
    	mAnswer2.setText(mQuestionBean.getAnswer2());
    	mAnswer3.setText(mQuestionBean.getAnswer3());
    	mAnswer4.setText(mQuestionBean.getAnswer4());
    	mCorrectAnswer = mQuestionBean.getCorrectAnswer();
    }
    
    /**
     * Obtiene los elementos del nivel para el ahorcado
     */
    private void generateLevelHangman() {
    	mQuestion.setText(mQuestionBean.getQuestion());
    	
    	mCorrectAnswer = mQuestionBean.getCorrectAnswer();
    	String answer = mQuestionBean.getCorrectAnswerString();
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
    }
    
    /**
     * Comprueba si la respuesta es correcta
     */
    private boolean checkAnswer(int answer) { 
    	pause = true;
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


    
	//Función para pasar al siguiente nivel
	private void nextLevel() {
		QuestionController questionController = new QuestionController(getApplicationContext());
		questionController.setAsAnswered(mQuestionID); //Indicamos que hemos respondido a esta pregunta

		//Comprobamos si hay preguntas del nivel sin responder
		ArrayList<Question> questions = questionController.getUnansweredQuestionsFromLevel(mLevel);
		if (null != questions && questions.size() > 0) {
			//Hay más elementos de este nivel
			saveStatus(mLevel, 0);
			Intent intent = new Intent(LevelActivity.this, LevelActivity.class);
	        intent.putExtra("level", mLevel);
	        startActivity(intent);
	        finish();
		} else {
			//Comprobamos si hay elementos en el siguiente nivel
			mLevel = mLevel + 1;
			questions = questionController.getUnansweredQuestionsFromLevel(mLevel);
			if (null != questions && questions.size() > 0) {
				//Hay más elementos del siguiente nivel
				saveStatus(mLevel, 0);
				Intent intent = new Intent(LevelActivity.this, LevelActivity.class);
		        intent.putExtra("level", mLevel);
		        startActivity(intent);
			} else {
				saveStatus(1, 0);
				Intent intent = new Intent(LevelActivity.this, ChampionActivity.class);
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
	
	/**
	 * Obtiene los caracteres para seleccionar en el ahorcado
	 */
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
	
	/**
	 * Comprueba la letra seleccionada
	 */
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
			pause = true;
			mTextResult.setText(R.string.correct);
			mTextLifesResult.setText(Status.getLifes(getApplicationContext(), mNumLifes));
			mNextLevel = 2;
			animateResult();
		}
	}
	
	private void countDown() {
		try {
			mTimer.cancel();
		} catch (Exception e) {
		}
		if (mType == 2) {
			mTime = 31000;
			mMinus = 100;
		}
		timeUntilFinish = mTime;
		
		mTimer = new CountDownTimer(timeUntilFinish, 1000) {
			public void onTick(long millisUntilFinished) {
				if (!pause) {
					timeUntilFinish = (int) millisUntilFinished;
					mTextTime.setText(getText(R.string.time1) + " " + (timeUntilFinish / 1000) + " " + getText(R.string.time2));
					mScore = mScore - mMinus;
				}
			}
		 
			@Override
			public void onFinish() {
				if (!pause) {
					mTextResult.setText(R.string.time_over);
		    		mNextLevel = 1;
		    		if (mType == 1) {
		    			habilitarBotones(false);
		    			animateResult();
		    			habilitarBotones(true);
		    		} else if (mType == 2) {
		    			animateResult();
		    		}
				}
			}
		}.start();
	}
	
    /**
     * Anima los resultados
     */
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
			            		    			mScore = 4000;
			            		    		} else if (mNumLifes == 2) {
			            		    			mScore = 7000;
			            		    		}
			            		    		mTextLifes.setText(Status.getLifes(getApplicationContext(), mNumLifes));
			            		    		mTextLifesResult.setText(Status.getLifes(getApplicationContext(), mNumLifes));
			            		    		Animation zoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
				            		    	mResult.startAnimation(zoomOut);
				            		    	mResult.setVisibility(View.INVISIBLE);
				            		    	countDown();
			            		    	} else {
			            		    		//Guardamos el estado del usuario
			            		    		mScore = 0;
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

	private void saveStatus(int level, int question) {
		StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
		int puntuacion = status.getScore() + mScore;
		status.setLevel(level);
		status.setScore(puntuacion);
		statusController.update(status);
	}
	
	
	
	private void crearAnuncio() {
		//Crear anuncio
	    adView = new AdView(this);
	    adView.setAdUnitId(""+getText(R.string.add_id));
	    adView.setAdSize(AdSize.SMART_BANNER);
	    LinearLayout layout = (LinearLayout)findViewById(R.id.anuncio);
	    layout.addView(adView);
	    AdRequest adRequest = AppUtils.getAddRequest();
	    adView.loadAd(adRequest);
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
