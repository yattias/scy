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
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.impl.component.ConceptDiagramView;
import eu.scy.scymapper.impl.controller.DiagramController;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.shapes.concepts.RoundRectangle;
import eu.scy.scymapper.impl.shapes.concepts.SVGConcept;
import eu.scy.scymapper.impl.shapes.concepts.Star;
import eu.scy.scymapper.impl.shapes.concepts.Ellipse;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.sessionmanager.SessionManager;
import org.apache.log4j.Logger;
import roolo.elo.api.IMetadataKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.io.IOException;

/**
 * User: Bjoerge
 * Date: 27.aug.2009
 * Time: 13:29:56
 */
public class SCYMapperMain extends JFrame {
	//private JXTaskPaneContainer taskPaneContainer;
	private JTabbedPane conceptMapTabPane;
	private IDiagramModel diagramModel;
	private ConceptDiagramView diagramView;
	private IAwarenessService awarenessService;
	private static final String HARD_CODED_USER_NAME = "obama";
	private static final String HARD_CODED_PASSWORD = "obama";
	private ToolBrokerAPI<IMetadataKey> toolBroker;
	private JTextArea logView;
	private final static Logger logger = Logger.getLogger(SCYMapperMain.class);
	private AwarenessView awarenessPanel;
	private SessionManager session;
	private JToolBar toolBar;

	public static void main(String[] args) {
		new SCYMapperMain().setVisible(true);
	}

	public SCYMapperMain() throws HeadlessException {
		super("SCYMapper");
		initLAF();
		initToolBroker();
		initComponents();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1200, 900);

		addSomeTestConcepts();
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

		INodeModel ellipse = new NodeModel();
		ellipse.setStyle(new DefaultNodeStyle());
		ellipse.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		ellipse.getStyle().setBackground(new Color(0x0099ff));
		ellipse.setLabel("I'm also a NODE. Hurray for me too!");
		ellipse.setLocation(new Point(300, 450));
		ellipse.setSize(new Dimension(150, 100));
		ellipse.setShape(new Ellipse());
		diagramModel.addNode(ellipse);

		IConceptLinkModel link = new NodeLinkModel(redStar, ellipse);
		link.getStyle().setColor(new Color(0x444444));
		link.getStyle().setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[]{6.0f}, 0.0f));
		link.setShape(new Arrow());
		link.setLabel("I'm in between");
		diagramModel.addLink(link);
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
		awarenessService = toolBroker.getAwarenessService();
		awarenessService.init(toolBroker.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));

		try {
			logger.debug("Is connected: " + awarenessService.isConnected());
			logger.info("my buddies: " + awarenessService.getBuddies());
			awarenessService.sendMessage("bjoerge@129.177.24.191", "Hi, this is Obama!");
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
		awarenessPanel = new AwarenessView(awarenessService);

		// Tab pane
		conceptMapTabPane = new JTabbedPane();

		// Diagram
		diagramModel = new DiagramModel();
		diagramView = new ConceptDiagramView(new DiagramController(diagramModel), diagramModel);
		conceptMapTabPane.add("Static Concept map", diagramView);

		// Message log
		logView = new JTextArea();
		logView.setEditable(false);

		// 
		FormLayout layout = new FormLayout(
				"default:grow, 2dlu, right:200dlu", // columns
				"default, 2dlu, default:grow, 2dlu, 100dlu");		// rows

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		builder.add(toolBar, cc.xyw(1, 1, 2, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(conceptMapTabPane, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(awarenessPanel, cc.xywh(3, 3, 1, 3, CellConstraints.FILL, CellConstraints.FILL));

		builder.add(new JScrollPane(logView), cc.xy(1, 5, CellConstraints.FILL, CellConstraints.FILL));

		add(builder.getPanel());
	}
}
