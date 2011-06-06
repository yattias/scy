package eu.scy.eloimporter.gui.panels.technical;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.eloimporter.gui.EloImporterApplication;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class TechnicalPanel extends AbstractEloDisplayPanel {

	private JTextField formatTextfield;
	private JTextField sizeField;
	private EloImporterApplication application;

	public TechnicalPanel(EloImporterApplication app) {
		this.application = app;
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		JLabel formatLabel = new JLabel("Format:");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		layout.setConstraints(formatLabel, c);
		this.add(formatLabel);

		this.formatTextfield = new JTextField("text/html");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		layout.setConstraints(this.formatTextfield, c);
		this.add(this.formatTextfield);

		JLabel sizeLabel = new JLabel("Size (Bytes):");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		layout.setConstraints(sizeLabel, c);
		this.add(sizeLabel);

		this.sizeField = new JTextField("1024");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		layout.setConstraints(this.sizeField, c);
		this.add(this.sizeField);
	}

	@Override
	public void setElo(IELO<IMetadataKey> elo) {
		IMetadataTypeManager<IMetadataKey> typeManager = this.application.getImporter()
				.getTypeManager();

		IMetadataValueContainer typeValue = elo.getMetadata().getMetadataValueContainer(
				typeManager.getMetadataKey("type"));
		String type = (String) typeValue.getValue();
		this.formatTextfield.setText(type);

		IMetadataValueContainer sizeValue = elo.getMetadata().getMetadataValueContainer(
				typeManager.getMetadataKey("size"));
		Long size = (Long) sizeValue.getValue();
		if (size != null) {
			this.sizeField.setText("" + size);
		} else {
			this.sizeField.setText("");
		}
	}

}
