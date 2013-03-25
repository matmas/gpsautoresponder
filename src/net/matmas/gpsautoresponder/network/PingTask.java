package net.matmas.gpsautoresponder.network;

import java.io.PrintWriter;

import net.matmas.gpsautoresponder.App;
import net.matmas.gpsautoresponder.Data;

public class PingTask extends NetworkTask  {

	public PingTask() {
		this.URL = App.BASE_URL + "/ping";
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
	}
}
