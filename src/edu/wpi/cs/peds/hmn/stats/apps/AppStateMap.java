/**
 * Used to track how long a single app spends in each state.
 */
package edu.wpi.cs.peds.hmn.stats.apps;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import edu.wpi.cs.peds.hmn.appcollector.AppState;

/**
 * A special HashMap that initializes itself with a default duration of 0 for
 * each possible app state, as well as defines how to convert itself into JSON.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 */
public class AppStateMap extends HashMap<AppState,Long>
{
	// generated serial version
	private static final long serialVersionUID = -8797161514386626838L;
	
	public AppStateMap()
	{
		for(AppState type : AppState.values())
			this.put(type,0L);
	}
	
	public String detailedInfo()
	{
		StringBuilder appStateMapStr = new StringBuilder();
		
		for (AppState state : this.keySet())
			appStateMapStr.append(String.format("%s: %s miliseconds\n",state.toString(),this.get(state)));
		
		return appStateMapStr.toString();
	}
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		
		for(AppState state : this.keySet())
		{
			Long duration = this.get(state);
			if (duration > 0)
				json.put(state.toJSONName(),duration);
		}
		
		return json;
	}
}