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


public class SimulationSettingsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2993338310582112519L;
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
		northPanel.setLayout(new java.awt.GridLayout(5, 2));
		northPanel.add(new JLabel("start time"));
		startField = new JTextField(10);
		startField.setEditable(false);
		northPanel.add(startField);
		northPanel.add(new JLabel("stop time"));
		stopField = new JTextField(10);
		northPanel.add(stopField);
		northPanel.add(new JLabel("time step"));
		stepField = new JTextField(10);
		northPanel.add(stepField);
		northPanel.add(new JLabel("method"));
		methodbox = getMethodBox();
		northPanel.add(methodbox);
		JButton run = new JButton("run simulation");
		run.setActionCommand("run");
		run.addActionListener(listener);
		northPanel.add(run);		
		JButton export = new JButton("export to sqv");
		export.setActionCommand("export");
		export.addActionListener(listener);
		northPanel.add(export);
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
