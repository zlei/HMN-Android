/**
 * Stores all collected network data for an individual app.
 */
package edu.wpi.cs.peds.hmn.stats.net;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.net.TrafficStats;
import edu.wpi.cs.peds.hmn.appcollector.AppState;
import edu.wpi.cs.peds.hmn.appdetailviewer.ApplicationDetailViewActivity;
import edu.wpi.cs.peds.hmn.stats.apps.Application;

/**
 * A list of network data which can be formatted nicely for output, as well as
 * emitted as JSON.
 * 
 * @author Richard Brown, rpb111@wpi.edu
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu
 */
public class NetUsageList extends LinkedList<NetUsageEntry> {
	/**
	 * Auto-generated serial ID
	 */
	private static final long serialVersionUID = 6577374198701492542L;

	private final long BASE = 1024, KB = BASE, MB = KB * BASE, GB = MB * BASE;
	private final DecimalFormat df = new DecimalFormat("#.##");
	private float totalBytes;

	Application chosenApp;
	// Interface implementation for getting a NetUsageEntry's uploaded bytes
	private final GetBytes getUploadedBytes = new GetBytes() {
		private static final long serialVersionUID = -3706296390701186785L;

		@Override
		public double getBytes(NetUsageEntry entry) {
			return entry.transmittedBytes;
		}
	};

	// Interface implementation for getting a NetUsageEntry's downloaded bytes
	private final GetBytes getDownloadedBytes = new GetBytes() {
		private static final long serialVersionUID = 4281829415865193155L;

		@Override
		public double getBytes(NetUsageEntry entry) {
			return entry.receivedBytes;
		}
	};

	private final GetBytes getTotalBytes = new GetBytes() {
		private static final long serialVersionUID = 1598451321684946546L;

		@Override
		public double getBytes(NetUsageEntry entry) {

			totalBytes = entry.transmittedBytes + entry.receivedBytes;
			return totalBytes;
		}
	};

	/**
	 * Converts the provided number of bytes into larger units (KB, MB, GB) and
	 * returns it as a string.
	 * 
	 * @param numBytes
	 *            number of bytes
	 * @return the bytes, converted into larger units
	 */
	private String formatBytes(Double numBytes) {
		if (numBytes >= GB) {
			return df.format(numBytes / GB) + " GB";
		}
		if (numBytes >= MB) {
			return df.format(numBytes / MB) + " MB";
		}
		if (numBytes >= KB) {
			return df.format(numBytes / KB) + " KB";
		}
		return numBytes + " byte(s)";
	}

	/**
	 * Gets the bytes exchanged in the foreground and formats the result.
	 * 
	 * @param getBytes
	 *            the method used for extracting bytes from a NetUsageEntry
	 * @return nicely formatted bytes
	 */
	public String getPrettyForegroundBytes(GetBytes getBytes) {
		return getAndFormatBytes(getBytes, AppState.FOREGROUND, AppState.ACTIVE);
	}

	/**
	 * Gets the bytes exchanged in the background and formats the result.
	 * 
	 * @param getBytes
	 *            the method used for extracting bytes from a NetUsageEntry
	 * @return nicely formatted bytes
	 */
	public String getPrettyBackgroundBytes(GetBytes getBytes) {
		return getAndFormatBytes(getBytes, AppState.BACKGROUND, AppState.CACHED);
	}

	/**
	 * Gets the total bytes exchanged and formats the result.
	 * 
	 * @param getBytes
	 *            the method used for extracting bytes from a NetUsageEntry
	 * @return nicely formatted bytes
	 */
	public String getPrettyBytes(GetBytes getBytes) {
		return getAndFormatBytes(getBytes);
	}

	/**
	 * Accumulates the total bytes exchanged according to getBytes, and filters
	 * out all except the ones produced while the app was in one of the given
	 * states. The output is then transformed into a more readable format.
	 * 
	 * @param getBytes
	 *            the method used for extracting bytes from a NetUsageEntry
	 * @param states
	 * @return
	 */
	private String getAndFormatBytes(GetBytes getBytes, AppState... states) {
		List<AppState> stateList = Arrays.asList(states);
		double bytes = 0;
		for (NetUsageEntry entry : this)
			if (stateList.isEmpty() || stateList.contains(entry.state))
				bytes += getBytes.getBytes(entry);
		return formatBytes(bytes);
	}

	public String getShortString() {
		return String.format("Total Up: %s\nTotal Down: %s",
				getPrettyBytes(getUploadedBytes),
				getPrettyBytes(getDownloadedBytes));
	}

	@Override
	public String toString() {
		String totals = String.format(
				"Uploaded: %s\nDownloaded: %s\n\nENTRIES\n",
				getPrettyBytes(getUploadedBytes),
				getPrettyBytes(getDownloadedBytes));

		StringBuilder netUsageStr = new StringBuilder(totals);
		for (NetUsageEntry entry : this)
			netUsageStr.append(entry.toString() + "\n");
		return netUsageStr.toString();
	}

	public String detailedInfo() {
		StringBuilder netUsageStr = new StringBuilder();

		netUsageStr.append("\nUPLOADED\n");
		netUsageStr.append(String.format("Total: %s\n",
				getPrettyBytes(getUploadedBytes)));
		netUsageStr.append(String.format("In foreground: %s\n",
				getPrettyForegroundBytes(getUploadedBytes)));
		netUsageStr.append(String.format("In background: %s\n",
				getPrettyForegroundBytes(getUploadedBytes)));

		netUsageStr.append("\nDOWNLOADED\n");
		netUsageStr.append(String.format("Total: %s\n",
				getPrettyBytes(getDownloadedBytes)));
		netUsageStr.append(String.format("In foreground: %s\n",
				getPrettyForegroundBytes(getDownloadedBytes)));
		netUsageStr.append(String.format("In background: %s\n",
				getPrettyForegroundBytes(getDownloadedBytes)));

		return netUsageStr.toString();
	}

	public String costInfo() {
		StringBuilder netUsageStr = new StringBuilder();

		netUsageStr.append("\nTotal Data: ");
		netUsageStr.append(String.format(getPrettyBytes(getTotalBytes)));

		netUsageStr.append("\nTotal Uploaded: ");
		netUsageStr.append(String.format(getPrettyBytes(getUploadedBytes)));

		netUsageStr.append("\nTotal Downloaded: ");
		netUsageStr.append(String.format(getPrettyBytes(getDownloadedBytes)));

		netUsageStr
				.append(String
						.format("\nTotal Percentage: %.2f %%",
								(totalBytes / (float) (TrafficStats
										.getTotalTxBytes() + TrafficStats
										.getTotalRxBytes())) * 100));
		return netUsageStr.toString();
	}

	public String totalCostInfo() {
		StringBuilder netUsageStr = new StringBuilder();
		netUsageStr.append(String.format(
				"\nTotal Usage: %.2f MB",
				(float) (TrafficStats.getTotalTxBytes() + TrafficStats
						.getTotalRxBytes()) / (1024 * 1024)));
		netUsageStr.append(String.format("\nTotal Uploaded: %.2f MB",
				(float) TrafficStats.getTotalTxBytes() / (1024 * 1024)));
		netUsageStr.append(String.format("\nTotal Downloaded: %.2f MB",
				(float) TrafficStats.getTotalRxBytes() / (1024 * 1024)));
		return netUsageStr.toString();
	}

	public float networkMonitorInfo() {
		return totalBytes;
	}

	public String appUsageInfo() {
		int currentUid = ApplicationDetailViewActivity.uid;
		String appNetUsageStr = "";

		for (NetUsageEntry entry : this)
			if (entry.networkUsed()) {
				Float total = (float) (TrafficStats.getUidTxBytes(currentUid) + TrafficStats
						.getUidRxBytes(currentUid)) / 1024;
				String totalUsage = total.toString();
				String downloaded = " downloaded: " + (float) TrafficStats.getUidRxBytes(currentUid) / 1024;
				String uploaded = " uploaded: " + (float) TrafficStats.getUidTxBytes(currentUid) / 1024;
				appNetUsageStr = total + uploaded + downloaded;
				return totalUsage;
			}
		return "";
	}

	public String entryListDetails() {
		StringBuilder entryStr = new StringBuilder();
		for (NetUsageEntry entry : this)
			if (entry.networkUsed())
				entryStr.append(entry.toString() + "\n");

		if (entryStr.length() > 0)
			entryStr.deleteCharAt(entryStr.length() - 1);
		return entryStr.toString();
	}

	public JSONArray toJSON() throws JSONException {
		JSONArray json = new JSONArray();

		for (NetUsageEntry entry : this)
			if (entry.networkUsed())
				json.put(entry.toJSON());
		return json;
	}

	public JSONArray apptoJSON() throws JSONException {
		JSONArray json = new JSONArray();

		for (NetUsageEntry entry : this)
			if (entry.networkUsed())
				json.put(entry.apptoJSON());
		return json;
	}
}

/**
 * Passes a the getBytes function as a parameter
 * 
 * @author Auzzy
 * 
 */
interface GetBytes extends Serializable {
	/**
	 * Extracts bytes from an entry.
	 * 
	 * @param entry
	 *            the entry in question
	 * @return bytes extracted from entry
	 */
	double getBytes(NetUsageEntry entry);
}