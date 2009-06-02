/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo : edition d'une case d'un datasheet
 * @author MBO
 */
public class UndoEditDataSheetAction extends UndoAction{
    // ATTRIBUTS
    private EditDataSheetAction editDataSheetAction ;
    
    // CONSTRUCTEURS
    public UndoEditDataSheetAction(long dbkeyProc, String procName, EditDataSheetAction editDataSheetAction){
        super(dbkeyProc, procName);
        this.editDataSheetAction = editDataSheetAction ;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = editDataSheetAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.editDataSheetAction.addParameters();
    }
    
}
