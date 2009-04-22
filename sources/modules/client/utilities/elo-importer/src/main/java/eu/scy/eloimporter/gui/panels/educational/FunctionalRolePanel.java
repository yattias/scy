package eu.scy.eloimporter.gui.panels.educational;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.eloimporter.LearningResourceTypeValues;
import eu.scy.eloimporter.gui.EloImporterApplication;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class FunctionalRolePanel extends AbstractEloDisplayPanel {

	private EloImporterApplication application;
	IMetadataKey functionalRoleKey;
	private JComboBox functionalRoleComboBox;
	IELO<IMetadataKey> elo;

	public FunctionalRolePanel(EloImporterApplication app) {
		application = app;
		this.elo = this.application.getElo();

		IMetadataTypeManager<IMetadataKey> typeManager = this.application
				.getImporter().getTypeManager();
		this.functionalRoleKey = typeManager
				.getMetadataKey("functional_role/value");

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		this.functionalRoleComboBox = new JComboBox(LearningResourceTypeValues
				.values());
		this.functionalRoleComboBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				LearningResourceTypeValues selectedItem = (LearningResourceTypeValues) e
						.getItem();
				IMetadataValueContainer value = elo.getMetadata()
						.getMetadataValueContainer(functionalRoleKey);
				value.setValue(selectedItem.toString());
			}
		});

		{
			JLabel functionalRoleLabel = new JLabel("Functional Role:");
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.fill = GridBagConstraints.NONE;
			c.insets = new Insets(2, 2, 2, 2);
			c.anchor = GridBagConstraints.WEST;
			layout.setConstraints(functionalRoleLabel, c);
			add(functionalRoleLabel);
		}
		{
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1.0;
			c.weighty = 0.0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(2, 2, 2, 2);
			layout.setConstraints(this.functionalRoleComboBox, c);
			this.add(this.functionalRoleComboBox);
		}
	}

	@Override
	public void setElo(IELO<IMetadataKey> elo) {
		this.elo = elo;

		String logicalRepresentation = (String) elo.getMetadata()
				.getMetadataValueContainer(functionalRoleKey).getValue();
		functionalRoleComboBox.setSelectedItem(LearningResourceTypeValues
				.valueOf(logicalRepresentation));
	}
}
