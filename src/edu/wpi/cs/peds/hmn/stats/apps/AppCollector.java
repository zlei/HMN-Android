package edu.wpi.cs.peds.hmn.stats.apps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wpi.cs.peds.hmn.appstatviewer.sorting.SortingComparators;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 * 
 */
public class AppCollector
{
	/**
	 * Constructs a list of all apps installed on the device.
	 * 
	 * @param packageManager used to query the device
	 * @return a list of Application objects representing all installed apps.
	 */
	public static List<Application> getAllApps(PackageManager packageManager)
	{
		List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
		List<Application> appList = appListFromPackageList(packageInfoList, packageManager);
		return sortedApplicationList(appList);
	}
	
	/**
	 * Constructs a list of all apps in the active state. That is, displayed to
	 * the user.
	 * Note, this list should only ever contain a single app.
	 * 
	 * @param packageManager used to query the device
	 * @param activityManager aids in determining app state
	 * @return a list of Application objects representing all active apps.
	 */
	public static List<Application> getActive(PackageManager packageManager, ActivityManager activityManager)
	{
		RunningTaskInfo taskInfo = activityManager.getRunningTasks(1).get(0);
		ComponentName activeAppComponent = taskInfo.topActivity;
		PackageInfo activeAppPackage = getPackageInfo(activeAppComponent.getPackageName(),packageManager,activityManager);
		Application app = appFromPackage(activeAppPackage,packageManager);
		return Arrays.asList(app);
	}
	
	/**
	 * Constructs a list of all running apps on the device.
	 * 
	 * @param packageManager used to query the device
	 * @param activityManager aids in determining app state
	 * @return a list of Application objects representing all running apps.
	 */
	public static List<Application> getRunningApps(PackageManager packageManager, ActivityManager activityManager)
	{
		Set<String> packageNames = new HashSet<String>();
		for (RunningAppProcessInfo process : activityManager.getRunningAppProcesses())
			packageNames.addAll(Arrays.asList(process.pkgList));
		List<PackageInfo> packageInfoList = getPackageInfoList(new ArrayList<String>(packageNames), packageManager, activityManager);
		List<Application> appList = appListFromPackageList(packageInfoList, packageManager);
		return sortedApplicationList(appList);
	}
	
	/**
	 * Constructs a list of all foreground apps on the device. That is, all
	 * minimized apps.
	 * 
	 * @param packageManager used to query the device
	 * @param activityManager aids in determining app state
	 * @return a list of Application objects representing all foreground apps.
	 */
	public static List<Application> getForegroundApps(PackageManager packageManager, ActivityManager activityManager)
	{
		Set<String> packageNames = new HashSet<String>();
		for (RunningTaskInfo task : activityManager.getRunningTasks(1000))
			packageNames.add(task.baseActivity.getPackageName());
		List<PackageInfo> packageInfoList = getPackageInfoList(new ArrayList<String>(packageNames), packageManager, activityManager);
		List<Application> appList = appListFromPackageList(packageInfoList, packageManager);
		return sortedApplicationList(appList);
	}
	
	/**
	 * Constructs a list of all background apps on the device.
	 * 
	 * @param packageManager
	 * @param activityManager
	 * @return
	 */
	public static List<Application> getBackgroundApps(PackageManager packageManager, ActivityManager activityManager)
	{
		List<Application> foreground = getForegroundApps(packageManager, activityManager);
		List<Application> cached = getCachedApps(packageManager, activityManager);

		List<Application> background = getRunningApps(packageManager, activityManager);
		background.removeAll(foreground);
		background.removeAll(cached);
		
		return background;
	}
	
	/**
	 * Constructs a list of all cached apps on the device. That is, apps that
	 * are stopped but can still receive signals.
	 * 
	 * @param packageManager used to query the device
	 * @param activityManager aids in determining app state
	 * @return a list of Application objects representing all cached apps.
	 */
	public static List<Application> getCachedApps(PackageManager packageManager, ActivityManager activityManager)
	{
		Set<String> packageNames = new HashSet<String>();
		for (RunningAppProcessInfo process : activityManager.getRunningAppProcesses())
			if (process.importance==ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
				packageNames.addAll(Arrays.asList(process.pkgList));
		List<PackageInfo> packageInfoList = getPackageInfoList(new ArrayList<String>(packageNames), packageManager, activityManager);
		List<Application> appList = appListFromPackageList(packageInfoList, packageManager);
		return sortedApplicationList(appList);
	}
	
	
	/**
	 * Sorts the given app list in alphabetical order.
	 * 
	 * @param appList a list of applications
	 * @return the sorted list of applications
	 */
	private static List<Application> sortedApplicationList(List<Application> appList)
	{
		List<Application> sortedApps = new ArrayList<Application>(appList);
		Collections.sort(sortedApps,new SortingComparators.SortByNameAtoZ());
		return sortedApps;
	}
	
	/**
	 * Finds each package in the package list and gets its associated info.
	 * 
	 * @param packageNames the list of packages to look up
	 * @param packageManager used to query the device
	 * @param activityManager aids in determining app state
	 * @return the associated package info
	 */
	private static List<PackageInfo> getPackageInfoList(List<String> packageNames, PackageManager packageManager, ActivityManager activityManager)
	{
		List<PackageInfo> packageInfoList = new ArrayList<PackageInfo>();
		for (String packageName : packageNames)
			packageInfoList.add(getPackageInfo(packageName, packageManager, activityManager));
		return packageInfoList;
	}
	
	/**
	 * Converts each PackageInfo object into an Application object representing
	 * the corresponding app.
	 * 
	 * @param packageInfoList a list of packages
	 * @param packageManager used to find each package's human-readable name
	 * @return each PackageInfo converted into an Application
	 */
	private static List<Application> appListFromPackageList(List<PackageInfo> packageInfoList, PackageManager packageManager)
	{
		HashMap<String,Application> appsByName = new HashMap<String,Application>();
		
		for (PackageInfo packageInfo : packageInfoList)
		{
			Application app = appFromPackage(packageInfo, packageManager);
			String name = app.getName();
			if (!appsByName.containsKey(name))
				appsByName.put(name, app);
		}
		
		return new ArrayList<Application>(appsByName.values());
	}
	
	/**
	 * Constructs an Application object representing the app referenced by the
	 * PackageInfo
	 * 
	 * @param packageInfo the package corresponding to the app
	 * @param packageManager used in determining the apps human-readable name
	 * @return an Application representing the given package info
	 */
	private static Application appFromPackage(PackageInfo packageInfo, PackageManager packageManager)
	{
		ApplicationInfo appInfo = packageInfo.applicationInfo;
		String appName = appInfo.loadLabel(packageManager).toString();
		String packageName = packageInfo.packageName;
		String version = packageInfo.versionName;
		int uid = appInfo.uid;
		Application app = new Application(appName, packageName, uid, version);
		return app;
	}
	
	
	/**
	 * Attempts to retrieve info on the named package.
	 * 
	 * @param packageName
	 * @param packageManager used to query the device
	 * @param activityManager aids in determining app state
	 * @return information about the package, unless an error occurs, in which
	 * case null is returned
	 */
	private static PackageInfo getPackageInfo(String packageName, PackageManager packageManager, ActivityManager activityManager)
	{
		try
		{
			return packageManager.getPackageInfo(packageName, 0);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return null;
		}
	}
}
