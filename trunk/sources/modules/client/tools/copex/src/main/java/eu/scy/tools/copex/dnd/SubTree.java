/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.dnd;


import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.edp.CopexTree;
import eu.scy.tools.copex.edp.CopexTreeNode;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.MyConstants;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JTree;

/**
 * partie de l'arbre destinées à être collées ou glissée-déposée.
 * @author MBO
 */
public class SubTree extends JTree implements Serializable {
    // ATTRIBUTS
    /* fenetre mere */
    private EdPPanel edP;
    /* controller */
    private ControllerInterface controller;
    /* protocole initial */
    private ExperimentalProcedure proc;
    /* arbre auquel il appartient à l'origine */
    private CopexTree owner;
    /* modele de données */
    private CopexTreeModel subTreeModel;
    /* liste des taches representées */
    private ArrayList<CopexTask> listTask;
    /* liste des noeuds sources */
    private ArrayList<CopexTreeNode> listNodes;
    /* tache frere */
    private long lastBrother = -1;
    /* premier noeud origine */
    private CopexTask firstTaskOriginal;
    /* provient d'un drag and drop*/
    private boolean dragNdrop;

    // CONSTRUCTEURS 
    
    
    public SubTree(EdPPanel edP, ControllerInterface controller, ExperimentalProcedure proc, CopexTree owner, ArrayList<CopexTask> listTask, ArrayList<CopexTreeNode> listNode, boolean dragNdrop) {
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
        Question fictivTask = new Question();
        fictivTask.setDbKeyChild(listTask.get(0).getDbKey());
        subTreeModel = new CopexTreeModel(proc, listTask, fictivTask);
        // sur la derniere tache à connecter on enleve le lien frere eventuel 
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
    
    /* retourne l'indice de la derniere tache à connecter : ie le dernier enfant de 
     la racine, sinon elle meme */
    public int getIdLastTask(){
        CopexTreeNode rootNode = (CopexTreeNode)this.subTreeModel.getRoot();
        CopexTreeNode lastNode = (CopexTreeNode)rootNode.getChildAt(rootNode.getChildCount()-1);
        return this.listTask.indexOf(lastNode.getTask());
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
    public CopexTreeNode getNode(CopexTask task){
        return getNode(task, (CopexTreeNode)subTreeModel.getRoot());
    }
    
     /* renvoit le noeud crorrespondant à la tache */
    private CopexTreeNode getNode(CopexTask task, CopexTreeNode node){
       if (node.getTask().getDbKey() == task.getDbKey())
           return node;
       else{
           // on cherche dans les enfants
           if (node.getChildCount() > 0){
               for (int k=0; k<node.getChildCount(); k++){
                   CopexTreeNode n = getNode(task, (CopexTreeNode)subTreeModel.getChild(node, k));
                   if (n != null)
                       return n;
               }
           }
           // on cherche dans les freres
           CopexTreeNode parent = (CopexTreeNode)node.getParent();
           if (parent != null){
               CopexTreeNode bn = (CopexTreeNode)parent.getChildAfter(node);
               if (bn != null){
                   CopexTreeNode n = getNode(task, bn);
                   if (n != null)
                       return n;
               }
               
           }
       }
       return null;    
    }
    
    /* retourne le premier noeud */
    public CopexTreeNode getFirstNode(){
        return (CopexTreeNode)subTreeModel.getChild((CopexTreeNode)subTreeModel.getRoot(), 0);
    }
    
    /* retourne vrai si le noeud appartient au sous arbre */
    public boolean containNode(CopexTreeNode node){
        int nbN = listNodes.size();
        for (int i=0; i<nbN; i++){
            if (node.equals(listNodes.get(i)))
                return true;
        }
        return false;
    }
    
    /* mise à jour de la liste des taches */
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

    public void setListNodes(ArrayList<CopexTreeNode> listNodes) {
        this.listNodes = listNodes;
    }

    public ExperimentalProcedure getProc() {
        return proc;
    }

    public ArrayList<CopexTreeNode> getListNodes() {
        return listNodes;
    }

    public long getLastBrother() {
        return lastBrother;
    }

    public void setLastBrother(long lastBrother) {
        this.lastBrother = lastBrother;
    }
    
    
}
