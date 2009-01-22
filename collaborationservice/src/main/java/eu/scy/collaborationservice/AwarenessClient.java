package eu.scy.collaborationservice;


import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;


public class AwarenessClient extends JFrame {
    
    private final static Logger logger = Logger.getLogger(AwarenessClient.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "Spiffy Awareness Client";
    private static final long LOGIN_KEEPALIVE_DURATION = 1 * 1000;
    private static final String[] COLUMN_NAMES = { "username", "status", "doc id"};

    private JTable table;
    private JPanel panel;
    private JScrollPane scrollPane;
    private ArrayList<String> usersToWatch;
    private HashMap<String, Object> toolsToWatch = new HashMap<String, Object>();
    private String userName;
    private String loginId;

    private CollaborationService cs;
    
    private Timer loginTimer;
    private Timer buddyTimer;

    
    public AwarenessClient() {        
    }

    
    public static AwarenessClient createAwarenessClient(String userName, String toolName) {
        logger.debug("Awareness is upon you.");
        
        AwarenessClient ac = new AwarenessClient();
        ac.userName = userName;
        ac.toolsToWatch.put(toolName, toolName);
        ac.usersToWatch = new ArrayList<String>(Arrays.asList(new String[]{"jeremyt", "olesm", "janad"})); //this list will be populated by usermanagement
        
        ac.cs = CollaborationService.createCollaborationService(ac.userName, CollaborationService.AWARENESS_SERVICE_SPACE);
        ac.signUp();
        
        // Set the frame characteristics
        ac.setTitle( "Awereness client makes " + userName + " happy");
        ac.setSize(300, 500);

        ac.refreshBuddyList();
        
        ac.loginTimer = new Timer();
        ac.loginTimer.schedule(ac.new LoginTimer(), 1, LOGIN_KEEPALIVE_DURATION);
        ac.buddyTimer = new Timer();
        ac.buddyTimer.schedule(ac.new BuddyPresenceTimer(), 1, LOGIN_KEEPALIVE_DURATION);
        return ac;
    }
    
    
    private void refreshBuddyList() {
        logger.debug("SAC refreshes buddylist every " + LOGIN_KEEPALIVE_DURATION);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        table = new JTable(populateTable(usersToWatch, COLUMN_NAMES), COLUMN_NAMES);        
        table.setBackground(Color.orange);
        scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private class LoginTimer extends TimerTask {
        public void run() {
            signOff();
            signUp();
        }
    }
    
    private class BuddyPresenceTimer extends TimerTask {
        public void run() {
            getContentPane().remove(panel);
            refreshBuddyList();
            usersToWatch.add("mickey");
        }
    }


    private void signUp() {
        logger.debug("SAC signing up");
        ScyBaseObject sbo = new ScyBaseObject();
        sbo.setId("");
        sbo.setName("");
        sbo.setDescription("");
        this.loginId = cs.write(HARD_CODED_TOOL_NAME, sbo, LOGIN_KEEPALIVE_DURATION + 2000);
    }
    
    
    private void signOff() {
        logger.debug("SAC signing off");
        cs.takeById(this.loginId);
    }


    private String[][] populateTable(ArrayList<String> users, String[] columnNames) {
        String dataValues[][] = new String[users.size()][columnNames.length];
        for (int i = 0 ; i < users.size() ; i++) {
            dataValues[i][0] = users.get(i);
            dataValues[i][1] = "online";
            dataValues[i][2] = "some document";
        }
        return dataValues;
    }
 
}
