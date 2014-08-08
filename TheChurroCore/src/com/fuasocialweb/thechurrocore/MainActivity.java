package com.fuasocialweb.thechurrocore;

import com.nvanbenschoten.motion.ParallaxImageView;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private Thread thread;
	private Button mButton;
	private LinearLayout mStartLayout;
	private ParallaxImageView mBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    //mButton = (Button) findViewById(R.id.start);
	    mStartLayout = (LinearLayout) findViewById(R.id.start);
	    mBackground = (ParallaxImageView)findViewById(R.id.background);
	 
	    thread = new Thread(){
	        @Override
	        public void run(){
	            try {
	                synchronized(this){
	                    try {
	                    	wait(3000);
	                    	mStartLayout.getHandler().post(new Runnable() {
		            		    public void run() {
		            		    	Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
	
		            		    	mStartLayout.startAnimation(slideUp);
		            		    	mStartLayout.setVisibility(View.VISIBLE);
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
