package edu.scy.client.tools.presencetool.main;

import javax.swing.JFrame;

import eu.scy.awareness.IAwarenessService;
import eu.scy.client.tools.chattool.ChatPresencePanel;
import eu.scy.toolbroker.ToolBrokerImpl;

public class PresencePanelRunner {
	

	public static void main(String[] args) {
		JFrame frame = new JFrame("Selecting JList");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ToolBrokerImpl tbi = new ToolBrokerImpl("senders11@scy.intermedia.uio.no", "senders11");
		IAwarenessService aService = tbi.getAwarenessService();		
		
		ChatPresencePanel cmp = new ChatPresencePanel(null);
		frame.getContentPane().add(cmp);
		frame.setSize(200, 170);
		frame.setVisible(true);
	}

}
