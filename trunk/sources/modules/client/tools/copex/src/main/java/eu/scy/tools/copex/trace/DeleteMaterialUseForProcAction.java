/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.util.ArrayList;

/**
 * suppression d'un materiel utilise dans un protocole
 * @author MBO
 */
public class DeleteMaterialUseForProcAction extends TraceAction{
    // ATTRIBUTS
    /* identifiant materiel */
    private long dbKeyMat ;
    /* nom materiel */
    private String materialName;

    // CONSTRUCTEURS
    public DeleteMaterialUseForProcAction(long dbKeyProc, String procName, long dbKeyMat, String materialName) {
        super("DELETE_MATERIAL_USE", dbKeyProc, procName);
        this.dbKeyMat = dbKeyMat;
        this.materialName = materialName;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getDbKeyMatToXML();
        xml += getMaterialNameToXML();
        return xml;
    }
    
    
    /* nom material */
    protected String getMaterialNameToXML(){
        return "        <mat_name>"+this.materialName+"</mat_name>\n";
    }
    
    /* dbKey material */
    protected String getDbKeyMatToXML(){
        return "        <id_mat>"+this.dbKeyMat+"</id_mat>\n";
    }
    
    @Override
    protected void initParameter() {
        super.initParameter();
        addParameters();
    }
    
    public void addParameters(){
        ParameterUserAction p = new ParameterUserAction("mat_name", ""+this.materialName);
        //listParameter.add(p);
        p = new ParameterUserAction("id_mat", ""+this.dbKeyMat);
        listParameter.add(p);
    }
}
