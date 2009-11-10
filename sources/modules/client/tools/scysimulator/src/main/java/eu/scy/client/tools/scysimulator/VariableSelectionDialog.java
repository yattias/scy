package eu.scy.client.tools.scysimulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import sqv.ModelVariable;

public class VariableSelectionDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7882682383942333318L;
	private DataCollector collector;
	private JPanel variablePanel;
	private Map<ComparableJCheckBox, ModelVariable> variables;

	public VariableSelectionDialog(Frame owner, DataCollector collector) {
		super(owner, true); // modal
		this.collector = collector;
		variables = createVariables(collector.getSimulationVariables());
		initGUI();
	}

	private void initGUI() {
		setTitle("Select relevant variables...");
		setLayout(new BorderLayout());
		this.setLocation((int) getOwner().getBounds().getCenterX(),
				(int) getOwner().getBounds().getCenterY());

		variablePanel = new JPanel();
		// we need +3 because we have three more selection buttons
		variablePanel.setLayout(new GridLayout(variables.size(), 1));
		ComparableJCheckBox checkbox;
		for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes
		.hasNext();) {
			checkbox = boxes.next();
			variablePanel.add(checkbox);
		}

		JPanel selectPanel = new JPanel();
		selectPanel.setBorder(new TitledBorder("quick selection"));
		selectPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		selectPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		BoxLayout boxLayout = new BoxLayout(selectPanel, BoxLayout.Y_AXIS);
		selectPanel.setLayout(boxLayout);
		// the select-all-button...
		JButton button = new JButton("select all");
		button.setAlignmentY(JButton.CENTER_ALIGNMENT);
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		button.setActionCommand("all");
		button.addActionListener(this);
		selectPanel.add(button);
		// the select-non-button...
		button = new JButton("select none");
		button.setAlignmentY(JButton.CENTER_ALIGNMENT);
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		button.setActionCommand("none");
		button.addActionListener(this);
		selectPanel.add(button);
		// the invert-selection-button...
		button = new JButton("invert selection");
		button.setAlignmentY(JButton.CENTER_ALIGNMENT);
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		button.setActionCommand("invert");
		button.addActionListener(this);
		selectPanel.add(button);

		JScrollPane scroller = new JScrollPane();
		scroller.setBorder(new TitledBorder("variables"));
		scroller.setViewportView(variablePanel);
		scroller.setBorder(BorderFactory
				.createTitledBorder("available variables"));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		button = new JButton("save");
		button.setActionCommand("save");
		button.addActionListener(this);
		buttonPanel.add(button);
		button = new JButton("cancel");
		button.setActionCommand("cancel");
		button.addActionListener(this);
		buttonPanel.add(button);

		getContentPane()
		.add(
				new JLabel(
						"Please choose the variables that will be recorded."),
						BorderLayout.NORTH);
		getContentPane().add(scroller, BorderLayout.CENTER);
		getContentPane().add(selectPanel, BorderLayout.WEST);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(400, 400);
		this.setPreferredSize(new Dimension(400, 400));
		pack();
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("save")) {
			List<ModelVariable> selectedVariables = new LinkedList<ModelVariable>();
			for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes
			.hasNext();) {
				ComparableJCheckBox box = boxes.next();
				if (box.isSelected()) {
					selectedVariables.add(variables.get(box));
					// System.out.println("VariableSelectionDialog.actionPerformed(). add variable "+variables.get(box).getExternalName());
				}
			}
			// System.out.println("VariableSelectionDialog.actionPerformed(). added "+selectedVariables.size()+" variables");
			collector.setSelectedVariables(selectedVariables);
			dispose();
		} else if (evt.getActionCommand().equals("cancel")) {
			dispose();
		} else if (evt.getActionCommand().equals("all")) {
			for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes
			.hasNext();) {
				ComparableJCheckBox box = boxes.next();
				box.setSelected(true);
			}
		} else if (evt.getActionCommand().equals("none")) {
			for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes
			.hasNext();) {
				ComparableJCheckBox box = boxes.next();
				box.setSelected(false);
			}
		} else if (evt.getActionCommand().equals("invert")) {
			for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes
			.hasNext();) {
				ComparableJCheckBox box = boxes.next();
				box.setSelected(!box.isSelected());
			}
		}
	}

	private Map<ComparableJCheckBox, ModelVariable> createVariables(
			List<ModelVariable> simulationvariables) {
		Map<ComparableJCheckBox, ModelVariable> temp = new TreeMap<ComparableJCheckBox, ModelVariable>();
		ModelVariable var;
		ComparableJCheckBox checkbox;
		for (Iterator<ModelVariable> i = collector.getSimulationVariables()
				.iterator(); i.hasNext();) {
			var = i.next();
			if ((var.getKind() == ModelVariable.VK_INPUT)
					|| (var.getKind() == ModelVariable.VK_OUTPUT)
					|| (var.getKind() == ModelVariable.VK_TIME)) {
				checkbox = new ComparableJCheckBox(var.getName() + ": "
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
