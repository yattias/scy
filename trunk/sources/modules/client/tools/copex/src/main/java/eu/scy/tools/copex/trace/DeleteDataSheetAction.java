/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * suppression d'une feuille de données
 * @author MBO
 */
public class DeleteDataSheetAction extends TraceAction {

    //CONSTRUCTEURS
    public DeleteDataSheetAction(long dbKeyProc, String procName) {
        super("DELETE_DATASHEET", dbKeyProc, procName);
    }

}
