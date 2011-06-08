/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

/**
 * COPEX constants
 * @author Marjolaine
 */
public class MyConstants {
    /* rights */
    public static final char NONE_RIGHT = 'N';
    public static final char EXECUTE_RIGHT  = 'X';
    
    /* comments viewing modee */
    public static final char COMMENTS = 'C';
    public static final char NO_COMMENTS  = 'N';
    public static final char COMMENTS_TEACHER = 'T';
    
    /* max lenght of fields */
    public static final int MAX_LENGHT_TASK_DESCRIPTION = 1024;
    public static final int MAX_LENGHT_TASK_COMMENTS = 1024;
    public static final int MAX_LENGHT_HYPOTHESIS = 1024;
    public static final int MAX_LENGHT_PROC_NAME = 32;
    public static final int MAX_LENGHT_GENERAL_PRINCIPLE = 1024;
    public static final int MAX_LENGHT_EVALUATION = 1024;
    public static final int MAX_LENGHT_MATERIAL_NAME=64;
    public static final int MAX_LENGHT_MATERIAL_DESCRIPTION = 1024;
    public static final int MAX_LENGHT_MATERIAL_COMMENTS = 1024;
    public static final int MAX_LENGHT_QUANTITY_NAME=32;
    

    /* logging */
    public static final char TRACE_ACTIV = 'T';
    public static final char TRACE_INACTIV = 'N';
    
    /* UNDO REDO */
    public static final char UNDO = 'U';
    public static final char REDO = 'R';
    public static final char NOT_UNDOREDO = 'N';

    
    /* db labbook*/
    public final static int DB_LABBOOK = 0;
    /* db labbook_copex */
    public final static int DB_LABBOOK_COPEX = 1;

    /** labdoc status lock */
    public final static String LABDOC_STATUS_LOCK="LOCK";
    public final static String LABDOC_STATUS_UPDATE="UPDATE";

    /* xml language */
    public final static String XMLNAME_LANGUAGE="language";

    /* menu mode */
    public final static char MODE_MENU_NO='N';
    public final static char MODE_MENU='M';
    public final static char MODE_MENU_TREE='T';

    public final static String XML_BOOLEAN_TRUE = "true";
    public final static String XML_BOOLEAN_FALSE = "false";

    /* pop up menu */
    public final static char POPUPMENU_MANIPULATION = 'M';
    public final static char POPUPMENU_STEP_ACTION = 'E';
    public final static char POPUPMENU_TASK = 'T';
    public final static char POPUPMENU_STEP = 'S';
    public final static char POPUPMENU_ACTION = 'A';
    public final static char POPUPMENU_MULTINODE = 'N';
    public final static char POPUPMENU_UNDEFINED = 'U';

    public final static char INSERT_TASK_UNDEF = 'U';
    public final static char INSERT_TASK_IN = 'I';
    public final static char INSERT_TASK_AFTER = 'A';
    public final static char INSERT_TASK_FIX = 'F';

    /* log cst */
    public final static String LOG_TYPE_START_TOOL = "tool_started";
    public final static String LOG_TYPE_END_TOOL = "tool_quit";
    public final static String LOG_TYPE_OPEN_PROC = "proc_opened";
    public final static String LOG_TYPE_CREATE_PROC = "proc_created";
    public final static String LOG_TYPE_COPY_PROC = "proc_copied";
    public final static String LOG_TYPE_DELETE_PROC = "proc_deleted";
    public final static String LOG_TYPE_LOAD_ELO = "elo_loaded";
    public final static String LOG_TYPE_NEW_ELO = "elo_added";
    public final static String LOG_TYPE_SAVE_PROC = "proc_saved";
    public final static String LOG_TYPE_RENAME_PROC = "proc_renamed";
    public final static String LOG_TYPE_PRINT_PROC = "pdf_exported";
    public final static String LOG_TYPE_OPEN_HELP = "help_opened";
    public final static String LOG_TYPE_CLOSE_HELP = "help_closed";
    public final static String LOG_TYPE_OPEN_HELP_PROC = "help_proc_opened";
    public final static String LOG_TYPE_CLOSE_HELP_PROC = "help_proc_closed";
    public final static String LOG_TYPE_UPDATE_QUESTION = "question_updated";
    public final static String LOG_TYPE_UPDATE_STEP = "step_updated";
    public final static String LOG_TYPE_UPDATE_ACTION = "action_updated";
    public final static String LOG_TYPE_ADD_STEP = "step_added";
    public final static String LOG_TYPE_ADD_ACTION = "action_added";
    public final static String LOG_TYPE_DELETE = "part_procedure_deleted";
    public final static String LOG_TYPE_COPY = "part_procedure_copied";
    public final static String LOG_TYPE_PASTE = "part_procedure_pasted";
    public final static String LOG_TYPE_CUT = "part_procedure_cut";
    public final static String LOG_TYPE_DRAG_DROP = "part_procedure_dragged_dropped";
    public final static String LOG_TYPE_HYPOTHESIS = "hypothesis_updated";
    public final static String LOG_TYPE_GENERAL_PRINCIPLE = "general_principle_updated";
    public final static String LOG_TYPE_EVALUATION = "evaluation_updated";
    public final static String LOG_TYPE_CREATE_MATERIAL_USED = "material_added";
    public final static String LOG_TYPE_DELETE_MATERIAL_USED = "material_deleted";
    public final static String LOG_TYPE_UPDATE_MATERIAL_USED = "material_updated";
    public final static String LOG_TYPE_REDO = "action_redone";
    public final static String LOG_TYPE_UNDO = "action_undone";
}
