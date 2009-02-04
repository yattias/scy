package eu.scy.collaborationservice.liteclient;


import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.CollaborationService;
import eu.scy.collaborationservice.CollaborationServiceClientInterface;
import eu.scy.core.model.impl.ScyBaseObject;


public class AwarenessClient extends JFrame implements CollaborationServiceClientInterface {
    
    private final static Logger logger = Logger.getLogger(AwarenessClient.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "Spiffy Awareness Client";
    private static final long LOGIN_KEEPALIVE_DURATION = 1 * 1000;
    //private static final String[] COLUMN_NAMES = { "username", "status", "doc id"};

    private JTextArea textArea;
    private JPanel panel;
    private JScrollPane scrollPane;
    private ArrayList<String> usersToWatch;
    private HashMap<String, Object> toolsToWatch = new HashMap<String, Object>();
    private String userName;
    private String loginId;

    private CollaborationService cs;
    
    private Timer loginTimer;
    private Timer buddyTimer;
    
    private TableModel tableModel;

    
    public AwarenessClient() {        
    }

    
    public static AwarenessClient createAwarenessClient(String userName, String toolName) {
        logger.debug("Awareness is upon you.");
        
        AwarenessClient ac = new AwarenessClient();
        ac.userName = userName;
        ac.toolsToWatch.put(toolName, toolName);
        ac.usersToWatch = new ArrayList<String>(Arrays.asList(new String[]{"jeremyt", "olesm", "janad"})); //this list will be populated by usermanagement
        
        ac.cs = CollaborationService.createCollaborationService(ac.userName, CollaborationService.AWARENESS_SERVICE_SPACE, ac);
        ac.signUp();
        
        // Set the frame characteristics
        ac.setTitle( "Awereness client makes " + userName + " happy");
        ac.setSize(300, 500);
       
        ac.textArea = new JTextArea(15, 80);
        ac.textArea.setEditable(false);
        ac.textArea.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        ac.textArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(ac.textArea);
        
        ac.panel = new JPanel();
        ac.panel.setLayout(new BorderLayout());
        ac.panel.add(scrollingText, BorderLayout.CENTER);
        
        
        ac.getContentPane().add(ac.panel);
        ac.setLocationRelativeTo(null);
        ac.setLocation(ac.getLocation().x + 200, ac.getLocation().y + 200);
        ac.setVisible(true);
        
        ac.loginTimer = new Timer();
        ac.loginTimer.schedule(ac.new LoginTimer(), 1, LOGIN_KEEPALIVE_DURATION);
        ac.buddyTimer = new Timer();
        ac.buddyTimer.schedule(ac.new BuddyPresenceTimer(), 1, LOGIN_KEEPALIVE_DURATION);
        return ac;
    }
    
    
    private void refreshBuddyList() {
        logger.debug("SAC refreshes buddylist every " + LOGIN_KEEPALIVE_DURATION);
        String outputToTextArea = "";
        String user;
        String status;
        for (int i = 0 ; i<usersToWatch.size() ; i++) {
            user = usersToWatch.get(i);
            if (cs.read(user, HARD_CODED_TOOL_NAME) != null) {
                status = "online";
            } else {
                status = "offline";
            }
        	outputToTextArea = outputToTextArea + user + "\t" + status + "\n";
        }
        textArea.setText(outputToTextArea);
    }


    private class LoginTimer extends TimerTask {
        public void run() {
            signOff();
            signUp();
        }
    }
    
    private class BuddyPresenceTimer extends TimerTask {
        public void run() {
           refreshBuddyList();
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
    
    public void shutDown() {
    	 setVisible(false);
         dispose();
    }


    public void actionUponDelete(String username) {
        // TODO Auto-generated method stub
    }


    public void actionUponWrite(String username) {
        // TODO Auto-generated method stub     
    }
}
