package eu.scy.colemo.client.figures;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.apr.2009
 * Time: 12:19:28
 * To change this template use File | Settings | File Templates.
 */
public class LabelArrow extends Arrow implements FocusListener {

	private String default_label = "";
	private JTextField textField = new JTextField(default_label);

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
		textField.setOpaque(false);
		textField.setBorder(null);
		add(textField);
	}

	public void setLabel(String l) {
		textField.setText(l);
		updateWidth();
	}

	public String getLabel() {
		return textField.getText();
	}

	private void updateWidth() {
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
			if (textField.getText().equals(arrow.default_label)) textField.setText("");
			textField.setOpaque(true);
			textField.setBorder(BorderFactory.createEtchedBorder());
		}

		public void focusLost(FocusEvent e) {
			JTextField textField = (JTextField) e.getSource();
			if (textField.getText().equals("")) textField.setText(arrow.default_label);
			textField.setOpaque(false);
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
