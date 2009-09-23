package eu.scy.scymapper.impl;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.LookUtils;
import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.scymapper.impl.ui.palette.PalettePane;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.shapes.concepts.Star;
import eu.scy.scymapper.impl.shapes.concepts.Ellipse;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.sessionmanager.SessionManager;
import eu.scy.datasync.client.IDataSyncService;
import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import org.apache.log4j.Logger;
import roolo.elo.api.IMetadataKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * User: Bjoerge
 * Date: 27.aug.2009
 * Time: 13:29:56
 */
public class SCYMapperMain extends JFrame implements IDataSyncListener, IDiagramModelObserver, INodeModelObserver {
	//private JXTaskPaneContainer taskPaneContainer;
	private JTabbedPane conceptMapTabPane;
	private IAwarenessService awarenessService;
	private ToolBrokerAPI<IMetadataKey> toolBroker;
	//init props
	private JTextArea logView;
	private final static Logger logger = Logger.getLogger(SCYMapperMain.class);
	private JToolBar toolBar;
	private SessionManager sessionManager;
	private String currentToolSessionId;
	private DiagramModel diagramModel;
	private IDataSyncService dataSyncService;

	public static void main(String[] args) {
		new SCYMapperMain().setVisible(true);
	}

	public SCYMapperMain() throws HeadlessException {
		super("SCYMapper");

		diagramModel = new DiagramModel();
		addSomeTestConcepts();
		diagramModel.addObserver(this);

		initLAF();
		initToolBroker();
		initComponents();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1300, 800);
	}

	private void initLAF() {
		UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
		Options.setDefaultIconSize(new Dimension(18, 18));

		String lafName = LookUtils.IS_OS_WINDOWS_XP
				? Options.getCrossPlatformLookAndFeelClassName() : Options.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lafName);
		} catch (Exception e) {
			System.err.println("Can't set look & feel:" + e);
		}
	}

	private void initToolBroker() {

		String username = JOptionPane.showInputDialog("Enter username");
		String passwd = JOptionPane.showInputDialog("Enter password");

		toolBroker = new ToolBrokerImpl<IMetadataKey>();
		awarenessService = toolBroker.getAwarenessService();
		awarenessService.init(toolBroker.getConnection(username, passwd));

		logger.debug("Getting collab-service");

		dataSyncService = toolBroker.getDataSyncService();
		dataSyncService.init(toolBroker.getConnection(username, passwd));
		try {
			logger.debug("Is connected: " + awarenessService.isConnected());
			logger.info("my buddies: " + awarenessService.getBuddies());
			//awarenessService.sendMessage("bjoerge@129.177.24.191", "Hi, this is Obama!");
			awarenessService.setStatus("Yes, I can");
		} catch (AwarenessServiceException e) {
			logger.error("Awareness service error", e);
		}
		awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
			@Override
			public void handleAwarenessMessageEvent(IAwarenessEvent awarenessEvent) {
				logView.append(awarenessEvent.getUser() + " says:\n");
				logView.append(awarenessEvent.getMessage() + "\n\n");
			}
		});

		dataSyncService.addDataSyncListener(this);
		dataSyncService.createSession("eu.scy.scymapper", "obama");
	}

	private void initMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		//JMenu editMenu = new JMenu("Edit");

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);

		menuBar.add(fileMenu);
		//menuBar.add(editMenu);

		setJMenuBar(menuBar);
	}

	private void initToolBar() {
		toolBar = new JToolBar();
		toolBar.add(new JButton("Do"));
		toolBar.add(new JButton("The"));
		toolBar.add(new JButton("Toolbar"));
		toolBar.add(new JButton("Boogie"));
	}

	private void initComponents() {

		initMenuBar();

		initToolBar();

		// Tab pane
		conceptMapTabPane = new JTabbedPane();

		// ConceptMap editor
		// A Concept map editor contains two components:
		// 1. The concept map with nodes and vertices
		// 2. A list of collaborators (awareness view)
		ConceptMapEditorPane cmep = new ConceptMapEditorPane(awarenessService, diagramModel);
		conceptMapTabPane.add("Hardcoded concept map", cmep);

		// Message log
		logView = new JTextArea();
		logView.setEditable(false);

		// 
		FormLayout layout = new FormLayout(
				"150dlu, 2dlu, default:grow", // columns
				"default, 2dlu, default:grow, 2dlu, 50dlu");		// rows

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		builder.add(toolBar, cc.xyw(1, 1, 3, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(new PalettePane(), cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(conceptMapTabPane, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(new JScrollPane(logView), cc.xyw(1, 5, 3, CellConstraints.FILL, CellConstraints.FILL));

		add(builder.getPanel());
	}

	@Override
	public void handleDataSyncEvent(IDataSyncEvent e) {
		ISyncMessage syncMessage = e.getSyncMessage();
		if (syncMessage.getEvent().equals(Configuration.getInstance().getClientEventCreateSession())) {
			currentToolSessionId = syncMessage.getToolSessionId();
			logView.append("GOT SESSION ID: " + currentToolSessionId);
		}
		if (syncMessage.getEvent().equals(Configuration.getInstance().getClientEventSynchronize())) {
			logger.debug("GOT SYNCH" + syncMessage.getContent());
		}
	}

	@Override
	public void linkAdded(ILinkModel link) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void linkRemoved(ILinkModel link) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void nodeAdded(INodeModel n) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void nodeRemoved(INodeModel n) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void updated(IDiagramModel diagramModel) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void nodeSelected(INodeModel n) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void moved(INodeModel node) {
		logger.debug("User moved node");
		dataSyncService.sendMessage(SyncMessageHelper.createSyncMessage(currentToolSessionId, "eu.scy.scymapper", "obama", "bjoerge",
				"NODE MOVED", Configuration.getInstance().getClientEventSynchronize(),  "something", 2323));
	}

	@Override
	public void resized(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void labelChanged(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void styleChanged(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void shapeChanged(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void nodeSelected(NodeModel conceptNode) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	private void addSomeTestConcepts() {
		INodeModel redStar = new NodeModel();
		redStar.setStyle(new DefaultNodeStyle());
		redStar.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		redStar.getStyle().setBackground(new Color(0xcc0000));
		redStar.setShape(new Star());
		redStar.setLabel("I'm a NODE. Hurray!");
		redStar.setLocation(new Point(300, 150));
		redStar.setSize(new Dimension(200, 200));
		diagramModel.addNode(redStar);
		redStar.addObserver(this);

		INodeModel ellipse = new NodeModel();
		ellipse.setStyle(new DefaultNodeStyle());
		ellipse.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		ellipse.getStyle().setBackground(new Color(0x0099ff));
		ellipse.setLabel("I'm also a NODE. Hurray for me too!");
		ellipse.setLocation(new Point(300, 450));
		ellipse.setSize(new Dimension(150, 100));
		ellipse.setShape(new Ellipse());
		diagramModel.addNode(ellipse);
		ellipse.addObserver(this);

		IConceptLinkModel link = new NodeLinkModel(redStar, ellipse);
		link.getStyle().setColor(new Color(0x444444));
		link.getStyle().setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[]{6.0f}, 0.0f));
		link.setShape(new Arrow());
		link.setLabel("I'm in between");
		diagramModel.addLink(link);
	}
}
