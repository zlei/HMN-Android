/**
 * Created Aug 3, 2012
 * File AppInfoFinderTest.java for
 */
package edu.wpi.cs.peds.hmn.app.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.peds.hmn.app.Application;
import edu.wpi.cs.peds.hmn.app.components.ApplicationComponent;
import edu.wpi.cs.peds.hmn.app.components.StringComponent;
import edu.wpi.cs.peds.hmn.app.components.WebComponent;
import edu.wpi.cs.peds.hmn.app.components.WebComponent.Components;

/**
 * @author Richard Brown, rpb111@wpi.edu
 * 
 */
public class AppInfoFinderTest {
	Application app;

	@Before
	public void setup() {
		app = new Application();
		ApplicationComponent<Object> appName = new StringComponent(app,
				"app-name", "Firefox");
		ApplicationComponent<Object> appPackage = new StringComponent(app,
				"package-name", "org.mozilla.firefox");
		app.addComponent(appName);
		app.addComponent(appPackage);
	}

	@Test
	public void testGetNumDownloads() {
		ApplicationComponent<Object> downloads = new WebComponent(app,
				Components.DOWNLOADS);
		assertEquals(Long.class, downloads.getComponentValue().getClass());
	}

	@Test
	public void testGetNumReviews() {
		ApplicationComponent<Object> numReviews = new WebComponent(app,
				Components.REVIEWCOUNT);
		assertEquals(Long.class, numReviews.getComponentValue().getClass());
	}

	@Test
	public void testGetRating() {
		ApplicationComponent<Object> rating = new WebComponent(app,
				Components.RATING);
		assertEquals(Double.class, rating.getComponentValue().getClass());
	}

	@Test
	public void testGetVersion() {
		ApplicationComponent<Object> version = new WebComponent(app,
				Components.VERSION);
		assertEquals(String.class, version.getComponentValue().getClass());
	}

	@Test
	public void testGetVersionDate() {
		ApplicationComponent<Object> date = new WebComponent(app,
				Components.UPDATEDATE);
		assertEquals(Date.class, date.getComponentValue().getClass());
	}
}
