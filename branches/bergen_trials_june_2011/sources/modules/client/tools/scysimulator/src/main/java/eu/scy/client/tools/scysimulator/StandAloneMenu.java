package eu.scy.client.tools.scysimulator;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import roolo.elo.JDomStringConversion;

public class StandAloneMenu extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3753196108641246182L;
	private DataCollector dc;
	private JDomStringConversion jdomStringConversion = new JDomStringConversion();
    
	public StandAloneMenu(DataCollector dc) {
		this.dc = dc;
		this.setLayout(new FlowLayout());
		
		JButton button = new JButton("show dataset");
		button.setActionCommand("show dataset");
		button.addActionListener(this);
		this.add(button);
		
		button = new JButton("show simconfig");
		button.setActionCommand("show simconfig");
		button.addActionListener(this);
		this.add(button);
		
		button = new JButton("inject simconfig");
		button.setActionCommand("inject simconfig");
		button.addActionListener(this);
		
		this.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("show dataset")) {
			String dataset = jdomStringConversion.xmlToString(dc.getDataSet().toXML());
			TextDisplayDialog display = new TextDisplayDialog(dc.getSimQuestViewer().getMainFrame(), dataset, "dataset display");
            display.setVisible(true);
		} else if (e.getActionCommand().equals("show simconfig")) {
			String simconfig = jdomStringConversion.xmlToString(dc.getSimConfig().toXML());
			TextDisplayDialog display = new TextDisplayDialog(dc.getSimQuestViewer().getMainFrame(), simconfig, "simconfig display");
            display.setVisible(true);
		} else if (e.getActionCommand().equals("inject simconfig")) {
			SimConfigInjectDialog injector = new SimConfigInjectDialog(dc.getSimQuestViewer().getMainFrame(), dc);
			injector.setVisible(true);
		}
	}

}
