/**
 * The entry point for users to view all the data collected by the App
 * Collector service. 
 */
package edu.wpi.cs.peds.hmn.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
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
import android.widget.Toast;
import edu.wpi.cs.peds.hmn.appcollector.AppCollectorService;
import edu.wpi.cs.peds.hmn.appcollector.AppState;
import edu.wpi.cs.peds.hmn.appstatviewer.AppStatViewerActivity;
import edu.wpi.cs.peds.hmn.settings.SettingsActivity;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

/**
 * The main Activity of the application.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu 
 */
public class SleekAndroidActivity extends Activity
{
	private Button allAppsButton;
	private Button activeButton;
	private Button runningAppsButton;
	private Button foregroundAppsButton;
	private Button backgroundAppsButton;
	private Button cachedAppsButton;
	private Button notRunningAppsButton;
	
	private Intent appCollector;
	
	public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";

    // Whether there is a Wi-Fi connection.
    public static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    //private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;

    // The user's current network preference setting.
    public static String sPref = null;

    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkReceiver receiver = new NetworkReceiver();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        // Register BroadcastReceiver to track connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
		setContentView(R.layout.activity_app_class);
		initButtons();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");
        updateConnectedFlags();
		appCollector = new Intent(this, AppCollectorService.class);
		startService(appCollector);
	}

/*	public void onStart() {
        super.onStart();
        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");

        updateConnectedFlags();
        // Only loads the page if refreshDisplay is true. Otherwise, keeps previous
        // display. For example, if the user has set "Wi-Fi only" in prefs and the
        // device loses its Wi-Fi connection midway through the user using the app,
        // you don't want to refresh the display--this would force the display of
        // an error page instead of stackoverflow.com content.
//        if (refreshDisplay) {
 //       	loadPage();
  //      }
    }	
*/
	/**
	 * Wires up each button, connecting the object with its XML representation
	 * and the associated app state.
	 */
	private void initButtons()
	{
		initButton(allAppsButton,R.id.allButton,null);
		initButton(activeButton,R.id.activeButton,AppState.ACTIVE);
		initButton(runningAppsButton,R.id.runningButton,AppState.RUNNING);
		initButton(foregroundAppsButton,R.id.foregroundButton,AppState.FOREGROUND);
		initButton(backgroundAppsButton,R.id.backgroundButton,AppState.BACKGROUND);
		initButton(cachedAppsButton,R.id.cachedButton,AppState.CACHED);
		initButton(notRunningAppsButton,R.id.notRunningButton,AppState.NOTRUNNING);
	}
	
	/**
	 * Wires up a single button such that a click sends the user to a new screen
	 * displaying a list of apps corresponding to the associated app state.
	 * 
	 * @param button the button object to wire up
	 * @param buttonID the ID in the layout XML file
	 * @param appState the app state associated with the button
	 */
	private void initButton(Button button, int buttonID, final AppState appState)
	{
		button = (Button) findViewById(buttonID);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				GlobalAppList.getInstance().setDisplayState(appState);
				
				Intent appStatViewerIntent = new Intent(SleekAndroidActivity.this, AppStatViewerActivity.class);
				startActivity(appStatViewerIntent);
			}
		});
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_app_class, menu);
        return true;
    }

    // Handles the user's menu selection.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.settings:
                Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }
 
    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            //mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            //mobileConnected = false;
        }
    }

//    private void loadPage() {
 //       if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
  //              || ((sPref.equals(WIFI)) && (wifiConnected))) {
   //     	JSONSender.loadAndSendBuffer(this);
    //    } 
   // }
    @Override
	protected void onDestroy()
	{
		if (!AppCollectorService.startOnBoot)
			stopService(appCollector);
		
		super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
	}
        
    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // Checks the user prefs and the network connection. Based on the result, decides
            // whether
            // to refresh the display or keep the current display.
            // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
            if (WIFI.equals(sPref) && networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // If device has its Wi-Fi connection, sets refreshDisplay
                // to true. This causes the display to be refreshed when the user
                // returns to the app.
                refreshDisplay = true;
                Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();

                // If the setting is ANY network and there is a network connection
                // (which by process of elimination would be mobile), sets refreshDisplay to true.
            } else if (ANY.equals(sPref) && networkInfo != null) {
                refreshDisplay = true;

                // Otherwise, the app can't download content--either because there is no network
                // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
                // is no Wi-Fi connection.
                // Sets refreshDisplay to false.
            } else {
                refreshDisplay = false;
                Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }
}