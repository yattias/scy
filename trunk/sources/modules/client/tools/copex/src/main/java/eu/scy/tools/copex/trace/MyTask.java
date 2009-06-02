/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.util.ArrayList;

/**
 * tache representee de facon xml
 * @author MBO
 */
public class MyTask {
    // ATTRIBUTS
    /* identifiant de la tache */
    private long dbKeyTask;
    /* decsription de la tache */
    private String taskDescription;
    /* id de la tache parent */
    private long dbKeyParent;
    /* description de la tache parent */
    private String descriptionParent;
    /* id de la tache grand frere */
    private long dbKeyBrother;
    /* description de la tache grand frere */
    private String descriptionBrother;

    // CONSTRUCTEURS 
    public MyTask(long dbKeyTask, String taskDescription, long dbKeyParent, String descriptionParent, long dbKeyBrother, String descriptionBrother) {
        this.dbKeyTask = dbKeyTask;
        this.taskDescription = taskDescription;
        this.dbKeyParent = dbKeyParent;
        this.descriptionParent = descriptionParent;
        this.dbKeyBrother = dbKeyBrother;
        this.descriptionBrother = descriptionBrother;
    }

    public MyTask(long dbKeyTask, String taskDescription) {
        this.dbKeyTask = dbKeyTask;
        this.taskDescription = taskDescription;
        this.dbKeyParent = -1;
        this.descriptionParent = null;
        this.dbKeyBrother = -1;
        this.descriptionBrother = null;
    }

    public MyTask(long dbKeyTask, String taskDescription, long dbKeyParent, String descriptionParent) {
        this.dbKeyTask = dbKeyTask;
        this.taskDescription = taskDescription;
        this.dbKeyParent = dbKeyParent;
        this.descriptionParent = descriptionParent;
        this.dbKeyBrother = -1;
        this.descriptionBrother = null;
    }
    
    // METHODES 
    /* identifiant de la tache */
    protected String getDbKeyTaskToXML(){
        return "        <id_task>"+this.dbKeyTask+"</id_task>\n";
    }
    
    /* description de la tache */
    protected String getTaskDescriptionToXML(){
        return "        <desc_task>"+this.taskDescription+"</desc_task>\n";
    }
    
    
    /* identifiant du parent */
    protected String getDbKeyParentToXML(){
        return "        <id_parent>"+this.dbKeyParent+"</id_parent>\n";
    }
    
     /* description de la tache parent */
    protected String getDescriptionParentToXML(){
        return "        <desc_parent>"+this.descriptionParent+"</desc_parent>\n";
    }
    
    /* identifiant du grand frere eventuel */
    protected String getDbKeyOldBrotherToXML(){
        if (this.dbKeyBrother == -1 ) 
                return "";
        else
            return "        <id_brother>"+this.dbKeyBrother+"</id_brother>\n";
    }
    
    /* description du grand frere eventuel */
    protected String getDescriptionOldBrotherToXML(){
        if (this.descriptionBrother == null || this.descriptionBrother.length() == 0 ) 
                return "";
        else
            return "        <desc_brother>"+this.descriptionBrother+"</desc_brother>\n";
    }
    
    
    /* ajoute les parametres */
    public void addParameters(ArrayList<ParameterUserAction> listParameters){
        ParameterUserAction p = new ParameterUserAction("id_task", ""+this.dbKeyTask);
        listParameters.add(p);
        p = new ParameterUserAction("desc_task", ""+this.taskDescription);
        //listParameters.add(p);
        p = new ParameterUserAction("id_parent", ""+this.dbKeyParent);
        listParameters.add(p);
        p = new ParameterUserAction("desc_parent", ""+this.descriptionParent);
        //listParameters.add(p);
        if (this.dbKeyBrother == -1){
            p = new ParameterUserAction("id_brother", ""+this.dbKeyBrother);
            listParameters.add(p);
        }
        if (this.descriptionBrother != null && this.descriptionBrother.length() > 0){
            p = new ParameterUserAction("desc_brother", ""+this.descriptionBrother);
            //listParameters.add(p);
        }
    }
    
}
