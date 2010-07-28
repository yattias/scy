package eu.scy.client.tools.chattool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LinearGradientPaint;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import eu.scy.chat.controller.ChatController;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 * @author jeremyt
 * 
 */
public class ChatPanel extends JXTitledPanel {

	private static final long serialVersionUID = 1L;
	//private static final Logger logger = Logger.getLogger(ChatPanel.class.getName());

	private JTextArea chatArea = new JTextArea();
	private JTextField sendMessageTextField = new JTextField();
	private ChatController chatControler;
	private JScrollPane chatAreaScroll;
	private JPanel p;
	private int setWidth = 250;

	public ChatPanel(ChatController mucChatController) {
		this.chatControler = mucChatController;
		initGUI();
		this.chatControler.registerChatArea(chatArea);
		this.chatControler.registerTextField(sendMessageTextField);
		this.chatControler.connectToRoom();
		this.setTitle(this.chatControler.getCurrentUser());
		//this.setTitleForeground(Colors.White.color());
		this.setTitlePainter(getTitlePainter());
	}

    @Override
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
		p = new JPanel(new BorderLayout(1, 1));
		p.setBackground(Color.white);
		this.add(p);
		
		Font font = new Font("Arial", Font.PLAIN, 10);
		chatArea.setFont(font);
		
		chatArea.setEditable(false);
                chatArea.setLineWrap(true);
                chatArea.setWrapStyleWord(true);
		chatAreaScroll = new JScrollPane(chatArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//                p.add(chatArea,BorderLayout.CENTER);
		p.add(chatAreaScroll,BorderLayout.CENTER);
		chatAreaScroll.setPreferredSize(new Dimension(setWidth, 250));
//		chatArea.setPreferredSize(new Dimension(setWidth, 250));
		
		sendMessageTextField.setEditable(true);
		sendMessageTextField.setEnabled(true);
		
		p.add(sendMessageTextField, BorderLayout.SOUTH);
	}
	
	public void resizeChat(int newWidth, int newHeight) {
		this.chatAreaScroll.setPreferredSize(new Dimension(newWidth, newHeight));
//            System.out.println("Size: " + getSize());
//		this.chatArea.setPreferredSize(new Dimension(newWidth, newHeight));
//            System.out.println(chatArea.getSize() + "\n");
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
