package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.appdetailviewer.ApplicationDetailViewActivity;
import edu.wpi.cs.peds.hmn.stats.JSONParser;
import edu.wpi.cs.peds.hmn.stats.apps.Application;

/*
 * 
 * @author Zhenhao Lei, zlei@wpi.edu
 * 
 */

public class OverallBenefitDetailActivity extends Activity {

	// private static final String HmnLog.HMN_LOG_TAG = "ProjectServerDemo";

	Application chosenApp = null;
	JSONParser jsonParser = new JSONParser();
	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overall_benefit_detail);
		chosenApp = ApplicationDetailViewActivity.chosenApp;
		text = (TextView) findViewById(R.id.overallBenefitView);
		jsonParser.getFromServer(chosenApp);
		text.setText(chosenApp.allBenefitInfo());
	}
	
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}