/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : creation d'un datasheet
 * @author MBO
 */
public class RedoCreateDataSheetAction extends RedoAction {
    // ATTRIBUTS
    /* creation d'un datasheet */
    private CreateDataSheetAction createDatasheetAction;

    
    // CONSTRUCTEURS
    public RedoCreateDataSheetAction(long dbKeyProc, String procName, CreateDataSheetAction createDatasheetAction) {
        super(dbKeyProc, procName);
        this.createDatasheetAction = createDatasheetAction;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = createDatasheetAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        createDatasheetAction.addParameters();
    }
    
}
