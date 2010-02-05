/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.ActionParam;
import eu.scy.tools.copex.common.ActionParamData;
import eu.scy.tools.copex.common.ActionParamMaterial;
import eu.scy.tools.copex.common.CopexAction;
import eu.scy.tools.copex.common.CopexActionAcquisition;
import eu.scy.tools.copex.common.CopexActionChoice;
import eu.scy.tools.copex.common.CopexActionManipulation;
import eu.scy.tools.copex.common.CopexActionNamed;
import eu.scy.tools.copex.common.CopexActionParam;
import eu.scy.tools.copex.common.CopexActionTreatment;
import eu.scy.tools.copex.common.CopexTask;
import eu.scy.tools.copex.common.DataSheet;
import eu.scy.tools.copex.common.Evaluation;
import eu.scy.tools.copex.common.GeneralPrinciple;
import eu.scy.tools.copex.common.Hypothesis;
import eu.scy.tools.copex.common.InitialNamedAction;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.MaterialProc;
import eu.scy.tools.copex.common.QData;
import eu.scy.tools.copex.common.Question;
import eu.scy.tools.copex.common.Step;
import eu.scy.tools.copex.dnd.SubTree;
import eu.scy.tools.copex.dnd.TreeTransferHandler;
import eu.scy.tools.copex.undoRedo.AddTaskUndoRedo;
import eu.scy.tools.copex.undoRedo.CopexUndoManager;
import eu.scy.tools.copex.undoRedo.CutUndoRedo;
import eu.scy.tools.copex.undoRedo.DeleteTaskUndoRedo;
import eu.scy.tools.copex.undoRedo.DragAndDropUndoRedo;
import eu.scy.tools.copex.undoRedo.EvaluationUndoRedo;
import eu.scy.tools.copex.undoRedo.GeneralPrincipleUndoRedo;
import eu.scy.tools.copex.undoRedo.HypothesisUndoRedo;
import eu.scy.tools.copex.undoRedo.PasteUndoRedo;
import eu.scy.tools.copex.undoRedo.UpdateProcNameUndoRedo;
import eu.scy.tools.copex.undoRedo.UpdateTaskUndoRedo;
import eu.scy.tools.copex.utilities.CopexCellEditor;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexTreeCellRenderer;
import eu.scy.tools.copex.utilities.CopexTreeSelectionListener;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyBasicTreeUI;
import eu.scy.tools.copex.utilities.MyConstants;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.undo.CannotUndoException;
import org.jdom.Element;

/**
 * copex tree
 * @author Marjolaine
 */
public class CopexTree extends JTree implements MouseListener, KeyListener{
    private CopexTreeModel copexTreeModel;
    private CopexTreeCellRenderer copexTreeCellRenderer;
    private CopexCellEditor copexCellEditor;
    private LearnerProcedure proc;
    private EdPPanel owner;
    private CopexTreeSelectionListener selectionListener;

    // mode affichage commentaire
    private char displayComm = MyConstants.COMMENTS;
    /* pop up menu */
    private CopexPopUpMenu popUpMenu = null;
    /* Trandfer Handler */
    private TreeTransferHandler transferHandler;
    /* liste des noeuds visibles a un moment donne */
    private ArrayList<TaskTreeNode> listVisibleNode;
    /* gestionnaire de undo redo */
    private CopexUndoManager undoManager;
    /*marquage du undo redo */
    private SubTree subTree;
    private TaskSelected newTs;
    private TaskSelected oldTs;
    /*enregistrement etat taches*/
    private boolean register;

    public CopexTree(EdPPanel owner, LearnerProcedure proc) {
        this.proc = proc;
        this.owner = owner;
        // creation du model
        this.copexTreeModel = new CopexTreeModel(proc);
        this.setModel(copexTreeModel);
        // renderer
        copexTreeCellRenderer = new CopexTreeCellRenderer(this);
        this.setCellRenderer(copexTreeCellRenderer);
        copexCellEditor = new CopexCellEditor(this);
        this.setCellEditor(copexCellEditor);
        //event
        // ecoute des evenements souris
        this.selectionListener = new CopexTreeSelectionListener (this);
        addTreeSelectionListener(selectionListener);
        // param of the tree
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        setShowsRootHandles(false);
        setRootVisible(false);
        this.setRowHeight(0);
        this.setUI(new MyBasicTreeUI(this));
        this.setEditable(true);
        if(proc.getRight() == MyConstants.NONE_RIGHT)
            setEditable(false);
        setToggleClickCount(0);
        this.addMouseListener(this);
        this.addKeyListener(this);
        // DRAG AND DROP
        setDragEnabled(true);
        transferHandler  = new TreeTransferHandler();
        setTransferHandler(transferHandler);
        //setDropMode(DropMode.INSERT);
        setDropMode(DropMode.ON_OR_INSERT);
        ToolTipManager.sharedInstance().registerComponent(this);
        undoManager = new CopexUndoManager(this);
        int level = getLevelTree();
        displayLevel(level);
        resizeWidth();
    }


    public LearnerProcedure getProc(){
        return this.proc;
    }
   
    public ImageIcon getQuestionImageIcon(){
        return this.owner.getCopexImage("icone_AdT_question.png");
    }
    public ImageIcon getHypothesisImageIcon(){
        return this.owner.getCopexImage("icone_AdT_hypothese.png");
    }

    public ImageIcon getPrincipleImageIcon(){
        return this.owner.getCopexImage("icone_AdT_principe.png");
    }

    public ImageIcon getMaterialImageIcon(){
        return this.owner.getCopexImage("icone_AdT_material.png");
    }

    public ImageIcon getManipulationImageIcon(){
        if(proc.isTaskProc()){
            return this.owner.getCopexImage("icone_AdT_manip_tasks.png");
        }else{
            return this.owner.getCopexImage("icone_AdT_manip.png");
        }
    }

    public ImageIcon getDatasheetImageIcon(){
        return this.owner.getCopexImage("icone_AdT_datasheet.png");
    }

    public ImageIcon getEvaluationImageIcon(){
        return this.owner.getCopexImage("icone_AdT_eval.png");
    }

    public ImageIcon getActionImageIcon(){
        return this.owner.getCopexImage("Icone-AdT_action.png");
    }
      public ImageIcon getStepImageIcon(){
        if(proc.isTaskProc()){
            return this.owner.getCopexImage("Icone-AdT_tache.png");
        }else{
            return this.owner.getCopexImage("Icone-AdT_etape.png");
        }
    }
    public ImageIcon getStepWarningImageIcon(){
        return this.owner.getCopexImage("Icone-AdT_etape_warn.png");
    }
    public ImageIcon getStepReadOnlyImageIcon(){
        if(proc.isTaskProc()){
            return this.owner.getCopexImage("Icone-AdT_tache_lock.png");
        }else{
            return this.owner.getCopexImage("Icone-AdT_etape_lock.png");
        }
    }

    public ImageIcon getActionReadOnlyImageIcon(){
        return this.owner.getCopexImage("Icone-AdT_action_lock.png");
    }

    public boolean isTaskProc(){
        return this.proc.isTaskProc();
    }
   // retourne le texte d'un objet
   public String getDescriptionValue(Object value){
       if (value instanceof CopexNode ){
            CopexNode node =  (CopexNode)value;
            if (node == null)
                    return "";
            if(node.isQuestion()){
                return ((Question)node.getNode()).getDescription(owner.getLocale());
            }else if(node.isHypothesis()){
                return ((Hypothesis)node.getNode()).getHypothesis(owner.getLocale());
            }else if(node.isGeneralPrinciple()){
                return ((GeneralPrinciple)node.getNode()).getPrinciple(owner.getLocale());
            }else if(node.isMaterial()){
                //return ((MaterialProc)node.getNode()).toTreeString(owner.getLocale());
                return null;
            }else if(node.isManipulation()){
                return null;
            }else if(node.isDatasheet()){
                return ((DataSheet)node.getNode()).toTreeString(owner.getLocale());
            }else if(node.isEvaluation()){
                return ((Evaluation)node.getNode()).getEvaluation(owner.getLocale());
            }
            if (value instanceof TaskTreeNode ){
                TaskTreeNode tnode =  (TaskTreeNode)value;
                if (tnode == null || tnode.getTask() == null)
                    return "";
                if (tnode.getTask() instanceof CopexAction ){
                    return ((CopexAction)tnode.getTask()).toDescription(owner.getCopexPanel()) ;
                }else
                    return tnode.getTask().getDescription(owner.getLocale()) ;
            }
            return "";
       }else
            return "";
   }

    public String getDefaultDescriptionValue(Object value){
        if (value instanceof CopexNode ){
            CopexNode node =  (CopexNode)value;
            if (node == null)
                    return null;
            if(node.isQuestion()){
                String d = ((Question)node.getNode()).getDescription(owner.getLocale());
                if(d==null || d.length()==0){
                    d = owner.getBundleString("MSG_QUESTION");
                }
                return d;
            }else if(node.isHypothesis()){
                String h = ((Hypothesis)node.getNode()).getHypothesis(owner.getLocale());
                if(h == null || h.length()==0){
                    h = owner.getBundleString("DEFAULT_TEXT_HYPOTHESIS");
                }
                return h;
            }else if(node.isGeneralPrinciple()){
                String p = ((GeneralPrinciple)node.getNode()).getPrinciple(owner.getLocale());
                if(p == null || p.length()==0){
                    p = owner.getBundleString("DEFAULT_TEXT_PRINCIPLE");
                }
                return p;
            }
        }
        return null;
    }

    public List<String> getMaterialValue(Object value){
        if (value instanceof CopexNode ){
            CopexNode node =  (CopexNode)value;
            if (node == null)
                    return null;
            if(node.isMaterial()){
                return ((MaterialProc)node.getNode()).getListTree(owner.getLocale());
            }
        }
        return null;
    }


    public String getToolTipTextValue(Object value){
        if (value instanceof CopexNode ){
            CopexNode node =  (CopexNode)value;
            if (node == null)
                    return null;
            if(node.isQuestion()){
                String d = ((Question)node.getNode()).getDescription(owner.getLocale());
                if(d==null || d.length()==0){
                    return  owner.getBundleString("MSG_QUESTION");
                }
            }else if(node.isHypothesis()){
                String h = ((Hypothesis)node.getNode()).getHypothesis(owner.getLocale());
                if(h == null || h.length()==0){
                    return owner.getBundleString("DEFAULT_TEXT_HYPOTHESIS");
                }
            }else if(node.isGeneralPrinciple()){
                String p = ((GeneralPrinciple)node.getNode()).getPrinciple(owner.getLocale());
                if(p == null || p.length()==0){
                    return owner.getBundleString("DEFAULT_TEXT_PRINCIPLE");
                }
            }
        }
        return null;
    }


    // retourne en mode edition le mot commentaire
    public String getCommentLabelText(){
        return owner.getBundleString("LABEL_COMMENTS");
    }
   // retourne le commentaire
   public String getCommentValue(Object value){
       if (this.displayComm == MyConstants.NO_COMMENTS)
           return "";
       if (value instanceof CopexNode ){
            CopexNode node =  (CopexNode)value;
            if (node == null)
                    return "";
            if(node.isQuestion()){
                String c = ((Question)node.getNode()).getComments(owner.getLocale());
                if(c==null)
                    c = "";
                return c;
            }
            if (value instanceof TaskTreeNode ){
                TaskTreeNode tnode =  (TaskTreeNode)value;
                if (tnode == null || tnode.getTask() == null)
                    return "";
                return tnode.getTask().getComments(owner.getLocale());
            }
            if(node.isHypothesis())
                return ((Hypothesis)node.getNode()).getComment(owner.getLocale());
            if(node.isGeneralPrinciple())
                return ((GeneralPrinciple)node.getNode()).getComment(owner.getLocale());
            if(node.isEvaluation())
                return ((Evaluation)node.getNode()).getComment(owner.getLocale());
            return "";
       }else
            return "";
   }

   
   // retourne vrai si possibilite d'editer direct dans l'arbre
   public boolean isEditableValue(Object value){
       if(proc.getRight() == MyConstants.NONE_RIGHT)
           return false;
       if (value instanceof CopexNode ){
            CopexNode node =  (CopexNode)value;
            if (node == null)
                    return false;
            if(node.isQuestion()){
                return true;
            }else if(node.isHypothesis()){
                return true;
            }else if(node.isGeneralPrinciple()){
                return proc.getInitialProc().isDrawPrinciple()?false:true;
            }else if(node.isMaterial()){
                return false;
            }else if(node.isManipulation()){
                return false;
            }else if(node.isDatasheet()){
                return false;
            }else if(node.isEvaluation()){
                return true;
            }
            return false;
       }else
            return false;
   }

   // retourne l'intitule dans l'arbre
   public String getIntituleValue(Object value){
       if (value instanceof CopexNode ){
            CopexNode node =  (CopexNode)value;
            if (node == null)
                    return "";
            if(node.isQuestion()){
                return owner.getBundleString("TREE_QUESTION");
            }else if(node.isHypothesis()){
                return owner.getBundleString("TREE_HYPOTHESIS");
            }else if(node.isGeneralPrinciple()){
                return owner.getBundleString("TREE_GENERAL_PRINCIPLE");
            }else if(node.isMaterial()){
                return owner.getBundleString("TREE_MATERIAL");
            }else if(node.isManipulation()){
                return owner.getBundleString("TREE_MANIPULATION");
            }else if(node.isDatasheet()){
                return owner.getBundleString("TREE_DATASHEET");
            }else if(node.isEvaluation()){
                return owner.getBundleString("TREE_EVALUATION");
            }
            return "";
       }else
            return "";
   }

    public String getBundleString(String s){
        return owner.getBundleString(s);
    }
   /* retourne le tool tip text pour un noeud selon les droits dur la tache */
     public String getToolTipText(TaskTreeNode node){
         String msg = "";
         if (!node.canEdit())
             msg += getBundleString("TOOLTIPTEXT_TASK_EDIT")+" ";
         if (!node.canDelete())
             msg += getBundleString("TOOLTIPTEXT_TASK_DELETE")+" ";
         if (!node.canCopy())
             msg += getBundleString("TOOLTIPTEXT_TASK_COPY")+" ";
         if (!node.canMove())
             msg += getBundleString("TOOLTIPTEXT_TASK_MOVE")+" ";
         if (!node.canBeParent())
             msg += getBundleString("TOOLTIPTEX_TASK_ADD")+" ";
         return msg;
     }

     // retourne le nombre de repetition d'une tache
   public int getNbRepeat(Object value){
       if (value instanceof TaskTreeNode ){
            TaskTreeNode node =  (TaskTreeNode)value;
            if (node == null || node.getTask() == null  )
                    return 0;
            if (node.getTask().getTaskRepeat() == null)
                return 1;
            else
                return node.getTask().getTaskRepeat().getNbRepeat();
       }else
        return 0;
   }
   
   // retourne l'image d'un objet
   public ImageIcon getImageValue(Object value){
      if (value instanceof TaskTreeNode ){
            TaskTreeNode node =  (TaskTreeNode)value;
            if (node == null || node.getTask() == null || node.getTask().getTaskImage() == null  )
                    return null;
            Image img = null;
            if (node.getTask().getTaskImage() != null && !node.getTask().getTaskImage().equals("")){
                img = this.owner.getTaskImage("mini_"+node.getTask().getTaskImage()) ;
            }
            if (img == null)
                return null ;
            return new ImageIcon(img);

       }else
        return null;
   }

    // retourne le dessin d'un objet
   public Element getTaskDraw(Object value){
      if (value instanceof TaskTreeNode ){
            TaskTreeNode node =  (TaskTreeNode)value;
            if (node == null || node.getTask() == null  )
                    return null;
            return node.getTask().getDraw() ;
       }else
        return null;
   }

   
   /* retourne la place disponible pour l'arbre */
    public int getTextWidth(Object value, int row){
        int totalWidth = this.owner.getWidth()-10 ;
        // selon le niveau dans l'arbre la place disponible n'est pas la meme
        if (value instanceof CopexNode ){
            CopexNode node =  (CopexNode)value;
            if (node == null )
                    return totalWidth;
            int level = node.getLevel() ;
            if (level == 0)
                return totalWidth - 45;
            return totalWidth - (level*40);
        }
        return totalWidth ;
    }

    public void setHypothesis(Hypothesis hypothesis){
        proc.setHypothesis(hypothesis);
        copexTreeModel.setHypothesis(hypothesis);
        resizeWidth();
    }

    

    public void setGeneralPrinciple(GeneralPrinciple principle){
        proc.setGeneralPrinciple(principle);
        copexTreeModel.setGeneralPrinciple(principle);
        resizeWidth();
    }

    public void setEvaluation(Evaluation evaluation){
        proc.setEvaluation(evaluation);
        copexTreeModel.setEvaluation(evaluation);
        resizeWidth();
    }

    public void setDatasheet(DataSheet datasheet){
        copexTreeModel.setDatasheet(datasheet);
        proc.setDataSheet(datasheet);
        resizeWidth();
    }
    public void addEdit_hypothesis(Hypothesis oldHypothesis, Hypothesis newHypothesis){
        undoManager.addEdit(new HypothesisUndoRedo(owner, owner.getController(), this, proc, oldHypothesis, newHypothesis));
    }
    public void addEdit_principle(GeneralPrinciple oldPrinciple, GeneralPrinciple newPrinciple){
        undoManager.addEdit(new GeneralPrincipleUndoRedo(owner, owner.getController(), this, proc, oldPrinciple, newPrinciple));
    }
    public void addEdit_evaluation(Evaluation oldEvaluation, Evaluation newEvaluation){
        undoManager.addEdit(new EvaluationUndoRedo(owner, owner.getController(), this, proc, oldEvaluation, newEvaluation));
    }

    @Override
    public boolean isPathEditable(TreePath path){
        CopexNode selNode = getSelectedNode();
        if (selNode == null)
            return false;
        if(selNode.isQuestion() || selNode.isHypothesis() || selNode.isGeneralPrinciple() || selNode.isEvaluation()){
            return true;
        }
        return false;
    }
    /* retourne le noeud selectionne */
   public CopexNode getSelectedNode(){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 || tabPaths.length > 1)
          return null;
      TreePath mypath = tabPaths[0];
      return  (CopexNode) mypath.getLastPathComponent();
   }

   /* retourne le noeud selectionne */
   public TaskTreeNode getTaskSelectedNode(){
      CopexNode node = getSelectedNode();
      if(node instanceof TaskTreeNode)
          return (TaskTreeNode)node;
      return null;
   }

    @Override
    public void mouseClicked(MouseEvent e) {
        // double clic : on ouvre la fenetre correspondante
        if (e.getClickCount() == 2){
            int row = getRowForLocation(e.getX(), e.getY());
            if (row == -1)
                return;
            edit();
           return;
        }
        if (SwingUtilities.isRightMouseButton(e)){
            popUpMenu = null;
            int x = e.getPoint().x;
            int y = e.getPoint().y;
            if (!isElementSelTaskTree())
               return;
            getPopUpMenu(getSelectedTasksSimple());
            boolean alone =  isOnlyOneElementIsSel();

            popUpMenu.setInsertAfterEnabled(canAddAfter()) ;
            popUpMenu.setInsertInEnabled(canAddIn());
            popUpMenu.setCutItemEnabled(canCut());
            popUpMenu.setCopyItemEnabled(canCopy());
            popUpMenu.setPasteItemEnabled(canPaste());
            popUpMenu.setSupprItemEnabled(canSuppr());
            popUpMenu.setEditItemEnabled(alone);
            this.popUpMenu.show(this, x, y);

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void selectNode(){
        this.owner.updateMenu();
    }
    public void edit(){
        CopexNode currentNode = getSelectedNode();
        if (currentNode == null)
                return;
//        if (currentNode.isQuestion()){
//            editQuestion();
//        }else if(currentNode.isMaterial()){
        if(currentNode.isMaterial()){
            editMaterial();
        }else if(currentNode instanceof TaskTreeNode){
            if (((TaskTreeNode)currentNode).isAction()){
                editAction(((TaskTreeNode)currentNode));
            }else if (((TaskTreeNode)currentNode).isStep()){
                editStep(((TaskTreeNode)currentNode));
            }
        //}else if(currentNode.isManipulation() && currentNode.getChildCount() == 0){
          }else if(currentNode.isManipulation() ){
            openHelpManipulationDialog();
        }else if(currentNode.isManipulation() && getLevelTreeDisplay() <2 && currentNode.getChildCount() > 0)
            displayLevel(2);
    }

    private void openHelpManipulationDialog(){
        HelpManipulationDialog helpD = new HelpManipulationDialog(owner, proc.isTaskProc(), !proc.isValidQuestion(owner.getLocale()));
        helpD.setVisible(true);
    }


    

     public void setNodeText(CopexNode node, String txt, String comment){
         if(node != null){
             if(node.isHypothesis()){
                 if(txt.equals(owner.getBundleString("DEFAULT_TEXT_HYPOTHESIS"))){
                     txt = "";
                 }
                 String newHyp = this.owner.updateHypothesis((Hypothesis)node.getNode(), txt, comment);
                 ((Hypothesis)node.getNode()).setHypothesis(CopexUtilities.getTextLocal(newHyp,owner.getLocale()));
                 copexTreeModel.nodeChanged(node);
             }else if(node.isGeneralPrinciple()){
                 if(txt.equals(owner.getBundleString("DEFAULT_TEXT_PRINCIPLE"))){
                     txt = "";
                 }
                 String newPrinc = this.owner.updateGeneralPrinciple((GeneralPrinciple)node.getNode(), txt, comment);
                 ((GeneralPrinciple)node.getNode()).setPrinciple(CopexUtilities.getTextLocal(newPrinc,owner.getLocale()));
                 copexTreeModel.nodeChanged(node);
             }else if(node.isEvaluation()){
                 String newEval = this.owner.updateEvaluation((Evaluation)node.getNode(), txt, comment);
                 ((Evaluation)node.getNode()).setEvaluation(CopexUtilities.getTextLocal(newEval,owner.getLocale()));
                 copexTreeModel.nodeChanged(node);
             }else if(node.isQuestion()){
                 if(txt.equals(owner.getBundleString("MSG_QUESTION"))){
                     txt = "";
                 }
                 String newQuestion = this.owner.updateQuestion((Question)node.getNode(), txt, comment);
                 ((Question)node.getNode()).setDescription(CopexUtilities.getTextLocal(newQuestion,owner.getLocale()));
                 copexTreeModel.nodeChanged(node);
             }
         }
         repaint();
     }

    

    

     public void updateProc(LearnerProcedure p){
         this.proc = p;
         this.copexTreeModel.updateProc(p);
         resizeWidth();
     }
     public void updateProc(LearnerProcedure p, boolean update){
         this.proc = p;
         this.copexTreeModel.updateProc(p);
     }

     public void updateQuestion(Question q){
         proc.setQuestion(q);
         resizeWidth();
     }

     
     /* affichage ou non des commentaires */
    public void setComments(char displayC){
        markDisplay();
        this.displayComm = displayC;
        copexTreeModel.reload();
        //this.revalidate();
        displayTree();
        this.repaint();
    }


    /* marque l'affichage des chemins */
    public void markDisplay(){
       // System.out.println("*****");
        listVisibleNode = getListVisibleNode();
        //System.out.println("*****");
    }

    /* retourne la liste des noeuds visibles */
    private ArrayList<TaskTreeNode> getListVisibleNode(){
       ArrayList<TaskTreeNode> listNodes = new ArrayList();
       int nb = getRowCount();
       for (int i=0; i<nb; i++){
           TreePath path = getPathForRow(i);
           if (path != null){
               CopexNode n = (CopexNode)path.getLastPathComponent() ;
               //System.out.println("noeud visible : "+n.getTask().getDescription());
               if(n instanceof TaskTreeNode)
                    listNodes.add((TaskTreeNode)n);
           }
       }
        return listNodes;
    }


    /* affichage arbre dans etat dans lequel il etait */
    public void displayTree(){
        if (listVisibleNode == null)
            return;
        int nbT = listVisibleNode.size();
        for (int i=0; i<nbT; i++){
            this.scrollPathToVisible(new TreePath(listVisibleNode.get(i).getPath()));
        }
    }

    /*
     * l'ajout d'une etape est il possible ?
     * si 1 et 1 seul element est selectionne
     */
    public boolean canAddE(){
        if (isOnlyOneElementIsSel()){
            // controle que cet element peut avoir des enfants
            TaskTreeNode selNode = getTaskSelectedNode();
            if (selNode != null && canAdd(selNode))
                 return true;
        }
        return canAddQuestion();
    }
    
    public boolean canAddAfter(){
        if (isOnlyOneElementIsSel()){
            // controle que cet element peut avoir des enfants
            TaskTreeNode selNode = getTaskSelectedNode();
            if (selNode != null ){
                CopexNode parentNode = (CopexNode)(selNode.getParent());
                if(parentNode != null)
                    if(parentNode.isManipulation()){
                        return proc.getQuestion().getParentRight() == MyConstants.EXECUTE_RIGHT;

                    }else
                        return canAdd(parentNode);
            }
        }
        return canAddQuestion();
    }
    public boolean canAddIn(){
        if (isOnlyOneElementIsSel()){
            // controle que cet element peut avoir des enfants
            TaskTreeNode selNode = getTaskSelectedNode();
            if (selNode != null && canAdd(selNode))
                 return true;
        }
        return canAddQuestion();
    }

    /*retourne vrai si mani data est sel et que la question peut avoir des enfants*/
    public boolean canAddQuestion(){
        CopexNode node = getSelectedNode();
        if(node != null && node.isManipulation()){
             Question q = proc.getQuestion();
             if(q.getParentRight() == MyConstants.EXECUTE_RIGHT)
                 return true;
        }
        return false;
    }
    /*
     * l'ajout d'une action est il possible ?
     * si 1 et 1 seul element est selectionne
     */
    public boolean canAddA(){
        if (isOnlyOneElementIsSel()){
            // controle que cet element peut avoir des enfants
            TaskTreeNode selNode = getTaskSelectedNode();
            if (selNode != null && canAdd(selNode) )
                 return true;
        }
        return canAddQuestion();
    }

    /* retourne vrai si le noeud peut avoir des enfans ou si c'est une action, si le noeud parent peut avoir des enfants */
    private boolean canAdd(CopexNode node){
        if(node == null)
            return false;
        if (!node.isAction() && node.canBeParent())
            return true;
        else if (node.isAction()){
            CopexNode parentNode = (CopexNode)node.getParent();
            if (parentNode != null && parentNode.canBeParent())
                return true;
            if(parentNode.isManipulation()){
                return proc.getQuestion().getParentRight() == MyConstants.EXECUTE_RIGHT;
            }
        }
        return false;
    }
    /*
     * undo possible
     *
     */
    public boolean canUndo(){
        return undoManager.canUndo();
    }

    /*
     * redo possible
     *
     */
    public boolean canRedo(){
        return undoManager.canRedo();
    }

    /*
     * copie possible
     *
     */
    public boolean canCopy(){
        return isElementsSel() && selIsSubTree() && selCanBeCopy();
    }
    /*
     * coller possible
     *
     */
    public boolean canPaste(){
        return isOnlyOneElementIsSel() && this.owner.canPaste()  && (canAddA() || canAddE()) &&
                ((this.owner.getSubTreeCopy().isQuestion() && getSelectedNode().isQuestion() ) ||
                (!this.owner.getSubTreeCopy().isQuestion())) && canPasteFromAnotherProc();
    }
    /* retourne vrai si meme proc*/
    private boolean canPasteFromAnotherProc(){
        return this.owner.getSubTreeCopy().getOwner().getProc().getDbKey() == this.proc.getDbKey() ;
    }
    /*
     * suppr possible
     *
     */
    public boolean canSuppr(){
        return isElementsSel() && taskSelected() && selCanBeDelete() && !areActionSelLinkedByMaterial() && !areActionSelLinkedByData();
    }

    private boolean taskSelected(){
        ArrayList<CopexNode> listNode = getSelectedNodes();
        if(listNode == null)
            return false;
        for(Iterator<CopexNode> n = listNode.iterator();n.hasNext();){
            if(!(n.next() instanceof TaskTreeNode)){
                return false;
            }
        }
        return true;
    }


    /*
     * cut possible
     *
     */
    public boolean canCut(){
        return canSuppr() && canCopy();
    }

    /* retourne vrai s'il au moins une tache a un commentaires */
    public boolean isComments(){
        return isComments((CopexNode)copexTreeModel.getRoot());
    }

    private boolean  isComments(CopexNode node){
        if(node.isQuestion()){
            if(((Question)node.getNode()).getComments(owner.getLocale()) != null && !((Question)node.getNode()).getComments(owner.getLocale()).equals(""))
                return true;
        }
        else if(node.isHypothesis()){
            if(((Hypothesis)node.getNode()).getComment(owner.getLocale()) != null && !((Hypothesis)node.getNode()).getComment(owner.getLocale()).equals(""))
                return true;
        }
        else if(node.isGeneralPrinciple()){
            if(((GeneralPrinciple)node.getNode()).getComment(owner.getLocale()) != null && !((GeneralPrinciple)node.getNode()).getComment(owner.getLocale()).equals(""))
                return true;
        }
        else if(node.isEvaluation()){
            if(((Evaluation)node.getNode()).getComment(owner.getLocale()) != null && !((Evaluation)node.getNode()).getComment(owner.getLocale()).equals(""))
                return true;
        }
       if (node instanceof TaskTreeNode && ((TaskTreeNode)node).getTask().getComments(owner.getLocale()) != null && !((TaskTreeNode)node).getTask().getComments(owner.getLocale()).equals(""))
           return true;
        int nbC = copexTreeModel.getChildCount(node);
       if (nbC > 0){
           for (int i=0; i<nbC; i++){
               boolean isC = isComments((CopexNode)node.getChildAt(i));
               if (isC)
                   return true;
           }
       }
       return false;
    }

    /* renvoit false si au moins un element sel est en lecture seule */
    private boolean isSelEnabled(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
            return false;
        boolean enabled = true;
        for (int i=0;i<tabPaths.length; i++){
            CopexNode n = (CopexNode)tabPaths[i].getLastPathComponent();
            if (n.isDisabled()){
                enabled = false;
                break;
            }
        }
        return enabled;
    }

    /*
     * des elements de l'arbre sont ils selectionnes ?
     */
    public boolean isElementsSel(){
        return this.getSelectionCount() > 0;
    }
    /*
     * selection d'un et un seul element
     */
    public boolean isOnlyOneElementIsSel(){
        return this.getSelectionCount() == 1;
    }

    /* au - un element de sel dans l'arbre des taches */
    public boolean isElementSelTaskTree(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
            return false;
        for (int i=0;i<tabPaths.length; i++){
            CopexNode n = (CopexNode)tabPaths[i].getLastPathComponent();
            if (n instanceof TaskTreeNode){
                return true;
            }
        }
        return false;
    }

    /* ouverture de la fenetre d'edition de l'etape */
   public void editStep(TaskTreeNode currentNode){
       ImageIcon img = null ;
       if (owner.getTaskImage(currentNode.getTask().getTaskImage()) != null)
           img = new ImageIcon(owner.getTaskImage(currentNode.getTask().getTaskImage()));

       StepDialog stepD = new StepDialog(owner, false, !proc.isTaskProc(),(Step)currentNode.getTask(), img, proc.getInitialProc().isTaskRepeat(),  currentNode.getTask().getEditRight(), this.proc.getRight());
       stepD.setVisible(true);
   }

   /* ouverture de la fenetre d'edition de l'action */
   public void editAction(TaskTreeNode currentNode){
       ImageIcon img = null ;
       if (owner.getTaskImage(currentNode.getTask().getTaskImage()) != null)
           img = new ImageIcon(owner.getTaskImage(currentNode.getTask().getTaskImage()));
       InitialNamedAction actionNamed = null;
       Object[] tabParam = null;
       ArrayList<Object> materialProd = null;
       ArrayList<Object> dataProd = null;
       if (currentNode.getTask() instanceof CopexActionNamed){
           actionNamed = ((CopexActionNamed)currentNode.getTask()).getNamedAction();
       }
       if (currentNode.getTask() instanceof CopexActionAcquisition || currentNode.getTask() instanceof CopexActionChoice ||currentNode.getTask() instanceof CopexActionManipulation ||currentNode.getTask() instanceof CopexActionTreatment){
           tabParam = ((CopexActionParam)currentNode.getTask()).getTabParam() ;
           if (currentNode.getTask() instanceof CopexActionAcquisition )
               dataProd = ((CopexActionAcquisition)currentNode.getTask()).getListDataProd() ;
           else if (currentNode.getTask() instanceof CopexActionTreatment )
               dataProd = ((CopexActionTreatment)currentNode.getTask()).getListDataProd() ;
           else if (currentNode.getTask() instanceof CopexActionManipulation )
               materialProd = ((CopexActionManipulation)currentNode.getTask()).getListMaterialProd() ;
       }
       ActionDialog actionD = new ActionDialog(owner, false, currentNode.getTask().getDescription(owner.getLocale()),
               currentNode.getTask().getComments(owner.getLocale()), img ,currentNode.getTask().getDraw(), actionNamed,
               currentNode.getTask().getEditRight(),  this.proc.getRight(),
               this.proc.getInitialProc().isFreeAction(), this.proc.getInitialProc().getListNamedAction(),
               owner.getListPhysicalQuantity(), tabParam, materialProd, dataProd,
               this.proc.getInitialProc().isTaskRepeat(), currentNode.getTask().getTaskRepeat());
       actionD.setVisible(true);
   }
   /* retourne les noeuds selectionnes + enfants */
   public ArrayList<CopexNode> getSelectedNodes(){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 )
          return null;
      tabPaths = sortTabPath(tabPaths);
      ArrayList<CopexNode> listTreeNode = new ArrayList();
      for (int i=0; i<tabPaths.length; i++){
          CopexNode selNode = ((CopexNode) tabPaths[i].getLastPathComponent());
          if(selNode instanceof TaskTreeNode)
                listTreeNode.add((TaskTreeNode)selNode);
          int nbC = selNode.getChildCount();
          for (int c=0; c<nbC; c++){
              if (!isSelectedNode((CopexNode)selNode.getChildAt(c)))
                listTreeNode.add((CopexNode)selNode.getChildAt(c));
          }
      }

      return listTreeNode;
   }

   /* retourne les taches selectionnes + enfants */
   public ArrayList<CopexTask> getSelectedTasks(){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 )
          return null;
      tabPaths = sortTabPath(tabPaths);
      ArrayList<CopexTask> listT = new ArrayList();
      for (int i=0; i<tabPaths.length; i++){
          CopexNode selNode = ((CopexNode) tabPaths[i].getLastPathComponent());
          listT.add(selNode.getTask());
          if (selNode.getChildCount() > 0){
                // recupere les enfants
               ArrayList<CopexTask> lc = getChildTask(selNode, true);
               // on les ajoute
               int n = lc.size();
               for (int k=0; k<n; k++){
                   // ajoute
                   listT.add(lc.get(k));
               }
            }
      }
      // on supprime les occurences multiples
      int nbT = listT.size();
      ArrayList<CopexTask> lt = new ArrayList();
      ArrayList<CopexTask> listTClean = new ArrayList();
      for (int t=0; t<nbT; t++){
          int id = getId(lt, listT.get(t).getDbKey());
          if (id == -1){
              //System.out.println("tache du sous arbre : "+listT.get(t).getDescription());
              listTClean.add(listT.get(t));
          }
          lt.add(listT.get(t));
      }
      return listTClean;
   }

   /* retourne les taches selectionnes */
   public ArrayList<CopexTask> getSelectedTasksSimple(){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 )
          return null;
      tabPaths = sortTabPath(tabPaths);
      ArrayList<CopexTask> listT = new ArrayList();
      for (int i=0; i<tabPaths.length; i++){
          CopexNode selNode = ((CopexNode) tabPaths[i].getLastPathComponent());
          listT.add(selNode.getTask());
      }
      return listT;
   }

   /* cherche un indice dans la liste, -1 si non trouve */
    private int getId(ArrayList<CopexTask> listT, long dbKey){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            CopexTask t = listT.get(i);
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }


/* creation du pop up menu */
    private CopexPopUpMenu getPopUpMenu(ArrayList<CopexTask> taskList){
        if (popUpMenu == null){
            int nb = taskList.size();
            char mode = MyConstants.POPUPMENU_UNDEF;
            System.out.println("popUpMenu : "+nb);
            if(nb == 1){
                CopexTask task = taskList.get(0);
                if(task.isStep()){
                    mode = MyConstants.POPUPMENU_STEP;
                    if(proc.isTaskProc())
                        mode = MyConstants.POPUPMENU_TASK;
                }else if(task.isAction()){
                    mode = MyConstants.POPUPMENU_ACTION;
                }
            }
            System.out.println("mode popupmenu : "+mode);
            popUpMenu = new CopexPopUpMenu(owner, owner.getController(), this, mode);
        }
        return popUpMenu;
    }
    /* retourne la ts */
    private TaskSelected getTaskSelected(CopexNode selNode){
//        if (selNode == null)
//            return null;
//        CopexTask taskSel = null;
//        CopexTask taskBrother = null;
//        CopexTask taskParent = null;
//        CopexTask parentTask = null;
//        CopexTask taskOldBrother = null;
//        CopexNode brotherNode = null;
//        CopexNode parentNode = null;
//        CopexNode oldBrotherNode = null;
//        CopexNode lastBrotherNode = null;
//        ArrayList<CopexTask> listAllChildren = new ArrayList();
//        taskSel = selNode.getTask();
//
//        if(selNode.isManipulation()){
//            taskSel = proc.getQuestion();
//        }
//       if (selNode instanceof TaskTreeNode && ((TaskTreeNode)selNode).isAction()){
//            taskBrother = ((TaskTreeNode)selNode).getTask();
//            brotherNode = selNode;
//       } else {
//            listAllChildren = getChildTask(selNode, true);
//            brotherNode =  getLastChild(selNode);
//            // si etape ouverte ou vide => place en fin d'etape
//            // si etape fermee => apres etape (frere)
//            if (brotherNode == null){
//                  taskParent = selNode.getTask();
//                  parentNode = selNode;
//                  if(selNode.isManipulation()){
//                      taskParent = proc.getQuestion();
//                  }
//             }else{
//                taskBrother = brotherNode.getTask();
//                 if (!taskBrother.isVisible()){
//                     // etape fermee
//                     brotherNode = selNode;
//                     taskBrother = brotherNode.getTask();
//                     if(selNode.isManipulation())
//                         taskBrother = proc.getQuestion();
//                 }
//             }
//        }
//
//       // determine le grand frere qui n'est pas sel
//       CopexNode oldBrother = getOldBrother(selNode, true);
//       if (oldBrother != null){
//           taskOldBrother = oldBrother.getTask();
//       }
//       // determine le dernier frere selectionne
//       lastBrotherNode = getLastBrotherNode(selNode);
//       if (selNode.getParent() != null)
//           parentTask = ((CopexNode)selNode.getParent()).getTask();
//       if(((CopexNode)selNode.getParent()).isManipulation())
//           parentTask = proc.getQuestion();
//       CopexNode oldB = getOldBrother(selNode, false);
//       CopexTask oldBT = null;
//       if (oldB != null){
//           oldBT = oldB.getTask();
//       }
//       TaskSelected ts = new TaskSelected(proc, taskSel, taskBrother, taskParent, taskOldBrother, selNode, brotherNode, parentNode, oldBrotherNode, listAllChildren, parentTask, lastBrotherNode, oldBT);
//       return ts;
        return getTaskSelected(selNode, MyConstants.INSERT_TASK_UNDEF);
    }

    /* retourne la ts */
    public TaskSelected getTaskSelected(CopexTask selTask, char insertIn){
        return getTaskSelected(getNode(selTask, (CopexNode)copexTreeModel.getRoot()), insertIn);
    }
    /* retourne la ts */
    private TaskSelected getTaskSelected(CopexNode selNode, char insertIn){
        if (selNode == null)
            return null;
        CopexTask taskSel = null;
        CopexTask taskBrother = null;
        CopexTask taskParent = null;
        CopexTask parentTask = null;
        CopexTask taskOldBrother = null;
        CopexNode brotherNode = null;
        CopexNode parentNode = null;
        CopexNode oldBrotherNode = null;
        CopexNode lastBrotherNode = null;
        ArrayList<CopexTask> listAllChildren = new ArrayList();
        taskSel = selNode.getTask();

        if(selNode.isManipulation()){
            taskSel = proc.getQuestion();
        }
       if (selNode instanceof TaskTreeNode && ((TaskTreeNode)selNode).isAction()){
            taskBrother = ((TaskTreeNode)selNode).getTask();
            brotherNode = selNode;
       } else {
            listAllChildren = getChildTask(selNode, true);
            brotherNode =  getLastChild(selNode);
            // si etape ouverte ou vide => place en fin d'etape
            // si etape fermee => apres etape (frere)

            // si fixee => on garde les parametres actuels
            if(insertIn == MyConstants.INSERT_TASK_FIX){
                // si 1er enfant=> parent
                // sinon frere
                CopexNode p =(CopexNode)selNode.getParent();
                int id = selNode.getIndex(p);
                if (id==0){
                    taskParent = selNode.getTask();
                    parentNode = selNode;
                    if(selNode.isManipulation()){
                        taskParent = proc.getQuestion();
                    }
                }else{
                    brotherNode = selNode;
                     taskBrother = brotherNode.getTask();
                     if(selNode.isManipulation())
                         taskBrother = proc.getQuestion();
                }
            }else{
            if (brotherNode == null){
                if(selNode.isManipulation()){
                    taskParent = proc.getQuestion();
                    parentNode = selNode;
                }else{
                    if(insertIn == MyConstants.INSERT_TASK_UNDEF || insertIn == MyConstants.INSERT_TASK_IN)  {
                        taskParent = selNode.getTask();
                        parentNode = selNode;
                    }else{
                        // insertIn == INSERT_TASK_AFTER
                        brotherNode = selNode;
                        taskBrother = brotherNode.getTask();
                    }
                }
             }else{
                if(selNode.isManipulation()){
                    taskBrother = brotherNode.getTask();

                }else{
                    taskBrother = brotherNode.getTask();
                    if ((!taskBrother.isVisible() && (insertIn != MyConstants.INSERT_TASK_IN)) || insertIn == MyConstants.INSERT_TASK_AFTER){
                        // etape fermee
                        brotherNode = selNode;
                        taskBrother = brotherNode.getTask();
                    }
                    if(insertIn == MyConstants.INSERT_TASK_IN ){
                        taskParent = selNode.getTask();
                        parentNode = selNode;
                        taskBrother = null;
                    }
                }
             }
            }
        }

       // determine le grand frere qui n'est pas sel
       CopexNode oldBrother = getOldBrother(selNode, true);
       if (oldBrother != null){
           taskOldBrother = oldBrother.getTask();
       }
       // determine le dernier frere selectionne
       lastBrotherNode = getLastBrotherNode(selNode);
       if (selNode.getParent() != null)
           parentTask = ((CopexNode)selNode.getParent()).getTask();
       if(((CopexNode)selNode.getParent()).isManipulation())
           parentTask = proc.getQuestion();
       CopexNode oldB = getOldBrother(selNode, false);
       CopexTask oldBT = null;
       if (oldB != null){
           oldBT = oldB.getTask();
       }
       TaskSelected ts = new TaskSelected(proc, taskSel, taskBrother, taskParent, taskOldBrother, selNode, brotherNode, parentNode, oldBrotherNode, listAllChildren, parentTask, lastBrotherNode, oldBT);
       return ts;
    }


    public CopexNode getNode(CopexTask task){
        return getNode(task, (CopexNode)copexTreeModel.getRoot());
    }
    public  TaskSelected getTaskSelected(CopexTask task){
        return getTaskSelected(getNode(task, (CopexNode)copexTreeModel.getRoot()));
    }
   

    /* retourne les elements selectionnes */
    public ArrayList<TaskSelected> getTasksSelected(){
        // on en profite pour chercher la tache grand frere
        // si selection est la racine, il s'agit du dernier enfant de la racine
        // si selection est etape : dernier enfant de l'etape
        // si selection est action : action
        // si selection est sous question : dernier enfant de la sous question
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
            return null;
        tabPaths = sortTabPath(tabPaths);
        ArrayList<TaskSelected> listTaskSelected = new ArrayList();
        for (int i=0; i<tabPaths.length; i++){
            TaskSelected ts = getTaskSelected((CopexNode)tabPaths[i].getLastPathComponent());
            if (ts != null)
                listTaskSelected.add(ts);
        }
        return  listTaskSelected;
    }


     /* renvoit la TaskSelected d'un noeud donne pour un drag and drop */
   private TaskSelected getSelectedTask(CopexNode node, boolean brother){
       CopexTask taskBrother = null;
       CopexTask taskParent = null;
       CopexTask parentTask = null;
       CopexTask taskOldBrother = null;
       CopexNode brotherNode = null;
       CopexNode parentNode = null;
       CopexNode oldBrotherNode = null;
       CopexNode lastBrotherNode = null;
       ArrayList<CopexTask> listAllChildren = new ArrayList();
       CopexTask taskSel = node.getTask();
       if(node.isManipulation())
           taskSel = proc.getQuestion();
       if (node.isAction()){
             taskBrother = node.getTask();
             brotherNode = node;
        }  else {
           listAllChildren = getChildTask(node, true);
           if (brother)
               taskBrother = node.getTask();
           else{
               taskParent = node.getTask();
               if(node.isManipulation())
                taskParent = proc.getQuestion();
                parentNode = node;
           }

       }
       if (node.getParent() != null){
            parentTask = ((CopexNode)node.getParent()).getTask();
            if(((CopexNode)node.getParent()).isManipulation())
                parentTask = proc.getQuestion();
       }
       CopexNode oldB = getOldBrother(node, false);
       CopexTask oldBT = null;
       if (oldB != null)
           oldBT = oldB.getTask();
        TaskSelected ts = new TaskSelected(proc, taskSel, taskBrother, taskParent, taskOldBrother, node, brotherNode, parentNode, oldBrotherNode, listAllChildren, parentTask, lastBrotherNode, oldBT);
        return ts ;
   }


    /* retourne le grand frere */
    private CopexNode getOldBrother(CopexNode node, boolean sel){
        CopexNode oldBrotherNode = null;
        CopexNode p = (CopexNode)node.getParent();
        if (p != null){
              oldBrotherNode = (CopexNode)p.getChildBefore(node);
              if (oldBrotherNode != null){
                  // si c'est un grand frere sel on prend celui du dessus
                  if (sel && isSelectedNode(oldBrotherNode)){
                      return getOldBrother(oldBrotherNode, sel);
                  }else
                      return oldBrotherNode;
              }
        }
        return null ;
    }


    /* retourne le dernier frere selectionne, lui meme sinon */
    private CopexNode getLastBrotherNode(CopexNode node){
        CopexNode p = (CopexNode)node.getParent();
        if (p != null){
            int nbC = p.getChildCount();
            int id = p.getIndex(node);
            for (int i=nbC-1; i>id; i--){
                CopexNode t = (CopexNode)p.getChildAt(i);
                if (isSelectedNode(t)){
                    return t;
                }
            }
            return node;
        }
        return node;
    }


    /* retourne vrai s'il s'agit d'un noeud selectionne */
    private boolean isSelectedNode(CopexNode node){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
            return false;
        for (int i=0; i<tabPaths.length; i++){
            CopexNode selNode = (CopexNode) tabPaths[i].getLastPathComponent();
            if (selNode == node)
                return true;
        }
        return false;
    }

   /* retourne la liste des enfants d'un noeud - sous forme de CopexTask  */
    private ArrayList<CopexTask> getChildTask(CopexNode node, boolean withChild){
        ArrayList<CopexTask> listChild = new ArrayList();
        int nbC = node.getChildCount();
        for (int c=0; c<nbC; c++){
            CopexNode childNode = (CopexNode)node.getChildAt(c);
            // ajoute l'enfant
            if(childNode instanceof TaskTreeNode){
                listChild.add(((TaskTreeNode)childNode).getTask());
            }
            // on ajoute egalement les enfants de l'enfant
            if (withChild){
                ArrayList<CopexTask> lc = getChildTask(childNode, true);
                int n = lc.size();
                for (int i=0; i<n; i++){
                    listChild.add(lc.get(i));
                }
            }
        }
        return listChild;
    }



    /* en cas de selection simple renvoit la tache selectionne */
    public TaskSelected getTaskSelected(){
        ArrayList<TaskSelected> listTs = getTasksSelected();
        if (listTs != null && listTs.size() > 0)
            return listTs.get(0);
        else
            return null;
    }
    /* en cas de selection simple renvoit la tache selectionne */
    public TaskSelected getTaskSelected(char insertIn){
       TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
            return null;
        for (int i=0; i<tabPaths.length; i++){
            TaskSelected ts = getTaskSelected((CopexNode)tabPaths[i].getLastPathComponent(), insertIn);
            return ts;
        }
       return null;
    }
    /* retourne le niveau arborescence max */
    public int getLevelTree(){
        return getLevelTree((CopexNode)copexTreeModel.getRoot());
    }

    private int getLevelTree(CopexNode node){
        int level = node.getLevel();
        int nbC = copexTreeModel.getChildCount(node);
       if (nbC > 0){
           for (int i=0; i<nbC; i++){
               int l = getLevelTree((CopexNode)node.getChildAt(i));
               if (l > level)
                   level = l;
           }
       }
       return level;
    }

    /* retourne le dernier enfant du noeud passe en parametre
     * si le noeud n'a pas d'enfant, null
     */
    private CopexNode getLastChild(CopexNode node){
        int nbC = node.getChildCount();
        if (nbC == 0)
            return null;
        return ((CopexNode)node.getChildAt(nbC-1));
    }
    
    /* ajout d'une tache */
    public void addTask(CopexTask newTask, TaskSelected ts){
        TaskTreeNode newNode = new TaskTreeNode(newTask);
        if (ts.getTaskBrother() == null){
            // insertion en tant que premier enfant du parent
            if (ts.getParentNode() == null){
                System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : ");
            }
            copexTreeModel.insertNodeInto(newNode, ts.getParentNode(), 0);
        }else{ // apres le frere
            //System.out.println("ajout de la tache apres frere "+ts.getTaskBrother().getDescription());
            CopexNode pn = (CopexNode)ts.getBrotherNode().getParent();
            if (pn == null){
                System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : "+ts.getBrotherNode().getTask().getDescription(owner.getLocale()));
            }
            copexTreeModel.insertNodeInto(newNode, pn, pn.getIndex(ts.getBrotherNode())+1);
        }
        // developpe le chemin
        this.scrollPathToVisible(new TreePath(newNode.getPath()));
        this.clearSelection();
        this.setSelectionPath(new TreePath(newNode.getPath()));
        owner.updateLevel(getLevelTree());
        revalidate();
        repaint();
    }

    /* ajout d'une tache */
    public void addTask(CopexTask newTask, TaskSelected ts, boolean brother){
        if (!ts.getSelectedTask().isAction() && brother ){
            TaskTreeNode newNode = new TaskTreeNode(newTask);
            CopexNode pn = (CopexNode)ts.getSelectedNode().getParent();
            if (pn == null){
                System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : "+ts.getSelectedNode().getTask().getDescription(owner.getLocale()));
            }
            copexTreeModel.insertNodeInto(newNode, pn, pn.getIndex(ts.getSelectedNode())+1);
            // developpe le chemin
            this.scrollPathToVisible(new TreePath(newNode.getPath()));
            owner.updateLevel(getLevelTree());
            revalidate();
            repaint();
        }else if(!ts.getSelectedTask().isAction() && !brother){
            TaskTreeNode newNode = new TaskTreeNode(newTask);
            CopexNode pn = ts.getSelectedNode();
            copexTreeModel.insertNodeInto(newNode, pn, 0);
            // developpe le chemin
            this.scrollPathToVisible(new TreePath(newNode.getPath()));
            owner.updateLevel(getLevelTree());
            revalidate();
            repaint();
        }else
            addTask(newTask, ts);
    }


    /* ajout d'une liste de tache */
    public void addTasks(ArrayList<CopexTask> listTask, SubTree subTree, TaskSelected ts){
       CopexTask taskBranch = listTask.get(0);
        //TaskSelected ts = getTaskSelected();
        CopexTask taskBrother = ts.getTaskBrother();
        CopexTask taskParent = ts.getTaskParent();
        // construit le sous arbre
        TaskTreeNode newNode = new TaskTreeNode(taskBranch);
        if (taskBrother == null){
            CopexNode np = getNode(taskParent, (CopexNode)copexTreeModel.getRoot());
            // insertion en tant que premier enfant du parent
            copexTreeModel.insertNodeInto(newNode, np, 0);
        }else{ // apres le frere
            CopexNode nb = getNode(taskBrother, (CopexNode)copexTreeModel.getRoot());
            CopexNode pn = (CopexNode)nb.getParent();
            copexTreeModel.insertNodeInto(newNode, pn, pn.getIndex(nb)+1);

        }
        copexTreeModel.addNodes(newNode, listTask);
        // developpe le chemin
        this.scrollPathToVisible(new TreePath(newNode.getPath()));
        owner.updateLevel(getLevelTree());
        revalidate();
        repaint();
    }



    /* renvoit le noeud crorrespondant a la tache */
    public CopexNode getNode(CopexTask task, CopexNode node){
        if(node.isManipulation() && task.isQuestion())
            return node;
       if (node instanceof TaskTreeNode && ((TaskTreeNode)node).getTask().getDbKey() == task.getDbKey())
           return (TaskTreeNode)node;
       else{
           // on cherche dans les enfants
           if (node.getChildCount() > 0){
               for (int k=0; k<node.getChildCount(); k++){
                   CopexNode n = getNode(task, (CopexNode)copexTreeModel.getChild(node, k));
                   if (n != null)
                       return n;
               }
           }
           // on cherche dans les freres
           CopexNode parent = (CopexNode)node.getParent();
           if (parent != null){
               CopexNode bn = (CopexNode)parent.getChildAfter(node);
               if (bn != null){
                   CopexNode n = getNode(task, bn);
                   if (n != null)
                       return n;
               }

           }
       }
       return null;
    }

    /* mise a jour  d'une tache */
    public void updateTask(CopexTask newTask, TaskSelected ts){
        undoManager.addEdit(new UpdateTaskUndoRedo(owner, owner.getController(), this, ts.getSelectedNode().getTask(), ts.getSelectedNode(), newTask));
        updateTask(newTask, ts.getSelectedNode());
    }

    public void addEdit_updateQuestion(Question oldQuestion, Question newQuestion){
        undoManager.addEdit(new UpdateTaskUndoRedo(owner, owner.getController(), this, oldQuestion, (CopexNode)treeModel.getRoot(), newQuestion));
    }
    /* mise a jour  d'une tache */
    public void updateTask(CopexTask newTask, CopexNode node){
        CopexNode n = getNode(node.getTask());
        n.setUserObject(newTask);
        if(node.isQuestion())
           node.setNode(newTask);
        else if(node instanceof TaskTreeNode)
            ((TaskTreeNode)node).setTask(newTask);
        //n.setTask(newTask);
        copexTreeModel.reload(n);
        revalidate();
        repaint();
    }

    /* affichage d'un certain niveau
     * niveau 1 : affichage de la racine et de ses enfants (non deployes)....
     */
    public void displayLevel(int level){
        //level = level+1;
        setVisibleNode(level, (CopexNode)copexTreeModel.getRoot());
    }

    private void setVisibleNode(int level, CopexNode node){
       int levelNode = node.getLevel();
       int nbC = copexTreeModel.getChildCount(node);
       if (levelNode < level){
           if (nbC > 0){
                for (int i=0; i<nbC; i++){
                    setVisibleNode(level, (CopexNode)node.getChildAt(i));
                }
            }
       }else { // on referme les noeuds qui etaient eventuellement deployes
           collapsePath(new TreePath(node.getPath()));
            for (int i=0; i<nbC; i++){
                collapsePath(new TreePath((CopexNode)node.getChildAt(i)));
             }
       }
       this.scrollPathToVisible(new TreePath(node.getPath()));
    }

    /* suppression des noeuds */
    public void suppr(ArrayList<TaskSelected> listTs){
       //int level = getLevelTreeDisplay();
        this.register = false;
        markDisplay();
       int nbT = listTs.size();
       for (int i=0; i<nbT; i++){
           //TaskTreeNode node = listTs.get(i).getSelectedNode();
           CopexNode n = getNode(listTs.get(i).getSelectedTask());
           suppr(n);
           if(n instanceof TaskTreeNode)
            listVisibleNode.remove((TaskTreeNode)n);
       }
      copexTreeModel.reload();
      clearSelection();
      owner.updateMenu();
      revalidate();
      displayTree();
      this.register = true;
      ///level = Math.min(level, getLevelTree());
      //displayLevel(level);
      //repaint();
    }

    /* suppression d'un noeud*/
    private void suppr(CopexNode node){
        if(node==null)
            return;
        try{
            copexTreeModel.removeNodeFromParent(node);
        }catch(IllegalArgumentException e){
            System.out.println("Suppression d'un noeud qui n'est plus dans l'arbre "+node);
        }
        if (!node.isAction()){
            // etape ou sous question on supprime egalement tous les enfants
            int nbC = node.getChildCount();
            for (int c=nbC-1; c==0; c--){
                suppr((TaskTreeNode)node.getChildAt(c));
            }
        }
    }

    /* retourne le niveau d'arborescence affiche */
    private int getLevelTreeDisplay(){
        int level = 1;
        int nb = getRowCount();
        for (int i=0; i<nb; i++){
            TreePath path = getPathForRow(i);
            if (path != null){
               CopexNode node = (CopexNode)path.getLastPathComponent() ;
               int l = node.getLevel();
               if (l > level)
                   level = l;
            }
        }
        return level;
    }




    // EVENEMENTS CLAVIERS
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Evenements souris
        if (e.getKeyCode() == KeyEvent.VK_C && canCopy()){
            owner.copy();
        }else if (e.getKeyCode() == KeyEvent.VK_M && canAddE()){
            owner.addEtape();
        }else if (e.getKeyCode() == KeyEvent.VK_N && canAddA()){
            owner.addAction();
        }else if (e.getKeyCode() == KeyEvent.VK_X && canCut()){
            owner.cut();
        }else if (e.getKeyCode() == KeyEvent.VK_V && canPaste()){
            owner.paste();
        }else if (e.getKeyCode() == KeyEvent.VK_DELETE && canSuppr()){
            this.owner.suppr();
        }else if (e.getKeyCode() == KeyEvent.VK_E && isOnlyOneElementIsSel()){
            this.edit();
        }
    }

   /* renommer un protocole */
    public void updateProcName(String name){
        proc.setName(CopexUtilities.getTextLocal(name,owner.getLocale()));
    }

    /* retourne le sous arbre a copier */
    public SubTree getSubTreeCopy(boolean dragNdrop){
        // on selectionne egalement les taches enfants des etapes / ss question

        SubTree st = new SubTree(owner, owner.getController(), proc, this, getSelectedTasks(), getSelectedNodes(), dragNdrop);
        return st;
    }

    /* tri des selections pour mettre en ordre */
    private TreePath[] sortTabPath(TreePath[] tabPaths){
        // on tri selon le niveau de ligne dans l'arbre
        boolean bRetour = false;
        int cpt;
        int i;
        int lg = tabPaths.length;
        TreePath val;

        for(i = 1; i < lg ; i++){
            val = tabPaths[i];
            cpt = i-1;
            do{
            bRetour = true;
            if (this.getNoRow((CopexNode)tabPaths[cpt].getLastPathComponent()) > this.getNoRow((CopexNode)val.getLastPathComponent())){
            //if(tabPaths[cpt] > val){
                tabPaths[cpt+1] = tabPaths[cpt];
                cpt = cpt-1;
                bRetour = false;
            }
            if(cpt <0){
                bRetour = true;
            }
            }while(!bRetour);
            tabPaths[cpt+1] = val;
        }
        return tabPaths;
    }

    /* retourne la position dans l'arbre */
    private int getNoRow(CopexNode node){
        CopexNode parent = (CopexNode)node.getParent();
        if (parent == null)
            return 0;
        else{
            int idC = parent.getIndex(node);
            if (idC == 0)
                return getNoRow(parent)+1;
            else{
                CopexNode lastChild = getSubLastChild((CopexNode)parent.getChildBefore(node));
                return getNoRow(lastChild)+1;
            }
        }
    }


    /* retourne le dernier petit enfant- sinon lui meme */
    private CopexNode getSubLastChild(CopexNode node){
        int nbC = node.getChildCount();
        if (nbC == 0)
            return node;
        else{
            return getSubLastChild((CopexNode)node.getChildAt(nbC-1));
        }
    }
    /* retourne vrai si les elements selectionnes forme un sous arbre */
    public  boolean selIsSubTree(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        int firstLevel = getLevelTree((CopexNode) tabPaths[0].getLastPathComponent());
        for (int i=1; i<tabPaths.length; i++){
            if (getLevelTree((CopexNode) tabPaths[i].getLastPathComponent()) > firstLevel)
                    return false;
        }
        return true;
    }

     /* retourne vrai si les elements selectionnes peuvent etre copies */
    public  boolean selCanBeCopy(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexNode node = (CopexNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canCopy())
                    return false;
            // on regarde egalement les enfants
            for (int k=0; k<node.getChildCount(); k++){
                CopexNode n = (CopexNode)node.getChildAt(k);
                if (!n.canCopy())
                    return false;
            }
        }
        return true;
    }

     /* retourne vrai si les elements selectionnes peuvent etre supprimes */
    public  boolean selCanBeDelete(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexNode node = (CopexNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canDelete())
                return false;
            // on regarde egalement les enfants
            for (int k=0; k<node.getChildCount(); k++){
                CopexNode n = (CopexNode)node.getChildAt(k);
                if (!n.canDelete())
                    return false;
            }
        }
        return true;
    }

    /* retourne vrai si les elements selectionnes peuvent etre deplaces */
    public  boolean selCanBeMove(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexNode node = (CopexNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canMove())
                return false;
            // on regarde egalement les enfants
            for (int k=0; k<node.getChildCount(); k++){
                CopexNode n = (CopexNode)node.getChildAt(k);
                if (!n.canMove())
                    return false;
            }
        }
        return true;
    }

    /* deplace un sous arbre */
    public boolean moveSubTree(SubTree subTree, CopexNode node, boolean brother){
        TaskSelected ots ;
        TaskSelected t = getTaskSelected(subTree.getFirstTask());
//        System.out.println("premiere tache ss arbre : "+t.getSelectedTask().getDescription(owner.getLocale()));
        ots = getTaskSelected(t.getTaskToAttach(), MyConstants.INSERT_TASK_FIX);
//        if(t.getTaskToAttach() != null)
//            System.out.println("attachee a : "+t.getTaskToAttach().getDescription(owner.getLocale())+" comme frere ?"+t.attachLikeBrother());

        TaskSelected ts = getSelectedTask(node, brother);
        //System.out.println("recupe task selected");
        //System.out.println("tache insertion : "+node.getDebug(owner.getLocale()));
       // verifie que la tache sel n'est pas dans le sous arbre
        //System.out.println("appel controle");
       boolean control = control(node, subTree);
      //System.out.println("controle : "+control);
       if (!control)
           return false;
       //System.out.println("appel noyau");
       CopexReturn cr = owner.getController().move(ts, subTree, MyConstants.NOT_UNDOREDO);
        if (cr.isError()){
            owner.displayError(cr, owner.getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
       //System.out.println("maj graphique");
        // mise a jour graphique
        addTasks(subTree.getListTask(), subTree, ts);
        this.subTree = subTree;
        this.newTs = ts;
        this.oldTs = ots;
        this.owner.setModification();
        return true;
    }

    /* controle que la tache sel n'est pas dans le sous arbre */
    private boolean control(CopexNode node, SubTree subTree){
        return !subTree.containNode(node);
    }


    /* supprime les taches */
    public void removeTask(SubTree subTree, boolean addEdit){
        markDisplay();
        ArrayList<CopexNode> listNode = this.subTree.getListNodes();
        int nbN = listNode.size();
        for (int i=0; i<nbN; i++){
            copexTreeModel.removeNodeFromParent(listNode.get(i));
        }
        copexTreeModel.reload();
        clearSelection();
        owner.getController().finalizeDragAndDrop(this.proc);
        // met a jour les noeuds
        ArrayList<CopexNode> updateListNode = new ArrayList();
        ArrayList<CopexTask> listTask = new ArrayList();
        for (int i=0; i<nbN; i++){
            CopexTask t = getTask(subTree.getListTask().get(i).getDbKey());
            updateListNode.add(getNode(t));
            listTask.add(t);
        }

        this.subTree = new SubTree(owner, owner.getController(), proc, this, listTask, updateListNode, true);
        //subTree.setListNodes(updateListNode);
        //newTs = getTaskSelected(newTs.getSelectedTask(), MyConstants.INSERT_TASK_FIX);
        oldTs = getTaskSelected(oldTs.getSelectedTask(), MyConstants.INSERT_TASK_FIX);
        if (addEdit)
            addEdit_dragDrop(this.subTree, newTs, oldTs);
        refreshMouseOver((CopexNode)copexTreeModel.getRoot());
        revalidate();
        displayTree();
        owner.updateMenu();
        repaint();
    }

    private CopexTask getTask(long dbKey){
        int nbT = proc.getListTask().size();
        for (int i=0; i<nbT; i++){
            if (proc.getListTask().get(i).getDbKey() == dbKey)
                return proc.getListTask().get(i);
        }
        return null;
    }

    

    /* undo */
    public void undo(){
        try{
            undoManager.undo();
            owner.updateMenu();
        }catch(CannotUndoException e){

        }
    }

    /* redo */
    public void redo(){
        try{
            undoManager.redo();
            owner.updateMenu();
        }catch(CannotUndoException e){

        }
    }

    /*ajout d'un evenement : renommer proc*/
    public void addEdit_renameProc(LearnerProcedure proc, String name){
        this.undoManager.addEdit(new UpdateProcNameUndoRedo(owner, owner.getController(), this, proc, proc.getName(owner.getLocale()), name));
    }

    public void addEdit_addTask(TaskSelected task, TaskSelected ts){
        this.undoManager.addEdit(new AddTaskUndoRedo(owner, owner.getController(), this, task, ts));
    }

    public void addEdit_deleteTask(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs){
        this.undoManager.addEdit(new DeleteTaskUndoRedo(owner, owner.getController(), this, listTask, listTs));
    }



     /* couper */
      public void addEdit_cut(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs, SubTree subTree){
        this.undoManager.addEdit(new CutUndoRedo(owner, owner.getController(), this, listTask, listTs, subTree));
    }

      /* coller */
     public void addEdit_paste(SubTree subTree, TaskSelected ts, ArrayList<TaskSelected> listTask){
        this.undoManager.addEdit(new PasteUndoRedo(owner, owner.getController(), this, subTree, ts, listTask));
    }

      /* drag and drop */
     public void addEdit_dragDrop(SubTree subTree, TaskSelected ts, TaskSelected oldTs){
        this.undoManager.addEdit(new DragAndDropUndoRedo(owner, owner.getController(), this, subTree, ts, oldTs));
    }



    

     /* reaffiche arbre dans le dernier etat */
     private void initTree(){
         listVisibleNode = new ArrayList();
         setListNodeVisible((CopexNode)copexTreeModel.getRoot());
         displayTree();
     }

     /* met la liste des noeuds visibles */
     private void setListNodeVisible(CopexNode node){
         if (node instanceof TaskTreeNode && ((TaskTreeNode)node).getTask().isVisible())
             listVisibleNode.add(((TaskTreeNode)node));
         int nbC = node.getChildCount();
         for (int i=0; i<nbC; i++){
             setListNodeVisible((CopexNode)node.getChildAt(i));
         }
     }

     /* debute enregistrement */
     public void beginregister(){
         this.register = true;
     }

    @Override
    protected void setExpandedState(TreePath path, boolean state) {
        super.setExpandedState(path, state);
        if (register){
            CopexNode node = (CopexNode)path.getLastPathComponent();
            // recupere tous les enfants
            ArrayList<CopexTask> listChild = getChildTask(node, !state);
            int nb = listChild.size();
            for (int i=0; i<nb; i++){
                listChild.get(i).setVisible(state);
                CopexReturn cr = owner.getController().updateTaskVisible(this.proc, listChild);
                if (cr.isError()){
                    owner.displayError(cr, owner.getBundleString("TITLE_DIALOG_ERROR"));
                }
            }

        }
    }

   

    /* resize arbre */
    public void resizeWidth(){
//        if(listVisibleNode != null && listVisibleNode.size() == 0)
            markDisplay();
        copexTreeModel.reload();
        displayTree();
        revalidate();
        repaint();
    }

    /* retourne le dernier enfant (voire ss enfant) du noeud, lui meme sinon */
    private CopexNode getLastChildren(CopexNode node){
        CopexNode childNode = getLastChild(node);
        CopexNode lastChild = childNode == null ? node : childNode;
        while (childNode != null){
            childNode = getLastChild(childNode);
            lastChild = childNode == null ? lastChild : childNode;
        }
        return lastChild;

    }

    /* retourne la liste des taches qui sont avant celle sel (y compris) */
      public ArrayList<CopexTask> getListTaskBeforeSel(boolean modeAdd){
          ArrayList<CopexTask> listTaskBefore = new ArrayList();
          // ce n'est pas le noeud sel mais le noeud ou on va inserer si l'action si on est en modeAdd
          CopexNode selNode = getTaskSelectedNode();
          if (selNode == null)
              return listTaskBefore;
          if (modeAdd){
              TaskSelected ts = getTaskSelected(selNode);
              if (ts.getTaskBrother() == null){
                  // ajout au parent
                  selNode = ts.getParentNode() ;
              }else{
                  // ajout en frere
                  selNode = getLastChildren(ts.getBrotherNode()) ;
              }
          }
          ArrayList v= new ArrayList();
          //return getListTaskBeforeSelNode(modeAdd,(TaskTreeNode)treeModelProc.getRoot(), selNode, v);
          //listTaskBefore = getListTaskBeforeSelNode(modeAdd,(CopexNode)copexTreeModel.getRoot(), selNode, v);
          listTaskBefore = getListTaskBeforeSelNode(modeAdd,copexTreeModel.getManipulationNode(), selNode, v);
//          for (int i=0; i<listTaskBefore.size(); i++){
//              System.out.println("tache prec : "+listTaskBefore.get(i).getDbKey()+" ("+listTaskBefore.get(i).getDescription(owner.getLocale())+") ");
//          }
          return listTaskBefore;
      }

      /* retourne la liste des taches avant selNode, en v[0] retourne vrai si on a trouve la tache */
      private ArrayList<CopexTask> getListTaskBeforeSelNode(boolean modeAdd, CopexNode node, CopexNode selNode, ArrayList v){
          CopexTask task = node.getTask();
          if(node.isManipulation())
              task = proc.getQuestion();
          ArrayList<CopexTask> listTask = new ArrayList();
          if(task== null){
              v.add(false);
              return listTask;
          }
          boolean findNode = false;
          CopexTask t = selNode.getTask();
          if(t== null){
              if (selNode.isManipulation())
                  t = proc.getQuestion();
          }
          if (task.getDbKey() == t.getDbKey()){
              findNode = true;
              if (modeAdd)
                  listTask.add(task);
          }else{
              listTask.add(task);
              int nbC = node.getChildCount() ;
              for (int i=0; i<nbC; i++){
                  ArrayList v2 = new ArrayList();
                  ArrayList<CopexTask> listChild = getListTaskBeforeSelNode(modeAdd, (CopexNode)node.getChildAt(i), selNode, v2);
                  // on concatene les listes
                  int nbTC = listChild.size() ;
                  for (int j=0; j<nbTC;j++){
                      listTask.add(listChild.get(j));
                  }
                  findNode = (Boolean)v2.get(0);
                  if (findNode)
                      break;
              }
          }
          v.add(findNode);
          return listTask;
      }

      /* retourne vrai si les taches sel ne sont pas lies par le materiel a d'autres taches */
      private boolean areActionSelLinkedByMaterial(){
          ArrayList<CopexTask> listTask = getSelectedTasks() ;
          if(listTask == null)
              return false;
          int nbT = listTask.size();
          for (int i=0; i<nbT; i++){
              if (isActionLinkedByMaterial(listTask.get(i)))
                  return true;
          }
          return false;

      }

      /* retourne vrai si les taches sel ne sont pas lies par le data a d'autres taches */
      private boolean areActionSelLinkedByData(){
          ArrayList<CopexTask> listTask = getSelectedTasks() ;
          if(listTask == null)
              return false;
          int nbT = listTask.size();
          for (int i=0; i<nbT; i++){
              if (isActionLinkedByData(listTask.get(i)))
                  return true;
          }
          return false;

      }

      /* retourne vrai si au moins une  action utilise le materiel produit par cette tache */
      private boolean isActionLinkedByMaterial(CopexTask task){
          if (task instanceof CopexActionManipulation){
              ArrayList<Object> listMaterialProd = ((CopexActionManipulation)task).getListMaterialProd() ;
              int nbMat = listMaterialProd.size();
              for (int i=0; i<nbMat; i++){
                  if(listMaterialProd.get(i) instanceof Material){
                    long dbKeyMatProd = ((Material)listMaterialProd.get(i)).getDbKey();
                    boolean isL=  isMaterialLinked(dbKeyMatProd);
                    if(isL)
                        return true;
                  }else if (listMaterialProd.get(i) instanceof ArrayList){
                      int nb = ((ArrayList)listMaterialProd.get(i)).size();
                      for (int k=0; k<nb; k++){
                          Material m = ((ArrayList<Material>)listMaterialProd.get(i)).get(k);
                          boolean isL=  isMaterialLinked(m.getDbKey());
                          if(isL)
                            return true;
                      }
                  }
              }
          }
          return false;
      }

      private boolean isMaterialLinked(long dbKeyMatProd){
         List<CopexTask> listTask = proc.getListTask();
          int nbT = listTask.size();
          for (int j=0; j<nbT; j++){
            if (listTask.get(j) instanceof CopexActionAcquisition || listTask.get(j) instanceof CopexActionChoice || listTask.get(j) instanceof CopexActionManipulation || listTask.get(j) instanceof CopexActionTreatment){
                Object[] tabParam = ((CopexActionParam)listTask.get(j)).getTabParam();
                for (int k=0; k<tabParam.length; k++){
                    if (tabParam[k] instanceof ActionParamMaterial){
                        if (((ActionParamMaterial)tabParam[k]).getMaterial().getDbKey() == dbKeyMatProd)
                            return true;
                    }
                    else if (tabParam[k] instanceof ArrayList){
                        ArrayList<ActionParam> p = (ArrayList<ActionParam>)tabParam[k];
                        int np = p.size();
                        for (int l=0; l<np; l++){
                            if(p.get(l) instanceof ActionParamMaterial && ((ActionParamMaterial)p.get(l)).getMaterial().getDbKey() == dbKeyMatProd)
                                return true;
                        }
                    }
                }
            }
        }
        return false;
      }


       /* retourne vrai si au moins une  action utilise la data produite par cette tache */
      private boolean isActionLinkedByData(CopexTask task){
          if (task instanceof CopexActionTreatment || task instanceof CopexActionAcquisition){
              List<CopexTask> listTask = proc.getListTask();
              int nbT = listTask.size();
              ArrayList<Object> listDataProd = new ArrayList();
              if (task instanceof CopexActionAcquisition)
                  listDataProd = ((CopexActionAcquisition)task).getListDataProd() ;
              else if (task instanceof CopexActionTreatment)
                  listDataProd = ((CopexActionTreatment)task).getListDataProd() ;
              int nbData = listDataProd.size();
              for (int i=0; i<nbData; i++){
                  if(listDataProd.get(i) instanceof QData){
                    long dbKeyDataProd = ((QData)listDataProd.get(i)).getDbKey();
                    boolean isL=  isDataLinked(dbKeyDataProd);
                    if(isL)
                        return true;
                  }else if (listDataProd.get(i) instanceof ArrayList){
                      int nb = ((ArrayList)listDataProd.get(i)).size();
                      for (int k=0; k<nb; k++){
                          QData m = ((ArrayList<QData>)listDataProd.get(i)).get(k);
                          boolean isL=  isDataLinked(m.getDbKey());
                          if(isL)
                            return true;
                      }
                  }
              }
          }
          return false;
      }

      private boolean isDataLinked(long dbKeyDataProd){
          List<CopexTask> listTask = proc.getListTask();
          int nbT = listTask.size();
          for (int j=0; j<nbT; j++){
            if (listTask.get(j) instanceof CopexActionAcquisition || listTask.get(j) instanceof CopexActionChoice || listTask.get(j) instanceof CopexActionManipulation || listTask.get(j) instanceof CopexActionTreatment){
                Object[] tabParam = ((CopexActionParam)listTask.get(j)).getTabParam();
                for (int k=0; k<tabParam.length; k++){
                    if (tabParam[k] instanceof ActionParamData){
                        if (((ActionParamData)tabParam[k]).getData().getDbKey() == dbKeyDataProd)
                            return true;
                    }
                    else if (tabParam[k] instanceof ArrayList){
                        ArrayList<ActionParam> p = (ArrayList<ActionParam>)tabParam[k];
                        int np = p.size();
                        for (int l=0; l<np; l++){
                            if(p.get(l) instanceof ActionParamData && ((ActionParamData)p.get(l)).getData().getDbKey() == dbKeyDataProd)
                                return true;
                        }
                    }
                }
            }
        }
        return false;
      }


      /* retourne vrai si le deplacement du sous arbre pose probleme du point de vue du material produit */
    public  boolean isProblemWithMaterialProd(CopexNode insertNode, SubTree subTreeToMove){
        boolean isProblem = false;
        ArrayList<CopexTask> listTaskToMove = subTreeToMove.getListTask();
        ArrayList v = new ArrayList();
        ArrayList<CopexTask> listTaskBefore = getListTaskBeforeSelNode(true, (CopexNode)copexTreeModel.getRoot(), insertNode, v);
        int nbT = listTaskToMove.size();
        for (int i=0; i<nbT; i++){
            if (listTaskToMove.get(i) instanceof CopexActionAcquisition || listTaskToMove.get(i) instanceof CopexActionChoice || listTaskToMove.get(i) instanceof CopexActionManipulation ||listTaskToMove.get(i) instanceof CopexActionTreatment ){
                Object[] tabParam = ((CopexActionParam)listTaskToMove.get(i)).getTabParam();
                for (int j=0; j<tabParam.length; j++){
                    if (tabParam[j] instanceof ActionParamMaterial){
                        long dbKeyMaterialUse = ((ActionParamMaterial)tabParam[j]).getMaterial().getDbKey();
                        if(!owner.isMaterialFromMission(dbKeyMaterialUse)){
                            if (!isMaterialCreateInList(dbKeyMaterialUse, listTaskBefore)){
                                return true;
                            }
                        }
                    }else if (tabParam[j] instanceof ArrayList){
                        ArrayList<ActionParam> p = (ArrayList<ActionParam>)tabParam[j];
                        int np = p.size();
                        for (int k=0; k<np; k++){
                            if (p.get(k) instanceof ActionParamMaterial){
                                long dbKeyMaterialUse = ((ActionParamMaterial)p.get(k)).getMaterial().getDbKey();
                                if(!owner.isMaterialFromMission(dbKeyMaterialUse)){
                                    if (!isMaterialCreateInList(dbKeyMaterialUse, listTaskBefore)){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isProblem ;
    }

       /* retourne vrai si le deplacement du sous arbre pose probleme du point de vue du data produit */
    public  boolean isProblemWithDataProd(CopexNode insertNode, SubTree subTreeToMove){
        boolean isProblem = false;
        ArrayList<CopexTask> listTaskToMove = subTreeToMove.getListTask();
        ArrayList v = new ArrayList();
        ArrayList<CopexTask> listTaskBefore = getListTaskBeforeSelNode(true, (CopexNode)copexTreeModel.getRoot(), insertNode, v);
        int nbT = listTaskToMove.size();
        for (int i=0; i<nbT; i++){
            if (listTaskToMove.get(i) instanceof CopexActionAcquisition || listTaskToMove.get(i) instanceof CopexActionChoice || listTaskToMove.get(i) instanceof CopexActionManipulation ||listTaskToMove.get(i) instanceof CopexActionTreatment ){
                Object[] tabParam = ((CopexActionParam)listTaskToMove.get(i)).getTabParam();
                for (int j=0; j<tabParam.length; j++){
                    if (tabParam[j] instanceof ActionParamData){
                        long dbKeyDataUse = ((ActionParamData)tabParam[j]).getData().getDbKey();
                        if (!isDataCreateInList(dbKeyDataUse, listTaskBefore)){
                                return true;
                            }
                    }else if (tabParam[j] instanceof ArrayList){
                        ArrayList<ActionParam> p = (ArrayList<ActionParam>)tabParam[j];
                        int np = p.size();
                        for (int k=0; k<np; k++){
                            if (p.get(k) instanceof ActionParamData){
                                long dbKeyDataUse = ((ActionParamData)p.get(k)).getData().getDbKey();
                                if (!isDataCreateInList(dbKeyDataUse, listTaskBefore)){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isProblem ;
    }



    /* retourne vrai si le materiel est cree dans la liste des taches */
    private boolean isMaterialCreateInList(long dbKeyMaterial, ArrayList<CopexTask> listTask){
        int nbT = listTask.size();
        for (int i=0; i<nbT; i++){
            if (listTask.get(i) instanceof CopexActionManipulation){
                ArrayList<Object> listMaterialProd = ((CopexActionManipulation)listTask.get(i)).getListMaterialProd();
                int nbMP = listMaterialProd.size();
                for (int j=0; j<nbMP; j++){
                    if(listMaterialProd.get(j) instanceof Material){
                        if (((Material)listMaterialProd.get(j)).getDbKey() == dbKeyMaterial)
                            return true;
                    }else if (listMaterialProd.get(j) instanceof ArrayList){
                        int nb = ((ArrayList)listMaterialProd.get(j)).size();
                        for (int k=0; k<nb; k++){
                            if (((ArrayList<Material>)listMaterialProd.get(j)).get(k).getDbKey() == dbKeyMaterial)
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /* retourne vrai si le data est cree dans la liste des taches */
    private boolean isDataCreateInList(long dbKeyData, ArrayList<CopexTask> listTask){
        int nbT = listTask.size();
        for (int i=0; i<nbT; i++){
            if (listTask.get(i) instanceof CopexActionAcquisition || listTask.get(i) instanceof CopexActionTreatment){
                ArrayList<Object> listDataProd = new ArrayList();
                if (listTask.get(i) instanceof CopexActionAcquisition)
                    listDataProd = ((CopexActionAcquisition)listTask.get(i)).getListDataProd();
                else if (listTask.get(i) instanceof CopexActionTreatment)
                    listDataProd = ((CopexActionTreatment)listTask.get(i)).getListDataProd();
                int nbDP = listDataProd.size();
                for (int j=0; j<nbDP; j++){
                    if(listDataProd.get(j) instanceof QData){
                        if (((QData)listDataProd.get(j)).getDbKey() == dbKeyData)
                            return true;
                    }else if (listDataProd.get(j) instanceof ArrayList){
                        int nb = ((ArrayList)listDataProd.get(j)).size();
                        for (int k=0; k<nb; k++){
                            if (((ArrayList<QData>)listDataProd.get(j)).get(k).getDbKey() == dbKeyData)
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /* remet mosueOver false dans arbre*/
    private void refreshMouseOver(CopexNode node){
        if(node instanceof TaskTreeNode)
            ((TaskTreeNode)node).setMouseover(false);
        int nbC = node.getChildCount() ;
        for (int i=0; i<nbC; i++){
            refreshMouseOver((CopexNode)node.getChildAt(i));
        }
    }

    public void refreshMouseOver(){
        refreshMouseOver((CopexNode)copexTreeModel.getRoot());
        repaint();
    }

    /* ouverture auto de la fenetre d'edition de la question */
    public void openQuestionDialog(){
        setSelectionPath(new TreePath(copexTreeModel.getRoot()));
        edit();
    }

    public void editMaterial(){
        owner.openMaterialDialog();
    }

    public void setQuestionEditor(){
        //setNodeEditing((CopexNode)copexTreeModel.getRoot());
        setNodeEditing(copexTreeModel.getQuestionNode());
    }
    public void setHypothesisEditor(){
        setNodeEditing(copexTreeModel.getHypothesisNode());
    }
    public void setPrincipleEditor(){
        setNodeEditing(copexTreeModel.getGeneralPrincipleNode());
    }
    public void setEvaluationEditor(){
        setNodeEditing(copexTreeModel.getEvaluationNode());
    }

    private void setNodeEditing(CopexNode node){
        if(node == null)
            return;
        TreePath path  = new TreePath(node.getPath());
        scrollPathToVisible(path);
        setSelectionPath(path);
        startEditingAtPath(path);
    }
    
}
