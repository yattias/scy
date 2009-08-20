/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo : modification d'une feuille de donnees
 * @author MBO
 */
public class UndoUpdateDataSheetAction extends UndoAction {
    // ATTRIBUTS
    private UpdateDataSheetAction updateDataSheetAction ;
    
    // CONSTRUCTEURS
    public UndoUpdateDataSheetAction(long dbkeyProc, String procName, UpdateDataSheetAction updateDataSheetAction){
        super(dbkeyProc, procName);
        this.updateDataSheetAction = updateDataSheetAction ;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = updateDataSheetAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.updateDataSheetAction.addParameters();
    }
    
    
}
