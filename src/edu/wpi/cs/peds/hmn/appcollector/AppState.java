/**
 * Defines the possible states an installed app may be in, as well as how to
 * emit that name as JSON.
 */

package edu.wpi.cs.peds.hmn.appcollector;

import java.util.Locale;

/**
 * Possible app states, along with their label.
 * 
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 */
public enum AppState {
	ACTIVE("Active"), // currently being viewed by the user
	RUNNING("Running"), // running, no matter the state
	FOREGROUND("Foreground"), // minimized
	BACKGROUND("Background"), // running in the background
	CACHED("Cached"), // not running, but can still receive signals from the
						// system. This state is defined by Android.
	NOTRUNNING("Not running"); // not running in any capacity

	private final String name;

	private AppState(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public String toJSONName() {
		return this.name().toLowerCase(Locale.ENGLISH);
	}
}
