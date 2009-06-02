/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de renommer un protocole
 * @author MBO
 */
public class RenameProcAction extends TraceAction{
    // ATTRIBUTS
    /* nouveau nom du protocole */
    private String newNameProc;

    // CONSTRUCTEURS
    public RenameProcAction(long dbKeyProc, String procName, String newNameProc) {
        super("RENAME_PROC", dbKeyProc, procName);
        this.newNameProc = newNameProc;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getNewNameProcToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        addParameters();
    }
    public void addParameters(){
        ParameterUserAction p = new ParameterUserAction("new_name_proc", this.newNameProc);
        listParameter.add(p);
    }
    
     /* nouveau nom du protocole */
    private String getNewNameProcToXML(){
        return "        <new_name_proc>"+this.newNameProc+"</new_name_proc>\n";
    }
    
}
