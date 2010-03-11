/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

/**
 * constantes communes aux applications
 * @author Marjolaine
 */
public class MyConstants {
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

    public final static int MAX_LENGHT_DATASET_NAME=32;

    /* nombre max de plots */
    public final static int MAX_PLOT = 4;

    /* default type of a column */
    public final static String DEFAULT_TYPE_COLUMN = "double";
    public final static String TYPE_DOUBLE = "double";
    public final static String TYPE_STRING = "String";
}
