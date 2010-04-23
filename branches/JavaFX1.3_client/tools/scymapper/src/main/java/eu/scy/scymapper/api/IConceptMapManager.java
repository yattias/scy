package eu.scy.scymapper.api;

import java.util.List;

/**
 * The concept map manager keeps track of the different concept maps that are loaded in SCYMapper
 * It provides a single entry point to add listeners that will be notified when the user loads a new
 * concept map, or closes an already open one
 *
 * @author Bjoerge Naess
 *         Date: 29.sep.2009
 *         Time: 14:22:00
 */
public interface IConceptMapManager {

	/**
	 * Add a concept map to the concept map manager
	 *
	 * @param cmap the concept map to add
	 */
	void add(IConceptMap cmap);

	/**
	 * Remove a concept map from the concept map manager
	 *
	 * @param cmap
	 */
	void remove(IConceptMap cmap);

	/**
	 * Return all concept maps
	 *
	 * @return a list of all concept maps managed by this instance of the concept map manager
	 */
	List<IConceptMap> getAll();

	/**
	 * Set the current selected concept map. Will notify selection change to listeners.
	 *
	 * @param cmap the concept map to select
	 */
	void setSelected(IConceptMap cmap);

	/**
	 * Get the currently selected concept map
	 *
	 * @return the currently selected concept map
	 */
	IConceptMap getSelected();

	/**
	 * Allows clients to listen for selection changes
	 *
	 * @param listener the listener that will get notified about selection changes
	 */
	void addSelectionChangeListener(IConceptMapSelectionListener listener);

	/**
	 * Remove a selection change listener
	 *
	 * @param listener the listener to remove
	 */
	void removeSelectionChangeListener(IConceptMapSelectionListener listener);

	/**
	 * Adds a concept map manager listener
	 *
	 * @param listener The concept map manager listener to add
	 */
	void addConceptMapManagerListener(IConceptMapManagerListener listener);

	/**
	 * Removes a concept map manager listener
	 *
	 * @param listener the concept map manager listener to remove
	 */
	void removeConceptMapManagerListener(IConceptMapManagerListener listener);

	/**
	 * Ensure the implementation of this. Notifies listeners when a concept map is added
	 *
	 * @param cmap the added concept map
	 */
	void notifyConceptMapAdded(IConceptMap cmap);

	/**
	 * Ensure the implementation of this. Notifies listeners when a concept map is removed
	 *
	 * @param cmap the removed concept map
	 */
	void notifyConceptMapRemoved(IConceptMap cmap);

	/**
	 * Notify selection listeners when selection changes
	 */
	void notifySelectionChange();
}