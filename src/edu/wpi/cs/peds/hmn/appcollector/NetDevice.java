/**
 * Defines the network types that can be detected,  as well as how to emit that
 * name as JSON.
 */
package edu.wpi.cs.peds.hmn.appcollector;

import java.util.Locale;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import edu.wpi.cs.peds.hmn.stats.net.NetworkStrength;

/**
 * @author Richard Brown, rpb111@wpi.edu
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * 
 */
public enum NetDevice {
	WIFI("wifi"), MOBILE("mobile"), UNKNOWNMOBILE("unknownmobile"), GPRS("gprs"), EDGE(
			"edge"), CDMA("cdma"), HSPA("hspa"), LTE("lte"), WIMAX("wimax"), BLUETOOTH(
			"bluetooth"), ETHERNET("ethernet"), OTHER("other");

	private final String name;

	/**
	 * Creates a new (named) network connection type *
	 * 
	 * @param name
	 */
	private NetDevice(String name) {
		this.name = name;

	}

	/**
	 * Converts Android's representation of its network type to one our app
	 * understands.
	 * 
	 * @param activeNetwork
	 *            an object obtained from the ConnectivityManager
	 * @return our app's representation of that network type
	 */
	public static NetDevice determineNetworkConnection(NetworkInfo activeNetwork) {
		NetDevice connection = OTHER;
		NetworkStrength networkstrength = new NetworkStrength();
		int mobileType = networkstrength.getMobileType();
		if (activeNetwork != null) {
			switch (activeNetwork.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				connection = WIFI;
				break;
			case ConnectivityManager.TYPE_MOBILE:
			case ConnectivityManager.TYPE_MOBILE_DUN:
			case ConnectivityManager.TYPE_MOBILE_HIPRI:
			case ConnectivityManager.TYPE_MOBILE_MMS:
			case ConnectivityManager.TYPE_MOBILE_SUPL: {
				if (mobileType == 1) {
					connection = GPRS;
					break;
				} else if (mobileType == 2) {
					connection = EDGE;
					break;
				} else if (mobileType == 4) {
					connection = CDMA;
					break;
				} else if (mobileType == 10) {
					connection = HSPA;
					break;
				} else if (mobileType == 13) {
					connection = LTE;
					break;
				} else {
					connection = UNKNOWNMOBILE;
					break;
				}
			}
			case ConnectivityManager.TYPE_WIMAX:
				connection = WIMAX;
				break;
			case ConnectivityManager.TYPE_BLUETOOTH:
				connection = BLUETOOTH;
				break;
			case ConnectivityManager.TYPE_ETHERNET:
				connection = ETHERNET;
				break;
			default:
				connection = OTHER;
				break;
			}
		}

		return connection;
	}

	@Override
	public String toString() {
		return name;
	}

	public String toJSON() {
		return this.name().toLowerCase(Locale.ENGLISH);
	}
}