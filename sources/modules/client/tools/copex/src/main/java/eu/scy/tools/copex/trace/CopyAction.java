/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import java.util.ArrayList;

/**
 * action de copier d'une tache
 * @author MBO
 */
public class CopyAction extends TraceAction {
    // ATTRIBUTS
    /*liste de taches */
    private ArrayList<MyTask> listTask;

    // CONSTRUCTEURS
    public CopyAction(long dbKeyProc, String procName, ArrayList<MyTask> listTask) {
        super("COPY", dbKeyProc, procName);
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
