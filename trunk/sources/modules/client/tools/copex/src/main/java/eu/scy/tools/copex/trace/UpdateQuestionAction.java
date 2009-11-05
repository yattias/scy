/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de modification d'une sous question
 * @author MBO
 */
public class UpdateQuestionAction extends UpdateTaskAction{
    // ATTRIBUTS

    public UpdateQuestionAction(long dbKeyProc, String procName, long dbKeyTask, String oldDescription, String newDescription, String newComments) {
        super("UPDATE_QUESTION", dbKeyProc, procName, dbKeyTask, oldDescription, newDescription, newComments);
    }
    
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
    }
    
}
