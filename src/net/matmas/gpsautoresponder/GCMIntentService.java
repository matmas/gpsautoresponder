package net.matmas.gpsautoresponder;

import net.matmas.gpsautoresponder.network.Callback;
import net.matmas.gpsautoresponder.network.PingTask;
import net.matmas.gpsautoresponder.network.RegisterTask;
import net.matmas.gpsautoresponder.network.UnregisterTask;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	
	public GCMIntentService() {
		super(App.GCM_SENDER_ID);
	}
	
	@Override
	protected void onError(Context context, String errorId) {
		System.out.println("error! " + errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String command = intent.getStringExtra("command"); 
		if (command.equals("update") || command.equals("wake")) {
			String title = "GPS Auto Responder";
			String message = "Someone is viewing your location";
			Intent notificationIntent = new Intent(this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Resources res = context.getResources();
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setContentIntent(contentIntent)
			            .setSmallIcon(R.drawable.ic_action_search)
			            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
			            .setTicker(message)
			            .setWhen(System.currentTimeMillis())
			            .setAutoCancel(true)
			            .setContentTitle(title)
			            .setContentText(message);
			Notification notification = builder.build();
			final int notificationId = 1;
			notificationManager.notify(notificationId, notification);
			
			new Handler(Looper.getMainLooper()).post(new Runnable(){
				public void run() {
					LocationManager locationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, App.gpsLocationListener);
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, App.networkLocationListener);
					Log.d(this.getClass().toString(), "Turning on both location listeners");
					
					App.activeWakes++;
					Log.d(this.getClass().toString(), "Active wakes = " + App.activeWakes);
					
					new Handler().postDelayed(new Runnable() {
						public void run() {
							App.activeWakes--;
							Log.d(this.getClass().toString(), "Active wakes = " + App.activeWakes);
							
							if (App.activeWakes <= 0) {
								LocationManager locationManager = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
								Log.d(this.getClass().toString(), "Turning off both location listeners");
						    	locationManager.removeUpdates(App.gpsLocationListener);
						    	locationManager.removeUpdates(App.networkLocationListener);
							}
						}
					}, 60 * 1000);
				}
			});	
		}
		else if (command.equals("ping")) {
			new PingTask().execute();
		}
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		new RegisterTask(regId, new Callback() {
			public void run() {
				if (App.mainActivity != null) {
					App.mainActivity.register(); // try again with GCM ready
				}
			}
		}).execute();
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		new UnregisterTask().execute();
	}
	
	
}
