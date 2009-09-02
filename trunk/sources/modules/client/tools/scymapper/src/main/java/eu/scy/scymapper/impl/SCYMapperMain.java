package eu.scy.scymapper.impl;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.datasync.client.IDataSyncService;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.impl.component.ConceptDiagramView;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import roolo.elo.api.IMetadataKey;

import javax.swing.*;
import java.awt.*;

/**
 * User: Bjoerge
 * Date: 27.aug.2009
 * Time: 13:29:56
 */
public class SCYMapperMain extends JFrame {
	//private JXTaskPaneContainer taskPaneContainer;
	private JTabbedPane tabPane;
	private IDiagramModel diagramModel;
	private ConceptDiagramView diagramView;
	private DiagramController diagramController;
	private JToolBar toolBar;
	private IDataSyncService dataSyncService;
	private CommunicationProperties props = new CommunicationProperties();
	private String currentSession;
	private IActionLogger actionLogger;
	private JTextArea messages;
	private ICollaborationService collaborationService;
    private final static Logger logger = Logger.getLogger(SCYMapperMain.class);
	private IAwarenessService awarenessService;
	private static final String HARD_CODED_USER_NAME = "obama";
	private static final String HARD_CODED_PASSWORD = "obama";
	private ToolBrokerAPI<IMetadataKey> toolBroker;

	public static void main(String[] args) {
		new SCYMapperMain().setVisible(true);
	}

	public SCYMapperMain() throws HeadlessException {
		super("SCYMapper");
		setUpToolBroker();
		initComponents();
		initMenu();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 900);
	}

	private void setUpToolBroker() {
		toolBroker = new ToolBrokerImpl<IMetadataKey>();
		awarenessService = toolBroker.getAwarenessService();
		awarenessService.init(toolBroker.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
		try {
			logger.debug("Is connected: " +awarenessService.isConnected());
			logger.info("my buddies: " + awarenessService.getBuddies());
			awarenessService.sendMessage("bjoerge@129.177.24.191", "Hi, this is Obama!");
			awarenessService.setStatus("Yes, I can");
		} catch (AwarenessServiceException e) {
			logger.error("Awareness service error", e);
		}
		awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
			@Override
			public void handleAwarenessMessageEvent(IAwarenessEvent awarenessEvent) {
				messages.append(awarenessEvent.getUser()+" says:\n");
				messages.append(awarenessEvent.getMessage()+"\n\n");
			}
		});
	}

	private void initMenu() {

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu();

		JMenuItem exit = new JMenuItem("Exit");

		menu.add(exit);

		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	private void initComponents() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			LookAndFeelAddons.setAddon(MetalLookAndFeelAddons.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (InstantiationException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (IllegalAccessException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		// Tab pane
		tabPane = new JTabbedPane();
		getContentPane().add(tabPane);

		// Diagram
		diagramModel = new DiagramModel();
		diagramView = new ConceptDiagramView(new DiagramController(diagramModel), diagramModel);
		tabPane.add("SCYMapper", diagramView);

		// Message log
		messages = new JTextArea();
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.add(messages);
		tabPane.add("Recieved messages", messagePanel);
	}
}
