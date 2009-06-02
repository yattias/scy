/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * ajout d'une etape
 * @author MBO
 */
public class AddStepAction extends AddTaskAction{

    // CONSTRUCTEURS
    public AddStepAction(long dbKeyProc, String procName, MyTask task, String taskComments, String taskImage) {
        super("ADD_STEP", dbKeyProc, procName, task, taskComments, taskImage);
    }

}
