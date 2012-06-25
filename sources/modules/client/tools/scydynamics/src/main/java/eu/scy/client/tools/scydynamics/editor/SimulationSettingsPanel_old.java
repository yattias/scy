package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
public class SimulationSettingsPanel_old extends JPanel {

	private ModelEditor editor;
	private JTextField startField;
	private JTextField stopField;
	private JTextField stepField;
	private JComboBox methodbox;
	private String[] methods = { "RungeKuttaFehlberg", "euler"};
	private String calculationMethod;
	private AbstractButton runButton;
	private JButton stopButton;
	private JSpinner digitSpinner;
	private JPanel northPanel;

	public SimulationSettingsPanel_old(ModelEditor editor, ActionListener listener, boolean withDigitSpinner) {
		super();
		this.editor = editor;
		calculationMethod = (String) editor.getProperties().get("editor.fixedcalculationmethod");
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Simulation settings"));
		initUI(listener, withDigitSpinner);
	}

	private void initUI(ActionListener listener, boolean withDigitSpinner) {
		northPanel = new JPanel();
		northPanel.setLayout(new java.awt.GridLayout(5, 2));
		startField = new JTextField(6);
		startField.setHorizontalAlignment(JTextField.RIGHT);
		startField.setEditable(true);
		stopField = new JTextField(6);
		stopField.setHorizontalAlignment(JTextField.RIGHT);
		stepField = new JTextField(6);
		stepField.setHorizontalAlignment(JTextField.RIGHT);
		methodbox = getMethodBox();
		digitSpinner = new JSpinner(new SpinnerNumberModel( 2, 0, 5, 1));
		
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
		this.add(northPanel, BorderLayout.NORTH);
		
		JPanel southPanel = new JPanel();
		//FlowLayout flowCenter = new FlowLayout();
		//flowCenter.setAlignment(FlowLayout.CENTER);
		runButton = new JButton(Util.getImageIcon("media-playback-start.png"));
		runButton.setSize(36, 36);
		runButton.setPreferredSize(new Dimension(36, 36));
		runButton.setActionCommand("run");
		runButton.setToolTipText("Run model");
		runButton.addActionListener(listener);
		southPanel.add(runButton);
		
		stopButton = new JButton(Util.getImageIcon("media-playback-stop.png"));
		stopButton.setActionCommand("stop");
		stopButton.setSize(36, 36);
		stopButton.setPreferredSize(new Dimension(36, 36));
		
		stopButton.setToolTipText("Stop model");
		stopButton.setEnabled(false);
		stopButton.addActionListener(listener);
		southPanel.add(stopButton);
		
		JButton button = new JButton(Util.getImageIcon("media-floppy.png"));
		button.setActionCommand("export");
		button.setSize(36, 36);
		button.setPreferredSize(new Dimension(36, 36));
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
		System.out.println("SimulationSettingsPanel.updateSettings: Mode = "+editor.getMode());

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
		if (editor.getMode()==ModelEditor.Mode.QUALITATIVE_MODELLING) {
			northPanel.setVisible(false);
		} else {
			northPanel.setVisible(true);
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
