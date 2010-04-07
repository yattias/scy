package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eu.scy.client.tools.scydynamics.model.Model;

public class SimulationDialog extends javax.swing.JDialog implements
		java.awt.event.ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3615382141198730335L;
	private Model model;
	private JTextField startField;
	private JTextField stopField;
	private JTextField stepField;
	String[] methods = { "euler", "adamsbashfort2", "rungekutta2", "rungekutta4", "rungekutta45", "static", "discrete" };
	private JComboBox methodbox;


	public SimulationDialog(java.awt.Frame owner, Model model) {
		super(owner, false);
		this.setModal(true);
		this.model = model;
		this.setTitle("simulation properties");
		this.setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new java.awt.GridLayout(4, 2));
		centerPanel.add(new JLabel("start time"));
		startField = new JTextField(10);
		startField.setText(model.getStart()+"");
		centerPanel.add(startField);
		centerPanel.add(new JLabel("stop time"));
		stopField = new JTextField(10);
		stopField.setText(model.getStop()+"");
		centerPanel.add(stopField);
		centerPanel.add(new JLabel("time step"));
		stepField = new JTextField(10);
		stepField.setText(model.getStep()+"");
		centerPanel.add(stepField);
		centerPanel.add(new JLabel("method"));
		methodbox = new JComboBox(methods);
		methodbox.setSelectedItem(model.getMethod());
		centerPanel.add(methodbox);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new java.awt.FlowLayout());
		javax.swing.JButton okayButton = new javax.swing.JButton("ok");
		okayButton.setActionCommand("okay");
		javax.swing.JButton cancelButton = new javax.swing.JButton("cancel");
		cancelButton.setActionCommand("cancel");
		okayButton.addActionListener(this);
		cancelButton.addActionListener(this);
		southPanel.add(okayButton);
		southPanel.add(cancelButton);
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}

	

	public void actionPerformed(java.awt.event.ActionEvent event) {
		if (event.getActionCommand() == "okay") {
			try {
			model.setStart(Double.parseDouble(startField.getText()));
			model.setStop(Double.parseDouble(stopField.getText()));
			model.setStep(Double.parseDouble(stepField.getText()));
			model.setMethod((String)methodbox.getSelectedItem());
			setVisible(false);
			setEnabled(false);
			dispose();
			} catch(NumberFormatException ex) {
				JOptionPane.showMessageDialog( null, "Couldn't parse all the numbers.\nPlease check."); 
			}			
		} else if (event.getActionCommand() == "cancel") {
			setVisible(false);
			setEnabled(false);
			dispose();
		}
	}

}