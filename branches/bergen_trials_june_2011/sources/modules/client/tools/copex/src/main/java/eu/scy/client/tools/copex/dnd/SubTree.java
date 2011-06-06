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
 * partie de l'arbre destinees a etre collees ou glissee-deposee.
 * @author MBO
 */
public class SubTree extends JTree implements Serializable {
    /* fenetre mere */
    private EdPPanel edP;
    /* controller */
    private ControllerInterface controller;
    /* protocole initial */
    private ExperimentalProcedure proc;
    /* arbre auquel il appartient a l'origine */
    private CopexTree owner;
    /* modele de donnees */
    private CopexTreeModel subTreeModel;
    /* liste des taches representees */
    private ArrayList<CopexTask> listTask;
    /* liste des noeuds sources */
    private ArrayList<CopexNode> listNodes;
    /* tache frere */
    private long lastBrother = -1;
    /* premier noeud origine */
    private CopexTask firstTaskOriginal;
    /* provient d'un drag and drop*/
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
    
    // METHODES 
    /* initialisation */
    private void init(){
        lastBrother = -1;
        firstTaskOriginal = listTask.get(0);
        // on passe les taches en droit RW
        int nbT = listTask.size();
        if(!dragNdrop){
            for (int k=0; k<nbT; k++){
                listTask.get(k).setTaskRight(new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT));
            }
        }
        
        // creation du modele
        // on cree une racine fictive
        fictivTask = new Question(edP.getLocale());
        fictivTask.setDbKeyChild(listTask.get(0).getDbKey());
        subTreeModel = new CopexTreeModel(proc, listTask, fictivTask);
        // sur la derniere tache a connecter on enleve le lien frere eventuel 
        int idL = getIdLastTask();
        if (idL != -1){
            lastBrother = listTask.get(idL).getDbKeyBrother();
            listTask.get(idL).setDbKeyBrother(-1);
        }
        // si on a copier la question principale : 
        if (listTask.get(0).isQuestionRoot()){
            listTask.get(0).setRoot(false);
        }
         
    }
    
    
    /* clone la liste des taches */
    private ArrayList<CopexTask> cloneList(ArrayList<CopexTask> listT){
        ArrayList listClone = new ArrayList();
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            listClone.add((CopexTask)listT.get(i).clone());
        }
        return listClone;
    }
    
    /* retourne l'indice de la derniere tache a connecter : ie le dernier enfant de 
     la racine, sinon elle meme */
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

    /* retourne la premiere tache */
    public CopexTask getFirstTask(){
        return this.firstTaskOriginal;
    }
    /* retourne vrai si le premier element (hors racine) de l'arbre est une question */
    public boolean isQuestion(){
        return listTask.get(0).isQuestion();
            
    }
    
    /* retourne le noeud d'une tache */
    public TaskTreeNode getNode(CopexTask task){
        return getNode(task, (TaskTreeNode)subTreeModel.getRoot());
    }
    
     /* renvoit le noeud crorrespondant a la tache */
    private TaskTreeNode getNode(CopexTask task, TaskTreeNode node){
       if (node.getTask().getDbKey() == task.getDbKey())
           return node;
       else{
           // on cherche dans les enfants
           if (node.getChildCount() > 0){
               for (int k=0; k<node.getChildCount(); k++){
                   TaskTreeNode n = getNode(task, (TaskTreeNode)subTreeModel.getChild(node, k));
                   if (n != null)
                       return n;
               }
           }
           // on cherche dans les freres
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
    
    /* retourne le premier noeud */
    public TaskTreeNode getFirstNode(){
        return (TaskTreeNode)subTreeModel.getChild((TaskTreeNode)subTreeModel.getRoot(), 0);
    }
    
    /* retourne vrai si le noeud appartient au sous arbre */
    public boolean containNode(CopexNode node){
        int nbN = listNodes.size();
        for (int i=0; i<nbN; i++){
            if (node.equals(listNodes.get(i)))
                return true;
        }
        return false;
    }
    
    /* mise a jour de la liste des taches */
    public void updateListTask(ArrayList<CopexTask> listT){
        this.listTask = listT;
    }
    
    // GETTER AND SETTER
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
