/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : cut
 * @author MBO
 */
public class RedoCutAction extends RedoAction {
    // ATTRIBUTS
    private CutAction cutAction;

    // CONSTRUCTEURS
    public RedoCutAction(long dbKeyProc, String procName, CutAction cutAction) {
        super(dbKeyProc, procName);
        this.cutAction = cutAction;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = cutAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.cutAction.addParameters();
    }
    
    
}
