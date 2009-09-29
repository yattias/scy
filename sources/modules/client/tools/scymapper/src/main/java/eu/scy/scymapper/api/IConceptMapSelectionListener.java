package eu.scy.scymapper.api;

/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 14:22:00
 */
public interface IConceptMapSelectionListener {
	void setSelected(IConceptMap cmap);
	IConceptMap getSelected();
	void addChangeListener(IConceptMapSelectionChangeListener listener);
}
