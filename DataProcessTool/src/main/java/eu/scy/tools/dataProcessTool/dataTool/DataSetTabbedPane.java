/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.utilities.CloseTab;
import eu.scy.tools.dataProcessTool.utilities.CopexButtonPanel;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.ElementToSort;
import eu.scy.tools.dataProcessTool.utilities.ScyTabbedPane;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

/**
 * Tabbed Pane  : dataset visualization 
 * @author Marjolaine Bodin
 */
public class DataSetTabbedPane extends ScyTabbedPane{
    // PROPERTY
    /* liste des tables */
    private ArrayList<DataTable> listDataTable;
    /* liste des JScrollPane */
    private ArrayList<JScrollPane> listScrollPane;
    /* table active */
    private DataTable dataTableActiv;
    
    
    // CONSTRUCTOR 
    public DataSetTabbedPane(MainDataToolPanel a) {
        super();
        this.owner = a;
        listDataTable = new ArrayList();
        listScrollPane = new ArrayList();
        init();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void setSelected(CloseTab tab) {
        super.setSelected(tab);
    }
    
    @Override
    public void addTab(String title, Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setSize(this.getWidth() -10, this.getHeight() -10);
        listScrollPane.add(scrollPane);
        int index = 0;
        if (getTabCount() > 1)
            index = getTabCount()-1 ;
        super.insertTab("", null, scrollPane, "",  index);
        ImageIcon  iconClose = this.owner.getCopexImage("Bouton-onglet_fermeture.png");
        ImageIcon  iconRollOver = this.owner.getCopexImage("Bouton-onglet_fermeture_sur.png");
        ImageIcon  iconClic = this.owner.getCopexImage("Bouton-onglet_fermeture_cli.png");
        
        CloseTab closeTab = new CloseTab(this.owner, this, title, iconClose, iconRollOver, iconClic, iconClose);
        if (component instanceof DataTable){
            listDataTable.add((DataTable)component);
            dataTableActiv = (DataTable)component;
            listCloseTab.add(closeTab);
        }
        setTabComponentAt(index, closeTab);
        setSelectedIndex(index);
    }

    @Override
    public void setSelectedIndex(int index) {
        if (this.listDataTable.size() > 0 && index == this.listDataTable.size()){
            return;
        }
        if (listDataTable.size() == 0){
            if (this.closeTabAdd != null)
                this.closeTabAdd.setSelected(true);
            return;
        }

        super.setSelectedIndex(index);
        this.closeTabAdd.setSelected(false);
        if (index == this.listDataTable.size())
            this.dataTableActiv = null;
        else
            //this.treeActiv = this.listCopexTree.get(this.listCopexTree.size() - 1-index);
            this.dataTableActiv = this.listDataTable.get(index);
        for (int k=0; k<listCloseTab.size(); k++){
            //if (k == this.listCopexTree.size() - 1-index){
            if (k == index){
                this.listCloseTab.get(k).setSelected(true);
            }else
                this.listCloseTab.get(k).setSelected(false);
        }
        owner.setDatasetActiv(this.dataTableActiv.getDataset());
    }
    
    /* retourne vrai si le bouton est un bouton de fermeture et dans ce cas, 
     * met en v[0] le dataset à fermer
     */
    public boolean isButtonClose(CopexButtonPanel b, ArrayList v){
       for (int i=0;i<listCloseTab.size(); i++){
           CloseTab t = listCloseTab.get(i);
           if (b == t.getButtonClose()){
               v.add(listDataTable.get(i).getDataset());
               return true;
           }
       }
       return false;
    }

    /* retourne le dataset selectionne - null sinon */
    public Dataset getSelectedDataset(){
        Dataset ds = null;
        if (dataTableActiv != null)
            ds = dataTableActiv.getDataset();
        return ds;
    }
    
    /* retourne vrai si tri est possible */
    public boolean canSort(){
        if (this.dataTableActiv == null)
            return false;
        return dataTableActiv.canSort();
    }
    
    /* retourne vrai si undo est possible */
    public boolean canUndo(){
        if (this.dataTableActiv == null)
            return false;
        return dataTableActiv.canUndo();
    }
    
    /* retourne vrai si redo est possible */
    public boolean canRedo(){
        if (this.dataTableActiv == null)
            return false;
        return dataTableActiv.canRedo();
    }
    
    /* retourne vrai si suppr est possible */
    public boolean canSuppr(){
        if (this.dataTableActiv == null)
            return false;
        return dataTableActiv.canSuppr();
    }
    
    /* retourne vrai si copy est possible */
    public boolean canCopy(){
        if (this.dataTableActiv == null)
            return false;
        return dataTableActiv.canCopy();
    }
    
    /* retourne vrai si paste est possible */
    public boolean canPaste(){
        if (this.dataTableActiv == null)
            return false;
        return dataTableActiv.canPaste();
    }
    
    /* retourne vrai si cut est possible */
    public boolean canCut(){
        if (this.dataTableActiv == null)
            return false;
        return dataTableActiv.canCut();
    }

    /* recherche indice table correspondant au ds  // - 1 sinon */
    private int getIdDataTable(Dataset ds){
        int nb =listDataTable.size();
        for (int i=0; i<nb; i++){
            if (listDataTable.get(i).getDataset().getDbKey() == ds.getDbKey()){
                return i;
            }
        }
        return -1;
    }

    /* fermeture d'un onglet */
    public void closeTab(Dataset ds){
        int id = getIdDataTable(ds);
        if (id == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_CLOSE_DATA_TABLE"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        remove(id);
        listDataTable.remove(id);
        listCloseTab.remove(id);
        setSelectedIndex(0);
        revalidate();
        repaint();
    }

    /* retourne le dataset correspondant à un closeTab*/
    public Dataset getDataset(CloseTab closeTab){
        int nb = listCloseTab.size();
        for (int i=0; i<nb; i++){
            if (listCloseTab.get(i).equals(closeTab)){
                return listDataTable.get(i).getDataset() ;
            }
        }
        return null;
    }

    /* mise à jour du nom du dataset */
    public void updateDatasetName(Dataset ds, String newName){
        int id = getIdDataTable(ds);
        if (id == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_UPDATE_DATASET_NAME"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        listDataTable.get(id).getDataset().setName(newName);
        listCloseTab.get(id).updateTitle(newName);
        revalidate();
        repaint();
    }

    /* mise à jour du dataset */
    public void updateDataset(Dataset ds, boolean reload){
        int id = getIdDataTable(ds);
        if (id == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_DATA_IGNORED"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        listDataTable.get(id).updateDataset(ds, reload);
    }

    /* creation d'une nouvelle operation */
    public void createOperation(Dataset ds, DataOperation operation){
        int id = getIdDataTable(ds);
        if (id == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_CREATE_OPERATION"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        listDataTable.get(id).createOperation(ds, operation);
    }

    /* execution du tri */
    public void executeSort(ElementToSort keySort1, ElementToSort keySort2, ElementToSort keySort3){
        Dataset ds = getSelectedDataset();
        if(ds == null){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        int id = getIdDataTable(ds);
        if (id == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        try{
            //recupere le tableau a trier (concatenation des cles)
            Vector tabToSort = getElementsToSort(ds, keySort1, keySort2, keySort3);
            if (tabToSort == null)
                return;
            //tri quickSort sur tout le tableau
            Vector exchange = getExchange(ds);
            executeQuickSort(0,tabToSort.size() - 1 ,exchange, tabToSort);
            
            owner.updateDatasetRow(ds, exchange);
         }catch(Throwable t){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT")+" "+t, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* retourne le vecteur avec indice des lignes */
    private Vector getExchange(Dataset ds){
        Vector exchange = new Vector();
        for (int i=0; i<ds.getNbRows(); i++){
            exchange.add(i);
        }
        return exchange;
    }


    private void executeQuickSort(int g, int d,Vector exchange, Vector keys) {
        int i,j, idT=-1;
        String v = " ",t=" ";
        while (g < d) {
            v = (String)keys.elementAt(d);
            i= g-1;
            j = d;
            for (;;) {
                while (((String)keys.elementAt(++i)).compareTo(v) < 0 )
                    if (i == d)
                        break;
                while (((String)keys.elementAt(--j)).compareTo(v) > 0 )
                    if (j == g)
                        break;
                if (i >= j)
                    break;
            idT=i;
              /* On échange */
              t = ((String)keys.elementAt(i));
              keys.setElementAt(keys.elementAt(j),i);

              Object w = exchange.elementAt(j);
              exchange.setElementAt(exchange.elementAt(i),j);
              exchange.setElementAt(w,i);
              keys.setElementAt(t,j);
            }

            /* On échange */
            t = ((String)keys.elementAt(i));
            keys.setElementAt(keys.elementAt(d),i);
            Object w =  exchange.elementAt(d);
            exchange.setElementAt(exchange.elementAt(i),d);
            exchange.setElementAt(w,i);
            keys.setElementAt(t,d);

            executeQuickSort (g, i-1,exchange,  keys);
            g = i+1;
        }
    }

    /**
    * On récupère la colonne à trier. On la stocke dans un vecteur
    * @return Vector
    * @param col int
    */
    private Vector getElementsToSort(Dataset ds, ElementToSort keySort1, ElementToSort keySort2, ElementToSort keySort3){
        //nom de la premiere cle a trier
        String name = keySort1.getColumnName().trim() ;
        int col = getIdColWithNameLike(ds, name);
        if (col == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT_KEY")+" "+name,false),owner.getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }

        // vecteur dans lequel on range les cles  : tableau a trier
        Vector elementsToSort = new Vector() ;
        // construction de la premiere cle de tri
        elementsToSort = buildKeyForSort(ds, elementsToSort,col,keySort1.getOrder());
        if (elementsToSort == null){
            return null;
        }
        // si 2ieme cle :
        if (keySort2!=null){
            name = keySort2.getColumnName().trim() ;
            col = getIdColWithNameLike(ds, name);
            if (col == -1){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT_KEY")+" "+name,false),owner.getBundleString("TITLE_DIALOG_ERROR"));
                return null;
            }

            // construction de la 2ieme cle
            elementsToSort = buildKeyForSort(ds, elementsToSort,col,keySort2.getOrder());
            if (elementsToSort == null){
                return null;
            }

            // si 3ieme cle :
            if (keySort3!=null){
                name = keySort3.getColumnName().trim() ;
                col = getIdColWithNameLike(ds, name);

                if (col == -1){
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT_KEY")+" "+name,false),owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return null;
                }

                // construction de la 3ieme cle :
                elementsToSort = buildKeyForSort(ds, elementsToSort,col,keySort3.getOrder());
                if (elementsToSort == null){
                    return null;
                }
            }
        }
        return elementsToSort;
    }

    /**
    * Selon le nom de la colonne passée en paramètre, elle renvoie l'indice de cette colonne dans la table agent.
    * @return int : le numéro de la colonne de la table agent
    * @param nom java.lang.String
    */
    private int getIdColWithNameLike(Dataset ds, String nom){
        for (int i=0; i<ds.getListDataHeader().length; i++){
            if (ds.getListDataHeader()[i] != null && ds.getListDataHeader()[i].getValue().equals(nom)){
                return i;
            }
        }
        return -1;
    }

    /* construction des cles pour le tri */
    private Vector buildKeyForSort(Dataset ds, Vector elementsToSort, int col, int order) {
        int nb = ds.getNbRows() ;
        for(int i=0; i<nb; i++){
            String element;
            // recuperation objet
            Data o = ds.getData(i, col) ;
            if (o == null)
                element = "";
            else
                element = ""+o.getValue();
            String element1="";
            // maxSize est la longueur max des objets dans chq colonne
            int maxSize = ds.getValueMaxSizeIn(col);
            if (maxSize == -1){
                owner.displayError(new CopexReturn ("Erreur fatale : problème avec la méthode getValueMaxSizeIn() !",false),"Construction de la clé de tri");
                return null;
            }
            // si chaine est plus courte, on complete avec des blancs.
            if (element.length() < maxSize ){
                for(int j=element.length(); j <= maxSize; j++)
                    element =" "+element;
            }

            // ordre decroissant
            // on inverse
            // char est borne entre 0 et 255
            if (order == 0){
                for(int k=0; k<maxSize; k++){
                    int c = 255 - element.charAt(k);
                    element1+=c;
                }
            }

            // ordre croissant
            if (element1.equals(""))
                element1=element;


            if (elementsToSort.size() == nb){
                String chaine = (String)(elementsToSort.elementAt(i));
                String el  = chaine.concat(element1);
                elementsToSort.removeElementAt(i);
                elementsToSort.insertElementAt(el,i);
            }
            else
                elementsToSort.addElement(element1);
        }
        return elementsToSort;
    }

}
