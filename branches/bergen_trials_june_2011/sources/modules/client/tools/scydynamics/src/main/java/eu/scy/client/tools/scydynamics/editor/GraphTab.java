package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sqv.Interface;
import sqv.Model;
import sqv.ModelVariable;
import sqv.data.DataServer;
import sqv.widgets.Curve;
import sqv.widgets.GraphWidget;
import sqv.widgets.VariableRef;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.model.SimquestModelQuantitative;

@SuppressWarnings("serial")
public class GraphTab extends JPanel implements Runnable, ChangeListener, ActionListener {

	private ModelEditor editor;
	private VariableSelectionPanel variablePanel;
	private JPanel graphPanel;
	private DataServer dataServer;
	private GraphWidget graph;
	private SimulationSettingsPanel simulationPanel;
	private Interface iface;
	private SimquestModelQuantitative sqModel;
	private final ResourceBundleWrapper bundle;
	private JComboBox xAxisSelector;
	private LinkedList<Curve> curves;
	private JCheckBox multiPlotsCheckBox;
	private Thread simulationThread;
	private Model sqvModel;

	public GraphTab(ModelEditor editor, ResourceBundleWrapper bundle) {
		super();
		this.editor = editor;
		this.bundle = bundle;
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(variablePanel = new VariableSelectionPanel(editor,
				bundle, false), BorderLayout.NORTH);
		westPanel.add(simulationPanel = new SimulationSettingsPanel(editor,
				this), BorderLayout.CENTER);
		JScrollPane scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setViewportView(westPanel);
		this.add(scroller, BorderLayout.WEST);
		this.add(createGraphPanel(), BorderLayout.CENTER);
		clearGraph();
	}

	public void updateXAxisSelector() {
		xAxisSelector.removeAllItems();
		xAxisSelector.addItem("time");
		for (String varName : variablePanel.getVariables().keySet()) {
			xAxisSelector.addItem(varName);
		}
	}

	private void prepareGraph() {
		dataServer = new DataServer();
		iface = new Interface(dataServer);
		iface.addWidget(graph);
		graph.setInterface(iface);
	}

	private void clearGraph() {
		curves = new LinkedList<Curve>();
		graphPanel.remove(graph.getComponent());
		graph = new GraphWidget();
		graph.setBasics(true, true);
		graphPanel.add(graph.getComponent(), BorderLayout.CENTER);
		graphPanel.updateUI();
	}

	private JPanel createGraphPanel() {
		graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());
		graphPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PANEL_GRAPH")));
		curves = new LinkedList<Curve>();
		graph = new GraphWidget();
		graph.setBasics(true, true);
		graphPanel.add(graph.getComponent(), BorderLayout.CENTER);
		JPanel axisPanel = new JPanel();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.RIGHT);
		axisPanel.setLayout(flow);
		multiPlotsCheckBox = new JCheckBox();
		axisPanel.add(new JLabel("multiple plots"));
		axisPanel.add(multiPlotsCheckBox);
		JButton button = new JButton("clear graph");
		button.setActionCommand("clear");
		button.addActionListener(this);
		axisPanel.add(button);
		xAxisSelector = new JComboBox();
		updateXAxisSelector();
		axisPanel.add(xAxisSelector);
		graphPanel.add(axisPanel, BorderLayout.SOUTH);
		return graphPanel;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		variablePanel.updateVariables();
		simulationPanel.updateSettings();
		this.updateXAxisSelector();
		editor.getActionLogger().logActivateWindow("graph", null, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("clear")) {
			this.clearGraph();
		} else if (e.getActionCommand().equals("run")) {
			editor.checkModel();
			// can the model be parsed?
			if (editor.getModelCheckMessages().size() > 0) {
				String messages = new String(
						bundle.getString("PANEL_CANNOTEXECUTE") + "\n");
				for (String msg : editor.getModelCheckMessages()) {
					messages = messages + msg + "\n";
				}
				JOptionPane.showMessageDialog(null, messages);
				return;
			}
			// can the variable values be parsed?
			try {
				variablePanel.getValues();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "The variable values cannot be parsed correctly.\n Please check and try again.");
				return;
			}
			// can the simulation settings be parsed?
			try {
				injectSimulationSettings();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						bundle.getString("PANEL_CANNOTPARSE"));
				return;
			}

			if (!multiPlotsCheckBox.isSelected()) {
				clearGraph();
			}
			prepareGraph();

			// create the SimQuest model from the CoLab model
			sqModel = new SimquestModelQuantitative(editor.getModel(), variablePanel.getValues());
			sqvModel = new sqv.Model(sqModel, dataServer);

			//fade out existing curves
			for (Curve curve : curves) {
				curve.setColor(curve.getColor().brighter());
				curve.setLineWidth(1);
			}

			// adding the new curves
			// get the x-axis-variable
			VariableRef xAxisVariable = null;
			for (ModelVariable var : sqvModel.getVariables()) {
				if (var.getName().equals(
						xAxisSelector.getSelectedItem().toString())) {
					xAxisVariable = new VariableRef(var);
					break;
				}
			}
			for (ModelVariable var : sqvModel.getVariables()) {
				// if the variable is one of the selected ones
				if (variablePanel.getSelectedVariables().contains(var.getName())) {
					curves.add(new Curve(curves.size(), xAxisVariable, new VariableRef(var),
							editor.getModel().getNodes().get(var.getName()).getLabelColor(),
							(float) 2, null));
				}
			}

			graph.setCurvesList(curves);
			// need the variableIdList for logging
			String variableIdList = new String();
			for (String varname : variablePanel.getSelectedVariables()) {
				variableIdList = variableIdList.concat(editor.getModel().getObjectOfName(varname).getID()+ "; ");
			}

			if (variablePanel.getSelectedVariables().size() > 0) {
				// simulate
				//LOGGER.info("starting simulation");
				simulationThread = new Thread(this);
				simulationThread.start();
				// log
				String injectedVariables = "";
				for (String varName: variablePanel.getValues().keySet()) {
					injectedVariables = injectedVariables + editor.getModel().getObjectOfName(varName).getID() + "=" + variablePanel.getValues().get(varName)+"; ";
				}
				editor.getActionLogger().logModelRan(editor.getXmModel().getXML("", true), injectedVariables.substring(0, injectedVariables.length()-2));
				editor.getActionLogger().logInspectVariablesAction(ModellingLogger.GRAPH_VIEWED, variableIdList.substring(0, variableIdList.length()-2));
			} else {
				JOptionPane.showMessageDialog(null,
						bundle.getString("PANEL_SELECTVARIABLE"));
			}

		} else if (e.getActionCommand().equals("export")) {
			export();
		}
	}

	private void export() {
		editor.checkModel();
		if (editor.getModelCheckMessages().size() > 0) {
			String messages = new String(
					bundle.getString("PANEL_CANNOTEXECUTE") + "\n");
			for (String msg : editor.getModelCheckMessages()) {
				messages = messages + msg + "\n";
			}
			JOptionPane.showMessageDialog(null, messages);
			return;
		}
		try {
			injectSimulationSettings();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,
					bundle.getString("PANEL_CANNOTPARSE"));
			return;
		}
		// create the SimQuest model from the CoLab model
		sqModel = new SimquestModelQuantitative(editor.getModel());
		sqv.Model model = new sqv.Model(sqModel, dataServer);
		FileDialog dialog = new FileDialog((Frame) editor.getRootPane().getParent(), bundle.getString("PANEL_SAVESQX"), FileDialog.SAVE);
		dialog.setFile("*.sqx");
		dialog.setVisible(true);
		if (dialog.getFile() != null) {
			Element application = new Element("application");
			Element topics = new Element("topics");
			Element topic = new Element("topic");
			topic.setAttribute("id", java.util.UUID.randomUUID().toString());
			topic.addContent(sqModel);
			topics.addContent(topic);
			application.addContent(topics);
			Document doc = new Document(application);
			try {
				XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
				FileWriter writer = new FileWriter(dialog.getDirectory()
						+ dialog.getFile());
				out.output(doc, writer);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void injectSimulationSettings() throws NumberFormatException {
		if (editor.isQualitative()) {
			editor.getModel().setStart(-0.9);
			editor.getModel().setStop(0.9);
			editor.getModel().setStep(0.1);
			editor.getModel().setMethod("RungeKuttaFehlberg");
		} else {
			editor.getModel().setStart(Double.parseDouble(simulationPanel.getStart()));
			editor.getModel().setStop(Double.parseDouble(simulationPanel.getStop()));
			editor.getModel().setStep(Double.parseDouble(simulationPanel.getStep()));
			editor.getModel().setMethod(simulationPanel.getMethod());
		}
	}

	@Override
	public void run() {
		WaiterDialog waiter = new WaiterDialog(graphPanel, simulationThread, "please wait...", "running model...");	
		sqvModel.getSimulation().Simulate();
		waiter.breakGlass();
		waiter.dispose();
		// for some reason, it's important to call these after the WaiterDialog has been disposed
		// modality vs. update issue?
		graph.update();
		graphPanel.updateUI();
		graphPanel.repaint();

	}
}
