/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : suppression de taches
 * @author MBO
 */
public class RedoDeleteTaskAction extends RedoAction{
    // ATTRIBUTS
    private SupprAction supprAction;

    // CONSTRUCTEURS
    public RedoDeleteTaskAction(long dbKeyProc, String procName, SupprAction supprAction) {
        super(dbKeyProc, procName);
        this.supprAction = supprAction;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = supprAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.supprAction.addParameters();
    }
    
    
}
