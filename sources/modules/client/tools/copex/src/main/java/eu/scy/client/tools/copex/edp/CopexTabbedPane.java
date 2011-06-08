/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.utilities.ActionCloseTab;
import eu.scy.client.tools.copex.utilities.CloseTab;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * tabs which represents the differents procedures
 * @author Marjolaine
 */
public class CopexTabbedPane extends JTabbedPane implements ActionCloseTab{
    protected CopexPanel copex;
    /* list of the CloseTab */
    protected ArrayList<CloseTab> listCloseTab;
    /* CloseTab + (open/new)*/
    protected CloseTab closeTabAdd;
    private ArrayList<EdPPanel> listCopexPanel;
    private EdPPanel copexActivPanel;
    
    /* save */
    private boolean register;
    
  
    public CopexTabbedPane(CopexPanel copex) {
        super();
        this.copex = copex;
        this.register = false;
        setBackground (CopexPanel.backgroundColor);
        this.listCopexPanel = new ArrayList();
        init();
        
        if (!copex.canAddProc()){
            closeTabAdd.setDisabled();
        }
    }


    protected void init(){
//       UIManager.put("TabbedPane.contentAreaColor",Color.WHITE);
//       UIManager.put("TabbedPane.selectedColor",Color.WHITE);
//       UIManager.put("TabbedPane.selected",Color.WHITE);
//       UIManager.put("TabbedPane.focus",Color.WHITE);
//       UIManager.put("TabbedPane.borderHightlightColor",CopexPanel.backgroundColor);
//       UIManager.put("TabbedPane.tabAreaBackground",CopexPanel.backgroundColor);
//       UIManager.put("TabbedPane.light",CopexPanel.backgroundColor);
//       UIManager.put("TabbedPane.unselectedTabBackground",CopexPanel.backgroundColor);
//       UIManager.put("TabbedPane.unselectedTabHighlight",CopexPanel.backgroundColor);
//       updateUI();
       this.listCloseTab = new ArrayList();
       // initialisation du tabbedPane : onglet vierge afin d'ajouter un proc
       addTab(null, new JLabel(""));
       ImageIcon  iconClose = copex.getCopexImage("Bouton-onglet_ouverture.png");
       ImageIcon  iconRollOver = copex.getCopexImage("Bouton-onglet_ouverture_sur.png");
       ImageIcon  iconClic = copex.getCopexImage("Bouton-onglet_ouverture_cli.png");
       ImageIcon  iconDisabled = copex.getCopexImage("Bouton-onglet_ouverture_grise.png");
        closeTabAdd = new CloseTab(null, getBgColor(), getBgSelColor(),"", iconClose, iconRollOver, iconClic, iconDisabled, copex.getToolTipTextOpen(), true);
        closeTabAdd.addActionCloseTab(this);
        setTabComponentAt(0, closeTabAdd);
   }

    public void setAddProcDisabled(){
        closeTabAdd.setDisabled();
    }

    @Override
    public void addTab(String title, Component component) {
        int index = 0;
        if (getTabCount() > 1)
            index = getTabCount()-1 ;
        super.insertTab("", null, component, "",  index);
        ImageIcon  iconClose = copex.getCopexImage("Bouton-onglet_fermeture.png");
        ImageIcon  iconRollOver = copex.getCopexImage("Bouton-onglet_fermeture_sur.png");
        ImageIcon  iconClic = copex.getCopexImage("Bouton-onglet_fermeture_cli.png");
        
        ExperimentalProcedure p = null;
        if (component instanceof EdPPanel){
            p =((EdPPanel)component).getExperimentalProc();
        }
        boolean canClose = copex.canCloseProc(p);
        CloseTab closeTab = new CloseTab(p, getBgColor(), getBgSelColor(),title, iconClose, iconRollOver, iconClic, iconClose, copex.getBundleString("TOOLTIPTEXT_CLOSE_PROC"), canClose);
        closeTab.addActionCloseTab(this);
        if (component instanceof EdPPanel){
            listCopexPanel.add((EdPPanel)component);
            copexActivPanel = (EdPPanel)component;
            listCloseTab.add(closeTab);
        }
        setTabComponentAt(index, closeTab);
        setSelectedIndex(index);
    }

  
    
    /* update the procedure */
    public void updateProc(LearnerProcedure newProc){
        copexActivPanel.updateProc(newProc);
    }
    
    
    /*gets the id of a proc, -1 otherwise */
    private int getIdPanel(ExperimentalProcedure proc){
        int id = -1;
        int nbT = listCopexPanel.size();
        for (int i=0; i<nbT; i++){
            if (listCopexPanel.get(i).getExperimentalProc().getDbKey() == proc.getDbKey())
                return i;
        }
        return id;
    }
    
    
    /* remove a procedure*/
    public void removeProc(ExperimentalProcedure proc){
        int id = getIdPanel(proc);
        if (id == -1)
            copex.displayError(new CopexReturn(copex.getBundleString("MSG_ERROR_CLOSE_PROC"), false), copex.getBundleString("TITLE_DIALOG_ERROR"));
        remove(listCopexPanel.get(id));
        listCopexPanel.remove(id);
        listCloseTab.remove(id);
        setSelectedIndex(0);
        revalidate();
        repaint();
    }
    

    /* returns the activ procedure */
    public ExperimentalProcedure getProcActiv(){
        ExperimentalProcedure proc = null;
        if (copexActivPanel != null)
            proc = copexActivPanel.getExperimentalProc();
        return proc;
    }

    @Override
    public void setSelectedIndex(int index) {
        if (this.listCopexPanel.size() > 0 && index == this.listCopexPanel.size()){
            return;
        }
        if (listCopexPanel.isEmpty()){
            if (this.closeTabAdd != null)
                this.closeTabAdd.setSelected(true);
            return;
        }
            
        super.setSelectedIndex(index);
        this.closeTabAdd.setSelected(false);
        if (index == this.listCopexPanel.size())
            this.copexActivPanel = null;
        else
            this.copexActivPanel = this.listCopexPanel.get(index);
        for (int k=0; k<listCloseTab.size(); k++){
            //if (k == this.listCopexTree.size() - 1-index){
            if (k == index){
                this.listCloseTab.get(k).setSelected(true);
            }else
                this.listCloseTab.get(k).setSelected(false);
        }
        copexActivPanel.setActiv(register);
        copexActivPanel.updateMenu();
    }
    
    /* rename a procedure*/
    public void updateProcName(ExperimentalProcedure proc, String name){
        int id = getIdPanel(proc);
        if (id == -1)
            copex.displayError(new CopexReturn(copex.getBundleString("MSG_ERROR_RENAME_PROC"), false), copex.getBundleString("TITLE_DIALOG_ERROR"));
        listCloseTab.get(id).updateProcName(name);
        if (copexActivPanel != null)
            copexActivPanel.updateProcName(name);
    }
    
    
     
     
      /* begin the register of the proc. */
      public void beginRegister(){
          this.register = true;
          int nbT = listCopexPanel.size();
          for (int i=0; i<nbT; i++){
              listCopexPanel.get(i).beginregister();
          }
      }
      
      /*  set a proc active*/
      public void setSelected(ExperimentalProcedure proc){
          int id = getIdPanel(proc);
          if (id != -1)
              setSelectedIndex(id);
      }

      /* resize the width */
      public void resizeWidth(int width){
          if (copexActivPanel != null)
              copexActivPanel.resizeWidth(width);
          setSize(width, getHeight());
      }

    @Override
    public void setSelectedTab(CloseTab closeTab) {
        int index = -1;
         for (int i=0;i<listCloseTab.size(); i++){
           CloseTab t = listCloseTab.get(i);
           if (closeTab.equals(t)){
               index = i;
               break;
           }
       }
        if (index != -1){
            setSelectedIndex( index);
            listCloseTab.get(index).setSelected(true);
        }
    }

    @Override
    public void doubleClickTab(CloseTab closeTab) {
        if(copex.canUpdateTitle()){
            int id = getIdPanel(closeTab.getProc());
            if(id != -1){
                listCopexPanel.get(id).openDialogEditProc();
            }
        }
    }

    @Override
    public void openDialogAddProc() {
        copex.openDialogaddProc();
    }

    @Override
    public void openDialogCloseProc(ExperimentalProcedure proc) {
        copex.openDialogCloseProc(proc);
    }

    public void updateProcName(LearnerProcedure proc, String name){
        int id = getIdPanel(proc);
        if(id != -1){
            listCloseTab.get(id).updateProcName(name);
        }
    }

    private Color getBgColor(){
        return UIManager.getColor("TabbedPane.background");
    }
    private Color getBgSelColor(){
        return UIManager.getColor("TabbedPane.highlight");
    }
      

    
    
}