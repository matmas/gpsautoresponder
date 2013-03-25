package net.matmas.gpsautoresponder.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import net.matmas.gpsautoresponder.App;

import android.os.AsyncTask;
import android.widget.Toast;

public class NetworkTask extends AsyncTask<Void, Void, String> {
	
	private Exception exception = null;
	protected Callback onSuccessCallback = null;
	protected Callback onFailureCallback = null;
	
	protected String doInBackground(Void... none) {
		URL url;
		HttpURLConnection urlConnection = null;
		String response = "";
		try {
			url = new URL(URL);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			PrintWriter out = new PrintWriter(new BufferedOutputStream(urlConnection.getOutputStream()));
			send(out);
			out.close();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
    		response = Tools.convertStreamToString(in);
		} catch (ProtocolException e) {
			exception = e;
		} catch (MalformedURLException e) {
			exception = e;
		} catch (IOException e) {
			exception = e;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return response;
	}
	
	protected String URL; // fill this in constructor
	
	protected void send(PrintWriter out) {
		// override this to send parameters
	}
	
	protected boolean isSilent() {
		return false; // override this if needed
	}
	
	protected void onPostExecute(String response) {
		if (exception != null) {
			if ( !isSilent()) {
				Toast.makeText(App.getContext(), exception.toString(), Toast.LENGTH_LONG).show();
			}
			onFailure();
			return;
		}
//		if (response != "") {
//			if ( !isSilent()) {
//				Toast.makeText(App.getContext(), response, Toast.LENGTH_LONG).show();
//			}
//			onFailure();
//			return;
//		}
		onSuccess(response);
	}
	
	protected void onFailure() {
		if (this.onFailureCallback != null) {
			this.onFailureCallback.run();
		}
		// may be extended
	}
	
	protected void onSuccess(String response) {
		if (this.onSuccessCallback != null) {
			this.onSuccessCallback.run();
		}
		// may be extended
	}
}
