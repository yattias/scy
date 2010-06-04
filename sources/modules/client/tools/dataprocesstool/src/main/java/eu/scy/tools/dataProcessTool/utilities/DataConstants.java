/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

import java.awt.Color;


/**
 * Constantes de l'application
 * @author Marjolaine Bodin
 */
public class DataConstants {
    /* OPERATIONS */
    public static final int OP_SUM = 0;
    public static final int OP_AVERAGE=1;
    public static final int OP_MIN = 2;
    public static final int OP_MAX = 3;
    public static final int OP_SUM_IF = 4;
    
    /* TYPES DE VISUALISATION */
    public static final int VIS_GRAPH = 0;
    public static final int VIS_PIE=1;
    public static final int VIS_BAR = 2;
    public static final int VIS_HISTO = 3;
    public static final String TYPE_VIS_GRAPH = "GRAPH";
    public static final String TYPE_VIS_PIE = "PIE CHART";
    public static final String TYPE_VIS_BAR = "BAR CHART";
    public static final String TYPE_VIS_HISTO = "HISTO";
    
    /* LONGUEUR MAX DESC CHAMPS */
    public static final int MAX_LENGHT_DATASET_NAME = 32;
    public static final int MAX_LENGHT_DATAHEADER_NAME = 32;
    public static final int MAX_LENGHT_DATAHEADER_DESCRIPTION = 1024;
     public static final int MAX_LENGHT_DATAHEADER_FORMULA = 1024;
    public static final int MAX_LENGHT_UNIT = 32;
    public static final int MAX_LENGHT_VISUALIZATION_NAME = 32;
    public static final int MAX_LENGHT_GRAPH_NAME = 32;
    public static final int MAX_LENGHT_AXIS_NAME= 10;
    public final static int MAX_LENGHT_DATA=128;

    /* MODE GRAPH */
    public static final char MODE_ZOOM = 'Z';
    public static final char MODE_MOVE = 'M';
    public static final char MODE_DEFAULT = MODE_ZOOM;

    /* DROITS */
    public static final char NONE_RIGHT='N';
    public static final char EXECUTIVE_RIGHT='X';

    /* applet de l'applet venant de SCY*/
    public final static int SCY_MODE = 0;
    /* appel de l'applet venant su site COPEX */
    public final static int COPEX_MODE = 1;

    /* base copex */
    public final static int DB_COPEX = 0;
    /* base editeur de protocole */
    public final static int DB_COPEX_EDP = 1;
    /* base data tool */
    public final static int DB_COPEX_DATA = 2 ;

    /* nombre max de plots */
    public final static int MAX_PLOT = 4;
    /* couleur points graphique */
    public static final Color SCATTER_PLOT_COLOR_1 = Color.RED ;
    public static final Color SCATTER_PLOT_COLOR_2 = new Color(153,0,153);
    public static final Color SCATTER_PLOT_COLOR_3 = new Color(255,120,0);
    public static final Color SCATTER_PLOT_COLOR_4 = new Color(102,51,0);

    /* default type of a column */
    public final static String DEFAULT_TYPE_COLUMN = "double";
    public final static String TYPE_DOUBLE = "double";
    public final static String TYPE_STRING = "String";

    /* csv separator */
    public final static String CSV_SEPARATOR_COMMA=",";
    public final static String CSV_SEPARATOR_SEMICOLON=";";
    public final static String CSV_SEPARATOR_TEXT="";
    public final static String CSV_SEPARATOR_TEXT_QUOTATION_MARK="\" \"";

    /* nombre max de plotsfonctions */
    public final static int MAX_FUNCTION = 43;
    /* couleur points graphique */
    public static final Color FUNCTION_COLOR_1 = Color.BLUE ;
    public static final Color FUNCTION_COLOR_2 = new Color(51, 153, 0);
    public static final Color FUNCTION_COLOR_3 = Color.BLACK;

    /*function type */
    // y=f(x)
    public static final char FUNCTION_TYPE_Y_FCT_X = 'Y';
    // x=f(y)
    public static final char FUNCTION_TYPE_X_FCT_Y = 'X';
    public static final String F_Y = "y=f(x)";
    public static final String F_X = "x=f(y)";

    /* xml language */
    public final static String XMLNAME_LANGUAGE="language";
    /* LOG TYPE */
    public static final String LOG_TYPE_START_TOOL = "start_tool";
    public static final String LOG_TYPE_END_TOOL = "end_tool";
    public static final String LOG_TYPE_NEW = "new_elo";
    public final static String LOG_TYPE_SAVE_DATASET = "save_dataset";
    public static final String LOG_TYPE_OPEN_DATASET = "open_dataset";
    public static final String LOG_TYPE_MERGE_DATASET = "merge_dataset";
    public static final String LOG_TYPE_IMPORT_CSV_FILE = "import_csv_file";
    public static final String LOG_TYPE_EXPORT_CSV_FILE = "export_csv_file";
    public final static String LOG_TYPE_DELETE_DATASET = "delete_dataset";
    public static final String LOG_TYPE_EDIT_DATA = "edit_data";
    public static final String LOG_TYPE_EDIT_HEADER = "edit_header";
    public final static String LOG_TYPE_INSERT_COLUMNS = "insert_columns";
    public final static String LOG_TYPE_INSERT_ROWS = "insert_rows";
    public final static String LOG_TYPE_DELETE_ROWS = "delete_rows";
    public final static String LOG_TYPE_DELETE_COLS = "delete_cols";
    public final static String LOG_ADD_ROW = "add_row";
    public final static String LOG_INITIALIZE_HEADER = "initialize_header";
    public final static String LOG_TYPE_DELETE_OPERATIONS = "delete_operations";
    public final static String LOG_TYPE_ADD_OPERATION = "add_operation";
    public final static String LOG_TYPE_IGNORE_DATA = "ignore_data";
    public final static String LOG_TYPE_CREATE_VISUALIZATION = "create_visulization";
    public final static String LOG_TYPE_DELETE_VISUALIZATION = "delte_visualization";
    public final static String LOG_TYPE_GRAPH_MODE = "graph_mode";
    public final static String LOG_TYPE_UPDATE_VISUALIZATION = "update_visualization";
    public final static String LOG_TYPE_SORT_DATASET = "sort_dataset";
    public final static String LOG_TYPE_COPY = "copy";
    public final static String LOG_TYPE_PASTE = "paste";
    public final static String LOG_TYPE_CUT = "cut";
    public final static String LOG_TYPE_UNDO = "undo";
    public final static String LOG_TYPE_REDO = "redo";
    public final static String LOG_TYPE_GRAPH_FUNCTION = "graph_function";
    public final static String LOG_TYPE_RENAME_DATASET = "rename_dataset";
    
}
