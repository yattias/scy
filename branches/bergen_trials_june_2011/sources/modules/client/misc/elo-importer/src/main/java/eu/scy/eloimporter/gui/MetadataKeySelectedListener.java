/**
 * 
 */
package eu.scy.eloimporter.gui;

import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;
import eu.scy.eloimporter.gui.panels.content.ContentPanel;
import eu.scy.eloimporter.gui.panels.educational.FunctionalRolePanel;
import eu.scy.eloimporter.gui.panels.general.AggregationLevelPanel;
import eu.scy.eloimporter.gui.panels.general.DescriptionPanel;
import eu.scy.eloimporter.gui.panels.general.GeneralPanel;
import eu.scy.eloimporter.gui.panels.general.KeywordPanel;
import eu.scy.eloimporter.gui.panels.general.LogicalRepresentationPanel;
import eu.scy.eloimporter.gui.panels.general.TitlePanel;
import eu.scy.eloimporter.gui.panels.lifecycle.ContributionPanel;
import eu.scy.eloimporter.gui.panels.technical.TechnicalPanel;

public final class MetadataKeySelectedListener implements TreeSelectionListener {

	private JSplitPane splitPane;
	private EloImporterApplication application;

	/**
	 * @param splitPane
	 * @param eloImporterApplication
	 */
	MetadataKeySelectedListener(EloImporterApplication app) {
		this.application = app;

		this.splitPane = this.application.getSplitPane();
	}

	public void valueChanged(TreeSelectionEvent e) {
		if ("General".equals(e.getPath().getLastPathComponent().toString()
				.trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			GeneralPanel generalPanel = new GeneralPanel(this.application);
			generalPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(generalPanel);
			this.splitPane.setDividerLocation(dividerLocation);
		}
		if ("Title"
				.equals(e.getPath().getLastPathComponent().toString().trim())) {
			TitlePanel titlePanel = new TitlePanel(this.application);
			titlePanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(titlePanel);
		}
		if ("Aggregation Level".equals(e.getPath().getLastPathComponent()
				.toString().trim())) {
			AggregationLevelPanel aggregationLevelPanel = new AggregationLevelPanel(
					this.application);
			aggregationLevelPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(aggregationLevelPanel);
		}
		if ("Description".equals(e.getPath().getLastPathComponent().toString()
				.trim())) {
			DescriptionPanel descriptionPanel = new DescriptionPanel(
					this.application);
			descriptionPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(descriptionPanel);
		}
		if ("Keywords".equals(e.getPath().getLastPathComponent().toString()
				.trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			KeywordPanel keywordPanel = new KeywordPanel(this.application);
			keywordPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(keywordPanel);
			this.splitPane.setDividerLocation(dividerLocation);
		}
		if ("Logical Representation".equals(e.getPath().getLastPathComponent()
				.toString().trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			LogicalRepresentationPanel logicalRepresentationPanel = new LogicalRepresentationPanel(
					this.application);
			logicalRepresentationPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(logicalRepresentationPanel);
			this.splitPane.setDividerLocation(dividerLocation);
		}
		if ("Content".equals(e.getPath().getLastPathComponent().toString()
				.trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			AbstractEloDisplayPanel contentPanel = new ContentPanel();
			contentPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(contentPanel);
			this.splitPane.setDividerLocation(dividerLocation);
		}
		if ("Technical".equals(e.getPath().getLastPathComponent().toString()
				.trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			TechnicalPanel technicalPanel = new TechnicalPanel(this.application);
			technicalPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(technicalPanel);
			this.splitPane.setDividerLocation(dividerLocation);
		}
		if ("Contribute".equals(e.getPath().getLastPathComponent().toString()
				.trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			ContributionPanel contributionPanel = new ContributionPanel(
					this.application);
			contributionPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(contributionPanel);
			this.splitPane.setDividerLocation(dividerLocation);
		}
		if ("Functional Role".equals(e.getPath().getLastPathComponent()
				.toString().trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			FunctionalRolePanel functionalRolePanel = new FunctionalRolePanel(
					this.application);
			functionalRolePanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(functionalRolePanel);
			this.splitPane.setDividerLocation(dividerLocation);
		} else {
			// System.out.println(e.getPath().getLastPathComponent());
		}
	}
}
