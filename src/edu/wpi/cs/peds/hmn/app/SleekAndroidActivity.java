/**
 * The entry point for users to view all the data collected by the App
 * Collector service. 
 */
package edu.wpi.cs.peds.hmn.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import edu.wpi.cs.peds.hmn.appcollector.AppCollectorService;
import edu.wpi.cs.peds.hmn.appcollector.AppState;
import edu.wpi.cs.peds.hmn.appstatviewer.AppStatViewerActivity;
import edu.wpi.cs.peds.hmn.settings.NetworkReceiver;
import edu.wpi.cs.peds.hmn.settings.SettingsActivity;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

/**
 * The main Activity of the application.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu
 */
public class SleekAndroidActivity extends Activity {
	private Button allAppsButton;
	private Button activeButton;
	private Button runningAppsButton;
	private Button foregroundAppsButton;
	private Button backgroundAppsButton;
	private Button cachedAppsButton;
	private Button notRunningAppsButton;

	private Intent appCollector;

	// Whether there is a Wi-Fi connection.
	public static boolean wifiConnected = false;
	// Whether there is a mobile connection.
	// private static boolean mobileConnected = false;
	// Whether the display should be refreshed.
	public static boolean refreshDisplay = false;
	// Whether hide the system apps
	public static boolean hideSystemApp = true;
	// The user's current network preference setting.
	public static String sPref = null;

	// The BroadcastReceiver that tracks network connectivity changes.
	private NetworkReceiver receiver = new NetworkReceiver();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Register BroadcastReceiver to track connection changes.
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver();
		this.registerReceiver(receiver, filter);
	}

	public void onStart() {
		super.onStart();
		setContentView(R.layout.activity_app_class);
		// Log.i(HmnLog.HMN_LOG_TAG,"GET STARTED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		updateConnectedFlags();
		updateSettingStatus();

//		Log.i(HmnLog.HMN_LOG_TAG, "Display!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
//				+ refreshDisplay);
		if (refreshDisplay)
			stopService(appCollector);
		refreshDisplay = false;
		initButtons();
		appCollector = new Intent(this, AppCollectorService.class);
		startService(appCollector);
	}

	/**
	 * Wires up each button, connecting the object with its XML representation
	 * and the associated app state.
	 */
	private void initButtons() {
		initButton(allAppsButton, R.id.allButton, null);
		initButton(activeButton, R.id.activeButton, AppState.ACTIVE);
		initButton(runningAppsButton, R.id.runningButton, AppState.RUNNING);
		initButton(foregroundAppsButton, R.id.foregroundButton,
				AppState.FOREGROUND);
		initButton(backgroundAppsButton, R.id.backgroundButton,
				AppState.BACKGROUND);
		initButton(cachedAppsButton, R.id.cachedButton, AppState.CACHED);
		initButton(notRunningAppsButton, R.id.notRunningButton,
				AppState.NOTRUNNING);
	}

	/**
	 * Wires up a single button such that a click sends the user to a new screen
	 * displaying a list of apps corresponding to the associated app state.
	 * 
	 * @param button
	 *            the button object to wire up
	 * @param buttonID
	 *            the ID in the layout XML file
	 * @param appState
	 *            the app state associated with the button
	 */
	private void initButton(Button button, int buttonID, final AppState appState) {
		button = (Button) findViewById(buttonID);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				GlobalAppList.getInstance().setDisplayState(appState);
				Intent appStatViewerIntent = new Intent(
						SleekAndroidActivity.this, AppStatViewerActivity.class);
				startActivity(appStatViewerIntent);
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_settings, menu);
		return true;
	}

	// Handles the user's menu selection.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			Intent settingsActivity = new Intent(getBaseContext(),
					SettingsActivity.class);
			startActivity(settingsActivity);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void updateSettingStatus() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences sharedPrefsHide = PreferenceManager
				.getDefaultSharedPreferences(this);
		sPref = sharedPrefs.getString("listPref", "Wi-Fi");
		hideSystemApp = sharedPrefsHide.getBoolean("showSysPref", false);
	}

	private void updateConnectedFlags() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
			// mobileConnected = activeInfo.getType() ==
			// ConnectivityManager.TYPE_MOBILE;
		} else {
			wifiConnected = false;
			// mobileConnected = false;
		}
	}

	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// if (!AppCollectorService.startOnBoot)
		stopService(appCollector);

		if (receiver != null) {
			this.unregisterReceiver(receiver);
		}
	}
}