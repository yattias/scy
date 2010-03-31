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
import javax.swing.text.html.HTMLEditorKit;
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
import java.net.URL;

/**
* Rich text editor component for SCY project.
*/
public class RichTextEditor extends JPanel implements DocumentListener, Printable {
    private static final Logger logger = Logger.getLogger("eu.scy.client.common.richtexteditor.RichTextEditor");
    private ResourceBundle messages = ResourceBundle.getBundle("eu.scy.client.common.richtexteditor.RichTextEditor");
	private JTextPane textPane;
	private RTFEditorKit rtfEditor;
    private HTMLEditorKit htmlEditor;
    private RtfFileToolbar fileToolbar;
	private RtfFormatToolbar formatToolbar;
    private RichTextEditorLogger rtfLogger = null;
    private boolean html;

    /**
    * Creates rich text editor component for rtf format.
    */
    public RichTextEditor() {
        super();
        html = false;
        initUI();
    }

    /**
    * Creates rich text editor component for rtf or html format.
    * @param html if true then then generates html format editor else generates rtf format editor
    */
    public RichTextEditor(boolean html) {
        super();
        this.html = html;
        initUI();
    }

    /**
    * Is editor for html format
    * @return true if for html format, false if for rtf format
    */
    public boolean isHTML() {
        return html;
    }

    /**
     * Sets logger.
     * @param actionLogger actionlogger, usually use actionlogger from injected service variable in your tool
     * @param username username
     * @param toolname toolname
     * @param missionname missionname
     * @param sessionname sessionname
     * @param parent if you use several rich text editor components in your tool you may want identify, which component is logging current action, you can use parent for that purpose, can be left blank if only one rich text editor component in your tool or no need to identify which particular component logs current action
     */
    public void setRichTextEditorLogger(IActionLogger actionLogger,
        String username, String toolname, String missionname,
        String sessionname, String parent) {
        rtfLogger = new RichTextEditorLogger(actionLogger, username,
            toolname, missionname, sessionname, parent, this);
    }

    /**
     * Sets eloUri of actionlogger.
     * @param eloUri eloUri
     */
    public void setEloUri(String eloUri) {
        if (rtfLogger != null) {
            rtfLogger.setEloUri(eloUri);
        }
    }

    /**
     * Gets logger.
     * @return logger assigned to this component
     */
    public RichTextEditorLogger getRichTextEditorLogger() {
        return rtfLogger;
    }

    /**
     * @return component used for text showing and input
     */
    public JTextPane getJTextPane() {
        return textPane;
    }

    /**
     * Sets text in the rtf or html format.
     * @param text if isHTML()==true then html string in UTF-8 format, else rtf string (which is naturally in ASCII format)
     */
    public void setText(String text) {
        ByteArrayInputStream bis = null;
        try {
            textPane.setText("");
            StringBuffer stringBuffer = new StringBuffer(text);
            bis = new ByteArrayInputStream(stringBuffer.toString().getBytes("UTF-8"));
            if (html) {
                htmlEditor.read(bis, textPane.getDocument(), 0);
            } else {
                rtfEditor.read(bis, textPane.getDocument(), 0);
            }
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

    /**
     * Sets text inside editor. No formatting of new text.
     * @param text new text in plain text format
     */
    public void setPlainText(String text) {
        try {
            textPane.setText("");
            textPane.getDocument().insertString(0, text, null);
        } catch (BadLocationException ex) {
            showError("setTextError", ex);
        }
    }

    /**
     * Loads HTML document into component. Use only if isHTML()==true
     * @param url URL of the document to load
     */
    public void setTextFromUrl(URL url) {
        textPane.getDocument().putProperty(Document.StreamDescriptionProperty, null);
        try {
            textPane.setPage(url);
        } catch (IOException ex) {
            showError("setTextError", ex);
        }
    }

    /**
     * Gets text from component as formatted text.
     * @return if isHTML()==true then gets HTML source else gets RTF string
     */
    public String getRtfText() {
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            if (html) {
                htmlEditor.write(str, textPane.getDocument(), 0, textPane.getDocument().getLength());
            } else {
                rtfEditor.write(str, textPane.getDocument(), 0, textPane.getDocument().getLength());
            }
            return str.toString("UTF-8");
        } catch (IOException ex) {
            showError("getTextError", ex);
        } catch (BadLocationException ex) {
            showError("getTextError", ex);
        }
        return "";
    }

    /**
     * Gets plain text.
     * @return plain text without formatting
     */
    public String getPlainText() {
        try {
            Document doc = (Document)textPane.getDocument();
            return doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {
            showError("getTextError", ex);
        }
        return "";
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
        htmlEditor = new HTMLEditorKit();
		textPane = new JTextPane();
        if (html) {
    		textPane.setEditorKit(htmlEditor);
            textPane.setContentType("text/html;charset=utf-8");
        } else {
    		textPane.setEditorKit(rtfEditor);
            textPane.setContentType("text/rtf;charset=utf-8");
        }
		textPane.getDocument().addDocumentListener(this);
        this.add(new JScrollPane(textPane), BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.fileToolbar = new RtfFileToolbar(this);
		panel.add(fileToolbar, BorderLayout.WEST);
        if (!html) {
    		this.formatToolbar = new RtfFormatToolbar(this);
        	panel.add(formatToolbar, BorderLayout.CENTER);
        }
		this.add(panel, BorderLayout.NORTH);
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
/*
        Document doc = (Document)e.getDocument();
        int changeLength = e.getLength();
        String newline = "\n";
        logger.info(changeLength + " character" +
            ((changeLength == 1) ? " " : "s ") +
            action + " " + doc.getProperty("name") + "." + newline +
            "  Text length = " + doc.getLength() + newline);
*/
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
