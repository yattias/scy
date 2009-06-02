/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * ajout d'un materiel à utliser
 * @author MBO
 */
public class AddMaterialUseForProcAction extends TraceAction {
    // ATTRIBUTS
    /* identifiant materiel */
    private long dbKeyMat ;
    /* nom materiel */
    private String materialName;
    /* justification */
    private String justification;

    // CONSTRUCTEURS
    public AddMaterialUseForProcAction(long dbKeyProc, String procName, long dbKeyMat, String materialName, String justification) {
        super("ADD_MATERIAL_USE", dbKeyProc, procName);
        this.dbKeyMat = dbKeyMat;
        this.materialName = materialName;
        this.justification = justification;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getDbKeyMatToXML();
        xml += getMaterialNameToXML();
        xml += getJustificationToXML();
        return xml;
    }
    
    /* justification */
    protected String getJustificationToXML(){
        if (this.justification == null ) 
                return "";
        else
            return "        <justification>"+this.justification+"</justification>\n";
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
        if (this.justification != null) {
            ParameterUserAction p = new ParameterUserAction("justification", ""+this.justification);
            listParameter.add(p);
        }
        ParameterUserAction p = new ParameterUserAction("mat_name", ""+this.materialName);
        //listParameter.add(p);
        p = new ParameterUserAction("id_mat", ""+this.dbKeyMat);
        listParameter.add(p);
    }
    
}
