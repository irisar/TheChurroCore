package com.lovelydeveloper.churrocore.activities;

import com.lovelydeveloper.churrocore.R;
import com.lovelydeveloper.churrocore.db.beans.Status;
import com.lovelydeveloper.churrocore.db.controllers.QuestionController;
import com.lovelydeveloper.churrocore.db.controllers.StatusController;
import com.lovelydeveloper.churrocore.utils.AppUtils;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Actividad de puntuaciones
 * @author fmagana
 *
 */
public class LevelSelectorActivity extends Activity {

	private AdView mAdView;
	private Button mBack;
	private ImageButton mLevel1;
	private ImageButton mLevel2;
	private ImageButton mLevel3;
	private ImageButton mLevel4;
	private ImageButton mLevel5;
	private ImageButton mLevel6;
	private ImageButton mLevel7;
	private ImageButton mLevel8;
	private ImageButton mLevel9;
	private int mLastLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createResources(); //Creamos los recursos necesarios
		lastLevel(); //Obtiene el último nivel visitado
		createEvents(); //Crea los eventos
		crearAnuncio(); //Crea los anuncios
	}

    
    private void createResources() {
    	setContentView(R.layout.activity_level_selector);
    	
    	mBack = (Button) findViewById(R.id.back);
    	mLevel1 = (ImageButton) findViewById(R.id.level1);
    	mLevel2 = (ImageButton) findViewById(R.id.level2);
    	mLevel3 = (ImageButton) findViewById(R.id.level3);
    	mLevel4 = (ImageButton) findViewById(R.id.level4);
    	mLevel5 = (ImageButton) findViewById(R.id.level5);
    	mLevel6 = (ImageButton) findViewById(R.id.level6);
    	mLevel7 = (ImageButton) findViewById(R.id.level7);
    	mLevel8 = (ImageButton) findViewById(R.id.level8);
    	mLevel9 = (ImageButton) findViewById(R.id.level9);
    }
    
    private void createEvents() {
    	
    	mBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LevelSelectorActivity.this, MainActivity.class);
		        intent.putExtra("back", true);
		        startActivity(intent);
				finish();
			}
		});
    	
    	
    	mLevel1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				play(1);
			}
    	});
    	
    	if (mLastLevel > 1) {
	    	mLevel2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					play(2);
				}
	    	});
    	} else {
    		mLevel2.setVisibility(ImageButton.GONE);
    	}
    	
    	if (mLastLevel > 2) {
	    	mLevel3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					play(3);
				}
	    	});
    	} else {
    		mLevel3.setVisibility(ImageButton.GONE);
    	}
    	
    	if (mLastLevel > 3) {
	    	mLevel4.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					play(4);
				}
	    	});
    	} else {
    		mLevel4.setVisibility(ImageButton.GONE);
    	}
    	
    	if (mLastLevel > 4) {
	    	mLevel5.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					play(5);
				}
	    	});
    	} else {
    		mLevel5.setVisibility(ImageButton.GONE);
    	}
    	
    	if (mLastLevel > 5) {
	    	mLevel6.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					play(6);
				}
	    	});
    	} else {
    		mLevel6.setVisibility(ImageButton.GONE);
    	}
    	
    	if (mLastLevel > 6) {
	    	mLevel7.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					play(7);
				}
	    	});
    	} else {
    		mLevel7.setVisibility(ImageButton.GONE);
    	}
    	
    	if (mLastLevel > 7) {
	    	mLevel8.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					play(8);
				}
	    	});
    	} else {
    		mLevel8.setVisibility(ImageButton.GONE);
    	}
    	
    	if (mLastLevel > 8) {
	    	mLevel9.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					play(9);
				}
	    	});
    	} else {
    		mLevel9.setVisibility(ImageButton.GONE);
    	}
    }

    private void play(int level) {
    	saveStatus(level);
    	Intent intent = new Intent(LevelSelectorActivity.this, LevelActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
        finish();
    }
    
    
    private void crearAnuncio() {
		//Crear anuncio
	    mAdView = new AdView(this);
	    mAdView.setAdUnitId(""+getText(R.string.add_id));
	    mAdView.setAdSize(AdSize.SMART_BANNER);
	    LinearLayout layout = (LinearLayout)findViewById(R.id.anuncio);
	    layout.addView(mAdView);
	    AdRequest adRequest = AppUtils.getAddRequest();
	    mAdView.loadAd(adRequest);
	}
    
    
    /**
     * Obtiene el mayor nivel visitado (1 si es la primera partida)
     */
    private void lastLevel() {
    	//Comprobamos si se puede leer el status
    	StatusController statusController = new StatusController(getApplicationContext());
    	Status status = statusController.getMaxStatus();
    	int level = 1;
    	
    	if (null != status) {
    		//Si el nivel es mayor que el primero mostramos la opción de continuar
    		if (status.getLevel() > 1) {
    			level = status.getLevel();
    		}
    	} else {
    		status = new Status();
        	statusController.addStatus(status);
    	}
    	mLastLevel = level;
    }
    
    /**
     * Inicia el status
     * @param level
     * @param question
     */
	private void saveStatus(int level) {
		StatusController statusController = new StatusController(getApplicationContext());
		Status status = new Status();
		status.setLevel(level);
		statusController.update(status);
		QuestionController questionController = new QuestionController(getApplicationContext());
		questionController.reset(level);
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
		Intent intent = new Intent(LevelSelectorActivity.this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
		finish();
	}
	
	public void onPause() {
		mAdView.pause();
		super.onPause();
	}
	 
	public void onResume() {
		super.onResume();
		mAdView.resume();
	}

	public void onDestroy() {
		mAdView.destroy();
		super.onDestroy();
	}
	
	
	
}
