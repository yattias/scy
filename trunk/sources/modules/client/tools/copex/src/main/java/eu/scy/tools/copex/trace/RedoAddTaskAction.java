/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo ajout d'une tache
 * @author MBO
 */
public class RedoAddTaskAction extends RedoAction {
    // ATTRIBUTS
    private AddTaskAction addTaskAction;

    // CONSTRUCTEURS
    public RedoAddTaskAction(long dbKeyProc, String procName, AddTaskAction addTaskAction) {
        super(dbKeyProc, procName);
        this.addTaskAction = addTaskAction;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = addTaskAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        addTaskAction.addParameters();
    }
    
}
