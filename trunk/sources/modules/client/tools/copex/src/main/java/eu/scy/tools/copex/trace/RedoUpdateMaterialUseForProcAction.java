/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : miseà  jour du materiel utlise pour le protocole
 * @author MBO
 */
public class RedoUpdateMaterialUseForProcAction extends RedoAction {
    // ATTRIBUTS
    private UpdateMaterialUseForProcAction updateMatAction;

    public RedoUpdateMaterialUseForProcAction(long dbKeyProc, String procName, UpdateMaterialUseForProcAction updateMatAction) {
        super(dbKeyProc, procName);
        this.updateMatAction = updateMatAction;
    }
    
     // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = updateMatAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.updateMatAction.addParameters();
    }
}
