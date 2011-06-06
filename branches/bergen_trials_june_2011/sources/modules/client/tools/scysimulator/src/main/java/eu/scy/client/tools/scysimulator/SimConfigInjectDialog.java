package eu.scy.client.tools.scysimulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jdom.JDOMException;

public class SimConfigInjectDialog extends JDialog implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 7809983127880229969L;
    private DataCollector dc;
    private JTextArea textArea;

    public SimConfigInjectDialog(Frame owner, DataCollector dc) {
	super(owner, false); // not modal
	this.dc = dc;
	this.setTitle("simconfig injection");
	this.setPreferredSize(new Dimension(400, 600));
	this.setLayout(new BorderLayout());
	textArea = new JTextArea(80, 80);
	textArea.setEditable(true);
	JScrollPane scroller = new JScrollPane(textArea);
	this.add(scroller, BorderLayout.CENTER);

	JPanel southPanel = new JPanel();
	southPanel.setLayout(new FlowLayout());
	JButton button = new JButton("INJECT");
	button.addActionListener(this);
	button.setActionCommand("inject");
	southPanel.add(button);
	button = new JButton("CANCEL");
	button.addActionListener(this);
	button.setActionCommand("cancel");
	southPanel.add(button);

	this.add(southPanel, BorderLayout.SOUTH);

	this.setSize(400, 600);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand().equals("inject")) {
	    try {
		SimConfig config = new SimConfig(textArea.getText());
		dc.setVariableValues(config.getVariables());
	    } catch (JDOMException ex) {
		JOptionPane.showMessageDialog(this, "Could not parse the SimConfig; the current simulation will not be changed.", "Parsing problem", JOptionPane.WARNING_MESSAGE);
	    }
	} else if (e.getActionCommand().equals("cancel")) {
	    this.setVisible(false);
	    this.dispose();
	}
    }
}
