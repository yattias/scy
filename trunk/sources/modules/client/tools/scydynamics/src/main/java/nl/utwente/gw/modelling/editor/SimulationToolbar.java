package nl.utwente.gw.modelling.editor;

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

import sqv.Interface;
import sqv.ModelVariable;
import sqv.data.DataServer;
import sqv.data.ScreenOutputDataClient;
import sqv.widgets.Curve;
import sqv.widgets.GraphWidget;
import sqv.widgets.VariableRef;

import nl.utwente.gw.modelling.model.Model;
import nl.utwente.gw.modelling.model.SimquestModel;

public class SimulationToolbar extends JToolBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5071178017935714682L;
	private ModelEditor editor;
	private SimquestModel sqModel;

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
			System.out.println(editor.getModel());
			sqModel = new SimquestModel(editor.getModel());
			System.out.println(new XMLOutputter(Format.getPrettyFormat()).outputString(sqModel));
			DataServer dataServer = new DataServer();
			sqv.Model model = new sqv.Model(sqModel, dataServer);
			new ScreenOutputDataClient(dataServer);
						
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
					System.out.println("SimulationToolbar.actionPerformed. adding curve for "+variables.get(i).getName());
					graph.addCurve(new Curve(i, timeRef, new VariableRef(variables.get(i)), Color.RED, (float) 5.0, variables.get(i).getName()));
				}
			}	

			graphFrame.getContentPane().add(graph.getComponent());
			graphFrame.setVisible(true);
			
			model.getSimulation().Simulate();
		}
	}

}