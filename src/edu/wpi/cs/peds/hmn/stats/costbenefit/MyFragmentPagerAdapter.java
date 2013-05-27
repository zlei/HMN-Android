package edu.wpi.cs.peds.hmn.stats.costbenefit;

 
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
 
    final int PAGE_COUNT = 2;
 
    /** Constructor of the class */
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){
            /** Cost tab is selected */
            case 0:
                CostFragment costFragment = new CostFragment();
                data.putInt("current_page", arg0+1);
                costFragment.setArguments(data);
                return costFragment;
 
            /** Benefit tab is selected */
            case 1:
                BenefitFragment benefitFragment = new BenefitFragment();
                data.putInt("current_page", arg0+1);
                benefitFragment.setArguments(data);
                return benefitFragment;
        }
        return null;
    }
 
    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}