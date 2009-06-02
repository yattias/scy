/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;


import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.dnd.SubTree;
import eu.scy.tools.copex.dnd.TreeTransferHandler;
import eu.scy.tools.copex.undoRedo.*;
import eu.scy.tools.copex.utilities.*;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.ArrayList;
import javax.swing.undo.CannotUndoException;
import org.jdom.Element;

/**
 * représente l'arbre d'un protocole
 * @author MBO
 */
public class CopexTree extends JTree implements MouseListener, KeyListener{

    // ATTRIBUTS
    /*
     * protocole représenté sous forme d'arbre
     */
    protected CopexTreeModel treeModelProc;
    /*
     * représentation des noeuds
     */
    private MyTreeCellRenderer aMyDefaultTreeCellRenderer;
    /*
     * fenetre mere
     */
    private EdPPanel edP;
    /* controller */
    private ControllerInterface controller;
    /*
     * protocole
     */
    private LearnerProcedure proc;
    /* protocole initial */
    private InitialProcedure initProc;
    
    // liste des elements selectionnes
    private ArrayList<CopexTreeNode> listSel;
    // ecoute des evenements souris
    private MyTreeSelectionListener selectionListener;
    // mode affichage commentaire 
    private char displayComm = MyConstants.COMMENTS;
    /* pop up menu */
    private CopexPopUpMenu popUpMenu = null;
    /* Trandfer Handler */
    private TreeTransferHandler transferHandler;
    /* liste des noeuds visibles à un moment donné */
    private ArrayList<CopexTreeNode> listVisibleNode;
    /* gestionnaire de undo redo */
    private CopexUndoManager undoManager;
    /*marquage du undo redo */
    private SubTree subTree;
    private TaskSelected newTs;
    private TaskSelected oldTs;
    /*enregistrement etat taches*/
    private boolean register;
    /* taille panel mere */
    private int panelOwnerWidth;
    

    // CONSTRUCTEURS
    public CopexTree(EdPPanel edP, LearnerProcedure proc, InitialProcedure initProc, ControllerInterface controller, int panelOwnerWidth) {
        this.edP = edP;
        this.proc = proc;
        this.panelOwnerWidth = panelOwnerWidth ;
        this.initProc = initProc ;
        this.controller = controller;
        this.listSel = new ArrayList();
        this.register = false;
        // creation du modele
        treeModelProc = new CopexTreeModel(proc, proc.getListTask() );
        
        // ecoute des evenements souris
        this.selectionListener = new MyTreeSelectionListener (this);
        addTreeSelectionListener(selectionListener);
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setModel(treeModelProc);
        aMyDefaultTreeCellRenderer = new MyTreeCellRenderer(this);
        this.setCellRenderer(aMyDefaultTreeCellRenderer);
        //this.setRowHeight(aMyDefaultTreeCellRenderer.getHeight());
        this.setRowHeight(0);
        this.setUI(new MyBasicTreeUI(this));
        this.setEditable(false);
        // DRAG AND DROP 
        setDragEnabled(true);
        transferHandler  = new TreeTransferHandler();
        setTransferHandler(transferHandler);
        setDropMode(DropMode.INSERT);
        setToggleClickCount(0);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        setShowsRootHandles(false);
        ToolTipManager.sharedInstance().registerComponent(this);
        undoManager = new CopexUndoManager(this);
        initTree();
    }

    

    
    // GETTER AND SETTER 
    public LearnerProcedure getProc(){
        return this.proc;
    }
    // OPERATIONS
    public ImageIcon getQuestionImageIcon(){
        return this.edP.getCopexImage("Icone-AdT_question.png");
    }
     public ImageIcon getActionImageIcon(){
        return this.edP.getCopexImage("Icone-AdT_action.png");
    }
      public ImageIcon getStepImageIcon(){
        return this.edP.getCopexImage("Icone-AdT_etape.png");
    }
    public ImageIcon getStepWarningImageIcon(){
        return this.edP.getCopexImage("Icone-AdT_etape_warn.png");
    }
    public ImageIcon getStepReadOnlyImageIcon(){
        return this.edP.getCopexImage("Icone-AdT_etape_lock.png");
    }  
    
    public ImageIcon getActionReadOnlyImageIcon(){
        return this.edP.getCopexImage("Icone-AdT_action_lock.png");
    } 
    public String getBundleString(String s){
        return edP.getBundleString(s);
    }
   
    // METHODES
    /*
     * l'ajout d'une sous question est il possible ?
     * si 1 et 1 seul élément est sélectionné et s'il set possible d'insérer une sous question (fils d'une sous question)
     */
    public boolean canAddQ(){
        if (isOnlyOneElementIsSel()){
            // controle que cet element est bien une question / racine
            CopexTreeNode selNode = getSelectedNode();
            if (selNode != null && (selNode.isRoot() || selNode.isQuestion()) && canAdd(selNode))
                 return true;
        }
        return false;
    }
   
    /*
     * l'ajout d'une etape est il possible ?
     * si 1 et 1 seul élément est sélectionné 
     */
    public boolean canAddE(){
        if (isOnlyOneElementIsSel()){
            // controle que cet element peut avoir des enfants
            CopexTreeNode selNode = getSelectedNode();
            if (selNode != null && canAdd(selNode))
                 return true;
        }
        return false;
    }

    /*
     * l'ajout d'une action est il possible ?
     * si 1 et 1 seul élément est sélectionné 
     */
    public boolean canAddA(){
        if (isOnlyOneElementIsSel()){
            // controle que cet element peut avoir des enfants
            CopexTreeNode selNode = getSelectedNode();
            if (selNode != null && canAdd(selNode) )
                 return true;
        }
        return false;
    }
    /* retourne vrai si le noeud peut avoir des enfans ou si c'est une action, si le noeud parent peut avoir des enfants */
    private boolean canAdd(CopexTreeNode node){
        if (!node.isAction() && node.canBeParent())
            return true;
        else if (node.isAction()){
            CopexTreeNode parentNode = (CopexTreeNode)node.getParent();
            if (parentNode != null && parentNode.canBeParent())
                return true;
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
        return isOnlyOneElementIsSel() && this.edP.canPaste()  && canAdd(getSelectedNode()) && 
                ((this.edP.getSubTreeCopy().isQuestion() && getSelectedNode().isQuestion() ) ||
                (!this.edP.getSubTreeCopy().isQuestion())) && canPasteFromAnotherProc();
    }
    /* retourne vrai si meme proc*/
    private boolean canPasteFromAnotherProc(){
        return this.edP.getSubTreeCopy().getOwner().getProc().getDbKey() == this.proc.getDbKey() ;
    }
    /*
     * suppr possible
     *  
     */
    public boolean canSuppr(){
        return isElementsSel() && !rootIsSelected() && selCanBeDelete() && !areActionSelLinkedByMaterial() && !areActionSelLinkedByData();
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
        return isComments((CopexTreeNode)treeModelProc.getRoot());
    }

    private boolean  isComments(CopexTreeNode node){
       if (node.getTask().getComments() != null && !node.getTask().getComments().equals(""))
           return true;
        int nbC = treeModelProc.getChildCount(node);
       if (nbC > 0){
           for (int i=0; i<nbC; i++){
               boolean isC = isComments((CopexTreeNode)node.getChildAt(i));
               if (isC)
                   return true;
           }
       }
       return false;
    }


    /* renvoit true si la racine est selectionnée */
    private boolean rootIsSelected(){
        ArrayList<CopexTreeNode> listN = getSelectedNodes();
        if (listN == null )
            return false;
        int nbN = listN.size();
        for (int k=0; k<nbN; k++){
            CopexTreeNode node = listN.get(k);
            if (node.isRoot()){
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
            CopexTreeNode n = (CopexTreeNode)tabPaths[i].getLastPathComponent();
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
    
    // des noeuds sont selectionnes sur l'arbre 
    public void selectNode(){
        edP.updateMenu();
    }

     // retourne le texte d'un objets
   public String getDescriptionValue(Object value){
       if (value instanceof CopexTreeNode ){
            CopexTreeNode node =  (CopexTreeNode)value;
            if (node == null || node.getTask() == null)
                    return "";

            if (node.getTask() instanceof CopexAction ){
                    return ((CopexAction)node.getTask()).toDescription(edP) ;
            }else
                return node.getTask().getDescription() ;
       }else
            return "";
   }

    // retourne le commentaire d'un objets
   public String getCommentValue(Object value){
       if (this.displayComm == MyConstants.NO_COMMENTS)
           return "";
       if (value instanceof CopexTreeNode ){
            CopexTreeNode node =  (CopexTreeNode)value;
            if (node == null || node.getTask() == null)
                    return "";
            if (node.getTask().isQuestion()){
                String s = node.getTask().getComments();
               String hyp = ((Question)node.getTask()).getHypothesis() ;
               if (hyp != null && hyp.length() > 0){
                   if (s.length()> 0)
                       s += "\n";
                   s += edP.getBundleString("LABEL_HYPOTHESIS")+" "+hyp;
               }
               String princ = ((Question)node.getTask()).getGeneralPrinciple() ;
               if (princ != null && princ.length() > 0){
                   if (s.length()> 0)
                       s += "\n";
                   s += edP.getBundleString("LABEL_GENERAL_PRINCIPLE")+" : "+princ;
               }
               return s;
            }
            return node.getTask().getComments();
       }else
        return "";
   }
   
    // retourne l'image d'un objet
   public ImageIcon getImageValue(Object value){
      if (value instanceof CopexTreeNode ){
            CopexTreeNode node =  (CopexTreeNode)value;
            if (node == null || node.getTask() == null || node.getTask().getTaskImage() == null  )
                    return null;
            Image img = null;
            if (node.getTask().getTaskImage() != null && !node.getTask().getTaskImage().equals("")){
                img = this.edP.getTaskImage("mini_"+node.getTask().getTaskImage()) ;
            }
            if (img == null)
                return null ;
            return new ImageIcon(img);
        
       }else
        return null;
   }

    // retourne le dessin d'un objet
   public Element getTaskDraw(Object value){
      if (value instanceof CopexTreeNode ){
            CopexTreeNode node =  (CopexTreeNode)value;
            if (node == null || node.getTask() == null  )
                    return null;
            return node.getTask().getDraw() ;
       }else
        return null;
   }

   /* ouverture fenetre d'edition de la question */
   public void editQuestion(CopexTreeNode currentNode){
       ImageIcon img = null ;
       if (edP.getTaskImage(currentNode.getTask().getTaskImage()) != null)
           img = new ImageIcon(edP.getTaskImage(currentNode.getTask().getTaskImage()));
       QuestionDialog questD = new QuestionDialog(edP,  currentNode.getTask().getEditRight(), false,   currentNode.getTask().getDescription(), ((Question)(currentNode.getTask())).getHypothesis(), currentNode.getTask().getComments(), ((Question)(currentNode.getTask())).getGeneralPrinciple(), img, this.proc.getRight());
       questD.setVisible(true);
   }

   /* selection de la question principale et edition */
   public void editQuestion(){
       this.clearSelection();
       this.setSelectionPath(new TreePath(treeModelProc.getRoot()));
       editQuestion((CopexTreeNode)treeModelProc.getRoot());
   }
   
   /* ouverture de la fenetre d'edition de l'etape */
   public void editStep(CopexTreeNode currentNode){
       ImageIcon img = null ;
       if (edP.getTaskImage(currentNode.getTask().getTaskImage()) != null)
           img = new ImageIcon(edP.getTaskImage(currentNode.getTask().getTaskImage()));
       
       StepDialog stepD = new StepDialog(edP, false, currentNode.getTask().getDescription(), currentNode.getTask().getComments(), img, currentNode.getTask().getEditRight(), this.proc.getRight());
       stepD.setVisible(true);
   }
   
   /* ouverture de la fenetre d'edition de l'action */
   public void editAction(CopexTreeNode currentNode){
       ImageIcon img = null ;
       if (edP.getTaskImage(currentNode.getTask().getTaskImage()) != null)
           img = new ImageIcon(edP.getTaskImage(currentNode.getTask().getTaskImage()));
       InitialNamedAction actionNamed = null;
       ActionParam[] tabParam = null;
       ArrayList<Material> materialProd = null;
       ArrayList<QData> dataProd = null;
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
       ActionDialog2 actionD = new ActionDialog2(edP, false, currentNode.getTask().getDescription(), currentNode.getTask().getComments(), img ,currentNode.getTask().getDraw(), actionNamed, currentNode.getTask().getEditRight(),  this.proc.getRight(), this.initProc.isFreeAction(), this.initProc.getListNamedAction(), edP.getListPhysicalQuantity(), tabParam, materialProd, dataProd);
       actionD.setVisible(true);
   }
   
   /* retourne le noeud selectionne */
   public CopexTreeNode getSelectedNode(){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 || tabPaths.length > 1)
          return null;
      TreePath mypath = tabPaths[0];
      return  (CopexTreeNode) mypath.getLastPathComponent();
   }
   
   /* retourne les noeuds selectionnés + enfants */ 
   public ArrayList<CopexTreeNode> getSelectedNodes(){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 )
          return null;
      tabPaths = sortTabPath(tabPaths);
      ArrayList<CopexTreeNode> listTreeNode = new ArrayList();
      for (int i=0; i<tabPaths.length; i++){
          CopexTreeNode selNode = ((CopexTreeNode) tabPaths[i].getLastPathComponent());
          listTreeNode.add(selNode);
          int nbC = selNode.getChildCount();
          for (int c=0; c<nbC; c++){
              if (!isSelectedNode((CopexTreeNode)selNode.getChildAt(c)))
                listTreeNode.add((CopexTreeNode)selNode.getChildAt(c));
          }
      }
      
      return listTreeNode;
   }
  
   /* retourne les taches selectionnés + enfants */ 
   public ArrayList<CopexTask> getSelectedTasks(){
      TreePath[] tabPaths = this.getSelectionPaths();
      if (tabPaths == null || tabPaths.length == 0 )
          return null;
      tabPaths = sortTabPath(tabPaths);
      ArrayList<CopexTask> listT = new ArrayList();
      for (int i=0; i<tabPaths.length; i++){
          CopexTreeNode selNode = ((CopexTreeNode) tabPaths[i].getLastPathComponent());
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
   
   /* cherche un indice dans la liste, -1 si non trouvé */
    private int getId(ArrayList<CopexTask> listT, long dbKey){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            CopexTask t = listT.get(i);
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
   
  
   
   
   /* edit */
   public void edit(){
       CopexTreeNode currentNode = getSelectedNode();
        if (currentNode == null)
                return;
        if (currentNode.isAction() ){
            editAction(currentNode);
        }else if (currentNode.isStep()){
            editStep(currentNode);
        }else if (currentNode.isQuestion()){
            editQuestion(currentNode);
        }
       
   }
   
   
   
    public void mouseClicked(MouseEvent e) {
        // double clic : on ouvre la fenetre d'edition
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
            if (!isElementsSel()) 
               return;
            getPopUpMenu();
           boolean alone =  isOnlyOneElementIsSel();
            
            popUpMenu.setAddMenuEnabled(alone);
            popUpMenu.setAddQEnabled(canAddQ());
            popUpMenu.setAddSEnabled(canAddE());
            popUpMenu.setAddAEnabled(canAddA());
            popUpMenu.setCutItemEnabled(canCut());
            popUpMenu.setCopyItemEnabled(canCopy());
            popUpMenu.setPasteItemEnabled(canPaste());
            popUpMenu.setSupprItemEnabled(canSuppr());
            popUpMenu.setEditItemEnabled(alone);
            this.popUpMenu.show(this, x, y);
           
        }
    }

    public void mousePressed(MouseEvent e) {
        
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }
    
    /* affichage ou non des commentaires */
    public void setComments(char displayC){
        markDisplay();
        this.displayComm = displayC;
        treeModelProc.reload();
        //this.revalidate();
        displayTree();
        this.repaint();
    }
    
    
    
    
    /* creation du pop up menu */
    private CopexPopUpMenu getPopUpMenu(){
        if (popUpMenu == null){
            popUpMenu = new CopexPopUpMenu(edP, edP.getController(), this);
        }
        return popUpMenu;
    }
    /* retourne la ts */
    private TaskSelected getTaskSelected(CopexTreeNode selNode){
        if (selNode == null)
            return null;
        CopexTask taskSel = null;
        CopexTask taskBrother = null;
        CopexTask taskParent = null;
        CopexTask parentTask = null;
        CopexTask taskOldBrother = null;
        CopexTreeNode brotherNode = null;
        CopexTreeNode parentNode = null;
        CopexTreeNode oldBrotherNode = null;
        CopexTreeNode lastBrotherNode = null;
        ArrayList<CopexTask> listAllChildren = new ArrayList();
        taskSel = selNode.getTask();
            
       if (selNode.isAction()){
            taskBrother = selNode.getTask();
            brotherNode = selNode;
       } else {
            listAllChildren = getChildTask(selNode, true);
            brotherNode =  getLastChild(selNode);
            // si etape ouverte ou vide => place en fin d'etape
            // si etape fermee => apres etape (frere)
            if (brotherNode == null){
                  taskParent = selNode.getTask();
                  parentNode = selNode;
             }else{
                 taskBrother = brotherNode.getTask();
                 if (!taskBrother.isVisible()){
                     // etape fermee
                     brotherNode = selNode;
                     taskBrother = brotherNode.getTask();
                 }
             }
        }
            
       // determine le grand frere qui n'est pas sel
       CopexTreeNode oldBrother = getOldBrother(selNode, true);
       if (oldBrother != null)
               taskOldBrother = oldBrother.getTask();
       // determine le dernier frere selectionne
       lastBrotherNode = getLastBrotherNode(selNode);
       if (selNode.getParent() != null)
           parentTask = ((CopexTreeNode)selNode.getParent()).getTask();
       CopexTreeNode oldB = getOldBrother(selNode, false);
       CopexTask oldBT = null;
       if (oldB != null){
           oldBT = oldB.getTask();
       }
       TaskSelected ts = new TaskSelected(proc, taskSel, taskBrother, taskParent, taskOldBrother, selNode, brotherNode, parentNode, oldBrotherNode, listAllChildren, parentTask, lastBrotherNode, oldBT);
       return ts;
    }
    
    public CopexTreeNode getNode(CopexTask task){
        return getNode(task, (CopexTreeNode)treeModelProc.getRoot());
    }
    public  TaskSelected getTaskSelected(CopexTask task){
        return getTaskSelected(getNode(task, (CopexTreeNode)treeModelProc.getRoot()));
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
            TaskSelected ts = getTaskSelected((CopexTreeNode)tabPaths[i].getLastPathComponent());
            if (ts != null)
                listTaskSelected.add(ts);
        }
        return  listTaskSelected;
    }
    
    
     /* renvoit la TaskSelected d'un noeud donné pour un drag and drop */
   private TaskSelected getSelectedTask(CopexTreeNode node, boolean brother){
       CopexTask taskBrother = null;
       CopexTask taskParent = null;
       CopexTask parentTask = null;
       CopexTask taskOldBrother = null;
       CopexTreeNode brotherNode = null;
       CopexTreeNode parentNode = null;
       CopexTreeNode oldBrotherNode = null;
       CopexTreeNode lastBrotherNode = null;
       ArrayList<CopexTask> listAllChildren = new ArrayList();
       CopexTask taskSel = node.getTask();
            
       if (node.isAction()){
             taskBrother = node.getTask();
             brotherNode = node;
        }  else {
           listAllChildren = getChildTask(node, true);
           if (brother)
               taskBrother = node.getTask();
           else{
               taskParent = node.getTask();
                parentNode = node;
           }
            
       }
            
       if (node.getParent() != null)
            parentTask = ((CopexTreeNode)node.getParent()).getTask();
       CopexTreeNode oldB = getOldBrother(node, false);
       CopexTask oldBT = null;
       if (oldB != null)
           oldBT = oldB.getTask();
        TaskSelected ts = new TaskSelected(proc, taskSel, taskBrother, taskParent, taskOldBrother, node, brotherNode, parentNode, oldBrotherNode, listAllChildren, parentTask, lastBrotherNode, oldBT);
        return ts ;
   }
   
   
    /* retourne le grand frere */
    private CopexTreeNode getOldBrother(CopexTreeNode node, boolean sel){
        CopexTreeNode oldBrotherNode = null;
        CopexTreeNode p = (CopexTreeNode)node.getParent();
        if (p != null){
              oldBrotherNode = (CopexTreeNode)p.getChildBefore(node);
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
    private CopexTreeNode getLastBrotherNode(CopexTreeNode node){
        CopexTreeNode p = (CopexTreeNode)node.getParent();
        if (p != null){
            int nbC = p.getChildCount();
            int id = p.getIndex(node);
            for (int i=nbC-1; i>id; i--){
                CopexTreeNode t = (CopexTreeNode)p.getChildAt(i);
                if (isSelectedNode(t)){
                    return t;
                }
            }
            return node;
        }
        return node;
    }
    
    
    /* retourne vrai s'il s'agit d'un noeud selectionne */
    private boolean isSelectedNode(CopexTreeNode node){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
            return false;
        for (int i=0; i<tabPaths.length; i++){
            CopexTreeNode selNode = (CopexTreeNode) tabPaths[i].getLastPathComponent();
            if (selNode == node)
                return true;
        }
        return false;
    }
            
   /* retourne la liste des enfants d'un noeud - sous forme de CopexTask  */
    private ArrayList<CopexTask> getChildTask(CopexTreeNode node, boolean withChild){
        ArrayList<CopexTask> listChild = new ArrayList();
        int nbC = node.getChildCount();
        for (int c=0; c<nbC; c++){
            CopexTreeNode childNode = (CopexTreeNode)node.getChildAt(c);
            // ajoute l'enfant
            listChild.add(childNode.getTask());
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
    /* retourne le niveau arborescence max */
    public int getLevelTree(){
        return getLevelTree((CopexTreeNode)treeModelProc.getRoot());
    }
    
    private int getLevelTree(CopexTreeNode node){
        int level = node.getLevel();
        int nbC = treeModelProc.getChildCount(node);
       if (nbC > 0){
           for (int i=0; i<nbC; i++){
               int l = getLevelTree((CopexTreeNode)node.getChildAt(i));
               if (l > level)
                   level = l;
           }
       }
       return level;
    }
    
    /* retourne le dernier enfant du noeud passe en parametre 
     * si le noeud n'a pas d'enfant, null
     */
    private CopexTreeNode getLastChild(CopexTreeNode node){
        int nbC = node.getChildCount();
        if (nbC == 0)
            return null;
        return ((CopexTreeNode)node.getChildAt(nbC-1));
    }
    
    
    /* mise à jour du protocole */
    public void updateProc(LearnerProcedure newProc){
        this.proc = newProc;
        treeModelProc.updateProc(newProc);
        
    }
    /* ajout d'une tache */
    public void addTask(CopexTask newTask, TaskSelected ts){
        CopexTreeNode newNode = new CopexTreeNode(newTask);
        if (ts.getTaskBrother() == null){
            // insertion en tant que premier enfant du parent 
            if (ts.getParentNode() == null){
                System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : ");
            }
            treeModelProc.insertNodeInto(newNode, ts.getParentNode(), 0);
        }else{ // apres le frere
            //System.out.println("ajout de la tache apres frere "+ts.getTaskBrother().getDescription());
            CopexTreeNode pn = (CopexTreeNode)ts.getBrotherNode().getParent();
            if (pn == null){
                System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : "+ts.getBrotherNode().getTask().getDescription());
            }
            treeModelProc.insertNodeInto(newNode, pn, pn.getIndex(ts.getBrotherNode())+1);
        }
        // developpe le chemin
        this.scrollPathToVisible(new TreePath(newNode.getPath()));
        this.clearSelection();
        this.setSelectionPath(new TreePath(newNode.getPath()));
        edP.updateLevel(getLevelTree());
        revalidate();
        repaint();
    }
    
    /* ajout d'une tache */
    public void addTask(CopexTask newTask, TaskSelected ts, boolean brother){
        if (!ts.getSelectedTask().isAction() && brother ){
            CopexTreeNode newNode = new CopexTreeNode(newTask);
            CopexTreeNode pn = (CopexTreeNode)ts.getSelectedNode().getParent();
            if (pn == null){
                System.out.println("ERREUR LORS DE L'AJOUT D'UNE TACHE : "+ts.getSelectedNode().getTask().getDescription());
            }
            treeModelProc.insertNodeInto(newNode, pn, pn.getIndex(ts.getSelectedNode())+1);
            // developpe le chemin
            this.scrollPathToVisible(new TreePath(newNode.getPath()));
            edP.updateLevel(getLevelTree());
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
        CopexTreeNode newNode = new CopexTreeNode(taskBranch);
        if (taskBrother == null){
            CopexTreeNode np = getNode(taskParent, (CopexTreeNode)treeModelProc.getRoot());
            // insertion en tant que premier enfant du parent 
            treeModelProc.insertNodeInto(newNode, np, 0);
        }else{ // apres le frere
            CopexTreeNode nb = getNode(taskBrother, (CopexTreeNode)treeModelProc.getRoot());
            CopexTreeNode pn = (CopexTreeNode)nb.getParent();
            treeModelProc.insertNodeInto(newNode, pn, pn.getIndex(nb)+1);
            
        }
        treeModelProc.addNodes(newNode, listTask);
        // developpe le chemin
        this.scrollPathToVisible(new TreePath(newNode.getPath()));
        edP.updateLevel(getLevelTree());
        revalidate();
        repaint();
    }
    
   
        
    /* renvoit le noeud crorrespondant à la tache */
    public CopexTreeNode getNode(CopexTask task, CopexTreeNode node){
       if (node.getTask().getDbKey() == task.getDbKey())
           return node;
       else{
           // on cherche dans les enfants
           if (node.getChildCount() > 0){
               for (int k=0; k<node.getChildCount(); k++){
                   CopexTreeNode n = getNode(task, (CopexTreeNode)treeModelProc.getChild(node, k));
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
    
    /* mise à jour  d'une tache */
    public void updateTask(CopexTask newTask, TaskSelected ts){
        undoManager.addEdit(new UpdateTaskUndoRedo(edP, controller, this, ts.getSelectedNode().getTask(), ts.getSelectedNode(), newTask));
        updateTask(newTask, ts.getSelectedNode());
    }
    
    /* mise à jour  d'une tache */
    public void updateTask(CopexTask newTask, CopexTreeNode node){
        CopexTreeNode n = getNode(node.getTask());
        n.setUserObject(newTask);
        node.setTask(newTask);
        n.setTask(newTask);
        treeModelProc.reload(n);
        revalidate();
        repaint();
    }
    
    /* affichage d'un certain niveau 
     * niveau 1 : affichage de la racine et de ses enfants (non deployes)....
     */
    public void displayLevel(int level){
        setVisibleNode(level, (CopexTreeNode)treeModelProc.getRoot());
    }
    
    private void setVisibleNode(int level, CopexTreeNode node){
       int levelNode = node.getLevel();
       int nbC = treeModelProc.getChildCount(node);
       if (levelNode < level){
           if (nbC > 0){
                for (int i=0; i<nbC; i++){
                    setVisibleNode(level, (CopexTreeNode)node.getChildAt(i));
                }
            }
       }else { // on referme les noeuds qui etaient eventuellement deployes
           collapsePath(new TreePath(node.getPath()));
            for (int i=0; i<nbC; i++){
                collapsePath(new TreePath((CopexTreeNode)node.getChildAt(i)));
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
           //CopexTreeNode node = listTs.get(i).getSelectedNode();
           CopexTreeNode n = getNode(listTs.get(i).getSelectedTask());
           suppr(n);
           listVisibleNode.remove(n);
       }
      treeModelProc.reload();
      clearSelection();
      edP.updateMenu();
      revalidate();
      displayTree();
      this.register = true;
      ///level = Math.min(level, getLevelTree());
      //displayLevel(level);
      //repaint();
    }
    
    /* suppression d'un noeud*/
    private void suppr(CopexTreeNode node){
        try{ 
            treeModelProc.removeNodeFromParent(node);
        }catch(IllegalArgumentException e){
            System.out.println("Suppression d'un noeud qui n'est plus dans l'arbre "+node);
        }
        if (!node.isAction()){
            // etape ou sous question on supprime également tous les enfants
            int nbC = node.getChildCount();
            for (int c=nbC-1; c==0; c--){
                suppr((CopexTreeNode)node.getChildAt(c));
            }
        }
    }

    /* retourne le niveau d'arborescence affiché */
    private int getLevelTreeDisplay(){
        int level = 1;
        int nb = getRowCount();
        for (int i=0; i<nb; i++){
            TreePath path = getPathForRow(i);
            if (path != null){
               CopexTreeNode node = (CopexTreeNode)path.getLastPathComponent() ;
               int l = node.getLevel();
               if (l > level)
                   level = l;
            }
        }
        return level;
    }
    
    
    
    
    // EVENEMENTS CLAVIERS
    public void keyTyped(KeyEvent e) {
        
    }

    public void keyPressed(KeyEvent e) {
        
    }

    public void keyReleased(KeyEvent e) {
        // Evenements souris 
        if (e.getKeyCode() == KeyEvent.VK_C && canCopy()){
            CopexReturn cr = this.controller.copy();
            if (cr.isError())
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }else if (e.getKeyCode() == KeyEvent.VK_M && canAddE()){
            this.controller.addEtape();
        }else if (e.getKeyCode() == KeyEvent.VK_N && canAddA()){
            this.controller.addAction();
        }else if (e.getKeyCode() == KeyEvent.VK_X && canCut()){
            CopexReturn cr = this.controller.cut();
            if (cr.isError())
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }else if (e.getKeyCode() == KeyEvent.VK_V && canPaste()){
            CopexReturn cr = this.controller.paste();
            if (cr.isError())
                edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }else if (e.getKeyCode() == KeyEvent.VK_DELETE && canSuppr()){
            this.edP.suppr();
        }else if (e.getKeyCode() == KeyEvent.VK_E && isOnlyOneElementIsSel()){
            this.edit();
        }
    }

   /* renommer un protocole */
    public void updateProcName(String name){
        proc.setName(name);
    }
    
    /* retourne le sous arbre à copier */
    public SubTree getSubTreeCopy(boolean dragNdrop){
        // on selectionne egalement les taches enfants des etapes / ss question
        
        SubTree st = new SubTree(edP, controller, proc, this, getSelectedTasks(), getSelectedNodes(), dragNdrop);
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
            if (this.getNoRow((CopexTreeNode)tabPaths[cpt].getLastPathComponent()) > this.getNoRow((CopexTreeNode)val.getLastPathComponent())){
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
    private int getNoRow(CopexTreeNode node){
        CopexTreeNode parent = (CopexTreeNode)node.getParent();
        if (parent == null)
            return 0;
        else{
            int idC = parent.getIndex(node);
            if (idC == 0)
                return getNoRow(parent)+1;
            else{
                CopexTreeNode lastChild = getSubLastChild((CopexTreeNode)parent.getChildBefore(node));
                return getNoRow(lastChild)+1;
            }
        }            
    }
    
    
    /* retourne le dernier petit enfant- sinon lui meme */
    private CopexTreeNode getSubLastChild(CopexTreeNode node){
        int nbC = node.getChildCount();
        if (nbC == 0)
            return node;
        else{
            return getSubLastChild((CopexTreeNode)node.getChildAt(nbC-1));
        }
    }
    /* retourne vrai si les elements selectionnés forme un sous arbre */
    public  boolean selIsSubTree(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        int firstLevel = getLevelTree((CopexTreeNode) tabPaths[0].getLastPathComponent());
        for (int i=1; i<tabPaths.length; i++){
            if (getLevelTree((CopexTreeNode) tabPaths[i].getLastPathComponent()) > firstLevel)
                    return false;
        }
        return true;
    }
    
     /* retourne vrai si les elements selectionnés peuvent etre copies */
    public  boolean selCanBeCopy(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexTreeNode node = (CopexTreeNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canCopy())
                    return false;
            // on regarde egalement les enfants
            for (int k=0; k<node.getChildCount(); k++){
                CopexTreeNode n = (CopexTreeNode)node.getChildAt(k);
                if (!n.canCopy())
                    return false;
            }
        }
        return true;
    }
    
     /* retourne vrai si les elements selectionnés peuvent etre supprimes */
    public  boolean selCanBeDelete(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexTreeNode node = (CopexTreeNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canDelete())
                return false;
            // on regarde egalement les enfants
            for (int k=0; k<node.getChildCount(); k++){
                CopexTreeNode n = (CopexTreeNode)node.getChildAt(k);
                if (!n.canDelete())
                    return false;
            }
        }
        return true;
    }
    
    /* retourne vrai si les elements selectionnés peuvent etre deplaces */
    public  boolean selCanBeMove(){
        TreePath[] tabPaths = this.getSelectionPaths();
        if (tabPaths == null || tabPaths.length == 0 )
          return false;
        tabPaths = sortTabPath(tabPaths);
        for (int i=0; i<tabPaths.length; i++){
            CopexTreeNode node = (CopexTreeNode) tabPaths[i].getLastPathComponent() ;
            if (!node.canMove())
                return false;
            // on regarde egalement les enfants
            for (int k=0; k<node.getChildCount(); k++){
                CopexTreeNode n = (CopexTreeNode)node.getChildAt(k);
                if (!n.canMove())
                    return false;
            }
        }
        return true;
    }
    
    /* deplace un sous arbre */
    public boolean moveSubTree(SubTree subTree, CopexTreeNode node, boolean brother){
        TaskSelected ots ;
        TaskSelected t = getTaskSelected(subTree.getFirstTask());
        System.out.println("premiere tache ss arbre : "+t.getSelectedTask().getDescription());
        ots = getTaskSelected(t.getTaskToAttach());
        System.out.println("attachée à : "+t.getTaskToAttach().getDescription());
        
        TaskSelected ts = getSelectedTask(node, brother);
        System.out.println("tache insertion : "+node.getTask().getDescription());
       // verifie que la tache sel n'est pas dans le sous arbre
       boolean control = control(node, subTree);
       if (!control)
           return false;
       CopexReturn cr = this.controller.move(ts, subTree, MyConstants.NOT_UNDOREDO);
        if (cr.isError()){
            edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        // mise à jour graphique
        addTasks(subTree.getListTask(), subTree, ts);
        this.subTree = subTree;
        this.newTs = ts;
        this.oldTs = ots;
        return true;
    }
    
    /* controle que la tache sel n'est pas dans le sous arbre */
    private boolean control(CopexTreeNode node, SubTree subTree){
        return !subTree.containNode(node);
    }
    
    
    /* supprime les taches */
    public void removeTask(SubTree subTree, boolean addEdit){
        markDisplay();
        ArrayList<CopexTreeNode> listNode = this.subTree.getListNodes();
        int nbN = listNode.size();
        for (int i=0; i<nbN; i++){
            treeModelProc.removeNodeFromParent(listNode.get(i));
        }
        treeModelProc.reload();
        clearSelection();
        this.controller.finalizeDragAndDrop(this.proc);
        // met à jour les noeuds
        ArrayList<CopexTreeNode> updateListNode = new ArrayList();
        ArrayList<CopexTask> listTask = new ArrayList();
        for (int i=0; i<nbN; i++){
            CopexTask t = getTask(subTree.getListTask().get(i).getDbKey());
            updateListNode.add(getNode(t));
            listTask.add(t);
        }
       
        this.subTree = new SubTree(edP, controller, proc, this, listTask, updateListNode, true);
        //subTree.setListNodes(updateListNode);
        newTs = getTaskSelected(newTs.getSelectedTask());
        oldTs = getTaskSelected(oldTs.getSelectedTask());
        if (addEdit)
            addEdit_dragDrop(this.subTree, newTs, oldTs);
        refreshMouseOver((CopexTreeNode)treeModelProc.getRoot());
        revalidate();
        displayTree();
        edP.updateMenu();
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
    
    /* marque l'affichage des chemins */
    public void markDisplay(){
       // System.out.println("*****");
        listVisibleNode = getListVisibleNode();
        //System.out.println("*****");
    }
    
    /* retourne la liste des noeuds visibles */
    private ArrayList<CopexTreeNode> getListVisibleNode(){
       ArrayList<CopexTreeNode> listNodes = new ArrayList();
       int nb = getRowCount();
       for (int i=0; i<nb; i++){
           TreePath path = getPathForRow(i);
           if (path != null){
               CopexTreeNode n = (CopexTreeNode)path.getLastPathComponent() ;
               //System.out.println("noeud visible : "+n.getTask().getDescription());
               listNodes.add(n);
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
    
    
    /* undo */
    public void undo(){
        try{
            undoManager.undo();
            edP.updateMenu();
        }catch(CannotUndoException e){
            
        }
    }
    
    /* redo */
    public void redo(){
        try{
            undoManager.redo();
            edP.updateMenu();
        }catch(CannotUndoException e){
            
        }
    }
    
    /*ajout d'un evenement : renommer proc*/
    public void addEdit_renameProc(LearnerProcedure proc, String name){
        this.undoManager.addEdit(new UpdateProcNameUndoRedo(edP, controller, this, proc, proc.getName(), name));
    }
    
    public void addEdit_addTask(TaskSelected task, TaskSelected ts){
        this.undoManager.addEdit(new AddTaskUndoRedo(edP, controller, this, task, ts));
    }
    
    public void addEdit_deleteTask(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs){
        this.undoManager.addEdit(new DeleteTaskUndoRedo(edP, controller, this, listTask, listTs));
    }
    
    /* ajout d'un evenement : creation d'un datasheet */
     public void addEdit_createDataSheet(int nbRows, int nbCols, DataSheet dataSheet){
        this.undoManager.addEdit(new CreateDataSheetUndoRedo(edP, controller, this, nbRows, nbCols, dataSheet));
    }
     
     /* ajout d'un evenement : modification d'un datasheet */
     public void addEdit_updateDataSheet(DataSheet dataSheet, int oldNbRows, int oldNbCols, int newNbRows, int newNbCols){
        this.undoManager.addEdit(new UpdateDataSheetUndoRedo(edP, controller, this, dataSheet, oldNbRows, oldNbCols, newNbRows, newNbCols));
    }
     
     /* ajout d'un evenement : edition d'une donnee d'un datasheet */
     public void addEdit_editDataSheet(DataSheet dataSheet, String oldData, String newData, int noRow, int noCol){
        this.undoManager.addEdit(new EditDataSheetUndoRedo(edP, controller, this, dataSheet, oldData, newData, noRow, noCol));
    }
     
     /* couper */
      public void addEdit_cut(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs, SubTree subTree){
        this.undoManager.addEdit(new CutUndoRedo(edP, controller, this, listTask, listTs, subTree));
    }
      
      /* coller */
     public void addEdit_paste(SubTree subTree, TaskSelected ts, ArrayList<TaskSelected> listTask){
        this.undoManager.addEdit(new PasteUndoRedo(edP, controller, this, subTree, ts, listTask));
    }
   
      /* drag and drop */
     public void addEdit_dragDrop(SubTree subTree, TaskSelected ts, TaskSelected oldTs){
        this.undoManager.addEdit(new DragAndDropUndoRedo(edP, controller, this, subTree, ts, oldTs));
    }
     
      /* ajout d'un evenement : ajout mat utilise */
     public void addEdit_addMaterialUseForProc(LearnerProcedure proc, Material m, String justification){
         this.undoManager.addEdit(new AddMaterialUseForProcUndoRedo(edP, controller, this, proc, m, justification));
     }
      /* ajout d'un evenement : update mat utilise */
     public void addEdit_updateMaterialUseForProc(LearnerProcedure proc, Material m, String oldJustification,  String newJustification){
         this.undoManager.addEdit(new UpdateMaterialUseForProcUndoRedo(edP, controller, this, proc, m, oldJustification, newJustification));
     }
      /* ajout d'un evenement : remove mat utilise */
     public void addEdit_removeMaterialUseForProc(LearnerProcedure proc, Material m, String justification){
         this.undoManager.addEdit(new DeleteMaterialUseForProcUndoRedo(edP, controller, this, proc, m, justification));
     }
     
     /* retourne le tool tip text pour un noeud selon les droits dur la tache */
     public String getToolTipText(CopexTreeNode node){
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
     
     /* reaffiche arbre dans le dernier etat */
     private void initTree(){
         listVisibleNode = new ArrayList();
         setListNodeVisible((CopexTreeNode)treeModelProc.getRoot());
         displayTree();
     }
     
     /* met la liste des noeuds visibles */
     private void setListNodeVisible(CopexTreeNode node){
         if (node.getTask().isVisible())
             listVisibleNode.add(node);
         int nbC = node.getChildCount();
         for (int i=0; i<nbC; i++){
             setListNodeVisible((CopexTreeNode)node.getChildAt(i));
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
            CopexTreeNode node = (CopexTreeNode)path.getLastPathComponent();
            // recupere tous les enfants
            ArrayList<CopexTask> listChild = getChildTask(node, !state);
            int nb = listChild.size();
            for (int i=0; i<nb; i++){
                listChild.get(i).setVisible(state);
                CopexReturn cr = controller.updateTaskVisible(this.proc, listChild);
                if (cr.isError()){
                    edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
                }
            }
            
        }
    }

    /* retourne la place disponible pour l'arbre */
    public int getTextWidth(Object value, int row){
        int totalWidth = this.panelOwnerWidth-10 ;
        // selon le niveau dans l'arbre la place disponible n'est pas la meme
        if (value instanceof CopexTreeNode ){
            CopexTreeNode node =  (CopexTreeNode)value;
            if (node == null || node.getTask() == null)
                    return totalWidth;
            int level = node.getLevel() ;
            if (level == 0)
                return totalWidth - 35;
            return totalWidth - (level*45);
        }
        return totalWidth ;
    }

    /* resize arbre */
    public void resizeWidth(int width){
         markDisplay();
        this.panelOwnerWidth = width;
        treeModelProc.reload();
        displayTree();
    }

    /* retourne le dernier enfant (voire ss enfant) du noeud, lui meme sinon */
    private CopexTreeNode getLastChildren(CopexTreeNode node){
        CopexTreeNode childNode = getLastChild(node);
        CopexTreeNode lastChild = childNode == null ? node : childNode;
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
          CopexTreeNode selNode = getSelectedNode();
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
          System.out.println("***recuperation des taches precedentes : "+selNode.getTask().getDbKey()+" ("+selNode.getTask().getDescription()+")");
          ArrayList v= new ArrayList();
          //return getListTaskBeforeSelNode(modeAdd,(CopexTreeNode)treeModelProc.getRoot(), selNode, v);
          listTaskBefore = getListTaskBeforeSelNode(modeAdd,(CopexTreeNode)treeModelProc.getRoot(), selNode, v);
          for (int i=0; i<listTaskBefore.size(); i++){
              System.out.println("tache prec : "+listTaskBefore.get(i).getDbKey()+" ("+listTaskBefore.get(i).getDescription()+") ");
          }
          return listTaskBefore;
      }

      /* retourne la liste des taches avant selNode, en v[0] retourne vrai si on a trouve la tache */
      private ArrayList<CopexTask> getListTaskBeforeSelNode(boolean modeAdd, CopexTreeNode node, CopexTreeNode selNode, ArrayList v){
          ArrayList<CopexTask> listTask = new ArrayList();
          boolean findNode = false;
          if (node.getTask().getDbKey() == selNode.getTask().getDbKey()){
              findNode = true;
              if (modeAdd)
                  listTask.add(node.getTask());
          }else{
              listTask.add(node.getTask());
              int nbC = node.getChildCount() ;
              for (int i=0; i<nbC; i++){
                  ArrayList v2 = new ArrayList();
                  ArrayList<CopexTask> listChild = getListTaskBeforeSelNode(modeAdd, (CopexTreeNode)node.getChildAt(i), selNode, v2);
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

      /* retourne vrai si les taches sel ne sont pas lies par le materiel à d'autres taches */
      private boolean areActionSelLinkedByMaterial(){
          ArrayList<CopexTask> listTask = getSelectedTasks() ;
          int nbT = listTask.size();
          for (int i=0; i<nbT; i++){
              if (isActionLinkedByMaterial(listTask.get(i)))
                  return true;
          }
          return false;

      }

      /* retourne vrai si les taches sel ne sont pas lies par le data à d'autres taches */
      private boolean areActionSelLinkedByData(){
          ArrayList<CopexTask> listTask = getSelectedTasks() ;
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
              ArrayList<CopexTask> listTask = proc.getListTask();
              int nbT = listTask.size();
              ArrayList<Material> listMaterialProd = ((CopexActionManipulation)task).getListMaterialProd() ;
              int nbMat = listMaterialProd.size();
              for (int i=0; i<nbMat; i++){
                  long dbKeyMatProd = listMaterialProd.get(i).getDbKey();
                  for (int j=0; j<nbT; j++){
                      if (listTask.get(j) instanceof CopexActionAcquisition || listTask.get(j) instanceof CopexActionChoice || listTask.get(j) instanceof CopexActionManipulation || listTask.get(j) instanceof CopexActionTreatment){
                          ActionParam[] tabParam = ((CopexActionParam)listTask.get(j)).getTabParam();
                          for (int k=0; k<tabParam.length; k++){
                              if (tabParam[k] instanceof ActionParamMaterial){
                                  if (((ActionParamMaterial)tabParam[k]).getMaterial().getDbKey() == dbKeyMatProd)
                                      return true;
                              }
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
              ArrayList<CopexTask> listTask = proc.getListTask();
              int nbT = listTask.size();
              ArrayList<QData> listDataProd = new ArrayList();
              if (task instanceof CopexActionAcquisition)
                  listDataProd = ((CopexActionAcquisition)task).getListDataProd() ;
              else if (task instanceof CopexActionTreatment)
                  listDataProd = ((CopexActionTreatment)task).getListDataProd() ;
              int nbData = listDataProd.size();
              for (int i=0; i<nbData; i++){
                  long dbKeyDataProd = listDataProd.get(i).getDbKey();
                  for (int j=0; j<nbT; j++){
                      if (listTask.get(j) instanceof CopexActionAcquisition || listTask.get(j) instanceof CopexActionChoice || listTask.get(j) instanceof CopexActionManipulation || listTask.get(j) instanceof CopexActionTreatment){
                          ActionParam[] tabParam = ((CopexActionParam)listTask.get(j)).getTabParam();
                          for (int k=0; k<tabParam.length; k++){
                              if (tabParam[k] instanceof ActionParamData){
                                  if (((ActionParamData)tabParam[k]).getData().getDbKey() == dbKeyDataProd)
                                      return true;
                              }
                          }
                      }
                  }
              }
          }
          return false;
      }

      /* retourne vrai si le deplacement du sous arbre pose probleme du point de vue du material produit */
    public  boolean isProblemWithMaterialProd(CopexTreeNode insertNode, SubTree subTreeToMove){
        boolean isProblem = false;
        ArrayList<CopexTask> listTaskToMove = subTreeToMove.getListTask();
        ArrayList v = new ArrayList();
        ArrayList<CopexTask> listTaskBefore = getListTaskBeforeSelNode(true, (CopexTreeNode)treeModelProc.getRoot(), insertNode, v);
        int nbT = listTaskToMove.size();
        for (int i=0; i<nbT; i++){
            if (listTaskToMove.get(i) instanceof CopexActionAcquisition || listTaskToMove.get(i) instanceof CopexActionChoice || listTaskToMove.get(i) instanceof CopexActionManipulation ||listTaskToMove.get(i) instanceof CopexActionTreatment ){
                ActionParam[] tabParam = ((CopexActionParam)listTaskToMove.get(i)).getTabParam();
                for (int j=0; j<tabParam.length; j++){
                    if (tabParam[j] instanceof ActionParamMaterial){
                        long dbKeyMaterialUse = ((ActionParamMaterial)tabParam[j]).getMaterial().getDbKey();
                        if(!edP.isMaterialFromMission(dbKeyMaterialUse)){
                            if (!isMaterialCreateInList(dbKeyMaterialUse, listTaskBefore)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return isProblem ;
    }

       /* retourne vrai si le deplacement du sous arbre pose probleme du point de vue du data produit */
    public  boolean isProblemWithDataProd(CopexTreeNode insertNode, SubTree subTreeToMove){
        boolean isProblem = false;
        ArrayList<CopexTask> listTaskToMove = subTreeToMove.getListTask();
        ArrayList v = new ArrayList();
        ArrayList<CopexTask> listTaskBefore = getListTaskBeforeSelNode(true, (CopexTreeNode)treeModelProc.getRoot(), insertNode, v);
        int nbT = listTaskToMove.size();
        for (int i=0; i<nbT; i++){
            if (listTaskToMove.get(i) instanceof CopexActionAcquisition || listTaskToMove.get(i) instanceof CopexActionChoice || listTaskToMove.get(i) instanceof CopexActionManipulation ||listTaskToMove.get(i) instanceof CopexActionTreatment ){
                ActionParam[] tabParam = ((CopexActionParam)listTaskToMove.get(i)).getTabParam();
                for (int j=0; j<tabParam.length; j++){
                    if (tabParam[j] instanceof ActionParamData){
                        long dbKeyDataUse = ((ActionParamData)tabParam[j]).getData().getDbKey();
                        if (!isDataCreateInList(dbKeyDataUse, listTaskBefore)){
                                return true;
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
                ArrayList<Material> listMaterialProd = ((CopexActionManipulation)listTask.get(i)).getListMaterialProd();
                int nbMP = listMaterialProd.size();
                for (int j=0; j<nbMP; j++){
                    if (listMaterialProd.get(j).getDbKey() == dbKeyMaterial)
                        return true;
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
                ArrayList<QData> listDataProd = new ArrayList();
                if (listTask.get(i) instanceof CopexActionAcquisition)
                    listDataProd = ((CopexActionAcquisition)listTask.get(i)).getListDataProd();
                else if (listTask.get(i) instanceof CopexActionTreatment)
                    listDataProd = ((CopexActionTreatment)listTask.get(i)).getListDataProd();
                int nbDP = listDataProd.size();
                for (int j=0; j<nbDP; j++){
                    if (listDataProd.get(j).getDbKey() == dbKeyData)
                        return true;
                }
            }
        }
        return false;
    }

    /* remet mosueOver false dans arbre*/
    private void refreshMouseOver(CopexTreeNode node){
        node.setMouseover(false);
        int nbC = node.getChildCount() ;
        for (int i=0; i<nbC; i++){
            refreshMouseOver((CopexTreeNode)node.getChildAt(i));
        }
    }

    public void refreshMouseOver(){
        refreshMouseOver((CopexTreeNode)treeModelProc.getRoot());
        repaint();
    }

    /* ouverture auto de la fenetre d'edition de la question */
    public void openQuestionDialog(){
        setSelectionPath(new TreePath(treeModelProc.getRoot()));
        edit();
    }
    
}


