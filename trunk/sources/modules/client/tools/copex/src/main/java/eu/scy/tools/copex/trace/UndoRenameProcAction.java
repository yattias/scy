/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * undo renommer un proc
 * @author MBO
 */
public class UndoRenameProcAction extends UndoAction {
    // ATTRIBUTS
    protected RenameProcAction renameProcAction ;
    
    // CONSTRUCTEURS
    public UndoRenameProcAction(long dbkeyProc, String procName, RenameProcAction renameProcAction){
        super(dbkeyProc, procName);
        this.renameProcAction = renameProcAction ;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml = renameProcAction.getParameterToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        this.renameProcAction.addParameters();
    }
    
    
}
