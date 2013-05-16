package edu.wpi.cs.peds.hmn.stats.costbenefit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.wpi.cs.peds.hmn.appcollector.AppState;
import edu.wpi.cs.peds.hmn.stats.apps.AppStateMap;

/**
 * A list of data relating to the length of time an app spent in a certain
 * state.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 *
 */
public class StateChanges extends ArrayList<StateChangesEntry>
{
	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 3337790457827469549L;

	/**
	 * Creates a new entry in the list from the provided data.
	 * 
	 * @param currentTime a timestamp, in miliseconds from the epoch
	 * @param appState the state being logged
	 * @param stateDuration time spent in this state
	 */
	public void addChange(long currentTime, AppState appState, long stateDuration)
	{
		this.add(new StateChangesEntry(currentTime,appState,stateDuration));
	}
	
	/**
	 * Finds all states stored in this object whose timestamps are greater than
	 * or equal to time and compiles them into a map where each key is a state
	 * and each value is the amount of miliseconds spent in that state. 
	 * 
	 * @param time the earliest timestamp to consider 
	 * @return a map with states mapped to durations
	 */
	public AppStateMap getStateMapSince(long time)
	{
		AppStateMap stateMap = new AppStateMap();
		
		List<StateChangesEntry> stateChangeList = (StateChanges)this.clone();
		Collections.reverse(stateChangeList);
		
		for (StateChangesEntry stateChange : stateChangeList)
		{
			if (stateChange.timestamp < time)
				break;
			
			long stateTotal = stateMap.get(stateChange.state);
			stateMap.put(stateChange.state,stateTotal+stateChange.duration);
		}
		
		return stateMap;
	}
}

/**
 * A container for the data within a list of state changes
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 *
 */
class StateChangesEntry implements Serializable
{
	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -1158135696072772287L;
	
	public long timestamp;
	public AppState state;
	public long duration;
	
	public StateChangesEntry(long timestamp, AppState state, long duration)
	{
		this.timestamp = timestamp;
		this.state = state;
		this.duration = duration;
	}
}