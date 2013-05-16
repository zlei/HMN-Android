/**
 * Used to register a class as an observer, and to record changes to the
 * observed object.
 */
package edu.wpi.cs.peds.hmn.appstatviewer;

/**
 * @author Richard Brown, rpb111@wpi.edu
 * 
 */
public interface IObserver {
	/**
	 * This method is called whenever the observed object is changed.
	 * 
	 * @param O
	 *            The IObservable which has changed.
	 * @param arg
	 *            An Object relating in some manner to the changes of the
	 *            IObservable.
	 */
	void update(IObservable O, Object arg);
}