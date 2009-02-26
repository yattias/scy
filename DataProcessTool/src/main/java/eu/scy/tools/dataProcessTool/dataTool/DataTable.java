/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.dnd.SubData;
import eu.scy.tools.dataProcessTool.dnd.SubDataTransfertHandler;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.DataTableRenderer;
import eu.scy.tools.dataProcessTool.utilities.CellEditorTextField ;
import java.awt.FontMetrics;
import javax.swing.DefaultCellEditor;
import javax.swing.DropMode;
import javax.swing.table.TableColumn;

/**
 * table which represents the dataset and the operations
 * @author Marjolaine Bodin
 */
public class DataTable extends JTable implements MouseListener{
    // CONSTANTES
    /* largeur min d'une colonne */
    private static final int MIN_WIDTH_COL = 60;
    // PROPERTY 
    /* owner */
    protected MainDataToolPanel owner;
    /* modele de données */
    protected DataTableModel tableModel ;
    /* dataset correspondant */
    protected Dataset dataset;
    /* rnederer*/
    protected DataTableRenderer dataTableRenderer;
    /* cell editor */
    DefaultCellEditor myCellEditor;
    
    /*menu droit */
    private DataMenu popUpMenu;

    /* sous table copiée */
    private SubData copySubData;

    /* drag and drop */
    private SubDataTransfertHandler transferHandler;
    
    
    // CONSTRUCTOR
    public DataTable(MainDataToolPanel owner, Dataset ds) {
        super();
        this.owner = owner;
        this.tableModel = new DataTableModel(owner, this, ds);
        setModel(this.tableModel);// modele de données
        this.dataset = ds;
        setTableHeader(null);
        // rederer
        this.dataTableRenderer = new DataTableRenderer();
        setDefaultRenderer(Object.class, dataTableRenderer);
        // selection 
        setColumnSelectionAllowed(true);
        // cell editor
        CellEditorTextField cellField = new CellEditorTextField(this);
        myCellEditor = new DefaultCellEditor(cellField);
        this.setCellEditor(new DefaultCellEditor(cellField));
        this.setDefaultEditor(Object.class, cellEditor);
        // ecoute evements
        addMouseListener(this);
        // DRAG AND DROP
        /*setDragEnabled(true);
        transferHandler  = new SubDataTransfertHandler();
        setTransferHandler(transferHandler);
        setDropMode(DropMode.INSERT);*/

        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resizeColumn();

    }

    // GETTER AND SETTER
    public Dataset getDataset() {
        return dataset;
    }
    
    // METHOD
    /* retourne vrai s'il s'agit d'une cellule contenant la numerotation des lignes*/
    public boolean isValueNoRow(int noRow, int noCol){
        return  tableModel.isValueNoRow(noRow, noCol);
    }
    
    /* retourne vrai s'il s'agit d'une cellule contenant un header (hors titre des operations) */
    public boolean isValueHeader(int noRow, int noCol){
        return  tableModel.isValueHeader(noRow, noCol);
    }
    
    /* retourne vrai s'il s'agit d'une cellule operations */
    public boolean isValueOperation(int noRow, int noCol){
        return  tableModel.isValueOperation(noRow, noCol);
    }
    
    /* retourne vrai s'il s'agit d'une cellule titre operations */
    public boolean isValueTitleOperation(int noRow, int noCol){
        return  tableModel.isValueTitleOperation(noRow, noCol);
    }
    /* retourne vrai s'il s'agit d'une celllue data */
    public boolean isValueData(int noRow, int noCol){
        return tableModel.isValueData(noRow, noCol);
    }
     /* retourne vrai s'il s'agit d'une celllue data ignorée */
    public boolean isValueDataIgnored(int noRow, int noCol){
        return tableModel.isValueDataIgnored(noRow, noCol);
    }
    /* retourne vrai s'il s'agit d'une celllue de la dernière ligne (entre 1 et nbColsDs) */
    public boolean isValueLastRow(int noRow, int noCol){
        return tableModel.isValueLastRow(noRow, noCol);
    }
    /* retourne vrai s'il s'agit d'une celllue de la dernière colonne (entre 1 et nbRowsDs) */
    public boolean isValueLastCol(int noRow, int noCol){
        return tableModel.isValueLastCol(noRow, noCol);
    }
    /* retourne la couleur selon le type d'operations */
    public Color getOperationColor(int noRow, int noCol){
        return  tableModel.getOperationColor(noRow, noCol);
    }
    

    // EVENEMENTS SOURIS
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            popUpMenu = null;
            int x = e.getPoint().x;
            int y = e.getPoint().y;
            if (!isElementsSel()) 
               return;
            getPopUpMenu();
            boolean isOnlyDataSel = isElementsDataSel() ;
            boolean isLastCellSel = isLastCellSel();
            boolean isALineSel = isALineSel();


            this.popUpMenu.setEnabledItemIgnored(isOnlyDataSel);
            this.popUpMenu.setEnabledItemNotIgnored(isOnlyDataSel);
            this.popUpMenu.setEnabledItemOperation(isLastCellSel);
            this.popUpMenu.setEnabledItemInsert(isALineSel);
            this.popUpMenu.setEnabledItemDelete(isALineSel);
            this.popUpMenu.setEnabledItemCopy(canCopy());
            this.popUpMenu.setEnabledItemPaste(false);
            this.popUpMenu.setEnabledItemCut(false);
            this.popUpMenu.setEnabledItemSort(false);

            this.popUpMenu.show(this, x, y);
        }else if (e.getClickCount() == 1){
            Point p = e.getPoint() ;
            int c = columnAtPoint(p);
            int r = rowAtPoint(p);
            int nbR = getNbRows() ;
            int nbC = getNbCols() ;
            if (r == 0 && (c > 0 &&  c < (nbC -1))){
                // selectionne toute la colonne
                for (int i=1; i<nbR; i++)
                    changeSelection(i, c, true, true);
            }else if (c == 0 && (r > 0 && r <(nbR  -1) )){
                // selectionne toute la ligne
                for (int j=1; j<nbC; j++)
                    changeSelection(r, j, true, true);
            }
            
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
    
    /* retourne la liste des cellules selectionnées */
    private ArrayList<int[]> getSelectedCells(){
        ArrayList<int[]> selCells = new ArrayList();
        int[] selRows = this.getSelectedRows(); 
        int[] selCols = this.getSelectedColumns() ;
        for (int i=0; i<selRows.length; i++){
            for (int j=0; j<selCols.length; j++){
                int[] cell = new int[2];
                cell[0] = selRows[i];
                cell[1] = selCols[j];
                selCells.add(cell);
            }
        }
        return selCells;
    }
    
    /* retourne vrai s'il existe des elements sel */
    private boolean isElementsSel(){
        ArrayList<int[]> cellsSel = getSelectedCells();
        return cellsSel.size() > 0;
    }
    
    /* retourne vrai ssi des données sont sélectionnées*/
    private boolean isElementsDataSel(){
        boolean isData = true;
        ArrayList<int[]> cellsSel = getSelectedCells();
        int nb = cellsSel.size();
        for (int i=0; i<nb; i++){
            if (!isValueHeader(cellsSel.get(i)[0], cellsSel.get(i)[1]) && !isValueData(cellsSel.get(i)[0], cellsSel.get(i)[1])){
                isData = false;
                break;
            }
        }
        return isData;
    }

    /* retourne vrai ssi au moins des données sont sélectionnées*/
    private boolean isAtLeastElementsDataSel(){
        boolean isData = false;
        ArrayList<int[]> cellsSel = getSelectedCells();
        int nb = cellsSel.size();
        for (int i=0; i<nb; i++){
            if (isValueHeader(cellsSel.get(i)[0], cellsSel.get(i)[1]) || isValueData(cellsSel.get(i)[0], cellsSel.get(i)[1])){
                isData = true;
                break;
            }
        }
        return isData;
    }

    /* retourne vrai si dans la sélection il y a des cellules de la dernière ligne ou des cellules de la derniere colonne seulement */
    private boolean isLastCellSel(){
        boolean lastRow = isLastRowSel();
        boolean lastCol = isLastColSel();
        return ((lastCol && !lastRow) || (lastRow && !lastCol) );
    }

    /* retourne vrai si dans la sélection il y a des cellules de la dernière ligne*/
    private boolean isLastRowSel(){
        ArrayList<int[]> cellsSel = getSelectedCells();
        int nb = cellsSel.size();
        for (int i=0; i<nb; i++){
            if (isValueLastRow(cellsSel.get(i)[0], cellsSel.get(i)[1])){
                return true;
            }
        }
        return false;
    }
     /* retourne vrai si dans la sélection il y a des cellules de la dernière colonne*/
    private boolean isLastColSel(){
        ArrayList<int[]> cellsSel = getSelectedCells();
        int nb = cellsSel.size();
        for (int i=0; i<nb; i++){
            if (isValueLastCol(cellsSel.get(i)[0], cellsSel.get(i)[1])){
                return true;
            }
        }
        return false;
    }

    /* retourne vrai si au moins une   ligne ou une  colonne est selectionnée */
    private boolean isALineSel(){
        boolean rowSel = isLineRowSel();
        boolean colSel = isLineColSel();
        return ((colSel && !rowSel) || (rowSel && !colSel) );
    }



    /* retourne vrai si une ou plusieurs colonnes sont selectionnées */
    private boolean isLineRowSel(){
        int[] selCols = this.getSelectedColumns() ;
        return selCols.length == this.getNbCols() ;
    }
    /* retourne vrai si une ou plusieurs lignes sont selectionnées */
    private boolean isLineColSel(){
        int[] selRows = this.getSelectedRows() ;
        return selRows.length == this.getNbRows() ;
    }



    /*retourne la liste des données selectionnées */
    private ArrayList<Data> getSelectedData(){
        return this.tableModel.getSelectedData(getSelectedCells());
    }

    /* retourne le nb de colonnes */
    private int getNbCols(){
       return tableModel.getColumnCount() ;
    }
     /* retourne le nb de lignes */
    private int getNbRows(){
       return tableModel.getRowCount() ;
    }

    /* creation du pop up menu */
    private DataMenu getPopUpMenu(){
        if (this.popUpMenu == null){
            popUpMenu = new DataMenu(owner, this);
        }
        return this.popUpMenu ;
    }
    
    /* retourne vrai si tri est possible */
    public boolean canSort(){
        return this.dataset.getNbCol() > 0;
    }
    
    /* retourne vrai si undo est possible */
    public boolean canUndo(){
        return false;
    }
    
    /* retourne vrai si redo est possible */
    public boolean canRedo(){
        return false;
    }
    
    /* retourne vrai si suppr est possible */
    public boolean canSuppr(){
        return isALineSel() ;
    }
    
    /* retourne vrai si copy est possible */
    public boolean canCopy(){
        return isAtLeastElementsDataSel();
    }
    
    /* retourne vrai si paste est possible */
    public boolean canPaste(){
        return false;
    }
    
    /* retourne vrai si cut est possible */
    public boolean canCut(){
        return canSuppr() && canCopy() ;
    }

    /* clic sur ignorer data */
    public void ignoreData(){
        owner.setDataIgnored(dataset, true,getSelectedData());
    }
    /* clic sur non ignorer data */
    public void restoreIgnoreData(){
        owner.setDataIgnored(dataset, false,getSelectedData());
    }

     /* calcule */
    public void calculate(int type){
        boolean isOnCol = false;
        ArrayList<Integer> listNo = new ArrayList();
        ArrayList<int[]> cellsSel = getSelectedCells();
        int nb = cellsSel.size();
        for (int i=0; i<nb; i++){
            if (isValueLastRow(cellsSel.get(i)[0], cellsSel.get(i)[1])){
                isOnCol = true;
                listNo.add(cellsSel.get(i)[1]-1);
            }else if (isValueLastCol(cellsSel.get(i)[0], cellsSel.get(i)[1])){
                listNo.add(cellsSel.get(i)[0]-1);
            }
        }
        owner.createOperation(dataset, type, isOnCol, listNo);
    }
    /* somme */
    public void sum(){
        calculate(DataConstants.OP_SUM);
    }

     /* moyenne */
    public void avg(){
        calculate(DataConstants.OP_AVERAGE);
    }

     /* min */
    public void min(){
        calculate(DataConstants.OP_MIN);
    }

     /* max */
    public void max(){
        calculate(DataConstants.OP_MAX);
    }

    /* somme */
    public void sumIf(){
        calculate(DataConstants.OP_SUM_IF);
    }

    /* insertion de lignes */
    public void insert(){
        ArrayList<int[]> listSelCell = getSelectedCells();
        ArrayList v  = tableModel.getSelectedIdForInsert(listSelCell);
        boolean isOnCol = (Boolean)v.get(0);
        int nb=(Integer)v.get(1);
        int idBefore = (Integer)v.get(2);
        owner.insertData(dataset, isOnCol, nb, idBefore);
    }

    /* suppression de lignes ou de colonnes */
    public void delete(){
        ArrayList<int[]> listSelCell = getSelectedCells();
        owner.deleteData(dataset, tableModel.getSelectedData(listSelCell), tableModel.getSelectedOperation(listSelCell), tableModel.getSelectedRowAndCol(listSelCell));
    }

    /* mise àjour dataset */
    public void updateDataset(Dataset ds, boolean reload){
        this.dataset = ds ;
        this.tableModel.updateDataset(ds, reload);
        revalidate();
        repaint();
    }

    /* creation d'une nouvelle operation */
    public void createOperation(Dataset ds, DataOperation operation){
        this.dataset = ds;
        this.tableModel.createOperation(ds, operation);
        resizeColumn();
        revalidate();
        repaint();
    }

    /*retourne vrai si la selection est une sous table (colonnes entières) */
    public boolean selIsSubData(){
        return isLineColSel();
    }

    
    /*retourne la sous table correspondant à la selection */
    public SubData getSubDataCopy(){
        return new SubData(owner, tableModel.getSelectedDataset(getSelectedCells()), this, tableModel.getSelectedNoHeaders(getSelectedCells()));
    }

   /* move de données avant le no de col (en dernier si = size) */
    public boolean moveSubData(SubData subDataToMove, int noColInsertBefore){
        // TODO appel au controller
        boolean isOk = this.owner.moveSubData(subDataToMove, noColInsertBefore);
        if (!isOk)
            return false;
        this.tableModel.moveSubData(subDataToMove, noColInsertBefore);
        return true;
    }

    /* suppression données apres DRAG and drop */
    public void removeData(SubData subData ){
       // this.tableModel.removeData(subData);
        //revalidate();
        //repaint();
    }

    /* retourne vrai si la cellule selectionnée est de type data */
    public boolean isCellSelectedData(){
        ArrayList<int[]> listCellSel = getSelectedCells();
       if (listCellSel.size() != 1)
            return false;
        return isValueData(listCellSel.get(0)[0], listCellSel.get(0)[1]) ;

    }

    /* copie => met en cache les données sélectionnées  + update menu */
    public void copy(){
       // subData = new SubData(owner, dataset, this, noHeaders)
        owner.updateMenu();
    }

    /* resize column */
    public void resizeColumn(){
        /**for (int i=0; i<getColumnCount(); i++){
            TableColumn tc = getColumnModel().getColumn(i);
            tc.sizeWidthToFit();
        }
        repaint();
         * */
         int widthTot = 0;
        for (int j = 0 ; j < this.getNbCols() ; j++){
            int max = 0;
            for (int i = 0 ; i < this.getNbRows() ; i++){
                FontMetrics fm = getFontMetrics(this.dataTableRenderer.getFont(this, i, j));
               int taille = 0;
               if (this.getValueAt(i, j) != null){
                   String s = "";
                   if (this.getValueAt(i, j) instanceof String)
                       s = (String)this.getValueAt(i, j) ;
                   else if (this.getValueAt(i, j) instanceof Double){
                       s = Double.toString((Double)this.getValueAt(i, j)) ;
                   }else if (this.getValueAt(i, j) instanceof Integer){
                       s = Integer.toString((Integer)this.getValueAt(i, j)) ;
                   }
                   taille = fm.stringWidth(s);
               }
               max = Math.max(taille, max);
            }
            
           max = Math.max(MIN_WIDTH_COL, max);
           getColumnModel().getColumn(j).setPreferredWidth(max+10);
           widthTot += (max+10);
        }
         int height = this.getRowCount() * getRowHeight() ;
         System.out.println("widthTot : "+widthTot);
         System.out.println("height : "+height);
         setSize(widthTot, height);
         setPreferredSize(getSize());
        repaint();

    }

    
   
}
