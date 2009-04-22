package eu.scy.eloimporter.gui.panels.general;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.eloimporter.StructureValues;
import eu.scy.eloimporter.gui.EloImporterApplication;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class LogicalRepresentationPanel extends AbstractEloDisplayPanel {

	private EloImporterApplication application;
	IMetadataKey logicalRepresentationKey;
	private JComboBox logicalRepresentationComboBox;
	IELO<IMetadataKey> elo;

	public LogicalRepresentationPanel(EloImporterApplication app) {
		application = app;
		this.elo = this.application.getElo();

		IMetadataTypeManager<IMetadataKey> typeManager = this.application
				.getImporter().getTypeManager();
		this.logicalRepresentationKey = typeManager
				.getMetadataKey("logical_representation/value");

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.logicalRepresentationComboBox = new JComboBox(StructureValues
				.values());
		this.logicalRepresentationComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				StructureValues selectedItem = (StructureValues) e.getItem();
				// System.out.println(selectedItem);
				IMetadataValueContainer value = elo.getMetadata()
						.getMetadataValueContainer(logicalRepresentationKey);
				value.setValue(selectedItem.toString());
			}
		});
		layout.setConstraints(this.logicalRepresentationComboBox, c);
		this.add(this.logicalRepresentationComboBox);
	}

	@Override
	public void setElo(IELO<IMetadataKey> elo) {
		this.elo = elo;

		String logicalRepresentation = (String) elo.getMetadata()
				.getMetadataValueContainer(logicalRepresentationKey).getValue();
		logicalRepresentationComboBox.setSelectedItem(StructureValues
				.valueOf(logicalRepresentation));
	}
}
