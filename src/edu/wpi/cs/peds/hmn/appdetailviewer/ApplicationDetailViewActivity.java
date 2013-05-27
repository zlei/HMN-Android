/**
 * Handles formatting gathered data for the application detail view screen
 */
package edu.wpi.cs.peds.hmn.appdetailviewer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;
import edu.wpi.cs.peds.hmn.stats.costbenefit.CostBenefitActivity;

/**
 * A class representing an application's detail view.
 * 
 * @author Richard Brown, rpb111@wpi.edu
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu 
 * 
 */
public class ApplicationDetailViewActivity extends Activity {
	Application chosenApp = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_application_detail_view);

		final Button button = (Button) findViewById(R.id.costbutton);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent appCostBenefitIntent = new Intent(
						ApplicationDetailViewActivity.this,
						CostBenefitActivity.class);
				startActivity(appCostBenefitIntent);
			}
		});
		// Used to get the app that was clicked on the app list screen, which
		// is streamed over upon selection.
		// It may make more sense to make chosenApp a public static variable...
		Intent requestedAppIntent = getIntent();
		byte[] objectBytes = requestedAppIntent.getByteArrayExtra("uid");
		int uid = -1;
		// Receives the transmitted UID
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(objectBytes));
			uid = ois.readInt();
			ois.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// Uses the transmitted UID to lookup the appropriate Application object
		chosenApp = GlobalAppList.getInstance().getApp(uid);

		// Formats the chosen app's data and puts it on the screen
		if (chosenApp != null) {
			ImageView icon = (ImageView) findViewById(R.id.appicon);
			TextView appTitleView = (TextView) findViewById(R.id.lblapp_detail_title);
			TextView appDetailsView = (TextView) findViewById(R.id.txt_app_info);

			String appName = chosenApp.getName();
			String appDetails = chosenApp.detailedInfo();
			Drawable appIcon = chosenApp.getIcons();

			icon.setImageDrawable(appIcon);
			appTitleView.setText(appName);
			appDetailsView.setText(appDetails);

			RatingBar yourRatingBar = (RatingBar) findViewById(R.id.yourRating);
			yourRatingBar.setRating(chosenApp.userRating);
			yourRatingBar
					.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
						@Override
						public void onRatingChanged(RatingBar ratingBar,
								float newRating, boolean fromUser) {
							chosenApp.updateRating(newRating);
						}
					});

			RatingBar dbRatingBar = (RatingBar) findViewById(R.id.dbRating);
			dbRatingBar.setRating(chosenApp.dbRating);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
