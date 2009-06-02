/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * trace de ce qui n'est pas une action de l'utilisateur sur le proc, mias plus général : impression, aide...
 * texte sans paramètres a priori
 * @author Marjolaine
 */
public class TraceFunction extends EdPTrace {

    // CST : DIFF TYPES
    public final static String TRACE_PRINT = "PRINT" ;
    public final static String TRACE_OPEN_HELP = "OPEN_HELP";
    public final static String TRACE_CLOSE_HELP = "CLOSE_HELP";
    public final static String TRACE_OPEN_PROC_HELP = "OPEN_PROC_HELP";
    public final static String TRACE_OPEN_EDITOR = "OPEN_EDITOR";
    public final static String TRACE_CLOSE_EDITOR = "CLOSE_EDITOR";
    
    // CONSTRUCTOR
    public TraceFunction(String type) {
        super(type);
    }


}
