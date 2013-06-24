package edu.wpi.cs.peds.hmn.stats;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import edu.wpi.cs.peds.hmn.log.HmnLog;
import edu.wpi.cs.peds.hmn.stats.apps.Application;

/*
 * 
 * @author Zhenhao Lei, zlei@wpi.edu
 * 
 */

//Parse data from database

public class JSONParser {

		Application chosenApp = null;
		int currentUid;
		private static final String USER_ID = Build.SERIAL;
		private static final String URL = "http://hmn.cs.wpi.edu/cgi-bin/getAppAttri.pl?user_Id="
				+ USER_ID + "&app_name=";

		HttpClient client;

		public void getFromServer(Application thisApp) {
			chosenApp = thisApp;
			client = new DefaultHttpClient();
			new Read().execute("averageOverallRating");
		}

		public JSONObject appInfo(String appname)
				throws ClientProtocolException, IOException, JSONException {
			String url = "";
			url = URL + appname;
			url = url.replaceAll(" ", "%20");

			Log.i(HmnLog.HMN_LOG_TAG, "APPNAME!!!" + appname);
			Log.i(HmnLog.HMN_LOG_TAG, "URL!!!" + url);
			HttpGet get = new HttpGet(url.toString());
			Log.i(HmnLog.HMN_LOG_TAG, "client.execute(get)");
			Log.i(HmnLog.HMN_LOG_TAG, "URL" + url.toString());
			HttpResponse r = client.execute(get);
			int status = r.getStatusLine().getStatusCode();
			Log.i(HmnLog.HMN_LOG_TAG, "STATUS" + status);

			if (status == 200) {
				HttpEntity e = r.getEntity();
				String data = EntityUtils.toString(e);
				// JSONArray timeline = new JSONArray(data);
				JSONObject appinfo = new JSONObject(data);
				// JSONObject appinfo = timeline.getJSONObject(0);

				// Log.i(HmnLog.HMN_LOG_TAG, "TIMELINE!" + timeline.toString());
				Log.i(HmnLog.HMN_LOG_TAG, "appinfo!" + appinfo.toString());
				return appinfo;
			}
			return null;
			}

		public class Read extends AsyncTask<String, Integer, String> {

			protected String doInBackground(String... params) {
				try {
					String APP_ID = chosenApp.name;
					if (chosenApp.name == null)
						APP_ID = chosenApp.packageName;
					// Log.i(HmnLog.HMN_LOG_TAG, APP_ID);
					JSONObject json = appInfo(APP_ID);
					Log.i(HmnLog.HMN_LOG_TAG, "json.getString(params[0])!"
							+ json.getString(params[0]));
					return json.getString(params[0]);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(String result) {
				Log.i(HmnLog.HMN_LOG_TAG, result);
				chosenApp.dbRating = Float.parseFloat(result);
			}
		}

}
