package com.fuasocialweb.thechurrocore.activities;


import java.util.List;

import com.fuasocialweb.thechurrocore.R;
import com.fuasocialweb.thechurrocore.db.DataBaseHelper;
import com.fuasocialweb.thechurrocore.db.beans.Status;
import com.fuasocialweb.thechurrocore.db.controllers.StatusController;
import com.google.analytics.tracking.android.EasyTracker;
import com.nvanbenschoten.motion.ParallaxImageView;

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
		animateMenu(); //Ejecutamos la animación del menú
	    lastLevel(); //Obtiene el último nivel visitado
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
			}
		});
	    
	    mButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LevelActivity.class);
	            intent.putExtra("level", 1);
	            startActivity(intent);
			}
		});
	    
	    mButton3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	    
	    mButton4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
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
	                    	wait(3000);
	                    	mOptionsMenu.getHandler().post(new Runnable() {
		            		    public void run() {
		            		    	Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
		            		    	mOptionsMenu.startAnimation(slideUp);
		            		    	mOptionsMenu.setVisibility(View.VISIBLE);
		            		    }
		            		});
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
     * Obtiene el último nivel visitado (1 si es la primera partida)
     */
    private void lastLevel() {
    	//Comprobamos si se puede leer el status
    	StatusController statusController = new StatusController(getApplicationContext());
    	List<Status> statuses = statusController.getAllStatus();
    	Status status = null;
    	int level = 1;
    	
    	if (null != statuses && statuses.size() > 0) {
    		status = statuses.get(0);
    		//Si el nivel es mayor que el primero mostramos la opción de continuar
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

    
}
