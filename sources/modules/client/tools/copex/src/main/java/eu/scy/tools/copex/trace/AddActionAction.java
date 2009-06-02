/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action d'ajouter une action !!
 * @author MBO
 */
public class AddActionAction extends AddTaskAction{

    // CONSTRUCTEURS
    public AddActionAction(long dbKeyProc, String procName, MyTask task, String taskComments, String taskImage) {
        super("ADD_ACTION", dbKeyProc, procName, task, taskComments, taskImage);
    }

}
