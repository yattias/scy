/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : modification d'une feuille de donnees
 * @author MBO
 */
public class RedoUpdateDataSheetAction extends RedoAction{
    // ATTRIBUTS
    private UpdateDataSheetAction updateDataSheetAction ;
    
    // CONSTRUCTEURS
    public RedoUpdateDataSheetAction(long dbkeyProc, String procName, UpdateDataSheetAction updateDataSheetAction){
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
