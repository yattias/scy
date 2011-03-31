package eu.scy.chat.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.util.StringUtils;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;

/**
 * Does OOO chat (One on One)
 * 
 * @author anthonjp
 *
 */
public class OOOChatController implements ChatController {

    static final Logger logger = Logger.getLogger(OOOChatController.class.getName());
    private DefaultListModel buddyList = new DefaultListModel();
    IAwarenessService awarenessService;

    public OOOChatController(IAwarenessService awarenessService) {
        logger.debug("OOOChatController: starting ... ");
        logger.debug("OOChatController: awarenessService.isConnected(): " + awarenessService.isConnected());
        this.awarenessService = awarenessService;
    }

    public String getCurrentUser() {
        String user = getAwarenessService().getConnection().getUser();
        if (user != null) {
            return StringUtils.parseName(user);
        }

        return "NO ONE";
    }

    public DefaultListModel populateBuddyList() {
        // get this from the awareness service

        buddyList = new DefaultListModel();

        List<IAwarenessUser> buddies = null;
        if (getAwarenessService() != null && getAwarenessService().isConnected()) {
            try {
                buddies = getAwarenessService().getBuddies();
            } catch (AwarenessServiceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (buddies != null) {
                logger.debug("ChatController: populateBuddyList: num of buddies: "
                        + buddies.size());
                for (IAwarenessUser b : buddies) {
                    b.setNickName(b.getNickName());
                    buddyList.addElement(b);
                    logger.debug("ChatController: populateBuddyList: buddy name: "
                            + b.getNickName());
                }
            }
        } else {
            //buddyList.addElement("no buddies");
            logger.debug("ChatController: populateBuddyList: ####################### No buddies ###########################");
        }
        logger.debug("ChatController: populateBuddyList: ####################### end ###########################");
        return buddyList;
    }

    public void sendMessage(IAwarenessUser recipient, final String message) {
        // send a message to the awareness server
        if (recipient != null) {
            final IAwarenessUser user = (IAwarenessUser) recipient;
            // awarenessService.sendMessage(user.getUsername(), message);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        awarenessService.sendMessage(user, message);
                    } catch (AwarenessServiceException e) {
                        logger.error("ChatController: sendMessage: AwarenessServiceException: " + e);
                    }
                }
            });
        }
    }

    @Override
    public void addBuddy(final AwarenessUser buddy) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                buddyList.addElement(buddy);
            }
        });
    }

    @Override
    public void removeBuddy(final AwarenessUser buddy) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                buddyList.removeElement(buddy);
            }
        });
    }

    public void registerChatArea(final JTextArea chatArea) {

        awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {

            @Override
            public void handleAwarenessMessageEvent(final IAwarenessEvent awarenessEvent) {

                // System.out.println("calling message event");

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {

                        String oldText = chatArea.getText();
                        List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();

                        if (awarenessEvent.getMessage() != null) {
                            chatArea.setText(oldText + awarenessEvent.getUser().getNickName() + ": " + awarenessEvent.getMessage() + "\n");
                            logger.debug("message sent from: " + awarenessEvent.getUser().getNickName() + " message: " + awarenessEvent.getMessage());
                        }

                        //users.add(awarenessEvent.getUser());
                        //awarenessService.updatePresenceTool(users);
                    }
                });

            }
        });

        awarenessService.addAwarenessRosterListener(new IAwarenessRosterListener() {

            @Override
            public void handleAwarenessRosterEvent(IAwarenessRosterEvent e) {
                //do stuff for buddy going offline online away etc...
            }
        });


    }

    public void connectToRoom() {
        //for MUC only
    }

    public void setAwarenessService(IAwarenessService awarenessService) {
        this.awarenessService = awarenessService;
    }

    public IAwarenessService getAwarenessService() {
        return awarenessService;
    }

    public void setELOUri(String eLOUri) {
        //for MUC
    }

    public String getELOUri() {
        //for MUC
        return null;
    }

    public DefaultListModel getBuddyListModel() {
        return populateBuddyList();
    }

    @Override
    public void sendMessage(String ELOUri, String message) {
        //for MUC
    }

    public void setBuddyListModel(DefaultListModel buddyListModel) {
        this.buddyList = buddyListModel;
    }

    @Override
    public void registerTextField(JTextField sendMessageTextField) {
        // TODO Auto-generated method stub
    }

    @Override
    public void registerChat(IChat chat) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
