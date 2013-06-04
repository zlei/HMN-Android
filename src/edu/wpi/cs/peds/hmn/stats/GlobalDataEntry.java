/**
 * Orchestrates collection of stats relating to global phone state.
 */
package edu.wpi.cs.peds.hmn.stats;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
public class GlobalDataEntry
{
	private PowerStats powerStats;
	private NetworkStateMap networkStateMap = new NetworkStateMap();
	private long timestamp = 0;
	
	/**
	 * Creates a new snapshot of some global phone stats.
	 * 
	 * @param context the context from which to gather these stats.
	 */
	public GlobalDataEntry(Context context)
	{
		timestamp = System.currentTimeMillis();
		
		gatherPowerStats(context);
		gatherNetworkStats();
	}
	
	/**
	 * Takes a snapshot of the phone's power stats.
	 * 
	 * @param context the context from which to gather power stats.
	 */
	private void gatherPowerStats(Context context)
	{
		powerStats = PowerStats.gatherPowerStats(context);
	}
	
	/**
	 * Aggregates network stats and stores them by type of connection.
	 */
	private void gatherNetworkStats()
	{
		for (Application app : GlobalAppList.getInstance().getAllApps())
		{
			NetUsageEntry entry = app.netUsage.getLast();
			NetworkStats stats = networkStateMap.get(entry.connectionType);
			stats.receivedBytes += entry.receivedBytes;
			stats.transmittedBytes += entry.transmittedBytes;
			networkStateMap.put(entry.connectionType,stats);
		}
	}
	
	/**
	 * Converts this object into a JSON object.
	 * 
	 * @return a JSON object representing this object
	 * @throws JSONException if there is an error converting the data into JSON
	 */
	public JSONObject toJSON() throws JSONException
	{
		JSONObject dataEntry = new JSONObject();

		dataEntry.put("timestamp",timestamp);
		dataEntry.put("network",networkStateMap.toJSON());
		dataEntry.put("power",powerStats.toJSON());
		
		return dataEntry;
	}
}
