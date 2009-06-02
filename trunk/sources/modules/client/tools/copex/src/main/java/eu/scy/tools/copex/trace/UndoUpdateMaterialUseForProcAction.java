/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo : modification du materiel utilise pour proc
 * @author MBO
 */
public class UndoUpdateMaterialUseForProcAction extends UndoAction {
    // ATTRIBUTS
    private UpdateMaterialUseForProcAction updateMatAction;

    public UndoUpdateMaterialUseForProcAction(long dbKeyProc, String procName, UpdateMaterialUseForProcAction updateMatAction) {
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
