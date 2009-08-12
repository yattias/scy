package eu.scy.client.tools.scysimulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sqv.SimQuestViewer;

public class SCYSimulatorStandalone {

	public SCYSimulatorStandalone() throws URISyntaxException {
		URI fileUri = new URI("http://scy.collide.info/balance.sqzx");
		SimQuestViewer simquestViewer = new SimQuestViewer(false);
		// FileName fileName = new FileName("src/main/java/eu/scy/elobrowser/tool/simquest/balance.sqzx");
		// fileUri = fileName.toURI();
		System.out
		.println("SimQuestNode.createSimQuestNode(). trying to load: "+fileUri.getPath().toString());
		simquestViewer.setFile(fileUri);
		simquestViewer.createFrame(false);

		JPanel simquestPanel = new JPanel();
		DataCollector dataCollector = null;
		EloSimQuestWrapper eloSimQuestWrapper = new EloSimQuestWrapper();

		try {
			simquestViewer.run();

			simquestPanel.setLayout(new BorderLayout());
			simquestViewer.getInterfacePanel().setMinimumSize(
					new Dimension(450, 450));
			simquestPanel.add(simquestViewer.getInterfacePanel(),
					BorderLayout.CENTER);

			dataCollector = new DataCollector(simquestViewer);
			simquestPanel.add(dataCollector, BorderLayout.SOUTH);

		} catch (java.lang.Exception e) {
			System.out
			.println("SimQuestNode.createSimQuestNode(). exception caught:");
			e.printStackTrace();

			JTextArea info = new JTextArea(4, 42);
			info.append("Simulation could not be loaded.\n");
			info.append("Probably the simulation file was not found,\n");
			info.append("it was expected at:\n");
			info.append(fileUri.getPath().toString());
			simquestPanel.add(info);
		}
		
		JFrame mainFrame = new JFrame();
		mainFrame.getContentPane().add(simquestPanel);
		mainFrame.pack();
		mainFrame.setSize(600,650);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.addWindowListener(dataCollector);
		
	}

	public static void main(String[] args) throws URISyntaxException {
		new SCYSimulatorStandalone();
	}
}
