package edu.wpi.cs.peds.hmn.stats.costbenefit;

import java.util.Random;

import edu.wpi.cs.peds.hmn.appdetailviewer.ApplicationDetailViewActivity;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

public class MockData {
	private static Application chosenApp = null;
	private static int  currentUid;
	public static Point getDataFromReceiver(int x){
		return new Point(x, generateRandomData());
	}
	private static int generateRandomData(){
		currentUid = ApplicationDetailViewActivity.uid;
		chosenApp = GlobalAppList.getInstance().getApp(currentUid);
		float y = chosenApp.networkMonitorInfo();
		Random random = new Random();
		return random.nextInt(40);
	}
}
