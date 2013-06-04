package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.wpi.cs.peds.hmn.app.R;

/**
 * @author Zhenhao Lei, zlei@wpi.edu
 */

public class OverallBenefitFragment extends Fragment {

	int aLaunchCount = 0;
	int popular = 12000;
	long aUseTime = 0;

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
		// appCount();
		TextView Rview = (TextView) view
				.findViewById(R.id.benefit_RatingDetail);
		TextView Pview = (TextView) view
				.findViewById(R.id.benefit_PopularityDetail);
		Rview.setText(ReviewDetail);
		Pview.setText(PopularityDetail);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public String reviewInfo() {
		StringBuilder appCostStr = new StringBuilder();
		appCostStr.append(String.format("Overall Rating: 4.2/5"));
		return appCostStr.toString();
	}

	public String popularityInfo() {
		StringBuilder appCostStr = new StringBuilder();
		appCostStr.append(String.format("Total installed: %d", popular));
		return appCostStr.toString();
	}
}