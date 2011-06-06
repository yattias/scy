package eu.scy.scymapper.api;

/**
 * User: Bjoerge Naess
 * Date: 30.sep.2009
 * Time: 12:28:46
 */

public interface IConceptMapManagerListener {
    /**
     * Gets invoked by the concept map manager when a new concept map is added
     * @param manager The manager the concept map was added to
     * @param map The concept map that was added
     */
	void conceptMapAdded(IConceptMapManager manager, IConceptMap map);

    /**
     * Gets invoked by the concept map manager when a new concept map is removed
     * @param manager The manager of which the concept map was removed
     * @param map The concept map that was removed
     */
	void conceptMapRemoved(IConceptMapManager manager, IConceptMap map);
}
