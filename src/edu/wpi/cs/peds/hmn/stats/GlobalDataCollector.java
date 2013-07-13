/**
 * Orchestrates collection and temporary storage of all relevant phone and app
 * stats, as well as how to emit all this data as JSON.
 */
package edu.wpi.cs.peds.hmn.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import edu.wpi.cs.peds.hmn.appcollector.AppState;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

/**
 * A singleton, to ensure that there's only ever a single data collector
 * instance, which can be accessed from anywhere.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu
 * 
 */
public class GlobalDataCollector {
	private List<GlobalDataEntry> dataEntries;

	private static GlobalDataCollector globalDataCollector;

	private GlobalDataCollector() {
		dataEntries = new ArrayList<GlobalDataEntry>();
	}

	/**
	 * Defines the GlobalDataCOllector as a singleton, ensuring all data is
	 * collected in a central location.
	 * 
	 * @return a new GlobalDataCollector instance
	 */
	public static GlobalDataCollector getInstance() {
		if (globalDataCollector == null)
			globalDataCollector = new GlobalDataCollector();
		return globalDataCollector;
	}

	/**
	 * Initiates a data collection in the given context (usually the calling
	 * Activity). Each app's stats get updated, and a snapshot is taken of some
	 * global phone stats. Each snapshot is stored for later reporting.
	 * 
	 * @param context
	 *            a context to use for collecting global phone stats.
	 */
	public void gatherStats(Context context) {
		Map<Application, AppState> appStateMap = GlobalAppList.getInstance()
				.getAppStateMap();
		for (Application app : GlobalAppList.getInstance().getAllApps()) {
			app.updateStats(context, appStateMap.get(app));
		}
		dataEntries.add(new GlobalDataEntry(context));
	}

	/**
	 * Gathers general phone info, mostly intended for filtering purposes.
	 * 
	 * @return a map of keys and values representing the discovered stats.
	 */
	private Map<String, String> getPhoneInfo() {
		Map<String, String> info = new HashMap<String, String>();

		info.put("os", "Android");
		info.put("version", Build.VERSION.RELEASE);
		info.put("device",
				String.format("%s %s", Build.MANUFACTURER, Build.MODEL));
		return info;
	}

	/**
	 * Clears all collected data entries.
	 */
	public void clearStats() {
		dataEntries = new ArrayList<GlobalDataEntry>();
	}

	/**
	 * Converts this object into a JSON object. It also generates an ID for the
	 * device which is intended to be unique, which is added to the gathered
	 * JSON object.
	 * 
	 * @return a JSON object representing all data collected so far
	 * @throws JSONException
	 *             if there is an error converting the data into JSON
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();

		json.put("id", Build.SERIAL);
		json.put("info", new JSONObject(getPhoneInfo()));

		JSONArray statsArray = new JSONArray();
		for (GlobalDataEntry dataEntry : dataEntries)
			statsArray.put(dataEntry.toJSON());
		json.put("stats", statsArray);
		return json;
	}
}