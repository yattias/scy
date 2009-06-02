/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de creation d'un nouveau protocole
 * @param Nom du protocole
 * @param Id du protocole
 * @author MBO
 */
public class CreateProcAction extends TraceAction {

    // CONSTRUCTEURS
    public CreateProcAction(long dbKeyProc, String procName) {
        super("CREATE_PROC", dbKeyProc, procName);
    }

}
