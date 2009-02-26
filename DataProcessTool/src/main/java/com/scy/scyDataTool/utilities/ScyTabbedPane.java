/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.utilities;

import com.scy.scyDataTool.dataTool.MainDataToolPanel;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 * tabbedPane avec la croix
 * @author Marjolaine Bodin
 */
public class ScyTabbedPane extends JTabbedPane {
    //PROPERTY 
    /* owner */
    protected MainDataToolPanel owner;
    /* liste des CloseTAb */
    protected ArrayList<CloseTab> listCloseTab;
    /* close TAb du +*/
    protected CloseTab closeTabAdd;

    public ScyTabbedPane(MainDataToolPanel owner) {
        super();
        this.owner = owner;
    }

    public ScyTabbedPane() {
        super();
    }
    
    /* selectionne l'onglet */
    public void setSelected(CloseTab tab){
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
    
    protected void init(){
       UIManager.put("TabbedPane.contentAreaColor",Color.WHITE);
       UIManager.put("TabbedPane.selectedColor",Color.WHITE);
       UIManager.put("TabbedPane.selected",Color.WHITE);
       UIManager.put("TabbedPane.focus",Color.WHITE);
       UIManager.put("TabbedPane.borderHightlightColor",MainDataToolPanel.backgroundColor);
       UIManager.put("TabbedPane.tabAreaBackground",MainDataToolPanel.backgroundColor);
       UIManager.put("TabbedPane.light",MainDataToolPanel.backgroundColor);
       UIManager.put("TabbedPane.unselectedTabBackground",MainDataToolPanel.backgroundColor);
       UIManager.put("TabbedPane.unselectedTabHighlight",MainDataToolPanel.backgroundColor);
       updateUI();
       this.listCloseTab = new ArrayList();
       // initialisation du tabbedPane : onglet vierge afin d'ajouter un proc
       addTab(null, new JLabel(""));
       ImageIcon  iconClose = owner.getCopexImage("Bouton-onglet_ouverture.png");
       ImageIcon  iconRollOver = owner.getCopexImage("Bouton-onglet_ouverture_sur.png");
       ImageIcon  iconClic = owner.getCopexImage("Bouton-onglet_ouverture_cli.png");
       ImageIcon  iconDisabled = owner.getCopexImage("Bouton-onglet_ouverture_grise.png");
        closeTabAdd = new CloseTab(owner, this, "", iconClose, iconRollOver, iconClic, iconDisabled);
        setTabComponentAt(0, closeTabAdd);
        
   }
    
    /* retourne vrai si le bouton + est clique */
    public boolean isButtonAdd(CopexButtonPanel b){
        return b == closeTabAdd.getButtonClose();
    }
}
