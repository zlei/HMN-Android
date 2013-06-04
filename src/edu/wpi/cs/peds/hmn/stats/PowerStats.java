/**
 * Orchestrates collection of power stats.
 */
package edu.wpi.cs.peds.hmn.stats;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.PowerManager;

/**
 * Collects and stores a snapshot of the phone's power state.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * 
 */
public class PowerStats {
	/**
	 * Valid charging methods
	 * 
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 * 
	 */
	enum ChargingMethod {
		AC, USB, OTHER
	}

	private boolean isCharging;
	private ChargingMethod chargingMethod;
	private boolean screenOn;
	private static float batteryPercentage;

	/**
	 * Ensures the only way a PowerStats object can be made is through
	 * {@link #gatherPowerStats(Context) gatherPowerStats}.
	 */
	public PowerStats() {

	}

	/**
	 * Gathers power stats info, such as screen state, charging state, and
	 * remaining battery life.
	 * 
	 * @param context
	 *            dictates where to query for this info.
	 * @return a new PowerStats object containing the gathered information.
	 */
	public static PowerStats gatherPowerStats(Context context) {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);

		PowerStats powerStats = new PowerStats();
		powerStats.screenOn = isScreenOn(context);
		powerStats.isCharging = isCharging(batteryStatus);
		if (powerStats.isCharging)
			powerStats.chargingMethod = getChargingMethod(batteryStatus);
		powerStats.batteryPercentage = getBatteryPercentage(batteryStatus);
		batteryPercentage = getBatteryPercentage(batteryStatus);
		return powerStats;
	}

	/**
	 * Detects whether or not the screen is on.
	 * 
	 * @param context
	 *            used to get the PowerManager, which is queried for the screen
	 *            state.
	 * @return true if the screen is on, false if it's off.
	 */
	private static boolean isScreenOn(Context context) {
		PowerManager powerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return powerManager.isScreenOn();
	}

	/**
	 * Detects if the phone is currently charging.
	 * 
	 * @param batteryStatus
	 *            contains extra info about the phone's power state.
	 * @return true if the phone is charging, false if it's not.
	 */
	private static boolean isCharging(Intent batteryStatus) {
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		return status == BatteryManager.BATTERY_STATUS_CHARGING
				|| status == BatteryManager.BATTERY_STATUS_FULL;
	}

	/**
	 * If the phone is charging, returns the method by which it's being charged.
	 * 
	 * @param batteryStatus
	 *            contains extra info about the phone's power state.
	 * @return the phone's charging method, or null if it's not charging
	 */
	private static PowerStats.ChargingMethod getChargingMethod(
			Intent batteryStatus) {
		if (!isCharging(batteryStatus))
			return null;

		int chargePlug = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_PLUGGED, -1);
		if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB)
			return ChargingMethod.USB;
		else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC)
			return ChargingMethod.AC;
		else
			return ChargingMethod.OTHER;
	}

	/**
	 * Calculates the remaining battery life.
	 * 
	 * @param batteryStatus
	 *            contains extra info about the phone's power state.
	 * @return remaining battery life, as a percentage
	 */
	private static float getBatteryPercentage(Intent batteryStatus) {
		float level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		float scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		return 100 * (level / scale);
	}

	/**
	 * Converts this object into a string fit for printing.
	 * 
	 * @return a string representation of this object
	 */
	public String detailedInfo() {
		StringBuilder powerStatsStr = new StringBuilder();

		powerStatsStr.append(String.format("screen on: %s\n", screenOn));
		if (isCharging)
			powerStatsStr.append(String.format("charging method: %s\n",
					chargingMethod));
		powerStatsStr.append(String.format("remaining battery: %.0f%%\n",
				batteryPercentage));

		return powerStatsStr.toString();
	}

	/*
	 * Detail battery information for cost fragment
	 */
	public String costInfo() {
		StringBuilder powerCostStatsStr = new StringBuilder();
		powerCostStatsStr.append(String.format("\nRemaining battery: %.2f%%\n",
				batteryPercentage));
		return powerCostStatsStr.toString();
	}

	/**
	 * Converts this object into a JSON object.
	 * 
	 * @return a JSON object representing this object
	 * @throws JSONException
	 *             if there is an error converting the data into JSON
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();

		json.put("screenOn", screenOn);
		json.put("charging", isCharging);
		if (isCharging)
			json.put("chargingMethod", chargingMethod.toString().toLowerCase());
		json.put("battery", batteryPercentage);

		return json;
	}
}
