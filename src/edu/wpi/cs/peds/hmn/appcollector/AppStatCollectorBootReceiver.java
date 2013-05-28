/**
 * Receiver responsible for starting the service on boot. 
 */
package edu.wpi.cs.peds.hmn.appcollector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Starts the AppCOllectorService upon receipt of a broadcast.
 * 
 * @author Richard Brown, rpb111@wpi.edu
 * 
 */
public class AppStatCollectorBootReceiver extends BroadcastReceiver {
	/**
	 * Invoked when the action android.intent.action.BOOT_COMPLETED is received.
	 * This behavior is defined in the Manifest. When this function is called,
	 * it boots the AppCollectorService.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
				&& AppCollectorService.startOnBoot) {
			Intent deviceBootIntent = new Intent(context,
					AppCollectorService.class);
			deviceBootIntent.putExtra(
					"edu.wpi.cs.peds.hmn.appstatviewer.action.devicebooted",
					true);
			context.startService(deviceBootIntent);
		}
	}
}
