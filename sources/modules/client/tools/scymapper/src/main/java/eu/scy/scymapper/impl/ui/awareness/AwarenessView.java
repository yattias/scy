package eu.scy.scymapper.impl.ui.awareness;

import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.*;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * User: Bjoerge Naess
 * Date: 02.sep.2009
 * Time: 15:31:09
 */
public class AwarenessView extends JPanel implements IAwarenessMessageListener, IAwarenessRosterListener, IAwarenessPresenceListener {
	private XMPPConnection connection;
	private IAwarenessService awarenessService;
	private JTabbedPane chatPane;
	private JPanel buddiesPane;
	private JPanel statusPane;
	private JLabel connectionStatus;
	private JList buddyListComponent;
	private final static Logger logger = Logger.getLogger(AwarenessView.class);

	public AwarenessView(IAwarenessService awarenessService) {
		this.awarenessService = awarenessService;
		initComponents();
		setupListeners();
	}

	private void initComponents() {

		setLayout(new BorderLayout());
		setBorder(new TitledBorder("Awareness"));
		chatPane = new JTabbedPane();
		chatPane.add("Collaborators", new BuddyPane(awarenessService));

		statusPane = new PresenceStatusPane(awarenessService);
		statusPane.setBorder(BorderFactory.createEmptyBorder());
	}

	private void setupListeners() {
		awarenessService.addAwarenessMessageListener(this);
		awarenessService.addAwarenessPresenceListener(this);
		awarenessService.addAwarenessRosterListener(this);
	}

	@Override
	public void handleAwarenessMessageEvent(IAwarenessEvent awarenessEvent) {
		updateStatus();
	}

	@Override
	public void handleAwarenessRosterEvent(IAwarenessRosterEvent e) {
		updateStatus();
	}

	@Override
	public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
		updateStatus();
	}

	void updateStatus() {
		logger.info("updateStatus!");
	}

	class PresenceStatusPane extends JPanel implements IAwarenessMessageListener, IAwarenessPresenceListener {

		private JLabel connectionStatus;
		private JLabel userName;

		PresenceStatusPane(IAwarenessService awarenessService) {
			initComponents();
			awarenessService.addAwarenessMessageListener(this);
			awarenessService.addAwarenessPresenceListener(this);
		}

		private void initComponents() {
			setBorder(BorderFactory.createEmptyBorder());
			setLayout(new BorderLayout());

			connectionStatus = new JLabel(awarenessService.isConnected() ? "Online" : "Offline");
			userName = new JLabel("");

		}

		@Override
		public void handleAwarenessMessageEvent(IAwarenessEvent awarenessEvent) {

		}

		@Override
		public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
			connectionStatus.setText(e.getPresence());
			userName.setText(e.getUser());
		}
	}

	static class BuddyListCellRenderer extends JLabel implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			IAwarenessUser user = (IAwarenessUser) value;
			setText(user.getName());
			return this;
		}
	}
}
