/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : paste
 * @author MBO
 */
public class RedoPasteAction extends RedoAction {
    // ATTRIBUTS
    private PasteAction pasteAction;
    
    // CONSTRUCTEURS
    public RedoPasteAction(long dbKeyProc, String procName, PasteAction pasteAction) {
        super(dbKeyProc, procName);
        this.pasteAction = pasteAction;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = pasteAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.pasteAction.addParameters();
    }
    
}
