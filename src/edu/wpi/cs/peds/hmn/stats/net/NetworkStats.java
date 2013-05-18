/**
 * Defines a storage unit for network statistics.
 */
package edu.wpi.cs.peds.hmn.stats.net;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.TrafficStats;
import edu.wpi.cs.peds.hmn.appcollector.AppState;

/**
 * A container for a snapshot of network traffic.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Richard Brown, rpb111@wpi.edu
 * 
 */
public class NetworkStats implements Serializable
{
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 6809596130296723855L;
	
	public long receivedBytes = 0;
	public long transmittedBytes = 0;
	public AppState state = null;
	
	/**
	 * Returns a new object containing the current network usage totals for the
	 * given uid. If the received bytes or transmitted bytes (or both) cannot
	 * be retrieved, that stat is reported as 0.
	 * 
	 * @param uid the UID of the Application you wish to query
	 * @return
	 */
	public static NetworkStats getTotalNetworkUsage(int uid)
	{
		long recveivedBytes = TrafficStats.getUidRxBytes(uid);
		if (recveivedBytes < 0)
			recveivedBytes = 0;
		long sentBytes = TrafficStats.getUidTxBytes(uid);
		if (sentBytes < 0)
			sentBytes = 0;
		
		return new NetworkStats(recveivedBytes,sentBytes,null);
	}
	
	public NetworkStats()
	{
		
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param networkStats the object to copy
	 */
	public NetworkStats(NetworkStats networkStats)
	{
		this.receivedBytes = networkStats.receivedBytes;
		this.transmittedBytes = networkStats.transmittedBytes;
		this.state = networkStats.state;
	}
	
	public NetworkStats(long receivedBytes, long transmittedBytes, AppState state)
	{
		this.receivedBytes = receivedBytes;
		this.transmittedBytes = transmittedBytes;
		this.state = state;
	}
	
	/**
	 * Calculates the difference between this instance and the provided
	 * instance, and produces a new NetworkStats object containing the result.
	 * 
	 * @param networkStats the object to subtract from this instance
	 * @return a new object containing the results
	 */
	public NetworkStats difference(NetworkStats networkStats)
	{
		return new NetworkStats(
				this.receivedBytes - networkStats.receivedBytes,
				this.transmittedBytes - networkStats.transmittedBytes,
				this.state
			);
	}
	
	/**
	 * Returns if this instance represents any network activity in either
	 * direction.
	 * 
	 * @return true if there has been network activity; false otherwise
	 */
	public boolean networkUsed()
	{
		return this.receivedBytes > 0 || this.transmittedBytes > 0;
	}

	@Override
	public String toString()
	{
		return String.format("downloaded: %s bytes\n" +
				"uploaded: %s bytes",
				receivedBytes,transmittedBytes);
	}

	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = new JSONObject();

		json.put("sent",transmittedBytes);
		json.put("received",receivedBytes);
		
		return json;
	}
}
