package eu.scy.client.common.richtexteditor;

import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.Document;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

public class RichTextEditor extends JPanel implements DocumentListener {
	private static final Logger logger = Logger.getLogger(RichTextEditor.class.getSimpleName());
	private JTextPane textPane;
	private RTFEditorKit rtfEditor;
	private RtfFileToolbar fileToolbar;
	private RtfFormatToolbar formatToolbar;

	public RichTextEditor() {
		super();
		initUI();
	}

	private void initUI() {
		this.setLayout(new BorderLayout());
		rtfEditor = new RTFEditorKit();
		textPane = new JTextPane();
		textPane.setEditorKit(rtfEditor);
		textPane.getDocument().addDocumentListener(this);
		this.add(new JScrollPane(textPane), BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.fileToolbar = new RtfFileToolbar(this);
		panel.add(fileToolbar, BorderLayout.WEST);
		this.formatToolbar = new RtfFormatToolbar(this);
		panel.add(formatToolbar, BorderLayout.EAST);
		this.add(panel, BorderLayout.NORTH);
	}

    public void setText(String text) {
        ByteArrayInputStream bis = null;
        try {
            textPane.setText("");
            StringBuffer stringBuffer = new StringBuffer(text);
            bis = new ByteArrayInputStream(stringBuffer.toString().getBytes("UTF-8"));
            rtfEditor.read(bis, textPane.getDocument(), 0);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getText() {
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            rtfEditor.write(str, textPane.getDocument(), 0, textPane.getDocument().getLength());
            return str.toString("UTF-8");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "";
    }

	public void insertUpdate(DocumentEvent e) {
		updateLog(e, "inserted into");
	}

	public void removeUpdate(DocumentEvent e) {
		updateLog(e, "removed from");
	}

	public void changedUpdate(DocumentEvent e) {
		updateLog(e, "changed");
	}

	public void updateLog(DocumentEvent e, String action) {
        Document doc = (Document)e.getDocument();
        int changeLength = e.getLength();
        String newline = "\n";
        logger.log(Level.INFO,
            changeLength + " character" +
            ((changeLength == 1) ? " " : "s ") +
            action + " " + doc.getProperty("name") + "." + newline +
            "  Text length = " + doc.getLength() + newline);
	}
}
