package edu.wpi.cs.peds.hmn.stats.costbenefit;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.appdetailviewer.ApplicationDetailViewActivity;
import edu.wpi.cs.peds.hmn.log.HmnLog;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

/*
 * 
 * @author Zhenhao Lei, zlei@wpi.edu
 * 
 */

public class MyBenefitDetailActivity extends Activity {

	// private static final String HmnLog.HMN_LOG_TAG = "ProjectServerDemo";

	Application chosenApp = null;
	int currentUid;
	private static final String USER_ID = Build.SERIAL;
	private static final String URL = "http://hmn.cs.wpi.edu/cgi-bin/getAppAttri.pl?user_Id="
			+ USER_ID + "&app_name=";

	HttpClient client;
	TextView text;
	JSONObject json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_benefit_detail);
		currentUid = ApplicationDetailViewActivity.chosenApp.uid;
		chosenApp = GlobalAppList.getInstance().getApp(currentUid);
		text = (TextView) findViewById(R.id.myBenefitView);
		client = new DefaultHttpClient();
		new Read().execute("averageRating");
	}

	@SuppressLint("ShowToast")
	public JSONObject appInfo(String username) throws ClientProtocolException,
			IOException, JSONException {
		StringBuilder url = new StringBuilder(URL);
		url.append(username);

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
//			Log.i(HmnLog.HMN_LOG_TAG, "appinfo!" + appinfo.toString());
			return appinfo;
		} else {
			Toast.makeText(MyBenefitDetailActivity.this, "error",
					Toast.LENGTH_SHORT);
			return null;
		}
	}

	public class Read extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			try {
				String APP_ID = chosenApp.name;
				if (chosenApp.name == null)
					APP_ID = chosenApp.packageName;

				json = appInfo(APP_ID);
				Log.i(HmnLog.HMN_LOG_TAG, json.getString(params[0]));
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
			text.setText(result);
		}
	}
}