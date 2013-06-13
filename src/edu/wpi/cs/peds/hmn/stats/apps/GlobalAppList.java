/**
 * Acts as a database of the apps installed on the phone, including storing the
 * list of apps currently displayed to the user. 
 */
package edu.wpi.cs.peds.hmn.stats.apps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import edu.wpi.cs.peds.hmn.app.SleekAndroidActivity;
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
	private List<Application> toSendApps;
	private AppState displayState;

	private PackageManager packageManager;
	private ActivityManager activityManager;

	// The list of system packages to exclude from the app list
	private final List<String> systemPackages = Arrays.asList(
			// "android", // Android System
			"com.android.backupconfirm",
			"com.android.sharedstoragebackup",
			"com.android.wallpaper.holospiral",
			"com.google.android.voicesearch",
			"com.google.android.inputmethod.latin", // Android Keyboard
			"com.android.providers.calendar", // Calendar Storage
			"com.google.android.deskclock", // Clock
			"com.android.providers.contacts", // Contacts Storage
			"com.android.providers.drm", // DRM Protected Content Storage
			"com.android.providers.downloads", // Download Manager
			"com.android.location.fused", // Fused Location
			"com.google.android.gallery3d", // Gallery
			"com.android.keychain", // Key Chain
			"com.android.launcher", // Launcher
			"com.android.providers.media", // Media Storage
			"com.android.musicfx", // MusicFX
			"com.android.defcontainer", // Package Access Helper
			"com.android.phasebeam", // Phase Beam
			"com.android.phone", // Phone
			"com.android.providers.telephony", // Phone/Messaging Storage
			"com.android.stk", // SIM Toolkit
			"com.android.providers.applications", // Search Applications
			"com.android.providers.settings", // Settings Storage
			"com.android.systemui", // System UI
			"com.android.providers.userdictionary", // User Dictionary
			"android", "com.android.certinstaller", "com.android.contacts",
			"com.android.dreams.basic", "com.android.facelock",
			"com.android.htmlviewer", "com.android.inputdevices",
			"com.android.mms", "com.android.packageinstaller",
			"com.android.vpndialogs", "com.google.android.backup",
			"com.google.android.configupdater", "com.google.android.exchange",
			"com.google.android.gsf", "com.google.android.gsf.login",
			"com.google.android.location",
			"com.google.android.onetimeinitializer",
			"com.google.android.partnersetup",
			"com.google.android.syncadapters.bookmarks",
			"com.google.android.tts", "com.tf.thinkdroid.sg");
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
		systemApps = findSystemApps();
		// Log.i(HmnLog.HMN_LOG_TAG, "INITIATE APPS!!!!!!!!!!!");
		if (!SleekAndroidActivity.hideSystemApp) {
			allApps.removeAll(systemApps);
		}
		// get to send app list

		// toSendApps = toSendApps();
//		toSendApps = getActive();
//		toSendApps = getRunningApps();
		toSendApps = getForegroundApps();
//		toSendApps = getBackgroundApps();
//		toSendApps = getCachedApps();
//		toSendApps = getNotRunningApps();

		displayedApps = allApps;
	}

	/**
	 * Finds systems apps according to the hard coded list.
	 * 
	 * @return a list of Application objects that represent the system apps to
	 *         be ignored.
	 */
	private List<Application> findSystemApps() {
		if (allApps == null)
			return null;

		List<Application> systemApps = new ArrayList<Application>();
		for (Application app : allApps)
			if (systemPackages.contains(app.packageName))
				systemApps.add(app);
		return systemApps;
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

	private List<Application> toSendApps() {
		List<Application> running = getRunningApps();
		return running;
	}

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

		if (!this.getActive().isEmpty())
			appStateMap.put(this.getActive().get(0), AppState.ACTIVE);

		for (Application app : this.getBackgroundApps())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.BACKGROUND);
		for (Application app : this.getCachedApps())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.CACHED);
		for (Application app : this.getForegroundApps())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.FOREGROUND);
		for (Application app : this.getRunningApps())
			if (!appStateMap.containsKey(app))
				appStateMap.put(app, AppState.RUNNING);
		for (Application app : this.allApps)
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
