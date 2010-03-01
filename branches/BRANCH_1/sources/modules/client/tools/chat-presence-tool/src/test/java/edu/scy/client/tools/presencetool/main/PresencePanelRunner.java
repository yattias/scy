package edu.scy.client.tools.presencetool.main;

import javax.swing.JFrame;

import eu.scy.awareness.IAwarenessService;
import eu.scy.chat.controller.ChatController;
import eu.scy.chat.controller.MUCChatController;
import eu.scy.client.tools.chattool.ChatPresencePanel;
import eu.scy.toolbroker.ToolBrokerImpl;

public class PresencePanelRunner {
	

	public static void main(String[] args) {
		JFrame frame = new JFrame("Selecting JList");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ToolBrokerImpl tbi = new ToolBrokerImpl("jeremy@scy.collide.info", "jeremy");
		IAwarenessService aService = tbi.getAwarenessService();		
		
		String eloUri = "roolomemory20Reporttext";
		ChatController mucChatController = new MUCChatController(aService, eloUri);
		
		ChatPresencePanel cmp = new ChatPresencePanel(mucChatController);
		frame.getContentPane().add(cmp);
		frame.setSize(200, 170);
		frame.setVisible(true);
	}

}
