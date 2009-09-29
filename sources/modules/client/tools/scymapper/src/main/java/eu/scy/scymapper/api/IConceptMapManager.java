package eu.scy.scymapper.api;

import java.util.List;

/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 14:22:00
 */
public interface IConceptMapManager {
	void add(IConceptMap cmap);

	void remove(IConceptMap cmap);

	List<IConceptMap> getAll();

	void setSelected(IConceptMap cmap);

	IConceptMap getSelected();

	void addSelectionChangeListener(IConceptMapSelectionChangeListener listener);

	void removeChangeListener(IConceptMapSelectionChangeListener listener);

	void notifySelectionChange();
}