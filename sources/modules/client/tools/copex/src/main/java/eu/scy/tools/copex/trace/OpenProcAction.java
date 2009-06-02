/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * Ouverture d'un protocole
 * @param Id de la mission
 * @param Code de la mission
 * @author MBO
 */
public class OpenProcAction extends TraceAction{
    // ATTRIBUTS
    /* identifiant de la mission */
    private long dbKeyMission;
    /* code de la mission */
    private String codeMission;

    // CONSTRUCTEURS
    public OpenProcAction(long dbKeyProc, String procName, long dbKeyMission, String codeMission) {
        super("OPEN_PROC", dbKeyProc, procName);
        this.dbKeyMission = dbKeyMission;
        this.codeMission = codeMission;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getDbKeyMissionToXML();
        xml += getCodeMissionXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        ParameterUserAction p = new ParameterUserAction("id_mission", ""+this.dbKeyMission);
        listParameter.add(p);
        p = new ParameterUserAction("code_mission", this.codeMission);
        //listParameter.add(p);
    }
    
    
     /* identifiant de la mission */
    private String getDbKeyMissionToXML(){
        return "        <id_mission>"+this.dbKeyMission+"</id_mission>\n";
    }
    
    /* code de la mission */
    private String getCodeMissionXML(){
        return "        <code_mission>"+this.codeMission+"</codeMission>\n";
    }
    

    
    
    
}
