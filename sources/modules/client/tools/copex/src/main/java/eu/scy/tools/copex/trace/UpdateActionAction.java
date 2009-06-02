/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de modifier une action !!
 * @author MBO
 */
public class UpdateActionAction extends UpdateTaskAction{

    public UpdateActionAction(long dbKeyProc, String procName, long dbKeyTask, String oldDescription, String newDescription, String newComments) {
        super("UPDATE_ACTION", dbKeyProc, procName, dbKeyTask, oldDescription, newDescription, newComments);
    }

}
