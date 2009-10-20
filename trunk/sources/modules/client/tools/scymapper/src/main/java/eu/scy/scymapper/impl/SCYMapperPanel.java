package eu.scy.scymapper.impl;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.Options;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.datasync.client.IDataSyncService;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.IConceptMapManager;
import eu.scy.scymapper.api.IConceptMapManagerListener;
import eu.scy.scymapper.api.IConceptMapSelectionListener;
import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.model.*;
import eu.scy.scymapper.impl.shapes.concepts.Ellipse;
import eu.scy.scymapper.impl.shapes.concepts.Star;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.shapes.links.Arrowhead;
import eu.scy.scymapper.impl.shapes.links.Line;
import eu.scy.scymapper.impl.ui.palette.PalettePane;
import eu.scy.scymapper.impl.ui.tabpane.ConceptMapEditor;
import eu.scy.scymapper.impl.ui.tabpane.ConceptMapTabbedPane;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.api.*;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.*;

/**
 * User: Bjoerge
 * Date: 27.aug.2009
 * Time: 13:29:56
 */
public class SCYMapperPanel extends JPanel implements IDataSyncListener, IDiagramListener, INodeModelListener, IConceptMapSelectionListener, IConceptMapManagerListener {
	private ConceptMapTabbedPane conceptMapTabPane;
	private ToolBrokerAPI<IMetadataKey> toolBroker;

	//init props
	private JTextArea logView;
	private final static Logger logger = Logger.getLogger(SCYMapperPanel.class);
	private JToolBar toolBar;
	private String currentToolSessionId;
	private static SCYMapperPanel INSTANCE;
	private IConceptMapManager conceptMapManager;
	private IELOFactory eloFactory;

	private IMetadataTypeManager metadataTypeManager;
	private IDataSyncService dataSyncService;
	private IRepository repository;
	private static final String SCYMAPPER_ELOTYPE = "scy/conceptmap";
	private static final String DEFAULT_CMAP_NAME = "New Concept Map";
	private XMPPConnection connection;

	public SCYMapperPanel() {

	 	conceptMapManager = DefaultConceptMapManager.getInstance();
		conceptMapManager.addSelectionChangeListener(this);
		conceptMapManager.addConceptMapManagerListener(this);

		setLayout(new BorderLayout());

		initLAF();
		initToolBroker();
		initComponents();
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

		toolBroker = new ToolBrokerImpl<IMetadataKey>();

		//logger.debug("Getting datasync-service");
		//dataSyncService = toolBroker.getDataSyncService();
		//dataSyncService.init(toolBroker.getConnection(username, password));

		//dataSyncService.addDataSyncListener(this);
		//dataSyncService.createSession("eu.scy.scymapper", username);
	}

	private void initMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

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

		//setJMenuBar(menuBar);
	}

	private void initToolBar() {
		toolBar = new JToolBar();

		JButton connectButton = new JButton("Connect", new ImageIcon(getClass().getResource("icons/connect.png")));
		connectButton.setToolTipText("EXPERIMENTAL");
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		toolBar.add(connectButton);

		JButton newConceptMapBtn = new JButton("New", new ImageIcon(getClass().getResource("icons/clear.png")));
		newConceptMapBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IConceptMap cmap = new DefaultConceptMap(DEFAULT_CMAP_NAME, new DiagramModel());
				conceptMapManager.add(cmap);
				conceptMapManager.setSelected(cmap);
			}
		});
		toolBar.add(newConceptMapBtn);
		JButton saveConceptMapBtn = new JButton("Save", new ImageIcon(getClass().getResource("icons/save.png")));
		saveConceptMapBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveELO(conceptMapManager.getSelected());
			}
		});
		toolBar.add(saveConceptMapBtn);
		JButton openConceptMapBtn = new JButton("Open", new ImageIcon(getClass().getResource("icons/open.png")));
		openConceptMapBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IQuery query = null;
				java.util.List<ISearchResult> searchResults = repository.search(query);
				URI[] uris = new URI[searchResults.size()];
				int i = 0;
				for (ISearchResult searchResult : searchResults) {
					uris[i++] = searchResult.getUri();
				}
				URI drawingUri = (URI) JOptionPane.showInputDialog(null, "Select concept map", "Select concept map", JOptionPane.QUESTION_MESSAGE, null, uris, null);
				if (drawingUri != null) {
					loadElo(drawingUri);
				}
			}
		});
		toolBar.add(openConceptMapBtn);

		JButton addConceptBtn = new JButton("Add concept", new ImageIcon(getClass().getResource("icons/new.png")));
		addConceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				conceptMapManager.getSelected().getDiagram().addNode(new NodeModel(), true);
			}
		});
		toolBar.add(addConceptBtn);

		JButton removeConceptBtn = new JButton("Remove concept", new ImageIcon(getClass().getResource("icons/delete.png")));
		removeConceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				INodeModel selectedNode = conceptMapManager.getSelected().getDiagramSelectionModel().getSelectedNode();
				IDiagramModel diagram = conceptMapManager.getSelected().getDiagram();
				Set<INodeLinkModel> toBeRemoved = new HashSet<INodeLinkModel>();
				for (ILinkModel link : diagram.getLinks()) {
					if (link instanceof INodeLinkModel) {
						INodeLinkModel nodeLink = (INodeLinkModel) link;
						if (selectedNode.equals(nodeLink.getFromNode()) || selectedNode.equals(nodeLink.getToNode())) {
							toBeRemoved.add(nodeLink);
						}
					}
				}
				for (INodeLinkModel link : toBeRemoved) diagram.removeLink(link);
				conceptMapManager.getSelected().getDiagram().removeNode(selectedNode);
			}
		});
		toolBar.add(removeConceptBtn);
	}
	public void saveELO(IConceptMap cmap) {
		saveELO(cmap, false);
	}
	public void saveELO(IConceptMap cmap, boolean saveAs) {

		if (saveAs || cmap.getName() == null || cmap.getName().equals(DEFAULT_CMAP_NAME)) {

			String name = "";

			do name = JOptionPane.showInputDialog("Enter name:", cmap.getName());
			while (name.trim().equals(""));

			cmap.setName(name);
		}

		IELO elo = eloFactory.createELO();
		elo.setDefaultLanguage(Locale.ENGLISH);
		IMetadataKey uriKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
		IMetadataKey titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
		IMetadataKey typeKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
		IMetadataKey dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
		IMetadataKey authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
		elo.getMetadata().getMetadataValueContainer(titleKey).setValue(cmap.getName());
		elo.getMetadata().getMetadataValueContainer(titleKey).setValue(cmap.getName(), Locale.CANADA);
		elo.getMetadata().getMetadataValueContainer(typeKey).setValue(SCYMAPPER_ELOTYPE);
		elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(new Long(System.currentTimeMillis()));

		elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute("my vcard", System.currentTimeMillis()));
		IContent content = eloFactory.createContent();
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(cmap);
		content.setXmlString(xml);
		elo.setContent(content);
		IMetadata resultMetadata = repository.addNewELO(elo);
		eloFactory.updateELOWithResult(elo, resultMetadata);
	}


	public void loadElo(URI eloUri) {
		logger.info("Trying to load elo " + eloUri);
		IELO elo = repository.retrieveELO(eloUri);
		if (elo != null) {
			IMetadataKey titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
			IMetadataKey typeKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
			IMetadataKey dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());

			String eloType = elo.getMetadata().getMetadataValueContainer(typeKey).getValue().toString();

			if (!eloType.equals(SCYMAPPER_ELOTYPE))
				throw new IllegalArgumentException("ELO (" + eloUri + ") is of wrong type: " + eloType + " should be " + SCYMAPPER_ELOTYPE);
			IMetadata metadata = elo.getMetadata();
			IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);

			XStream xstream = new XStream(new DomDriver());
			IConceptMap cmap = (IConceptMap) xstream.fromXML(elo.getContent().getXmlString());
			conceptMapManager.add(cmap);
		}
	}

	private void initComponents() {

		initMenuBar();

		initToolBar();

		ArrayList<INodeShape> availNodeShapes = new ArrayList<INodeShape>();
		availNodeShapes.add(new Star());
		availNodeShapes.add(new eu.scy.scymapper.impl.shapes.concepts.RoundRectangle());
		availNodeShapes.add(new Ellipse());

		ArrayList<ILinkShape> availLinkShapes = new ArrayList<ILinkShape>();
		Arrow a = new Arrow();
		a.setBidirectional(false);
		Arrowhead arrowhead = new Arrowhead(10);
		arrowhead.setFixedSize(true);
		a.setArrowhead(arrowhead);
		availLinkShapes.add(a);
		availLinkShapes.add(new Line());

		// Tab pane
		conceptMapTabPane = new ConceptMapTabbedPane();
		conceptMapTabPane.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				conceptMapManager.setSelected(((ConceptMapEditor) conceptMapTabPane.getSelectedComponent()).getConceptMap());
			}
		});

		// ConceptMap editor
		// A Concept map editor contains two components:
		// 1. The concept map with nodes and vertices
		// 2. A list of collaborators (awareness view)
		// This means that the user can have several concept maps open at the same time, collaborating with different
		// groups of users

		IDiagramModel diagram = new DiagramModel();
		IConceptMap cmap = new DefaultConceptMap(DEFAULT_CMAP_NAME, diagram);
		conceptMapManager.add(cmap);
		diagram.addDiagramListener(this);

		//createMockConceptMaps();

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
		builder.add(new PalettePane(conceptMapManager, availLinkShapes, availNodeShapes), cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(conceptMapTabPane, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(new JScrollPane(logView), cc.xyw(1, 5, 3, CellConstraints.FILL, CellConstraints.FILL));

		add(builder.getPanel());
	}

	@Override
	public void selectionChanged(IConceptMapManager manager, IConceptMap cmap) {
		for (Component c : conceptMapTabPane.getComponents()) {
			if (c instanceof ConceptMapEditor) {
				ConceptMapEditor cmapPane = (ConceptMapEditor) c;
				if (cmapPane.getConceptMap().equals(cmap)) {
					conceptMapTabPane.setSelectedComponent(cmapPane);
				}
			}
		}
	}

	private boolean login() {
		JTextField userField = new JTextField();
		JPasswordField passField = new JPasswordField();
		Object[] options = {"Please enter your user name and password.", userField, passField};

		int result = JOptionPane.showOptionDialog(null, options, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

		//user hit OK
		if (result == JOptionPane.OK_OPTION) {
			String username = userField.getText();
			String password = new String(passField.getPassword());
			connection = toolBroker.getConnection(username, password);
			return true;
		}

		//user hit cancel
		return false;
	}

	@Override
	public void conceptMapAdded(IConceptMapManager manager, IConceptMap cmap) {
		ConceptMapEditor editor = new ConceptMapEditor(cmap);
		conceptMapTabPane.add(editor);
		conceptMapManager.setSelected(cmap);
	}

	@Override
	public void conceptMapRemoved(IConceptMapManager manager, IConceptMap map) {
		logger.debug("Concept map was removed from manager");
		conceptMapManager.setSelected(((ConceptMapEditor)conceptMapTabPane.getSelectedComponent()).getConceptMap());
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
		System.out.println("SCYMapperPanel.nodeAdded");
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
		//dataSyncService.sendMessage(SyncMessageHelper.createSyncMessage(currentToolSessionId, "eu.scy.scymapper", "bjoerge",
		//		"NODE MOVED", Configuration.getInstance().getClientEventSynchronize(), "something", 2323));
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
	public void shapeChanged(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}


	public void setRepository(IRepository repo) {
		repository = repo;
	}

	public void setEloFactory(IELOFactory eloFactory) {
		this.eloFactory = eloFactory;
	}


	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
		this.metadataTypeManager = metadataTypeManager;
	}

	private void createMockConceptMaps() {
		IConceptMap mockConceptMap = createMockConceptMap();
		IConceptMap anotherMockConceptMap = createAnotherMockConceptMap();

		ConceptMapEditor mockMapEditorPane1 = new ConceptMapEditor(mockConceptMap);
		ConceptMapEditor anotherMockMapEditor = new ConceptMapEditor(anotherMockConceptMap);

		conceptMapTabPane.add(mockMapEditorPane1);
		conceptMapTabPane.add(anotherMockMapEditor);

		conceptMapManager.setSelected(anotherMockConceptMap);
	}
	private IConceptMap createMockConceptMap() {
		IDiagramModel diagram = new DiagramModel();
		INodeModel redStar = new NodeModel();
		redStar.setStyle(new DefaultNodeStyle());
		redStar.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		redStar.getStyle().setBackground(new Color(0xcc0000));
		redStar.setShape(new Star());
		redStar.setLabel("I'm a NODE. Hurray!");
		redStar.setLocation(new Point(300, 150));
		redStar.setSize(new Dimension(200, 200));
		diagram.addNode(redStar);
		redStar.addListener(this);

		INodeModel ellipse = new NodeModel();
		ellipse.setStyle(new DefaultNodeStyle());
		ellipse.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		ellipse.getStyle().setBackground(new Color(0x0099ff));
		ellipse.setLabel("I'm a NODE too!");
		ellipse.setLocation(new Point(300, 450));
		ellipse.setSize(new Dimension(150, 100));
		ellipse.setShape(new Ellipse());
		diagram.addNode(ellipse);
		ellipse.addListener(this);

		INodeLinkModel link = new NodeLinkModel(redStar, ellipse);
		link.getStyle().setColor(new Color(0x444444));
		link.getStyle().setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[]{6.0f}, 0.0f));
		Arrow arrow = new Arrow();
		Arrowhead arrowhead = new Arrowhead(25, Math.PI / 3);
		arrowhead.setFixedSize(true);
		arrow.setArrowhead(arrowhead);
		link.setShape(arrow);
		link.setLabel("I'm in between");
		diagram.addLink(link);
		diagram.addDiagramListener(this);

		return new DefaultConceptMap("Mock concept map #1", diagram);
	}

	private DefaultConceptMap createAnotherMockConceptMap() {
		IDiagramModel diagram = new DiagramModel();
		INodeModel greenCircle = new NodeModel();
		greenCircle.setStyle(new DefaultNodeStyle());
		greenCircle.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		greenCircle.getStyle().setBackground(Color.green);
		greenCircle.setShape(new Star());
		greenCircle.setLabel("I'm a green circle!");
		greenCircle.setLocation(new Point(450, 400));
		greenCircle.setSize(new Dimension(200, 200));
		diagram.addNode(greenCircle);

		greenCircle.addListener(this);

		INodeModel blueStar = new NodeModel();
		blueStar.setStyle(new DefaultNodeStyle());
		blueStar.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		blueStar.getStyle().setBackground(new Color(0x0099ff));
		blueStar.setLabel("I'm a blue star!");
		blueStar.setLocation(new Point(50, 50));
		blueStar.setSize(new Dimension(150, 100));
		blueStar.setShape(new Ellipse());
		diagram.addNode(blueStar);
		blueStar.addListener(this);

		INodeLinkModel link = new NodeLinkModel(greenCircle, blueStar);
		link.getStyle().setColor(new Color(0x444444));
		link.getStyle().setStroke(new BasicStroke(1.0f));
		Arrow arrow = new Arrow();
		arrow.setBidirectional(true);
		Arrowhead arrowhead = new Arrowhead(25, Math.PI / 3);
		arrowhead.setFixedSize(true);
		arrow.setArrowhead(arrowhead);
		link.setShape(arrow);
		link.setLabel("Or was it the other way round?");
		diagram.addLink(link);
		diagram.addDiagramListener(this);

		return new DefaultConceptMap("Mock concept map #2", diagram);
	}
}
