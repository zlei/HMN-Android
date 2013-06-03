package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

/**
 * @author Zhenhao Lei, zlei@wpi.edu
 */

public class OverallCostFragment extends Fragment{

	public void onAttach(Activity CostBenefitActivity){
		super.onAttach(CostBenefitActivity);
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	Application chosenApp = null;
    	CostBenefitActivity costuid = (CostBenefitActivity)getActivity();
		chosenApp = GlobalAppList.getInstance().getApp(costuid.currentUid);
    	String appDetails = chosenApp.costInfo();
    	View view = inflater.inflate(R.layout.activity_costbenefit_overallcosttab, container, false);
        TextView textview = (TextView) view.findViewById(R.id.costDetail);
        textview.setText(appDetails);

        Button button = (Button) view.findViewById(R.id.networkMonitor);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), CostGraphActivity.class);
				getActivity().startActivity(intent);
			}
		});
        return view;
    }
 
    @Override
    public void onStart() {
        super.onStart();
    }

}