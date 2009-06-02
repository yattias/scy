/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo : creation d'un datasheet
 * @author MBO
 */
public class UndoCreateDataSheetAction extends UndoAction{
    // ATTRIBUTS
    /* suppression dataSheet */
    private DeleteDataSheetAction deleteDataSheetAction;

    // CONSTRUCTEURS
    public UndoCreateDataSheetAction(long dbKeyProc, String procName, DeleteDataSheetAction deleteDataSheetAction) {
        super(dbKeyProc, procName);
        this.deleteDataSheetAction = deleteDataSheetAction;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = deleteDataSheetAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        //this.deleteDataSheetAction.addParameters();
    }
    
}
