package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
 
public class BenefitFragment extends Fragment{
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//       Application chosenApp = null;
 //      RatingBar dbRatingBar = (RatingBar) getView().findViewById(R.id.benefitdbRating);
  //     dbRatingBar.setRating(chosenApp.dbRating);
 
        return inflater.inflate(R.layout.activity_costbenefit_benefittab, container, false);
    }
 
    @Override
    public void onStart() {
       super.onStart();
   }
}