package fr.univpau.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class MyPreferenceManager {
	
	private Activity _activity;

	public MyPreferenceManager(Activity activity) {
		this._activity = activity;
	}

	public double getLat() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_activity.getBaseContext());	
		return prefs.getFloat("lat", (float) 0.0);
	}

	public void setLat(float lat) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_activity.getBaseContext());	
		Editor editor = prefs.edit();
		editor.putFloat("lat", lat);
		editor.apply();
	}

	public double getLng() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_activity.getBaseContext());	
		return prefs.getFloat("lng", (float) 0.0);
	}

	public void setLng(float lng) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_activity.getBaseContext());	
		Editor editor = prefs.edit();
		editor.putFloat("lng", lng);
		editor.apply();
	}
	
	public int getGender(){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(_activity.getBaseContext());	
		return Integer.parseInt(SP.getString("gender", "1"));
	}
	
	public int getDistance(){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(_activity.getBaseContext());	
		return SP.getInt("distance", 50);
	}
	
	public boolean isLazyLoad(){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(_activity.getBaseContext());	
		return SP.getBoolean("pagination",false);
	}
	
	public boolean isAutoUpdate(){
		SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(_activity.getBaseContext());	
		return SP.getBoolean("update",false);
	}
	
}
