package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eu.scy.client.tools.scydynamics.model.Model;


@SuppressWarnings("serial")
public class SimulationSettingsPanel extends JPanel {

	private ModelEditor editor;
	private JTextField startField;
	private JTextField stopField;
	private JTextField stepField;
	private JComboBox methodbox;
	String[] methods = { "RungeKuttaFehlberg", "euler", "static"};
	private String calculationMethod;

	public SimulationSettingsPanel(ModelEditor editor, ActionListener listener) {
		super();
		this.editor = editor;
		calculationMethod = (String) editor.getProperties().get("editor.fixedcalculationmethod");
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("simulation settings"));
		initUI(listener);
	}

	public void initUI(ActionListener listener) {
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new java.awt.GridLayout(6, 2));
		startField = new JTextField(10);
		startField.setEditable(false);
		stopField = new JTextField(10);
		stepField = new JTextField(10);
		methodbox = getMethodBox();
		
		if (!editor.isQualitative()) {
			northPanel.add(new JLabel("start time"));
			northPanel.add(startField);
			northPanel.add(new JLabel("stop time"));
			northPanel.add(stopField);
			northPanel.add(new JLabel("time step"));
			northPanel.add(stepField);
			northPanel.add(new JLabel("method"));
			northPanel.add(methodbox);
		}
		
		JButton button = new JButton("run simulation");
		button.setActionCommand("run");
		button.addActionListener(listener);
		northPanel.add(button);
		button = new JButton("export to sqv");
		button.setActionCommand("export");
		button.addActionListener(listener);
		if (editor.getProperties().getProperty("editor.export_to_sqv", "false").equals("true")) {
		    northPanel.add(button);
		}
		this.add(northPanel, BorderLayout.NORTH);
	}
	
	private JComboBox getMethodBox() {
		JComboBox box = new JComboBox(methods);
		if (calculationMethod!=null && !calculationMethod.equals("false") ) {
			box.setSelectedItem(calculationMethod);
			box.setEnabled(false);
		}
		return box;
	}

	public void updateSettings() {
		Model model = editor.getModel();
		startField.setText(model.getStart() + "");
		stopField.setText(model.getStop() + "");
		stepField.setText(model.getStep() + "");
		if (calculationMethod!=null && !calculationMethod.equals("false") ) {
			methodbox.setSelectedItem(calculationMethod);
			methodbox.setEnabled(false);
		} else {
			methodbox.setSelectedItem(model.getMethod());
		}
	}

	public String getStart() {
		return startField.getText();
	}

	public String getStop() {
		return stopField.getText();
	}

	public String getStep() {
		return stepField.getText();
	}

	public String getMethod() {
		return (String)methodbox.getSelectedItem();
	}

}
