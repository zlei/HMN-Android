/**
 * Used to track network usage for each app across each kind of network
 * connection it could possible use. Realistically, in most cases, only a single
 * connection type will be used, but this allows the app to handle the case
 * where the connection type changes for some reason.
 */
package edu.wpi.cs.peds.hmn.stats.net;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import edu.wpi.cs.peds.hmn.appcollector.NetDevice;

/**
 * A special HashMap that initializes itself with an empty NetworkStats for each
 * possible network state, as well as defines how to convert itself into JSON.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 *
 */
public class NetworkStateMap extends HashMap<NetDevice, NetworkStats>
{
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 4379904919342508773L;

	public NetworkStateMap()
	{
		for(NetDevice type : NetDevice.values())
			this.put(type,new NetworkStats());
	}
	
	/**
	 * Creates a new NetworkStateMap in which each value is the difference
	 * between the corresponding NetworkStats in this map and the provided map. 
	 * 
	 * @param networkStateMap another map 
	 * @return a NetworkStateMap representing the difference between this map
	 * and the provided one.
	 */
	public NetworkStateMap difference(NetworkStateMap networkStateMap)
	{
		NetworkStateMap diffMap = new NetworkStateMap();
		
		for(NetDevice type : NetDevice.values())
			diffMap.put(type,this.get(type).difference(networkStateMap.get(type)));
		
		return diffMap;
	}
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		
		for(NetDevice type : this.keySet())
		{
			NetworkStats stats = this.get(type);
			if (stats.networkUsed())
				json.put(type.toJSON(),stats.toJSON());
		}
		
		return json;
	}
	
	public String toString()
	{
		StringBuilder appStateMapStr = new StringBuilder();
		
		for (NetDevice type : this.keySet())
			appStateMapStr.append(String.format("%s: %s\n",type.toString(),this.get(type).toString()));
		
		return appStateMapStr.toString();
	}
}
