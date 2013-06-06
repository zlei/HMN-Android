package edu.wpi.cs.peds.hmn.stats.costbenefit;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.log.HmnLog;

public class MyBenefitDetailActivity extends Activity {

	// private static final String HmnLog.HMN_LOG_TAG = "ProjectServerDemo";

	public static final String APP_ID = "1234567890";
	public static final String SERVER_URL = "http://demoprojectserver1234.appspot.com/";
	public static final String QUERY_FILE = "xmlquery.cgi";
	public static final String QUERY_OPTIONS = "?appid=" + APP_ID;
	public static final String QUERY_URL = SERVER_URL + QUERY_FILE
			+ QUERY_OPTIONS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_benefit_detail);
		AsyncDownloader downloader = new AsyncDownloader();
		downloader.execute();
	}

	private void handleNewRecord(String itemId, String data) {
		TextView textView = (TextView) findViewById(R.id.myBenefitView);
		String message = textView.getText().toString() + "\n" + itemId + ": "
				+ data;
		textView.setText(message);
	}

	private class AsyncDownloader extends AsyncTask<Object, String, Integer> {
		@Override
		protected Integer doInBackground(Object... arg0) {
			XmlPullParser receivedData = tryDownloadingXmlData();
			int recordsFound = tryParsingXmlData(receivedData);
			return recordsFound;
		}

		private XmlPullParser tryDownloadingXmlData() {
			try {
				Log.i(HmnLog.HMN_LOG_TAG, "Now downloading...");
				URL xmlUrl = new URL(QUERY_URL);
				XmlPullParser receivedData = XmlPullParserFactory.newInstance()
						.newPullParser();
				receivedData.setInput(xmlUrl.openStream(), null);
				return receivedData;
			} catch (XmlPullParserException e) {
				Log.e(HmnLog.HMN_LOG_TAG, "XmlPullParserExecption", e);
			} catch (IOException e) {
				Log.e(HmnLog.HMN_LOG_TAG, "XmlPullParserExecption", e);
			}
			return null;
		}

		private int tryParsingXmlData(XmlPullParser receivedData) {
			if (receivedData != null) {
				try {
					return processReceivedData(receivedData);
				} catch (XmlPullParserException e) {
					Log.e(HmnLog.HMN_LOG_TAG, "Pull Parser failure", e);
				} catch (IOException e) {
					Log.e(HmnLog.HMN_LOG_TAG, "IO Exception parsing XML", e);
				}
			}
			return 0;
		}

		private int processReceivedData(XmlPullParser xmlData)
				throws XmlPullParserException, IOException {
			int recordsFound = 0; // Find values in the XML records
			String appId = ""; // Attributes
			String itemId = "";
			String timeStamp = "";
			String data = ""; // Text
			int eventType = -1;
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				String tagName = xmlData.getName();
				switch (eventType) {
				case XmlResourceParser.START_TAG:
					// Start of a record, so pull values encoded as attributes.
					if (tagName.equals("record")) {
						appId = xmlData.getAttributeValue(null, "appid");
						itemId = xmlData.getAttributeValue(null, "itemid");
						timeStamp = xmlData
								.getAttributeValue(null, "timestamp");
						data = "";
					}
					break;
				// Grab data text (very simple processing)
				// NOTE: This could be full XML data to process.
				case XmlResourceParser.TEXT:
					data += xmlData.getText();
					break;

				case XmlPullParser.END_TAG:
					if (tagName.equals("record")) {
						recordsFound++;
						publishProgress(appId, itemId, data, timeStamp);
					}
					break;
				}
				eventType = xmlData.next();
			}
			if (recordsFound == 0) {
				publishProgress();
			}
			Log.i(HmnLog.HMN_LOG_TAG, "Finished processing " + recordsFound
					+ " records.");
			return recordsFound;
		}

		protected void onProgressUpdate(String... values) {
			if (values.length == 0) {
				Log.i(HmnLog.HMN_LOG_TAG, "No data downloaded");
			}
			if (values.length == 4) {
				String appId = values[0];
				String itemId = values[1];
				String data = values[2];
				String timeStamp = values[3];
				Log.i(HmnLog.HMN_LOG_TAG, "AppID: " + appId + ", Timestamp: "
						+ timeStamp);
				Log.i(HmnLog.HMN_LOG_TAG, " ItemID: " + itemId + ", Data: "
						+ data); // Pass it to the application
				handleNewRecord(itemId, data);
			}
			super.onProgressUpdate(values);
		}
	}

}
