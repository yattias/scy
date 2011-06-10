package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.model.Model;


@SuppressWarnings("serial")
public class SimulationSettingsPanel extends JPanel {

	private ModelEditor editor;
	private JTextField startField;
	private JTextField stopField;
	private JTextField stepField;
	private JComboBox methodbox;
	//private String[] methods = { "RungeKuttaFehlberg", "euler", "static"};
	private String[] methods = { "RungeKuttaFehlberg", "euler"};
	private String calculationMethod;
	private AbstractButton runButton;
	private JButton stopButton;
	private JSpinner digitSpinner;

	public SimulationSettingsPanel(ModelEditor editor, ActionListener listener, boolean withDigitSpinner) {
		super();
		this.editor = editor;
		calculationMethod = (String) editor.getProperties().get("editor.fixedcalculationmethod");
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Simulation settings"));
		initUI(listener, withDigitSpinner);
	}

	private void initUI(ActionListener listener, boolean withDigitSpinner) {
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new java.awt.GridLayout(5, 2));
		startField = new JTextField(6);
		startField.setHorizontalAlignment(JTextField.RIGHT);
		startField.setEditable(false);
		stopField = new JTextField(6);
		stopField.setHorizontalAlignment(JTextField.RIGHT);
		stepField = new JTextField(6);
		stepField.setHorizontalAlignment(JTextField.RIGHT);
		methodbox = getMethodBox();
		digitSpinner = new JSpinner(new SpinnerNumberModel( 2, 0, 5, 1));
		
		if (editor.getMode()!=Mode.QUALITATIVE_MODELLING) {
			northPanel.add(new JLabel("Start time"));
			northPanel.add(startField);
			northPanel.add(new JLabel("Stop time"));
			northPanel.add(stopField);
			northPanel.add(new JLabel("Time step"));
			northPanel.add(stepField);
			northPanel.add(new JLabel("Method"));
			northPanel.add(methodbox);
			if (withDigitSpinner) {
				northPanel.add(new JLabel("Digits in table"));
				northPanel.add(digitSpinner);
			}
		}
		this.add(northPanel, BorderLayout.NORTH);
		
		JPanel southPanel = new JPanel();
		FlowLayout flowCenter = new FlowLayout();
		flowCenter.setAlignment(FlowLayout.CENTER);
		runButton = new JButton(Util.getImageIcon("media-playback-start.png"));
		runButton.setActionCommand("run");
		runButton.setToolTipText("Run model");
		runButton.addActionListener(listener);
		southPanel.add(runButton);
		
		stopButton = new JButton(Util.getImageIcon("media-playback-stop.png"));
		stopButton.setActionCommand("stop");
		stopButton.setToolTipText("Stop model");
		stopButton.setEnabled(false);
		stopButton.addActionListener(listener);
		southPanel.add(stopButton);
		
		JButton button = new JButton(Util.getImageIcon("media-floppy.png"));
		button.setActionCommand("export");
		button.setToolTipText("Export to SQX");
		button.addActionListener(listener);
		if (editor.getProperties().getProperty("editor.export_to_sqv", "false").equals("true")) {
			southPanel.add(button);
		}
		this.add(southPanel, BorderLayout.SOUTH);
	}
	
	public void setRunning(boolean isRunning) {
		runButton.setEnabled(!isRunning);
		stopButton.setEnabled(isRunning);
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

	public Integer getDigits() {
	    return (Integer) this.digitSpinner.getValue();
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
