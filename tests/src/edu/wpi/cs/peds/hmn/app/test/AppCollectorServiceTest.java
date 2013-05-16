/**
 */
package edu.wpi.cs.peds.hmn.app.test;

import org.junit.Test;

import android.app.IntentService;
import android.test.ServiceTestCase;

/**
 * Class for testing the proper lifecycle of our collection service.
 * 
 * @param <AppCollectorService>
 *            The class under test
 */
public class AppCollectorServiceTest<AppCollectorService> extends
		ServiceTestCase<IntentService> {

	/**
	 * @param serviceClass
	 *            Class path of service class under test
	 */
	public AppCollectorServiceTest(Class serviceClass) {
		super(serviceClass);
	}

	/**
	 * The first bind to this service should create (start) it. This command
	 * should also call starCommand, so we check to ensure the service is
	 * handling these methods correctly and responding properly.
	 */
	@Test
	public void testFirstBindCallsOnCreateAndStartCommand() {
		fail("Not implemented.");
	}

	/**
	 * Test to ensure that the second bind only triggers a start command and not
	 * a on create.
	 */
	@Test
	public void testSecondBindCallsStartCommandOnly() {
		fail("not implemented.");
	}

	/**
	 * Ensure onCreate is called on initial bind
	 */
	@Test
	public void testServiceOnCreateCalled() {
		fail("Not implemented.");
	}

	/**
	 * Ensure the service is properly stopped / destroyed when the destroy
	 * method is called.
	 */
	@Test
	public void testServiceOnDestroyCalled() {
		fail("Not implemented.");
	}
}
