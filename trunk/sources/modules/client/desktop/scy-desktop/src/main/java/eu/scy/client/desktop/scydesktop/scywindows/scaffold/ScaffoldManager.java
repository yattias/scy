package eu.scy.client.desktop.scydesktop.scywindows.scaffold;

import eu.scy.actionlogging.SystemOutActionLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ScaffoldManager {

	private static final Logger logger = Logger.getLogger(ScaffoldManager.class.getName());
	private static ScaffoldManager scaffoldManager;
	private int scaffoldLevel;
	private List<IScaffoldChangeListener> listeners;
	public static final int SCAFFOLD_OFF = 0;
	public static final int SCAFFOLD_MEDIUM = 1;
	public static final int SCAFFOLD_HIGH = 2;

	//singleton pattern
	private ScaffoldManager() {
		// TODO store / retrieve level from runtime settings
		scaffoldLevel = SCAFFOLD_MEDIUM;
		listeners = Collections.synchronizedList(new ArrayList<IScaffoldChangeListener>());
	}

	public static ScaffoldManager getInstance() {
		if (scaffoldManager == null) {
			scaffoldManager = new ScaffoldManager();
		}
		return scaffoldManager;
	}

	public int getScaffoldLevel() {
		return this.scaffoldLevel;
	}

	public void setScaffoldLevel(int newLevel) {
		if (newLevel >= SCAFFOLD_OFF && newLevel <= SCAFFOLD_HIGH) {
			scaffoldLevel = newLevel;
			logger.info("setting new scaffold level to "+newLevel);
			notifyListeners();
		} else {
			logger.warning("scaffold level '"+newLevel+"' not available, doing nothing");
		}
	}

	public void setScaffoldLevel(String newLevelString) {
		try {
			int newLevel = Integer.parseInt(newLevelString);
			setScaffoldLevel(newLevel);
		} catch (NumberFormatException ex) {
			logger.warning("don't understand scaffold level '"+newLevelString+"', doing nothing");
		}
	}

	private void notifyListeners() {
		for (IScaffoldChangeListener listener: listeners) {
			try {
				listener.scaffoldLevelChanged(scaffoldLevel);
			} catch (Exception ex) {
				logger.warning("exception caught: "+ex.getMessage());
			}
		}
	}

	public int addScaffoldListener(IScaffoldChangeListener newListener) {
		listeners.add(newListener);
		return scaffoldLevel;
	}

	public void removeScaffoldListener(IScaffoldChangeListener listener) {
		listeners.remove(listener);
	}

}
