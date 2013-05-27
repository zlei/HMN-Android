package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.wpi.cs.peds.hmn.app.R;

public class CostFragment extends Fragment{

 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        return inflater.inflate(R.layout.activity_costbenefit_costtab, container, false);
    }
 
    @Override
    public void onStart() {
        super.onStart();
    }
}	