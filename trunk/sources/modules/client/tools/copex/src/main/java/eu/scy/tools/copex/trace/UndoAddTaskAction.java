/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo ajout d'une a tache
 * @author MBO
 */
public class UndoAddTaskAction extends UndoAction{
    
    // ATTRIBUTS
    private SupprAction supprAction;

    // CONSTRUCTEURS
    public UndoAddTaskAction(long dbKeyProc, String procName, SupprAction supprAction) {
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
