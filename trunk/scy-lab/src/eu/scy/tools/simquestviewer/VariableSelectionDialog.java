package eu.scy.tools.simquestviewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import sqv.ModelVariable;

public class VariableSelectionDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7882682383942333318L;
	private DataCollector collector;
	private JPanel variablePanel;
	private Map<JCheckBox, ModelVariable> variables;

	public VariableSelectionDialog(Frame owner, DataCollector collector) {
		super(owner, true); // modal
		this.collector = collector;
		variables = createVariables(collector.getSimulationVariables());
		initGUI();
	}

	private void initGUI() {
		this.setTitle("Select relevant variables...");
		this.setLayout(new BorderLayout());
		this.setLocation((int)this.getOwner().getBounds().getCenterX(), (int)this.getOwner().getBounds().getCenterY());
		

		variablePanel = new JPanel();
		variablePanel.setLayout(new GridLayout(variables.size(),
				1));
		JCheckBox checkbox;	
		for (Iterator<JCheckBox> boxes = variables.keySet().iterator(); boxes.hasNext();) {
			checkbox = boxes.next();
			variablePanel.add(checkbox);
		}
		variablePanel.setBorder(BorderFactory.createTitledBorder("available variables"));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton button = new JButton("save");
		button.setActionCommand("save");
		button.addActionListener(this);
		buttonPanel.add(button);
		button = new JButton("cancel");
		button.setActionCommand("cancel");
		button.addActionListener(this);
		buttonPanel.add(button);

		this.getContentPane().add(new JLabel("Please choose the variables that will be recorded."), BorderLayout.NORTH);
		this.getContentPane().add(variablePanel, BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("save")) {
			List<ModelVariable> selectedVariables = new LinkedList<ModelVariable>();
			for (Iterator<JCheckBox> boxes = variables.keySet().iterator(); boxes.hasNext();) {
				JCheckBox box = boxes.next();
				if (box.isSelected()) {
					selectedVariables.add(variables.get(box));
					System.out.println("VariableSelectionDialog.actionPerformed(). add variable "+variables.get(box).getExternalName());
				}
			}
			System.out.println("VariableSelectionDialog.actionPerformed(). added "+selectedVariables.size()+" variables");
			collector.setSelectedVariables(selectedVariables);
			this.dispose();
		} else if (evt.getActionCommand().equals("cancel")) {
			this.dispose();
		}
	}

	private Map<JCheckBox, ModelVariable> createVariables(List<ModelVariable> simulationvariables) {
		Map<JCheckBox, ModelVariable> temp = new HashMap<JCheckBox, ModelVariable>();
		ModelVariable var;
		JCheckBox checkbox;
		for (Iterator<ModelVariable> i = collector.getSimulationVariables()
				.iterator(); i.hasNext();) {
			var = i.next();
			if (var.getKind() == ModelVariable.VK_INPUT
					|| var.getKind() == ModelVariable.VK_OUTPUT || var.getKind() == ModelVariable.VK_TIME) {
				checkbox = new JCheckBox(var.getName() + ": "
						+ var.getExternalName());
				if (collector.getSelectedVariables().contains(var)) {
					checkbox.setSelected(true);
				}
				temp.put(checkbox, var);
			}
		}
		return temp;
	}

}
