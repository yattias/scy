package eu.scy.client.tools.chattool;

import java.awt.Color;
import java.awt.Dimension;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTitledPanel;

import eu.scy.awareness.AwarenessUser;
import eu.scy.chat.controller.ChatController;
import eu.scy.presence.IPresenceEvent;


/**
 * @author jeremyt
 */
public class ChatPresencePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ChatPresencePanel.class.getName());

    protected JList buddyList;
    static ChatPresencePanel cmp;
    JXTitledPanel chatAreaPanel;
    private ChatController chatController;
    private DefaultListModel model = new DefaultListModel();
    private JScrollPane buddyListScroll;


    public ChatPresencePanel(ChatController mucChatController) {
        this.chatController = mucChatController;
        initGUI();
    }

    protected void initGUI() {
        this.setLayout(new MigLayout("insets 0 0 0 0, wrap 1"));
        this.setBackground(Color.white);
        this.add(createBuddyListPanel(), "align center,grow");
    }

    protected JPanel createBuddyListPanel() {
        JPanel buddyPanel = new JPanel();
        buddyPanel.setBackground(Color.white);
        buddyList = new JList(chatController.getBuddyListModel());
        buddyList.setBorder(BorderFactory.createEmptyBorder());
        buddyList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        buddyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buddyList.setVisibleRowCount(1);
        buddyList.setCellRenderer(new BuddyListRenderer());
        buddyListScroll = new JScrollPane(buddyList);
        buddyListScroll.setBorder(BorderFactory.createEmptyBorder());
        buddyListScroll.setPreferredSize(new Dimension(200, 75));

        buddyPanel.add(buddyListScroll);
        
        return buddyPanel;
    }

    public void resizeChat(int newWidth, int newHeight) {
        this.buddyListScroll.setPreferredSize(new Dimension(newWidth, newHeight));
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }


    public ChatController getChatController() {
        return chatController;
    }
    
    public void addTemporaryUser(String user) {
    	AwarenessUser au = new AwarenessUser();
    	au.setJid(user);
    	au.setNickName(user);
    	au.setPresence(IPresenceEvent.WAITING);
    	logger.debug("ChatPresencePanel: addTemporaryUser jid...: " + au.getJid());
    	this.chatController.addBuddy(au);
    }

    public void removeTemporaryUser(String user) {
    	StringTokenizer st = new StringTokenizer(user, "@");
    	String userName = st.nextToken();
    	AwarenessUser au = new AwarenessUser();
    	au.setJid(userName);
    	logger.debug("ChatPresencePanel: removeTemporaryUser jid...: " + au.getJid());
    	this.chatController.removeBuddy(au);
    }
}
