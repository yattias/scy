/**
 * 
 */
package eu.scy.eloimporter.gui;

import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;
import eu.scy.eloimporter.gui.panels.content.ContentPanel;
import eu.scy.eloimporter.gui.panels.general.TitlePanel;
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
		if ("Title".equals(e.getPath().getLastPathComponent().toString().trim())) {
			TitlePanel titlePanel = new TitlePanel(this.application);
			titlePanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(titlePanel);
		}
		if ("Content".equals(e.getPath().getLastPathComponent().toString().trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			AbstractEloDisplayPanel contentPanel = new ContentPanel();
			contentPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(contentPanel);
			this.splitPane.setDividerLocation(dividerLocation);
		}
		if ("Technical".equals(e.getPath().getLastPathComponent().toString().trim())) {
			int dividerLocation = this.splitPane.getDividerLocation();
			TechnicalPanel technicalPanel = new TechnicalPanel(this.application);
			technicalPanel.setElo(this.application.getElo());
			this.splitPane.setRightComponent(technicalPanel);
			this.splitPane.setDividerLocation(dividerLocation);
		} else {
			System.out.println(e.getPath().getLastPathComponent());
		}
	}
}
