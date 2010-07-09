/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.print;

import eu.scy.client.tools.copex.common.CopexAction;
import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.common.MaterialProc;
import eu.scy.client.tools.copex.edp.CopexPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marjolaine
 */
public class CopexHTML {
    private  CopexPanel edp;
    private  String copexHtml;

    public CopexHTML(CopexPanel edp) {
        this.edp = edp;
        copexHtml = "";
    }


    private void addString(String s){
        copexHtml += s+"\n";
    }

    public  CopexReturn exportProcHTML(ExperimentalProcedure proc, ArrayList v){
        copexHtml = "";
        addString("<table width='100%'  border='0' cellpadding='0'>");
        setItem(proc.getQuestion().getDescription(edp.getLocale()),proc.getQuestion().getComments(edp.getLocale()), "icone_AdT_question.png",  edp.getBundleString("TREE_QUESTION"));
        if(proc.getHypothesis() != null){
            setItem(proc.getHypothesis().getHypothesis(edp.getLocale()), proc.getHypothesis().getComment(edp.getLocale()), "icone_AdT_hypothese.png", edp.getBundleString("TREE_HYPOTHESIS"));
        }
        if(proc.getGeneralPrinciple() != null){
            setItem(proc.getGeneralPrinciple().getPrinciple(edp.getLocale()), proc.getGeneralPrinciple().getComment(edp.getLocale()), "icone_AdT_principe.png", edp.getBundleString("TREE_GENERAL_PRINCIPLE"));
        }
        setMaterial(proc.getMaterials());
        setManipulation(proc);
        if(proc.getDataSheet() != null){
            setItem(proc.getDataSheet().toTreeString(edp.getLocale()), null, "icone_AdT_datasheet", edp.getBundleString("TREE_DATASHEET"));
        }
        if(proc.getEvaluation() != null){
            setItem(proc.getEvaluation().getEvaluation(edp.getLocale()), proc.getEvaluation().getComment(edp.getLocale()), "icone_AdT_eval.png", edp.getBundleString("TREE_EVALUATION"));
        }
        addString("</table>");
        v.add(copexHtml);
        return new CopexReturn();
    }

   private void setItem(String item, String comments, String icon, String title ){
        if(item == null)
            return;
        addString("<tr>");
            addString("<td width='3%' valign='top'>");
                addString("<img src = ../tool_copex/images/"+icon+">");
            addString("</td>");
            addString("<td>");
                addString("<table width='100%'  border='0' cellpadding='0'>");
                    addString("<tr>");
                        addString("<td>");
                            addString("<span class='title'>"+title+"</span>");
                        addString("</td>");
                    addString("</tr>");
                    addString("<tr>");
                        addString("<td>");
                            addString("<span class='proc'>"+item+"</span>");
                        addString("</td>");
                    addString("</tr>");
                    if(comments != null && comments.length() > 0){
                    addString("<tr>");
                        addString("<td>");
                            addString("<span class='comment'>"+comments+"</span>");
                            addString("</td>");
                     addString("</tr>");
                    }
                addString("</table>");
            addString("</td>");
        addString("<tr>");
    }


    private void setMaterial(MaterialProc materialProc){
        if(materialProc == null)
            return;
        List<String> listMaterial = materialProc.getListTree(edp.getLocale());
        int nb = listMaterial.size();
        int nbRow = 0;
        if(nb%2 == 1){
            nbRow =nb/2+1;
        }else
            nbRow = nb/2;

        addString("<tr>");
            addString("<td width='3%' valign='top'>");
                addString("<img src = ../tool_copex/images/icone_AdT_material.png>");
            addString("</td>");
            addString("<td>");
                addString("<table width='100%'  border='0' cellpadding='0'>");
                    addString("<tr>");
                        addString("<td>");
                            addString("<span class='title'>"+edp.getBundleString("TREE_MATERIAL")+"</span>");
                        addString("</td>");
                    addString("</tr>");
                    addString("<tr>");
                        addString("<td>");
                            addString("<table border='0' cellpadding='0'>");
                            for(int i=0; i<nbRow; i++){
                                addString("<tr>");
                                for(int j=1; j<3; j++){
                                    addString("<td>");
                                    int k = i+(j-1)*nbRow;
                                    if(k < nb){
                                        addString(listMaterial.get(k));
                                    }else{
                                        addString("&nbsp;");
                                    }
                                    addString("</td>");
                                }
                                addString("</tr>");
                            }
                            addString("</table>");
                        addString("</td>");
                    addString("</tr>");
                addString("</table>");
            addString("</td>");
        addString("<tr>");
    }

    private void setManipulation(ExperimentalProcedure proc){
        String icon = "icone_AdT_manip.png";
        if(proc.isTaskProc()){
            icon = "icone_AdT_manip_tasks.png";
        }
        addString("<tr>");
            addString("<td width='3%' valign='top'>");
                addString("<img src = ../tool_copex/images/"+icon+">");
            addString("</td>");
            addString("<td>");
                addString("<table width='100%'  border='0' cellpadding='0'>");
                    addString("<tr>");
                        addString("<td>");
                            addString("<span class='title'>"+edp.getBundleString("TREE_MANIPULATION")+"</span>");
                        addString("</td>");
                    addString("</tr>");
                    addString("<tr>");
                        addString("<td>");
                            addString(getChildTaskTable(proc, proc.getQuestion()));
                        addString("</td>");
                    addString("</tr>");
                addString("</table>");
            addString("</td>");
        addString("<tr>");
    }

    private String getChildTaskTable(ExperimentalProcedure proc, CopexTask task){
        String manip = "<table width='100%'  border='0' cellpadding='0'>\n";
        ArrayList<CopexTask> childTasks = getTaskListChild(proc, task);
        int nb = childTasks.size();
        for(int i=0; i<nb; i++){
            manip += "<tr>\n";
                manip += "<td width='3%' valign='top'>\n";
                    manip += "<img src = ../tool_copex/images/"+getTaskIcon(proc, childTasks.get(i))+">\n";
                manip += "</td>\n";
                manip += "<td>\n";
                    manip += getTaskTable(proc, childTasks.get(i))+"\n";
                manip += "</td>\n";
            manip += "</tr>\n";
        }
        manip += "</table>\n";
        return manip;
    }

    private String getTaskTable(ExperimentalProcedure proc, CopexTask task){
        String descriptionTask = "";
        if(task instanceof CopexAction){
            descriptionTask = ((CopexAction)task).toDescription(edp);
        }else{
            descriptionTask = task.getDescription(edp.getLocale());
        }
        String comments = task.getComments(edp.getLocale());
        boolean hasChildren = task.getDbKeyChild() != -1;
        String taskTable = "<table width='100%'  border='0' cellpadding='0'>\n";
        taskTable += "<tr>\n";
            taskTable += "<td>\n";
                taskTable += descriptionTask+"\n";
            taskTable += "</td>\n";
        taskTable += "</tr>\n";
        if(comments != null && comments.length() > 0){
        taskTable += "<tr>\n";
            taskTable += "<td>\n";
                taskTable += comments+"\n";
            taskTable += "</td>\n";
        taskTable += "</tr>\n";
        }
        if(hasChildren){
        taskTable += "<tr>\n";
            taskTable += "<td>\n";
                taskTable += getChildTaskTable(proc, task)+"\n";
            taskTable += "</td>\n";
        taskTable += "</tr>\n";
        }
        taskTable += "</table>\n";
        return taskTable;
    }

    private String getTaskIcon(ExperimentalProcedure proc, CopexTask task){
        String img = "";
        if (task == null){
            System.out.println("ATTENTION TACHE NULL");
            return "";
        }
        if (task.isStep()){
            if(proc.isTaskProc()){
                if (task.getEditRight() == MyConstants.NONE_RIGHT ||task.getDeleteRight() == MyConstants.NONE_RIGHT ||task.getCopyRight() == MyConstants.NONE_RIGHT ||task.getMoveRight() == MyConstants.NONE_RIGHT ||task.getParentRight() ==MyConstants.NONE_RIGHT )
                    img = "Icone-AdT_tache_lock.png";
                else{
                 img = "Icone-AdT_tache.png";
                }
            }else{
                if (task.getDbKeyChild() == -1)
                    img = "Icone-AdT_etape_warn.png";
                else if (task.getEditRight() == MyConstants.NONE_RIGHT ||task.getDeleteRight() == MyConstants.NONE_RIGHT ||task.getCopyRight() == MyConstants.NONE_RIGHT ||task.getMoveRight() == MyConstants.NONE_RIGHT ||task.getParentRight() ==MyConstants.NONE_RIGHT )
                    img = "Icone-AdT_etape_lock.png";
                else
                    img = "Icone-AdT_etape.png";
            }
        }else if (task.isAction()){
            if (task.getEditRight() == MyConstants.NONE_RIGHT)
                img = "Icone-AdT_action_lock.png";
            else
                img = "Icone-AdT_action.png";
        }
        return img;
    }

    /* returns the list of the children of a task  */
    private ArrayList<CopexTask> getTaskListChild(ExperimentalProcedure proc, CopexTask task){
        ArrayList<CopexTask> childs = new ArrayList();
        // premier enfant
        long dbKeyFirstChild = task.getDbKeyChild();
        if (dbKeyFirstChild != -1){
            CopexTask t = getTask(proc.getListTask(), dbKeyFirstChild);
            if (t != null){
                 childs.add(t);
                // recupere les freres de t
                ArrayList<CopexTask> brothers = getTaskBrother(proc, t);
                int nbB = brothers.size();
                for (int j=0; j<nbB; j++){
                    childs.add(brothers.get(j));
                }
            }
        }
        return childs;
    }

    /* returns the brothers of a task */
    private ArrayList<CopexTask> getTaskBrother(ExperimentalProcedure proc, CopexTask task){
        ArrayList<CopexTask> brothers = new ArrayList();
        long idb = task.getDbKeyBrother();
        if (idb != -1){
            CopexTask tb = getTask(proc.getListTask(), idb);
            if (tb != null){
                brothers.add(tb);
                ArrayList<CopexTask> lb = getTaskBrother(proc, tb);
                int n = lb.size();
                for (int k=0; k<n; k++){
                    brothers.add(lb.get(k));
                }
            }
        }
        return brothers;
    }

    /* returns the task with the specified id, null otherwise */
    private CopexTask getTask(List<CopexTask> listTask, long dbKey){
        int nbT = listTask.size();
        for(int i=0; i<nbT; i++){
            if (listTask.get(i).getDbKey() == dbKey)
                return listTask.get(i);
        }
        return null;
    }
}
