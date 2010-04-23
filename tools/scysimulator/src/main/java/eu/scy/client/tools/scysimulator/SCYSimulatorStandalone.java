package eu.scy.client.tools.scysimulator;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.util.Base64;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

import sqv.SimQuestViewer;
import utils.FileName;
import eu.scy.actionlogging.TimeFormatHelper;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class SCYSimulatorStandalone implements INotifiable {

    private TupleSpace toolAliveSpace;

    private JPanel simquestPanel;

    private ToolBrokerAPI tbi = null;

    private JFrame mainFrame;

    private DataCollector dataCollector;

    public SCYSimulatorStandalone() throws URISyntaxException, InterruptedException, TupleSpaceException {
        //URI fileUri = new URI("http://www.scy-lab.eu/sqzx/RotatingPendulum.sqzx");
    	//URI fileUri = new URI("http://www.scy-lab.eu/sqzx/balance.sqzx");
    	//URI fileUri = new URI("http://localhost/co2_house.sqzx");
    	//URI fileUri = new URI("http://www.scy-lab.eu/sqzx/co2_converter.sqzx");
    	URI fileUri = new URI("http://alephnull.de/co2_house.sqzx");
    	
    	SimQuestViewer simquestViewer = new SimQuestViewer(false);
        //FileName fileName = new FileName("D:/temp/sqzx/co2-converter/co2_converter.sqzx");
        //FileName fileName = new FileName("D:/temp/sqzx/co2-house/co2_house.sqzx");
        //URI fileUri = fileName.toURI();
        System.out.println("SimQuestNode.createSimQuestNode(). trying to load: " + fileUri.toString());
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
            System.out.println("SimQuestNode.createSimQuestNode(). exception caught:");
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
       // System.out.println(System.currentTimeMillis());
            new SCYSimulatorStandalone();
    }

    @Override
    public void processNotification(INotification notification) {
        dataCollector.processNotification(notification);
      
    }

   
}
