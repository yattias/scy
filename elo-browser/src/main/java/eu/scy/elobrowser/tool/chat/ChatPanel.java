/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.chat;

import eu.scy.collaborationservice.CollaborationService;
import eu.scy.collaborationservice.CollaborationServiceClientInterface;
import eu.scy.core.model.impl.ScyBaseObject;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author lars
 */
public class ChatPanel extends JPanel implements CollaborationServiceClientInterface {

    private static final long serialVersionUID = 1L;
	private static final String HARD_CODED_TOOL_NAME = "Spiffy Awareness Client";
    private static final long LOGIN_KEEPALIVE_DURATION = 30 * 1000;
    //private static final String[] COLUMN_NAMES = { "username", "status", "doc id"};

    private JTextArea textArea;
    private JPanel panel;
    private HashMap<String, Boolean> usersToWatch = new HashMap<String, Boolean>();
    private HashMap<String, Object> toolsToWatch = new HashMap<String, Object>();
    private String userName = "jeremyt";
    private String loginId;

    private CollaborationService cs;

    private Timer loginTimer;

    
    private ScyBaseObject temporarySbo;

    
    public ChatPanel() {

        super();
        //this.userName = userName;
        this.toolsToWatch.put(HARD_CODED_TOOL_NAME, HARD_CODED_TOOL_NAME);
        this.usersToWatch = new HashMap<String, Boolean>();
        this.usersToWatch.put("thomasd", false);
        this.usersToWatch.put("olesm", false);
        this.usersToWatch.put("janad", false);
        
        // Set the frame characteristics
        //this.setTitle( "Awereness client makes " + userName + " happy");
      
        this.textArea = new JTextArea(15, 80);
        this.textArea.setEditable(false);
        this.textArea.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        this.textArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(this.textArea);

        this.setLayout(new BorderLayout());
        this.add(scrollingText, BorderLayout.CENTER);
        this.setVisible(true);

        this.cs = CollaborationService.createCollaborationService(this.userName, CollaborationService.AWARENESS_SERVICE_SPACE, this);
        this.signUp();

        this.loginTimer = new Timer();
        this.loginTimer.schedule(this.new LoginTimer(), 1, LOGIN_KEEPALIVE_DURATION);
    }

    private void signUp() {
        //logger.debug("SAC signing up");
        this.loginId = cs.write(HARD_CODED_TOOL_NAME, getTemporarySbo(), LOGIN_KEEPALIVE_DURATION + 2000);
    }

    private void generateBuddyList() {
        //logger.debug("SAC refreshes buddylist every " + LOGIN_KEEPALIVE_DURATION);
        String outputToTextArea = "";
        String status;
        for (String user : usersToWatch.keySet()) {
            if (cs.read(user, HARD_CODED_TOOL_NAME) != null) {
                status = "online";
            } else {
                status = "offline";
            }
        	outputToTextArea = outputToTextArea + user + "\t" + status + "\n";
        }
        textArea.setText(outputToTextArea);
    }


        private void refreshBuddyList() {
        String outputToTextArea = "";
        String status;
        for (String user : usersToWatch.keySet()) {
            if (usersToWatch.get(user)) {
                status = "online";
            } else {
                status = "offline";
            }
        	outputToTextArea = outputToTextArea + user + "\t" + status + "\n";
        }
        final String text = outputToTextArea;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                textArea.setText(text);
            }
        });
        
    }


    private class LoginTimer extends TimerTask {
        public void run() {
            System.out.println("renewing signup");
            cs.write(loginId, HARD_CODED_TOOL_NAME, getTemporarySbo(), LOGIN_KEEPALIVE_DURATION + 2000);
        }
    }

//    private class BuddyPresenceTimer extends TimerTask {
//        public void run() {
//           System.out.println("fetching buddies");
//           refreshBuddyList();
//        }
//    }

    private void signOff() {
        cs.takeById(this.loginId);
    }

    private ScyBaseObject getTemporarySbo() {
        if(temporarySbo == null) {
            temporarySbo = new ScyBaseObject();
            temporarySbo.setId("");
            temporarySbo.setName("");
            temporarySbo.setDescription("");
        }
        
        return temporarySbo;
    }
    

	public void actionUponDelete(String username) {
        this.usersToWatch.put(username, false);
        refreshBuddyList();
    }


	public void actionUponWrite(String username) {
        this.usersToWatch.put(username, true);
        refreshBuddyList();
	}

    public void DELETE_ME_QUICKLY() {
        this.usersToWatch.put("janad", true);
    }
}
