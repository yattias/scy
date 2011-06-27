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

    /*
     * Constructor of the logger
     * @param actionLogger actionlogger
     * @param username username
     * @param toolname tool name
     * @param missionname mission name
     * @param sessionname session ID
     * @param parent if component is used as subcomponent then identification of the paren tool
     * @param editorPanel reference to main rich text editor component
     */
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

    /*
     * Sets ELO URI
     * @param eloUri ELO URI
     */
    public void setEloUri(String eloUri) {
        this.eloUri = eloUri;
    }

    /*
     * Writes action to actionlog
     */
    private void write() {
        actionLogger.log(action);
    }

    /*
     * Helper function to initialize action
     */
    private void createBasicAction(String type) {
        action = new Action();
        action.setUser(username);
        action.setType(type);
        //action.addContext(ContextConstants.tool, toolname+"_"+String.valueOf(loggerCount)+"_rich text editor component");
        // instances of the logger doesn't need to be identified (e.g. by numbering)
		action.addContext(ContextConstants.tool, toolname);
        action.addContext(ContextConstants.mission, missionname);
        action.addContext(ContextConstants.session, sessionname);
        if (eloUri != null) {
            action.addContext(ContextConstants.eloURI, eloUri);
        }
        action.addAttribute("parent", parent);
    }

    /*
     * Logs simple, so called basic, action
     * @param type action type, defined as constants at the end of this module
     */
    public void logBasicAction(String type) {
        createBasicAction(type);
        write();
    }

    /*
     * Logs file action
     * @param type action type, defined as constants at the end of this module
     */
    public void logFileAction(String type) {
        createBasicAction(type);
        action.addAttribute("text", editorPanel.getPlainText());
        write();
    }

    /*
     * Logs format action
     * @param type action type, defined as constants at the end of this module
     * @param text text what is formatted
     * @param isStyleIn is text formatted or is formatting cleared
     */
    public void logFormatAction(String type, String text, String isStyleIn) {
        createBasicAction(type);
        action.addAttribute("text", text);
        action.addAttribute("isStyleIn", isStyleIn);
        write();
    }

    /*
     * Logs delete action
     * @param text text what is deleted
     */
    public void logDeleteAction(String text) {
        createBasicAction(DELETE);
        action.addAttribute("text", text);
        write();
    }

    /*
     * Logs insert action
     * @param text text what is inserted
     * @param interval interval after what collected insertions are sent to actionlog
     */
    public void logInsertAction(String text, int interval) {
        createBasicAction(INSERT);
        action.addAttribute("text", text);
        action.addAttribute("interval", String.valueOf(interval));
        write();
    }
}
