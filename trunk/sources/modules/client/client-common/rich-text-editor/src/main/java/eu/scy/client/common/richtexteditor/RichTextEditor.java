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
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.UUID;
/**
* Rich text editor component for SCY project.
*/
public class RichTextEditor extends JPanel implements DocumentListener, Printable, CaretListener, ISyncListener {
    public final static String TOOLNAME = "scytexteditor";
    public final static String SYNC_TOOL_INSTANCE_UUID = "sync_tool_instance_uuid";
    public final static String SYNC_TEXT = "sync_text";
    public final static String SYNC_POSITION_START = "sync_position_start";
    public final static String SYNC_LENGTH = "sync_length";
    private static final Logger logger = Logger.getLogger("eu.scy.client.common.richtexteditor.RichTextEditor");
    private ResourceBundle messages = ResourceBundle.getBundle("eu.scy.client.common.richtexteditor.RichTextEditor");
	private JTextPane textPane;
	private RTFEditorKit rtfEditor;
    private HTMLEditorKit htmlEditor;
    private RtfFileToolbar fileToolbar;
	private RtfFormatToolbar formatToolbar;
    private RichTextEditorLogger rtfLogger = null;
    private boolean html = false;
    private String oldText = "";
    private String insertedText = "";
    private boolean authorMode = false;
    private int typingLogIntervalMs = 30000;
    private Timer timer = null;
    private int typingOldPos = 0;
    private ISyncSession syncSession = null;
    private boolean syncing = false;
    private UUID uuid = UUID.randomUUID();

    /**
    * Creates rich text editor component for rtf format.
    * By default creates rtf format editor with authorMode = false
    */
    public RichTextEditor() {
        super();
        initUI();
    }

    /**
    * Creates rich text editor component for rtf or html format with no authoring mode.
    * @param html if true then then generates html format editor else generates rtf format editor
    */
    public RichTextEditor(boolean html) {
        super();
        this.html = html;
        initUI();
    }

    /**
    * Creates rich text editor component for rtf or html format in authoring mode or not.
    * @param html if true then then generates html format editor else generates rtf format editor
    * @param authorMode if true then in authoring mode else non-authoring mode
    */
    public RichTextEditor(boolean html, boolean authorMode) {
        super();
        this.html = html;
        this.authorMode = authorMode;
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
            StringBuilder stringBuffer = new StringBuilder(text);
            bis = new ByteArrayInputStream(stringBuffer.toString().getBytes("UTF-8"));
            if (html) {
                htmlEditor.read(bis, textPane.getDocument(), 0);
            } else {
                rtfEditor.read(bis, textPane.getDocument(), 0);
            }
            oldText = getPlainText();
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
            oldText = text;
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
            oldText = getPlainText();
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

    /**
     * Sets time period after what inserted text is logged to actionlog
     * @param interval interval in milliseconds
     */
    public void setTypingLogIntervalMs(int interval) {
        typingLogIntervalMs = interval;
    }

    /**
     * Gets inserted text logging interval in milliseconds
     * @return inserted text logging interval in milliseconds
     */
    public int getTypingLogIntervalMs() {
        return typingLogIntervalMs;
    }

    /**
     * Gets synchronization session
     * @return synchronization session
     */
    public ISyncSession getSyncSession() {
        return syncSession;
    }

    /**
     * Sets synchronization session
     * @param synchronization session
     */
    public void setSyncSession(ISyncSession sess) {
        syncSession = sess;
    }

    private void showError(String messageID, Throwable e) {
        logger.error(messages.getString(messageID), e);
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
        textPane.addCaretListener(this);
        this.add(new JScrollPane(textPane), BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        this.fileToolbar = new RtfFileToolbar(this, authorMode);
        panel.add(fileToolbar, BorderLayout.WEST);
        if (!html) {
            this.formatToolbar = new RtfFormatToolbar(this);
            panel.add(formatToolbar, BorderLayout.CENTER);
        }
        this.add(panel, BorderLayout.NORTH);
    }

    public void insertedTextToActionLog() {
        if (timer != null) {
            if (!insertedText.equals("")) {
                if (rtfLogger != null)
                    rtfLogger.logInsertAction(insertedText, typingLogIntervalMs);
                insertedText = "";
                if (timer.getDelay() != typingLogIntervalMs)
                    timer.setDelay(typingLogIntervalMs);
            }
        }
    }

    private SyncObject createSyncObject() {
        SyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(SYNC_TOOL_INSTANCE_UUID, uuid.toString());
        return syncObject;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        synchronized(this) {
            if (!syncing) {
                oldText = getPlainText();
                if (typingOldPos!=e.getOffset()-1)
                    insertedText = insertedText + "\n";
                typingOldPos = e.getOffset();
                insertedText = insertedText + oldText.substring(e.getOffset(), e.getOffset()+e.getLength());
                if (timer == null) {
                    timer = new Timer(typingLogIntervalMs, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            insertedTextToActionLog();
                        }
                    });
                    timer.start();
                }
                if (syncSession != null) {
                    SyncObject syncObject = createSyncObject();
                    String text = oldText.substring(e.getOffset(), e.getOffset()+e.getLength());
                    String position = String.valueOf(e.getOffset());
                    syncObject.setProperty(SYNC_TEXT, text);
                    syncObject.setProperty(SYNC_POSITION_START, position);
logger.info("sending inserted text '" + text + "' at position '" + position + "'.");
                    syncSession.addSyncObject(syncObject);
logger.info("after sending");
                }
            } else {
                syncing = false;
            }
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        synchronized(this) {
            if (!syncing) {
                rtfLogger.logDeleteAction(oldText.substring(e.getOffset(), e.getOffset()+e.getLength()));
                oldText = getPlainText();
                if (syncSession != null) {
                    SyncObject syncObject = createSyncObject();
                    syncObject.setProperty(SYNC_TEXT, oldText.substring(e.getOffset(), e.getOffset()+e.getLength()));
                    syncObject.setProperty(SYNC_POSITION_START, String.valueOf(e.getOffset()));
                    syncObject.setProperty(SYNC_LENGTH, String.valueOf(e.getLength()));
                    syncSession.removeSyncObject(syncObject);
                }
            }
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
/*
        logger.debug("formatted: "+oldText.substring(e.getOffset(), e.getOffset()+e.getLength()));
        AttributeSet attributeSet = ((StyledDocument) e.getDocument()).getCharacterElement(e.getOffset()).getAttributes();
        logger.debug("italics: " + attributeSet.getAttribute(StyleConstants.Italic).toString());
        logger.debug("bold: " + attributeSet.getAttribute(StyleConstants.Bold).toString());
        String underlineLogValue = "null";
        if (attributeSet.getAttribute(StyleConstants.Underline)!=null)
            underlineLogValue = attributeSet.getAttribute(StyleConstants.Underline).toString();
        logger.debug("underline: " + underlineLogValue);
        String superscript = (StyleConstants.isSuperscript(rtfEditor.getInputAttributes())) ? "true" : "false";
        logger.debug("superscript: " + superscript);
        String subscript = (StyleConstants.isSubscript(rtfEditor.getInputAttributes())) ? "true" : "false";
        logger.debug("subscript: " + subscript);
*/
        //syncSession.changeSyncObject(syncObject);
    }

    @Override
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

    @Override
    public void caretUpdate(CaretEvent e) {
        synchronized(this) {
            if (!syncing) {
                int location = e.getMark();
                try {
                    if (location != 0 && textPane.getDocument().getLength()>0 &&
                        !textPane.getDocument().getText(location-1, 1).equals("\n"))
                        location--;
                } catch (BadLocationException ex) {
                    logger.error("BadLocationException in caretUpdate");
                }
                AttributeSet attributeSet = ((StyledDocument)textPane.getDocument()).getCharacterElement(location).getAttributes();
                String underlineValue = "false";
                if (attributeSet.getAttribute(StyleConstants.Underline)!=null)
                    underlineValue = attributeSet.getAttribute(StyleConstants.Underline).toString();
                String superscriptValue = "false";
                if (attributeSet.getAttribute(StyleConstants.Superscript)!=null)
                    superscriptValue = attributeSet.getAttribute(StyleConstants.Superscript).toString();
                String subscriptValue = "false";
                if (attributeSet.getAttribute(StyleConstants.Subscript)!=null)
                    subscriptValue = attributeSet.getAttribute(StyleConstants.Subscript).toString();
                formatToolbar.setFormatIcons(
                    Boolean.parseBoolean(attributeSet.getAttribute(StyleConstants.Bold).toString()),
                    Boolean.parseBoolean(attributeSet.getAttribute(StyleConstants.Italic).toString()),
                    Boolean.parseBoolean(underlineValue),
                    Boolean.parseBoolean(superscriptValue),
                    Boolean.parseBoolean(subscriptValue)
                );
            }
        }
    }
/*
    @Override
    protected void finalize() throws Throwable {
        try {
            insertedTextToActionLog();
        } catch(Exception e) {
        } finally {
            super.finalize();
        }
    }
 */
    @Override
    public void syncObjectAdded(ISyncObject syncObject) {
        synchronized(this) {
            if (syncObject.getProperty(SYNC_TOOL_INSTANCE_UUID) == null ||
                !syncObject.getProperty(SYNC_TOOL_INSTANCE_UUID).equals(uuid.toString())) {
                String text = syncObject.getProperty(SYNC_TEXT);
                String position = syncObject.getProperty(SYNC_POSITION_START);
logger.info("received inserted text '" + text + "' at position '" + position + "'.");
                syncing = true;
                try {
logger.info("text before insert: " + getPlainText());
                    textPane.setEditable(false);
                    textPane.getDocument().insertString(Integer.parseInt(position), text, null);
                    oldText = getPlainText();
logger.info("text after insert: " + getPlainText());
                } catch (Exception e) {
                    logger.error("Error adding symbol",e);
                } finally {
                    textPane.setEditable(true);
                }
            }
        }
    }

    @Override
    public void syncObjectChanged(ISyncObject syncObject) {

    }

    @Override
    public void syncObjectRemoved(ISyncObject syncObject) {
        synchronized(this) {
            if (syncObject.getProperty(SYNC_TOOL_INSTANCE_UUID) == null ||
                !syncObject.getProperty(SYNC_TOOL_INSTANCE_UUID).equals(uuid.toString())) {
                String text = syncObject.getProperty(SYNC_TEXT);
                String position = syncObject.getProperty(SYNC_POSITION_START);
                String length = syncObject.getProperty(SYNC_LENGTH);
logger.info("received deleted text '" + text + "' at position '" + position + "', length='" + length + ".");
                syncing = true;
                try {
logger.info("text before delete: " + getPlainText());
//                    textPane.setEnabled(false);
                    textPane.setEditable(false);
                    textPane.getDocument().remove(Integer.parseInt(position), Integer.parseInt(length));
                    oldText = getPlainText();
logger.info("text after delete: " + getPlainText());
                } catch (Exception e) {
                    logger.error("Error deleting text",e);
                } finally {
//                    textPane.setEnabled(true);
                    textPane.setEditable(true);
                }
            }
        }
    }
}
