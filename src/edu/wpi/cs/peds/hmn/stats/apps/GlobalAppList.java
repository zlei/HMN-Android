/**
 * Acts as a database of the apps installed on the phone, including storing the
 * list of apps currently displayed to the user. 
 */
package edu.wpi.cs.peds.hmn.stats.apps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import edu.wpi.cs.peds.hmn.app.HMNAndroidActivity;
import edu.wpi.cs.peds.hmn.appcollector.AppState;

/**
 * Manages the list of apps installed on the phone. Provides the master list of
 * all apps installed, with irrelevant system apps removed. Also allows
 * filtering the app list by state. In order to ensure only a single instance of
 * this manager exists, it is implemented as a singleton.
 * 
 * @author Zhenhao Lei, zlei@wpi.edu
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * 
 */
public class GlobalAppList {
	private static GlobalAppList globalAppList;

	private final List<Application> allApps;
	private List<Application> displayedApps;
	private final List<Application> toSendApps;
	private AppState displayState;

	private PackageManager packageManager;
	private ActivityManager activityManager;

	private static List<Application> systemApps;

	public static void init(PackageManager packageManager,
			ActivityManager activityManager) {
		globalAppList = new GlobalAppList(packageManager, activityManager);
	}

	public static GlobalAppList getInstance() {
		return globalAppList;
	}

	/**
	 * Initializes the list of installed apps by querying the device, then
	 * subtracts the hard coded system apps that are irrelevant.
	 * 
	 * @param packageManager
	 *            allows querying the phone for its app list
	 * @param activityManager
	 *            aids in determining app state
	 */
	private GlobalAppList(PackageManager packageManager,
			ActivityManager activityManager) {
		this.packageManager = packageManager;
		this.activityManager = activityManager;
		allApps = AppCollector.getAllApps(packageManager);
		systemApps = AppCollector.getSysApps(packageManager);
		if (!HMNAndroidActivity.hideSystemApp) {
			allApps.removeAll(systemApps);
		}
		// get to send app list, can choose which kind of apps to be sent

		toSendApps = getForegroundApps();
//		toSendApps = getRunningApps();
		displayedApps = allApps;
	}

	/**
	 * Sets the display state and updates the app list accordingly.
	 * 
	 * @param state
	 */
	public void setDisplayState(AppState state) {
		this.displayState = state;
		loadDisplayApps();
	}

	/**
	 * Retrieves the current display state
	 * 
	 * @return the display state
	 */
	public AppState getDisplayState() {
		return displayState;
	}

	/**
	 * Retrieves a list of all applications on on the phone, minus the
	 * hard-coded list of system apps.
	 * 
	 * @return a list of all application on the phone
	 */
	public List<Application> getAllApps() {
		return allApps;
	}

	/**
	 * Retireves a list of all applications to display to the user.
	 * 
	 * @return all applications the user should be shown
	 */
	public List<Application> getDisplayedApps() {
		return displayedApps;
	}

	/**
	 * Updates the display app list based on the global display app state.
	 */
	private void loadDisplayApps() {
		if (displayState == null)
			displayedApps = allApps;
		else {
			switch (displayState) {
			case ACTIVE:
				displayedApps = getActive();
				break;
			case RUNNING:
				displayedApps = getRunningApps();
				break;
			case FOREGROUND:
				displayedApps = getForegroundApps();
				break;
			case BACKGROUND:
				displayedApps = getBackgroundApps();
				break;
			case CACHED:
				displayedApps = getCachedApps();
				break;
			case NOTRUNNING:
				displayedApps = getNotRunningApps();
				break;
			default:
				displayedApps = allApps;
			}
		}
	}

	/**
	 * Retrieves the list of active applications. An active application is the
	 * application the user is currently viewing.
	 * 
	 * Note that this should only ever contain a single app.
	 * 
	 * @return the list of active apps
	 */
	private List<Application> getActive() {
		return filter(AppCollector.getActive(packageManager, activityManager));
	}

	/**
	 * Retrieves the list of running applications.
	 * 
	 * @return the list of running apps
	 */
	private List<Application> getRunningApps() {
		return filter(AppCollector.getRunningApps(packageManager,
				activityManager));
	}

	/**
	 * Retrieves the list of foreground applications. A foreground application
	 * is one that is minimized, but not active.
	 * 
	 * @return the list of foreground apps
	 */
	private List<Application> getForegroundApps() {
		return filter(AppCollector.getForegroundApps(packageManager,
				activityManager));
	}

	/**
	 * Retrieves the list of background applications.
	 * 
	 * @return the list of bbackground apps
	 */
	private List<Application> getBackgroundApps() {
		return filter(AppCollector.getBackgroundApps(packageManager,
				activityManager));
	}

	/**
	 * Retrieves the list of cached applications. A cached application is not
	 * running, but can still receive signals.
	 * 
	 * @return the list of cached apps
	 */
	private List<Application> getCachedApps() {
		return filter(AppCollector.getCachedApps(packageManager,
				activityManager));
	}

	/**
	 * Retrieves the list of cached applications.
	 * 
	 * @return
	 */
	private List<Application> getNotRunningApps() {
		List<Application> running = getRunningApps();
		List<Application> notRunning = new ArrayList<Application>(allApps);
		notRunning.removeAll(running);
		return notRunning;
	}

	/*
	 * private List<Application> toSendApps() { List<Application> running =
	 * getRunningApps(); return running; }
	 */
	/**
	 * Makes a copy of the given app list, but grabs each Application reference
	 * from the global app list. In this way, any modification to an Application
	 * object will be properly reflected in the global list, and can thus be
	 * seen by the entire application.
	 * 
	 * @param apps
	 *            a list of applications to return
	 * @return a copy of apps, but with each Application object replaced by the
	 *         corresponding one from the global app list.
	 */
	private List<Application> filter(List<Application> apps) {
		List<Application> filteredApps = new ArrayList<Application>();
		for (Application app : apps)
			if (allApps.contains(app))
				filteredApps.add(allApps.get(allApps.indexOf(app)));
		return filteredApps;
	}

	/**
	 * Finds an app in the master app list based on its UID, which Android
	 * guarantees to be unique.
	 * 
	 * @param uid
	 *            the UID of the app to be retrieved
	 * @return an Application from the master app list
	 */
	public Application getApp(int uid) {
		for (Application app : allApps)
			if (app.uid == uid)
				return app;
		return null;
	}

	/**
	 * Maps each app to its current state on the phone.
	 * 
	 * This function is computationally expensive, so ONLY use it if you need
	 * the most up to date app state.
	 * 
	 * @return each key is an Application and each value is its an AppState
	 */
	public Map<Application, AppState> getAppStateMap() {
		Map<Application, AppState> appStateMap = new HashMap<Application, AppState>();


		for (Application app : this.getActive())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.ACTIVE);

		for (Application app : this.getForegroundApps())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.FOREGROUND);

		for (Application app : this.getBackgroundApps()) {
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.BACKGROUND);
		}

		for (Application app : this.getCachedApps())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.CACHED);

		for (Application app : this.getRunningApps())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.RUNNING);

		for (Application app : this.getNotRunningApps())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.NOTRUNNING);

		return appStateMap;
	}

	/**
	 * Determines the current state of the given app. This method should be used
	 * sparingly, as it is computationally expensive.
	 * 
	 * @param app
	 *            the app to query
	 * @return the state of the app in question
	 */
	public AppState getAppState(Application app) {
		if (getRunningApps().contains(app))
			return AppState.RUNNING;
		if (getBackgroundApps().contains(app))
			return AppState.BACKGROUND;
		if (getCachedApps().contains(app))
			return AppState.CACHED;
		if (getForegroundApps().contains(app))
			return AppState.FOREGROUND;
		if (getActive().equals(app))
			return AppState.ACTIVE;

		return AppState.NOTRUNNING;
	}

	/**
	 * Clears the stats of all apps. Should be invoked after a successful data
	 * transmission.
	 */
	public void clearStats() {
		clearStats(allApps);
	}

	/**
	 * Clears the stats of the each app in the provided appList.
	 * 
	 * @param appList
	 *            the list of whose stats to clear
	 */
	public void clearStats(List<Application> appList) {
		for (Application app : appList)
			app.resetStats();
	}

	public JSONArray toJSON() throws JSONException {
		return toJSON(toSendApps);
	}

	public JSONArray toJSON(List<Application> appList) throws JSONException {
		JSONArray json = new JSONArray();
		for (Application app : appList)
			json.put(app.toJSON());
		return json;
	}
}
