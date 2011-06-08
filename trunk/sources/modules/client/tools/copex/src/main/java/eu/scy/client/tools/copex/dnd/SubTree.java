/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.dnd;


import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.edp.CopexNode;
import eu.scy.client.tools.copex.edp.CopexTree;
import eu.scy.client.tools.copex.edp.CopexTreeModel;
import eu.scy.client.tools.copex.edp.TaskTreeNode;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JTree;

/**
 * sub tree that can be paste or drag'n'drop
 * @author Marjolaine
 */
public class SubTree extends JTree implements Serializable {
    /* owner */
    private EdPPanel edP;
    /* controller */
    private ControllerInterface controller;
    /* initial procedure */
    private ExperimentalProcedure proc;
    /* arbre auquel il appartient a l'origine */
    private CopexTree owner;
    /* data model */
    private CopexTreeModel subTreeModel;
    /* list task */
    private ArrayList<CopexTask> listTask;
    /* list nodes  */
    private ArrayList<CopexNode> listNodes;
    /* brother task */
    private long lastBrother = -1;
    /* first node */
    private CopexTask firstTaskOriginal;
    /* is from drag and drop?*/
    private boolean dragNdrop;
    private Question fictivTask;

    
    public SubTree(EdPPanel edP, ControllerInterface controller, ExperimentalProcedure proc, CopexTree owner, ArrayList<CopexTask> listTask, ArrayList<CopexNode> listNode, boolean dragNdrop) {
        super();
        this.edP = edP;
        this.controller = controller;
        this.proc = proc;
        this.owner = owner;
        this.listTask = cloneList(listTask) ;
        this.listNodes = listNode;
        this.dragNdrop = dragNdrop;
        init();
    }
    
    /* initialization*/
    private void init(){
        lastBrother = -1;
        firstTaskOriginal = listTask.get(0);
        // change rights RW
        int nbT = listTask.size();
        if(!dragNdrop){
            for (int k=0; k<nbT; k++){
                listTask.get(k).setTaskRight(new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT));
            }
        }
        
        // model creation
        // fictiv root creation
        fictivTask = new Question(edP.getLocale());
        fictivTask.setDbKeyChild(listTask.get(0).getDbKey());
        subTreeModel = new CopexTreeModel(proc, listTask, fictivTask);
        // on the last task to connect, remove the brother link, if exists
        int idL = getIdLastTask();
        if (idL != -1){
            lastBrother = listTask.get(idL).getDbKeyBrother();
            listTask.get(idL).setDbKeyBrother(-1);
        }
        // if we have to copy the question
        if (listTask.get(0).isQuestionRoot()){
            listTask.get(0).setRoot(false);
        }
         
    }
    
    
    /* clone task list */
    private ArrayList<CopexTask> cloneList(ArrayList<CopexTask> listT){
        ArrayList listClone = new ArrayList();
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            listClone.add((CopexTask)listT.get(i).clone());
        }
        return listClone;
    }
    
    /* returns the index of the last task to connect: ie the last child of the root, or the root */
    public int getIdLastTask(){
        CopexNode rootNode = (CopexNode)this.subTreeModel.getManipulationNode();
        CopexNode lastNode = (CopexNode)rootNode.getChildAt(rootNode.getChildCount()-1);
        CopexTask task = null;
        if(lastNode.isQuestion())
            task = (Question)lastNode.getNode();
        else 
            task = ((CopexNode)lastNode).getTask();
        return this.listTask.indexOf(task);
    }

    /* returns the first task */
    public CopexTask getFirstTask(){
        return this.firstTaskOriginal;
    }

    /* returns true if the first element (apart root) is a question  */
    public boolean isQuestion(){
        return listTask.get(0).isQuestion();
            
    }
    
    /* returns the node of a task  */
    public TaskTreeNode getNode(CopexTask task){
        return getNode(task, (TaskTreeNode)subTreeModel.getRoot());
    }
    
     /* returns the node that corresponds to the task */
    private TaskTreeNode getNode(CopexTask task, TaskTreeNode node){
       if (node.getTask().getDbKey() == task.getDbKey())
           return node;
       else{
           // search in the childs
           if (node.getChildCount() > 0){
               for (int k=0; k<node.getChildCount(); k++){
                   TaskTreeNode n = getNode(task, (TaskTreeNode)subTreeModel.getChild(node, k));
                   if (n != null)
                       return n;
               }
           }
           // search in the brothers
           TaskTreeNode parent = (TaskTreeNode)node.getParent();
           if (parent != null){
               TaskTreeNode bn = (TaskTreeNode)parent.getChildAfter(node);
               if (bn != null){
                   TaskTreeNode n = getNode(task, bn);
                   if (n != null)
                       return n;
               }
               
           }
       }
       return null;    
    }
    
    /* returns the first node */
    public TaskTreeNode getFirstNode(){
        return (TaskTreeNode)subTreeModel.getChild((TaskTreeNode)subTreeModel.getRoot(), 0);
    }
    
    /* returns true if the node belongs to the sub tree  */
    public boolean containNode(CopexNode node){
        int nbN = listNodes.size();
        for (int i=0; i<nbN; i++){
            if (node.equals(listNodes.get(i)))
                return true;
        }
        return false;
    }
    
    /* update the tasks list  */
    public void updateListTask(ArrayList<CopexTask> listT){
        this.listTask = listT;
    }
    
    public ArrayList<CopexTask> getListTask() {
        return listTask;
    }

    public CopexTree getOwner() {
        return owner;
    }

    public void setListNodes(ArrayList<CopexNode> listNodes) {
        this.listNodes = listNodes;
    }

    public ExperimentalProcedure getProc() {
        return proc;
    }

    public ArrayList<CopexNode> getListNodes() {
        return listNodes;
    }

    public long getLastBrother() {
        return lastBrother;
    }

    public void setLastBrother(long lastBrother) {
        this.lastBrother = lastBrother;
    }
    
    
}
