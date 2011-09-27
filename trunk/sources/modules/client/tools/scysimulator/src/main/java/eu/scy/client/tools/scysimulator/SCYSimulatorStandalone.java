package eu.scy.client.tools.scysimulator;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sqv.ModelVariable;
import sqv.SimQuestViewer;
import sqv.widgets.DynamicWidget;
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

	public SCYSimulatorStandalone(String[] args) throws URISyntaxException, InterruptedException, TupleSpaceException {
		SimQuestViewer simquestViewer = new SimQuestViewer(true);

		URI fileUri;
		if (args.length == 0) {
			// no argument has been given, take the balance simulation (or something else)
			// as a fallback
			fileUri = new URI("http://www.scy-lab.eu/sqzx/balance.sqzx");
			//fileUri = new URI("http://www.scy-lab.eu/sqzx/co2_house.sqzx");
			//fileUri = new URI("http://www.scy-lab.eu/sqzx/pizza.sqzx");
			//fileUri = new URI("http://www.scy-lab.eu/sqzx/glucose.sqzx");
			//fileUri = new URI("http://www.scy-lab.eu/sqzx/RotatingPendulum.sqzx");
		} else {
			if (args[0].toLowerCase().startsWith("http://")) {
				fileUri = new URI(args[0]);
			} else {
				FileName fileName = new FileName(args[0]);
				//FileName fileName = new FileName("D:/projects/scy/sqzx/co2-converter/co2_converter.sqzx");
				//FileName fileName = new FileName("D:/projects/scy/sqzx/glucose/glucose.sqx");
				//FileName fileName = new FileName("D:/projects/scy/sqzx/pizza/feedback/pizza/pizza.sqx");
				fileUri = fileName.toURI();	
			}
		}

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
		
		// addJXLayer(simquestPanel, mainFrame);
		// String userName = dataCollector.getLogger().getUserName();
		// String toolName = dataCollector.getLogger().getToolName();
		String userName = "n/a";
		String toolName = "scysimulator standalone";
		// toolAliveSpace = new TupleSpace(new User(userName),
		// "scy.collide.info", 2525, "toolAliveSpace");
		// startAliveThread(toolAliveSpace, userName, toolName);
		EventQueue.invokeLater(new InterfaceUpdater(simquestViewer));
	}

	public static void main(String[] args) throws URISyntaxException, InterruptedException, TupleSpaceException {
		new SCYSimulatorStandalone(args);
	}

	@Override
	public boolean processNotification(INotification notification) {
		return dataCollector.processNotification(notification);
	}
	
	private final class InterfaceUpdater implements Runnable {
	    private SimQuestViewer simquestViewer;
		public InterfaceUpdater(SimQuestViewer simquestViewer) {
	    	this.simquestViewer = simquestViewer;
	    }
		public void run(){
			// bruteforce interface update
			for (ModelVariable variable: simquestViewer.getDataServer().getVariables("n/a")) {
				variable.set();
			}
			simquestViewer.getInterface().updateVariables();
	    }
	  }

}