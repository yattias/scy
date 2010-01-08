package eu.scy.client.tools.chattool;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTitledPanel;

import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.tool.IChatPresenceToolEvent;
import eu.scy.awareness.tool.IChatPresenceToolListener;
import eu.scy.chat.controller.ChatController;
import eu.scy.toolbroker.ToolBrokerImpl;


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
	protected DefaultListModel buddlyListModel;
	protected ChatController chatController;
	private DefaultListModel model = new DefaultListModel();

	
	public ChatPresencePanel(ChatController chatController) {
		this.chatController = chatController;
		initGUI();
		updateModel();
	}
	
	
	protected void initGUI() {
		this.add(createBuddyListPanel(), BorderLayout.WEST);
		initListeners();
	}

	
	protected JPanel createBuddyListPanel() {
		JPanel buddyPanel = new JPanel(new MigLayout("wrap 1"));

		buddyList = new JList(model);
		buddyList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		buddyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		buddyList.setVisibleRowCount(1);
		buddyList.setCellRenderer(new BuddyListRenderer());
		//updateModel();

		JScrollPane buddyListScroll = new JScrollPane(buddyList);
		//buddyListScroll.setPreferredSize(new Dimension(400, 100));

		buddyPanel.add(buddyListScroll);
		return buddyPanel;
	}

	protected void initListeners() {
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				logger.debug("initListeners: First index: " + listSelectionEvent.getFirstIndex());
				logger.debug("initListeners: Last index: " + listSelectionEvent.getLastIndex());
				boolean adjust = listSelectionEvent.getValueIsAdjusting();
				logger.debug("initListeners: Adjusting? " + adjust);
				
				if (!adjust) {
					JList list = (JList) listSelectionEvent.getSource();
					int selections[] = list.getSelectedIndices();
					Object selectionValues[] = list.getSelectedValues();
					
					List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();
					IAwarenessUser iau = null;
					for (int i = 0, n = selections.length; i < n; i++) {
						if(String.valueOf(selectionValues[i]).contains("available")) {
							iau = (IAwarenessUser) model.elementAt(selections[i]);
							users.add(iau);
							
						}// if
					}// for
					
					chatController.getAwarenessService().updateChatTool(users);
					
				}
			}
		};
		buddyList.addListSelectionListener(listSelectionListener);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Object o = theList.getModel().getElementAt(index);
						logger.debug("initListeners: mouseClicked: Double-clicked on: " + o.toString());
					}
				}
			}
		};
		buddyList.addMouseListener(mouseListener);
		
		chatController.getAwarenessService().addAwarenessPresenceListener(new IAwarenessPresenceListener() {		
			@Override
			public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
				logger.debug("registerChatArea: handleAwarenessPresenceEvent: smthg happened...: " + e.getUser().getNickName()+" : "+e.getUser().getPresence());				
				
				IAwarenessUser iau;
				for(int i=0; i<model.getSize(); i++) {
					iau = (IAwarenessUser) model.elementAt(i);
					logger.debug("registerChatArea: handleAwarenessPresenceEvent: " + iau.getNickName());	
					if(iau.getNickName().equals(e.getUser().getNickName())) {
						((IAwarenessUser) model.elementAt(i)).setPresence(e.getUser().getPresence());
					}
				}
				buddyList.repaint();
			}
		});
		
		//not needed right now
//		chatController.getAwarenessService().addChatToolListener(new IChatPresenceToolListener() {		
//			@Override
//			public void handleChatPresenceToolEvent(IChatPresenceToolEvent event) {
//				logger.debug("ChatPresencePanelMain: registerChatArea: handleChatPresenceToolEvent: " + event);
//				logger.debug("ChatPresencePanelMain: registerChatArea: handleChatPresenceToolEvent: " + event.getUsers().size());
//				AwarenessUser au = (AwarenessUser) event.getUsers().get(0);
//				for(int i = 0; i<model.getSize(); i++) {
//					String mau = ((AwarenessUser) model.get(i)).getNickName();
//					logger.debug("ChatPresencePanelMain: registerChatArea: handleChatPresenceToolEvent: " + au.getNickName());
//					logger.debug("ChatPresencePanelMain: registerChatArea: handleChatPresenceToolEvent: " + mau);
//					if(mau.equals(au.getNickName())) {
//						buddyList.setSelectedIndex(i);
//					}
//				}
//			}
//		});
		
	}


	protected void updateModel() {
		model.removeAllElements();
		IAwarenessUser iau;
		
		for(int i=0; i<chatController.populateBuddyList().getSize(); i++) {
			iau = (IAwarenessUser) chatController.getBuddyListArray().elementAt(i);
			model.addElement(iau);
		}
	}
	
}
