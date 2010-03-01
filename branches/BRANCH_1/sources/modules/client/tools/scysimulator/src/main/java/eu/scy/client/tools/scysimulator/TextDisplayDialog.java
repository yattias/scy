package eu.scy.client.tools.scysimulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextDisplayDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7809983127880229969L;

	public TextDisplayDialog(Frame owner, String text, String title) {
		super(owner, true); // modal
		this.setTitle(title);
		this.setPreferredSize(new Dimension(400,600));
		this.setLayout(new BorderLayout());
		JTextArea textArea = new JTextArea(80,80);
		textArea.setEditable(true);
		textArea.setText(text);
		JScrollPane scroller = new JScrollPane(textArea);
		this.add(scroller, BorderLayout.CENTER);		
				
		JButton button = new JButton("OKAY");
		button.addActionListener(this);
		button.setActionCommand("okay");
		this.add(button, BorderLayout.SOUTH);
		
		this.setSize(400,600);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("okay")) {
			this.setVisible(false);
			this.dispose();
		}
	}
	
}
