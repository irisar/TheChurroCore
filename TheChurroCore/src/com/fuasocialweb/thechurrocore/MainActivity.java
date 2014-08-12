package com.fuasocialweb.thechurrocore;

import com.nvanbenschoten.motion.ParallaxImageView;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private Thread thread;
	private LinearLayout mOptionsMenu;
	private ParallaxImageView mBackground;
	private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	private Button mButton4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    mButton1 = (Button) findViewById(R.id.button1);
	    mButton2 = (Button) findViewById(R.id.button2);
	    mButton3 = (Button) findViewById(R.id.button3);
	    mButton4 = (Button) findViewById(R.id.button4);
	    mOptionsMenu = (LinearLayout) findViewById(R.id.start);
	    mBackground = (ParallaxImageView)findViewById(R.id.background);
	 
	    
	    mButton1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	    
	    mButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
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
	    
	    
	    thread = new Thread(){
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
	            catch(InterruptedException ex){                    
	            }
	            // TODO              
	        }
	    };
	    thread.start();
	    
	    
	    
	    
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
}
