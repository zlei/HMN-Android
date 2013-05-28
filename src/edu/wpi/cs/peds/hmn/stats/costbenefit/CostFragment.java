package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

public class CostFragment extends Fragment{

	public void onAttach(Activity CostBenefitActivity){
		super.onAttach(CostBenefitActivity);
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	Application chosenApp = null;
    	CostBenefitActivity costuid = (CostBenefitActivity)getActivity();
		chosenApp = GlobalAppList.getInstance().getApp(costuid.currentUid);
    	String appDetails = chosenApp.costInfo();
    	View view = inflater.inflate(R.layout.activity_costbenefit_costtab, container, false);
        TextView textview = (TextView) view.findViewById(R.id.costDetail);
        textview.setText(appDetails);
        return view;
    }
 
    @Override
    public void onStart() {
        super.onStart();
    }
    
}