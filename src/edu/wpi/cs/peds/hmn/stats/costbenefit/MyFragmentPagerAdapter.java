package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Zhenhao Lei, zlei@wpi.edu
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 4;

	/** Constructor of the class */
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) {
		Bundle data = new Bundle();
		switch (arg0) {
		/** Cost tab is selected */
		case 0:
			MyCostFragment myCostFragment = new MyCostFragment();
			data.putInt("current_page", arg0 + 1);
			myCostFragment.setArguments(data);
			return myCostFragment;
		case 1:
			OverallCostFragment overallCostFragment = new OverallCostFragment();
			data.putInt("current_page", arg0 + 1);
			overallCostFragment.setArguments(data);
			return overallCostFragment;
		case 2:
			MyBenefitFragment myBenefitFragment = new MyBenefitFragment();
			data.putInt("current_page", arg0 + 1);
			myBenefitFragment.setArguments(data);
			return myBenefitFragment;
		case 3:
			OverallBenefitFragment overallBenefitFragment = new OverallBenefitFragment();
			data.putInt("current_page", arg0 + 1);
			overallBenefitFragment.setArguments(data);
			return overallBenefitFragment;
		}
		return null;
	}

	/** Returns the number of pages */
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
}