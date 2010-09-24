package eu.scy.tools.eportfolio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.log4j.Logger;
import eu.scy.tools.eportfolio.listeners.ToggleViewListener;
import eu.scy.tools.eportfolio.utils.SpringUtilities;

public class ReflectionPanel extends JPanel {

	private static final long serialVersionUID = 2484462767670822151L;
	private static final Logger logger = Logger.getLogger(ReflectionPanel.class.getName());
	private Dimension passedDim;
	private Vector<ToggleViewListener> ToggleViewlisteners = new Vector<ToggleViewListener>();
	private BufferedImage image;

	public ReflectionPanel(Dimension dim) {
		this.passedDim = dim;
		this.setLayout(null);
		this.setSize(passedDim);
		this.setOpaque(true);

		try {
			setImage(ImageIO.read(ReflectionPanel.class.getResource("blur.png")));
		} catch (IOException e) {
			logger.error("ReflectionPanel: IOException: "+e);
		}
		
		JPanel staticInfo = new JPanel();
		staticInfo.setOpaque(false);
		staticInfo.setLayout(new SpringLayout());
		staticInfo.setBounds(120, 10, passedDim.width-130, 200);
		
		JLabel missionLabel = new JLabel("Mission: ", JLabel.TRAILING);
		missionLabel.setForeground(Color.WHITE);
		staticInfo.add(missionLabel);
		JLabel missionText = new JLabel("CO2 neutral house");
		missionText.setForeground(Color.WHITE);
        missionLabel.setLabelFor(missionText);
        staticInfo.add(missionText);
        
        JLabel elonameLabel = new JLabel("ELO name: ", JLabel.TRAILING);
		elonameLabel.setForeground(Color.WHITE);
		staticInfo.add(elonameLabel);
		JLabel elonameText = new JLabel("elo123456789");
		elonameText.setForeground(Color.WHITE);
        elonameLabel.setLabelFor(elonameText);
        staticInfo.add(elonameText);

		
		SpringUtilities.makeCompactGrid(staticInfo, 2, 2, 6, 6, 6, 6); 
		this.add(staticInfo);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Color s1 = Color.BLACK;
		Color e = Color.LIGHT_GRAY;
		GradientPaint gradient = new GradientPaint(0, 0, s1, 0, passedDim.height, e, true);
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, passedDim.width, passedDim.height);
		g2d.drawImage(getImage(), 10, 10, null);
	}

	public void addToggleViewListener(ToggleViewListener tgvl) {
		if (!ToggleViewlisteners.contains(tgvl)) {
			ToggleViewlisteners.addElement(tgvl);
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}
}
