package eu.scy.scymapper.impl;

import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.event.*;
import eu.scy.scymapper.impl.ui.awareness.BuddyPane;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.builder.PanelBuilder;

import java.awt.*;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * User: Bjoerge Naess
 * Date: 02.sep.2009
 * Time: 15:31:09
 */
public class AwarenessView extends JPanel implements IAwarenessMessageListener, IAwarenessRosterListener, IAwarenessPresenceListener {
	private IAwarenessService awarenessService;
	private JTabbedPane chatPane;
	private JPanel buddiesPane;
	private JPanel statusPane;
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
		chatPane.add("Your buddies", new BuddyPane(awarenessService));

		statusPane = new PresenceStatusPane();
		statusPane.setBorder(BorderFactory.createEmptyBorder());

		FormLayout layout = new FormLayout(
				"default:grow",
				"pref, 2dlu, 75dlu, 2dlu, pref:grow"
		);

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		builder.addSeparator("Status", cc.xy(1, 1));
		builder.add(statusPane, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(chatPane, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.FILL));

		add(builder.getPanel());
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

	class PresenceStatusPane extends JPanel {

		PresenceStatusPane() {
			this.initComponents();
		}

		private void initComponents() {
			setBorder(BorderFactory.createEmptyBorder());
			setLayout(new BorderLayout());
			FormLayout layout = new FormLayout(
					"left:pref, 4dlu, left:pref:grow",
					"pref, 2dlu, pref"
			);

			PanelBuilder builder = new PanelBuilder(layout);
			CellConstraints cc = new CellConstraints();

			builder.add(new JLabel("Username"), cc.xy(1, 1));
			builder.add(new JLabel("obama"), cc.xy(3, 1));

			add(builder.getPanel());
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
