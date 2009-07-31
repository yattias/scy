package eu.scy.colemo.client.figures;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 02.apr.2009
 * Time: 12:19:28
 * To change this template use File | Settings | File Templates.
 */
public class LabelArrow extends Arrow implements FocusListener {

	public static final String DEFAULT_LABEL = "Unnamed";
	private JTextField textField = new JTextField(DEFAULT_LABEL);

	public LabelArrow() {
		setOpaque(false);
		setBackground(Color.cyan);
		setLayout(null);
		setFocusable(true);
		addFocusListener(this);
		TextFieldListener textFieldListener = new TextFieldListener(this);
		textField.addFocusListener(textFieldListener);
		textField.addKeyListener(textFieldListener);
		textField.setSize(textField.getPreferredSize());
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setOpaque(true);
		textField.setBorder(null);
		textField.setBackground(new Color(255, 255, 255, 200));
		add(textField);
	}

	public String getLabel() {
		return textField.getText().equals(DEFAULT_LABEL) ? "" : textField.getText();
	}

	protected void updateWidth() {
		Dimension prefsize = textField.getPreferredSize();

		prefsize.width += 10;

		setMinWidth(prefsize.width);
		setMinHeight(prefsize.height);

		textField.setBounds(getWidth() / 2 - (prefsize.width / 2), getHeight() / 2 - (prefsize.height / 2), prefsize.width, prefsize.height);
		repaint();
	}

	public void focusGained(FocusEvent e) {
		setColor(Color.blue);
		repaint();
	}

	public void focusLost(FocusEvent e) {
		setColor(Color.black);
		repaint();
	}

	private static class TextFieldListener implements FocusListener, KeyListener {
		private LabelArrow arrow;

		TextFieldListener(LabelArrow arrow) {
			this.arrow = arrow;
		}

		public void focusGained(FocusEvent e) {
			JTextField textField = (JTextField) e.getSource();
			if (textField.getText().equals(DEFAULT_LABEL)) textField.setText("");
			textField.setBorder(BorderFactory.createEtchedBorder());
		}

		public void focusLost(FocusEvent e) {
			JTextField textField = (JTextField) e.getSource();
			if (textField.getText().equals("")) textField.setText(DEFAULT_LABEL);
			textField.setBorder(null);
			arrow.updateWidth();
		}

		public void keyTyped(KeyEvent e) {
			arrow.updateWidth();
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10) arrow.requestFocus();
		}

		public void keyReleased(KeyEvent e) {

		}
	}
}
