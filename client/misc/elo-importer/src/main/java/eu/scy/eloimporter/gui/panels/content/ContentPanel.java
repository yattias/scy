package eu.scy.eloimporter.gui.panels.content;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class ContentPanel extends AbstractEloDisplayPanel {

	JTextArea textArea;

	public ContentPanel() {
		this.setLayout(new BorderLayout());

		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.add(new JScrollPane(this.textArea), BorderLayout.CENTER);
	}

	@Override
	public void setElo(IELO<IMetadataKey> elo) {
		IContent content = elo.getContent();
		if (content != null) {
			this.textArea.setText(content.getXml());
		} else {
			this.textArea.setText("");
		}
	}
}
