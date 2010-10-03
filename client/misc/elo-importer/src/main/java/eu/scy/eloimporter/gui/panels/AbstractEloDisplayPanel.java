package eu.scy.eloimporter.gui.panels;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;

public abstract class AbstractEloDisplayPanel extends JPanel {

	public AbstractEloDisplayPanel() {
		super();
	}

	public AbstractEloDisplayPanel(LayoutManager layout) {
		super(layout);
	}

	public AbstractEloDisplayPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public AbstractEloDisplayPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public abstract void setElo(IELO<IMetadataKey> elo);

}
