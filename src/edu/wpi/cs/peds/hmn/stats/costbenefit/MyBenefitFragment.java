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

public class MyBenefitFragment extends Fragment{

	int aLaunchCount = 0;
	int popular = 12000;
    long aUseTime = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//       	Application chosenApp = null;
//   	CostBenefitActivity costuid = (CostBenefitActivity)getActivity();
//		chosenApp = GlobalAppList.getInstance().getApp(costuid.currentUid);
    	String ReviewDetail = reviewInfo();
    	String PopularityDetail = popularityInfo();
    	String LaunchCountDetail = launchCountInfo();
    	String UseTimeDetail = useTimeInfo();
    	View view = inflater.inflate(R.layout.activity_costbenefit_overallbenefittab, container, false);
//		appCount();
        TextView Rview = (TextView) view.findViewById(R.id.benefit_RatingDetail);
        TextView Pview = (TextView) view.findViewById(R.id.benefit_PopularityDetail);
        TextView Lview = (TextView) view.findViewById(R.id.benefit_LaunchCountDetail);
        TextView Uview = (TextView) view.findViewById(R.id.benefit_UseTimeDetail);
        Rview.setText(ReviewDetail);
        Pview.setText(PopularityDetail);
        Lview.setText(LaunchCountDetail);
        Uview.setText(UseTimeDetail);
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
     public String launchCountInfo() {
		StringBuilder appCostStr = new StringBuilder();
		appCostStr.append(String.format("Usage Count: %d", aLaunchCount));
		return appCostStr.toString();
	}
     public String useTimeInfo() {
		StringBuilder appCostStr = new StringBuilder();
		appCostStr.append(String.format("Used Time: %d", aUseTime));
		return appCostStr.toString();
	}
    
/*    public final void appCount() {
    	Application a;
    	CostBenefitActivity costuid = (CostBenefitActivity)getActivity();
		a = GlobalAppList.getInstance().getApp(costuid.currentUid);
 
    	ComponentName aName = new ComponentName(a.packageName, a.packageName);
		Log.i(HmnLog.HMN_LOG_TAG,"GET STARTED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + aName);
         try {
             //获得ServiceManager类
             Class<?> ServiceManager = Class
                .forName("android.os.ServiceManager");
             
             //获得ServiceManager的getService方法
             Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
             
             //调用getService获取RemoteService
             Object oRemoteService = getService.invoke(null, "usagestats");
             
             //获得IUsageStats.Stub类
             Class<?> cStub = Class
                .forName("com.android.internal.app.IUsageStats$Stub");
             //获得asInterface方法
             Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
             //调用asInterface方法获取IUsageStats对象
             Object oIUsageStats = asInterface.invoke(null, oRemoteService);
             //获得getPkgUsageStats(ComponentName)方法
             Method getPkgUsageStats = oIUsageStats.getClass().getMethod("getPkgUsageStats", ComponentName.class);
             //调用getPkgUsageStats 获取PkgUsageStats对象
             Object aStats = getPkgUsageStats.invoke(oIUsageStats, aName);
             
             //获得PkgUsageStats类
             Class<?> PkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");
             
             aLaunchCount = PkgUsageStats.getDeclaredField("launchCount").getInt(aStats);
             aUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(aStats);
             
             } catch (Exception e) {
              Log.e("###", e.toString(), e);
             }
    }
    */
}