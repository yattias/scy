/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : suppresion d'un materiel pour un protocole
 * @author MBO
 */
public class RedoDeleteMaterialUseForProcAction extends RedoAction {
    // ATTRIBUTS
    private DeleteMaterialUseForProcAction delMatAction;

    public RedoDeleteMaterialUseForProcAction(long dbKeyProc, String procName, DeleteMaterialUseForProcAction delMatAction) {
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
