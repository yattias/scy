package eu.scy.tools.eportfolio;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import eu.scy.tools.eportfolio.listeners.ToggleEvent;
import eu.scy.tools.eportfolio.listeners.ToggleViewListener;

public class Eportfolio implements ToggleViewListener {

	private Logger logger = Logger.getLogger(Eportfolio.class.getName());
	private JPanel jPanel;
	private IntroductionPanel introductionPanel;
	private SearchPanel searchPanel;
	private ReflectionPanel reflectionPanel;
	private Vector<JPanel> jPanelLayers = new Vector<JPanel>();
	
	
	public Eportfolio() {
	}

	public JPanel getGUI() {
		jPanel = new JPanel();
		jPanel.setLayout(null);
		jPanel.setPreferredSize(new Dimension(600, 400));
		introductionPanel = new IntroductionPanel(new Dimension(600, 400));
		introductionPanel.addToggleViewListener(this);
		jPanel.add(introductionPanel);
		jPanelLayers.addElement(introductionPanel);
		searchPanel = new SearchPanel(new Dimension(600, 400));
		searchPanel.addToggleViewListener(this);
		searchPanel.setVisible(false);
		jPanel.add(searchPanel);
		jPanelLayers.addElement(searchPanel);
		reflectionPanel = new ReflectionPanel(new Dimension(600, 400));
		reflectionPanel.addToggleViewListener(this);
		reflectionPanel.setVisible(false);
		jPanel.add(reflectionPanel);
		jPanelLayers.addElement(reflectionPanel);
		return jPanel;
	}

	public void resizeSPT(int newWidth, int newHeight) {
		this.jPanel.setPreferredSize(new Dimension(newWidth, newHeight));
	}

	private static void createAndShowGUI() {
		final Eportfolio eportfolio = new Eportfolio();
		JFrame frame = new JFrame("EPortfolio Tool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JComponent newContentPane = eportfolio.getGUI();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	@Override
	public void ToggleAlert(ToggleEvent toggleEvent) {
		for (int i = 0; i < jPanelLayers.size(); i++) {
			if(i == toggleEvent.getTarget()) {
				(jPanelLayers.elementAt(i)).setVisible(true);
			}
			else {
				(jPanelLayers.elementAt(i)).setVisible(false);
			}
		}
	}
}
