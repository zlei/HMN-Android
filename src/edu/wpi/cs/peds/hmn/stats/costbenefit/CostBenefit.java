/**
 * A placeholder for the final calculation/measurement of cost/benefit. This is
 * something we've been talking about for a while, but were never able to
 * actually quantify and decide on. There's some dummy calculations and
 * placeholders in here, such as the weights on battery life. They have no data
 * backing them; rather, they're rough estimates whose goal was more of a proof
 * of concept than a final result.
 */

package edu.wpi.cs.peds.hmn.stats.costbenefit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import edu.wpi.cs.peds.hmn.appcollector.AppState;
import edu.wpi.cs.peds.hmn.stats.apps.AppStateMap;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

/**
 * Contains everything needed to calculate the cost/benefit of an app.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 *
 */
public class CostBenefit
{
	private static long lastBatteryLevel;
	private static long deltaBatteryLevel;
	private static long lastRead = new Date().getTime();
	private static long startTime;
	
	private static final double ACTIVE_BATTERY_LEVEL_WEIGHT = .75;
	private static final double FOREGROUND_BATTERY_LEVEL_WEIGHT = .50;
	private static final double BACKGROUND_BATTERY_LEVEL_WEIGHT = .45;
	private static final double RUNNING_BATTERY_LEVEL_WEIGHT = BACKGROUND_BATTERY_LEVEL_WEIGHT;
	private static final double CACHED_BATTERY_LEVEL_WEIGHT = 0;
	private static final double NOTRUNNING_BATTERY_LEVEL_WEIGHT = 0;
	
	// TODO: ...so in hindsight, this method makes no sense. It was intended to
	// be called by each app in succession, but it calculates the
	// weightedBatteryLevelMap for each one! That's HORRIBLE. If this class is
	// kept, FIX THIS.
	public static int calcCost(Context context)
	{
		startTime = new Date().getTime();
		long duration = lastRead-startTime;
		
		setBatteryLevel(context);
		
		int cost = 100;
		if (duration != 0)
		{
			Map<Application,Long> weightedBatteryLevelMap = calcWeightedBatteryMap(context,duration);
		}
		
		lastRead = startTime;
		
		return cost;
	}
	
	// TODO: As above, this was intended to calculate the benefit of a single
	// app, but clearly was written as scaffolding for not that. If this class
	// is kept, FIX THIS.
	public static int calcBenefit(Context context)
	{
		int benefit = 100;
		
		return benefit;
	}
	
	/**
	 * Determines how much the battery level has dropped since the last time it was read.
	 * 
	 * @param context used to obtain the battery data
	 */
	private static void setBatteryLevel(Context context)
	{
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);
		
		long currentBatteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		deltaBatteryLevel = lastBatteryLevel - currentBatteryLevel;
		lastBatteryLevel = currentBatteryLevel;
	}
	
	/**
	 * Calculates the weighted battery life for each app. The weighted battery
	 * life is intended to take into account all resources being tracked by
	 * the app and rate its impact on the battery. A higher value is worse.
	 * 
	 * @param context used to accessing system resources
	 * @param duration how long it's been since the last calculation
	 * @return a map of each application and its weighted battery life
	 */
	private static Map<Application,Long> calcWeightedBatteryMap(Context context, long duration)
	{
		Map<Application,Long> weightedBatteryLevelMap = new HashMap<Application,Long>();
		for (Application app : GlobalAppList.getInstance().getAllApps())
		{
			StateChanges stateChanges = app.stateChanges;
			AppStateMap stateChangesSince = stateChanges.getStateMapSince(lastRead);
		
			long weightedTime = stateWeightedTime(stateChangesSince);
			long weightedBatteryLevel = (weightedTime/duration)*deltaBatteryLevel;
			
			weightedBatteryLevelMap.put(app,weightedBatteryLevel);
		}
		
		return weightedBatteryLevelMap;
	}
	
	/**
	 * Calculates the weighted time this app has consumed based on which states
	 * its been in and for how long.
	 * 
	 * @param stateChangesSince a map of state changes and their duration
	 * @return a weighted time
	 */
	private static long stateWeightedTime(AppStateMap stateChangesSince)
	{
		long weightedTime = 0;

		weightedTime += stateChangesSince.get(AppState.ACTIVE)*ACTIVE_BATTERY_LEVEL_WEIGHT;
		weightedTime += stateChangesSince.get(AppState.FOREGROUND)*FOREGROUND_BATTERY_LEVEL_WEIGHT;
		weightedTime += stateChangesSince.get(AppState.BACKGROUND)*BACKGROUND_BATTERY_LEVEL_WEIGHT;
		weightedTime += stateChangesSince.get(AppState.RUNNING)*RUNNING_BATTERY_LEVEL_WEIGHT;
		weightedTime += stateChangesSince.get(AppState.CACHED)*CACHED_BATTERY_LEVEL_WEIGHT;
		weightedTime += stateChangesSince.get(AppState.NOTRUNNING)*NOTRUNNING_BATTERY_LEVEL_WEIGHT;
		
		return weightedTime;
	}
}
