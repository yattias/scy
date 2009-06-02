/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : ajoutd 'une tache
 * @author MBO
 */
public class RedoAddMaterialUseForProcAction extends RedoAction{
    // ATTRIBUTS
    private AddMaterialUseForProcAction addMatAction;

    public RedoAddMaterialUseForProcAction(long dbKeyProc, String procName, AddMaterialUseForProcAction addMatAction) {
        super(dbKeyProc, procName);
        this.addMatAction = addMatAction;
    }
    
     // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = addMatAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.addMatAction.addParameters();
    }
}
