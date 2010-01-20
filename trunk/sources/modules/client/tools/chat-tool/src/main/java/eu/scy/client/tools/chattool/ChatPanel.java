package eu.scy.client.tools.chattool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.tool.IChatPresenceToolEvent;
import eu.scy.awareness.tool.IChatPresenceToolListener;
import eu.scy.chat.controller.ChatController;
import eu.scy.chat.controller.MUCChatController;

/**
 * @author jeremyt
 * 
 */
public class ChatPanel extends JXTitledPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ChatPanel.class
			.getName());

	private JTextArea chatArea;
	private JXTitledPanel chatAreaPanel;
	protected JTextField sendMessageTextField;
	private ChatController chatControler;
	private String ELOUri;

	public ChatPanel(ChatController mucChatController) {
		this.chatControler = mucChatController;
		initGUI();
		this.chatControler.registerChatArea(chatArea);
		this.chatControler.connectToRoom();
		this.setTitle(this.chatControler.getCurrentUser());
		//this.setTitleForeground(Colors.White.color());
		this.setTitlePainter(getTitlePainter());

	}

	public Painter getTitlePainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.5f);
		Color color2 = Colors.Black.color(0.5f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1.0f }, new Color[] {
				color1, color2});
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}
	protected void initGUI() {
		JXPanel p = new JXPanel(new MigLayout("insets 1 1 1 1,wrap 1"));
		p.setBackground(Color.white);
		this.add(p);
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		p.add(chatArea);
		JScrollPane chatAreaScroll = new JScrollPane(chatArea);

		p.add(chatAreaScroll);
		chatAreaScroll.setPreferredSize(new Dimension(350, 250));
		sendMessageTextField = new JTextField();
		sendMessageTextField.setEditable(true);
		sendMessageTextField.setEnabled(true);
		sendMessageTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String oldText = getChatArea().getText();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						getChatController().sendMessage( chatControler.getELOUri(),sendMessageTextField.getText());
						sendMessageTextField.setText("");
					}
				});
			}
		});

		p.add(sendMessageTextField, "align left, grow");
	}

	public void setChatArea(JTextArea chatArea) {
		this.chatArea = chatArea;
	}

	public JTextArea getChatArea() {
		return chatArea;
	}

	public void setChatController(ChatController chatController) {
		this.chatControler = chatController;
	}

	public ChatController getChatController() {
		return chatControler;
	}
}
