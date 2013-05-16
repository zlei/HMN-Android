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
public enum AppState
{
	ACTIVE("active"),			// currently being viewed by the user
	RUNNING("running"),			// running, no matter the state
	FOREGROUND("foreground"),	// minimized
	BACKGROUND("background"),	// running in the background
	CACHED("cached"),			// not running, but can still receive signals from the system. This state is defined by Android.
	NOTRUNNING("not running");	// not running in any capacity
	
	private final String name;
	
	private AppState(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}
	
	public String toJSONName()
	{
		return this.name().toLowerCase(Locale.ENGLISH);
	}
}
