package com.lovelydeveloper.churrocore.activities;

import com.lovelydeveloper.churrocore.R;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Actividad de puntuaciones
 * @author fmagana
 *
 */
public class InfoActivity extends Activity {

	private WebView mWeb;
	private Button mGoToMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createResources(); //Creamos los recursos necesarios
		createEvents(); //Crea los eventos
	}

    
    private void createResources() {
    	setContentView(R.layout.activity_info);
    	mGoToMenu = (Button) findViewById(R.id.go_to_menu);
    	
    	String color = "" + getText(R.color.mainText);
    	color = color.replaceAll("#FF", "#").replaceAll("#ff", "#");
    	String color2 = "" + getText(R.color.altText);
    	color2 = color2.replaceAll("#FF", "#").replaceAll("#ff", "#");
    	
    	mWeb = (WebView) findViewById(R.id.info);
    	mWeb.getSettings().setJavaScriptEnabled(true);
    	mWeb.getSettings().setLoadsImagesAutomatically(true);
    	mWeb.setBackgroundColor(0x00000000);
    	mWeb.clearCache(true);
    	String content = "<html><head><style>*{background-color:transparent !important;}body{width:95%;margin-left:auto;margin-right:auto;font-size:1.4em;color:" + color + ";}a{color:" + color2 + ";text-decoration: none;}</style></head><body>" + getText(R.string.informacion) + "</body></html>";
    	Log.d("TheChurroCore", content);
    	mWeb.loadDataWithBaseURL(null,content,"text/html","utf-8",null);    	
    	mWeb.setBackgroundColor(0x00000000);
    }
    
    private void createEvents() {
	    mGoToMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Vamos al menu 
				Intent intent = new Intent(InfoActivity.this, MainActivity.class);
		        intent.putExtra("back", true);
		        startActivity(intent);
				finish();
			}
		});
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
		Intent intent = new Intent(InfoActivity.this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
		finish();
	}
}
