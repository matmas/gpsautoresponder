package net.matmas.gpsautoresponder;

import net.matmas.gpsautoresponder.network.PingTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		if (intent.getExtras() != null) {
			@SuppressWarnings("deprecation")
			NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
				SharedPreferences settings = App.getContext().getSharedPreferences("Ping", 0);
				Long lastPinged = settings.getLong("LAST_PINGED", 0);
				
				Long now = System.currentTimeMillis();
				Long oneWeek = 1000L * 60 * 60 * 24 * 7;
				
				if (now - lastPinged > oneWeek) {
					new PingTask().execute();
					
					SharedPreferences.Editor editor = settings.edit();
					editor.putLong("LAST_PINGED", now);
					editor.commit();
				}
			}
		}
	}
}
