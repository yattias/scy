/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

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
    public static final int MAX_LENGHT_QUESTION_HYPOTHESIS = 1024;
    public static final int MAX_LENGHT_PROC_NAME = 32;
    public static final int MAX_LENGHT_DATA = 128;
    public static final int MAX_LENGHT_QUESTION_GENERAL_PRINCIPLE = 1024;
    public static final int MAX_LENGHT_JUSTIFICATION = 1024;
    public static final int MAX_LENGHT_UNIT=12;
    public static final int MAX_LENGHT_MATERIAL_NAME=32;
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
    public final static int DB_COPEX = 0;
    /* base editeur de protocole */
    public final static int DB_COPEX_EDP = 1;
    /* base data tool */
    public final static int DB_COPEX_DATA = 2 ;
}
