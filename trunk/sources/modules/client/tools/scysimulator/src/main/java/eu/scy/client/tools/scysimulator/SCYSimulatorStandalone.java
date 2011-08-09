package eu.scy.client.tools.scysimulator;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import java.awt.BorderLayout;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import sqv.SimQuestViewer;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.awt.Dimension;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import utils.FileName;

public class SCYSimulatorStandalone implements INotifiable {

	private final static Logger LOGGER = Logger.getLogger(SCYSimulatorStandalone.class.getName());
	private TupleSpace toolAliveSpace;
	private JPanel simquestPanel;
	private ToolBrokerAPI tbi = null;
	private JFrame mainFrame;
	private DataCollector dataCollector;

	public SCYSimulatorStandalone() throws URISyntaxException, InterruptedException, TupleSpaceException {
		SimQuestViewer simquestViewer = new SimQuestViewer(true);

		// URI fileUri = new
		// URI("http://www.scy-lab.eu/sqzx/RotatingPendulum.sqzx");
		// URI fileUri = new URI("http://www.scy-lab.eu/sqzx/balance.sqzx");
		 //URI fileUri = new URI("http://www.scy-lab.eu/sqzx/co2_house.sqzx");
		//URI fileUri = new URI("http://www.scy-lab.eu/sqzx/pizzagr.sqzx");
		// URI fileUri = new URI("http://alephnull.de/co2_house.sqzx");

		// FileName fileName = new
		//FileName fileName = new FileName("D:/projects/scy/sqzx/co2-converter/co2_converter.sqzx");
		//FileName("D:/projects/scy/sqzx/pizza/PizzaSimulation/pizza.sqx");
		// URI fileUri = new URI("file:lib/sqzx/pizza.sqzx");
		FileName fileName = new FileName("D:/projects/scy/sqzx/pizza/pizzagr/pizzagr.sqx");
		URI fileUri = fileName.toURI();

		LOGGER.log(Level.INFO, "trying to load: {0}", fileUri.toString());
		simquestViewer.setFile(fileUri);
		simquestViewer.createFrame(false);
		simquestPanel = new JPanel();
		dataCollector = null;

		try {
			JScrollPane scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			simquestViewer.setContainer(scroller.getParent());
			JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			split.setDividerLocation(0.75);
			split.setResizeWeight(0.75);
			split.setTopComponent(scroller);
			split.setEnabled(true);
			simquestPanel.add(split, BorderLayout.CENTER);
			simquestViewer.run();
			simquestViewer.getInterfacePanel().setPreferredSize(simquestViewer.getRealSize());
			scroller.setViewportView(simquestViewer.getInterfacePanel());
			
			dataCollector = new DataCollector(simquestViewer, tbi, "n/a");
			dataCollector.setPreferredSize(new Dimension(100, 300));
			
			split.setBottomComponent(dataCollector);
			simquestViewer.getInterface().updateVariables();
			
		} catch (java.lang.Exception e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
			JTextArea info = new JTextArea(4, 42);
			info.append("Simulation could not be loaded.\n");
			info.append("Probably the simulation file was not found,\n");
			info.append("it was expected at:\n");
			info.append(fileUri.toString());
			simquestPanel.add(info);
		}

		mainFrame = new JFrame();
		mainFrame.getContentPane().add(simquestPanel);
		mainFrame.pack();
		mainFrame.setSize(800, 650);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		simquestViewer.getInterface().updateVariables();
		// addJXLayer(simquestPanel, mainFrame);
		// String userName = dataCollector.getLogger().getUserName();
		// String toolName = dataCollector.getLogger().getToolName();
		String userName = "n/a";
		String toolName = "scysimulator standalone";
		// toolAliveSpace = new TupleSpace(new User(userName),
		// "scy.collide.info", 2525, "toolAliveSpace");
		// startAliveThread(toolAliveSpace, userName, toolName);
	}

	public static void main(String[] args) throws URISyntaxException, InterruptedException, TupleSpaceException {
		new SCYSimulatorStandalone();
	}

	@Override
	public boolean processNotification(INotification notification) {
		return dataCollector.processNotification(notification);
	}

}