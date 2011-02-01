package eu.scy.client.tools.scysimulator;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import java.awt.BorderLayout;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import sqv.SimQuestViewer;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.logging.Logger;

public class SCYSimulatorStandalone implements INotifiable {

    private final static Logger LOGGER = Logger.getLogger(SCYSimulatorStandalone.class.getName());
    private TupleSpace toolAliveSpace;
    private JPanel simquestPanel;
    private ToolBrokerAPI tbi = null;
    private JFrame mainFrame;
    private DataCollector dataCollector;

    public SCYSimulatorStandalone() throws URISyntaxException, InterruptedException, TupleSpaceException {
        //URI fileUri = new URI("http://www.scy-lab.eu/sqzx/RotatingPendulum.sqzx");
    	//URI fileUri = new URI("http://www.scy-lab.eu/sqzx/balance.sqzx");
    	//URI fileUri = new URI("http://localhost/co2_house.sqzx");
    	URI fileUri = new URI("http://www.scy-lab.eu/sqzx/co2_house.sqzx");
    	//URI fileUri = new URI("http://alephnull.de/co2_house.sqzx");
    	
    	SimQuestViewer simquestViewer = new SimQuestViewer(false);
        //FileName fileName = new FileName("D:/temp/sqzx/co2-converter/co2_converter.sqzx");
        //FileName fileName = new FileName("D:/temp/sqzx/co2-house/co2_house.sqzx");
        //URI fileUri = fileName.toURI();
        LOGGER.info("SimQuestNode.createSimQuestNode(). trying to load: " + fileUri.toString());
        simquestViewer.setFile(fileUri);
        simquestViewer.createFrame(false);
        // TODO remove hardcoded username/pass
        //tbi = new ToolBrokerImpl("Jan", "jan");
        //tbi.registerForNotifications(this);
        simquestPanel = new JPanel();
        dataCollector = null;

        try {
            simquestViewer.run();
            simquestPanel.setLayout(new BorderLayout());
            simquestViewer.getInterfacePanel().setMinimumSize(simquestViewer.getRealSize());
            simquestPanel.add(simquestViewer.getInterfacePanel(), BorderLayout.CENTER);
            dataCollector = new DataCollector(simquestViewer, tbi, "n/a");
            simquestPanel.add(dataCollector, BorderLayout.SOUTH);
        } catch (java.lang.Exception e) {
            LOGGER.warning("SimQuestNode.createSimQuestNode(). exception caught:");
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
        String userName = dataCollector.getLogger().getUserName();
        String toolName = dataCollector.getLogger().getToolName();
        toolAliveSpace = new TupleSpace(new User(userName), "scy.collide.info", 2525, "toolAliveSpace");
        startAliveThread(toolAliveSpace, userName, toolName);
    }

    private void startAliveThread(final TupleSpace toolAliveSpace, final String userName, final String toolName) {
//        Thread t = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                TupleID alive = null;
//                while (true) {
//
//                    synchronized (this) {
//                        try {
//                            this.wait(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    try {
//                        Tuple tuple = new Tuple(toolName, userName, "i'm alive...nanannannannannaa", TimeFormatHelper.getInstance().getCurrentTimeMillisAsISO8601());
//                        tuple.setExpiration(3000);
//                        if (alive == null) {
//                            alive = toolAliveSpace.write(tuple);
//                        } else {
//                            toolAliveSpace.update(alive, tuple);
//                        }
//                    } catch (TupleSpaceException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });
//        t.start();
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException, TupleSpaceException {
            new SCYSimulatorStandalone();
    }

    @Override
    public boolean processNotification(INotification notification) {
        return dataCollector.processNotification(notification);
    }

}