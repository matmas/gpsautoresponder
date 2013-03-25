package net.matmas.gpsautoresponder;

import net.matmas.gpsautoresponder.network.UpdateLocationTask;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSLocationListener implements LocationListener {

	@Override
	public void onLocationChanged(Location location) {
		new UpdateLocationTask(location).execute();
		
		Log.d(this.getClass().toString(), "Turning off networkLocationListener because GPS location is received");
		LocationManager locationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
    	locationManager.removeUpdates(App.networkLocationListener);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// do nothing
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// do nothing
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle bundle) {
		// do nothing
	}

}
