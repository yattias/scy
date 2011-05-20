/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.utilities.ActionCloseTab;
import eu.scy.client.tools.dataProcessTool.utilities.CloseTab;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 *
 * @author Marjolaine
 */
public class FitexTabbedPane extends JTabbedPane implements ActionCloseTab{
    private DataProcessToolPanel owner;
    private ArrayList<CloseTab> listCloseTab;
    private CloseTab closeTabAdd;
    private ArrayList<FitexToolPanel> listPanelFitex;
    private FitexToolPanel fitexActiv;

    public FitexTabbedPane(DataProcessToolPanel owner) {
        super();
        this.owner = owner;
        this.listPanelFitex = new ArrayList();
        initGUI();
    }

    private void initGUI(){
       setName("fitexTabbedPane");
//       UIManager.put("TabbedPane.contentAreaColors", Color.WHITE);
//       UIManager.put("TabbedPane.selectedColor",Color.WHITE);
//       UIManager.put("TabbedPane.selected",Color.WHITE);
//       UIManager.put("TabbedPane.focus",Color.WHITE);
//      UIManager.put("TabbedPane.borderHightlightColor",Color.WHITE);
//       UIManager.put("TabbedPane.tabAreaBackground",DataProcessToolPanel.backgroundColor);
//       UIManager.put("TabbedPane.light",DataProcessToolPanel.backgroundColor);
//       UIManager.put("TabbedPane.unselectedTabBackground",DataProcessToolPanel.backgroundColor);
//       UIManager.put("TabbedPane.unselectedTabHighlight",DataProcessToolPanel.backgroundColor);
//       updateUI();
       this.listCloseTab = new ArrayList();
       // initialisation du tabbedPane : onglet vierge afin d'ajouter un dataset
       addTab(null, new JLabel(""));
       ImageIcon  iconClose = owner.getCopexImage("Bouton-onglet_ouverture.png");
       ImageIcon  iconRollOver = owner.getCopexImage("Bouton-onglet_ouverture_sur.png");
       ImageIcon  iconClic = owner.getCopexImage("Bouton-onglet_ouverture_cli.png");
       ImageIcon  iconDisabled = owner.getCopexImage("Bouton-onglet_ouverture_grise.png");
       closeTabAdd = new CloseTab(null,  getBgColor(), getBgSelColor(),"", iconClose, iconRollOver, iconClic, iconDisabled, owner.getToolTipTextOpen(), true);
       closeTabAdd.addActionCloseTab(this);
       setTabComponentAt(0, closeTabAdd);
     }

    @Override
    public void setSelectedTab(CloseTab tab) {
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

    /* return the selected dataset*/
    public Dataset getDatasetActiv(){
        return fitexActiv == null ? null: fitexActiv.getDataset();
    }

    @Override
    public void doubleClickTab(CloseTab closeTab) {
        if(fitexActiv != null && fitexActiv.canRenameDataset()){
            String name = fitexActiv.renameDataset();
            closeTab.updateTitle(name);
        }
    }

   
    @Override
    public void addTab(String title, Component component) {
        int index = 0;
        if (getTabCount() > 1)
            index = getTabCount()-1 ;
        super.insertTab("", null, component, "",  index);
        ImageIcon  iconClose = owner.getCopexImage("Bouton-onglet_fermeture.png");
        ImageIcon  iconRollOver = owner.getCopexImage("Bouton-onglet_fermeture_sur.png");
        ImageIcon  iconClic = owner.getCopexImage("Bouton-onglet_fermeture_cli.png");

        Dataset ds = null;
        if (component instanceof FitexToolPanel){
            ds =((FitexToolPanel)component).getDataset();
        }
        boolean canClose = owner.canCloseDataset(ds);
        CloseTab closeTab = new CloseTab(ds, getBgColor(), getBgSelColor(),title, iconClose, iconRollOver, iconClic, iconClose, owner.getBundleString("TOOLTIPTEXT_CLOSE_DATASET"), canClose);
        closeTab.addActionCloseTab(this);
        if (component instanceof FitexToolPanel){
            listPanelFitex.add((FitexToolPanel)component);
            fitexActiv = (FitexToolPanel)component;
            listCloseTab.add(closeTab);
        }
        setTabComponentAt(index, closeTab);
        setSelectedIndex(index);
    }

    @Override
    public void setSelectedIndex(int index) {
        if (this.listPanelFitex.size() > 0 && index == this.listPanelFitex.size()){
            return;
        }
        if (listPanelFitex.size() == 0){
            if (this.closeTabAdd != null)
                this.closeTabAdd.setSelected(true);
            return;
        }

        super.setSelectedIndex(index);
        this.closeTabAdd.setSelected(false);
        if (index == this.listPanelFitex.size())
            this.fitexActiv = null;
        else
            this.fitexActiv = this.listPanelFitex.get(index);
        for (int k=0; k<listCloseTab.size(); k++){
            if (k == index){
                this.listCloseTab.get(k).setSelected(true);
            }else
                this.listCloseTab.get(k).setSelected(false);
        }
        owner.setActivFitex(fitexActiv);
    }

    @Override
    public void openDialogAddDataset() {
        owner.openDialogAddDataset();
    }

    @Override
    public void openDialogCloseDataset(Dataset ds) {
        owner.openDialogCloseDataset(ds);
    }

    /* remove a dataset */
    public void removeDataset(Dataset ds){
        int id = getIdDataset(ds);
        if (id == -1)
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_CLOSE_DATASET"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
        remove(listPanelFitex.get(id));
        listPanelFitex.remove(id);
        listCloseTab.remove(id);
        setSelectedIndex(0);
        revalidate();
        repaint();
    }

    private int getIdDataset(Dataset ds){
        int nb = listPanelFitex.size();
        for (int i=0; i<nb; i++){
            if(listPanelFitex.get(i).getDataset().getDbKey() == ds.getDbKey())
                return i;
        }
        return -1;
    }

    private Color getBgColor(){
        return UIManager.getColor("TabbedPane.background");
    }
    private Color getBgSelColor(){
        return UIManager.getColor("TabbedPane.highlight");
    }

    public DataTableModel getDataTableModel(Dataset ds){
        int id = getIdDataset(ds);
        if(id==-1)
            return null;
        return listPanelFitex.get(id).getDataTableModel();
    }
}
