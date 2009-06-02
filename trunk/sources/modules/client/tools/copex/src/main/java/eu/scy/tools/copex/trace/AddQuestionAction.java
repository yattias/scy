/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * ajout d'une question
 * @param hypothesis
 * @author MBO
 */
public class AddQuestionAction extends AddTaskAction {
    // ATTRIBUTS
    private String hypothesis;

    // CONSTRUCTEURS
    public AddQuestionAction(long dbKeyProc, String procName, MyTask task, String taskComments, String taskImage, String hypothesis) {
        super("ADD_QUESTION", dbKeyProc, procName, task, taskComments, taskImage);
        this.hypothesis = hypothesis;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getHypothesisToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        if (this.hypothesis == null || this.hypothesis.length() == 0)
            return ;
        ParameterUserAction p = new ParameterUserAction("hypothesis", ""+this.hypothesis);
        listParameter.add(p);
    }
    
    
     /* hypothese */
    private String getHypothesisToXML(){
        if (this.hypothesis == null || this.hypothesis.length() == 0)
            return "";
        else
            return "        <hypothesis>"+this.hypothesis+"</hypothesis>\n";
    }
    
}
