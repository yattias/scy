package eu.scy.eloimporter.gui.panels.general;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.eloimporter.gui.EloImporterApplication;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class GeneralPanel extends AbstractEloDisplayPanel {

	private EloImporterApplication application;
	private TitlePanel titlePanel;
	private AggregationLevelPanel aggregationLevelPanel;
	private DescriptionPanel descriptionPanel;
	private KeywordPanel keywordPanel;
	private LogicalRepresentationPanel logicalRepresentationPanel;

	public GeneralPanel(EloImporterApplication app) {
		this.application = app;
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.fill = GridBagConstraints.BOTH;
		this.titlePanel = new TitlePanel(this.application);
		this.titlePanel.setBorder(BorderFactory.createTitledBorder("Title"));
		layout.setConstraints(this.titlePanel, c);
		this.add(this.titlePanel);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.fill = GridBagConstraints.BOTH;
		this.aggregationLevelPanel = new AggregationLevelPanel(this.application);
		this.aggregationLevelPanel.setBorder(BorderFactory
				.createTitledBorder("Aggregation Level"));
		layout.setConstraints(this.aggregationLevelPanel, c);
		this.add(this.aggregationLevelPanel);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		this.descriptionPanel = new DescriptionPanel(this.application);
		this.descriptionPanel.setBorder(BorderFactory
				.createTitledBorder("Description"));
		layout.setConstraints(this.descriptionPanel, c);
		this.add(this.descriptionPanel);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.fill = GridBagConstraints.BOTH;
		this.keywordPanel = new KeywordPanel(this.application);
		this.keywordPanel.setBorder(BorderFactory
				.createTitledBorder("Keywords"));
		layout.setConstraints(this.keywordPanel, c);
		this.add(this.keywordPanel);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.fill = GridBagConstraints.BOTH;
		this.logicalRepresentationPanel = new LogicalRepresentationPanel(
				this.application);
		this.logicalRepresentationPanel.setBorder(BorderFactory
				.createTitledBorder("Logical Representation"));
		layout.setConstraints(this.logicalRepresentationPanel, c);
		this.add(this.logicalRepresentationPanel);
	}

	@Override
	public void setElo(IELO<IMetadataKey> elo) {
		this.titlePanel.setElo(elo);
		this.aggregationLevelPanel.setElo(elo);
		this.descriptionPanel.setElo(elo);
		this.keywordPanel.setElo(elo);
		this.logicalRepresentationPanel.setElo(elo);
	}

}
