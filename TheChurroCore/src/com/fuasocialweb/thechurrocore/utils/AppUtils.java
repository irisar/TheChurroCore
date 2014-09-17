package com.fuasocialweb.thechurrocore.utils;

import com.google.android.gms.ads.AdRequest;

public class AppUtils {
	
	public static AdRequest getAddRequest() {
		//AdRequest adRequest = new AdRequest.Builder().build(); // Iniciar una solicitud genérica.
		//AdRequest adRequest = new AdRequest.Builder().setGender(AdRequest.GENDER_FEMALE).build(); // Iniciar una solicitud para el target de mujeres.
	    AdRequest adRequest = new AdRequest.Builder().addTestDevice("974B45D65AD330177EDDC7B8D1F144A5").build(); //Solicitud de test
	    return adRequest;
	}
}
