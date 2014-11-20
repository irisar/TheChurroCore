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
 * Actividad principal de la aplicación
 * @author fmagana
 *
 */
public class MainActivity extends Activity {

	private Thread mThread;
	private LinearLayout mOptionsMenu;
	private ParallaxImageView mBackground;
	private Button mPlay;
	private Button mScores;
	private Button mInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createResources(); //Creamos los recursos necesarios
		createDatabase(); //Creamos la base de datos (si no existe)
		animateMenu(); //Ejecutamos la animación del menú
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
    	mPlay = (Button) findViewById(R.id.play);
	    mScores = (Button) findViewById(R.id.scores);
	    mInfo = (Button) findViewById(R.id.info);
	    mOptionsMenu = (LinearLayout) findViewById(R.id.start);
	    mBackground = (ParallaxImageView)findViewById(R.id.background);
    }
    
    private void createEvents() {
	    mPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LevelSelectorActivity.class);
	            startActivity(intent);
	            finish();
			}
		});

	    mScores.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ScoresActivity.class);
		        startActivity(intent);
				finish();
			}
		});
	    
	    mInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, InfoActivity.class);
		        startActivity(intent);
				finish();
			}
		});
    }
    
    /**
     * Crea el hilo para la animación del menú principal
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


}
