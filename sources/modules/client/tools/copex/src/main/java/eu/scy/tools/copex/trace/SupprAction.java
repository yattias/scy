/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.util.ArrayList;

/**
 * action de supprimer des taches
 * @author MBO
 */
public class SupprAction extends TraceAction{

    // ATTRIBUTS
    /* liste des taches */
    private ArrayList<MyTask> listTask;

    // CONSTRUCTEURS
    public SupprAction(long dbKeyProc, String procName, ArrayList listTask) {
        super("SUPPR", dbKeyProc, procName);
        this.listTask = listTask;
    }
    
    // OVERRIDE
    @Override
    public String getParameterToXML() {
        String xml =  super.getParameterToXML();
        int nbT = listTask.size();
        for (int i=0; i<nbT; i++){
            xml += getTaskToXML(listTask.get(i));
        }
        return xml;
    }

    @Override
    protected void initParameter() {
        super.initParameter();
        addParameters();
    }
    
    public void addParameters(){
        int nbT = listTask.size();
        for (int i=0; i<nbT; i++){
            listTask.get(i).addParameters(listParameter);
        }
    }
    
    
     /* tache a  copier */
    private String getTaskToXML(MyTask task){
        String xml = task.getDbKeyTaskToXML();
        xml += task.getTaskDescriptionToXML();
        return xml;
    }
}
