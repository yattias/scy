package eu.scy.scymapper.impl.ui.awareness;

import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.event.IAwarenessRosterEvent;

import javax.swing.*;
import java.util.List;
import java.awt.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.builder.PanelBuilder;
import org.apache.log4j.Logger;

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

		FormLayout layout = new FormLayout(
				"pref:grow",
				"pref, 2dlu, pref:grow"
		);
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		builder.add(toolBar, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(buddyList, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		add(builder.getPanel());
	}

	private void updateBuddyList() {
		try {
			List<IAwarenessUser> buddies = this.awarenessService.getBuddies();
			logger.info("Buddy list is now " + buddies);
			buddyList.setModel(new BuddyListModel(buddies));
			buddyList.repaint();
		} catch (AwarenessServiceException e) {
			logger.error("Exception when getting buddies", e);
		}
	}

	@Override
	public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
		updateBuddyList();
	}

	@Override
	public void handleAwarenessRosterEvent(IAwarenessRosterEvent e) {
		logger.debug("RosterEvent: " + e.getMessage());
		updateBuddyList();
	}
}
