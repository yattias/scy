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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sqv.Interface;
import sqv.ModelVariable;
import sqv.data.DataServer;
import sqv.widgets.Curve;
import sqv.widgets.GraphWidget;
import sqv.widgets.VariableRef;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.model.SimquestModel;

@SuppressWarnings("serial")
public class GraphTab extends JPanel implements ChangeListener, ActionListener {

	private ModelEditor editor;
	private VariableSelectionPanel variablePanel;
	private JPanel graphPanel;
	private DataServer dataServer;
	private GraphWidget graph;
	private SimulationSettingsPanel simulationPanel;
	private Interface iface;
	private SimquestModel sqModel;
	private final ResourceBundleWrapper bundle;
	private JComboBox xAxisSelector;

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
		this.add(westPanel, BorderLayout.WEST);
		this.add(createGraph(), BorderLayout.CENTER);
	}

	public void updateXAxisSelector() {
		xAxisSelector.removeAllItems();
		xAxisSelector.addItem("time");
		for (String varName : variablePanel.getVariables().keySet()) {
			xAxisSelector.addItem(varName);
		}
	}

	private JPanel createGraph() {
		graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());
		graphPanel.setBorder(BorderFactory.createTitledBorder(bundle
				.getString("PANEL_GRAPH")));
		dataServer = new DataServer();
		// new ScreenOutputDataClient(dataServer);
		graph = new GraphWidget();
		graph.setBasics(true, true);
		iface = new Interface(dataServer);
		iface.addWidget(graph);
		graph.setInterface(iface);
		graphPanel.add(graph.getComponent(), BorderLayout.CENTER);

		JPanel axisPanel = new JPanel();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.RIGHT);
		axisPanel.setLayout(flow);
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
		if (e.getActionCommand().equals("run")) {
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

			graphPanel.remove(graph.getComponent());
			iface.removeWidget(graph);
			graph = new GraphWidget();
			graph.setBasics(true, true);
			iface.addWidget(graph);
			graph.setInterface(iface);
			graphPanel.add(graph.getComponent(), BorderLayout.CENTER);
			graphPanel.updateUI();

			// create the SimQuest model from the CoLab model
			sqModel = new SimquestModel(editor.getModel());
			sqv.Model model = new sqv.Model(sqModel, dataServer);

			// adding the curves
			// the old way
			// List<ModelVariable> variables = model.getVariables();
			// VariableRef timeRef = null;
			// List<String> selectedIDs = new LinkedList<String>();
			// for (ModelVariable var : variables) {
			// // getting a reference to the time variable
			// if (var.getKind() == ModelVariable.VK_TIME) {
			// timeRef = new VariableRef(var);
			// }
			// }
			// List<Curve> curves = new LinkedList<Curve>();
			// for (int i = 0; i < variables.size(); i++) {
			// // if the variable is one of the selected ones
			// if
			// (variablePanel.getSelectedVariables().contains(variables.get(i).getName()))
			// {
			// curves.add(new Curve(i, timeRef, new
			// VariableRef(variables.get(i)),
			// editor.getModel().getNodes().get(variables.get(i).getName()).getLabelColor(),
			// (float) 1.5, variables.get(i).getName()));
			// }
			// }

			// and the new way

			// get the x-axis-variable
			//List<ModelVariable> variables = model.getVariables();
			VariableRef xAxisVariable = null;
			for (ModelVariable var : model.getVariables()) {
				if (var.getName().equals(
					xAxisSelector.getSelectedItem().toString())) {
					xAxisVariable = new VariableRef(var);
					break;
				}
			}
			List<Curve> curves = new LinkedList<Curve>();
			int count = 0;
			for (ModelVariable var : model.getVariables()) {
				// if the variable is one of the selected ones
				if (variablePanel.getSelectedVariables()
						.contains(var.getName())) {
					curves.add(new Curve(count++, xAxisVariable, new VariableRef(
							var), editor.getModel().getNodes()
							.get(var.getName()).getLabelColor(),
							(float) 1.5, var.getName()));
				}
			}

			graph.setCurvesList(curves);
			graph.update();

			// need the variableIdList for logging
			String variableIdList = new String();
			for (String varname : variablePanel.getSelectedVariables()) {
				variableIdList = variableIdList.concat(editor.getModel()
						.getObjectOfName(varname).getID()
						+ ", ");
			}

			if (variableIdList.length() > 0) {
				// simulate
				System.out.println("simulating...");
				model.getSimulation().Simulate();
				// log
				editor.getActionLogger().logSimpleAction("run_model",
						editor.getXmModel().getXML("", true));
				editor.getActionLogger()
						.logInspectVariablesAction(
								"inspect_gaph",
								variableIdList.substring(0,
										variableIdList.length() - 2));
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
		sqModel = new SimquestModel(editor.getModel());
		sqv.Model model = new sqv.Model(sqModel, dataServer);
		FileDialog dialog = new FileDialog((Frame) editor.getRootPane()
				.getParent(), bundle.getString("PANEL_SAVESQX"),
				FileDialog.SAVE);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void injectSimulationSettings() throws NumberFormatException {
		editor.getModel().setStart(
				Double.parseDouble(simulationPanel.getStart()));
		editor.getModel()
				.setStop(Double.parseDouble(simulationPanel.getStop()));
		editor.getModel()
				.setStep(Double.parseDouble(simulationPanel.getStep()));
		editor.getModel().setMethod(simulationPanel.getMethod());
	}
}
