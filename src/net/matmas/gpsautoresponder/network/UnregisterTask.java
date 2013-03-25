package net.matmas.gpsautoresponder.network;

import java.io.PrintWriter;

import net.matmas.gpsautoresponder.App;
import net.matmas.gpsautoresponder.Data;

public class UnregisterTask extends NetworkTask {
	
	public UnregisterTask() {
		this(null, null);
	}
	
	public UnregisterTask(Callback onSuccessCallback, Callback onFailureCallback) {
		this.URL = App.BASE_URL + "/unregister";
		this.onSuccessCallback = onSuccessCallback;
		this.onFailureCallback = onFailureCallback;
	}
	
	@Override
	protected void send(PrintWriter out) {
		out.print("id=");
		out.print(Data.getToken());
		
		out.print("&password=");
		out.print(Data.getPassword());
	}
}
