package edu.wpi.cs.peds.hmn.appstatviewer;


/**
 * @author Richard Brown, rpb111@wpi.edu
 * 
 * An Observable-style interface. Contains many methods from the java Observable
 * class.
 * 
 */
public interface IObservable {
	/**
	 * Adds an IObserver to the set of observers for this object, provided that
	 * it is not the same as some observer already in the set.
	 * 
	 * @param O
	 *            An IObserver to add to the IObservers observing this object.
	 */
	void addObserver(IObserver O);

	/**
	 * Returns the number of observers of this IObservable object.
	 * 
	 * @return An int representing the number of IObservers observing this
	 *         object.
	 */
	int countObservers();

	/**
	 * Deletes an IObserver from the set of observers of this object.
	 * 
	 * @param O
	 *            An IObserver to delete from the IObservers observing this
	 *            object.
	 */
	void deleteObserver(IObserver O);

	/**
	 * Clears the observer list so that this object no longer has any
	 * IObservers.
	 */
	void deleteObservers();

	/**
	 * If this object has changed, as indicated by the hasChanged method, then
	 * notify all of its observers and then call the clearChanged method to
	 * indicate that this object has no longer changed.
	 */
	void notifyObservers();

	/**
	 * If this object has changed, as indicated by the hasChanged method, then
	 * notify all of its observers and then call the clearChanged method to
	 * indicate that this object has no longer changed.
	 * 
	 * @param arg
	 *            The Object to be passed to the IObserver's update method.
	 */
	void notifyObservers(Object arg);
}