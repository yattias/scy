package eu.scy.scymapper.api;

/**
 * User: Bjoerge Naess
 * Date: 30.sep.2009
 * Time: 12:28:46
 */
public interface IConceptMapManagerListener {
	void conceptMapAdded(IConceptMapManager manager, IConceptMap map);
	void conceptMapRemoved(IConceptMapManager manager, IConceptMap map);
}
