/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * redo renommer un protocole
 * @author MBO
 */
public class RedoRenameProcAction  extends RedoAction{
     // ATTRIBUTS
    protected RenameProcAction renameProcAction ;
    
    // CONSTRUCTEURS
    public RedoRenameProcAction(long dbkeyProc, String procName, RenameProcAction renameProcAction){
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
