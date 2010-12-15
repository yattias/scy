package eu.scy.client.common.richtexteditor;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.Action;

/**
* Action logger for rich text editor component.
*/
public class RichTextEditorLogger {
    public final static String OPEN = "file_opened";
    public final static String SAVE_RTF = "rtf_saved";
    public final static String PRINT = "rtf_printed";
    public final static String PDF = "pdf_saved";
    public final static String BOLD = "bold_clicked";
    public final static String ITALIC = "italic_clicked";
    public final static String UNDERLINE = "underline_clicked";
    public final static String SUPERSCRIPT = "superscript_clicked";
    public final static String SUBSCRIPT = "subscript_clicked";
    public final static String INSERT = "text_inserted";
    public final static String DELETE = "text_deleted";
    public static int COUNT = 0;

    private IActionLogger actionLogger;
    private String username;
    private String toolname;
    private String missionname;
    private String sessionname;
    private String eloUri = null;
    private String parent;
    private int loggerCount;
    private IAction action;
    private RichTextEditor editorPanel;

    public RichTextEditorLogger(IActionLogger actionLogger,
        String username, String toolname, String missionname,
        String sessionname, String parent, RichTextEditor editorPanel) {
        this.actionLogger = actionLogger;
        this.username = username;
        this.toolname = toolname;
        this.missionname = missionname;
        this.sessionname = sessionname;
        this.parent = parent;
        this.editorPanel = editorPanel;
        loggerCount = ++COUNT;
    }

    public void setEloUri(String eloUri) {
        this.eloUri = eloUri;
    }

    private void write() {
        actionLogger.log(action);
    }

    private void createBasicAction(String type) {
        action = new Action();
        action.setUser(username);
        action.setType(type);
        action.addContext(ContextConstants.tool, toolname+"_"+String.valueOf(loggerCount)+"_rich text editor component");
        action.addContext(ContextConstants.mission, missionname);
        action.addContext(ContextConstants.session, sessionname);
        if (eloUri != null) {
            action.addContext(ContextConstants.eloURI, eloUri);
        }
        action.addAttribute("parent", parent);
    }

    public void logBasicAction(String type) {
        createBasicAction(type);
        write();
    }

    public void logFileAction(String type) {
        createBasicAction(type);
        action.addAttribute("text", editorPanel.getPlainText());
        write();
    }

    public void logFormatAction(String type, String text, String isStyleIn) {
        createBasicAction(type);
        action.addAttribute("text", text);
        action.addAttribute("isStyleIn", isStyleIn);
        write();
    }

    public void logDeleteAction(String text) {
        createBasicAction(DELETE);
        action.addAttribute("text", text);
        write();
    }

    public void logInsertAction(String text, int interval) {
        createBasicAction(INSERT);
        action.addAttribute("text", text);
        action.addAttribute("interval", String.valueOf(interval));
        write();
    }
}
