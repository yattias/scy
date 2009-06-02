/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;


/**
 * undo : cut
 * @author MBO
 */
public class UndoCutAction extends UndoAction{
    // ATTRIBUTS
    private AddTaskAction addTaskAction;

    // CONSTRUCTEURS
    public UndoCutAction(long dbKeyProc, String procName, AddTaskAction addTaskAction) {
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
