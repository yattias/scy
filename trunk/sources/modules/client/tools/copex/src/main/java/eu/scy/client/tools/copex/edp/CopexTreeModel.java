/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.DataSheet;
import eu.scy.client.tools.copex.common.Evaluation;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.common.GeneralPrinciple;
import eu.scy.client.tools.copex.common.Hypothesis;
import eu.scy.client.tools.copex.common.MaterialProc;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Marjolaine
 */
public class CopexTreeModel extends DefaultTreeModel{
    private ExperimentalProcedure proc;
    /* liste des taches */
    private List<CopexTask> listTask;
    private CopexNode manipChild;
    private CopexTask fictivTask;

    public CopexTreeModel(ExperimentalProcedure proc) {
        super(new CopexNode(proc.getQuestion()));
        this.proc = proc;
        this.listTask = proc.getListTask();
        this.fictivTask = proc.getQuestion();
        buildTree();
    }

    // constructeur d'un sous arbre
    public CopexTreeModel(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, CopexTask fictivTask){
        super(new CopexNode(proc.getQuestion()));
        this.proc = proc;
        this.listTask = listTask;
        this.fictivTask = fictivTask;
        buildTree();

    }
     /* construction de l'arbre */
    private void buildTree(){
        CopexNode rootNode = (CopexNode)root;
        // question
        insertNodeInto(new CopexNode(proc.getQuestion()), rootNode, rootNode.getChildCount());
        // hypothesis
        Hypothesis hyp = proc.getHypothesis();
        if(hyp != null && !hyp.isHide() || proc.getHypothesisMode() == MyConstants.MODE_MENU_TREE){
            CopexNode hypChild = new CopexNode(hyp);
            insertNodeInto(hypChild, rootNode, rootNode.getChildCount());
        }
        // general principle
        GeneralPrinciple p = proc.getGeneralPrinciple();
        if(p != null && !p.isHide() || proc.getPrincipleMode() == MyConstants.MODE_MENU_TREE){
            CopexNode princChild = new CopexNode(p);
            insertNodeInto(princChild, rootNode, rootNode.getChildCount());
        }
        // materials
        MaterialProc m = proc.getMaterials();
        if(m != null && proc.hasMaterial()){
            CopexNode matChild = new CopexNode(m);
            insertNodeInto(matChild, rootNode, rootNode.getChildCount());
        }
        // manipulation
        manipChild = new CopexNode(proc.getManipulation());
        insertNodeInto(manipChild, rootNode, rootNode.getChildCount());
        buildTaskTree(manipChild, listTask);
        // datasheet
        DataSheet ds = proc.getDataSheet();
        if(ds!= null){
            CopexNode dsChild = new CopexNode(ds);
            insertNodeInto(dsChild, rootNode, rootNode.getChildCount());
        }
        // evaluation
        Evaluation eval = proc.getEvaluation();
        if(eval != null && !eval.isHide() || proc.getEvaluationMode() == MyConstants.MODE_MENU_TREE){
            CopexNode evalChild = new CopexNode(eval);
            insertNodeInto(evalChild, rootNode, rootNode.getChildCount());
        }
    }

    public CopexNode getManipulationNode(){
        return this.manipChild;
    }
    /* construction de l'arbredes taches */
    private void buildTaskTree(CopexNode node, List<CopexTask> listT){
        CopexTask task = node.getTask();
        if(task == null){
            task = fictivTask;
        }
       CopexTask[] tabTask = getBrotherAndChild(task, listT);
       CopexTask tB = tabTask[0];
       CopexTask tC = tabTask[1];
       if (tC != null){
           TaskTreeNode newChild = new TaskTreeNode(tC);
           insertNodeInto(newChild, node , node.getChildCount());
           buildTaskTree(newChild, listT);
       }
       if (tB != null){
           TaskTreeNode newBrother = new TaskTreeNode(tB);
           insertNodeInto(newBrother, (CopexNode)node.getParent() , node.getParent().getChildCount());
           buildTaskTree(newBrother, listT);
       }
    }

    /* renvoit la tache frere et enfant , null sinon */
    private CopexTask[] getBrotherAndChild(CopexTask task, List<CopexTask> listT){
        int nbT = listT.size();
        CopexTask[] tabTask = new CopexTask[2];
        long dbKeyB = task.getDbKeyBrother();
        long dbKeyC = task.getDbKeyChild();
        CopexTask tB = null;
        CopexTask tC = null;
        if (dbKeyB != -1 || dbKeyC != -1){
            for (int i=0;i<nbT;i++){
                CopexTask t = listT.get(i);
                if (t.getDbKey() == dbKeyB)
                    tB = t;
                else if (t.getDbKey() == dbKeyC)
                    tC = t;
            }
        }
        tabTask[0] = tB;
        tabTask[1] = tC;
        return tabTask;
    }

    public void updateProc(ExperimentalProcedure newProc){
        this.proc = newProc;
        this.listTask = proc.getListTask();
        updateNode((CopexNode)root);
    }
    
    private void updateNode(CopexNode node){
        int nbC = node.getChildCount();
        // recherche de la tache
        if(node.isQuestion()){
            node.setNode(proc.getQuestion());
        }else if(node.isHypothesis()){
            node.setNode(proc.getHypothesis());
        }else if(node.isGeneralPrinciple()){
            node.setNode(proc.getGeneralPrinciple());
        }else if(node.isMaterial()){
            node.setNode(proc.getMaterials());
        }else if(node.isManipulation()){
            node.setNode(proc.getManipulation());
        }else if (node.isDatasheet()){
            node.setNode(proc.getDataSheet());
        }else if (node.isEvaluation()){
            node.setNode(proc.getEvaluation());
        }else if(node instanceof TaskTreeNode){
            CopexTask task = getTask((TaskTreeNode)node);
            if (task != null){
                ((TaskTreeNode)node).getTask().setDbKeyBrother(task.getDbKeyBrother());
                ((TaskTreeNode)node).getTask().setDbKeyChild(task.getDbKeyChild());
            }
        }
        for (int k=0; k<nbC; k++){
            updateNode((CopexNode)node.getChildAt(k));
        }
    }


    public CopexNode getQuestionNode(){
        int nbC = ((CopexNode)root).getChildCount();
        for (int i=0; i<nbC; i++){
            if (((CopexNode)((CopexNode)root).getChildAt(i)).isQuestion())
                return ((CopexNode)((CopexNode)root).getChildAt(i));
        }
        return null;
    }

    public void setHypothesis(Hypothesis hypothesis){
        if(hypothesis == null || hypothesis.isHide()){
            CopexNode n = getHypothesisNode();
            if(n != null)
                removeNodeFromParent(n);
        }else if(getHypothesisNode() == null){
            CopexNode rootNode = (CopexNode)root;
            CopexNode hypChild = new CopexNode(hypothesis);
            //insertNodeInto(hypChild, rootNode, 0);
            insertNodeInto(hypChild, rootNode, 1);
        }
    }

    public CopexNode getHypothesisNode(){
//        if(((CopexNode)((CopexNode)root).getChildAt(0)).isHypothesis())
//            return ((CopexNode)((CopexNode)root).getChildAt(0));
//        return null;
        int nbC = ((CopexNode)root).getChildCount();
        for (int i=0; i<nbC; i++){
            if (((CopexNode)((CopexNode)root).getChildAt(i)).isHypothesis())
                return ((CopexNode)((CopexNode)root).getChildAt(i));
        }
        return null;
    }

    public void setGeneralPrinciple(GeneralPrinciple principle){
        CopexNode node = getGeneralPrincipleNode();
        if(node != null && (principle == null || principle.isHide())){
            removeNodeFromParent(getGeneralPrincipleNode());
        }else if (node == null && principle != null && !principle.isHide()){
            CopexNode rootNode = (CopexNode)root;
            CopexNode princChild = new CopexNode(principle);
            int id = 2;
            if(getHypothesisNode() == null)
                id = 1;
            insertNodeInto(princChild, rootNode, id);
        }
    }

    public CopexNode getGeneralPrincipleNode(){
        int nbC = ((CopexNode)root).getChildCount();
        for (int i=0; i<nbC; i++){
            if (((CopexNode)((CopexNode)root).getChildAt(i)).isGeneralPrinciple())
                return ((CopexNode)((CopexNode)root).getChildAt(i));
        }
        return null;
    }

    public void setEvaluation(Evaluation evaluation){
        CopexNode node = getEvaluationNode();
        if(node != null && (evaluation == null || evaluation.isHide())){
            removeNodeFromParent(getEvaluationNode());
        }else if (node == null && (evaluation !=null && !evaluation.isHide())){
            CopexNode rootNode = (CopexNode)root;
            CopexNode evalChild = new CopexNode(evaluation);
            insertNodeInto(evalChild, rootNode, root.getChildCount());
        }
    }

    public CopexNode getEvaluationNode(){
        int nbC = ((CopexNode)root).getChildCount();
        for (int i=0; i<nbC; i++){
            if (((CopexNode)((CopexNode)root).getChildAt(i)).isEvaluation())
                return ((CopexNode)((CopexNode)root).getChildAt(i));
        }
        return null;
    }

    public void setDatasheet(DataSheet datasheet){
        CopexNode node = getDatasheetNode();
        if(datasheet == null && node != null ){
            removeNodeFromParent(node);
        }else if(datasheet != null){
            if (node == null){
                CopexNode rootNode = (CopexNode)root;
                CopexNode dataChild = new CopexNode(datasheet);
                int id = root.getChildCount() ;
                if(getEvaluationNode() != null)
                    id--;
                insertNodeInto(dataChild, rootNode, id);
            }else{
                node.setNode(datasheet);
                nodeChanged(node);
            }
        }
    }

    private CopexNode getDatasheetNode(){
        int nbC = ((CopexNode)root).getChildCount();
        for (int i=0; i<nbC; i++){
            if (((CopexNode)((CopexNode)root).getChildAt(i)).isDatasheet())
                return ((CopexNode)((CopexNode)root).getChildAt(i));
        }
        return null;
    }

    /* ajout de noeuds a partir d'un noeud donne*/
    public void addNodes(TaskTreeNode node, ArrayList<CopexTask> listTask){
        buildTaskTree(node, listTask);
    }

    

    private CopexTask getTask(TaskTreeNode node){
        int listTaskSize = listTask.size();
        for (int i=0; i<listTaskSize; i++){
            CopexTask t = listTask.get(i);
            if (t.getDbKey() == node.getTask().getDbKey())
                return t;
        }
        return null;
    }

    
}
