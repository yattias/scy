/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo  : ajout d'un materiel utlise pour un proc
 * @author MBO
 */
public class UndoAddMaterialUseForProcAction extends UndoAction{
    // ATTRIBUTS
    private DeleteMaterialUseForProcAction delMatAction;

    public UndoAddMaterialUseForProcAction(long dbKeyProc, String procName, DeleteMaterialUseForProcAction delMatAction) {
        super(dbKeyProc, procName);
        this.delMatAction = delMatAction;
    }
    
     // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = delMatAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.delMatAction.addParameters();
    }
    
}
