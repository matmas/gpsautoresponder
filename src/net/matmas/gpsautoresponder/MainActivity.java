package net.matmas.gpsautoresponder;

import net.matmas.gpsautoresponder.network.Callback;
import net.matmas.gpsautoresponder.network.RegisterTask;
import net.matmas.gpsautoresponder.network.UnregisterTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        App.mainActivity = this;
        
        final Button registerButton = (Button) findViewById(R.id.register_button);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress_indicator);
        final Button revokeButton = (Button) findViewById(R.id.revoke_button);
        final Button shareButton = (Button) findViewById(R.id.share_button);
        final RelativeLayout results = (RelativeLayout)findViewById(R.id.results);
        
        
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	registerButton.setVisibility(View.GONE);
            	progress.setVisibility(View.VISIBLE);
            	register();
            }
        });
        
        revokeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(MainActivity.this)
	            	.setIcon(android.R.drawable.ic_dialog_alert)
	            	.setTitle(getResources().getString(R.string.revoke_link))
	            	.setMessage(getResources().getString(R.string.are_you_sure_revoke_link))
	            	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            		public void onClick(DialogInterface dialog, int which) {
	            			results.setVisibility(View.GONE);
	            			progress.setVisibility(View.VISIBLE);
	            			
	            			new UnregisterTask(new Callback() {
								public void run() {
									Data.deleteTokenAndPassword(); // delete it
			        	        	refreshUIState();
								}
							}, new Callback() {
								public void run() {
									results.setVisibility(View.VISIBLE);
									progress.setVisibility(View.GONE);
								}
							}).execute(); // using old token
	            		}
	        		})
	        		.setNegativeButton("No", null)
	        		.show();
			}
		});
        
        shareButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent sendIntent = new Intent();
	        	sendIntent.setAction(Intent.ACTION_SEND);
	        	sendIntent.putExtra(Intent.EXTRA_TEXT, getLink());
	        	sendIntent.setType("text/plain");
	        	startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        	}
        });
        
        refreshUIState();
    }
    
    private void refreshUIState() {
    	final Button registerButton = (Button) findViewById(R.id.register_button);
		final ProgressBar progressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);
		final TextView output = (TextView)findViewById(R.id.output);
		final RelativeLayout results = (RelativeLayout)findViewById(R.id.results);
		
    	if (Data.getToken() != null && Data.getPassword() != null) {
    		registerButton.setVisibility(View.GONE);
    		progressIndicator.setVisibility(View.GONE);
    		results.setVisibility(View.VISIBLE);
        	
        	output.setText(
	        		getResources().getText(R.string.this_is_the_link) + "\n\n"
	        		+ getLink() + "\n\n"
	        		+ getResources().getText(R.string.you_will_be_notified)
	        	);
    	}
    	else {
    		registerButton.setVisibility(View.VISIBLE);
    		progressIndicator.setVisibility(View.GONE);
        	results.setVisibility(View.GONE);
    	}
    }
    
    public void register() {
    	GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
        	GCMRegistrar.register(this, App.GCM_SENDER_ID);
        } else {
        	Log.d(this.getClass().toString(), "Already registered");
        	new RegisterTask(regId, new Callback() {
				public void run() {
					new Handler(Looper.getMainLooper()).post(new Runnable(){
			    		public void run() {
			    			refreshUIState();
			    		}
			    	});
				}
			}, new Callback() {
				public void run() {
					refreshUIState();
				}
			}).execute();
        }
    }
    
    @Override
    protected void onDestroy() {
    	App.mainActivity = null;
    	super.onDestroy();
    }
    
    public String getLink() {
    	String url = App.BASE_URL + "/view/" + Data.getToken();
    	return url;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }
    
}
