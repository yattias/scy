package eu.scy.agents.conceptmap.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class TextHighlightingPanel extends JPanel {

    private JTextPane textPane;
    private JScrollPane scrollPane;
    private String emptyText;

    public TextHighlightingPanel(int rows) {
        super(new GridLayout(1, 1));
        StyleContext context = new StyleContext();
        textPane = new JTextPane(new DefaultStyledDocument(context));
        scrollPane= new JScrollPane(textPane);
        emptyText= "";
        for (int i = 0; i < rows; i++) {
            emptyText += "\n";
        }
        textPane.setText(emptyText);
        add(scrollPane);
    }

    public synchronized void highlight(String string, Color color) {
        Document doc = textPane.getDocument();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setBackground(attributes, color);

        String delimiter = " ;:,.()?!";
        for (int i = 1; i < doc.getLength() - string.length(); i++) {
            try {
                String beforeText = doc.getText(i-1, 1);
                String afterText = doc.getText(i + string.length(), 1);
                if (doc.getText(i, string.length()).equalsIgnoreCase(string) && delimiter.contains(beforeText) && delimiter.contains(afterText)) {
                    String s = doc.getText(i, string.length());
                    doc.remove(i, string.length());
                    doc.insertString(i, s, attributes);
                    i += string.length();
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void setText(String string) {
        textPane.setText(string);
    }

    public void clearText() {
        textPane.setText(emptyText);
    }

    public String getText() {
        return textPane.getText();
    }

}
