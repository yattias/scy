package eu.scy.scymapper.impl.ui.awareness;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: Bjoerge Naess
 * Date: 02.sep.2009
 * Time: 17:30:23
 */
public class BuddyPane extends JPanel implements IAwarenessPresenceListener, IAwarenessRosterListener {
	private IAwarenessService awarenessService;
	private JToolBar toolBar;
	private JList buddyList;
	private final static Logger logger = Logger.getLogger(BuddyPane.class);

	public BuddyPane(IAwarenessService awarenessService) {
		this.awarenessService = awarenessService;
		initComponents();
		updateBuddyList();
		setupListeners();
	}


	private void setupListeners() {
		awarenessService.addAwarenessPresenceListener(this);
		awarenessService.addAwarenessRosterListener(this);
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		buddyList = new JList();
		buddyList.setBorder(BorderFactory.createEtchedBorder());
		buddyList.setCellRenderer(new BuddyListCellRenderer());
		buddyList.setSelectionModel(new DefaultListSelectionModel());

		toolBar = new BuddyListToolbar(buddyList, this.awarenessService);
		toolBar.setFloatable(false);
	}

	private void updateBuddyList() {
		if (awarenessService.isConnected()) {
			try {
				List<IAwarenessUser> buddies = awarenessService.getBuddies();
				logger.info("Buddy list is now " + buddies);
				buddyList.setModel(new BuddyListModel(buddies));
				buddyList.repaint();
			} catch (AwarenessServiceException e) {
				logger.error("Exception when getting buddies", e);
			}
		}
	}

	@Override
	public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
		updateBuddyList();
	}

	@Override
	public void handleAwarenessRosterEvent(IAwarenessRosterEvent e) {
		updateBuddyList();
	}
}
