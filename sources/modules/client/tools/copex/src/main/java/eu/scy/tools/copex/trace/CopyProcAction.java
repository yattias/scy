/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de copie d'un protocole existant 
 * @param Id du protocole a  copier 
 * @param nom du protocole a  copier
 * @param Id du nouveau protocole
 * @param Nom du nouveau protocole a  copier
 * @author MBO
 */
public class CopyProcAction extends TraceAction{
    // ATTRIBUTS
    /* id du protocole a  copier */
    private long dbKeyProcToCopy;
    /* nom du protocole a  copier */
    private String procNameToCopy;

    // CONSTRUCTEURS 
    public CopyProcAction(long dbKeyProc, String procName, long dbKeyProcToCopy, String procNameToCopy) {
        super("COPY_PROC", dbKeyProc, procName);
        this.dbKeyProcToCopy = dbKeyProcToCopy;
        this.procNameToCopy = procNameToCopy;
    }

    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getDbKeyProcToCopyToXML();
        xml += getProcNameToCopyToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        ParameterUserAction p = new ParameterUserAction("id_proc_copy", ""+this.dbKeyProcToCopy);
        listParameter.add(p);
        p = new ParameterUserAction("proc_name_copy", this.procNameToCopy);
        //listParameter.add(p);
    }
    
    
     /* identifiant du protocole */
    private String getDbKeyProcToCopyToXML(){
        return "        <id_proc_copy>"+this.dbKeyProcToCopy+"</id_proc_copy>\n";
    }
    
    /* nom du protocole */
    private String getProcNameToCopyToXML(){
        return "        <proc_name_copy>"+this.procNameToCopy+"</proc_name_copy>\n";
    }
    
    
    
}
