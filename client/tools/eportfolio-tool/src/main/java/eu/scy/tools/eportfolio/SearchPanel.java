package eu.scy.tools.eportfolio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import eu.scy.tools.eportfolio.listeners.ToggleEvent;
import eu.scy.tools.eportfolio.listeners.ToggleViewListener;

public class SearchPanel extends JPanel {

	private static final Logger logger = Logger.getLogger(SearchPanel.class.getName());
	private static final long serialVersionUID = -4336296329393765045L;
	private Dimension passedDim;
	private Vector<ToggleViewListener> ToggleViewlisteners = new Vector<ToggleViewListener>();
	

	public SearchPanel(Dimension dim) {
		this.passedDim = dim;
		this.setLayout(new BorderLayout());
		this.setSize(passedDim);
    	this.setOpaque(true);
    	
    	JPanel buttonBar = new JPanel();
    	buttonBar.setLayout(new FlowLayout());
    	buttonBar.setSize(passedDim.width, 50);
    	buttonBar.setBackground(new Color(50, 100, 150));
    	
    	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ToggleEvent toggleEvent = new ToggleEvent(this, 2);

				@SuppressWarnings("unchecked")
				Vector<ToggleViewListener> vtemp = (Vector<ToggleViewListener>) ToggleViewlisteners.clone();
				for (int x = 0; x < vtemp.size(); x++) {
					ToggleViewListener target = null;
					target = (ToggleViewListener) vtemp.elementAt(x);
					target.ToggleAlert(toggleEvent);
				}
			}
		});
    	
    	JButton cancelButton = new JButton("CANCEL");
    	cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ToggleEvent toggleEvent = new ToggleEvent(this, 0);

				@SuppressWarnings("unchecked")
				Vector<ToggleViewListener> vtemp = (Vector<ToggleViewListener>) ToggleViewlisteners.clone();
				for (int x = 0; x < vtemp.size(); x++) {
					ToggleViewListener target = null;
					target = (ToggleViewListener) vtemp.elementAt(x);
					target.ToggleAlert(toggleEvent);
				}
			}
		});
    	
    	buttonBar.add(okButton);
    	buttonBar.add(cancelButton);
    	this.add(buttonBar, BorderLayout.PAGE_END);
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(new Color(50, 100, 150));
        g2d.fillRect(0,0,passedDim.width, passedDim.height);
    }
	
	public void addToggleViewListener(ToggleViewListener tgvl) {
		if (!ToggleViewlisteners.contains(tgvl)) {
			ToggleViewlisteners.addElement(tgvl);
		}
	}
}
