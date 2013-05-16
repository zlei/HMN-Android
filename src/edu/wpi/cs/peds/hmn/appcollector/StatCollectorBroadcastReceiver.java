/**
 * A handler to capture events fired by the Android device, specifically a
 * change in screen state (turned on/off) or network connection, and device
 * shutdown.
 */
package edu.wpi.cs.peds.hmn.appcollector;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import edu.wpi.cs.peds.hmn.log.HmnLog;

/**
 * @author Richard Brown, rpb111@wpi.edu
 * 
 */
public class StatCollectorBroadcastReceiver extends BroadcastReceiver {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intentToSendService = new Intent(context, AppCollectorService.class);
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_SCREEN_ON)) {
			Log.i(HmnLog.HMN_LOG_TAG, "Screen turned on.");
			// Collect Data Usage
			intentToSendService.putExtra("action", action);
			context.startService(intentToSendService);

		} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
			long time = new Date().getTime();
			Log.i(HmnLog.HMN_LOG_TAG, "Screen turned off (" + time + " ).");
			// Collect Data Usage
			intentToSendService.putExtra("action", action);
			context.startService(intentToSendService);

		} else if (action.equals(Intent.ACTION_SHUTDOWN)) {
			Log.i(HmnLog.HMN_LOG_TAG, "Prepping for shutdown.");
			// TODO persist
			context.stopService(intentToSendService);

		} else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			Log.i(HmnLog.HMN_LOG_TAG, "Change in network.");
			// Collect Data Usage
			intentToSendService.putExtra("action", action);
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			intentToSendService.putExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO,connectivityManager.getActiveNetworkInfo());
			context.startService(intentToSendService);
		}

	}
}
