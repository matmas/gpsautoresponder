package net.matmas.gpsautoresponder.network;

import java.io.PrintWriter;

import net.matmas.gpsautoresponder.App;
import net.matmas.gpsautoresponder.Data;

import android.location.Location;

public class UpdateLocationTask extends NetworkTask {
	
	private Location location;
	
	public UpdateLocationTask(Location location) {
		this.URL = App.BASE_URL + "/update";
		this.location = location;
	}
	
	@Override
	protected boolean isSilent() {
		return true;
	}
	
	@Override
	protected void send(PrintWriter out) {
		out.print("&id=");
		out.print(Data.getToken());
		
		out.print("&password=");
		out.print(Data.getPassword());
		
		out.print("&latitude=");
		out.print(this.location.getLatitude());
		
		out.print("&longitude=");
		out.print(this.location.getLongitude());
		
		out.print("&accuracy=");
		out.print(this.location.getAccuracy());
		
		out.print("&altitude=");
		out.print(this.location.getAltitude());
		
		out.print("&time=");
		out.print(this.location.getTime());
		
		out.print("&speed=");
		out.print(this.location.getSpeed());
		
		out.print("&bearing=");
		out.print(this.location.getBearing());
		
		out.print("&provider=");
		out.print(this.location.getProvider());
	}
}
