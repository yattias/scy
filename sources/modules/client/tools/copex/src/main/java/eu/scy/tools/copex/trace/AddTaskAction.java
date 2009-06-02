/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * ajout d'une tache
 * @author MBO
 */
public abstract class AddTaskAction extends TraceAction {
    // ATTRIBUTS
    /* tache */
    protected MyTask task;
    /* commentaires de la tache */
    protected String taskComments;
    /* image de la tache */
    protected String taskImage ;

    // CONSTRUCTEURS
    public AddTaskAction(String type, long dbKeyProc, String procName, MyTask task, String taskComments, String taskImage) {
        super(type, dbKeyProc, procName);
        this.task = task;
        this.taskComments = taskComments;
        this.taskImage = taskImage;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        xml += task.getDbKeyTaskToXML();
        xml += task.getTaskDescriptionToXML();
        xml += getTaskCommentsToXML();
        xml += getTaskImageToXML();
        xml += task.getDbKeyParentToXML();
        xml += task.getDescriptionParentToXML();
        xml += task.getDbKeyOldBrotherToXML();
        xml += task.getDescriptionOldBrotherToXML();
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        addParameters();
    }
    
    public void addParameters(){
        if (this.taskComments != null && this.taskComments.length() > 0) {
            ParameterUserAction p = new ParameterUserAction("com_task", ""+this.taskComments);
            //listParameter.add(p);
        }
        if (this.taskImage != null && this.taskImage.length() > 0) {
            ParameterUserAction p = new ParameterUserAction("img_task", ""+this.taskImage);
            //listParameter.add(p);
        }
        this.task.addParameters(listParameter);
    }
    
    /* commentaires de la tache */
    protected String getTaskCommentsToXML(){
        if (this.taskComments == null || this.taskComments.length() == 0) 
                return "";
        else
            return "        <com_task>"+this.taskComments+"</com_task>\n";
    }
    
    /* image de la tache */
    protected String getTaskImageToXML(){
        if (this.taskImage == null || this.taskImage.length() == 0) 
                return "";
        else
            return "        <img_task>"+this.taskImage+"</img_task>\n";
    }
}
