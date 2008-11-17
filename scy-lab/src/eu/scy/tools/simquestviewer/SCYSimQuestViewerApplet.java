package eu.scy.tools.simquestviewer;

import java.awt.BorderLayout;

import sqv.SimQuestViewerApplet;

public class SCYSimQuestViewerApplet extends SimQuestViewerApplet {

	public void init() {
		super.init();
		// injecting the SCY datacollector
		DataCollector dataCollector = new DataCollector(this.getSimQuestViewer());
		this.getContentPane().add(dataCollector, BorderLayout.SOUTH);
	}
}