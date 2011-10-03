/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.common.ActionParam;
import eu.scy.client.tools.copex.common.ActionParamData;
import eu.scy.client.tools.copex.common.ActionParamMaterial;
import eu.scy.client.tools.copex.common.CopexAction;
import eu.scy.client.tools.copex.common.CopexActionAcquisition;
import eu.scy.client.tools.copex.common.CopexActionChoice;
import eu.scy.client.tools.copex.common.CopexActionManipulation;
import eu.scy.client.tools.copex.common.CopexActionNamed;
import eu.scy.client.tools.copex.common.CopexActionParam;
import eu.scy.client.tools.copex.common.CopexActionTreatment;
import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.DataSheet;
import eu.scy.client.tools.copex.common.Evaluation;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.common.GeneralPrinciple;
import eu.scy.client.tools.copex.common.Hypothesis;
import eu.scy.client.tools.copex.common.InitialNamedAction;
import eu.scy.client.tools.copex.common.InitialProcedure;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.common.Material;
import eu.scy.client.tools.copex.common.MaterialProc;
import eu.scy.client.tools.copex.common.QData;
import eu.scy.client.tools.copex.common.Question;
import eu.scy.client.tools.copex.common.Step;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.dnd.TreeTransferHandler;
import eu.scy.client.tools.copex.undoRedo.AddTaskUndoRedo;
import eu.scy.client.tools.copex.undoRedo.CopexUndoManager;
import eu.scy.client.tools.copex.undoRedo.CutUndoRedo;
import eu.scy.client.tools.copex.undoRedo.DeleteTaskUndoRedo;
import eu.scy.client.tools.copex.undoRedo.DragAndDropUndoRedo;
import eu.scy.client.tools.copex.undoRedo.EvaluationUndoRedo;
import eu.scy.client.tools.copex.undoRedo.GeneralPrincipleUndoRedo;
import eu.scy.client.tools.copex.undoRedo.HypothesisUndoRedo;
import eu.scy.client.tools.copex.undoRedo.PasteUndoRedo;
import eu.scy.client.tools.copex.undoRedo.UpdateProcNameUndoRedo;
import eu.scy.client.tools.copex.undoRedo.UpdateTaskUndoRedo;
import eu.scy.client.tools.copex.utilities.CopexCellEditor;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexTreeCellRenderer;
import eu.scy.client.tools.copex.utilities.CopexTreeSelectionListener;
import eu.scy.client.tools.copex.utilities.MyBasicTreeUI;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.undo.CannotUndoException;
import org.jdom.Element;

/**
 * copex tree
 * @author Marjolaine
 */
public class CopexTree extends JTree implements MouseListener, KeyListener, MouseMotionListener{
    private CopexTreeModel copexTreeModel;
    private CopexTreeCellRenderer copexTreeCellRenderer;
    private CopexCellEditor copexCellEditor;
    private ExperimentalProcedure proc;
    private EdPPanel owner;
    private CopexTreeSelectionListener selectionListener;

    // viewing mode of the comments
    private char displayComm = MyConstants.COMMENTS;
    /* pop up menu */
    private CopexPopUpMenu popUpMenu = null;
    /* Trandfer Handler */
    private TreeTransferHandler transferHandler;
    /* list of the visible nodes */
    private ArrayList<TaskTreeNode> listVisibleNode;
    /*  undo redo manager */
    private CopexUndoManager undoManager;
    /*markers for undo redo */
    private SubTree subTree;
    private TaskSelected newTs;
    private TaskSelected oldTs;
    /*register the tasks status*/
    private boolean register;
    private boolean isEditingNode;
    private CopexNode lastPath;

    public CopexTree(EdPPanel owner, ExperimentalProcedure proc) {
        this.proc = proc;
        this.owner = owner;
        this.setBackground(Color.WHITE);
        isEditingNode = false;
        lastPath = null;
        // model creation
        this.copexTreeModel = new CopexTreeModel(proc);
        this.setModel(copexTreeModel);
        // renderer
        copexTreeCellRenderer = new CopexTreeCellRenderer(this);
        this.setCellRenderer(copexTreeCellRenderer);
        //event
        // listen the mouse events
        this.selectionListener = new CopexTreeSelectionListener (this);
        addTreeSelectionListener(selectionListener);
        copexCellEditor = new CopexCellEditor(this);
        this.setCellEditor(copexCellEditor);
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
        this.addMouseMotionListener(this);
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

//   @Override
//    public void collapseRow(int row) {
//       int nrOfRows = getRowCount();
//       // prevent the collapse of the last row
//       if (row<4){
//          super.collapsePath(getPathForRow(row));
//       }
//    }
//
   public void closeTree(){
        int rowCount = getRowCount();
        rowCount = 3; // don't close the manupulation leave
        for (int i = 0; i < rowCount; i++) {
            collapseRow(i);
        }
   }

    public ExperimentalProcedure getProc(){
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
   // returns the description of an object
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


    // returns the comments (edition mode)
    public String getCommentLabelText(){
        return owner.getBundleString("LABEL_COMMENTS");
    }
   // returns the comment
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

   
   // return true if we can edit directly in the tree
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
                if(proc instanceof LearnerProcedure)
                    return ((LearnerProcedure)proc).getInitialProc().isDrawPrinciple()?false:true;
                else if(proc instanceof InitialProcedure){
                    return ((InitialProcedure)proc).isDrawPrinciple()?false:true;
                }
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

   // gets the title value
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
   /* returns the tool tiptex for a specified node (depends of the task right)*/
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

   // returns the repeat number of a task
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
   
   // returns the image of an object
   public ImageIcon getImageValue(Object value){
      if (value instanceof TaskTreeNode ){
            TaskTreeNode node =  (TaskTreeNode)value;
            if (node == null || node.getTask() == null || node.getTask().getTaskImage() == null  )
                    return null;
            Image img = null;
//            if (node.getTask().getTaskImage() != null && !node.getTask().getTaskImage().equals("")){
//                img = this.owner.getTaskImage("mini_"+node.getTask().getTaskImage()) ;
//            }
            if (img == null)
                return null ;
            return new ImageIcon(img);

       }else
        return null;
   }

    // returns the draw for a task
   public Element getTaskDraw(Object value){
      if (value instanceof TaskTreeNode ){
            TaskTreeNode node =  (TaskTreeNode)value;
            if (node == null || node.getTask() == null  )
                    return null;
            if(node.getTask().getDraw() == null){
                return null;
            }else{
                if(node.getTask().getDraw().getChild("whiteboardContainers") == null || (node.getTask().getDraw().getChild("whiteboardContainers") != null && node.getTask().getDraw().getChild("whiteboardContainers").getContentSize() > 0)){
                    return node.getTask().getDraw();
                }else{
                    return null;
                }
            }
       }else
        return null;
   }

   
   /* returns the available width in the tree */
    public int getTextWidth(Object value, int row){
        int totalWidth = this.owner.getWidth()-10 ;
        // the width is not the same (depends on the  level)
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
    /* returns the selectionned node */
   public CopexNode getSelectedNode(){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 || tabPaths.length > 1)
          return null;
      TreePath mypath = tabPaths[0];
      return  (CopexNode) mypath.getLastPathComponent();
   }

   /* returns the selectionned node */
   public TaskTreeNode getTaskSelectedNode(){
      CopexNode node = getSelectedNode();
      if(node instanceof TaskTreeNode)
          return (TaskTreeNode)node;
      return null;
   }

    @Override
    public void mouseClicked(MouseEvent e) {
//        if(popUpMenu != null){
//            popUpMenu.setVisible(false);
//            popUpMenu = null;
//        }
        // double-click : open the dialog
//       if (e.getClickCount() == 2){
//            int row = getRowForLocation(e.getX(), e.getY());
//            if (row == -1)
//                return;
//            edit(false);
//           return;
//        }
        edit(true);
        //openPopUpMenu(e.getX(), e.getY());
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
//        Object o=new Object();
//        try{
//            synchronized(o) { o.wait(100); }
//        }catch(InterruptedException ex) {
//        }
//        if (e.getClickCount() ==1){
//            int row = getRowForLocation(e.getX(), e.getY());
//            if (row == -1)
//                return;
//            edit(true);
//            openPopUpMenu(e.getX(), e.getY());
//        }
        openPopUpMenu(e.getX(), e.getY());
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
    public void edit(boolean oneClick){
        CopexNode currentNode = getSelectedNode();
        if (currentNode == null)
                return;
        if(!isEditingNode && (lastPath == null || (lastPath != null && !lastPath.equals(currentNode)))){
            if (currentNode.isQuestion()){
                lastPath = currentNode;
                setQuestionEditor();
            }else if (currentNode.isHypothesis()){
                lastPath = currentNode;
                setHypothesisEditor();
            }else if (currentNode.isGeneralPrinciple()) {
                lastPath = currentNode;
                setPrincipleEditor();
            }else if (currentNode.isEvaluation()) {
                lastPath = currentNode;
                setEvaluationEditor();
            }
        }else{
            setEditingNode(false);
        }
        if(currentNode.isMaterial()){
            editMaterial();
        }else if(currentNode instanceof TaskTreeNode && !oneClick){
            if (((TaskTreeNode)currentNode).isAction()){
                editAction(((TaskTreeNode)currentNode));
            }else if (((TaskTreeNode)currentNode).isStep()){
                editStep(((TaskTreeNode)currentNode));
            }
        //}else if(currentNode.isManipulation() && currentNode.getChildCount() == 0){
          }
//        else if(currentNode.isManipulation() ){
//            openHelpManipulationDialog();
//        }else if(currentNode.isManipulation() && getLevelTreeDisplay() <2 && currentNode.getChildCount() > 0)
//            displayLevel(1);
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
                 String newHyp = this.owner.updateHypothesis((Hypothesis)node.getNode(), txt, comment, true);
                 //((Hypothesis)node.getNode()).setHypothesis(CopexUtilities.getTextLocal(newHyp,owner.getLocale()));
                 ((Hypothesis)node.getNode()).setHypothesis(newHyp);
                 copexTreeModel.nodeChanged(node);
             }else if(node.isGeneralPrinciple()){
                 if(txt.equals(owner.getBundleString("DEFAULT_TEXT_PRINCIPLE"))){
                     txt = "";
                 }
                 String newPrinc = this.owner.updateGeneralPrinciple((GeneralPrinciple)node.getNode(), txt, comment, true);
                 //((GeneralPrinciple)node.getNode()).setPrinciple(CopexUtilities.getTextLocal(newPrinc,owner.getLocale()));
                 ((GeneralPrinciple)node.getNode()).setPrinciple(newPrinc);
                 copexTreeModel.nodeChanged(node);
             }else if(node.isEvaluation()){
                 String newEval = this.owner.updateEvaluation((Evaluation)node.getNode(), txt, comment, true);
                 //((Evaluation)node.getNode()).setEvaluation(CopexUtilities.getTextLocal(newEval,owner.getLocale()));
                 ((Evaluation)node.getNode()).setEvaluation(newEval);
                 copexTreeModel.nodeChanged(node);
             }else if(node.isQuestion()){
                 if(txt.equals(owner.getBundleString("MSG_QUESTION"))){
                     txt = "";
                 }
                 String newQuestion = this.owner.updateQuestion((Question)node.getNode(), txt, comment, true);
                 //((Question)node.getNode()).setDescription(CopexUtilities.getTextLocal(newQuestion,owner.getLocale()));
                 ((Question)node.getNode()).setDescription(newQuestion);
                 copexTreeModel.nodeChanged(node);
             }
         }
         repaint();
     }

    

    

     public void updateProc(ExperimentalProcedure p){
         this.proc = p;
         this.copexTreeModel.updateProc(p);
         resizeWidth();
     }
     public void updateProc(ExperimentalProcedure p, boolean update){
         this.proc = p;
         this.copexTreeModel.updateProc(p);
     }

     public void updateQuestion(Question q){
         proc.setQuestion(q);
         resizeWidth();
     }

     
     /* view (or not) the comments */
    public void setComments(char displayC){
        markDisplay();
        this.displayComm = displayC;
        copexTreeModel.reload();
        //this.revalidate();
        displayTree();
        this.repaint();
    }


    /* mark the path nodes visible */
    public void markDisplay(){
       // // System.out.println("*****");
        listVisibleNode = getListVisibleNode();
        //// System.out.println("*****");
    }

    /* returns the list of visible nodes */
    private ArrayList<TaskTreeNode> getListVisibleNode(){
       ArrayList<TaskTreeNode> listNodes = new ArrayList();
       int nb = getRowCount();
       for (int i=0; i<nb; i++){
           TreePath path = getPathForRow(i);
           if (path != null){
               CopexNode n = (CopexNode)path.getLastPathComponent() ;
               if(n instanceof TaskTreeNode)
                    listNodes.add((TaskTreeNode)n);
           }
       }
        return listNodes;
    }


    /* set the tree in the same state (visible nodes) */
    public void displayTree(){
        if (listVisibleNode == null)
            return;
        int nbT = listVisibleNode.size();
        for (int i=0; i<nbT; i++){
            this.scrollPathToVisible(new TreePath(listVisibleNode.get(i).getPath()));
        }
    }

    /*
     * returns true if can add a step
     * one node is selectionned
     */
    public boolean canAddE(){
        if (isOnlyOneElementIsSel()){
            // control that it can contains some children
            TaskTreeNode selNode = getTaskSelectedNode();
            if (selNode != null && canAdd(selNode))
                 return true;
        }
        return canAddQuestion();
    }
    
    public boolean canAddAfter(){
        if (isOnlyOneElementIsSel()){
            // control that it can contains some children
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
            // control that it can contains some children
            TaskTreeNode selNode = getTaskSelectedNode();
            if (selNode != null && canAdd(selNode))
                 return true;
        }
        return canAddQuestion();
    }

    /* returns true if manipulation data is selectionned and question can have children*/
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
     * can add action?
     */
    public boolean canAddA(){
        if (isOnlyOneElementIsSel()){
            // control that it can contains some children
            TaskTreeNode selNode = getTaskSelectedNode();
            if (selNode != null && canAdd(selNode) )
                 return true;
        }
        return canAddQuestion();
    }

    /* returns true if the node can have children or if this is an action, if
     the parent node can have children */
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
     * can undo
     *
     */
    public boolean canUndo(){
        return undoManager.canUndo();
    }

    /*
     * can redo
     *
     */
    public boolean canRedo(){
        return undoManager.canRedo();
    }

    /*
     * can copy
     *
     */
    public boolean canCopy(){
        return isElementsSel() && selIsSubTree() && selCanBeCopy();
    }
    /*
     * can paste
     *
     */
    public boolean canPaste(){
        return isOnlyOneElementIsSel() && this.owner.canPaste()  && (canAddA() || canAddE()) &&
                ((this.owner.getSubTreeCopy().isQuestion() && getSelectedNode().isQuestion() ) ||
                (!this.owner.getSubTreeCopy().isQuestion())) && canPasteFromAnotherProc();
    }
    /* returns true if same proc*/
    private boolean canPasteFromAnotherProc(){
        return this.owner.getSubTreeCopy().getOwner().getProc().getDbKey() == this.proc.getDbKey() ;
    }
    /*
     * can delete
     *
     */
    public boolean canSuppr(){
        return isElementsSel() && taskSelected() && selCanBeDelete() && !areActionSelLinkedByMaterial() && !areActionSelLinkedByData();
    }

    private boolean taskSelected(){
        ArrayList<CopexNode> listNode = getSelectedNodes(true);
        if(listNode == null)
            return false;
        for(Iterator<CopexNode> n = listNode.iterator();n.hasNext();){
            if(!(n.next() instanceof TaskTreeNode)){
                return false;
            }
        }
        return true;
    }


    public boolean canEdit(){
        return selCanBeEdit();
    }
    /*
     * can cut
     *
     */
    public boolean canCut(){
        return canSuppr() && canCopy();
    }

    /* returns true if at least one task has a comment */
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

    /* returns false od at least one element sel is in read only */
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
     * some elements of the tree are sel?
     */
    public boolean isElementsSel(){
        return this.getSelectionCount() > 0;
    }
    /*
     * only one element selectionned
     */
    public boolean isOnlyOneElementIsSel(){
        return this.getSelectionCount() == 1;
    }

    /* at least one element sel in the task tree*/
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

    /* open the edition step dialog  */
   public void editStep(TaskTreeNode currentNode){
       ImageIcon img = null ;
//       if (owner.getTaskImage(currentNode.getTask().getTaskImage()) != null)
//           img = new ImageIcon(owner.getTaskImage(currentNode.getTask().getTaskImage()));

       boolean isTaskRepeat = false;
       if(proc instanceof LearnerProcedure){
           isTaskRepeat=  ((LearnerProcedure)this.proc).getInitialProc().isTaskRepeat();
       }else if(proc instanceof InitialProcedure){
           isTaskRepeat = ((InitialProcedure)this.proc).isTaskRepeat();
       }
       StepDialog stepD = new StepDialog(owner, false, !proc.isTaskProc(),(Step)currentNode.getTask(), img, isTaskRepeat,  currentNode.getTask().getEditRight(), this.proc.getRight());
       stepD.setVisible(true);
   }

   /* open the edition action dialog */
   public void editAction(TaskTreeNode currentNode){
       ImageIcon img = null ;
//       if (owner.getTaskImage(currentNode.getTask().getTaskImage()) != null)
//           img = new ImageIcon(owner.getTaskImage(currentNode.getTask().getTaskImage()));
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
       boolean isFreeAction = true;
       boolean isTaskRepeat = false;
       boolean isProcDraw = false;
       ArrayList<InitialNamedAction> listNamedAction = new ArrayList();
       if(proc instanceof LearnerProcedure){
           isFreeAction = ((LearnerProcedure)this.proc).getInitialProc().isFreeAction();
           isTaskRepeat=  ((LearnerProcedure)this.proc).getInitialProc().isTaskRepeat();
           listNamedAction = ((LearnerProcedure)this.proc).getInitialProc().getListNamedAction();
           isProcDraw = ((LearnerProcedure)this.proc).getInitialProc().isTaskDraw();
       }else if(proc instanceof InitialProcedure){
           isFreeAction = ((InitialProcedure)this.proc).isFreeAction();
           isTaskRepeat = ((InitialProcedure)this.proc).isTaskRepeat();
           listNamedAction = ((InitialProcedure)this.proc).getListNamedAction();
           isProcDraw = ((InitialProcedure)this.proc).isTaskDraw();
       }
       ActionDialog actionD = new ActionDialog(owner, false, currentNode.getTask().getDescription(owner.getLocale()),
               currentNode.getTask().getComments(owner.getLocale()), img ,currentNode.getTask().getDraw(), actionNamed,
               currentNode.getTask().getEditRight(),  this.proc.getRight(),
               isFreeAction, listNamedAction,
               owner.getListPhysicalQuantity(), tabParam, materialProd, dataProd,
               isTaskRepeat, currentNode.getTask().getTaskRepeat(), isProcDraw);
       actionD.setVisible(true);
   }

   /* returns the sel nodes and the children */
   public ArrayList<CopexNode> getSelectedNodes(boolean withChildren){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 )
          return null;
      tabPaths = sortTabPath(tabPaths);
      ArrayList<CopexNode> listTreeNode = new ArrayList();
      for (int i=0; i<tabPaths.length; i++){
          CopexNode selNode = ((CopexNode) tabPaths[i].getLastPathComponent());
          if(selNode instanceof TaskTreeNode)
                listTreeNode.add((TaskTreeNode)selNode);
          if(withChildren){
            int nbC = selNode.getChildCount();
            for (int c=0; c<nbC; c++){
                if (!isSelectedNode((CopexNode)selNode.getChildAt(c)))
                    listTreeNode.add((CopexNode)selNode.getChildAt(c));
            }
          }
      }

      return listTreeNode;
   }

   /* returns the sel tasks and the children */
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
                // gets the childs
               ArrayList<CopexTask> lc = getChildTask(selNode, true);
               // add
               int n = lc.size();
               for (int k=0; k<n; k++){
                   // add
                   listT.add(lc.get(k));
               }
            }
      }
      // remove multiples occurences
      int nbT = listT.size();
      ArrayList<CopexTask> lt = new ArrayList();
      ArrayList<CopexTask> listTClean = new ArrayList();
      for (int t=0; t<nbT; t++){
          int id = getId(lt, listT.get(t).getDbKey());
          if (id == -1){
              //// System.out.println("tache du sous arbre : "+listT.get(t).getDescription());
              listTClean.add(listT.get(t));
          }
          lt.add(listT.get(t));
      }
      return listTClean;
   }

   /* returns the selectionned tasks */
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

   /* gets the id in the list, -1 otherwise */
    private int getId(ArrayList<CopexTask> listT, long dbKey){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            CopexTask t = listT.get(i);
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }


    
    /* returns the ts */
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

    /* returns the ts */
    public TaskSelected getTaskSelected(CopexTask selTask, char insertIn){
        return getTaskSelected(getNode(selTask, (CopexNode)copexTreeModel.getRoot()), insertIn);
    }
    /* returns the ts */
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
            // if step is opened or empty => placed at the end of the step
            // id step closed => after the step (brother)

            // if fixed => keep the actual parameters
            if(insertIn == MyConstants.INSERT_TASK_FIX){
                // if 1st child => parent
                // otherwise brother
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
                        // closed step
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

       // determine the brohter which is not selectionned
       CopexNode oldBrother = getOldBrother(selNode, true);
       if (oldBrother != null){
           taskOldBrother = oldBrother.getTask();
       }
       // determines the last brother sel
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
   

    /* returns the selectionned elements */
    public ArrayList<TaskSelected> getTasksSelected(){
        // search the task old brother
        // if the root is selected, => last child of the root
        // if the selection is a step => last chil of the step
        // if the selection is an action : action
        // is selection is a question => last child of the question
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


     /* returns theTaskSelected of a specidfied node for the drag and drop */
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


    /* returns the old brohter */
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


    /* returns the last brother sel, otherwise itself*/
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


    /* return true if it is a selectionned node */
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

   /* returns the list of the children of a node - CopexTask list*/
    private ArrayList<CopexTask> getChildTask(CopexNode node, boolean withChild){
        ArrayList<CopexTask> listChild = new ArrayList();
        int nbC = node.getChildCount();
        for (int c=0; c<nbC; c++){
            CopexNode childNode = (CopexNode)node.getChildAt(c);
            // add the child
            if(childNode instanceof TaskTreeNode){
                listChild.add(((TaskTreeNode)childNode).getTask());
            }
            // add the childs of the child
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



    /* simple selection, returns the selectionned task */
    public TaskSelected getTaskSelected(){
        ArrayList<TaskSelected> listTs = getTasksSelected();
        if (listTs != null && listTs.size() > 0)
            return listTs.get(0);
        else
            return null;
    }
    /* simple selection, returns the selectionned task */
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
    /* returns the arborescence level max */
    public int getLevelTree(){
        int level = getLevelTree((CopexNode)copexTreeModel.getRoot());
        return level-1;
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

    /*  returns the last child of the specified node . If the node doesn't have childre, null */
    private CopexNode getLastChild(CopexNode node){
        int nbC = node.getChildCount();
        if (nbC == 0)
            return null;
        return ((CopexNode)node.getChildAt(nbC-1));
    }
    
    /* add a task */
    public void addTask(CopexTask newTask, TaskSelected ts){
        TaskTreeNode newNode = new TaskTreeNode(newTask);
        if (ts.getTaskBrother() == null){
            // insertion as the fisrt child of the parent
            if (ts.getParentNode() == null){
                // System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : ");
            }
            copexTreeModel.insertNodeInto(newNode, ts.getParentNode(), 0);
        }else{ // after the brother
            //// System.out.println("ajout de la tache apres frere "+ts.getTaskBrother().getDescription());
            CopexNode pn = (CopexNode)ts.getBrotherNode().getParent();
            if (pn == null){
                // System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : "+ts.getBrotherNode().getTask().getDescription(owner.getLocale()));
            }
            copexTreeModel.insertNodeInto(newNode, pn, pn.getIndex(ts.getBrotherNode())+1);
        }
        // developped the path
        this.scrollPathToVisible(new TreePath(newNode.getPath()));
        this.clearSelection();
        this.setSelectionPath(new TreePath(newNode.getPath()));
        owner.updateLevel(getLevelTree());
        revalidate();
        repaint();
    }

    /* add a task */
    public void addTask(CopexTask newTask, TaskSelected ts, boolean brother){
        if (!ts.getSelectedTask().isAction() && brother ){
            TaskTreeNode newNode = new TaskTreeNode(newTask);
            CopexNode pn = (CopexNode)ts.getSelectedNode().getParent();
            if (pn == null){
                // System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : "+ts.getSelectedNode().getTask().getDescription(owner.getLocale()));
            }
            copexTreeModel.insertNodeInto(newNode, pn, pn.getIndex(ts.getSelectedNode())+1);
            // path
            this.scrollPathToVisible(new TreePath(newNode.getPath()));
            owner.updateLevel(getLevelTree());
            revalidate();
            repaint();
        }else if(!ts.getSelectedTask().isAction() && !brother){
            TaskTreeNode newNode = new TaskTreeNode(newTask);
            CopexNode pn = ts.getSelectedNode();
            copexTreeModel.insertNodeInto(newNode, pn, 0);
            // path
            this.scrollPathToVisible(new TreePath(newNode.getPath()));
            owner.updateLevel(getLevelTree());
            revalidate();
            repaint();
        }else
            addTask(newTask, ts);
    }


    /* add a list of tasks */
    public void addTasks(ArrayList<CopexTask> listTask, SubTree subTree, TaskSelected ts){
       CopexTask taskBranch = listTask.get(0);
        //TaskSelected ts = getTaskSelected();
        CopexTask taskBrother = ts.getTaskBrother();
        CopexTask taskParent = ts.getTaskParent();
        // sub tree
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
        // path
        this.scrollPathToVisible(new TreePath(newNode.getPath()));
        owner.updateLevel(getLevelTree());
        revalidate();
        repaint();
    }



    /* gets the node for the specified task */
    public CopexNode getNode(CopexTask task, CopexNode node){
        if(node.isManipulation() && task.isQuestion())
            return node;
       if (node instanceof TaskTreeNode && ((TaskTreeNode)node).getTask().getDbKey() == task.getDbKey())
           return (TaskTreeNode)node;
       else{
           // search in the children
           if (node.getChildCount() > 0){
               for (int k=0; k<node.getChildCount(); k++){
                   CopexNode n = getNode(task, (CopexNode)copexTreeModel.getChild(node, k));
                   if (n != null)
                       return n;
               }
           }
           // search in the brothers
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

    /* update a task */
    public void updateTask(CopexTask newTask, TaskSelected ts){
        undoManager.addEdit(new UpdateTaskUndoRedo(owner, owner.getController(), this, ts.getSelectedNode().getTask(), ts.getSelectedNode(), newTask));
        updateTask(newTask, ts.getSelectedNode());
    }

    public void addEdit_updateQuestion(Question oldQuestion, Question newQuestion){
        undoManager.addEdit(new UpdateTaskUndoRedo(owner, owner.getController(), this, oldQuestion, (CopexNode)treeModel.getRoot(), newQuestion));
    }
    /* update a task*/
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

    /* set the level arborescence
     * level 1: root and its children (non deployed)
     */
    public void displayLevel(int level){
        level = level+1;
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
       }else { 
           collapsePath(new TreePath(node.getPath()));
            for (int i=0; i<nbC; i++){
                collapsePath(new TreePath((CopexNode)node.getChildAt(i)));
             }
       }
       this.scrollPathToVisible(new TreePath(node.getPath()));
    }

    /* delete nodes*/
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

    /* delete node */
    private void suppr(CopexNode node){
        if(node==null)
            return;
        try{
            copexTreeModel.removeNodeFromParent(node);
        }catch(IllegalArgumentException e){
            // System.out.println("Suppression d'un noeud qui n'est plus dans l'arbre "+node);
        }
        if (!node.isAction()){
            // etape ou sous question on supprime egalement tous les enfants
            int nbC = node.getChildCount();
            for (int c=nbC-1; c==0; c--){
                suppr((TaskTreeNode)node.getChildAt(c));
            }
        }
    }

    /* returns the level displayed */
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




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C && canCopy()){
            owner.copy();
        }else if (e.getKeyCode() == KeyEvent.VK_M && canAddE()){
            owner.addEtape();
        }else if (e.getKeyCode() == KeyEvent.VK_N && canAddA()){
            owner.addAction();
        }else if (e.getKeyCode() == KeyEvent.VK_X && canCut()){
            owner.cut();
        }else if (e.getKeyCode() == KeyEvent.VK_V && canPaste()){
            owner.pasteUnder();
        }else if (e.getKeyCode() == KeyEvent.VK_DELETE && canSuppr()){
            this.owner.suppr();
        }else if (e.getKeyCode() == KeyEvent.VK_E && isOnlyOneElementIsSel()){
            this.edit(false);
        }
    }

   /* rename a procedure */
    public void updateProcName(String name){
        //proc.setName(CopexUtilities.getTextLocal(name,owner.getLocale()));
        proc.setName(name);
    }

    /* returns the sub tree to be copied  */
    public SubTree getSubTreeCopy(boolean dragNdrop){
        // on selectionne egalement les taches enfants des etapes / ss question
        SubTree st = new SubTree(owner, owner.getController(), proc, this, getSelectedTasks(), getSelectedNodes(true), dragNdrop);
        return st;
    }

    /* sort of the selection */
    private TreePath[] sortTabPath(TreePath[] tabPaths){
        // sort by level in the tree
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

    /* returns the position in the tree */
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


    /* returns the last child - otherwise itself*/
    private CopexNode getSubLastChild(CopexNode node){
        int nbC = node.getChildCount();
        if (nbC == 0)
            return node;
        else{
            return getSubLastChild((CopexNode)node.getChildAt(nbC-1));
        }
    }

    /* returns true if all elements are a sub tree  */
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

     /* returns true if all elements selected can be copied  */
    public  boolean selCanBeCopy(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexNode node = (CopexNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canCopy())
                    return false;
            // in the childs
            for (int k=0; k<node.getChildCount(); k++){
                CopexNode n = (CopexNode)node.getChildAt(k);
                if (!n.canCopy())
                    return false;
            }
        }
        return true;
    }

     /* returns true if all elements can be deleted */
    public  boolean selCanBeDelete(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexNode node = (CopexNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canDelete())
                return false;
            // in the childs
            for (int k=0; k<node.getChildCount(); k++){
                CopexNode n = (CopexNode)node.getChildAt(k);
                if (!n.canDelete())
                    return false;
            }
        }
        return true;
    }

    /* returns true if selected elements can be edited  */
    public  boolean selCanBeEdit(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexNode node = (CopexNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canEdit())
                return false;
            // in childs
            for (int k=0; k<node.getChildCount(); k++){
                CopexNode n = (CopexNode)node.getChildAt(k);
                if (!n.canEdit())
                    return false;
            }
        }
        return true;
    }

    /* returns true if selected elements can be moved  */
    public  boolean selCanBeMove(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexNode node = (CopexNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canMove())
                return false;
            // in childs
            for (int k=0; k<node.getChildCount(); k++){
                CopexNode n = (CopexNode)node.getChildAt(k);
                if (!n.canMove())
                    return false;
            }
        }
        return true;
    }

    /* move a sub tree */
    public boolean moveSubTree(SubTree subTree, CopexNode node, boolean brother){
        TaskSelected ots ;
        TaskSelected t = getTaskSelected(subTree.getFirstTask());
        ots = getTaskSelected(t.getTaskToAttach(), MyConstants.INSERT_TASK_FIX);
        TaskSelected ts = getSelectedTask(node, brother);
       // control that the selected task is not in the tree
       boolean control = control(node, subTree);
       if (!control)
           return false;
       CopexReturn cr = owner.getController().move(ts, subTree, MyConstants.NOT_UNDOREDO);
        if (cr.isError()){
            owner.displayError(cr, owner.getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        // update gui
        addTasks(subTree.getListTask(), subTree, ts);
        this.subTree = subTree;
        this.newTs = ts;
        this.oldTs = ots;
        this.owner.setModification();
        return true;
    }

    /*control that the selected task is not in the sub tree */
    private boolean control(CopexNode node, SubTree subTree){
        return !subTree.containNode(node);
    }


    /* remove tasks */
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
        // update nodes
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

    /*add event: rename proc*/
    public void addEdit_renameProc(ExperimentalProcedure proc, String name){
        this.undoManager.addEdit(new UpdateProcNameUndoRedo(owner, owner.getController(), this, proc, proc.getName(owner.getLocale()), name));
    }

    public void addEdit_addTask(TaskSelected task, TaskSelected ts){
        this.undoManager.addEdit(new AddTaskUndoRedo(owner, owner.getController(), this, task, ts));
    }

    public void addEdit_deleteTask(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs){
        this.undoManager.addEdit(new DeleteTaskUndoRedo(owner, owner.getController(), this, listTask, listTs));
    }



     /* cut */
      public void addEdit_cut(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs, SubTree subTree){
        this.undoManager.addEdit(new CutUndoRedo(owner, owner.getController(), this, listTask, listTs, subTree));
    }

      /* paste */
     public void addEdit_paste(SubTree subTree, TaskSelected ts, ArrayList<TaskSelected> listTask){
        this.undoManager.addEdit(new PasteUndoRedo(owner, owner.getController(), this, subTree, ts, listTask));
    }

      /* drag and drop */
     public void addEdit_dragDrop(SubTree subTree, TaskSelected ts, TaskSelected oldTs){
        this.undoManager.addEdit(new DragAndDropUndoRedo(owner, owner.getController(), this, subTree, ts, oldTs));
    }


     /* begin register */
     public void beginregister(){
         this.register = true;
     }

    @Override
    protected void setExpandedState(TreePath path, boolean state) {
        super.setExpandedState(path, state);
        if (register){
            CopexNode node = (CopexNode)path.getLastPathComponent();
            // get all childs
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

   

    /* resize tree */
    public void resizeWidth(){
//        if(listVisibleNode != null && listVisibleNode.size() == 0)
            markDisplay();
        isEditingNode = false;
        copexTreeModel.reload();
        displayTree();
        revalidate();
        repaint();
    }

    /* returns the last child of the node, itself otherwise */
    private CopexNode getLastChildren(CopexNode node){
        CopexNode childNode = getLastChild(node);
        CopexNode lastChild = childNode == null ? node : childNode;
        while (childNode != null){
            childNode = getLastChild(childNode);
            lastChild = childNode == null ? lastChild : childNode;
        }
        return lastChild;

    }

    /* returns list of tasks which are before the selected task (including itself) */
      public ArrayList<CopexTask> getListTaskBeforeSel(boolean modeAdd){
          ArrayList<CopexTask> listTaskBefore = new ArrayList();
          // this is not the sel node, but the insert node (idf mode = add)
          CopexNode selNode = getTaskSelectedNode();
          if (selNode == null)
              return listTaskBefore;
          if (modeAdd){
              TaskSelected ts = getTaskSelected(selNode);
              if (ts.getTaskBrother() == null){
                  // add to parent
                  selNode = ts.getParentNode() ;
              }else{
                  // add as brother
                  selNode = getLastChildren(ts.getBrotherNode()) ;
              }
          }
          ArrayList v= new ArrayList();
          //return getListTaskBeforeSelNode(modeAdd,(TaskTreeNode)treeModelProc.getRoot(), selNode, v);
          //listTaskBefore = getListTaskBeforeSelNode(modeAdd,(CopexNode)copexTreeModel.getRoot(), selNode, v);
          listTaskBefore = getListTaskBeforeSelNode(modeAdd,copexTreeModel.getManipulationNode(), selNode, v);
          return listTaskBefore;
      }

      /* returns the list of tasks before selected node , in v[0] returns true if we found the task */
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

      /* returns true id selected tasks are not linked via material to other tasks */
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

      /* return s true if the selected tasks are not linked via data to others tasks */
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

      /* returns true if at least an action uses material prod by this task */
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


       /* returns true if at least an action uses data prod; by this task  */
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


      /* returns true if the move of the sub tree is a problem from a point of view of the material prod  */
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

       /* returns true if the move of the subtree is a problem from a point of view of data prod */
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



    /* returns true if the material is created in the list of tasks  */
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

    /* returns true if the data is created in the list of tasks  */
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

    /* set mouseIver to false in the tree*/
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

    /* auto open of the edit question dialog  */
    public void openQuestionDialog(){
        setSelectionPath(new TreePath(copexTreeModel.getRoot()));
        edit(false);
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

    private void openPopUpMenu(int x, int y){
        popUpMenu = null;
        ArrayList<CopexNode> listNodes = getSelectedNodes(false);
        if(listNodes == null)
            return;
        char modeNode = MyConstants.POPUPMENU_UNDEFINED;
        boolean manipulation = false;
        if(getSelectedNode() != null && getSelectedNode().isManipulation()  ){
            listNodes = new ArrayList();
            listNodes.add(getSelectedNode());
            modeNode = MyConstants.POPUPMENU_MANIPULATION;
            manipulation = true;
        }
        if(listNodes.isEmpty())
            return;
        char mode = proc.isTaskProc()?MyConstants.POPUPMENU_TASK:MyConstants.POPUPMENU_STEP_ACTION;
        if(listNodes.size() > 1){
            modeNode = MyConstants.POPUPMENU_MULTINODE;
        }else if(!manipulation){
            for(Iterator<CopexNode> n = listNodes.iterator();n.hasNext();){
                CopexNode node = n.next();
                if(node.isAction()){
                    modeNode = MyConstants.POPUPMENU_ACTION;
                }else{
                    modeNode = MyConstants.POPUPMENU_STEP;
                    if(proc.isTaskProc()){
                        modeNode = MyConstants.POPUPMENU_TASK;
                    }
                }
            }
        }
        popUpMenu = new CopexPopUpMenu(owner, owner.getController(), this, mode, modeNode);
        popUpMenu.setInsertAfterEnabled(canAddAfter()) ;
        popUpMenu.setInsertInEnabled(canAddIn());
        popUpMenu.setCutItemEnabled(canCut());
        popUpMenu.setCopyItemEnabled(canCopy());
        popUpMenu.setPasteItemEnabled(canPaste());
        popUpMenu.setSupprItemEnabled(canSuppr());
        popUpMenu.setEditItemEnabled(canEdit() && isOnlyOneElementIsSel());
        popUpMenu.init();
        double w = popUpMenu.getPreferredSize().getWidth();
        double h = popUpMenu.getPreferredSize().getHeight();
        if(x+w > (owner.getWidth())){
            x = (int)(x-w);
        }
        if(y+h > (owner.getHeight())){
            y = (int)(y-h);
        }
        popUpMenu.show(this, x, y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(popUpMenu != null){
            popUpMenu.setVisible(false);
            popUpMenu = null;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(popUpMenu != null){
            popUpMenu.setVisible(false);
            popUpMenu = null;
        }
    }

    @Override
    public void startEditingAtPath(TreePath path) {
        super.startEditingAtPath(path);
        this.isEditingNode = true;
    }

    public void setEditingNode(boolean b){
        this.isEditingNode = b;
    }
}
