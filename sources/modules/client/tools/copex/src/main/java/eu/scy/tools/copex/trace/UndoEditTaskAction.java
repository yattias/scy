/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo pour la modification d'une tache
 * @author MBO
 */
public class UndoEditTaskAction extends UndoAction {
    // ATTRIBUTS
    protected UpdateTaskAction updateTaskAction ;
    
    // CONSTRUCTEURS
    public UndoEditTaskAction(long dbkeyProc, String procName, UpdateTaskAction updateTaskAction){
        super(dbkeyProc, procName);
        this.updateTaskAction = updateTaskAction ;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = updateTaskAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.updateTaskAction.addParameters();
    }
    
}
