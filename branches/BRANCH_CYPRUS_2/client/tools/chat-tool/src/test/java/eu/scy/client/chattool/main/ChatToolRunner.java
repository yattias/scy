package eu.scy.client.chattool.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import eu.scy.client.tools.chattool.ChatPresencePanel;
import org.apache.log4j.Logger;

import eu.scy.awareness.IAwarenessService;
import eu.scy.chat.controller.ChatController;
import eu.scy.chat.controller.OOOChatController;
import eu.scy.chat.controller.MUCChatController;
import eu.scy.client.tools.chattool.ChatPanel;
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
		
		ToolBrokerImpl tbi;
		
		String[] elos = { "qwertyuioplkjhgfd","roolomemory5800new_Empty_concept_map_1scymapping", "roolomemory00hypothesistext" };
		String[] users = { "hulk", "tony" };
		
		
		for(int i = 0; i<users.length; i++) {
			
			//choose which server the tbi should be created against
			//tbi = new ToolBrokerImpl(users[i]+"@scy.collide.info", users[i]);
			tbi = new ToolBrokerImpl(users[i]+"@83.168.205.138", users[i]);
			final IAwarenessService aService = tbi.getAwarenessService();
			
			final JFrame frame = new JFrame("Selecting JList");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			frame.getContentPane().setLayout(new GridLayout(1,2));			
			
			JPanel jp = new JPanel();
			JTextField jtf = new JTextField();
			jtf.setEnabled(false);
			jtf.setText(users[i]+"@scy.collide.info");
			jp.add(jtf);
			final JComboBox c = new JComboBox(elos);
			jp.add(c);
			final JButton jb = new JButton("Connect");
			jp.add(jb);
			jb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					jb.setEnabled(false);
					c.setEnabled(false);
					
//		Random r = new Random();
//		String eloUri = Long.toString(Math.abs(r.nextLong()), 36);
//	
					//String eloUri = "";
					//roolomemory20Reporttext
					String eloUri = c.getSelectedItem().toString();
					ChatController mucChatController = new MUCChatController(aService, eloUri);
					
					
					ChatPanel cmp = new ChatPanel(mucChatController);
					
					
					ChatPresencePanel cpm = new ChatPresencePanel(mucChatController);
					
					frame.getContentPane().add(cpm);
					frame.getContentPane().add(cmp);
					frame.pack();
				}
			});
			
			frame.getContentPane().add(jp);
			//frame.setSize(600, 450);
			frame.pack();
			frame.setVisible(true);			
		}
		
	}

}
