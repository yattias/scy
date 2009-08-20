/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.util.ArrayList;

/**
 * action de drag and drop
 * @author MBO
 */
public class DragDropAction extends TraceAction {
    // ATTRIBUTS
    /* liste des taches */
    private ArrayList<MyTask> listTask;

    public DragDropAction(long dbKeyProc, String procName, ArrayList<MyTask> listTask) {
        super("DRAG_AND_DROP", dbKeyProc, procName);
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
        xml += task.getDbKeyParentToXML();
        xml += task.getDescriptionParentToXML();
        xml += task.getDbKeyOldBrotherToXML();
        xml += task.getDescriptionOldBrotherToXML();
        return xml;
    }
}
