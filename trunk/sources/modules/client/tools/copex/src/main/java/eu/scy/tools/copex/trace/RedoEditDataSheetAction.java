/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : edition d'une case de la feuille de donnees
 * @author MBO
 */
public class RedoEditDataSheetAction extends RedoAction{
    // ATTRIBUTS
    private EditDataSheetAction editDataSheetAction ;
    
    // CONSTRUCTEURS
    public RedoEditDataSheetAction(long dbkeyProc, String procName, EditDataSheetAction editDataSheetAction){
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
