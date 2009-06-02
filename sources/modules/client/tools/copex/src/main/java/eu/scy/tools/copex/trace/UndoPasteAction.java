/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo : paste
 * @author MBO
 */
public class UndoPasteAction extends UndoAction{
    // ATTRIBUTS
    private SupprAction supprAction;

    // CONSTRUCTEURS
    public UndoPasteAction(long dbKeyProc, String procName, SupprAction supprAction) {
        super(dbKeyProc, procName);
        this.supprAction = supprAction;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = supprAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.supprAction.addParameters();
    }
    
}
