/**
 * Orchestrates collection of stats relating to global phone state.
 */
package edu.wpi.cs.peds.hmn.stats;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.wpi.cs.peds.hmn.appcollector.AppCollectorService;
import edu.wpi.cs.peds.hmn.log.HmnLog;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;
import edu.wpi.cs.peds.hmn.stats.net.NetUsageEntry;
import edu.wpi.cs.peds.hmn.stats.net.NetworkStateMap;
import edu.wpi.cs.peds.hmn.stats.net.NetworkStats;

/**
 * Upon creation of a new instance, a snapshot is taken of some global phone
 * stats.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu
 */
public class GlobalDataEntry {
	private PowerStats powerStats;
	private int cpuStats;
	private NetworkStateMap networkStateMap = new NetworkStateMap();
	private long timestamp = 0;

	/**
	 * Creates a new snapshot of some global phone stats.
	 * 
	 * @param context
	 *            the context from which to gather these stats.
	 */

	public GlobalDataEntry(Context context) {
		timestamp = AppCollectorService.timestamp;
		cpuStats = (int) (getCPUStats() * 100);
		gatherPowerStats(context);
		gatherNetworkStats();
	}

	public GlobalDataEntry() {
	}

	// Globalize all timestamp in every sending
	public void setGlobalTimestamp() {
		timestamp = System.currentTimeMillis();
//		Log.i(HmnLog.HMN_LOG_TAG, "System timestamp!!!!!!" + timestamp);
	}

	public long getGlobalTimestamp() {
		return timestamp;
	}

	/**
	 * Takes a snapshot of the phone's power stats.
	 * 
	 * @param context
	 *            the context from which to gather power stats.
	 */
	private void gatherPowerStats(Context context) {
		powerStats = PowerStats.gatherPowerStats(context);
	}

	/**
	 * Aggregates network stats and stores them by type of connection.
	 */
	private void gatherNetworkStats() {
		for (Application app : GlobalAppList.getInstance().getAllApps()) {
			NetUsageEntry entry = app.netUsage.getLast();
			NetworkStats stats = networkStateMap.get(entry.connectionType);
			stats.receivedBytes += entry.receivedBytes;
			stats.transmittedBytes += entry.transmittedBytes;
			networkStateMap.put(entry.connectionType, stats);
		}
	}

	/**
	 * Get current total CPU usage information.
	 */

	private float getCPUStats() {
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
			String load = reader.readLine();

			String[] toks = load.split(" ");

			long idle1 = Long.parseLong(toks[5]);
			long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
					+ Long.parseLong(toks[4]) + Long.parseLong(toks[6])
					+ Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			try {
				Thread.sleep(360);
			} catch (Exception e) {
			}

			reader.seek(0);
			load = reader.readLine();
			reader.close();

			toks = load.split(" ");

			long idle2 = Long.parseLong(toks[5]);
			long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
					+ Long.parseLong(toks[4]) + Long.parseLong(toks[6])
					+ Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return 0;
	}

	public JSONObject CPUtoJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("cpuUsage", cpuStats);
		return json;
	}

	/**
	 * Converts this object into a JSON object.
	 * 
	 * @return a JSON object representing this object
	 * @throws JSONException
	 *             if there is an error converting the data into JSON
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject dataEntry = new JSONObject();

		dataEntry.put("timestamp", timestamp);
		dataEntry.put("network", networkStateMap.toJSON());
		dataEntry.put("cpu", CPUtoJSON());
		dataEntry.put("power", powerStats.toJSON());

		return dataEntry;
	}
}
