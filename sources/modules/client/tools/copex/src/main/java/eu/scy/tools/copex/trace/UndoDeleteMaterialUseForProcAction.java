/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo : suppression d'un materiel pour un procotole
 * @author MBO
 */
public class UndoDeleteMaterialUseForProcAction extends UndoAction{
    // ATTRIBUTS
    private AddMaterialUseForProcAction addMatAction;

    public UndoDeleteMaterialUseForProcAction(long dbKeyProc, String procName, AddMaterialUseForProcAction addMatAction) {
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
