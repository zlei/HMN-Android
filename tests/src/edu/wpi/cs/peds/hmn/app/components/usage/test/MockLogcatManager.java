/**
 * 
 */
package edu.wpi.cs.peds.hmn.app.components.usage.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.peds.hmn.app.components.usage.LogcatManager;

/**
 * @author richie
 * 
 */
public class MockLogcatManager extends LogcatManager {

	String logfilePath;

	public MockLogcatManager(String string) {
		super();
		this.logfilePath = string;
	}

	@Override
	public void initialize() {
		List<String> logEntries = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(
							logfilePath).getAbsolutePath())));
			for (String line; (line = reader.readLine()) != null;) {
				logEntries.add(line);
			}
			parseLogFile(logEntries);
		} catch (FileNotFoundException e) {
			/** Shouldn't happen in tests... */
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
