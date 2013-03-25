package net.matmas.gpsautoresponder.network;

import java.io.PrintWriter;

import net.matmas.gpsautoresponder.App;
import net.matmas.gpsautoresponder.Data;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.widget.Toast;

public class RegisterTask extends NetworkTask {
	
	private String gcmRegId;
	
	public RegisterTask(String gcmRegId, Callback onSuccessCallback) {
		this(gcmRegId, onSuccessCallback, null);
	}
	
	public RegisterTask(String gcmRegId, Callback onSuccessCallback, Callback onFailureCallback) {
		this.URL = App.BASE_URL + "/register";
		this.gcmRegId = gcmRegId;
		this.onSuccessCallback = onSuccessCallback;
		this.onFailureCallback = onFailureCallback;
	}
	
	@Override
	protected void send(PrintWriter out) {
		String gcm_reg_id = this.gcmRegId;
		out.print("gcm_reg_id=");
		out.print(gcm_reg_id);
	}
	
	@Override
	protected void onSuccess(String response) {
		try {
			JSONObject responseObject = (JSONObject) new JSONTokener(response).nextValue();
			String token = responseObject.getString("token");
			String password = responseObject.getString("password");
			
			Data.setToken(token);
			Data.setPassword(password);
			
		} catch (JSONException e) {
			Toast.makeText(App.getContext(), response, Toast.LENGTH_LONG).show();
		}
		
		super.onSuccess(response);
	}
}
