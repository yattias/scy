package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.IConceptMapManager;
import eu.scy.scymapper.api.IConceptMapSelectionChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Bjoerge Naess
 * Date: 29.sep.2009
 * Time: 14:49:00
 */
public class DefaultConceptMapManager implements IConceptMapManager {
	private List<IConceptMap> conceptMaps = new ArrayList<IConceptMap>();
	private IConceptMap selected;
	private List<IConceptMapSelectionChangeListener> listeners = new ArrayList<IConceptMapSelectionChangeListener>();

	@Override
	public void add(IConceptMap cmap) {
		conceptMaps.add(cmap);
	}

	@Override
	public void remove(IConceptMap cmap) {
		conceptMaps.remove(cmap);
	}

	@Override
	public List<IConceptMap> getAll() {
		return conceptMaps;
	}

	@Override
	public void setSelected(IConceptMap cmap) {
		selected = cmap;
		notifySelectionChange();
	}

	@Override
	public IConceptMap getSelected() {
		return selected;
	}

	@Override
	public void addSelectionChangeListener(IConceptMapSelectionChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeChangeListener(IConceptMapSelectionChangeListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void notifySelectionChange() {
		for (IConceptMapSelectionChangeListener listener : listeners) {
			listener.selectionChanged(this, getSelected());
		}
	}
}
