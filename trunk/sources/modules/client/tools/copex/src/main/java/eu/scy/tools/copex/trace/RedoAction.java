/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action du redo
 * @author MBO
 */
public class RedoAction extends TraceAction {

    // CONSTRUCTEURS
    public RedoAction(long dbKeyProc, String procName) {
        super("REDO", dbKeyProc, procName);
    }

}
