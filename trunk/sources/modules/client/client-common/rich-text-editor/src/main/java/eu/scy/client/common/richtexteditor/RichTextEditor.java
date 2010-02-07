package eu.scy.client.common.richtexteditor;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.Document;
import org.apache.log4j.Logger;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.awt.print.Printable;
import java.awt.Graphics2D;
import javax.swing.RepaintManager;
import java.util.ResourceBundle;
import java.util.Date;
import javax.swing.JOptionPane;
import eu.scy.actionlogging.api.IActionLogger;

public class RichTextEditor extends JPanel implements DocumentListener, Printable {
    private static final Logger logger = Logger.getLogger("eu.scy.client.common.richtexteditor.RichTextEditor");
    private ResourceBundle messages = ResourceBundle.getBundle("eu.scy.client.common.richtexteditor.RichTextEditor");
	private JTextPane textPane;
	private RTFEditorKit rtfEditor;
	private RtfFileToolbar fileToolbar;
	private RtfFormatToolbar formatToolbar;
    private RichTextEditorLogger rtfLogger;

	public RichTextEditor() {
		super();
		initUI();
	}

    public RichTextEditorLogger getRichTextEditorLogger() {
        return rtfLogger;
    }

    public void setRichTextEditorLogger(IActionLogger actionLogger,
        String username, String toolname, String missionname,
        String sessionname, String parent) {
        rtfLogger = new RichTextEditorLogger(actionLogger, username,
            toolname, missionname, sessionname, parent, this);
    }

    public JTextPane getJTextPane() {
        return textPane;
    }

    private void showError(String messageID, Throwable e) {
        logger. error(messages.getString(messageID), e);
        System.err.println(messages.getString(messageID) + " " + new Date());
        e.printStackTrace(System.err);
        JOptionPane.showMessageDialog(textPane, messages.getString(messageID) +
            ":\nMessage: " + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
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
		panel.add(formatToolbar, BorderLayout.CENTER);
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
            showError("setTextError", ex);
        } catch (BadLocationException ex) {
            showError("setTextError", ex);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                showError("setTextError", ex);
            }
        }
    }

    public String getRtfText() {
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            rtfEditor.write(str, textPane.getDocument(), 0, textPane.getDocument().getLength());
            return str.toString("UTF-8");
        } catch (IOException ex) {
            showError("getTextError", ex);
        } catch (BadLocationException ex) {
            showError("getTextError", ex);
        }
        return "";
    }

    public String getPlainText() {
        try {
            Document doc = (Document)textPane.getDocument();
            return doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {
            showError("getTextError", ex);
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
        logger.info(changeLength + " character" +
            ((changeLength == 1) ? " " : "s ") +
            action + " " + doc.getProperty("name") + "." + newline +
            "  Text length = " + doc.getLength() + newline);
	}

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
          return(NO_SUCH_PAGE);
        } else {
          Graphics2D g2d = (Graphics2D)g;
          g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
          RepaintManager currentManager = RepaintManager.currentManager(textPane);
          boolean isDoubleBufferingEnabled = currentManager.isDoubleBufferingEnabled();
          currentManager.setDoubleBufferingEnabled(false);
          textPane.paint(g2d);
          currentManager.setDoubleBufferingEnabled(isDoubleBufferingEnabled);
          return(PAGE_EXISTS);
        }
    }
}
