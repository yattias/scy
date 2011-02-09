package edu.scy.tools.math.test.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class PopupExample extends JPanel {

	private JEditorPane fTextComponent;

	public PopupExample() {
		build();
	}

	protected void build() {
		setLayout(new BorderLayout());
		fTextComponent = new JEditorPane();
		fTextComponent.setContentType("text/html");
		fTextComponent.setEditable(false);
//		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void setText(String text) {
		fTextComponent.setText(text);
		fTextComponent.selectAll();
	}

	/**
	 * Demonstrates the TextCopy component.
	 * 
	 * @param args
	 *            the text content to display in the demo. If none is specified,
	 *            a sample of HTML will be used.
	 */
	public static void main(String[] args) {
	final String text;
	if (args.length == 0){
		text = "<html>\n\t<body>Content</body>\n</html>";
	}else{
	text = args[0];
	}
	final JFrame f = new JFrame("TextCopy demo");
	JButton b = new JButton("Show Dialog");
	b.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e) {
		PopupExample tc = new PopupExample();
		tc.setText(text);
	JOptionPane.showMessageDialog(
	f, tc, "Copy This", JOptionPane.PLAIN_MESSAGE);
	}
	});
	f.add(b);
	f.pack();
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setVisible(true);
	}
}
