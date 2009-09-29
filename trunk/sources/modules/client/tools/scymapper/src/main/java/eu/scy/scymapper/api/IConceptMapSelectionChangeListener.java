package eu.scy.scymapper.api;

/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 14:23:47
 */
public interface IConceptMapSelectionChangeListener {
	void selectionChanged(IConceptMapManager manager, IConceptMap cmap);
	void conceptMapAdded(IConceptMapManager manager, IConceptMap map);
	void conceptMapRemoved(IConceptMapManager manager, IConceptMap map);
}
