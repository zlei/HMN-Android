package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import edu.wpi.cs.peds.hmn.app.R;

/**
 * @author Zhenhao Lei, zlei@wpi.edu
 */

public class OverallBenefitFragment extends Fragment {

	int aLaunchCount = 0;
	int popular = 0;
//	long aUseTime = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Application chosenApp = null;
		// CostBenefitActivity costuid = (CostBenefitActivity)getActivity();
		// chosenApp = GlobalAppList.getInstance().getApp(costuid.currentUid);
		String ReviewDetail = reviewInfo();
		String PopularityDetail = popularityInfo();
		View view = inflater.inflate(
				R.layout.activity_costbenefit_overallbenefittab, container,
				false);

		//set detail information
		TextView Rview = (TextView) view
				.findViewById(R.id.benefit_RatingDetail);
		TextView Pview = (TextView) view
				.findViewById(R.id.benefit_PopularityDetail);
		Rview.setText(ReviewDetail);
		Pview.setText(PopularityDetail);
	
		//set rating bar, retrieved data from database
		RatingBar Orate = (RatingBar) view.findViewById(R.id.benefit_Rating);
		RatingBar Prate = (RatingBar) view.findViewById(R.id.benefit_Popularity);
		Orate.setRating(Float.parseFloat("3.6"));
		Prate.setRating(Float.parseFloat("2.0"));	
	
		//to show all the detail information retrieved from database
		Button button = (Button) view.findViewById(R.id.overallbenefitbutton);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), OverallBenefitDetailActivity.class);
				getActivity().startActivity(intent);
			}
		});
		
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public String reviewInfo() {
		StringBuilder appCostStr = new StringBuilder();
		appCostStr.append(String.format("Overall Rating: 0/5"));
		return appCostStr.toString();
	}

	public String popularityInfo() {
		StringBuilder appCostStr = new StringBuilder();
		appCostStr.append(String.format("Total installed: %d", popular));
		return appCostStr.toString();
	}
}