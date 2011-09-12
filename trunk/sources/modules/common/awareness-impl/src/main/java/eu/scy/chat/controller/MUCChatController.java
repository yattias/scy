package eu.scy.chat.controller;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;

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
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.presence.IPresenceEvent;
import java.net.URLEncoder;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class MUCChatController implements ChatController {

    private static final Logger logger = Logger.getLogger(MUCChatController.class.getName());
    private DefaultListModel buddyListModel = new DefaultListModel();
    private IAwarenessService awarenessService;
    private String ELOUri;
    private boolean isTempMUCRoom;

    public MUCChatController(IAwarenessService awarenessService, String ELOUri) {
        logger.debug("MUC ChatController: starting ... ");
        logger.debug("MUC ChatController: awarenessService.isConnected(): "
                + awarenessService.isConnected());
        this.ELOUri = ELOUri;
        this.awarenessService = awarenessService;
    }

    public String getCurrentUser() {
        String user = getAwarenessService().getConnection().getUser();
        if (user != null) {
            return StringUtils.parseName(user);
        }
        return "NO ONE";
    }

    public void sendMessage(final String ELOUri, final String message) {
        try {
            awarenessService.sendMUCMessage(ELOUri, message);
        } catch (AwarenessServiceException e) {
            logger.error("ChatController: sendMessageMUC: AwarenessServiceException for ELOUri:" + ELOUri + " " + e);
        }

    }

    public void sendMessage(final String message) {
        try {
            awarenessService.sendMUCMessage(ELOUri, message);
        } catch (AwarenessServiceException e) {
            logger.error("ChatController: sendMessageMUC: AwarenessServiceException for ELOUri:" + ELOUri + " " + e);
        }

    }

    public void addBuddy(final AwarenessUser user) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (getIndexOfBuddyByJID(user) > -1) {
                    logger.debug("user already present, we're not adding it");
                } else {
                    buddyListModel.addElement(user);
                }
            }
        });
    }

    protected int getIndexOfBuddyByJID(IAwarenessUser awarenessUser) {
        Enumeration<?> elements = buddyListModel.elements();
        while (elements.hasMoreElements()) {
            IAwarenessUser auser = (IAwarenessUser) elements.nextElement();

            if (auser.getNickName() != null
                    && auser.getNickName().equals(awarenessUser.getNickName())) {
                return buddyListModel.indexOf(auser);
            }
        }
        return -1;
    }

    public void removeBuddy(final AwarenessUser user) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                logger.debug("MUCChatController: removeBuddy: " + user.getJid());
                Enumeration<?> elements = buddyListModel.elements();
                while (elements.hasMoreElements()) {
                    IAwarenessUser auser = (IAwarenessUser) elements.nextElement();
                    if (auser.getJid() != null && auser.getJid().equals(user.getJid())) {
                        buddyListModel.remove(buddyListModel.indexOf(auser));
                    }
                }
                // kick that fucker away call handler
            }
        });
    }

    private String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date day = new Date();
        return formatter.format(day);
        //return DateFormat.getInstance().format(day);
    }

    @Override
    public void registerChat(final IChat chat) {
        awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {

            @Override
            public void handleAwarenessMessageEvent(
                    final IAwarenessEvent awarenessEvent) {


                logger.debug("calling message event the new chat");

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        try {
                            // System.out.println( "Checking room id" );
                            String awarenessEventRoomId = awarenessEvent.getRoomId();
                            if (awarenessEventRoomId != null && awarenessEventRoomId.contains("@")) {
                                //need to parse it text@conference.org
                                awarenessEventRoomId = StringUtils.parseName(awarenessEventRoomId);
                                logger.debug("NEW ROOMID " + awarenessEventRoomId);
                            }
                            if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(URLEncoder.encode(ELOUri, "utf-8"), awarenessEventRoomId)) {
                                logger.debug("MATCHED ELOURI " + ELOUri + " roomid " + awarenessEventRoomId);
                                if (awarenessEvent.getMessage() != null) {
                                    chat.addMessage(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(new Date(awarenessEvent.getTimestamp())), awarenessEvent.getUser().getNickName(), awarenessEvent.getMessage());
                                }
                            } else {
                                logger.debug("ELOURI MISS MATCH " + ELOUri + " roomid " + awarenessEventRoomId);
                            }
                        } catch (UnsupportedEncodingException ex) {
                            java.util.logging.Logger.getLogger(MUCChatController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            }
        });

        awarenessService.addAwarenessPresenceListener(new IAwarenessPresenceListener() {

            @Override
            public void handleAwarenessPresenceEvent(final IAwarePresenceEvent awarenessPresenceEvent) {
                logger.debug("registerChatArea: handleAwarenessPresenceEvent: smthg happened...: " + awarenessPresenceEvent.getUser().getNickName() + " : " + awarenessPresenceEvent.getUser().getPresence());

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        try {
                            String awarenessEventRoomId = awarenessPresenceEvent.getRoomId();
                            logger.debug("NEW awarenessEventRoomId ROOMID " + awarenessEventRoomId);
                            if (awarenessEventRoomId != null && awarenessEventRoomId.contains("@")) {
                                //need to parse it text@conference.org
                                awarenessEventRoomId = StringUtils.parseName(awarenessEventRoomId);
                                logger.debug("NEW awarenessEventRoomId ROOMID " + awarenessEventRoomId);
                            }
                            if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(URLEncoder.encode(ELOUri, "utf-8"), awarenessEventRoomId)) {
                                logger.debug("MATCHED awarenessEventRoomId ELOURI " + ELOUri + " roomid " + awarenessEventRoomId);
                                IAwarenessUser awarenessUser = awarenessPresenceEvent.getUser();
                                boolean isFound = false;
                                IAwarenessUser iau;
                                logger.debug("AwarenessPresenceListener elouri: " + ELOUri + " ------ roomid :" + awarenessEventRoomId + " ------ number of buddies in chat room: " + buddyListModel.getSize());
                                for (int i = 0; i < buddyListModel.getSize(); i++) {
                                    iau = (IAwarenessUser) buddyListModel.elementAt(i);
                                    logger.debug("registerChatArea: handleAwarenessPresenceEvent: awarenessEventRoomId: " + iau.getNickName());
                                    logger.debug("registerChatArea: handleAwarenessPresenceEvent: awarenessEventRoomId: " + iau);
                                    if (iau.getNickName().equals(awarenessUser.getNickName())) {
                                        ((IAwarenessUser) buddyListModel.elementAt(i)).setPresence(awarenessUser.getPresence());
                                        isFound = true;
                                    }
                                }
                            }
                        } catch (UnsupportedEncodingException ex) {
                            java.util.logging.Logger.getLogger(MUCChatController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            }
        });

        awarenessService.addAwarenessRosterListener(new IAwarenessRosterListener() {

            @Override
            public void handleAwarenessRosterEvent(
                    final IAwarenessRosterEvent awarenessRosterEvent) {
                Collection<String> addresses = awarenessRosterEvent.getAddresses();
                for (String address : addresses) {
                    final IAwarenessUser a = new AwarenessUser();
                    a.setNickName(address);
                    if (awarenessRosterEvent.getMessage().equals(IAwarenessRosterEvent.ADD)) {

                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    String awarenessEventRoomId = awarenessRosterEvent.getRoomId();
                                    logger.debug("NEW awarenessRosterEventRoomId ROOMID " + awarenessEventRoomId);
                                    if (awarenessEventRoomId != null && awarenessEventRoomId.contains("@")) {
                                        //need to parse it text@conference.org
                                        awarenessEventRoomId = StringUtils.parseName(awarenessEventRoomId);
                                        logger.debug("NEW awarenessRosterEventRoomId ROOMID " + awarenessEventRoomId);
                                    }
                                    if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(URLEncoder.encode(ELOUri, "utf-8"), awarenessEventRoomId)) {
                                        logger.debug("MATCHED awarenessRosterEventRoomId ELOURI " + ELOUri + " roomid " + awarenessEventRoomId);
                                        int indexOfBuddy = getIndexOfBuddy(a);
                                        if (indexOfBuddy > -1) {
                                            IAwarenessUser elementAt = (IAwarenessUser) buddyListModel.elementAt(indexOfBuddy);
                                            elementAt.setPresence(IPresenceEvent.AVAILABLE);
                                            buddyListModel.remove(indexOfBuddy);
                                            buddyListModel.add(indexOfBuddy, elementAt);
                                        } else {
                                            a.setPresence(IPresenceEvent.AVAILABLE);
                                            buddyListModel.addElement(a);
                                        }
                                    }
                                } catch (UnsupportedEncodingException ex) {
                                    java.util.logging.Logger.getLogger(MUCChatController.class.getName()).log(Level.SEVERE, null, ex);
                                }


                            }
                        });

                    } else if (awarenessRosterEvent.getMessage().equals(IAwarenessRosterEvent.REMOVE)) {

                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    String awarenessEventRoomId = awarenessRosterEvent.getRoomId();
                                    logger.debug("NEW awarenessRosterEventRoomId ROOMID " + awarenessEventRoomId);
                                    if (awarenessEventRoomId != null && awarenessEventRoomId.contains("@")) {
                                        //need to parse it text@conference.org
                                        awarenessEventRoomId = StringUtils.parseName(awarenessEventRoomId);
                                        logger.debug("NEW awarenessRosterEventRoomId ROOMID " + awarenessEventRoomId);
                                    }
                                    if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(URLEncoder.encode(ELOUri, "utf-8"), awarenessEventRoomId)) {
                                        logger.debug("MATCHED awarenessRosterEventRoomId ELOURI " + ELOUri + " roomid " + awarenessEventRoomId);
                                        int indexOfBuddy = getIndexOfBuddy(a);
                                        if (indexOfBuddy > -1) {
                                            IAwarenessUser elementAt = (IAwarenessUser) buddyListModel.elementAt(indexOfBuddy);
                                            elementAt.setPresence(IPresenceEvent.UNAVAILABLE);
                                            buddyListModel.remove(indexOfBuddy);
                                            buddyListModel.add(indexOfBuddy, elementAt);
                                        } else {
                                            a.setPresence(IPresenceEvent.UNAVAILABLE);
                                            buddyListModel.addElement(a);
                                        }
                                    }
                                } catch (UnsupportedEncodingException ex) {
                                    java.util.logging.Logger.getLogger(MUCChatController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }
                }

            }
        });
    }

    @Override
    public void registerTextField(JTextField sendMessageTextField) {
        // not needed anymore
    }

    protected int getIndexOfBuddy(IAwarenessUser awarenessUser) {
        Enumeration<?> elements = buddyListModel.elements();
        while (elements.hasMoreElements()) {
            IAwarenessUser auser = (IAwarenessUser) elements.nextElement();

            if (auser.getNickName() != null
                    && auser.getNickName().equals(awarenessUser.getNickName())) {
                return buddyListModel.indexOf(auser);
            }
        }
        return -1;
    }

    public void connectToRoom() {
        logger.debug("ChatController: Joining room with ELOUri: " + getELOUri());
        try {
            this.getAwarenessService().joinMUCRoom(this.ELOUri);
        } catch (AwarenessServiceException e) {
            e.printStackTrace();
        }
    }

    public void setAwarenessService(IAwarenessService awarenessService) {
        this.awarenessService = awarenessService;
    }

    public IAwarenessService getAwarenessService() {
        return awarenessService;
    }

    public void setELOUri(String eLOUri) {
        ELOUri = eLOUri;
    }

    public String getELOUri() {
        return ELOUri;
    }

    public void setBuddyListModel(DefaultListModel buddyListModel) {
        this.buddyListModel = buddyListModel;
    }

    public DefaultListModel getBuddyListModel() {
        return buddyListModel;
    }

    public void sendMessage(IAwarenessUser recipient, String message) {
        // for OOO chat
    }

    @Override
    public void registerChatArea(JTextArea chatArea) {
    }

    @Override
    public void sendInvitation(String user) {
        awarenessService.inviteUserToChat(ELOUri, user);
    }
}
