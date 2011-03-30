/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.print;

import eu.scy.client.tools.copex.common.CopexAction;
import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.common.InitialProcedure;
import eu.scy.client.tools.copex.common.MaterialProc;
import eu.scy.client.tools.copex.common.MaterialStrategy;
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
        //addString("<table width='100%'  border='0' cellpadding='0'>");
        setItem(proc.getQuestion().getDescription(edp.getLocale()),proc.getQuestion().getComments(edp.getLocale()), "icone_AdT_question.png",  edp.getBundleString("TREE_QUESTION"));
        if(proc.getHypothesis() != null){
            setItem(proc.getHypothesis().getHypothesis(edp.getLocale()), proc.getHypothesis().getComment(edp.getLocale()), "icone_AdT_hypothese.png", edp.getBundleString("TREE_HYPOTHESIS"));
        }
        if(proc.getGeneralPrinciple() != null){
            setItem(proc.getGeneralPrinciple().getPrinciple(edp.getLocale()), proc.getGeneralPrinciple().getComment(edp.getLocale()), "icone_AdT_principe.png", edp.getBundleString("TREE_GENERAL_PRINCIPLE"));
        }
        setMaterial(proc, proc.getMaterials());
        setManipulation(proc);
        if(proc.getDataSheet() != null){
            setItem(proc.getDataSheet().toTreeString(edp.getLocale()), null, "icone_AdT_datasheet", edp.getBundleString("TREE_DATASHEET"));
        }
        if(proc.getEvaluation() != null){
            setItem(proc.getEvaluation().getEvaluation(edp.getLocale()), proc.getEvaluation().getComment(edp.getLocale()), "icone_AdT_eval.png", edp.getBundleString("TREE_EVALUATION"));
        }
        //addString("</table>");
        // System.out.println(copexHtml);
        v.add(copexHtml);
        return new CopexReturn();
    }

   private void setItem(String item, String comments, String icon, String title ){
        if(item == null)
            return;
        addString("<table  style=\"width: 100%;\" border='0' cellpadding='0'>");
        addString("<tr>");
            addString("<td style=\"width:3%;\" valign='top'>");
                addString("<img src=\"../tool_copex/images/"+icon+"\">");
            addString("</td>");
            addString("<td style=\"width: 97%;\" >");
                addString("<table  style=\"width: 100%;\" border='0' cellpadding='0'>");
                    addString("<tr>");
                        addString("<td style=\"width: 100%;\">");
                            addString("<span class='title'>"+title+"</span>");
                        addString("</td>");
                    addString("</tr>");
                    addString("<tr>");
                        addString("<td style=\"width: 100%;\">");
                            addString("<span class='proc'>"+item+"</span>");
                        addString("</td>");
                    addString("</tr>");
                    //if(comments != null && comments.length() > 0){
                    addString("<tr>");
                        addString("<td style=\"width: 100%;\">");
                            addString("<span class='taskcomment'>"+comments+"</span>");
                            addString("</td>");
                     addString("</tr>");
                    //}
                addString("</table>");
            addString("</td>");
        addString("</tr>");
        addString("</table>");
    }


    private void setMaterial(ExperimentalProcedure proc, MaterialProc materialProc){
        if(materialProc == null)
            return;
        if(!proc.hasMaterial())
            return;
        List<String> listMaterial = materialProc.getListTree(edp.getLocale());
        int nb = listMaterial.size();
        int nbRow = 0;
        if(nb%2 == 1){
            nbRow =nb/4+1;
        }else
            nbRow = nb/4;
        addString("<table  style=\"width: 100%;\"  border='0' cellpadding='0'>");
        addString("<tr>");
            addString("<td style=\"width: 3%;\" valign='top'>");
                addString("<img src=\"../tool_copex/images/icone_AdT_material.png\">");
            addString("</td>");
            addString("<td>");
                addString("<table  style=\"width: 97%;\"   border='0' cellpadding='0'>");
                    addString("<tr>");
                        addString("<td style=\"width: 100%;\">");
                            addString("<span class='title'>"+edp.getBundleString("TREE_MATERIAL")+"</span>");
                        addString("</td>");
                    addString("</tr>");
                    addString("<tr>");
                        addString("<td>");
                            addString("<table border='0' cellpadding='0' style=\"width: 100%;\">");
                            for(int i=0; i<nbRow; i++){
                                addString("<tr>");
                                for(int j=1; j<5; j++){
                                    addString("<td style=\"width: 25%;\">");
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
        addString("</tr>");
        addString("</table>");
    }

    private void setManipulation(ExperimentalProcedure proc){
        String icon = "icone_AdT_manip.png";
        if(proc.isTaskProc()){
            icon = "icone_AdT_manip_tasks.png";
        }
        addString("<table  style=\"width: 100%;\"   border='0' cellpadding='0'>");
        addString("<tr>");
            addString("<td style=\"width: 3%;\" valign='top'>");
                addString("<img src=\"../tool_copex/images/"+icon+"\">");
            addString("</td>");
            addString("<td>");
                addString("<table  style=\"width: 97%;\"   border='0' cellpadding='0'>");
                    addString("<tr>");
                        addString("<td style=\"width: 100%;\">");
                            addString("<span class='title'>"+edp.getBundleString("TREE_MANIPULATION")+"</span>");
                        addString("</td>");
                    addString("</tr>");
                            
                addString("</table>");
            addString("</td>");
        addString("</tr>");
        addString("</table>");
        addString(getChildTaskTable(proc, proc.getQuestion(), 1));
    }

    private String getChildTaskTable_old(ExperimentalProcedure proc, CopexTask task){
        String manip = "<table  style='width:100%'   border='0' cellpadding='0' class='level1'>\n";
        ArrayList<CopexTask> childTasks = getTaskListChild(proc, task);
        int nb = childTasks.size();
        for(int i=0; i<nb; i++){
            manip += "<tr>\n";
                manip += "<td width='3%' valign='top'>\n";
                    manip += "<img src=\"../tool_copex/images/"+getTaskIcon(proc, childTasks.get(i))+"\">\n";
                manip += "</td>\n";
                manip += "<td>\n";
                    manip += getTaskTable_old(proc, childTasks.get(i))+"\n";
                manip += "</td>\n";
            manip += "</tr>\n";
        }
        manip += "</table>\n";
        return manip;
    }

    private String getChildTaskTable(ExperimentalProcedure proc, CopexTask task, int level){
        String manip = "<ul class='copexlist'>";
        ArrayList<CopexTask> childTasks = getTaskListChild(proc, task);
        int nb = childTasks.size();
        for(int i=0; i<nb; i++){
            manip += "<li>"+getTaskTable(proc, childTasks.get(i),level)+"</li>\n";
        }
        manip+="</ul>";
        return manip;
    }

    private String getTaskTable_old(ExperimentalProcedure proc, CopexTask task){
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
            taskTable += "<td><span class='proc'>\n";
                taskTable += descriptionTask+"\n";
            taskTable += "</span></td>\n";
        taskTable += "</tr>\n";
        if(comments != null && comments.length() > 0){
            taskTable += "<tr>\n";
            taskTable += "<td> <span class='comment'>\n";
            taskTable += comments+"\n";
            taskTable += "</span></td>\n";
            taskTable += "</tr>\n";
        }
        if(task.getDraw() != null){
            taskTable += "<tr>\n";
            taskTable += "<td> <span class='task_draw'>\n";
            String fileName = "labdoc-task-"+task.getDbKey()+".png";
            taskTable += "<img src=\"../tools_utilities/InterfaceServer/labdoc/"+fileName+"\" alt=\"Dessin\">\n";
            taskTable += "</span></td>\n";
            taskTable += "</tr>\n";
        }
        if(hasChildren){
        taskTable += "<tr>\n";
            taskTable += "<td>\n";
                taskTable += getChildTaskTable_old(proc, task)+"\n";
            taskTable += "</td>\n";
        taskTable += "</tr>\n";
        }
        taskTable += "</table>\n";
        return taskTable;
    }

    private String getTaskTable(ExperimentalProcedure proc, CopexTask task, int level){
        String descriptionTask = "";
        if(task instanceof CopexAction){
            descriptionTask = ((CopexAction)task).toDescription(edp) ;
        }else{
            descriptionTask = task.getDescription(edp.getLocale());
        }
        String comments = task.getComments(edp.getLocale());
        boolean hasChildren = task.getDbKeyChild() != -1;
        String manip = "<table  style=\"width: 100%;\"  border='0' cellpadding='0' >\n";
            manip += "<tr>\n";
                manip += "<td style=\"width: 20px;\" valign='top'>\n";
                manip += "<img src=\"../tool_copex/images/"+getTaskIcon(proc, task)+"\">\n";
                manip += "</td>\n";
                int nbRepeat = 1;
                if(task.getTaskRepeat() != null)
                    nbRepeat = task.getTaskRepeat().getNbRepeat();
                if(nbRepeat > 1){
                    manip += "<td style=\"width: 10px%;\" valign='top'><span class='task_repeat'>\n";
                    manip += "("+nbRepeat+"*)\n";
                }else{
                    manip += "<td style=\"width: 0px;\" valign='top'><span class='task_repeat'>\n";
                }
                manip += "</span></td>\n";
                manip += "<td>\n";

        String taskTable = "<table style=\"width: 95%;\" border='0' cellpadding='0' >\n";
        taskTable += "<tr>\n";
            taskTable += "<td style=\"width: 100%;\"><span class='proc'>\n";
                taskTable += descriptionTask+"\n";
            taskTable += "</span></td>\n";
        taskTable += "</tr>\n";
        if(comments != null && comments.length() > 0){
            taskTable += "<tr>\n";
            taskTable += "<td style=\"width: 100%;\"> <span class='taskcomment'>\n";
            taskTable += comments+"\n";
            taskTable += "</span></td>\n";
            taskTable += "</tr>\n";
        }
        if(task.getDraw() != null){
            if(task.getDraw() .getChild("whiteboardContainers") == null || (task.getDraw() .getChild("whiteboardContainers") != null && task.getDraw() .getChild("whiteboardContainers").getContentSize() > 0)){
            taskTable += "<tr>\n";
            taskTable += "<td > <span class='task_draw' style=\"width: 100%;\">\n";
            String fileName = "labdoc-task-"+task.getDbKey()+".png";
            taskTable += "<img src=\"../tools_utilities/InterfaceServer/labdoc/"+fileName+"\" alt=\"Dessin\" >\n";
            taskTable += "</span></td>\n";
            taskTable += "</tr>\n";
            }
        }
        taskTable += "</table>\n";
        manip += taskTable;
        manip += "</td>\n";
            manip += "</tr>\n";
        manip += "</table>\n";
        if(hasChildren){
            level++;
            manip += getChildTaskTable(proc, task, level)+"\n";
        }
        //return taskTable;
        
        return manip;
    }

    private String getTaskIcon(ExperimentalProcedure proc, CopexTask task){
        String img = "";
        if (task == null){
            // System.out.println("ATTENTION TACHE NULL");
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
