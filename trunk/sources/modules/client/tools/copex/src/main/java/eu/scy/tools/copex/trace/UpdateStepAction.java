/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de modification d'une etape
 * @author MBO
 */
public class UpdateStepAction extends UpdateTaskAction{

    public UpdateStepAction(long dbKeyProc, String procName, long dbKeyTask, String oldDescription, String newDescription, String newComments) {
        super("UPDATE_STEP", dbKeyProc, procName, dbKeyTask, oldDescription, newDescription, newComments);
    }

}
