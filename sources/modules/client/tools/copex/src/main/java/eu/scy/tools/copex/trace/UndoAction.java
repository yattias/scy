/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action du undo
 * @author MBO
 */
public class UndoAction extends TraceAction{

    public UndoAction(long dbKeyProc, String procName) {
        super("UNDO", dbKeyProc, procName);
    }

}
