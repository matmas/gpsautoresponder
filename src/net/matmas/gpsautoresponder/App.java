package net.matmas.gpsautoresponder;

import android.content.Context;

public class App extends android.app.Application {
	
	private static Context context;
	
	public void onCreate() {
		super.onCreate();
		App.context = getApplicationContext();
	}
	
	public static Context getContext() {
		return App.context;
	}
	
	//-------------------------------------------
	
	public static GPSLocationListener gpsLocationListener = new GPSLocationListener();
	public static NetworkLocationListener networkLocationListener = new NetworkLocationListener();
	public static int activeWakes = 0;
	
	public static MainActivity mainActivity;
	
	public static final String BASE_URL = "https://gpsautoresponder.appspot.com";
//	public static final String BASE_URL = "http://10.0.1.2:8080";
	
	public static final String GCM_SENDER_ID = "927909513685"; // https://code.google.com/apis/console/#project:927909513685
}