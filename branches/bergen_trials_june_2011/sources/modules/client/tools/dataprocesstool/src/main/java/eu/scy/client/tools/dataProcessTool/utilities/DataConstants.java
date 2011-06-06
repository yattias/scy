/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.utilities;

import java.awt.Color;
import javax.swing.SwingConstants;


/**
 * App. cst
 * @author Marjolaine Bodin
 */
public class DataConstants {
    /* OPERATIONS */
    public static final int OP_SUM = 0;
    public static final int OP_AVERAGE=1;
    public static final int OP_MIN = 2;
    public static final int OP_MAX = 3;
    public static final int OP_SUM_IF = 4;
    
    /* Visualizations types */
    public static final int VIS_GRAPH = 0;
    public static final int VIS_PIE=1;
    public static final int VIS_BAR = 2;
    public static final int VIS_HISTO = 3;
    public static final String TYPE_VIS_GRAPH = "GRAPH";
    public static final String TYPE_VIS_PIE = "PIE CHART";
    public static final String TYPE_VIS_BAR = "BAR CHART";
    public static final String TYPE_VIS_HISTO = "HISTO";

    /* */
    public final static int NB_DECIMAL_UNDEFINED = Integer.MAX_VALUE;
    public final static int NB_SIGNIFICANT_DIGITS_UNDEFINED = Integer.MAX_VALUE;

    /* max lenght fields */
    public static final int MAX_LENGHT_DATASET_NAME = 32;
    public static final int MAX_LENGHT_DATAHEADER_NAME = 32;
    public static final int MAX_LENGHT_DATAHEADER_DESCRIPTION = 1024;
    public static final int MAX_LENGHT_DATAHEADER_FORMULA = 1024;
    public static final int MAX_LENGHT_UNIT = 32;
    public static final int MAX_LENGHT_VISUALIZATION_NAME = 64;
    public final static int MAX_LENGHT_DATA=128;

    /* graph mode */
    public static final char MODE_ZOOM = 'Z';
    public static final char MODE_MOVE = 'M';
    public static final char MODE_DEFAULT = MODE_ZOOM;

    /* rights */
    public static final char NONE_RIGHT='N';
    public static final char EXECUTIVE_RIGHT='X';

    /* scy mode*/
    public final static int SCY_MODE = 0;
    /* labbook_mode */
    public final static int COPEX_MODE = 1;

    /* db labbook*/
    public final static int DB_LABBOOK = 0;
    /* db labbook_fitex */
    public final static int DB_LABBOOK_FITEX = 2;

    /** labdoc status lock */
    public final static String LABDOC_STATUS_LOCK="LOCK";
    public final static String LABDOC_STATUS_UPDATE="UPDATE";

    /* number max of plots */
    public final static int MAX_PLOT = 4;
    /* points color on the graph */
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

    /* number max de plots functions */
    public final static int MAX_FUNCTION = 43;
    /* function color in the graph */
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

    public final static int DEFAULT_DATASET_ALIGNMENT = SwingConstants.RIGHT;
    /* xml language */
    public final static String XMLNAME_LANGUAGE="language";
    /* LOG TYPE */
    public static final String LOG_TYPE_START_TOOL = "tool_started";
    public static final String LOG_TYPE_END_TOOL = "tool_quit";
    public static final String LOG_TYPE_NEW = "elo_added";
    public final static String LOG_TYPE_SAVE_DATASET = "dataset_saved";
    public static final String LOG_TYPE_OPEN_DATASET = "dataset_opened";
    public static final String LOG_TYPE_MERGE_DATASET = "dataset_merged";
    public final static String LOG_TYPE_DELETE_DATASET = "dataset_deleted";
    public final static String LOG_TYPE_RENAME_DATASET = "dataset_renamed";
    public static final String LOG_TYPE_IMPORT_CSV_FILE = "csv_file_imported";
    public static final String LOG_TYPE_IMPORT_GMBL_FILE = "gmbl_file_imported";
    public static final String LOG_TYPE_EXPORT_CSV_FILE = "csv_file_exported";
    public static final String LOG_TYPE_EDIT_DATA = "data_edited";
    public static final String LOG_TYPE_EDIT_HEADER = "header_edited";
    public final static String LOG_TYPE_INSERT_COLUMNS = "datasheet_columns_inserted";
    public final static String LOG_TYPE_INSERT_ROWS = "datasheet_rows_inserted";
    public final static String LOG_TYPE_DELETE = "datasheet_deleted";
    public final static String LOG_ADD_ROW = "sync_row_added";
    public final static String LOG_INITIALIZE_HEADER = "sync_header_initialized";
    public final static String LOG_TYPE_ADD_OPERATION = "operation_added";
    public final static String LOG_TYPE_IGNORE_DATA = "data_ignored";
    public final static String LOG_TYPE_SORT_DATASET = "datasheet_sorted";
    public final static String LOG_TYPE_COPY = "datasheet_copied";
    public final static String LOG_TYPE_PASTE = "datasheet_pasted";
    public final static String LOG_TYPE_CUT = "datasheet_cut";
    public final static String LOG_TYPE_UNDO = "action_undone";
    public final static String LOG_TYPE_REDO = "action_redone";
    public final static String LOG_TYPE_CREATE_VISUALIZATION = "graph_added";
    public final static String LOG_TYPE_DELETE_VISUALIZATION = "graph_deleted";
    public final static String LOG_TYPE_UPDATE_VISUALIZATION = "graph_updated";
    public final static String LOG_TYPE_GRAPH_MODE = "graph_mode_updated";
    public final static String LOG_TYPE_GRAPH_FUNCTION = "graph_function_updated";

    public static String actionMerge="merge";
    public static String actionMergeRow = "mergeRow";
    public static String actionMatrixAddOperation = "matrixAddOperation";
    public static String actionMatrixMultiplyOperation = "matrixMultiplyOperation";
    
}
