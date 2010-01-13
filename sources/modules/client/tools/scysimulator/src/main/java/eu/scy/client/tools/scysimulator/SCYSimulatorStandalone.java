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

    public SCYSimulatorStandalone() throws URISyntaxException, InterruptedException, TupleSpaceException {
        URI fileUri = new URI("http://www.scy-lab.eu/sqzx/balance.sqzx");
    	SimQuestViewer simquestViewer = new SimQuestViewer(true);
        //FileName fileName = new FileName("D:/temp/sqzx/balance.sqzx");
        //URI fileUri = fileName.toURI();
        System.out.println("SimQuestNode.createSimQuestNode(). trying to load: " + fileUri.toString());
        simquestViewer.setFile(fileUri);
        simquestViewer.createFrame(false);
        // TODO remove hardcoded username/pass
        tbi = new ToolBrokerImpl("Jan", "jan");
        tbi.registerForNotifications(this);
        simquestPanel = new JPanel();
        DataCollector dataCollector = null;

        try {
        	simquestViewer.run();
            simquestPanel.setLayout(new BorderLayout());
            simquestViewer.getInterfacePanel().setMinimumSize(new Dimension(450, 450));
            simquestPanel.add(simquestViewer.getInterfacePanel(), BorderLayout.CENTER);
            dataCollector = new DataCollector(simquestViewer, tbi);
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
        mainFrame.setSize(600, 650);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.addWindowListener(dataCollector);
        // addJXLayer(simquestPanel, mainFrame);
        String userName = dataCollector.getLogger().getUserName();
        String toolName = dataCollector.getLogger().getToolName();
        toolAliveSpace = new TupleSpace(new User(userName), "localhost", 2525, "toolAliveSpace");
        startAliveThread(toolAliveSpace, userName, toolName);
    }

    private void startAliveThread(final TupleSpace toolAliveSpace, final String userName, final String toolName) {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                TupleID alive = null;
                while (true) {

                    synchronized (this) {
                        try {
                            this.wait(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Tuple tuple = new Tuple(toolName, userName, "i'm alive...nanannannannannaa", TimeFormatHelper.getInstance().getCurrentTimeMillisAsISO8601());
                        tuple.setExpiration(3000);
                        if (alive == null) {
                            alive = toolAliveSpace.write(tuple);
                        } else {
                            toolAliveSpace.update(alive, tuple);
                        }
                    } catch (TupleSpaceException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();

    }

    private void addJXLayer(JComponent c, Frame f) {
        // wrap your component
        JXLayer<JComponent> layer = new JXLayer<JComponent>(c);
        JProgressBar jpb = new JProgressBar();
        jpb.setIndeterminate(true);
        Dimension d = new Dimension(100, 50);
        jpb.setMaximumSize(d);
        jpb.setMinimumSize(d);
        jpb.setPreferredSize(d);
        layer.setLayout(new BorderLayout());
        layer.add(jpb, BorderLayout.NORTH);

        // create custom LayerUI
        AbstractLayerUI<JComponent> layerUI = new AbstractLayerUI<JComponent>() {

            @Override
            protected void paintLayer(Graphics2D g2, JXLayer<JComponent> l) {
                // this paints layer as is
                super.paintLayer(g2, l);
                // custom painting:
                // here we paint translucent foreground
                // over the whole layer
                g2.setColor(new Color(0, 30, 30, 100));

                int strokeWidth = 5;
                Stroke oldStroke = g2.getStroke();
                Stroke newStroke = new BasicStroke(strokeWidth);
                g2.setStroke(newStroke);
                g2.fillRect(0, 0, l.getWidth() - strokeWidth, l.getHeight() - strokeWidth);
                g2.setStroke(oldStroke);
            }
        };

        // set our LayerUI
        layer.setUI(layerUI);

        // add the layer as a usual component
        f.add(layer);

    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException, TupleSpaceException {
        new SCYSimulatorStandalone();
    }

    @Override
    public void processNotification(INotification notification) {
        Map<String, String> props = notification.getProperties();

        String message = props.get("message");
        if (message != null) {
            final JDialog jd = new JDialog(mainFrame);
            jd.setSize(850, 600);
            jd.setLocationRelativeTo(mainFrame);
            jd.setTitle("Notification from: " + notification.getSender());
            jd.setLayout(new BorderLayout());
            JPanel northPanel = new JPanel(new BorderLayout());

            JLabel notiLabel = new JLabel("Notification from: " + notification.getSender());
            setFontStyle(notiLabel,24,Font.BOLD);
            notiLabel.setHorizontalAlignment(SwingConstants.CENTER);

            northPanel.add(notiLabel, BorderLayout.NORTH);
            JPanel centerNorth = new JPanel(new GridLayout(3, 1));
            JLabel sessionLabel = new JLabel("Session : " + notification.getSession());
            setFontStyle(sessionLabel, 15,Font.PLAIN);
            sessionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel missionLabel = new JLabel("Mission : " + notification.getMission());
            setFontStyle(missionLabel,15,Font.PLAIN);
            missionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel timeLabel = new JLabel("Time : " + notification.getTimestamp());
            setFontStyle(timeLabel,15,Font.PLAIN);
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            centerNorth.add(sessionLabel);
            centerNorth.add(missionLabel);
            centerNorth.add(timeLabel);
            northPanel.add(centerNorth, BorderLayout.CENTER);
            jd.add(northPanel, BorderLayout.NORTH);

            JPanel centerPanel;
            JTextArea jta = new JTextArea(notification.getProperty("message"));
            JLabel textLabel = new JLabel();
            textLabel.setBorder(BorderFactory.createTitledBorder("Message"));
//            JTextPane jta = new JTextPane();
//            jta.setText(notification.getProperty("message"));
            jta.setEditable(false);
//            jta.setLogicalStyle(centerStyle);
            JLabel imageLabel = new JLabel();
            ImageIcon ii;
            String image = props.get("image");
            byte[] decode = Base64.decode(image);
            if (image != null) {
                centerPanel= new JPanel(new GridLayout(1, 2));
                try {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decode));
                    ii = new ImageIcon(bufferedImage);
                    imageLabel.setIcon(ii);
                    imageLabel.setBorder(BorderFactory.createTitledBorder("Image"));
                    centerPanel.add(imageLabel);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }else{
                centerPanel= new JPanel(new GridLayout(1, 1));
                
            }
            textLabel.add(jta, BorderLayout.CENTER);
            centerPanel.add(jta);
            jd.add(centerPanel, BorderLayout.CENTER);
            JButton ok = new JButton("OK");
            ok.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    jd.dispose();
                }
            });
            jd.add(ok, BorderLayout.SOUTH);
            jd.pack();
            jd.setVisible(true);
        }
    }

    private void setFontStyle(JLabel label, int size, int style) {
        label.setFont(new Font("Serif", style, size));
    }
}
