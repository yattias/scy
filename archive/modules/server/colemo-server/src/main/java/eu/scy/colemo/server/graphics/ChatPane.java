/*
 * Created on 11.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * @author Arild Sandven
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChatPane extends JScrollPane{

	private JTextPane infoArea;
	private DefaultStyledDocument logDoc;
	private static String NEW_LINE = "\n";
	private Style chatStyle, serverStyle, agentStyle, voteStyle;
	
	public ChatPane() {
		infoArea = new JTextPane();
		infoArea.setEditable(false);
		infoArea.setBackground(Color.WHITE);
		infoArea.setBorder(BorderFactory.createTitledBorder("Chat window"));
		infoArea.setMaximumSize(new Dimension(1000,50));
		StyleContext sContext = StyleContext.getDefaultStyleContext();
		Style defaultStyle = sContext.getStyle(StyleContext.DEFAULT_STYLE);
		
		chatStyle = sContext.addStyle("chat",defaultStyle);
		serverStyle = sContext.addStyle("server",defaultStyle);
		agentStyle = sContext.addStyle("agent",defaultStyle);
		voteStyle = sContext.addStyle("vote",defaultStyle);
		
		//CHAT style
		StyleConstants.setFontSize(chatStyle,10);
		StyleConstants.setForeground(chatStyle,Color.BLACK);
		
		//SERVER style
		StyleConstants.setFontSize(serverStyle,10);
		StyleConstants.setForeground(serverStyle,Color.BLUE);
		
		//AGENT style
		StyleConstants.setFontSize(agentStyle,10);
		StyleConstants.setForeground(agentStyle,Color.RED);
		
		//VOTE style
		StyleConstants.setFontSize(voteStyle,10);
		StyleConstants.setForeground(voteStyle,Color.DARK_GRAY);
		
		logDoc = new DefaultStyledDocument(sContext);
		infoArea.setDocument(logDoc);
		
		this.setViewportView(infoArea);
	}
	
	public void addChatText(String s) {
		try {
			logDoc.insertString(logDoc.getLength(),s,chatStyle);
			infoArea.setCaretPosition(logDoc.getLength());
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public void addServerText(String s) {
		try {
			logDoc.insertString(logDoc.getLength(),s,serverStyle);
			infoArea.setCaretPosition(logDoc.getLength());
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public void addAgentText(String s) {
		try {
			logDoc.insertString(logDoc.getLength(),s,agentStyle);
			infoArea.setCaretPosition(logDoc.getLength());
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public void addVoteText(String s) {
		try {
			logDoc.insertString(logDoc.getLength(),s,voteStyle);
			infoArea.setCaretPosition(logDoc.getLength());
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * @return Returns the logDoc.
	 */
	public DefaultStyledDocument getLogDoc() {
		return logDoc;
	}
	/**
	 * @return Returns the chatStyle.
	 */
	public Style getChatStyle() {
		return chatStyle;
	}
}
