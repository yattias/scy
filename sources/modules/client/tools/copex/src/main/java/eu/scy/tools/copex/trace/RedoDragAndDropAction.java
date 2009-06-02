/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo : drag and drop 
 * @author MBO
 */
public class RedoDragAndDropAction extends RedoAction {
    // ATTRIBUTS
    private DragDropAction dragAndDropAction;

    // CONSTRUCTEURS
    public RedoDragAndDropAction(long dbKeyProc, String procName, DragDropAction dragAndDropAction) {
        super(dbKeyProc, procName);
        this.dragAndDropAction = dragAndDropAction;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = dragAndDropAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.dragAndDropAction.addParameters();
    }
    
    
}
