package com.lovelydeveloper.churrocore.activities;

import com.lovelydeveloper.churrocore.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.lovelydeveloper.churrocore.db.DataBaseHelper;
import com.lovelydeveloper.churrocore.db.beans.Status;
import com.lovelydeveloper.churrocore.db.controllers.QuestionController;
import com.lovelydeveloper.churrocore.db.controllers.StatusController;
import com.lovelydeveloper.churrocore.nvanbenschoten.motion.ParallaxImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Actividad principal de la aplicaci�n
 * @author fmagana
 *
 */
public class MainActivity extends Activity {

	private Thread mThread;
	private LinearLayout mOptionsMenu;
	private ParallaxImageView mBackground;
	private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	private Button mButton4;
	private int mLastLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createResources(); //Creamos los recursos necesarios
		createDatabase(); //Creamos la base de datos (si no existe)
		animateMenu(); //Ejecutamos la animaci�n del men�
	    lastLevel(); //Obtiene el �ltimo nivel visitado
		createEvents(); //Crea los eventos
	}
	
	
	public void onResume() {
        super.onResume();
        mBackground.registerSensorManager();
        
    }

    @Override
    public void onPause() {
        mBackground.unregisterSensorManager();
         super.onPause();
    }
    
    
    private void createResources() {
    	setContentView(R.layout.activity_main);
    	mButton1 = (Button) findViewById(R.id.button1);
	    mButton2 = (Button) findViewById(R.id.button2);
	    mButton3 = (Button) findViewById(R.id.button3);
	    mButton4 = (Button) findViewById(R.id.button4);
	    mOptionsMenu = (LinearLayout) findViewById(R.id.start);
	    mBackground = (ParallaxImageView)findViewById(R.id.background);
    }
    
    private void createEvents() {
	    mButton1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LevelActivity.class);
	            intent.putExtra("level", mLastLevel);
	            startActivity(intent);
	            finish();
			}
		});
	    
	    mButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveStatus(1,0);
				mButton1.setVisibility(View.GONE);
				Intent intent = new Intent(MainActivity.this, LevelActivity.class);
				//Intent intent = new Intent(MainActivity.this, HangmanActivity.class);
	            intent.putExtra("level", 1);
	            startActivity(intent);
	            finish();
			}
		});
	    
	    mButton3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ScoresActivity.class);
		        startActivity(intent);
				finish();
			}
		});
	    
	    mButton4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, InfoActivity.class);
		        startActivity(intent);
				finish();
			}
		});
    }
    
    /**
     * Crea el hilo para la animaci�n del men� principal
     */
    private void animateMenu() {
	    mThread = new Thread(){
	        @Override
	        public void run(){
	            try {
	                synchronized(this){
	                    try {
	                    	Bundle extras = getIntent().getExtras();
	                    	if (extras != null && null != extras.get("back")) {
	                    		mOptionsMenu.setVisibility(View.VISIBLE);
	                    	} else {
	                    		wait(3000);
		                    	mOptionsMenu.getHandler().post(new Runnable() {
			            		    public void run() {
			            		    	Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
			            		    	mOptionsMenu.startAnimation(slideUp);
			            		    	mOptionsMenu.setVisibility(View.VISIBLE);
			            		    }
			            		});
	                    	}
	                    	
	                    	
	                    } catch (NullPointerException e) {
	                    	//Error al girar dispositivo mientras carga
	                    }
	                    
	                }
	            }
	            catch(InterruptedException e){  
	            	Log.d("TheChurroCore", "Hilo interrumpido: " + e.toString());
	            }            
	        }
	    };
	    mThread.start();
    }
    
    
    /**
     * Metodo encargado de crear la base de datos (si no existe) mediante DataBaseHelper
     */
    private void createDatabase() {
    	DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
        myDbHelper = new DataBaseHelper(this);
        try {
        	myDbHelper.createDataBase();
        } catch (Exception e) {
        	Log.d("TheChurroCore", "Excepcion al crear base de datos: " + e.toString());
        }
    }
    
    /**
     * Obtiene el �ltimo nivel visitado (1 si es la primera partida)
     */
    private void lastLevel() {
    	//Comprobamos si se puede leer el status
    	StatusController statusController = new StatusController(getApplicationContext());
    	Status status = statusController.getStatus(1);
    	int level = 1;
    	
    	if (null != status) {
    		//Si el nivel es mayor que el primero mostramos la opci�n de continuar
    		if (status.getLevel() > 1) {
    			mButton1.setVisibility(View.VISIBLE);
    			level = status.getLevel();
    		}
    	} else {
    		status = new Status("usuario1");
        	statusController.addStatus(status);
    	}
    	mLastLevel = level;
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

	private void saveStatus(int level, int question) {
		StatusController statusController = new StatusController(getApplicationContext());
		Status status = statusController.getStatus(1);
		status.setLevel(level);
		status.setScore(0);
		status.setLife(3);
		statusController.update(status);
		QuestionController questionController = new QuestionController(getApplicationContext());
		questionController.reset();
	}
}