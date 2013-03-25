package net.matmas.gpsautoresponder;

import net.matmas.gpsautoresponder.network.UpdateLocationTask;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class NetworkLocationListener implements LocationListener {

	@Override
	public void onLocationChanged(Location location) {
		new UpdateLocationTask(location).execute();
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
