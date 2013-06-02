package edu.wpi.cs.peds.hmn.stats.costbenefit;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.appdetailviewer.ApplicationDetailViewActivity;

/**
 * @author Zhenhao Lei, zlei@wpi.edu
 */
 
public class CostBenefitActivity extends FragmentActivity {
    ActionBar mActionBar;
    ViewPager mPager;

    int currentUid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costbenefit);
        currentUid = ApplicationDetailViewActivity.uid;

        /** Getting a reference to action bar of this activity */
        mActionBar = getActionBar();
 
        /** Set tab navigation mode */
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        /** Getting a reference to ViewPager from the layout */
        mPager = (ViewPager) findViewById(R.id.pager);
 
        /** Getting a reference to FragmentManager */
        FragmentManager fm = getSupportFragmentManager();
 
        /** Defining a listener for pageChange */
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionBar.setSelectedNavigationItem(position);
            }
        };
 
        /** Setting the pageChange listner to the viewPager */
        mPager.setOnPageChangeListener(pageChangeListener);
 
        /** Creating an instance of FragmentPagerAdapter */
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(fm);
 
        /** Setting the FragmentPagerAdapter object to the viewPager object */
        mPager.setAdapter(fragmentPagerAdapter);
 
        mActionBar.setDisplayShowTitleEnabled(true);
 
        /** Defining tab listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
 
			@Override
			public void onTabSelected(Tab tab,
					android.app.FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab,
					android.app.FragmentTransaction ft) {
			}

			@Override
			public void onTabReselected(Tab tab,
					android.app.FragmentTransaction ft) {
			}
        };
 
        /** Creating Android Tab */
        Tab tab = mActionBar.newTab()
                .setText("Cost")
                .setIcon(R.drawable.cost_fragment)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
 
        /** Creating Apple Tab */
        tab = mActionBar.newTab()
                .setText("Benefit")
                .setIcon(R.drawable.benefit_fragment)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
 
    }
}