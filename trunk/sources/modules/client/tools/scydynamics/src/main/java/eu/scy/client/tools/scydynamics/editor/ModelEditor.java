package eu.scy.client.tools.scydynamics.editor;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
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
import colab.um.draw.JdTools;
import colab.um.parser.JParserException;
import colab.um.parser.JParserExpr;
import colab.um.tools.JTools;
import colab.um.xml.model.JxmModel;
import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.listeners.EditorActionListener;
import eu.scy.client.tools.scydynamics.listeners.EditorMouseListener;
import eu.scy.client.tools.scydynamics.logging.IModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.model.Model;
import eu.scy.client.tools.scydynamics.model.ModelUtils;
import eu.scy.elo.contenttype.dataset.DataSet;

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

	private final static Logger DEBUGLOGGER = Logger.getLogger(ModelEditor.class.getName());
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
	private ModelSelection aSelection;
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
	private final ResourceBundleWrapper bundle;
	// private ModelSyncControl modelSyncControl;
	private Domain domain;

	public ModelEditor() {
		this(ModelEditor.getDefaultProperties());
	}

	public ModelEditor(Properties newProps) {
		this.setName("Model Editor");
		this.domain = ModelUtils.loadDomain(newProps);
		this.bundle = new ResourceBundleWrapper(this);
		this.properties = getDefaultProperties();
		properties.putAll(newProps);
		DEBUGLOGGER.info("using properties: " + properties);
		actionLogger = new ModellingLogger(new DevNullActionLogger(), "username");
		// actionLogger = new ModellingLogger(new SystemOutActionLogger(),
		// "username");
		new JTools(JColab.JCOLABAPP_RESOURCES, JColab.JCOLABSYS_RESOURCES);
		aSelection = new ModelSelection();
		initComponents();
		setNewModel();
		setMode(properties.getProperty("editor.mode", "quantitative_modelling"));
	}

	public Domain getDomain() {
		return domain;
	}

	public void setMode(String newMode) {
		DEBUGLOGGER.info("setting mode to " + newMode);
		if (newMode == null) {
			DEBUGLOGGER.info("setting mode to 'null' not allowed, setting to default mode 'modelling'");
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
			DEBUGLOGGER.info("unknown mode '" + newMode + "', setting to default mode 'modelling'");
			setMode(Mode.QUANTITATIVE_MODELLING);
		}
	}

	public void setMode(Mode newMode) {
		if (newMode != this.mode) {
			this.mode = newMode;
			switch (mode) {
			case BLACK_BOX:
			case CLEAR_BOX:
				addGraph();
				addTable();
				toolbar.toCursorAction();
				toolbar.setEnabled(false);
				clearSelectedObjects();
				break;
			case MODEL_SKETCHING:
				removeGraphAndTable();
				toolbar.setEnabled(true);
				break;
			case QUALITATIVE_MODELLING:
			case QUANTITATIVE_MODELLING:
				addGraph();
				addTable();
				toolbar.setEnabled(true);
				break;
			}
		}
		getFileToolbar().updateMode();
	}

	public Mode getMode() {
		return this.mode;
	}

	// public ModelSyncControl getModelSyncControl() {
	// return this.modelSyncControl;
	// }

	// public boolean isSynchronized() {
	// return this.modelSyncControl != null;
	// }

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
		props.put("editor.fixedcalculationmethod", "false");
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

	public void setActionLogger(IActionLogger newLogger, String username) {
		this.actionLogger = new ModellingLogger(newLogger, username);
	}

	public FileToolbar getFileToolbar() {
		return editorTab.getFileToolbar();
	}

	private void initComponents() {
		EditorActionListener actionListener = new EditorActionListener(this);
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

		editorTab = new EditorTab(this, actionListener, bundle);
		this.editorPanel = (editorTab).getEditorPanel();
		this.toolbar = (editorTab).getToolbar();
		graphTab = new GraphTab(this, bundle);
		tableTab = new TableTab(this, bundle);

		// tabbedPane.addTab(bundle.getString("PANEL_EDITOR"),
		// Util.getImageIcon("editor_22.png"), editorTab);
		tabbedPane.add(editorTab, 0);
		tabbedPane.setTabComponentAt(0, new TabPanel(bundle.getString("PANEL_EDITOR"), editorTab, this, properties));
		if (properties.get("show.graph").equals("true")) {
			addGraph();
		}
		if (properties.get("show.table").equals("true") && this.mode != Mode.QUALITATIVE_MODELLING) {
			addTable();
		}

		this.registerKeyboardAction(actionListener, EditorToolbar.DELETE + "", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.registerKeyboardAction(actionListener, EditorToolbar.COPY + "", KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.registerKeyboardAction(actionListener, EditorToolbar.PASTE + "", KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.registerKeyboardAction(actionListener, EditorToolbar.CUT + "", KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.registerKeyboardAction(actionListener, EditorToolbar.ALL + "", KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void addGraph() {
		tabbedPane.add(graphTab, 1);
		tabbedPane.setTabComponentAt(1, new TabPanel("Graph", graphTab, this, properties));
	}

	public void addTable() {
		tabbedPane.add(tableTab, tabbedPane.getComponentCount() - 1);
		tabbedPane.setTabComponentAt(tabbedPane.getComponentCount() - 2, new TabPanel("Table", tableTab, this, properties));
	}

	public void removeGraphAndTable() {
		tabbedPane.remove(graphTab);
		tabbedPane.remove(tableTab);
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

	public String getColabToolName() {
		return "editor";
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
	}

	private JxmModel getXmModel() {
		return (model == null) ? null : model.getXmModel();
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
			DEBUGLOGGER.info("invalid variable name: null");
		} else if (name.length() < 1) {
			// userMessage =
			// JTools.getAppResourceString("editorMsgYouMustIntroduceVariableName");
			DEBUGLOGGER.info("invalid variable name: length < 1");
		} else if (model.hasObjectOfName(name)) {
			// userMessage =
			// JTools.getAppResourceString("editorMsgDuplicateVariableName",
			// name);
			DEBUGLOGGER.info("invalid variable name: duplicate name");
		} else if (JParserExpr.isToken(name)) {
			// userMessage =
			// JTools.getAppResourceString("editorMsgReservedWord",name);
			DEBUGLOGGER.info("invalid variable name: expression token");
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
				JdNode n = (JdNode) o;
				boolean b3 = true;
				if (!n.isGraphExpr()) // do not check GRAPH(...) expressions
				{
					b3 = checkExpr(n.getLabel(), n.getExpr(), model.getLinkedVariablesTo(n, false), true);
				}
				o.setSpecified(b3);
				if (!b3 && !b) {
					b = false;
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
			modelCheckMessages.add("* the expression parser encoutered an error in element " + vName);
			return false;
		}
		// all linked variables used?
		if (b && vLinks != null && bUseAllLinks) {
			Vector<String> vExpr = parser.getExprVars(aExpr);
			if (vExpr.isEmpty() && !vLinks.isEmpty()) {
				// userMessage =
				// JTools.getAppResourceString("editorMsgYouMustUseAllLinkedVariables");
				// showStatusMsg(userMessage, true);
				DEBUGLOGGER.info("not all linked variables used (1)");
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
					DEBUGLOGGER.info("not all linked variables used (2)");
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
					DEBUGLOGGER.info("not all linked variables used (3)");
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

	// ---------------------------------------------------------------------------
	// set/get editor action
	// ---------------------------------------------------------------------------
	// private void setAction(String aCmd, int a) {
	// JTools.setToolBarToggleButtonSelected(aToolbar, aCmd);
	// //aControl.setAction(a);
	// }
	// -------------------------------------------------------------------------
	// public void setFixAction(boolean b) {
	// fixAction = b;
	// aEditorVT.setFixAction(b);
	// }
	// -------------------------------------------------------------------------
	// public void restoreAction() {
	// if (!fixAction && !"cursor".equals(eAction))
	// setAction("cursor");
	// }
	// -------------------------------------------------------------------------
	// ---------------------------------------------------------------------------
	// selection: undo/copy/paste/cut
	// ---------------------------------------------------------------------------
	public ModelSelection getSelection() {
		return aSelection;
	}

	// ---------------------------------------------------------------------------
	public void copySelection() {
		aSelection.copySelection();
	}

	public void pasteSelection() {
		aSelection.pasteSelection(this, model);
	}

	public void cutSelection() {
		aSelection.cutSelection(this);
	}

	public void deleteSelection() {
		aSelection.deleteSelection(this);
	}

	// public void undoModel() {
	// aSelection.undoModel(this);
	// }
	public void saveModel() {
		aSelection.saveModel(this);
	}

	public void clearSaveFirstModel() {
		aSelection.clearSaveFirstModel();
	}

	public void saveFirstModel() {
		aSelection.saveFirstModel(this);
	}

	public void clearUndoModel() {
		aSelection.clearUndoModel();
	}

	// ---------------------------------------------------------------------------
	// public void enableUndoButton(boolean b) {
	// aEditorVT.enableUndoButton(b);
	// }
	// ---------------------------------------------------------------------------
	public Hashtable<String, JdObject> getCopySelection() {
		return aSelection.getCopySelection();
	}

	// ---------------------------------------------------------------------------
	public void setCopySelection(Vector<JdObject> v) {
		aSelection.setCopySelection(v);
	}

	// ---------------------------------------------------------------------------
	public void clearSelectedObjects() {
		aSelection.unselectAll();
		updateCanvas();
		// this.updateSpecDialog(true);
	}

	// -------------------------------------------------------------------------
	public void selectAllObjects() {
		if (model == null) {
			return;
		}
		aSelection.unselectAll();
		for (JdObject o : model.getObjects().values()) {
			aSelection.selectObject(o, true);
		}
		updateCanvas();
		// updateSpecDialog(true);
	}

	// ---------------------------------------------------------------------------
	public void selectObjects(Vector<String> v) {
		if (model == null) {
			return;
		}
		aSelection.unselectAll();
		for (String objName : v) {
			JdObject o = model.getObjectOfName(objName);
			aSelection.selectObject(o, true);
		}
		updateCanvas();
		// updateSpecDialog(true);
	}

	// ---------------------------------------------------------------------------
	public void selectObjects(Rectangle r) {
		if (model == null) {
			return;
		}
		aSelection.unselectAll();
		for (JdObject o : model.getObjects().values()) {
			if (JdTools.testR1ContainsR2(r, o.getBounds())) {
				aSelection.selectObject(o, true);
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
		aSelection.selectObject(o, bToggle);
		updateCanvas();
		// updateSpecDialog(true);
	}

	// -------------------------------------------------------------------------
	public void selectObject(String s, boolean bEvent) {
		if (model == null) {
			return;
		}
		aSelection.unselectAll();
		JdObject o = model.getObjectOfName(s);
		aSelection.selectObject(o, false);
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
		if (aSelection != null) {
			for (JdObject o : aSelection.getObjects().values()) {
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
		JdObject o = model.getObjectOfName(name);
		if (o == null) {
			return;
		}
		// aEditorVT.sendLogEvent(JvtEditorLogs.LOG_DELETE_OBJECT,o); // log
		// delete object
		model.removeObjectAndRelations(o);
		clearSelectedObjects();
		setModelChanged();
		checkModel();
		// if (bEvent)
		// sendVisualToolEvent("DeleteFigure", name);
	}

	// ---------------------------------------------------------------------------
	public void setFigureProperties(String oldName, Hashtable<String, Object> h) {
		if (model == null || oldName == null) {
			return;
		}
		JdObject o = model.getObjectOfName(oldName);
		if (o == null || h == null) {
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
			aSelection.renameObjectKey(oldName, newName);
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

	public void adjustmentValueChanged(AdjustmentEvent e) {
		Hashtable<String, Object> h = new Hashtable<String, Object>();
		h.put("orientation", (e.getAdjustable().getOrientation() == Adjustable.HORIZONTAL) ? "horizontal" : "vertical");
		h.put("value", String.valueOf(e.getValue()));
	}

	public EditorToolbar getEditorToolbar() {
		return this.editorTab.getEditorToolbar();
	}
}
