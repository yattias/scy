package eu.scy.scymapper.impl;

import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.notification.Notification;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.impl.controller.datasync.DataSyncDiagramController;
import eu.scy.scymapper.impl.controller.datasync.DataSyncElementControllerFactory;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.SlideNotificator;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.palette.PalettePane;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * User: Bjoerge
 * Date: 27.aug.2009
 * Time: 13:29:56
 */
public class SCYMapperPanel extends JPanel implements ISyncListener {

	private final static Logger logger = Logger.getLogger(SCYMapperPanel.class);

	private ToolBrokerAPI toolBroker;
	private JSplitPane splitPane;
	private IConceptMap conceptMap;
	private ISCYMapperToolConfiguration configuration;
	private ConceptDiagramView conceptDiagramView;
	private JTextField sessionID;
	private ISyncSession currentSession;
	private SlideNotificator notificator;

	public SCYMapperPanel(IConceptMap cmap, ISCYMapperToolConfiguration configuration) {
		conceptMap = cmap;
		this.configuration = configuration;
		setLayout(new BorderLayout());
		initComponents();
		validate();
	}

	public void setToolBroker(ToolBrokerAPI tbi) {
		toolBroker = tbi;
		if (toolBroker != null) {
			toolBroker.registerForNotifications(new INotifiable() {
				@Override
				public void processNotification(INotification notification) {
					showNotification(notification);
				}
			});
		}
	}

	public void joinSession(ISyncSession session) {
		conceptDiagramView.setController(new DataSyncDiagramController(conceptMap.getDiagram(), session));
		conceptDiagramView.setElementControllerFactory(new DataSyncElementControllerFactory(session));
	}

	private void showNotification(INotification notification) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());
		Icon icon = UIManager.getIcon("OptionPane.informationIcon");
		JLabel label = new JLabel("Notification recieved", icon, SwingConstants.LEFT);
		panel.add(BorderLayout.NORTH, label);

		StringBuilder sb = new StringBuilder();

		sb.append("--- NOTIFICATION ---").append("\n\n").
			append("ToolID:	").append(notification.getToolId()).append("\n").
			append("Mission:	").append(notification.getMission()).append("\n").
			append("Sender:	").append(notification.getSender()).append("\n").
			append("UserId:	").append(notification.getUserId()).append("\n").
			append("ToolId:	").append(notification.getToolId()).append("\n").
			append("Session:	").append(notification.getSession()).append("\n").
			append("Properties --------------\n");

		for (Map.Entry<String, String> entry : notification.getProperties().entrySet()) {
			sb.append("  ").append(entry.getKey()).append(":	").append(entry.getValue()).append("\n");
		}

		JTextArea textArea = new JTextArea(sb.toString());
		panel.add(BorderLayout.CENTER, textArea);

		panel.setSize(400, 350);
		if (notificator != null) notificator.hide();
		notificator = new SlideNotificator(SCYMapperPanel.this, panel);

		final SlideNotificator n = notificator;
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				n.hide();
			}
		});

		JPanel btnPanel = new JPanel();
		btnPanel.add(close);
		panel.add(BorderLayout.SOUTH, btnPanel);

		notificator.show();
	}

	private void initComponents() {

		JPanel sessionPanel = new JPanel();
		//sessionPanel.setBorder(BorderFactory.createTitledBorder("Session status"));
		sessionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		sessionPanel.add(new JLabel("Session: "));
		sessionID = new JTextField("No session");
		sessionID.setEditable(false);

		JButton createSessionButton = new JButton("Create session");
		createSessionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createSession();
			}
		});
		JButton joinSessionButton = new JButton("Join session");
		joinSessionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				joinSession();
			}
		});
		sessionPanel.add(sessionID);
		sessionPanel.add(createSessionButton);
		sessionPanel.add(joinSessionButton);

		JButton makeNotificationButton = new JButton("Create dummy notification");
		makeNotificationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				INotification notification = new Notification();
				notification.setToolId("scymapper");
				notification.setSender("obama");
				notification.addProperty("dummyProperty", "DummyValue");
				showNotification(notification);
			}
		});
		sessionPanel.add(makeNotificationButton);

		JButton showNotificationButton = new JButton("Show notification");
		showNotificationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notificator.show();
			}
		});
		sessionPanel.add(showNotificationButton);
		JButton hideNotificationButton = new JButton("Hide notification");
		hideNotificationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notificator.hide();
			}
		});
		sessionPanel.add(hideNotificationButton);

		add(BorderLayout.NORTH, sessionPanel);

		ConceptMapPanel cmapPanel = new ConceptMapPanel(conceptMap);
		cmapPanel.setBackground(Color.WHITE);
		conceptDiagramView = cmapPanel.getDiagramView();

		JPanel palettePane = new PalettePane(conceptMap, configuration, cmapPanel);
		palettePane.setPreferredSize(new Dimension(120, 0));
		palettePane.setSize(palettePane.getPreferredSize());
		palettePane.setMinimumSize(palettePane.getPreferredSize());

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, palettePane, cmapPanel);

		add(splitPane, BorderLayout.CENTER);

	}

	public IConceptMap getConceptMap() {
		return conceptMap;
	}

	public void setConceptMap(IConceptMap conceptMap) {
		removeAll();
		this.conceptMap = conceptMap;
		initComponents();
		repaint();
	}

	@Override
	public void syncObjectAdded(ISyncObject syncObject) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void syncObjectChanged(ISyncObject syncObject) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void syncObjectRemoved(ISyncObject syncObject) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	private class CreateSessionAction extends AbstractAction {
		private CreateSessionAction() {
			super("Create and join session");
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			if (toolBroker == null) {
				JOptionPane.showMessageDialog(SCYMapperPanel.this, "Please login first", "Not logged in", JOptionPane.ERROR_MESSAGE);
				return;
			}
			System.out.println("CREATING SESSION");
			createSession();
			displaySessionId();

		}
	}

	private void displaySessionId() {
		sessionID.setText(currentSession.getId());
	}

	private class ShowSessionIDAction extends AbstractAction {
		private ShowSessionIDAction() {
			super("Display session ID");
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			displaySessionId();
		}
	}

	private class JoinSessionAction extends AbstractAction {
		private JoinSessionAction() {
			super("Join session");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (toolBroker == null) {
				JOptionPane.showMessageDialog(SCYMapperPanel.this, "Please login first", "Not logged in", JOptionPane.ERROR_MESSAGE);
				return;
			}
			joinSession();
		}
	}


	private void joinSession() {
		String sessId = JOptionPane.showInputDialog("Enter session ID");

		if (sessId != null) {
			currentSession = toolBroker.getDataSyncService().joinSession(sessId, SCYMapperPanel.this);
			joinSession(currentSession);
			sessionID.setText(sessId);
		}
	}

	private void createSession() {
		if (toolBroker == null)
			JOptionPane.showMessageDialog(this, "Error: ToolBroker is null", "Error", JOptionPane.ERROR_MESSAGE);
		try {
			currentSession = toolBroker.getDataSyncService().createSession(SCYMapperPanel.this);
			joinSession(currentSession);
			displaySessionId();
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
