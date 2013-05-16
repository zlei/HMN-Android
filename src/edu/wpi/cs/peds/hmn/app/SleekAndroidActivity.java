/**
 * The entry point for users to view all the data collected by the App
 * Collector service. 
 */
package edu.wpi.cs.peds.hmn.app;

import edu.wpi.cs.peds.hmn.appcollector.AppState;
import edu.wpi.cs.peds.hmn.appcollector.AppCollectorService;
import edu.wpi.cs.peds.hmn.appstatviewer.AppStatViewerActivity;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The main Activity of the application.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * 
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
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_class);
		initButtons();
		appCollector = new Intent(this, AppCollectorService.class);
		startService(appCollector);
	}
	
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
	
	@Override
	protected void onDestroy()
	{
		if (!AppCollectorService.startOnBoot)
			stopService(appCollector);
		
		super.onDestroy();
	}
}