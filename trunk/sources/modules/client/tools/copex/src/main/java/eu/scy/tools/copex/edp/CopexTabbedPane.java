/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.dnd.SubTree;
import eu.scy.tools.copex.utilities.CloseTab;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * onglets représentants les différents protocoles
 * @author MBO
 */
public class CopexTabbedPane extends JTabbedPane{
    //PROPERTY
    protected EdPPanel edP;
    /* liste des CloseTAb */
    protected ArrayList<CloseTab> listCloseTab;
    /* close TAb du +*/
    protected CloseTab closeTabAdd;
    private ArrayList<CopexTree> listCopexTree;
    private CopexTree treeActiv;
    
    /* enregistrement */
    private boolean register;
    
    // CONSRTUCTEURS
  
    public CopexTabbedPane(EdPPanel edP) {
        super();
        this.edP = edP;
        this.register = false;
        setBackground (EdPPanel.backgroundColor);
        this.listCopexTree = new ArrayList();
        init();
        
        if (!edP.canAddProc()){
            closeTabAdd.setDisabled();
        }
    }


    protected void init(){
       UIManager.put("TabbedPane.contentAreaColor",Color.WHITE);
       UIManager.put("TabbedPane.selectedColor",Color.WHITE);
       UIManager.put("TabbedPane.selected",Color.WHITE);
       UIManager.put("TabbedPane.focus",Color.WHITE);
       UIManager.put("TabbedPane.borderHightlightColor",EdPPanel.backgroundColor);
       UIManager.put("TabbedPane.tabAreaBackground",EdPPanel.backgroundColor);
       UIManager.put("TabbedPane.light",EdPPanel.backgroundColor);
       UIManager.put("TabbedPane.unselectedTabBackground",EdPPanel.backgroundColor);
       UIManager.put("TabbedPane.unselectedTabHighlight",EdPPanel.backgroundColor);
       updateUI();
       this.listCloseTab = new ArrayList();
       // initialisation du tabbedPane : onglet vierge afin d'ajouter un proc
       addTab(null, new JLabel(""));
       ImageIcon  iconClose = edP.getCopexImage("Bouton-onglet_ouverture.png");
       ImageIcon  iconRollOver = edP.getCopexImage("Bouton-onglet_ouverture_sur.png");
       ImageIcon  iconClic = edP.getCopexImage("Bouton-onglet_ouverture_cli.png");
       ImageIcon  iconDisabled = edP.getCopexImage("Bouton-onglet_ouverture_grise.png");
        closeTabAdd = new CloseTab(edP, this,null, "", iconClose, iconRollOver, iconClic, iconDisabled, edP.getToolTipTextOpen());
        setTabComponentAt(0, closeTabAdd);
   }

    public void setAddProcDisabled(){
        closeTabAdd.setDisabled();
    }
    // methodes surgargees
    @Override
    public void addTab(String title, Component component) {
        int index = 0;
        if (getTabCount() > 1)
            index = getTabCount()-1 ;
        super.insertTab("", null, component, "",  index);
        ImageIcon  iconClose = edP.getCopexImage("Bouton-onglet_fermeture.png");
        ImageIcon  iconRollOver = edP.getCopexImage("Bouton-onglet_fermeture_sur.png");
        ImageIcon  iconClic = edP.getCopexImage("Bouton-onglet_fermeture_cli.png");
        
        LearnerProcedure p = null;
        if (component instanceof CopexTree){
            p =((CopexTree)component).getProc();
        }
        CloseTab closeTab = new CloseTab(edP, this, p, title, iconClose, iconRollOver, iconClic, iconClose, edP.getBundleString("TOOLTIPTEXT_CLOSE_PROC"));
        if (component instanceof CopexTree){
            listCopexTree.add((CopexTree)component);
            treeActiv = (CopexTree)component;
            listCloseTab.add(closeTab);
        }
        setTabComponentAt(index, closeTab);
        setSelectedIndex(index);
    }

  

    /*
     * l'ajout d'une sous question est il possible ?
     * si 1 et 1 seul élément est sélectionné et s'il set possible d'insérer une sous question (fils d'une sous question)
     */
    public boolean canAddQ(){
        if(treeActiv != null)
            return treeActiv.canAddQ();
        return false;
    }
   
    /*
     * l'ajout d'une etape est il possible ?
     * si 1 et 1 seul élément est sélectionné 
     */
    public boolean canAddE(){
        if(treeActiv != null)
            return treeActiv.canAddE();
        return false;
    }

    /*
     * l'ajout d'une action est il possible ?
     * si 1 et 1 seul élément est sélectionné 
     */
    public boolean canAddA(){
        if(treeActiv != null)
            return treeActiv.canAddA();
        return false;
    }
    
    /*
     * undo possible
     *  
     */
    public boolean canUndo(){
        if(treeActiv != null)
            return treeActiv.canUndo();
        return false;
    }
    
    /*
     * redo possible
     *  
     */
    public boolean canRedo(){
        if(treeActiv != null)
            return treeActiv.canRedo();
        return false;
    }
    /*
     * copie possible
     *  
     */
    public boolean canCopy(){
        if(treeActiv != null)
            return treeActiv.canCopy();
        return false;
    }
    /*
     * coller possible
     *  
     */
    public boolean canPaste(){
        if(treeActiv != null)
            return treeActiv.canPaste();
        return false;
    }
    /*
     * suppr possible
     *  
     */
    public boolean canSuppr(){
        if(treeActiv != null)
            return treeActiv.canSuppr();
        return false;
    }
    /*
     * cut possible
     *  
     */
    public boolean canCut(){
        if(treeActiv != null)
            return treeActiv.canCut();
        return false;
    }

    /* retourne vrai s'il au moins une tache a un commentaires */
    public boolean isComments(){
        if (treeActiv != null)
            return treeActiv.isComments();
        return false;
    }
    
   
    
    /* affiche ou non les commentaires  */
    public void setComments(char displayC){
       /*for (Iterator iter=listCopexTree.iterator();iter.hasNext();){
            CopexTree tree = (CopexTree)iter.next();
            tree.setComments(displayC);
        }*/
        if (treeActiv == null)
            return;
        this.treeActiv.setComments(displayC);
        repaint();
    }
    
    /* retourne l' element selectionne */
    public TaskSelected getTaskSelected(){
        if (treeActiv == null)
            return null;
        return treeActiv.getTaskSelected();
    }
    
    /* retourne les elements selectionnes */
    public ArrayList<TaskSelected> getTasksSelected(){
        if (treeActiv == null)
            return null;
        return treeActiv.getTasksSelected();
    }
    
    /* mise à jour de l'arbre */
    public void updateProc(LearnerProcedure newProc){
        treeActiv.updateProc(newProc);
    }
    
    /* ajout d'une tache */
    public void addTask(CopexTask newTask, TaskSelected ts){
        treeActiv.addTask(newTask, ts);
    }
    
    /* ajout d'une liste de taches */
    public void addTasks(ArrayList<CopexTask> listTask, SubTree subTree, TaskSelected ts){
        //treeActiv.addTasks(listTask, edP.getSubTreeCopy(), treeActiv.getTaskSelected());
        treeActiv.addTasks(listTask, subTree, ts);
    }
    /* modification d'une tache */
    public void updateTask(CopexTask newTask, TaskSelected ts){
        treeActiv.updateTask(newTask, ts);
    }
    
    /* retourne le niveau d'arboresence de l'arbre actif */
    public int getLevel(){
        if (treeActiv == null)
            return 0;
        else
            return treeActiv.getLevelTree();
    }
    
    /* affichage d'un niveau donné */
    public void displayLevel(int level){
        if (treeActiv != null)
            treeActiv.displayLevel(level);
    }
    
    /* suprression des elements selectionnées */
    public void suppr(ArrayList<TaskSelected> listTs){
        if (treeActiv != null)
            treeActiv.suppr(listTs);
    }
    
    /*retourne l'indice d'un arbre, -1 si non trouve */
    private int getIdTree(ExperimentalProcedure proc){
        int id = -1;
        int nbT = listCopexTree.size();
        for (int i=0; i<nbT; i++){
            if (listCopexTree.get(i).getProc().getDbKey() == proc.getDbKey())
                return i;
        }
        return id;
    }
    
    
    /* suppression d'un protocole */
    public void removeProc(ExperimentalProcedure proc){
        int selProc = getSelectedIndex();
        int id = getIdTree(proc);
        if (id == -1)
            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_CLOSE_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
        remove(listCopexTree.get(id));
        listCopexTree.remove(id);
        listCloseTab.remove(id);
        setSelectedIndex(0);
        revalidate();
        repaint();
    }
    
    /* retourne le protocole actif */
    public LearnerProcedure getProcActiv(){
        LearnerProcedure proc = null;
        if (treeActiv != null)
            proc = treeActiv.getProc();
        return proc;
    }

    @Override
    public void setSelectedIndex(int index) {
        if (this.listCopexTree.size() > 0 && index == this.listCopexTree.size()){
            return;
        }
        if (listCopexTree.size() == 0){
            if (this.closeTabAdd != null)
                this.closeTabAdd.setSelected(true);
            return;
        }
            
        super.setSelectedIndex(index);
        this.closeTabAdd.setSelected(false);
        if (index == this.listCopexTree.size())
            this.treeActiv = null;
        else
            //this.treeActiv = this.listCopexTree.get(this.listCopexTree.size() - 1-index);
            this.treeActiv = this.listCopexTree.get(index);
        for (int k=0; k<listCloseTab.size(); k++){
            //if (k == this.listCopexTree.size() - 1-index){
            if (k == index){
                this.listCloseTab.get(k).setSelected(true);
            }else
                this.listCloseTab.get(k).setSelected(false);
        }
        edP.setActiv(this.treeActiv.getProc(), register);
        edP.updateMenu();
    }
    
    /* renommer un protocole */
    public void updateProcName(ExperimentalProcedure proc, String name){
        int id = getIdTree(proc);
        if (id == -1)
            edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_RENAME_PROC"), false), edP.getBundleString("TITLE_DIALOG_ERROR"));
        listCloseTab.get(id).updateProcName(name);
        if (treeActiv != null)
            treeActiv.updateProcName(name);
    }
    
    public void setSelected(CloseTab tab) {
        int index = -1;
         for (int i=0;i<listCloseTab.size(); i++){
           CloseTab t = listCloseTab.get(i);
           if (tab.equals(t)){
               index = i;
               break;
           }
       }
        if (index != -1){
            setSelectedIndex( index);
            listCloseTab.get(index).setSelected(true);
        }
    }
    
    /* copie des elements selectionnes */
    public SubTree copy(){
        return this.treeActiv.getSubTreeCopy(false);
    }
    
    /* undo */
    public void undo(){
        if (this.treeActiv != null)
            this.treeActiv.undo();
    }
    
    /* redo */
    public void redo(){
        if (this.treeActiv != null)
            this.treeActiv.redo();
    }
    
    /* retourne la ts d'une tache */
    public TaskSelected getTaskSelected(CopexTask task){
        if (this.treeActiv != null)
            return this.treeActiv.getTaskSelected(task);
        return null;
    }
    
    /*ajout d'un edit pour le undo redo */
    public void addEdit_renameProc(LearnerProcedure proc, String name){
        if (this.treeActiv != null)
            treeActiv.addEdit_renameProc(proc, name);
    }
    
    public void addEdit_addTask(TaskSelected task, TaskSelected ts){
        if (this.treeActiv != null)
            treeActiv.addEdit_addTask(task, ts);
    }
    
    public void addEdit_deleteTask(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs){
        if (this.treeActiv != null)
            treeActiv.addEdit_deleteTask(listTask, listTs);
    }
    
    /* ajout d'un evenement : creation d'un datasheet */
     public void addEdit_createDataSheet(int nbRows, int nbCols, DataSheet dataSheet){
         if (this.treeActiv != null)
            treeActiv.addEdit_createDataSheet(nbRows, nbCols, dataSheet);
     }
     
      /* ajout d'un evenement : modification d'un datasheet */
     public void addEdit_updateDataSheet(DataSheet dataSheet, int oldNbRows, int oldNbCols, int newNbRows, int newNbCols){
          if (this.treeActiv != null)
              treeActiv.addEdit_updateDataSheet(dataSheet, oldNbRows, oldNbCols, newNbRows, newNbCols);
     }
     
     /* ajout d'un evenement : edition d'une donnee d'un datasheet */
     public void addEdit_editDataSheet(DataSheet dataSheet, String oldData, String newData, int noRow, int noCol){
          if (this.treeActiv != null)
              treeActiv.addEdit_editDataSheet(dataSheet, oldData, newData, noRow, noCol);
     }
     
     /* ajout d'un evenement : cut */
     public void addEdit_cut(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs, SubTree subTree){
          if (this.treeActiv != null)
              treeActiv.addEdit_cut(listTask, listTs, subTree);
     }
     
     /* coller */
      public void addEdit_paste(SubTree subTree, TaskSelected ts, ArrayList<TaskSelected> listTask){
          if (this.treeActiv != null)
              treeActiv.addEdit_paste(subTree, ts, listTask);
      }
      
      /* ajout d'un evenement : ajout mat utilise */
     public void addEdit_addMaterialUseForProc(LearnerProcedure proc, Material m, String justification){
         if (this.treeActiv != null)
            treeActiv.addEdit_addMaterialUseForProc(proc, m, justification);
     }
      /* ajout d'un evenement : update mat utilise */
     public void addEdit_updateMaterialUseForProc(LearnerProcedure proc, Material m, String oldJustification,  String newJustification){
         if (this.treeActiv != null)
            treeActiv.addEdit_updateMaterialUseForProc(proc, m, oldJustification, newJustification);
     }
      /* ajout d'un evenement : remove mat utilise */
     public void addEdit_removeMaterialUseForProc(LearnerProcedure proc, Material m, String justification){
         if (this.treeActiv != null)
            treeActiv.addEdit_removeMaterialUseForProc(proc, m, justification );
     }
      /* active l'enregistrement des proc actifs */
      public void beginRegister(){
          this.register = true;
          int nbT = listCopexTree.size();
          for (int i=0; i<nbT; i++){
              listCopexTree.get(i).beginregister();
          }
      }
      
      /* rend actif un proc*/
      public void setSelected(ExperimentalProcedure proc){
          int id = getIdTree(proc);
          if (id != -1)
              setSelectedIndex(id);
      }

      /* resize arbre */
      public void resizeWidth(int width){
          if (treeActiv != null)
              treeActiv.resizeWidth(width);
          setSize(width, getHeight());
      }

      /* retourne la liste des taches qui sont avant celle sel (y compris) */
      public ArrayList<CopexTask> getListTaskBeforeSel(boolean modeAdd){
          if (treeActiv != null)
              return treeActiv.getListTaskBeforeSel(modeAdd);
          return new ArrayList();
      }

      /* ouverture auto de la fentre d'edition de la question */
      public void openQuestionDialog(){
          if (treeActiv != null)
              treeActiv.openQuestionDialog();
      }
    
}