package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import eu.scy.client.tools.scydynamics.model.SimquestModel;


import sqv.Interface;
import sqv.ModelVariable;
import sqv.data.DataServer;
import sqv.data.ScreenOutputDataClient;
import sqv.widgets.Curve;
import sqv.widgets.GraphWidget;
import sqv.widgets.VariableRef;

public class GraphTab extends JPanel implements ChangeListener, ActionListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2337114562359454914L;
	private ModelEditor editor;
	private VariableSelectionPanel variablePanel;
	private JPanel graphPanel;
	private DataServer dataServer;
	private GraphWidget graph;
	private SimulationSettingsPanel simulationPanel;
	private Interface iface;
	private SimquestModel sqModel;
	
	public GraphTab(ModelEditor editor) {
		super();
		this.editor = editor;
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(variablePanel = new VariableSelectionPanel(editor), BorderLayout.NORTH);
		westPanel.add(simulationPanel = new SimulationSettingsPanel(editor, this), BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(createGraph(), BorderLayout.CENTER);
	}
	
	private JPanel createGraph() {
		graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());
		graphPanel.setBorder(BorderFactory.createTitledBorder("graph"));
		dataServer = new DataServer();
		//new ScreenOutputDataClient(dataServer);
		graph = new GraphWidget();
		graph.setBasics(true, true);
		iface = new Interface(dataServer);
		iface.addWidget(graph);
		graph.setInterface(iface);
		graphPanel.add(graph.getComponent(), BorderLayout.CENTER);
		return graphPanel;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		variablePanel.updateVariables();
		simulationPanel.updateSettings();
		editor.getActionLogger().logActivateWindow("graph", null, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("run")) {
			editor.checkModel();
			if (editor.getModelCheckMessages().size()>0) {
				String messages = new String("The model cannot be executed, because\n");
				for (String msg: editor.getModelCheckMessages()) {
					messages = messages+msg+"\n";
				}
				JOptionPane.showMessageDialog(null, messages);
				return;				
			}
			try {
				injectSimulationSettings();
			} catch(NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Couldn't parse the simulation settings.\nPlease check."); 
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
			List<ModelVariable> variables = model.getVariables();
			VariableRef timeRef = null;
			List<String> selectedIDs = new LinkedList<String>();
			for (ModelVariable var : variables){
				// getting a reference to the time variable
				if (var.getKind()==ModelVariable.VK_TIME) {
					timeRef = new VariableRef(var);
				}
			}
			List<Curve> curves = new LinkedList<Curve>();
			for (int i=0; i<variables.size(); i++){
				// if the variable is one of the selected ones
				if (variablePanel.getSelectedVariables().contains(variables.get(i).getName())) {
					curves.add(new Curve(i, timeRef, new VariableRef(variables.get(i)), editor.getModel().getNodes().get(variables.get(i).getName()).getLabelColor(), (float) 1.5, variables.get(i).getName()));
				}
			}	
			graph.setCurvesList(curves);
			graph.update();
			
			String variableIdList = new String();
			for (String varname : variablePanel.getSelectedVariables()) {
				variableIdList = variableIdList.concat(editor.getModel().getObjectOfName(varname).getID()+", ");
			}
			
			if (variableIdList.length() > 0) {
				// simulate
				model.getSimulation().Simulate();	
				// log
				editor.getActionLogger().logSimpleAction("run_model", editor.getXmModel().getXML("", true));			
				editor.getActionLogger().logInspectVariablesAction("inspect_gaph", variableIdList.substring(0, variableIdList.length()-2));		
			} else {
				JOptionPane.showMessageDialog(null, "You should select at least one\nvariable for plotting."); 
			}
			
						
		} else if (e.getActionCommand().equals("export")) {
			export();			
		}
	}

	private void export() {
		editor.checkModel();
		if (editor.getModelCheckMessages().size()>0) {
			String messages = new String("The model cannot be executed, because\n");
			for (String msg: editor.getModelCheckMessages()) {
				messages = messages+msg+"\n";
			}
			JOptionPane.showMessageDialog(null, messages);
			return;				
		}
		try {
			injectSimulationSettings();
		} catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Couldn't parse the simulation settings.\nPlease check."); 
			return;
		}
		// create the SimQuest model from the CoLab model
		sqModel = new SimquestModel(editor.getModel());
		sqv.Model model = new sqv.Model(sqModel, dataServer);
		FileDialog dialog = new FileDialog((Frame) editor.getRootPane()
				.getParent(), "Save model as a SimQuest-sqx file...", FileDialog.SAVE);
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
				FileWriter writer = new FileWriter(dialog.getDirectory()+dialog.getFile());
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
			editor.getModel().setStart(Double.parseDouble(simulationPanel.getStart()));
			editor.getModel().setStop(Double.parseDouble(simulationPanel.getStop()));
			editor.getModel().setStep(Double.parseDouble(simulationPanel.getStep()));
			editor.getModel().setMethod(simulationPanel.getMethod());
	}
	
}
