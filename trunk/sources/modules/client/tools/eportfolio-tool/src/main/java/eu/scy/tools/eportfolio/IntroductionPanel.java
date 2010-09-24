package eu.scy.tools.eportfolio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import eu.scy.tools.eportfolio.listeners.ToggleEvent;
import eu.scy.tools.eportfolio.listeners.ToggleViewListener;

public class IntroductionPanel extends JPanel {

	private static final Logger logger = Logger.getLogger(IntroductionPanel.class.getName());
	private static final long serialVersionUID = -4580617406875991724L;
	private Dimension passedDim;
	private int titleBarHeight = 50;
	private String[] obliEloList = { "Select ...", "My obligatory ELO no. 1",
			"My obligatory ELO no. 2", "My obligatory ELO no. 3",
			"My obligatory ELO no. 4" };
	private String statusString = "creating";
	private JButton addButton;
	private Vector<ToggleViewListener> ToggleViewlisteners = new Vector<ToggleViewListener>();
	private Vector<JPanel> panelSArray = new Vector<JPanel>();
	private JPanel visibleObliJPanel;
	private JPanel obliEloPanel;

	public IntroductionPanel(Dimension dim) {
		this.passedDim = dim;
		this.setLayout(null);
		this.setSize(passedDim);
		this.setOpaque(true);
		JLabel title = new JLabel("<html><p align=center>Hey {userName}.<br> This is your eportfolio tool. Here you can do very cool things!!</p><html>");
		Font titleFont = new Font("Arial", Font.PLAIN, 16);
		title.setFont(titleFont);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBounds(0, 0, passedDim.width, titleBarHeight);
		title.setForeground(Color.white);
		this.add(title);

		JLabel status = new JLabel("Status: ");
		status.setHorizontalAlignment(JLabel.RIGHT);
		status.setBounds(0, titleBarHeight + 10, passedDim.width - 100, 20);
		status.setForeground(Color.BLACK);
		this.add(status);
		JLabel currentStatus = new JLabel(statusString);
		currentStatus.setHorizontalAlignment(JLabel.LEFT);
		currentStatus.setBounds(passedDim.width - 100, titleBarHeight + 10, 100, 20);
		currentStatus.setForeground(new Color(100, 200, 100));
		this.add(currentStatus);

		final JComboBox jComboBox = new JComboBox(obliEloList);
		jComboBox.setBounds(10, titleBarHeight + 10, 200, 25);
		this.add(jComboBox);
		jComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jComboBox.getSelectedIndex() > 0) {
					addButton.setVisible(true);
				} else {
					addButton.setVisible(false);
				}
				visibleObliJPanel.setVisible(false);
				visibleObliJPanel = panelSArray.elementAt(jComboBox.getSelectedIndex());
				visibleObliJPanel.setVisible(true);
			}
		});

		addButton = new JButton("ADD");
		addButton.setVisible(false);
		addButton.setBounds(10 + 200 + 10, titleBarHeight + 10, 70, 25);
		this.add(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ToggleEvent toggleEvent = new ToggleEvent(this, 1);

				@SuppressWarnings("unchecked")
				Vector<ToggleViewListener> vtemp = (Vector<ToggleViewListener>) ToggleViewlisteners.clone();
				for (int x = 0; x < vtemp.size(); x++) {
					ToggleViewListener target = null;
					target = (ToggleViewListener) vtemp.elementAt(x);
					target.ToggleAlert(toggleEvent);
				}
			}
		});
		
		obliEloPanel = getObligatoryEloPanel();
		this.add(obliEloPanel);
		visibleObliJPanel = obliEloPanel;
		obliEloPanel.setOpaque(false);
		JLabel instructions = new JLabel("<html><p align=center>Choose an obligatory ELO from the comboBox and add ELOs to your portfolio.</p><html>");
		Font font = new Font("Arial", Font.PLAIN, 20);
		instructions.setFont(font);
		instructions.setVerticalAlignment(JLabel.CENTER);
		instructions.setHorizontalAlignment(JLabel.CENTER);
		instructions.setBounds(110, 30, 380, 200);
		instructions.setForeground(Color.white);
		obliEloPanel.add(instructions);
		obliEloPanel.setVisible(true);
		panelSArray.addElement(obliEloPanel);
		
		buildObligatoryEloPanels(obliEloList);

	}

	private void buildObligatoryEloPanels(String[] obliElos) {
		JLabel testlabel;
		for (int i = 1; i < obliElos.length; i++) {
			obliEloPanel = getObligatoryEloPanel();
			obliEloPanel.setOpaque(true);
			testlabel = new JLabel("panel: "+i);
			testlabel.setHorizontalAlignment(JLabel.LEFT);
			testlabel.setBounds(0, titleBarHeight + 100, 100, 20);
			testlabel.setForeground(new Color(100, 200, 100));
			obliEloPanel.add(testlabel);
			this.add(obliEloPanel);
			panelSArray.addElement(obliEloPanel);
		}
	}

	private JPanel getObligatoryEloPanel() {
		JPanel obliEloPanel;
		obliEloPanel = new JPanel();
		obliEloPanel.setLayout(null);
		obliEloPanel.setBounds(0, titleBarHeight+10+25, passedDim.width, passedDim.height-(titleBarHeight+10+25));
		obliEloPanel.setVisible(false);
		return obliEloPanel;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Color s1 = Color.BLACK;
		Color e = Color.LIGHT_GRAY;
		GradientPaint gradient = new GradientPaint(0, 0, s1, 0, titleBarHeight, e, true);
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, passedDim.width, titleBarHeight);
		GradientPaint gradient1 = new GradientPaint(0, 0, s1, 0, 200, e, true);
		g2d.setPaint(gradient1);
		g2d.fillRect(100, 120, 400, 200);

	}

	public void addToggleViewListener(ToggleViewListener tgvl) {
		if (!ToggleViewlisteners.contains(tgvl)) {
			ToggleViewlisteners.addElement(tgvl);
		}
	}
}
