package fr.univpau.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import fr.univpau.waam.MainActivity;

public class MyLocationListener implements LocationListener {
	
	private MainActivity 			_context;
	
	public MyLocationListener(MainActivity context) {
		_context = context;
	}
	
	@Override  
    public void onLocationChanged(Location loc) {
		_context.onLocationChanged(loc);
    }

    @Override  
    public void onProviderDisabled(String provider) {
    }

    @Override  
    public void onProviderEnabled(String provider) {
    }

    @Override  
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}  