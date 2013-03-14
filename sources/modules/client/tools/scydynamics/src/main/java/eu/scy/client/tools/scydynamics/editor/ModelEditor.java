package eu.scy.client.tools.scydynamics.editor;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import colab.um.JColab;
import colab.um.draw.JdCursors;
import colab.um.draw.JdFigure;
import colab.um.draw.JdNode;
import colab.um.draw.JdObject;
import colab.um.draw.JdPopups;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;
import colab.um.draw.JdTools;
import colab.um.parser.JParserException;
import colab.um.parser.JParserExpr;
import colab.um.tools.JTools;
import colab.um.xml.model.JxmModel;
import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.common.datasync.DataSyncException;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.collaboration.ModelSyncControl;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.listeners.EditorMouseListener;
import eu.scy.client.tools.scydynamics.logging.IModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.main.AbstractModellingStandalone;
import eu.scy.client.tools.scydynamics.menu.EditorMenuBar;
import eu.scy.client.tools.scydynamics.model.Model;
import eu.scy.client.tools.scydynamics.model.ModelUtils;
import eu.scy.client.tools.scydynamics.store.FileStore;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

@SuppressWarnings("serial")
public class ModelEditor extends JPanel implements AdjustmentListener {

	public enum Mode {
		BLACK_BOX, CLEAR_BOX, MODEL_SKETCHING, QUALITATIVE_MODELLING, QUANTITATIVE_MODELLING;
	}

	public static final int LARGE_NEGATIVE = -2;
	public static final int SMALL_NEGATIVE = -1;
	public static final int ZERO = 0;
	public static final int SMALL_POSITIVE = 1;
	public static final int LARGE_POSITIVE = 2;

	public static final int TABINDEX_EDITOR = 0;
	public static final int TABINDEX_FEEDBACK = 1;
	public static final int TABINDEX_GRAPH = 2;
	public static final int TABINDEX_TABLE = 3;

	private final static Logger debugLogger = Logger.getLogger(ModelEditor.class.getName());
	public final static String DEFAULT_ACTION = "cursor";
	public final static int LNK_DRAG_POINT = 0;
	public final static int LNK_LOOP = 1;
	public final static int LNK_CONNECTED = 2;
	public final static int LNK_RELASE_MOUSE = 3;
	public final static int LNK_FLOW = 4;
	public final static int LNK_CONSTANT = 5;
	public final static int LNK_NOT_FREE = 6;
	public final static int LNK_DATASET = 7;
	public final static int LNK_NO_STOCK_ENDS = 8;
	private Mode mode = Mode.QUANTITATIVE_MODELLING;
	private JdPopups aPopups;
	private Model model = null;
	private ModelSelection selection;
	private EditorPanel editorPanel;
	private EditorMouseListener mouseListener;
	private int currentCursor;
	private JdCursors cursors;
	private EditorToolbar toolbar;
	private IModellingLogger actionLogger;
	private EditorTab editorTab;
	private ArrayList<String> modelCheckMessages = new ArrayList<String>();
	private final Properties properties;
	protected JTabbedPane tabbedPane;
	private SimulationPanel graphTab;
	private TableTab tableTab;
	private FeedbackTab feedbackTab;
	private final ResourceBundleWrapper bundle;
	private ModelSyncControl modelSyncControl = null;
	private ToolBrokerAPI toolBroker;
	private Domain domain;
	// private ModellingSQLSpacesLogger sqlspacesLogger;
	private String username = "unknown_user";
	private SCYDynamicsStore scyDynamicsStore = null;
	private AbstractModellingStandalone abstractModelling;
	private EditorMenuBar menuBar;
	
	public ModelEditor(Properties newProps) {
		this(newProps, "unknown_user", null);
	}
	
	public ModelEditor(Properties newProps, String username, AbstractModellingStandalone abstractModelling) {
		this.setName("Model Editor");
		this.username = username;
		debugLogger.info("setting username: " + username);
		this.domain = ModelUtils.loadDomain(newProps);
		this.bundle = new ResourceBundleWrapper(this);
		this.properties = getDefaultProperties();
		this.abstractModelling = abstractModelling;
		properties.putAll(newProps);
		debugLogger.info("using properties: " + properties);
		// TODO username
		// sqlspacesLogger = new ModellingSQLSpacesLogger("dummy-user");
		actionLogger = new ModellingLogger(new DevNullActionLogger(), "username", "nullmission");
		// actionLogger = new ModellingLogger(new SystemOutActionLogger(),"username");
		new JTools(JColab.JCOLABAPP_RESOURCES, JColab.JCOLABSYS_RESOURCES);
		initComponents();
		setNewModel();
		selection = new ModelSelection(this);
		setMode(properties.getProperty("editor.mode", "quantitative_modelling"));
	}
	
	public boolean isEditable() {
		if (getMode()==Mode.BLACK_BOX || getMode()==Mode.CLEAR_BOX) {
    		// editing the model is not allowed
    		return false;
    	} else {
    		return true;
    	}
	}
	
	public ModelSelection getModelSelection() {
		return this.selection;
	}
	
	private AbstractModellingStandalone getAbstractModelling() {
		return this.abstractModelling;
	}
	
	public EditorMenuBar getEditorMenuBar() {
		return this.menuBar;
	}
	
	public void setEditorMenuBar(EditorMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public void setSCYDynamicsStore(SCYDynamicsStore store) {
		this.scyDynamicsStore = store;
	}
	
	public SCYDynamicsStore getSCYDynamicsStore() {
		if (scyDynamicsStore == null) {
			setSCYDynamicsStore(new FileStore(this));
		}
		return scyDynamicsStore;
	}

	public void setToolBroker(ToolBrokerAPI tbi) {
		toolBroker = tbi;
		if (toolBroker != null) {
			actionLogger = new ModellingLogger(toolBroker.getActionLogger(), toolBroker.getLoginUserName(), toolBroker.getMissionRuntimeURI().toString());
			setUsername(toolBroker.getLoginUserName());
		}
	}
	
	private void setUsername(String username) {
		this.username  = username;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void joinSession_(ISyncSession session) {
		System.out.println("***** ModelEditor.joinSession(isyncsession) called.");
		debugLogger.info("joining session: " + session);
		modelSyncControl = new ModelSyncControl(this);
		modelSyncControl.setSession(session, true);
		// DataSyncDiagramController diagramController = new
		// DataSyncDiagramController(conceptMap.getDiagram(), session);
		// diagramController.setSession(currentSession, true);
		// conceptDiagramView.setController(diagramController);
		// conceptDiagramView.setElementControllerFactory(new
		// DataSyncElementControllerFactory(session));
	}

	public void leaveSession() {
		debugLogger.info("leaving session");
		modelSyncControl.getSession().leaveSession();
		modelSyncControl = null;
		// DiagramController dc = new DiagramController(conceptMap.getDiagram(),
		// conceptMap.getDiagram().getSelectionModel());
		// currentSession.leaveSession();
		// currentSession = null;
		// conceptDiagramView.setController(dc);
		// conceptDiagramView.setElementControllerFactory(new
		// DefaultElementControllerFactory());
	}

	public ISyncSession joinSession(String sessId) {
		System.out.println("***** ModelEditor.joinSession(string) called.");
		debugLogger.info("joining session: " + sessId);
		return joinSession(sessId, true);
	}

	private ISyncListener dummySyncListener = new ISyncListener() {

		@Override
		public void syncObjectAdded(ISyncObject iSyncObject) {
		}

		@Override
		public void syncObjectChanged(ISyncObject iSyncObject) {
		}

		@Override
		public void syncObjectRemoved(ISyncObject iSyncObject) {
		}
	};

	public ISyncSession joinSession(String sessId, boolean writeCurrentStateToServer) {
		System.out.println("***** ModelEditor.joinSession(string, boolean) called.");

		ISyncSession currentSession = null;
		if (sessId != null) {
			try {
				// DataSyncDiagramController diagramController = new
				// DataSyncDiagramController(conceptMap.getDiagram());
				modelSyncControl = new ModelSyncControl(this);
				currentSession = toolBroker.getDataSyncService().joinSession(sessId, dummySyncListener, "scy-dynamics");
				// currentSession =
				// toolBroker.getDataSyncService().joinSession(sessId,
				// modelSyncControl, "scy-dynamics");
				modelSyncControl.setSession(currentSession, writeCurrentStateToServer);
				// diagramController.setSession(currentSession,
				// writeCurrentStateToServer);
				// conceptDiagramView.setController(diagramController);
				// conceptDiagramView.setElementControllerFactory(new
				// DataSyncElementControllerFactory(currentSession));
			} catch (DataSyncException e) {
				e.printStackTrace();
			}
		}
		return currentSession;
	}

	public Domain getDomain() {
		return domain;
	}
	
	public void updateTitle() {
		try {
			if (this.abstractModelling != null) {
				String modeString = this.getMode().toString().toLowerCase().replace("_", " ");
				String modelName = this.getSCYDynamicsStore().getModelName();
				if (modelName == null) {
					modelName = "unsaved model";
				}
				abstractModelling.setTitle("SCYDynamics - "+modeString+" - "+modelName);
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
	}

	public void setMode(String newMode) {
		debugLogger.info("setting mode to " + newMode);
		if (newMode == null) {
			debugLogger.info("setting mode to 'null' not allowed, setting to default mode 'modelling'");
			setMode(Mode.QUANTITATIVE_MODELLING);
		} else if (newMode.equalsIgnoreCase("black_box")) {
			setMode(Mode.BLACK_BOX);
		} else if (newMode.equalsIgnoreCase("clear_box")) {
			setMode(Mode.CLEAR_BOX);
		} else if (newMode.equalsIgnoreCase("model_sketching")) {
			setMode(Mode.MODEL_SKETCHING);
		} else if (newMode.equalsIgnoreCase("qualitative_modelling")) {
			setMode(Mode.QUALITATIVE_MODELLING);
		} else if (newMode.equalsIgnoreCase("quantitative_modelling")) {
			setMode(Mode.QUANTITATIVE_MODELLING);
		} else {
			debugLogger.info("unknown mode '" + newMode + "', setting to default mode 'modelling'");
			setMode(Mode.QUANTITATIVE_MODELLING);
		}
		updateTitle();
	}

	public void setMode(Mode newMode) {
		System.out.println("ModelEditor: setting mode from " + this.mode + " to " + newMode);
		if (newMode != this.mode) {
			this.mode = newMode;
			switch (mode) {
			case BLACK_BOX:
			case CLEAR_BOX:
				enableTab(TABINDEX_GRAPH);
				enableTab(TABINDEX_TABLE);
				toolbar.toCursorAction();
				toolbar.setEnabled(false);
				clearSelectedObjects();
				break;
			case MODEL_SKETCHING:
				disableTab(TABINDEX_GRAPH);
				disableTab(TABINDEX_TABLE);
				toolbar.setEnabled(true);
				// clear qualitative relation icons
				model.clearQualitativeRelations();
				// reset the phase button
				if (editorTab.getFileToolbar().getPhaseButton()!= null) {
					editorTab.getFileToolbar().getPhaseButton().setEnabled(true);
				}
				break;
			case QUALITATIVE_MODELLING:
				enableTab(TABINDEX_GRAPH);
				disableTab(TABINDEX_TABLE);
				toolbar.setEnabled(true);
				break;
			case QUANTITATIVE_MODELLING:
				enableTab(TABINDEX_GRAPH);
				enableTab(TABINDEX_TABLE);
				toolbar.setEnabled(true);
				// clear qualitative relation icons
				model.clearQualitativeRelations();
				break;
			}
		}
		getFileToolbar().updateMode();
	}

	public Mode getMode() {
		return this.mode;
	}

	public ModelSyncControl getModelSyncControl() {
		return this.modelSyncControl;
	}

	public boolean isSynchronized() {
		return this.modelSyncControl != null;
	}

	public DataSet getDataSet() {
		return tableTab.getDataSet();
	}

	public static Properties getDefaultProperties() {
		Properties props = new Properties();
		props.put("actionlog.to.file", "false");
		props.put("actionlog.to.sqlspaces", "false");
		props.put("sqlspaces.ip", "127.0.0.1");
		props.put("sqlspaces.port", "2525");
		props.put("sqlspaces.space", "scydynamics_actionlog");
		props.put("show.graph", "true");
		props.put("show.table", "true");
		props.put("show.filetoolbar", "true");
		//props.put("editor.fixedcalculationmethod", null);
		props.put("show.popouttabs", "false");
		props.put("editor.mode", "quantitative_modelling");
		props.put("editor.modes_selectable", "false");
		return props;
	}

	public Properties getProperties() {
		return properties;
	}

	public IModellingLogger getActionLogger() {
		return actionLogger;
	}

	public void setActionLogger(IActionLogger newLogger, String username, String missionRuntimeUri) {
		this.actionLogger = new ModellingLogger(newLogger, username, missionRuntimeUri);
	}
	
	public void setActionLogger(IModellingLogger newLogger) {
		this.actionLogger = newLogger;
	}

	public FileToolbar getFileToolbar() {
		return editorTab.getFileToolbar();
	}

	private void initComponents() {
		// same color as background -> invisible
		UIManager.put("TabbedPane.focus", new Color(200,221,242));
		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				JTabbedPane pane = (JTabbedPane) evt.getSource();
				((ChangeListener) pane.getSelectedComponent()).stateChanged(evt);
			}
		});

		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);
		addTabs();
//
//		this.registerKeyboardAction(actionListener, EditorToolbar.DELETE + "", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
//		this.registerKeyboardAction(actionListener, EditorToolbar.COPY + "", KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
//		this.registerKeyboardAction(actionListener, EditorToolbar.PASTE + "", KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
//		this.registerKeyboardAction(actionListener, EditorToolbar.CUT + "", KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
//		this.registerKeyboardAction(actionListener, EditorToolbar.ALL + "", KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void addTabs() {
		editorTab = new EditorTab(this, bundle);
		this.editorPanel = (editorTab).getEditorPanel();
		this.toolbar = (editorTab).getToolbar();
		tabbedPane.setBackground(Color.GRAY.brighter());
		tabbedPane.add(editorTab, TABINDEX_EDITOR);
		tabbedPane.setTabComponentAt(TABINDEX_EDITOR, new TabPanel(bundle.getString("PANEL_EDITOR"), Util.getImageIcon("editor_24.png"), editorTab, this, properties));
		feedbackTab = new FeedbackTab(this, bundle);
		tabbedPane.add(feedbackTab, TABINDEX_FEEDBACK);
		tabbedPane.setTabComponentAt(TABINDEX_FEEDBACK, new TabPanel(bundle.getString("PANEL_BARCHART"), Util.getImageIcon("feedback_24.png"), feedbackTab, this, properties));
		graphTab = new GraphTab(this, bundle);
		tabbedPane.add(graphTab, TABINDEX_GRAPH);
		tabbedPane.setTabComponentAt(TABINDEX_GRAPH, new TabPanel(bundle.getString("PANEL_GRAPH"), Util.getImageIcon("graph_24.png"), graphTab, this, properties));
		tableTab = new TableTab(this, bundle);
		tabbedPane.add(tableTab, TABINDEX_TABLE);
		tabbedPane.setTabComponentAt(TABINDEX_TABLE, new TabPanel(bundle.getString("PANEL_TABLE"), Util.getImageIcon("table_24.png"), tableTab, this, properties));		
		
		if (!Boolean.parseBoolean(properties.getProperty("showFeedback"))) {
			disableTab(TABINDEX_FEEDBACK);
		}
	}

	public void disableTab(int index) {
		tabbedPane.setEnabledAt(index, false);
		for (Component comp : ((TabPanel) tabbedPane.getTabComponentAt(index)).getComponents()) {
			comp.setEnabled(false);
			//comp.setVisible(false);			
		}
	}

	public void enableTab(int index) {
		tabbedPane.setEnabledAt(index, true);
		for (Component comp : ((TabPanel) tabbedPane.getTabComponentAt(index)).getComponents()) {
			comp.setEnabled(true);
			//comp.setVisible(true);
		}
	}

	public Model getModel() {
		return model;
	}

	public EditorPanel getCanvas() {
		return editorPanel;
	}

	public int getCurrentAction() {
		return toolbar.getCurrentAction();
	}

	public void setNewModel() {
		editorPanel.removeMouseListener(mouseListener);
		editorPanel.removeMouseMotionListener(mouseListener);
		model = new Model(this);
		model.setMethod("euler");
		editorPanel.setModel(model);
		mouseListener = new EditorMouseListener(this, model);
		editorPanel.addMouseListener(mouseListener);
		editorPanel.addMouseMotionListener(mouseListener);
		this.getActionLogger().logSimpleAction(ModellingLogger.MODEL_CLEARED);
	}

	public void setJxmModel(JxmModel m) {
		clearSelectedObjects();
		if (m != null) {
			if (model == null) {
				mouseListener = new EditorMouseListener(this, model);
				editorPanel.addMouseListener(mouseListener);
				editorPanel.addMouseMotionListener(mouseListener);
			}
			model.setXmModel(m);
			editorPanel.setModel(model);
		} else {
			model = null;
			editorPanel.setModel(null);
			editorPanel.removeMouseListener(mouseListener);
		}
	}

	public void setModel(Element element) {
		JxmModel jxmModel = JxmModel.readStringXML(new XMLOutputter(Format.getPrettyFormat()).outputString(element));
		setJxmModel(jxmModel);
		// setting mode from model-element; QUANTITATIVE as default
		setMode(element.getAttributeValue("mode", ModelEditor.Mode.QUANTITATIVE_MODELLING.toString()));
	}

	public String getModelXML() {
		if (model == null) {
			return null;
		}
		JxmModel m = model.getXmModel(); // pass from JdObjects to JxmModel
		return m.getXML("", true); // get XML model string
	}

	public boolean isValidName(String name) {
		if (model == null) {
			return false;
		}
		boolean b = false;
		if (name == null) {
			// userMessage =
			// JTools.getAppResourceString("editorMsgYouMustIntroduceVariableName");
			debugLogger.info("invalid variable name: null");
		} else if (name.length() < 1) {
			// userMessage =
			// JTools.getAppResourceString("editorMsgYouMustIntroduceVariableName");
			debugLogger.info("invalid variable name: length < 1");
		} else if (model.hasObjectOfName(name)) {
			// userMessage =
			// JTools.getAppResourceString("editorMsgDuplicateVariableName",
			// name);
			debugLogger.info("invalid variable name: duplicate name");
		} else if (JParserExpr.isToken(name)) {
			// userMessage =
			// JTools.getAppResourceString("editorMsgReservedWord",name);
			debugLogger.info("invalid variable name: expression token");
		} else {
			b = true;
		}
		// if (!b)
		// showStatusMsg(userMessage, true);
		return b;
	}

	// ---------------------------------------------------------------------------
	public boolean isModelWithIF() {
		if (model == null) {
			return false;
		}
		for (JdObject o : model.getObjects().values()) {
			switch (o.getType()) {
			case JdFigure.CONSTANT:
			case JdFigure.AUX:
			case JdFigure.STOCK:
				String s = o.getExpr();
				if (s != null) {
					String upperExpr = s.toUpperCase();
					if (upperExpr.indexOf("IF(") >= 0) {
						return true;
					}
					if (upperExpr.indexOf("IF (") >= 0) {
						return true;
					}
					if (upperExpr.indexOf("IF  (") >= 0) {
						return true;
					}
				}
				break;
			}
		}
		return false;
	}

	public ArrayList<String> getModelCheckMessages() {
		return modelCheckMessages;
	}

	// ---------------------------------------------------------------------------
	public boolean checkModel() {
		modelCheckMessages.clear();
		if (model == null) {
			return true;
		}
		boolean b = true;
		JParserExpr parser = new JParserExpr();
		// set qualitative/graph expressions
		model.updateAuxExpressions();
		// add variables to parser
		for (JdObject o : model.getObjects().values()) {
			switch (o.getType()) {
			case JdFigure.CONSTANT:
			case JdFigure.AUX:
			case JdFigure.STOCK:
			case JdFigure.DATASET:
				if (!o.isSpecified()) {
					if (b) {
						// errMsg =
						// JTools.getAppResourceString("editorMsgVariableUndefined",
						// o.getLabel());
						// userMessage =
						// JTools.getAppResourceString("editorMsgUnableToSimulateModel")+
						// "\n" + errMsg;
						// showStatusMsg(errMsg, true);
						modelCheckMessages.add("* the element '" + o.getLabel() + "' is not defined.");
						b = false;
					}
				}
				try {
					parser.addVar(o.getLabel(), 1.0);
				} catch (JParserException ex) {
				}
				break;
			}
		}
		try {
			parser.addVar("time", 1.0);
		} catch (JParserException ex) {
		}
		// evaluate expressions
		for (JdObject o : model.getObjects().values()) {
			switch (o.getType()) {
			case JdFigure.CONSTANT:
			case JdFigure.AUX:
				// case JdFigure.DATASET:
			case JdFigure.STOCK:
				boolean b2 = false;
				try {
					b2 = parser.validate(o.getExpr());
				} catch (JParserException ex) {
				}
				o.setSpecified(b2);
				if (!b2 && !b) {
					// errMsg =
					// JTools.getAppResourceString("editorMsgVariableExpressionError",
					// o.getLabel());
					// userMessage =
					// JTools.getAppResourceString("editorMsgUnableToSimulateModel")+
					// "\n" + errMsg + "\n";
					// if (parser.getErrorMessage() != null)
					// userMessage +=
					// JTools.getAppResourceString("editorMsgExpressionError",
					// parser.getErrorMessage());
					// showStatusMsg(errMsg,true);
					modelCheckMessages.add("* the element '" + o.getLabel() + "' contains an invalid expression.");
					if (parser.getErrorMessage() != null) {
						modelCheckMessages.add(parser.getErrorMessage());
					}
					b = false;
				}
				break;
			}
		}
		// evaluate expressions (links)
		for (JdObject o : model.getObjects().values()) {
			switch (o.getType()) {
			// case JdFigure.DATASET:
			case JdFigure.AUX:
			case JdFigure.STOCK:
				JdNode node = (JdNode) o;
				boolean b3 = true;
				if (!node.isGraphExpr()) // do not check GRAPH(...) expressions
				{
					b3 = checkExpr(node.getLabel(), node.getExpr(), model.getLinkedVariablesTo(node, false), true);
				}
				o.setSpecified(b3);
				if (!b3 && !b) {
					b = false;
				}
				
				if (node instanceof JdStock) {
					// check if a stock node is used properly
					Vector<JdFigure> incoming = model.getIncomingFigures((JdStock)node);
					Vector<JdFigure> outgoing = model.getOutgoingFigures((JdStock)node);
					if (incoming.isEmpty() && outgoing.isEmpty()) {
						modelCheckMessages.add("* the Stock '" + o.getLabel() + "' has no incoming or outgoing flows. Please correct this or use a Const or Aux instead.");
						b = false;
					}
				}
 				break;
			}
		}
		return b;
	}

	// -------------------------------------------------------------------------
	public boolean checkExpr(String vName, String aExpr, Vector<String> vLinks, boolean bUseAllLinks) {
		if (model == null) {
			return true;
		}
		JParserExpr parser = new JParserExpr();
		if (vLinks == null) { // use model variables
			for (JdObject o : model.getObjects().values()) {
				switch (o.getType()) {
				case JdFigure.CONSTANT:
				case JdFigure.AUX:
				case JdFigure.DATASET:
				case JdFigure.STOCK:
					try {
						parser.addVar(o.getLabel(), 1.0);
					} catch (JParserException ex) {
					}
					break;
				}
			}
		} else { // use linked variables
			for (String o : vLinks) {
				try {
					parser.addVar(o, 1.0);
				} catch (JParserException ex) {
				}
			}
		}
		try {
			parser.addVar("time", 1.0);
		} catch (JParserException ex) {
		}
		boolean b = false;
		try {
			b = parser.validate(aExpr);
		} catch (JParserException ex) {
		}
		if (!b) {
			modelCheckMessages.add("* The expression parser encoutered an error in element " + vName);
			return false;
		}
		// all linked variables used?
		if (b && vLinks != null && bUseAllLinks) {
			Vector<String> vExpr = parser.getExprVars(aExpr);
			if (vExpr.isEmpty() && !vLinks.isEmpty()) {
				// userMessage =
				// JTools.getAppResourceString("editorMsgYouMustUseAllLinkedVariables");
				// showStatusMsg(userMessage, true);
				debugLogger.info("not all linked variables used");
				return false;
			}
			// create vector of linked variables in lowercase
			Vector<String> v1 = new Vector<String>();
			for (String o : vLinks) {
				v1.add(o.toLowerCase());
			}
			// create vector of expression variables in lowercase
			Vector<String> v2 = new Vector<String>();
			for (int i = 0; i < vExpr.size(); i++) {
				v2.add((vExpr.elementAt(i)).toLowerCase());
			}
			// test all linked variables match with expresion variables
			for (int i = 0; i < v1.size(); i++) {
				String aVar = v1.elementAt(i);
				if (v2.indexOf(aVar) < 0) {
					// userMessage =
					// JTools.getAppResourceString("editorMsgYouMustUseAllLinkedVariables");
					// showStatusMsg(userMessage, true);
					debugLogger.info("not all linked variables used");
					return false;
				}
			}
			// test all expression variables match with linked variables
			v1.add("time"); // allow time in expressions
			for (int i = 0; i < v2.size(); i++) {
				String aVar = v1.elementAt(i);
				if (v1.indexOf(aVar) < 0) {
					// userMessage =
					// JTools.getAppResourceString("editorMsgYouMustUseAllLinkedVariables");
					// showStatusMsg(userMessage, true);
					debugLogger.info("not all linked variables used");
					return false;
				}
			}
		}
		return b;
	}

	// ---------------------------------------------------------------------------
	public boolean checkConstant(String aExpr) {
		if (aExpr == null) {
			return false;
		}
		boolean valid = false;
		JParserExpr parser = new JParserExpr();
		try {
			valid = parser.validate(aExpr);
		} catch (JParserException ex) {
			return false;
		}
		return (parser.isConstant() && valid);
	}

	public ModelSelection getSelection() {
		return selection;
	}

	public Hashtable<String, JdObject> getCopySelection() {
		return selection.getCopySelection();
	}

	// ---------------------------------------------------------------------------
	public void setCopySelection(Vector<JdObject> v) {
		selection.setCopySelection(v);
	}

	// ---------------------------------------------------------------------------
	public void clearSelectedObjects() {
		selection.unselectAll();
		updateCanvas();
	}

	public void selectAllObjects() {
		if (model == null) {
			return;
		}
		selection.unselectAll();
		for (JdObject o : model.getObjects().values()) {
			selection.selectObject(o, true);
		}
		updateCanvas();
	}

	public void selectObjects(Vector<String> v) {
		if (model == null) {
			return;
		}
		selection.unselectAll();
		for (String objName : v) {
			JdObject o = model.getObjectOfName(objName);
			selection.selectObject(o, true);
		}
		updateCanvas();
		// updateSpecDialog(true);
	}

	// ---------------------------------------------------------------------------
	public void selectObjects(Rectangle r) {
		if (model == null) {
			return;
		}
		selection.unselectAll();
		for (JdObject o : model.getObjects().values()) {
			if (JdTools.testR1ContainsR2(r, o.getBounds())) {
				selection.selectObject(o, true);
			}
		}
		updateCanvas();
		// updateSpecDialog(true);
	}

	// -------------------------------------------------------------------------
	public void selectObject(JdObject o, boolean bToggle) {
		if (model == null) {
			return;
		}
		selection.selectObject(o, bToggle);
		updateCanvas();
		// updateSpecDialog(true);
	}

	// -------------------------------------------------------------------------
	public void selectObject(String s, boolean bEvent) {
		if (model == null) {
			return;
		}
		selection.unselectAll();
		JdObject o = model.getObjectOfName(s);
		selection.selectObject(o, false);
		updateCanvas();
		// if (bEvent)
		// sendVisualToolEvent("SelectObject", s);
	}

	public void updateCanvas() {
		editorPanel.repaint();
	}

	public void setModelChanged() {
		// // LOGGER.info("JdEditorStandalone.setModelChanged() called.");
	}

	public void startRect(int x, int y) {
		editorPanel.startRect(x, y);
	}

	public void extendRect(int x, int y) {
		editorPanel.extendRect(x, y);
	}

	public Rectangle stopRect() {
		return editorPanel.stopRect();
	}

	public void showPopupMenu(JdObject o, int x, int y) {
		if (o == null) {
			return;
		}
		JPopupMenu pm = aPopups.getPopup(o);
		if (pm != null) {
			pm.show(this, x, y);
		}
		if (!o.isNode()) {
			return;
		}
	}

	public Vector<String> getSelectedObjects() {
		Vector<String> v = new Vector<String>();
		if (selection != null) {
			for (JdObject o : selection.getObjects().values()) {
				v.add(o.getLabel());
			}
		}
		return v;
	}

	public void showSpecDialog(JdFigure figure, java.awt.Point position) {
		java.awt.Frame frame = javax.swing.JOptionPane.getFrameForComponent(this);
		VariableDialog vdialog = new VariableDialog(frame, position, figure, this, bundle);
		vdialog.setVisible(true);
		editorPanel.updateUI();
	}

	// ---------------------------------------------------------------------------
	public void deleteFigure(String name, boolean bEvent) {
		if (model == null || name == null) {
			return;
		}
		deleteFigure(model.getObjectOfName(name), bEvent);
	}

	public void deleteFigure(JdObject object, boolean bEvent) {
		if (model == null || object == null) {
			return;
		}
		// aEditorVT.sendLogEvent(JvtEditorLogs.LOG_DELETE_OBJECT,o); // log
		// delete object
		model.removeObjectAndRelations(object);
		clearSelectedObjects();
		setModelChanged();
		checkModel();
		if (isSynchronized()) {
			System.out.println("ModelEditor.deleteFigure called for " + object.getID());
			getModelSyncControl().removeObject(object);
		}
		// if (bEvent)
		// sendVisualToolEvent("DeleteFigure", name);
	}

	public void setFigureProperties(String oldName, Hashtable<String, Object> h) {
		if (model == null || oldName == null) {
			return;
		}
		JdObject o = model.getObjectOfName(oldName);
		if (o == null || h == null) {
			debugLogger.warning("couldn't find object "+oldName+", doing nothing");
			return;
		}
		boolean bChangeRel = false;
		boolean bModelChanged = false;
		String vname = (String) h.get("label");
		if (vname != null && !vname.equals(o.getLabel())) {
			bModelChanged = true;
		}
		switch (o.getType()) {
		case JdFigure.STOCK:
		case JdFigure.CONSTANT:
		case JdFigure.AUX:
		case JdFigure.DATASET:
			String vexpr = (String) h.get("expr");
			if (vexpr != null && !vexpr.equals(o.getExpr())) {
				bModelChanged = true;
			}
			if (h.containsKey("exprType")) {
				int t = ((Integer) h.get("exprType")).intValue();
				if (((JdNode) o).getExprType() != t) {
					bModelChanged = true;
					bChangeRel = true;
				}
			}
			break;
		case JdFigure.RELATION:
			JdRelation r = (JdRelation) o;
			if (h.containsKey("relType")) {
				if (r.getRelationType() != ((Integer) h.get("relType")).intValue()) {
					bModelChanged = true;
				}
			}
		}
		h.put("oldLabel", oldName);
		o.setProperties(h);
		Hashtable<String, Object> ho = o.getProperties();
		ho.put("oldLabel", oldName);
		// sendVisualToolEvent("ChangeObjectEvent", ho);
		editorPanel.repaint();
		// rename JdModel key
		String newName = h.containsKey("label") ? (String) h.get("label") : null;
		if (newName != null && !oldName.equals(newName)) {
			model.renameObjectKey(oldName, newName);
			selection.renameObjectKey(oldName, newName);
		}
		if (h.containsKey("relations")) {
			model.updateNodeRelations(((JdNode) o).getLabel(), (Hashtable) h.get("relations"));
			// updateSpecDialog(false);
		} else if (bChangeRel) {
			model.changeNodeRelations(((JdNode) o).getLabel(), ((Integer) h.get("exprType")).intValue());
			// updateSpecDialog(false);
		}
		if (bModelChanged) {
			setModelChanged();
		}
	}

	public void setCursor(int newCursor) {
		if (currentCursor != newCursor) {
			this.getCanvas().setCursor(cursors.getCursor(newCursor));
			// lastCursor = currentCursor;
			currentCursor = newCursor;
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		Hashtable<String, Object> h = new Hashtable<String, Object>();
		h.put("orientation", (e.getAdjustable().getOrientation() == Adjustable.HORIZONTAL) ? "horizontal" : "vertical");
		h.put("value", String.valueOf(e.getValue()));
	}

	public EditorToolbar getEditorToolbar() {
		return this.editorTab.getEditorToolbar();
	}

	public void doAutosave(StoreType type) {
		if (this.getProperties().getProperty("autoSave", "false").equalsIgnoreCase("true")) {
			// autosaving...
			try {
				String originalFilename = this.getSCYDynamicsStore().getModelName();
				this.getSCYDynamicsStore().doAutoSave(type);
				this.getSCYDynamicsStore().setModelName(originalFilename);
			} catch (Exception ex) {
				debugLogger.severe(ex.getMessage());
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(this),
					    "The model could not be automatically stored:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public ResourceBundleWrapper getBundle() {
		return this.bundle;
	}
}