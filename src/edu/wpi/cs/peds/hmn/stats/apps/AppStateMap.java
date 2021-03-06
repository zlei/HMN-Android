/**
 * Used to track how long a single app spends in each state.
 */
package edu.wpi.cs.peds.hmn.stats.apps;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wpi.cs.peds.hmn.appcollector.AppState;

/**
 * A special HashMap that initializes itself with a default duration of 0 for
 * each possible app state, as well as defines how to convert itself into JSON.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu
 * 
 */
public class AppStateMap extends HashMap<AppState, Long> {
	// generated serial version
	private static final long serialVersionUID = -8797161514386626838L;
	private long timestamp = 0;

	public AppStateMap() {
		for (AppState type : AppState.values())
			this.put(type, 0L);
	}

	// globalize timestamp
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String detailedInfo() {
		StringBuilder appStateMapStr = new StringBuilder();

		for (AppState state : this.keySet())
			appStateMapStr.append(String.format("%s: %s miliseconds\n",
					state.toString(), this.get(state)));

		return appStateMapStr.toString();
	}

	/*
	 * public boolean sendCurrentState(){ for (AppState state : this.keySet()){
	 * if (state.toString().equals("Not running")){ return false; } } return
	 * true; }
	 */
	public JSONObject toJSONState() throws JSONException {
		JSONObject json = new JSONObject();
		for (AppState state : this.keySet()) {
			Long duration = this.get(state);
			if (duration > 0) {
				json.put(state.toJSONName(), duration);
			}
		}
		return json;
	}

	public JSONObject toJSONObj() throws JSONException {
		JSONObject json = new JSONObject();
		for (AppState state : this.keySet()) {
			Long duration = this.get(state);
			if (duration > 0) {
				json.put("stateTime", toJSONState());
				json.put("currentState", state.toJSONName());
				json.put("timestamp", timestamp);
			}
		}
		return json;
	}

	public JSONArray toJSON() throws JSONException {
		JSONArray json = new JSONArray();
		json.put(toJSONObj());
		return json;
	}
}