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
import eu.scy.eloimporter.gui.EloImporterApplication;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class AggregationLevelPanel extends AbstractEloDisplayPanel {

	private EloImporterApplication application;
	IMetadataKey aggregationLevelKey;
	private JComboBox aggregationLevelComboBox;
	IELO<IMetadataKey> elo;

	public AggregationLevelPanel(EloImporterApplication app) {
		this.application = app;
		this.elo = this.application.getElo();
		IMetadataTypeManager<IMetadataKey> typeManager = this.application.getImporter()
				.getTypeManager();
		this.aggregationLevelKey = typeManager.getMetadataKey("aggregation");

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.aggregationLevelComboBox = new JComboBox(new Integer[] { 1, 2, 3, 4 });
		this.aggregationLevelComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				Integer selectedItem = (Integer) e.getItem();
				// System.out.println(selectedItem);
				IMetadataValueContainer value = AggregationLevelPanel.this.elo.getMetadata()
						.getMetadataValueContainer(AggregationLevelPanel.this.aggregationLevelKey);
				value.setValue(selectedItem);
			}
		});
		layout.setConstraints(this.aggregationLevelComboBox, c);
		this.add(this.aggregationLevelComboBox);
	}

	@Override
	public void setElo(IELO<IMetadataKey> elo) {
		this.elo = elo;
		IMetadataValueContainer value = elo.getMetadata().getMetadataValueContainer(
				this.aggregationLevelKey);
		this.aggregationLevelComboBox.setSelectedItem(value.getValue());
	}
}
