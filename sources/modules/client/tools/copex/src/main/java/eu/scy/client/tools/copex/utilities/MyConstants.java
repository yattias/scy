/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

/**
 * constantes communes aux applications
 * @author Marjolaine
 */
public class MyConstants {
    /* droits */
    public static final char NONE_RIGHT = 'N';
    public static final char EXECUTE_RIGHT  = 'X';
    
    /* mode affichage commentaire */
    public static final char COMMENTS = 'C';
    public static final char NO_COMMENTS  = 'N';
    public static final char COMMENTS_TEACHER = 'T';
    
    /* LONGUEUR MAX DESC CHAMPS */
    public static final int MAX_LENGHT_TASK_DESCRIPTION = 1024;
    public static final int MAX_LENGHT_TASK_COMMENTS = 1024;
    public static final int MAX_LENGHT_HYPOTHESIS = 1024;
    public static final int MAX_LENGHT_PROC_NAME = 32;
    public static final int MAX_LENGHT_DATA = 128;
    public static final int MAX_LENGHT_GENERAL_PRINCIPLE = 1024;
    public static final int MAX_LENGHT_EVALUATION = 1024;
    public static final int MAX_LENGHT_JUSTIFICATION = 1024;
    public static final int MAX_LENGHT_UNIT=12;
    public static final int MAX_LENGHT_MATERIAL_NAME=32;
    public static final int MAX_LENGHT_MATERIAL_DESCRIPTION = 500;
    public static final int MAX_LENGHT_MATERIAL_COMMENTS = 1024;
    public static final int MAX_LENGHT_QUANTITY_NAME=32;
    
    
    /* FORMAT IMPRESSION */
    public static final char FORMAT_TEXTE = 'T';
    public static final char FORMAT_PDF = 'P';
    
    /* TRACE */
    public static final char TRACE_ACTIV = 'T';
    public static final char TRACE_INACTIV = 'N';
    
    /* UNDO REDO */
    public static final char UNDO = 'U';
    public static final char REDO = 'R';
    public static final char NOT_UNDOREDO = 'N';

    /* MODE APPEL APPLET */
    public static final int MODE_APPLET_COPEX=0;
    public static final int MODE_APPLET_SCY = 1;
    public static final int MODE_APPLET_LOE = 2;

    /* champ de la base correspondants */
    public static final String BD_CHAMP_LOE = "LOE_USER";
    
    
    
    /* applet de l'applet venant de SCY*/
    public final static int SCY_MODE = 0;
    /* appel de l'applet venant su site COPEX */
    public final static int COPEX_MODE = 1;

    /* base copex */
    public final static int DB_LABBOOK = 0;
    /* base editeur de protocole */
    public final static int DB_LABBOOK_COPEX = 1;
    /* base data tool */
    public final static int DB_LABBOOK_FITEX = 2 ;

    /* xml language */
    public final static String XMLNAME_LANGUAGE="language";

    /* mode menu */
    public final static char MODE_MENU_NO='N';
    public final static char MODE_MENU='M';
    public final static char MODE_MENU_TREE='T';

    public final static String XML_BOOLEAN_TRUE = "true";
    public final static String XML_BOOLEAN_FALSE = "false";

    /* menu pop up */
    public final static char POPUPMENU_STEP = 'S';
    public final static char POPUPMENU_ACTION = 'A';
    public final static char POPUPMENU_TASK = 'T';
    public final static char POPUPMENU_UNDEF = 'U';

    public final static char INSERT_TASK_UNDEF = 'U';
    public final static char INSERT_TASK_IN = 'I';
    public final static char INSERT_TASK_AFTER = 'A';
    public final static char INSERT_TASK_FIX = 'F';

    /* log cst */
    public final static String LOG_TYPE_START_TOOL = "start_tool";
    public final static String LOG_TYPE_END_TOOL = "end_tool";
    public final static String LOG_TYPE_OPEN_PROC = "open_proc";
    public final static String LOG_TYPE_CREATE_PROC = "create_proc";
    public final static String LOG_TYPE_COPY_PROC = "copy_proc";
    public final static String LOG_TYPE_DELETE_PROC = "delete_proc";
    public final static String LOG_TYPE_LOAD_ELO = "load_elo";
    public final static String LOG_TYPE_NEW_ELO = "new_elo";
    public final static String LOG_TYPE_SAVE_PROC = "save_proc";
    public final static String LOG_TYPE_RENAME_PROC = "rename_proc";
    public final static String LOG_TYPE_PRINT_PROC = "print_proc";
    public final static String LOG_TYPE_OPEN_HELP = "open_help";
    public final static String LOG_TYPE_CLOSE_HELP = "close_help";
    public final static String LOG_TYPE_OPEN_HELP_PROC = "open_help_proc";
    public final static String LOG_TYPE_CLOSE_HELP_PROC = "close_help_proc";
    public final static String LOG_TYPE_UPDATE_QUESTION = "update_question";
    public final static String LOG_TYPE_UPDATE_STEP = "update_step";
    public final static String LOG_TYPE_UPDATE_ACTION = "update_action";
    public final static String LOG_TYPE_ADD_STEP = "add_step";
    public final static String LOG_TYPE_ADD_ACTION = "add_action";
    public final static String LOG_TYPE_PASTE = "paste";
    public final static String LOG_TYPE_DELETE = "delete";
    public final static String LOG_TYPE_CUT = "cut";
    public final static String LOG_TYPE_COPY = "copy";
    public final static String LOG_TYPE_DRAG_DROP = "drag_drop";
    public final static String LOG_TYPE_HYPOTHESIS = "hypothesis";
    public final static String LOG_TYPE_GENERAL_PRINCIPLE = "general_principle";
    public final static String LOG_TYPE_EVALUATION = "evaluation";
    public final static String LOG_TYPE_CREATE_MATERIAL_USED = "create_material_used";
    public final static String LOG_TYPE_DELETE_MATERIAL_USED = "delete_material_used";
    public final static String LOG_TYPE_UPDATE_MATERIAL_USED = "update_material_used";
    public final static String LOG_TYPE_REDO_DRAG_DROP = "redo_drag_drop";
    public final static String LOG_TYPE_REDO_RENAME_PROC = "redo_rename_proc";
    public final static String LOG_TYPE_REDO_EDIT_STEP = "redo_edit_step";
    public final static String LOG_TYPE_REDO_EDIT_ACTION = "redo_edit_action";
    public final static String LOG_TYPE_REDO_EDIT_QUESTION = "redo_edit_question";
    public final static String LOG_TYPE_REDO_ADD_STEP = "redo_add_step";
    public final static String LOG_TYPE_REDO_ADD_ACTION = "redo_add_action";
    public final static String LOG_TYPE_REDO_CUT = "redo_cut";
    public final static String LOG_TYPE_REDO_PASTE = "redo_paste";
    public final static String LOG_TYPE_REDO_DELETE_TASK = "redo_delete_task";
    public final static String LOG_TYPE_UNDO_PASTE = "undo_paste";
    public final static String LOG_TYPE_UNDO_DRAG_DROP = "undo_drag_drop";
    public final static String LOG_TYPE_UNDO_ADD_TASK = "undo_add_task";
    public final static String LOG_TYPE_UNDO_EDIT_STEP = "undo_edit_step";
    public final static String LOG_TYPE_UNDO_EDIT_ACTION = "undo_edit_action";
    public final static String LOG_TYPE_UNDO_EDIT_QUESTION = "undo_edit_question";
    public final static String LOG_TYPE_UNDO_CUT = "undo_cut";
    public final static String LOG_TYPE_UNDO_DELETE_TASK = "undo_delete_task";
    public final static String LOG_TYPE_UNDO_RENAME_PROC = "undo_rename_proc";
}
