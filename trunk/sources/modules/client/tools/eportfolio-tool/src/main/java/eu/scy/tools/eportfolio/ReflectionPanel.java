package eu.scy.tools.eportfolio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import eu.scy.tools.eportfolio.listeners.ToggleViewListener;

public class ReflectionPanel extends JPanel {

	private static final long serialVersionUID = 2484462767670822151L;
	private static final Logger logger = Logger.getLogger(ReflectionPanel.class.getName());
	private Dimension passedDim;
	private Vector<ToggleViewListener> ToggleViewlisteners = new Vector<ToggleViewListener>();

	public ReflectionPanel(Dimension dim) {
		this.passedDim = dim;
		this.setLayout(null);
		this.setSize(passedDim);
    	this.setOpaque(true);
	}

	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(new Color(150, 100, 50));
        g2d.fillRect(0,0,passedDim.width, passedDim.height);
    }
	
	public void addToggleViewListener(ToggleViewListener tgvl) {
		if (!ToggleViewlisteners.contains(tgvl)) {
			ToggleViewlisteners.addElement(tgvl);
		}
	}
}
