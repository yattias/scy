/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.util.ArrayList;

/**
 * action pouvant etre enregistree dans la trace
 * @author MBO
 */
public abstract class TraceAction extends EdPTrace {
    // ATTRIBUTS
    /* identifiant du protocole associe */
    protected long dbKeyProc;
    /* nom du protocole */
    protected String procName;
    /* tableau des parametres */
    protected ArrayList<ParameterUserAction> listParameter;

    // CONSTRUCTEURS
    public TraceAction(String type, long dbKeyProc, String procName) {
        super(type);
        this.dbKeyProc = dbKeyProc;
        this.procName = procName;
        this.listParameter = new ArrayList();
    }

    // GETTER AND SETTER
    

    public long getDbKeyProc() {
        return dbKeyProc;
    }

    public String getProcName() {
        return procName;
    }

    public ArrayList<ParameterUserAction> getListParameter() {
        return listParameter;
    }
    
    // METHODES
    /* convertit les parametres de l'action au format xml */
    public String getParameterToXML(){
        String xml = getDbKeyProcToXML();
        xml += getProcNameToXML();
        return xml;
    }
    
    /* construit la liste des parametres */
    protected void initParameter(){
        ParameterUserAction p = new ParameterUserAction("id_proc", ""+this.dbKeyProc);
        listParameter.add(p);
        p = new ParameterUserAction("proc_name", ""+this.procName);
        //listParameter.add(p);
    }
    
    /* identifiant du protocole */
    protected String getDbKeyProcToXML(){
        return "        <id_proc>"+this.dbKeyProc+"</id_proc>\n";
    }
    
    /* nom du protocole */
    protected String getProcNameToXML(){
        return "        <proc_name>"+this.procName+"</proc_name>\n";
    }
    
    /* retourne le nombre de parametres */
    public int getNbParameters(){
        return this.listParameter.size();
    }
    
}
