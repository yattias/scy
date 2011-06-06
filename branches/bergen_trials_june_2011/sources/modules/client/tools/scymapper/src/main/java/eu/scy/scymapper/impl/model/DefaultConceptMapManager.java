package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.IConceptMapManager;
import eu.scy.scymapper.api.IConceptMapManagerListener;
import eu.scy.scymapper.api.IConceptMapSelectionListener;

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
	private List<IConceptMapSelectionListener> selectionListeners = new ArrayList<IConceptMapSelectionListener>();
	private List<IConceptMapManagerListener> managerListeners = new ArrayList<IConceptMapManagerListener>();
	private static IConceptMapManager instance;

	public static IConceptMapManager getInstance() {
		if (instance == null)
			instance = new DefaultConceptMapManager();
		return instance;
	}

	private DefaultConceptMapManager() {}

	@Override
	public void add(IConceptMap cmap) {
		conceptMaps.add(cmap);
		notifyConceptMapAdded(cmap);
	}

	@Override
	public void remove(IConceptMap cmap) {
		conceptMaps.remove(cmap);
		notifyConceptMapRemoved(cmap);
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
	public void addSelectionChangeListener(IConceptMapSelectionListener listener) {
		selectionListeners.add(listener);
	}

	@Override
	public void removeSelectionChangeListener(IConceptMapSelectionListener listener) {
		selectionListeners.remove(listener);
	}

	@Override
	public void addConceptMapManagerListener(IConceptMapManagerListener listener) {
		managerListeners.add(listener);
	}

	@Override
	public void removeConceptMapManagerListener(IConceptMapManagerListener listener) {
		managerListeners.remove(listener);
	}

	@Override
	public void notifyConceptMapAdded(IConceptMap cmap) {
		for (IConceptMapManagerListener listener : managerListeners) {
			listener.conceptMapAdded(this, cmap);
		}
	}

	@Override
	public void notifyConceptMapRemoved(IConceptMap cmap) {
		for (IConceptMapManagerListener listener : managerListeners) {
			listener.conceptMapRemoved(this, cmap);
		}
	}

	@Override
	public void notifySelectionChange() {
		for (IConceptMapSelectionListener listener : selectionListeners) {
			listener.selectionChanged(this, getSelected());
		}
	}
}
