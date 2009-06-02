/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de modification d'une sous question
 * @author MBO
 */
public class UpdateQuestionAction extends UpdateTaskAction{
    // ATTRIBUTS
    /* nouvelle hypotheses */
    private String hypothesis;
    /* nouveau principe general */
    private String generalPrinciple;

    public UpdateQuestionAction(long dbKeyProc, String procName, long dbKeyTask, String oldDescription, String newDescription, String newComments, String hypothesis, String generalPrinciple) {
        super("UPDATE_QUESTION", dbKeyProc, procName, dbKeyTask, oldDescription, newDescription, newComments);
        this.hypothesis = hypothesis;
        this.generalPrinciple = generalPrinciple;
    }
    
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getHypothesisToXML();
        xml += getGeneralPrincipleToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        addParameters();
    }
    public void addParameters(){
        if (this.hypothesis != null){
            ParameterUserAction p = new ParameterUserAction("new_hypothesis", this.hypothesis);
            listParameter.add(p);
        }
        if (this.generalPrinciple != null){
            ParameterUserAction p = new ParameterUserAction("new_generalPrinciple", this.generalPrinciple);
            listParameter.add(p);
        }
    }
    
     /* hypothese */
    private String getHypothesisToXML(){
        if (this.hypothesis == null)
            return "";
        else
            return "        <new_hypothesis>"+this.hypothesis+"</new_hypothesis>\n";
    }
    
     /* principe genral */
    private String getGeneralPrincipleToXML(){
        if (this.generalPrinciple == null)
            return "";
        else
            return "        <new_generalPrinciple>"+this.generalPrinciple+"</new_generalPrinciple>\n";
    }
}
