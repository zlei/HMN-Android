/**
 * Class which holds information for identifying an application.
 */
package edu.wpi.cs.peds.hmn.stats.apps;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import edu.wpi.cs.peds.hmn.appcollector.AppCollectorService;
import edu.wpi.cs.peds.hmn.appcollector.AppState;
import edu.wpi.cs.peds.hmn.appcollector.NetDevice;
import edu.wpi.cs.peds.hmn.stats.JSONParser;
import edu.wpi.cs.peds.hmn.stats.PowerStats;
import edu.wpi.cs.peds.hmn.stats.costbenefit.StateChanges;
import edu.wpi.cs.peds.hmn.stats.net.NetUsageEntry;
import edu.wpi.cs.peds.hmn.stats.net.NetUsageList;
import edu.wpi.cs.peds.hmn.stats.net.NetworkStats;

/**
 * A container class for all information collected about an application. It also
 * is responsible for directing further data collection, such as network usage
 * and state information.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu
 */
public class Application implements Serializable {
	/**
	 * Autogenerated VersionID
	 */
	private static final long serialVersionUID = 2965841186830803626L;

	public String name;
	public Drawable icon;
	public String packageName;
	public String version;
	public Integer uid;
	public long installedTime;
	public long updatedTime;
	public int isSystem;
	public Integer cost = 0;
	public Integer benefit = 0;

	public NetUsageList netUsage;
	public NetUsageList netUsageArray;
	public NetworkStats cumulativeStats;
	public PowerStats powerStats;
	public NetDevice lastConnectionType;
	public AppStateMap appStateMap;
	public AppState currentState = null;
	public StateChanges stateChanges;
	public JSONParser jsonParser;
	private JSONArray statesArray;
	private JSONArray netsArray;

	public Float userRating = 0.0f;
	public Float dbRating = 0.0f;
	public Float totalNetwork = 0.0f;
	private long lastStateUpdateTime;

	public Application(String name, String packageName, int uid,
			String version, Drawable icon, long installedTime, long updatedTime, int isSystem) {
		this.name = name;
		this.packageName = packageName;
		this.uid = uid;
		this.version = version;
		this.icon = icon;
		this.installedTime = installedTime;
		this.updatedTime = updatedTime;
		this.isSystem = isSystem;
		netUsage = new NetUsageList();
		cumulativeStats = new NetworkStats();
		powerStats = new PowerStats();
		appStateMap = new AppStateMap();
		stateChanges = new StateChanges();
		statesArray = new JSONArray();
		netsArray = new JSONArray();
		jsonParser = new JSONParser();
		lastStateUpdateTime = new Date().getTime();

		cost = (int) (Math.random() * 100);
		// benefit = (int) (Math.random() * 100);
	}

	/**
	 * Tells the app it needs to update its stats, as well as informs the app of
	 * its current state.
	 * 
	 * @param context
	 *            used for accessing system functions
	 * @param appState
	 *            the app's current state
	 */
	public void updateStats(Context context, AppState appState) {
		updateState(appState);
		updateNetworkUsage(context);
		updateCostBenefit(context);
		updatePowerStats(context);
	}

	/**
	 * Updates the app's current state. In doing so, it also updates the app's
	 * total time spent in each state.
	 * 
	 * @param appState
	 *            the app's current state
	 */
	private void updateState(AppState appState) {
		long currentTime = new Date().getTime();
		if (currentState == null) {
			currentState = appState;
			lastStateUpdateTime = currentTime;
		} else {
			long stateDuration = currentTime - lastStateUpdateTime;

			// Put this new entry in the state change list, which is currently
			// only used for cost/benefit calculation
			stateChanges.addChange(currentTime, appState, stateDuration);
			long totalStateDuration = appStateMap.get(currentState);
			appStateMap.put(currentState, totalStateDuration + stateDuration);
			appStateMap.setTimestamp(AppCollectorService.timestamp);
			try {
				updateStatesPerApp();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			currentState = appState == null ? AppState.NOTRUNNING : appState;
			lastStateUpdateTime = currentTime;
		}
	}

	/**
	 * Updates the app's network usage totals, including the total data sent and
	 * received across each connection type.
	 * 
	 * @param context
	 *            used to access system functions
	 */
	private void updateNetworkUsage(Context context) {
		NetworkStats totalStats = NetworkStats.getTotalNetworkUsage(uid);
		NetworkStats newStats = totalStats.difference(cumulativeStats);
		newStats.state = currentState;
		cumulativeStats = totalStats;
		NetUsageEntry newEntry = new NetUsageEntry(lastConnectionType, newStats);
		netUsageArray = new NetUsageList();
		newEntry.setTimestamp(AppCollectorService.timestamp);
		netUsage.add(newEntry);
		netUsageArray.add(newEntry);
		try {
			updateNetPerApp();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		lastConnectionType = NetDevice
				.determineNetworkConnection(activeNetwork);
	}

	/**
	 * Updates the app's user rating. This should only be called when the user
	 * changes their rating of the app.
	 * 
	 * @param newRating
	 *            the new user rating for this app
	 */
	public void updateRating(Float newRating) {
		this.userRating = newRating;
	}

	public void updatePowerStats(Context context) {

	}

	/**
	 * Tells the app to update its cost/benefit calculation. At the moment, this
	 * method does nothing. The cost and benefit are randomly determined in the
	 * constructor.
	 * 
	 * @param context
	 *            for accessing system functions
	 */
	private void updateCostBenefit(Context context) {
		/*
		 * benefit = CostBenefit.calcBenefit(context); cost =
		 * CostBenefit.calcCost(context);
		 */
		// Using the network usage percentage for the moment, waiting for the
		// usage per app data from server
		// this.cost = (int) this.netUsage.networkMonitorInfo();
		// Using the overall rating for the moment
		// jsonParser.getFromServer(this);
		this.benefit = (int) (this.dbRating * 20);
	}

	/**
	 * Resets data collected by this app.
	 */
	public void resetStats() {
		netUsage = new NetUsageList();
		appStateMap = new AppStateMap();
		powerStats = new PowerStats();
		netsArray = new  JSONArray();
		statesArray = new JSONArray();
	}

	/**
	 * Returns the app's human readable name, if it exists. Otherwise, returns
	 * its package name.
	 * 
	 * @return the name of this app
	 */
	public String getName() {
		if (name == null)
			name = packageName;
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return name;
	}

	public Drawable getIcons() {
		return icon;
	}

	public float networkMonitorInfo() {
		return netUsage.networkMonitorInfo();
	}

	/*
	 * public JSONArray getNetUsage() { try { return netUsage.apptoJSON(); }
	 * catch (JSONException e) { e.printStackTrace(); } return null; }
	 */
	@Override
	public boolean equals(Object obj) {
		Application app = (Application) obj;
		return this.getName().equalsIgnoreCase(app.getName());
	}

	@Override
	public String toString() {
		return this.getName();
	}

	// show the general benefit data of an app, should be retrieved from
	// database
	public String allBenefitInfo() {
		StringBuilder appStr = new StringBuilder();
		appStr.append(String.format("Name:" + this.getName()));
		appStr.append(String.format("\nUID: " + this.uid));
		appStr.append(String.format("\nUser Rating: " + this.userRating));
		appStr.append(String.format("\nOverall Rating: " + this.dbRating));
		// appStr.append(String.format("Cost: ", cost));
		appStr.append(String.format("\nBenefit: " + this.benefit));

		return appStr.toString();
	}

	// show the general benefit data of an app, should be retrieved from
	// database
	public String allCostInfo() {
		StringBuilder appStr = new StringBuilder();
		appStr.append(String.format("Name:" + this.getName()));
		appStr.append(String.format("\nUID: " + this.uid));
		appStr.append(String.format("\nInstalled Time:" + this.installedTime));
		appStr.append(String.format("\nLast Updated Time:" + this.updatedTime));
		appStr.append(String.format("\nUser Rating: " + this.userRating));
		appStr.append(String.format("\nOverall Rating: " + this.dbRating));
		// appStr.append(String.format("Cost: ", cost));
		appStr.append(String.format("\nBenefit: " + this.benefit));

		return appStr.toString();
	}

	public String detailedInfo() {
		StringBuilder appStr = new StringBuilder();

		appStr.append(String.format("STATE INFO\n%s\n\n",
				appStateMap.detailedInfo()));
		appStr.append(String.format("NETWORK USAGE\n%s\n\n",
				netUsage.detailedInfo()));
		// appStr.append(String.format("Battery INFO\n%s\n\n",
		// powerStats.detailedInfo()));

		if (!netUsage.isEmpty())
			appStr.append(String.format("ENTRIES\n%s\n\n",
					netUsage.entryListDetails()));

		return appStr.toString();
	}

	// Current App cost information
	public String costInfo() {
		StringBuilder appCostStr = new StringBuilder();

		appCostStr.append(String.format("NETWORK USAGE: %s\n",
				netUsage.costInfo()));
		// appCostStr.append(String.format("Battery INFO: %s\n",
		// powerStats.costInfo()));
		return appCostStr.toString();
	}

	// total cost information of the device
	public String totalCostInfo() {
		StringBuilder appCostStr = new StringBuilder();

		appCostStr.append(String.format("NETWORK USAGE: %s\n",
				netUsage.totalCostInfo()));
		appCostStr.append(String.format("Battery INFO: %s\n",
				powerStats.costInfo()));
		return appCostStr.toString();
	}
	
	//update app state array in JSON
	public void updateStatesPerApp() throws JSONException {
		statesArray.put(appStateMap.toJSON());
	}

	//update app net usage array in JSON
	public void updateNetPerApp() throws JSONException {
		netsArray.put(netUsageArray.toJSON());
	}

	public JSONArray updateStatesJSON() throws JSONException {
		return statesArray;
	}

	public JSONArray updateNetJSON() throws JSONException {
		return netsArray;
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", getName());
		json.put("states", updateStatesJSON());
		json.put("network", updateNetJSON());
		json.put("rating", userRating);
		return json;
	}

}
