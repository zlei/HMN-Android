package edu.wpi.cs.peds.hmn.app.components.usage.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Date;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.cs.peds.hmn.app.Application;
import edu.wpi.cs.peds.hmn.app.components.ApplicationComponent;
import edu.wpi.cs.peds.hmn.app.components.StringComponent;
import edu.wpi.cs.peds.hmn.app.components.usage.LogcatManager;
import edu.wpi.cs.peds.hmn.app.components.usage.LogcatManager.AppUsageStat;

public class LogcatManagerTest {
	private LogcatManager manager;
	private Application testApp;
	String nexus_log_file_name = "galaxy_nexus_4.1.1_20120807.logcat";
	String easy_nexus_log_file_name = "galaxy_nexus_4.1.1_shorttest.logcat";

	@Before
	public void setUp() throws Exception {

		manager = new MockLogcatManager("logs" + File.separator
				+ easy_nexus_log_file_name);
		testApp = new Application();

	}

	@Test
	@Ignore
	public void testGetLastOpenedDate() {
		ApplicationComponent<Object> name = new StringComponent(testApp,
				"name", "browser");
		ApplicationComponent<Object> packageName = new StringComponent(testApp,
				"package-name", "com.google.android.browser");
		testApp.addComponent(name);
		testApp.addComponent(packageName);
		assertEquals(Date.class, manager.getLastOpenedDate(testApp).getClass());
	}

	@Test
	@Ignore
	public void testGetNumberOfTicks() {
		ApplicationComponent<Object> name = new StringComponent(testApp,
				"name", "browser");
		ApplicationComponent<Object> packageName = new StringComponent(testApp,
				"package-name", "com.google.android.browser");
		testApp.addComponent(name);
		testApp.addComponent(packageName);
		assertEquals(Integer.class, manager.countUsageTicks(testApp).getClass());
		assertEquals(Integer.valueOf(4), manager.countUsageTicks(testApp));
	}

	@Test
	@Ignore
	public void testGetTotalForegroundTime() {
		ApplicationComponent<Object> name = new StringComponent(testApp,
				"name", "browser");
		ApplicationComponent<Object> packageName = new StringComponent(testApp,
				"package-name", "com.google.android.browser");
		testApp.addComponent(name);
		testApp.addComponent(packageName);
		assertEquals(Long.class, manager.calculateUsageTime(testApp).getClass());
		assertEquals(Long.valueOf(42780366),
				manager.calculateUsageTime(testApp));
	}

	/** Quick method to print out parsed log contents. */
	@Test
	public void testGetTotalForegroundTimeRealLog() {
		manager = new MockLogcatManager("logs" + File.separator
				+ nexus_log_file_name);
		manager.initialize();
		TreeMap<String, LogcatManager.AppUsageStat> appMap = manager
				.getAppMap();
		for (String packageName : appMap.keySet()) {
			AppUsageStat thisStat = appMap.get(packageName);
			System.out.println(packageName + " " + thisStat.getTicks() + " "
					+ thisStat.getFgTime() + " "
					+ thisStat.getLastSeenDateFmt());
		}
	}
}
