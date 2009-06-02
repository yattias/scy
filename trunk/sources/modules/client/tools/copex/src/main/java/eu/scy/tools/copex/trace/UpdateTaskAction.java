/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * action de modifier une tache
 * @author MBO
 */
public class UpdateTaskAction extends TraceAction{
    // ATTRIBUTS
    /* identifiant de la tache */
    protected long dbKeyTask;
    /* ancienne description */
    protected String oldDescription;
    /* nouvelle description */
    protected String newDescription;
    /* nouveau commentaires */
    protected String newComments;

    // CONSTRUCTEURS
    public UpdateTaskAction(String type, long dbKeyProc, String procName, long dbKeyTask, String oldDescription, String newDescription, String newComments) {
        super(type, dbKeyProc, procName);
        this.dbKeyTask = dbKeyTask;
        this.oldDescription = oldDescription;
        this.newDescription = newDescription;
        this.newComments = newComments;
    }
    
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += getDbKeyTaskToXML();
        xml += getOldDescriptionToXML();
        xml += getNewDescriptionToXML();
        xml += getNewCommentsToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        addParameters();
    }
    
    public void addParameters(){
        ParameterUserAction p = new ParameterUserAction("id_task", ""+this.dbKeyTask);
        listParameter.add(p);
        p = new ParameterUserAction("desc_old_task", this.oldDescription);
       //listParameter.add(p);
        p = new ParameterUserAction("desc_new_task", this.newDescription);
        listParameter.add(p);
        if (this.newComments != null){
            p = new ParameterUserAction("com_new_task", this.newComments);
            listParameter.add(p);
        }
    }
    /* identifiant de la tache */
    protected String getDbKeyTaskToXML(){
        return "        <id_task>"+this.dbKeyTask+"</id_task>\n";
    }
    
    /* description de la tache */
    protected String getOldDescriptionToXML(){
        return "        <desc_old_task>"+this.oldDescription+"</desc_old_task>\n";
    }
    
    /* nouvelle description de la tache */
    protected String getNewDescriptionToXML(){
        return "        <desc_new_task>"+this.newDescription+"</desc_new_task>\n";
    }
    
    /* nouveaux commentaires de la tache */
    protected String getNewCommentsToXML(){
        if (this.newComments == null)
            return "";
        else
            return "        <com_new_task>"+this.newComments+"</com_new_task>\n";
    }
}
