package eu.scy.client.tools.scysimulator;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import sqv.ModelVariable;

public class VariableSelectionDialog extends JDialog implements ActionListener {

	public enum Mode {
		SYMBOLS_AND_DESCRIPTIONS, SYMBOLS_ONLY, DESCRIPTIONS_ONLY
	};

	private Mode mode;

	private DataCollector collector;
	private JPanel variablePanel;
	private Map<ComparableJCheckBox, ModelVariable> variables;
	private final ResourceBundleWrapper bundle;
	private JScrollPane scroller;

	public VariableSelectionDialog(Frame owner, DataCollector collector) {
		super(owner, true); // modal
		this.bundle = new ResourceBundleWrapper(this);
		this.collector = collector;
		variables = createVariables(collector.getSimulationVariables());
		initGUI();
	}

	private void initGUI() {
		setTitle(getBundleString("VARIABLESELECTIONDIALOG_TITLE"));
		setLayout(new BorderLayout());
		this.setLocation((int) getOwner().getBounds().getCenterX(), (int) getOwner().getBounds().getCenterY());

		JPanel selectPanel = new JPanel();
		selectPanel.setBorder(new TitledBorder(getBundleString("VARIABLESELECTIONDIALOG_QUICK_SELECTION")));
		selectPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		selectPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		BoxLayout boxLayout = new BoxLayout(selectPanel, BoxLayout.Y_AXIS);
		selectPanel.setLayout(boxLayout);
		// the select-all-button...
		JButton button = new JButton(getBundleString("VARIABLESELECTIONDIALOG_ALL"));
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
		button = new JButton(getBundleString("VARIABLESELECTIONDIALOG_INVERT"));
		button.setAlignmentY(JButton.CENTER_ALIGNMENT);
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		button.setActionCommand("invert");
		button.addActionListener(this);
		selectPanel.add(button);
		// the select symbol and descriptions buttons
		// ButtonGroup bgroup = new ButtonGroup();
		// JRadioButton rbutton = new JRadioButton("show symbols & descr.");
		// rbutton.setActionCommand(Mode.SYMBOLS_AND_DESCRIPTIONS.toString());
		// rbutton.addActionListener(this);
		// selectPanel.add(rbutton);
		// rbutton.setAlignmentX(JButton.LEFT_ALIGNMENT);
		// bgroup.add(rbutton);
		// rbutton = new JRadioButton("show symbols only");
		// rbutton.setActionCommand(Mode.SYMBOLS_ONLY.toString());
		// rbutton.addActionListener(this);
		// rbutton.setAlignmentX(JButton.LEFT_ALIGNMENT);
		// selectPanel.add(rbutton);
		// bgroup.add(rbutton);
		// rbutton = new JRadioButton("show descr. only");
		// rbutton.setActionCommand(Mode.DESCRIPTIONS_ONLY.toString());
		// rbutton.addActionListener(this);
		// rbutton.setAlignmentX(JButton.LEFT_ALIGNMENT);
		// selectPanel.add(rbutton);
		// bgroup.add(rbutton);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		button = new JButton(getBundleString("VARIABLESELECTIONDIALOG_SAVE"));
		button.setActionCommand("save");
		button.addActionListener(this);
		buttonPanel.add(button);
		button = new JButton(getBundleString("VARIABLESELECTIONDIALOG_CANCEL"));
		button.setActionCommand("cancel");
		button.addActionListener(this);
		buttonPanel.add(button);

		getContentPane().add(new JLabel(getBundleString("VARIABLESELECTIONDIALOG_PLEASE_CHOOSE")), BorderLayout.NORTH);

		getContentPane().add(selectPanel, BorderLayout.WEST);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		createVariablePanel();
		this.setSize(400, 400);
		this.setPreferredSize(new Dimension(400, 400));
		pack();
	}

	private void createVariablePanel() {
		if (scroller != null) {
			this.getContentPane().remove(scroller);
		}
		variablePanel = new JPanel();
		// we need +3 because we have three more selection buttons
		variablePanel.setLayout(new GridLayout(variables.size(), 1));
		ComparableJCheckBox checkbox;
		for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes.hasNext();) {
			checkbox = boxes.next();
			variablePanel.add(checkbox);
		}
		scroller = new JScrollPane();
		scroller.setBorder(new TitledBorder(getBundleString("VARIABLESELECTIONDIALOG_VARIABLES")));
		scroller.setViewportView(variablePanel);
		scroller.setBorder(BorderFactory.createTitledBorder(getBundleString("VARIABLESELECTIONDIALOG_AVAILABLE_VARIABLES")));
		getContentPane().add(scroller, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("save")) {
			List<ModelVariable> selectedVariables = new LinkedList<ModelVariable>();
			for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes.hasNext();) {
				ComparableJCheckBox box = boxes.next();
				if (box.isSelected()) {
					selectedVariables.add(variables.get(box));
				}
			}
			collector.setSelectedVariables(selectedVariables);
			dispose();
		} else if (evt.getActionCommand().equals("cancel")) {
			dispose();
		} else if (evt.getActionCommand().equals("all")) {
			for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes.hasNext();) {
				ComparableJCheckBox box = boxes.next();
				box.setSelected(true);
			}
		} else if (evt.getActionCommand().equals("none")) {
			for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes.hasNext();) {
				ComparableJCheckBox box = boxes.next();
				box.setSelected(false);
			}
		} else if (evt.getActionCommand().equals("invert")) {
			for (Iterator<ComparableJCheckBox> boxes = variables.keySet().iterator(); boxes.hasNext();) {
				ComparableJCheckBox box = boxes.next();
				box.setSelected(!box.isSelected());
			}
		} else if (evt.getActionCommand().equals(Mode.SYMBOLS_AND_DESCRIPTIONS.toString())) {
			this.mode = Mode.SYMBOLS_AND_DESCRIPTIONS;
			createVariablePanel();
		} else if (evt.getActionCommand().equals(Mode.SYMBOLS_ONLY.toString())) {
			this.mode = Mode.SYMBOLS_ONLY;
			createVariablePanel();
		} else if (evt.getActionCommand().equals(Mode.DESCRIPTIONS_ONLY.toString())) {
			this.mode = Mode.DESCRIPTIONS_ONLY;
			createVariablePanel();
		}
	}

	private Map<ComparableJCheckBox, ModelVariable> createVariables(List<ModelVariable> simulationvariables) {
		Map<ComparableJCheckBox, ModelVariable> temp = new TreeMap<ComparableJCheckBox, ModelVariable>();
		ModelVariable var;
		for (Iterator<ModelVariable> i = collector.getSimulationVariables().iterator(); i.hasNext();) {
			var = i.next();

			// if ((var.getKind() == ModelVariable.VK_INPUT)
			// || (var.getKind() == ModelVariable.VK_OUTPUT)
			// || (var.getKind() == ModelVariable.VK_TIME)) {
			ComparableJCheckBox checkbox = null;
			if (var.getDescription() == null || var.getDescription().isEmpty()) {
				// variable description does not exist or is empty
				// create a checkbox with ext.name and int.name
				if (var.getExternalName().equals(var.getName())) {
					checkbox = new ComparableJCheckBox(var.getExternalName());
				} else {
					checkbox = new ComparableJCheckBox(var.getExternalName() + ": " + var.getName());
				}
			} else if ((var.getDescription().equals("n/a") || (var.getKind() == ModelVariable.VK_SPECIAL) || (var.getKind() == ModelVariable.VK_TIME))) {
				// description is "n/a" or it's a special variable, doing
				// nothing
			} else {
				// description has some meaningful content
				checkbox = new ComparableJCheckBox(var.getExternalName() + ": " + var.getDescription());
			}

			if (checkbox != null) {
				// has a checkbox been created in the previous block?
				if (collector.getSelectedVariables().contains(var)) {
					checkbox.setSelected(true);
				}
				temp.put(checkbox, var);
			}
		}
		return temp;
	}

	private String getBundleString(String key) {
		return this.bundle.getString(key);
	}
}
