package eu.scy.client.tools.chattool;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTitledPanel;

import eu.scy.chat.controller.ChatController;


/**
 * @author jeremyt
 *
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
		//updateModel();
		logger.debug("ChatPresencePanel: starting ... ");
		logger.debug("ChatPresencePanel: awareness: awarenessService.isConnected(): "+ this.chatController.getAwarenessService().isConnected());
		initGUI();
		
	}
	
	
	protected void initGUI() {
		this.setLayout(new MigLayout("insets 0 0 0 0, wrap 1"));
		this.setBackground(Color.white);
		this.add(createBuddyListPanel(), "align center,grow");
	}

	protected JPanel createBuddyListPanel() {
		//JPanel buddyPanel = new JPanel(new MigLayout("insets 0 0 0 0,wrap 1"));
		JPanel buddyPanel = new JPanel();
		buddyPanel.setBackground(Color.red);
		//buddyPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		buddyList = new JList(chatController.getBuddyListModel());
		buddyList.setBorder(BorderFactory.createEmptyBorder());
		buddyList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		buddyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		buddyList.setVisibleRowCount(1);
		buddyList.setCellRenderer(new BuddyListRenderer());
		//updateModel();

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
	
}
