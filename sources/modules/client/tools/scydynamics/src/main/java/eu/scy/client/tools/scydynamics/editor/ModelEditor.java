package eu.scy.client.tools.scydynamics.editor;

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

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.collaboration.ModelSyncControl;
import eu.scy.client.tools.scydynamics.listeners.EditorActionListener;
import eu.scy.client.tools.scydynamics.listeners.EditorMouseListener;
import eu.scy.client.tools.scydynamics.logging.IModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.model.Model;
import eu.scy.elo.contenttype.dataset.DataSet;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ModelEditor extends JPanel implements AdjustmentListener {

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
	private JdPopups aPopups;
	private Model aModel = null;
	private ModelSelection aSelection;
	private EditorPanel aCanvas;
	private JScrollPane aScrollPane;
	private EditorMouseListener mouseListener;
	private int currentCursor;
	private JdCursors cursors;
	private EditorToolbar toolbar;
	private IModellingLogger actionLogger;
	//private JTools jtools;
	private EditorTab editorTab;
	private ArrayList<String> modelCheckMessages = new ArrayList<String>();
	private final Properties properties;
	protected JTabbedPane tabbedPane;
	private GraphTab graphTab;
	private TableTab tableTab;
	private final ResourceBundleWrapper bundle;
	private ModelSyncControl modelSyncControl;

	// private String eloUri = "n/a";

	public ModelEditor() {
		this(ModelEditor.getDefaultProperties());
	}

	public ModelEditor(Properties newProps) {
		this.setName("Model Editor");
		this.bundle = new ResourceBundleWrapper(this);
		this.properties = getDefaultProperties();
		properties.putAll(newProps);
		actionLogger = new ModellingLogger(new DevNullActionLogger(), "username");
		// actionLogger = new ModellingLogger(new SystemOutActionLogger(),
		// "username");
		new JTools(JColab.JCOLABAPP_RESOURCES, JColab.JCOLABSYS_RESOURCES);
		aSelection = new ModelSelection();
		initComponents();
		setNewModel();
	}

	public boolean isQualitative() {
		if (properties.getProperty("editor.qualitative").equals("true")) {
			return true;
		} else {
			return false;
		}
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

	public void setQualitative(boolean q) {
		properties.put("editor.qualitative", q);
	}

	public void setActionLogger(IActionLogger newLogger, String username) {
		this.actionLogger = new ModellingLogger(newLogger, username);
	}

	// public void setEloUri(String eloUri) {
	// this.eloUri = eloUri;
	// this.actionLogger.setEloUri(eloUri);
	// }

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
		props.put("editor.qualitative", "false");
		return props;
	}

	public Properties getProperties() {
		return properties;
	}

	public IModellingLogger getActionLogger() {
		return actionLogger;
	}

	public FileToolbar getFileToolbar() {
		return editorTab.getFileToolbar();
	}

	private void initComponents() {
		EditorActionListener actionListener = new EditorActionListener(this);
		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent evt) {
				JTabbedPane pane = (JTabbedPane) evt.getSource();
				((ChangeListener) pane.getSelectedComponent()).stateChanged(evt);
			}
		});
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		editorTab = new EditorTab(this, actionListener, bundle);
		this.aCanvas = (editorTab).getEditorPanel();
		this.toolbar = (editorTab).getToolbar();
		graphTab = new GraphTab(this, bundle);
		tableTab = new TableTab(this, bundle);

		tabbedPane.addTab(bundle.getString("PANEL_EDITOR"), new ImageIcon(JTools.getSysResourceImage("JvtEditor")), editorTab);
		if (properties.get("show.graph").equals("true")) {
			addGraph();
		}
		if (properties.get("show.table").equals("true") && !this.isQualitative()) {
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
		tabbedPane.setTabComponentAt(1, new TabPanel("graph", graphTab, this, properties));
	}

	public void addTable() {
		tabbedPane.add(tableTab, tabbedPane.getComponentCount() - 1);
		tabbedPane.setTabComponentAt(tabbedPane.getComponentCount() - 2, new TabPanel("table", tableTab, this, properties));
	}

	// ---------------------------------------------------------------------------
	// free memory
	// ---------------------------------------------------------------------------
	public void destroyEditor() {
		MouseMotionListener[] mmls = this.getListeners(MouseMotionListener.class);
		for (int i = 0; i < mmls.length; i++) {
			try {
				this.removeMouseMotionListener(mmls[i]);
			} catch (Exception ex) {
				System.err.println("JdEditor->destroyEditor removeMouseMotionListener exception: " + ex);
			}
		}
		MouseListener[] mls = this.getListeners(MouseListener.class);
		for (int i = 0; i < mls.length; i++) {
			try {
				this.removeMouseListener(mls[i]);
			} catch (Exception ex) {
				System.err.println("JdEditor->destroyEditor removeMouseListener exception: " + ex);
			}
		}
		aScrollPane.getVerticalScrollBar().removeAdjustmentListener(this);
		aScrollPane.getHorizontalScrollBar().removeAdjustmentListener(this);
		this.resetKeyboardActions();
		aModel = null;
		aPopups = null;
		// aControl = null;
		aSelection = null;
		aCanvas = null;
		aScrollPane = null;
	}

	public Model getModel() {
		return aModel;
	}

	public EditorPanel getCanvas() {
		return aCanvas;
	}

	public int getCurrentAction() {
		return toolbar.getCurrentAction();
	}

	public String getColabToolName() {
		return "editor";
	}
	
	public void setNewModel() {
		aCanvas.removeMouseListener(mouseListener);
		aCanvas.removeMouseMotionListener(mouseListener);
		aModel = new Model(this);
		aModel.setMethod("euler");
		aCanvas.setModel(aModel);
		mouseListener = new EditorMouseListener(this, aModel);
		aCanvas.addMouseListener(mouseListener);
		aCanvas.addMouseMotionListener(mouseListener);
	}

	public JxmModel getXmModel() {
		return (aModel == null) ? null : aModel.getXmModel();
	}

	public void setXmModel(JxmModel m) {
		clearSelectedObjects();
		if (m != null) {
			if (aModel == null) {
				mouseListener = new EditorMouseListener(this, aModel);
				aCanvas.addMouseListener(mouseListener);
				aCanvas.addMouseMotionListener(mouseListener);
			}
			aModel.setXmModel(m);
			aCanvas.setModel(aModel);
		} else {
			aModel = null;
			aCanvas.setModel(null);
			aCanvas.removeMouseListener(mouseListener);
		}
	}

	public String getModelXML() {
		if (aModel == null) {
			return null;
		}
		JxmModel m = aModel.getXmModel(); // pass from JdObjects to JxmModel
		return m.getXML("", true); // get XML model string
	}

//	public String getUserMessage() {
//		return userMessage;
//	}

	// -------------------------------------------------------------------------
	public boolean isValidName(String name) {
		if (aModel == null) {
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
		} else if (aModel.hasObjectOfName(name)) {
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
		if (aModel == null) {
			return false;
		}
		for (JdObject o : aModel.getObjects().values()) {
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
		if (aModel == null) {
			return true;
		}
		boolean b = true;
		JParserExpr parser = new JParserExpr();
		// set qualitative/graph expressions
		aModel.updateAuxExpressions();
		// add variables to parser
		for (JdObject o : aModel.getObjects().values()) {
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
		for (JdObject o : aModel.getObjects().values()) {
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
		for (JdObject o : aModel.getObjects().values()) {
			switch (o.getType()) {
			// case JdFigure.DATASET:
			case JdFigure.AUX:
			case JdFigure.STOCK:
				JdNode n = (JdNode) o;
				boolean b3 = true;
				if (!n.isGraphExpr()) // do not check GRAPH(...) expressions
				{
					b3 = checkExpr(n.getLabel(), n.getExpr(), aModel.getLinkedVariablesTo(n, false), true);
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
		if (aModel == null) {
			return true;
		}
		JParserExpr parser = new JParserExpr();
		if (vLinks == null) { // use model variables
			for (JdObject o : aModel.getObjects().values()) {
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
		aSelection.pasteSelection(this, aModel);
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
		this.updateSpecDialog(true);
	}

	// -------------------------------------------------------------------------
	public void selectAllObjects() {
		if (aModel == null) {
			return;
		}
		aSelection.unselectAll();
		for (JdObject o : aModel.getObjects().values()) {
			aSelection.selectObject(o, true);
		}
		updateCanvas();
		updateSpecDialog(true);
	}

	// ---------------------------------------------------------------------------
	public void selectObjects(Vector<String> v) {
		if (aModel == null) {
			return;
		}
		aSelection.unselectAll();
		for (String objName : v) {
			JdObject o = aModel.getObjectOfName(objName);
			aSelection.selectObject(o, true);
		}
		updateCanvas();
		updateSpecDialog(true);
	}

	// ---------------------------------------------------------------------------
	public void selectObjects(Rectangle r) {
		if (aModel == null) {
			return;
		}
		aSelection.unselectAll();
		for (JdObject o : aModel.getObjects().values()) {
			if (JdTools.testR1ContainsR2(r, o.getBounds())) {
				aSelection.selectObject(o, true);
			}
		}
		updateCanvas();
		updateSpecDialog(true);
	}

	// -------------------------------------------------------------------------
	public void selectObject(JdObject o, boolean bToggle) {
		if (aModel == null) {
			return;
		}
		aSelection.selectObject(o, bToggle);
		updateCanvas();
		updateSpecDialog(true);
	}

	// -------------------------------------------------------------------------
	public void selectObject(String s, boolean bEvent) {
		if (aModel == null) {
			return;
		}
		aSelection.unselectAll();
		JdObject o = aModel.getObjectOfName(s);
		aSelection.selectObject(o, false);
		updateCanvas();
		// if (bEvent)
		// sendVisualToolEvent("SelectObject", s);
	}

	// ---------------------------------------------------------------------------
	// control
	// ---------------------------------------------------------------------------
	public void updateCanvas() {
		aCanvas.repaint();
	}

	// public void setModelChanged() { aEditorVT.setModelChecked(false); }
	// TODO
	public void setModelChanged() {
		// // LOGGER.info("JdEditorStandalone.setModelChanged() called.");
	}

	// TODO
	// public void sendLogEventAddObject(JdObject o) {
	// aEditorVT.sendLogEvent(JvtEditorLogs.LOG_ADD_OBJECT,o); }
	// ---------------------------------------------------------------------------
	public void startRect(int x, int y) {
		aCanvas.startRect(x, y);
	}

	public void extendRect(int x, int y) {
		aCanvas.extendRect(x, y);
	}

	public Rectangle stopRect() {
		return aCanvas.stopRect();
	}

	// ---------------------------------------------------------------------------
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

	// ---------------------------------------------------------------------------
	// specify variable dialog methods
	// ---------------------------------------------------------------------------
	public Vector<String> getSelectedObjects() {
		Vector<String> v = new Vector<String>();
		if (aSelection != null) {
			for (JdObject o : aSelection.getObjects().values()) {
				v.add(o.getLabel());
			}
		}
		return v;
	}

	// ---------------------------------------------------------------------------
	// public void showSpecDialog() { aEditorVT.showSpecDialog(); }
	public void showSpecDialog(JdFigure figure, java.awt.Point position) {
		java.awt.Frame frame = javax.swing.JOptionPane.getFrameForComponent(this);
		VariableDialog vdialog = new VariableDialog(frame, position, figure, this, bundle);
		vdialog.setVisible(true);
		aCanvas.updateUI();
	}

	// public void updateSpecDialog(boolean bCheck) {
	// aEditorVT.updateSpecDialog(bCheck); }
	// TODO
	public void updateSpecDialog(boolean bCheck) {
		// // LOGGER.info("JdEditorStandalone.updateSpecDialog() called.");
		// String noSpecMsg =
		// JTools.getAppResourceString("EditorSpecMsgSelNone");
		// if (aModel==null) { wSpec.loadProperties(null,noSpecMsg); return; }
		// String oldObject = wSpec.getLastVarName();
		// if (bCheck && !wSpec.allowChange()) {
		// wSpec.vtwShowWindow();
		// aEditor.selectObject(oldObject, true);
		// return;
		// }
		// Hashtable<String,Object> h = null;
		// Vector<String> vsel = aEditor.getSelectedObjects();
		// int nsel = vsel.size();
		// if (nsel<1) { // none selected
		// h = null;
		// noSpecMsg = JTools.getAppResourceString("EditorSpecMsgSelNone");
		// } else if (nsel>1) { // multiple selection
		// h = null;
		// noSpecMsg = JTools.getAppResourceString("EditorSpecMsgSelMultiple");
		// } else { // only one object selected
		// String newObject = vsel.firstElement();
		// JdObject o = aModel.getObjectOfName(newObject);
		// if (o.isFlow()) {
		// noSpecMsg = JTools.getAppResourceString("EditorSpecMsgSelFlow");
		// } else if (o.isNode()) {
		// JdNode fNode = (JdNode) o;
		// h = fNode.getProperties();
		// Vector<String> vl =
		// JTools.sortStringVector(aModel.getLinkedVariablesTo(fNode,false));
		// h.put("linked", vl);
		// h.put("linkedColors", aModel.getObjectsLabelColors(vl));
		// if (o.isAux()) {
		// h.put("linkedRel",
		// JTools.sortStringVector(aModel.getLinkedVariablesTo(fNode,true)));
		// }
		// } else if (o.isRelation()) {
		// noSpecMsg = JTools.getAppResourceString("EditorSpecMsgSelRelation");
		// JdRelation r = (JdRelation) o;
		// if (r.canBeQualitative()) h = o.getProperties();
		// }
		// }
		// wSpec.loadProperties(h,noSpecMsg);
	}

	// ---------------------------------------------------------------------------
	public void deleteFigure(String name, boolean bEvent) {
		if (aModel == null || name == null) {
			return;
		}
		JdObject o = aModel.getObjectOfName(name);
		if (o == null) {
			return;
		}
		// aEditorVT.sendLogEvent(JvtEditorLogs.LOG_DELETE_OBJECT,o); // log
		// delete object
		aModel.removeObjectAndRelations(o);
		clearSelectedObjects();
		setModelChanged();
		checkModel();
		// if (bEvent)
		// sendVisualToolEvent("DeleteFigure", name);
	}

	// ---------------------------------------------------------------------------
	public void setFigureProperties(String oldName, Hashtable<String, Object> h) {
		if (aModel == null || oldName == null) {
			return;
		}
		JdObject o = aModel.getObjectOfName(oldName);
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
		aCanvas.repaint();
		// rename JdModel key
		String newName = h.containsKey("label") ? (String) h.get("label") : null;
		if (newName != null && !oldName.equals(newName)) {
			aModel.renameObjectKey(oldName, newName);
			aSelection.renameObjectKey(oldName, newName);
		}
		if (h.containsKey("relations")) {
			aModel.updateNodeRelations(((JdNode) o).getLabel(), (Hashtable) h.get("relations"));
			// updateSpecDialog(false);
		} else if (bChangeRel) {
			aModel.changeNodeRelations(((JdNode) o).getLabel(), ((Integer) h.get("exprType")).intValue());
			updateSpecDialog(false);
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
