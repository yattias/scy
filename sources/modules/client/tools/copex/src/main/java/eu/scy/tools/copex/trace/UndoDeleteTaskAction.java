/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo : suppression de taches
 * @author MBO
 */
public class UndoDeleteTaskAction extends UndoAction{
    // ATTRIBUTS
    private AddTaskAction addTaskAction;

    // CONSTRUCTEURS
    public UndoDeleteTaskAction(long dbKeyProc, String procName, AddTaskAction addTaskAction) {
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
        this.addTaskAction.addParameters();
    }
    
}
