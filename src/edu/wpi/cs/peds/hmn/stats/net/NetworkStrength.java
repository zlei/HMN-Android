package edu.wpi.cs.peds.hmn.stats.net;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;


public class NetworkStrength{

	private static int wifiStrength = 0;
	private static int mobileType = 0;
	public void updateNetworkStrength(Context context){
	TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	mobileType = telephony.getNetworkType();
	Log.d("type" , Integer.toString(mobileType));
//	Log.d("type" , telephony.getCellLocation().toString());

	WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	wifiStrength = wifi.getConnectionInfo().getRssi();
	//	Log.d("wifi" , Integer.toString(wifi.calculateSignalLevel(wifi.getConnectionInfo().getRssi(), wifi.getScanResults())));
	Log.d("wifi" , Integer.toString(wifiStrength));
	}
	
	public int getWifiStrength(){
		return -wifiStrength;
	}
	
	public int getMobileType(){
		return mobileType;
	}
	
}