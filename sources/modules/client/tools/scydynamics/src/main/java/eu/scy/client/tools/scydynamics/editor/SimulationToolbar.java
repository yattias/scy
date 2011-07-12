package eu.scy.client.tools.scydynamics.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import eu.scy.client.tools.scydynamics.model.Model;
import eu.scy.client.tools.scydynamics.model.SimquestModelQualitative;
import eu.scy.client.tools.scydynamics.model.SimquestModelQuantitative;
import java.util.logging.Logger;

import sqv.Interface;
import sqv.ModelVariable;
import sqv.data.DataServer;
import sqv.data.ScreenOutputDataClient;
import sqv.widgets.Curve;
import sqv.widgets.GraphWidget;
import sqv.widgets.VariableRef;


public class SimulationToolbar extends JToolBar implements ActionListener {

        private final static Logger LOGGER = Logger.getLogger(SimulationToolbar.class.getName());
	private ModelEditor editor;
	private SimquestModelQuantitative sqModel;

	public SimulationToolbar(ModelEditor editor) {
		super(JToolBar.VERTICAL);
		this.editor = editor;
		setFloatable(false);
		add(createButton("settings", "settings"));
		add(createButton("run", "run"));
	}

	private JButton createButton(String label, String command) {
		JButton button = new JButton(label);
		button.setActionCommand(command);
		button.addActionListener(this);
		return button;
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("settings")) {
			java.awt.Frame frame = javax.swing.JOptionPane.getFrameForComponent(this);
			SimulationDialog simdialog = new SimulationDialog(frame, editor.getModel());
		} else if (evt.getActionCommand().equals("run")) {
			// LOGGER.info(editor.getModel());
			if (editor.getMode().equals(ModelEditor.Mode.QUALITATIVE_MODELLING)) {
				sqModel = new SimquestModelQualitative(editor);
			} else {
				sqModel = new SimquestModelQuantitative(editor);
			}
			// LOGGER.info(new XMLOutputter(Format.getPrettyFormat()).outputString(sqModel));
			DataServer dataServer = new DataServer();
			sqv.Model model = new sqv.Model(sqModel, dataServer);
			//new ScreenOutputDataClient(dataServer);
						
			JFrame graphFrame = new JFrame("graph");
			graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			graphFrame.setSize(300,300);

			GraphWidget graph = new GraphWidget();
			graph.setBasics(true, true);
			Interface iface = new Interface(dataServer);
			iface.addWidget(graph);
			graph.setInterface(iface);
						
			List<ModelVariable> variables = model.getVariables();
			VariableRef timeRef = null;
			for (ModelVariable var : variables){
				// getting a reference to the time variable
				if (var.getKind()==ModelVariable.VK_TIME) {
					timeRef = new VariableRef(var);
				}
			}
			
			for (int i=0; i<variables.size(); i++){
				if (variables.get(i).getKind()==ModelVariable.VK_STATE) {
					// adding curves for the state variables in the model
					// LOGGER.info("SimulationToolbar.actionPerformed. adding curve for "+variables.get(i).getName());
					graph.addCurve(new Curve(i, timeRef, new VariableRef(variables.get(i)), Color.RED, (float) 5.0, variables.get(i).getName()));
				}
			}	

			graphFrame.getContentPane().add(graph.getComponent());
			graphFrame.setVisible(true);
			
			model.getSimulation().Simulate();
		}
	}

}