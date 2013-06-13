/**
 * Defines an element of a NetUsageList, as defined by the JSON schema.
 */
package edu.wpi.cs.peds.hmn.stats.net;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import edu.wpi.cs.peds.hmn.appcollector.NetDevice;

/**
 * An element of a NetUsageList. Along with network activity, it also includes
 * a timestamp and the type of connection at the time the data was collected.
 * 
 * @author Richard Brown, rpb111@wpi.edu
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu
 * 
 */
public class NetUsageEntry extends NetworkStats implements Serializable {
	// Generated serial version ID
	private static final long serialVersionUID = 4312896024169119760L;
	
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss.SSSa",Locale.US);
	public NetDevice connectionType;
	public Date currentDateStamp;

	public NetUsageEntry(NetDevice currentNetEnvironment, NetworkStats newStats)
	{
		super(newStats);
		this.connectionType = currentNetEnvironment;
		this.currentDateStamp = new Date(System.currentTimeMillis());
	}

	@Override
	public String toString()
	{
		String dateStr = dateFormatter.format(currentDateStamp);
		return dateStr + "\n" +
				"\tnetwork type: " + connectionType.toString() + "\n" +
				"\tdownloaded: " + receivedBytes + " bytes\n" +
				"\tuploaded: " + transmittedBytes + " bytes";
	}
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = super.toJSON();
		json.put("timestamp",currentDateStamp.getTime());
		json.put("connection",connectionType.toString().toLowerCase(Locale.ENGLISH));  // needs to be updated to support all types
		return json;
	}
	
	public JSONObject apptoJSON() throws JSONException
	{
		JSONObject json = super.toJSON();
		json.put("sent", appTransmittedBytes);
		json.put("received", appReceivedBytes);
		return json;
	}
}
