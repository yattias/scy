package eu.scy.client.chattool.main;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.Logger;

import eu.scy.awareness.IAwarenessService;
import eu.scy.chat.controller.ChatController;
import eu.scy.chat.controller.OOOChatController;
import eu.scy.chat.controller.MUCChatController;
import eu.scy.client.tools.chattool.ChatPanel;
import eu.scy.client.tools.chattool.ChatPresencePanel;
import eu.scy.toolbroker.ToolBrokerImpl;

public class ChatToolRunner {
	
public static void main(String[] args) {
		
		Logger logger = Logger.getLogger(ChatToolRunner.class.getName());

	
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (UnsupportedLookAndFeelException e) {
			logger.error("ChatPanelMain: UnsupportedLookAndFeelException: "+e);
		} catch (ClassNotFoundException e) {
			logger.error("ChatPanelMain: ClassNotFoundException: "+e);
		} catch (InstantiationException e) {
			logger.error("ChatPanelMain: InstantiationException: "+e);
		} catch (IllegalAccessException e) {
			logger.error("ChatPanelMain: IllegalAccessException: "+e);
		}      
		
		JFrame frame = new JFrame("Selecting JList");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ToolBrokerImpl tbi = new ToolBrokerImpl("djed11@scy.intermedia.uio.no", "djed11");
		IAwarenessService aService = tbi.getAwarenessService();
		
//		Random r = new Random();
//		String eloUri = Long.toString(Math.abs(r.nextLong()), 36);
//	
		String eloUri = "z168fb1jo51y";
		ChatController mucChatController = new MUCChatController(aService, eloUri);
		
		
		ChatPanel cmp = new ChatPanel(mucChatController);
		
		
		ChatPresencePanel cpm = new ChatPresencePanel(mucChatController);
		frame.getContentPane().setLayout(new GridLayout(1,2));
		frame.getContentPane().add(cpm);
		frame.getContentPane().add(cmp);
		frame.setSize(600, 450);
		frame.pack();
		frame.setVisible(true);
		
	}

}
