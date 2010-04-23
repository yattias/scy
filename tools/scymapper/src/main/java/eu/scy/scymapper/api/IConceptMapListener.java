package eu.scy.scymapper.api;

/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 14:18:31
 */
public interface IConceptMapListener {
    /**
     * Notifies listeners when the concept map is updated
     * @param cmap the concept map that has been updated
     */
	void conceptMapUpdated(IConceptMap cmap);
}
