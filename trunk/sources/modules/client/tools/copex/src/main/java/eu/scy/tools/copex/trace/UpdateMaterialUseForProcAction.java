/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * mise a  jour justification pour materiel utlise dfans un proc
 * @author MBO
 */
public class UpdateMaterialUseForProcAction extends TraceAction  {
    // ATTRIBUTS
    /* identifiant materiel */
    private long dbKeyMat ;
    /* nom materiel */
    private String materialName;
    /* old justification */
    private String oldJustification;
    /* new justification */
    private String newJustification;

    public UpdateMaterialUseForProcAction(long dbKeyProc, String procName, long dbKeyMat, String materialName, String oldJustification, String newJustification) {
        super("UPDATE_MATERIAL_USE", dbKeyProc, procName);
        this.dbKeyMat = dbKeyMat;
        this.materialName = materialName;
        this.oldJustification = oldJustification;
        this.newJustification = newJustification;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getDbKeyMatToXML();
        xml += getMaterialNameToXML();
        xml += getOldJustificationToXML();
        xml += getNewJustificationToXML();
        return xml;
    }
    
    /* justification */
    protected String getOldJustificationToXML(){
        if (this.oldJustification == null ) 
                return "";
        else
            return "        <old_justification>"+this.oldJustification+"</old_justification>\n";
    }
     /* justification */
    protected String getNewJustificationToXML(){
        if (this.newJustification == null ) 
                return "";
        else
            return "        <new_justification>"+this.newJustification+"</new_justification>\n";
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
        if (this.oldJustification != null) {
            ParameterUserAction p = new ParameterUserAction("old_justification", ""+this.oldJustification);
            //listParameter.add(p);
        }
         if (this.newJustification != null) {
            ParameterUserAction p = new ParameterUserAction("new_justification", ""+this.newJustification);
            listParameter.add(p);
        }
        ParameterUserAction p = new ParameterUserAction("mat_name", ""+this.materialName);
        //listParameter.add(p);
        p = new ParameterUserAction("id_mat", ""+this.dbKeyMat);
        listParameter.add(p);
    }

}
