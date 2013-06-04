package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.net.TrafficStats;
import edu.wpi.cs.peds.hmn.appdetailviewer.ApplicationDetailViewActivity;

/**
 * 
 * @author Zhenhao Lei, zlei@wpi.edu
 * 
 */

public class LiveData {
	private static int currentUid;
	private static float totalbefore = 0;
	private static float uploadbefore = 0;
	private static float downloadbefore = 0;
	private static float totalafter = 0;
	private static float uploadafter = 0;
	private static float downloadafter = 0;

	public static Point getTotalData(int x) {
		if (x == 0) {
			totalData();
			return new Point(x, 0);
		}
		return new Point(x, totalData());
	}

	public static Point getUploadData(int x) {
		if (x == 0) {
			uploadData();
			return new Point(x, 0);
		}
		return new Point(x, uploadData());
	}

	public static Point getDownloadData(int x) {
		if (x == 0) {
			downloadData();
			return new Point(x, 0);
		}
		return new Point(x, downloadData());
	}

	// Get the current upload, download and total bytes for the specific app by
	// its UID
	private static float totalData() {
		float y = 0;
		currentUid = ApplicationDetailViewActivity.uid;
		totalafter = TrafficStats.getUidRxBytes(currentUid)
				+ TrafficStats.getUidTxBytes(currentUid);
		// use unit KB
		y = (totalafter - totalbefore) / 1024;
		totalbefore = totalafter;
//		Log.e(HmnLog.HMN_LOG_TAG, "y!!!!!!!!!!!!!!!!!" + y);
		return y;
	}

	private static float uploadData() {
		float y = 0;
		currentUid = ApplicationDetailViewActivity.uid;
		uploadafter = TrafficStats.getUidTxBytes(currentUid);
		// use unit KB
		y = (uploadafter - uploadbefore) / 1024;
		uploadbefore = uploadafter;
//		Log.e(HmnLog.HMN_LOG_TAG, "y!!!!!!!!!!!!!!!!!" + y);
		return y;
	}

	private static float downloadData() {
		float y = 0;
		currentUid = ApplicationDetailViewActivity.uid;
		downloadafter = TrafficStats.getUidRxBytes(currentUid);
		// use unit KB
		y = (downloadafter - downloadbefore) / 1024;
		downloadbefore = downloadafter;
//		Log.e(HmnLog.HMN_LOG_TAG, "y!!!!!!!!!!!!!!!!!" + y);
		return y;
	}
	
		
	public static Point getOverallTotalData(int x) {
		if (x == 0) {
			overallTotalData();	
			return new Point(x, 0);
		}
		return new Point(x, overallTotalData());
	}

	public static Point getOverallUploadData(int x) {
		if (x == 0) {
			overallUploadData();
			return new Point(x, 0);
		}
		return new Point(x, overallUploadData());
	}

	public static Point getOverallDownloadData(int x) {
		if (x == 0) {
			overallDownloadData();
			return new Point(x, 0);
		}
		return new Point(x, overallDownloadData());
	}

	// Get the current upload, download and total bytes for the specific app by
	// its UID
	private static float overallTotalData() {
		float y = 0;
		totalafter = TrafficStats.getTotalRxBytes()
				+ TrafficStats.getTotalTxBytes();
		// use unit KB
		y = (totalafter - totalbefore) / 1024;
		totalbefore = totalafter;
		return y;
	}

	private static float overallUploadData() {
		float y = 0;
		uploadafter = TrafficStats.getTotalTxBytes();
		// use unit KB
		y = (uploadafter - uploadbefore) / 1024;
		uploadbefore = uploadafter;
		return y;
	}

	private static float overallDownloadData() {
		float y = 0;
		downloadafter = TrafficStats.getTotalRxBytes();
		// use unit KB
		y = (downloadafter - downloadbefore) / 1024;
		downloadbefore = downloadafter;
		return y;
	}
}
