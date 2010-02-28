package eu.scy.scymapper.api;

/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 14:23:47
 */
public interface IConceptMapSelectionListener {
    /**
     * Gets invoked when the selected concept map changes in the concept map manager
     * @param manager the manager that is the source of the event
     * @param cmap the concept map that was selected
     */
	void selectionChanged(IConceptMapManager manager, IConceptMap cmap);
}
