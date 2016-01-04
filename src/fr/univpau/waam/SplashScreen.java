package fr.univpau.waam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


public class SplashScreen extends Activity implements Runnable {
	
	private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_splash);
        
        new Handler().postDelayed(this, SPLASH_TIME_OUT);
    }

	@Override
	public void run() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
	}
}
