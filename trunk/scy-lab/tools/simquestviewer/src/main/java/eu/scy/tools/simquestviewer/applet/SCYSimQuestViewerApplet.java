package eu.scy.tools.simquestviewer.applet;

import java.awt.BorderLayout;

import eu.scy.tools.simquestviewer.client.SCYSimQuestViewerAppletIntegration;

import sqv.SimQuestViewerApplet;

public class SCYSimQuestViewerApplet extends SimQuestViewerApplet implements SCYSimQuestViewerAppletIntegration {

	// run it with
	// <PARAM NAME="file" VALUE="http://pervulgo.hopto.org/sqv/balance.sqzx">
	//    (or wherever you have put a *.sqzx file)
	// <PARAM NAME="security" VALUE="false">

	public void init() {
		super.init();
		// injecting the SCY datacollector
		DataCollector dataCollector = new DataCollector(this.getSimQuestViewer());
		this.getContentPane().add(dataCollector, BorderLayout.SOUTH);
	}
}