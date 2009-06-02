/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de supprimer le protocole
 * @author MBO
 */
public class DeleteProcAction extends TraceAction{
    
    // CONSTRUCTEURS
    public DeleteProcAction(long dbKeyProc, String procName) {
        super("DELETE_PROC", dbKeyProc, procName);
    }
}
