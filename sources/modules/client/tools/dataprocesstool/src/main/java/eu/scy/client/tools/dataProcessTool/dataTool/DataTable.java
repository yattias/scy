/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.common.CopyDataset;
import eu.scy.client.tools.dataProcessTool.common.Data;
import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.DataOperation;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.undoRedo.DataUndoManager;
import eu.scy.client.tools.dataProcessTool.undoRedo.DataUndoRedo;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.DataTableRenderer;
import eu.scy.client.tools.dataProcessTool.utilities.CellEditorTextField ;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.ElementToSort;
import eu.scy.client.tools.dataProcessTool.utilities.MyTableEditor;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.Timer;
import javax.swing.undo.CannotUndoException;

/**
 * table which represents the dataset and the operations
 * @author Marjolaine Bodin
 */
public class DataTable extends JTable implements MouseListener, MouseMotionListener, KeyListener{
    /* width min of a column */
    private static final int MIN_WIDTH_COL = 20;
    private static final int DELTA_RESIZE_COL = 5;


    /* owner */
    protected FitexToolPanel owner;
    /* data model */
    protected DataTableModel tableModel ;
    /* dataset */
    protected Dataset dataset;
    /* renderer*/
    protected DataTableRenderer dataTableRenderer;
    /* cell editor */
    private MyTableEditor myCellEditor;
    
    /*pop up menu, right click */
    private DataMenu popUpMenu;

    /* data copied */
    private CopyDataset copyDs = null;

    /* undo/redo */
    private DataUndoManager undoManager;
    
    private int ownerWidth = 500;

    private int noColToResize;
    private double xColStart;
    private boolean resizeTableWidth;
    private boolean resizeTableHeight;
    private Point startPoint;

    /* multi-selection */
    private int startIdColSel;
    private int startIdRowSel;

    /* cells selection */
    private List<int[]> listSelectedCell;

    private boolean doubleClickOnCell = false;
    private Timer timer;
    private final static int clickInterval = 2*(Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
    private MouseEvent lastEvent;

    public DataTable(FitexToolPanel owner, Dataset ds) {
        super();
        this.owner = owner;
        this.tableModel = new DataTableModel(owner, this, ds);
        setModel(this.tableModel);// data model
        this.dataset = ds;
        this.setTableHeader(null);
        noColToResize = -1;
        // renderer
        this.dataTableRenderer = new DataTableRenderer();
        setDefaultRenderer(Object.class, dataTableRenderer);
        // selection 
        setColumnSelectionAllowed(true);
        // cell editor
        CellEditorTextField cellField = new CellEditorTextField(this);
        //myCellEditor = new DefaultCellEditor(cellField);
        myCellEditor = new MyTableEditor(this,cellField);
        //this.setCellEditor(new DefaultCellEditor(cellField));
        this.setCellEditor(myCellEditor);
        this.setDefaultEditor(Object.class, myCellEditor);
        this.setRowHeight(21);
        // events
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        this.setSurrendersFocusOnKeystroke(false);
        // DRAG AND DROP
        /*setDragEnabled(true);
        transferHandler  = new SubDataTransfertHandler();
        setTransferHandler(transferHandler);
        setDropMode(DropMode.INSERT);*/
        undoManager = new DataUndoManager(this);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resizeColumn();
        this.setShowGrid(false);
        this.setIntercellSpacing(new Dimension(0,0));
        initTimer();
    }

    public Dataset getDataset() {
        return dataset;
    }
    
    /* returns true if the cell contains the rows no */
    public boolean isValueNoRow(int noRow, int noCol){
        return  tableModel.isValueNoRow(noRow, noCol);
    }
    
    /* returns true if the cell contains the header (no operation title) */
    public boolean isValueHeader(int noRow, int noCol){
        return  tableModel.isValueHeader(noRow, noCol);
    }
    /* returns true if the cell contains a header with double type */
    public boolean isValueDoubleHeader(int noRow, int noCol){
        return  tableModel.isValueDoubleHeader(noRow, noCol);
    }
    
    /* returns true if the cell contains an operation  */
    public boolean isValueOperation(int noRow, int noCol){
        return  tableModel.isValueOperation(noRow, noCol);
    }
    
    /* returns true if the cell contains operation title  */
    public boolean isValueTitleOperation(int noRow, int noCol){
        return  tableModel.isValueTitleOperation(noRow, noCol);
    }
    /* returns true if the cell contains data */
    public boolean isValueData(int noRow, int noCol){
        return tableModel.isValueData(noRow, noCol);
    }
     /*returns true if the cell contains data ignored*/
    public boolean isValueDataIgnored(int noRow, int noCol){
        return tableModel.isValueDataIgnored(noRow, noCol);
    }
    /* return true if the cell is in the last row  (between 1 and nbColsDs) */
    public boolean isValueLastRow(int noRow, int noCol){
        return tableModel.isValueLastRow(noRow, noCol);
    }
    /*return true if the cell is in the last column  (between 1 and nbRowsDs) */
    public boolean isValueLastCol(int noRow, int noCol){
        return tableModel.isValueLastCol(noRow, noCol);
    }
    /*returns the color depending of the operation's type */
    public Color getOperationColor(int noRow, int noCol){
        return  tableModel.getOperationColor(noRow, noCol);
    }
    
    //mouse event
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            boolean canEdit = dataset.getRight()==DataConstants.EXECUTIVE_RIGHT;
            popUpMenu = null;
            int x = e.getPoint().x;
            int y = e.getPoint().y;
            if (!isElementsSel())
               return;
            getPopUpMenu();
            this.popUpMenu.setEnabledItemIgnored(canEdit&& canIgnore());
            this.popUpMenu.setEnabledItemNotIgnored(canEdit&& canIgnore());
            this.popUpMenu.setEnabledItemOperation(canEdit&& canOperations());
            this.popUpMenu.setEnabledItemInsert(canEdit&& canInsert());
            this.popUpMenu.setEnabledItemDelete(canEdit&& canSuppr());
            this.popUpMenu.setEnabledItemCopy(canEdit&& canCopy());
            this.popUpMenu.setEnabledItemPaste(canEdit&& canPaste());
            this.popUpMenu.setEnabledItemCut(canEdit&& canCut());
            this.popUpMenu.setEnabledItemSort(canSort());
            this.popUpMenu.show(this, x, y);
        }
            Point p = e.getPoint() ;
            int c = columnAtPoint(p);
            int r = rowAtPoint(p);
            if(tableModel.isValueHeader(r, c) ){
                if(e.getClickCount() == 2){
                    owner.editDataHeader(tableModel.getHeader(r,c), c-1);
                    return;
                }
                changeSelection(r, c, false, false);
                selectEntireColumnRow(r,c);
                return;
            }

//        else if (e.getClickCount() == 1){
//            Point p = e.getPoint() ;
//            int c = columnAtPoint(p);
//            int r = rowAtPoint(p);
//            changeSelection(r, c, false, false);
//            selectEntireColumnRow(r,c);
//        }else if (e.getClickCount() ==2){
//            doubleClickOnCell = true;
//            Point p = e.getPoint() ;
//            int c = columnAtPoint(p);
//            int r = rowAtPoint(p);
//            if(tableModel.isValueHeader(r, c)){
//                owner.editDataHeader(tableModel.getHeader(r,c), c-1);
//            }
//        }
          if (e.getClickCount() > 2) return;
          lastEvent = e;
          if (timer.isRunning())  {
             timer.stop();
             mouseDoubleClick(lastEvent);
         }  else {
             timer.restart();
         }
    }
    
    private void initTimer(){
        timer = new Timer( clickInterval, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();  
                mouseSimpleClick(lastEvent);
            }
        });
    }

    private void mouseSimpleClick(MouseEvent e){
        Point p = e.getPoint();
        int c = columnAtPoint(p);
        int r = rowAtPoint(p);
        changeSelection(r, c, false, false);
        selectEntireColumnRow(r,c);
        doubleClickOnCell = false;
    }

    private void mouseDoubleClick(MouseEvent e){
        Point p = e.getPoint();
        doubleClickOnCell = true;
        int c = columnAtPoint(p);
        int r = rowAtPoint(p);
        if(tableModel.isValueHeader(r, c)){
            owner.editDataHeader(tableModel.getHeader(r,c), c-1);
        }
    }

    private void selectEntireColumnRow(int r, int c){
            int nbR = getNbRows() ;
            int nbC = getNbCols() ;
            if (r == 0 && (c > 0 &&  c < nbC)){
                // select the entire column
                for (int i=1; i<nbR-1; i++)
                    changeSelection(i, c, true, true);
                owner.setVerticalScroll();
            }else if (c == 0 && (r > 0 && r <nbR )){
                // select the entire row
                for (int j=1; j<nbC-1; j++)
                    changeSelection(r, j, true, true);
            }
            owner.updateMenuData();
    }


    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint() ;
        int c = columnAtPoint(p);
        int r = rowAtPoint(p);

        if(r == 0)
            startIdColSel = c;
        else if(c == 0)
            startIdRowSel = r;
        else{
            startIdColSel = -1;
            startIdRowSel = -1;
        }
        if(r == 0 && (c != (getNbCols() - 1)) &&(columnAtPoint(new Point((int)p.getX()+DELTA_RESIZE_COL, (int)p.getY())) == c+1 || columnAtPoint(new Point((int)p.getX()-DELTA_RESIZE_COL, (int)p.getY())) == c-1)){
            this.noColToResize = c;
            this.xColStart = p.getX();
        }
        if (p.getX() > this.getWidth() - DELTA_RESIZE_COL){
            this.resizeTableWidth = true;
            this.startPoint = p;
        }
//        if (p.getY() > this.getHeight() - DELTA_RESIZE_COL){
//            this.resizeTableHeight = true;
//            this.startPoint = p;
//        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = e.getPoint() ;
        int c = columnAtPoint(p);
        int r = rowAtPoint(p);
        this.noColToResize = -1;
        this.resizeTableHeight = false;
        this.resizeTableWidth = false;
        this.startPoint = null;
        if(startIdRowSel != -1 && c==0){
            if(startIdRowSel < r){
                for (int i=startIdRowSel; i<=r; i++){
                    selectEntireColumnRow(i,c);
                }
            }else if(startIdRowSel > r){
                for (int i=r; i<=startIdRowSel; i++){
                    selectEntireColumnRow(i,c);
                }
            }
        }else if (startIdColSel != -1 && r==0){
            if(startIdColSel < c){
                for (int i=startIdColSel; i<=c; i++){
                    selectEntireColumnRow(r,i);
                }
            }else if(startIdColSel > c){
                for (int i=c; i<=startIdColSel; i++){
                    selectEntireColumnRow(r,i);
                }
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        owner.updateMenuData();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    /* returns the list of selected cells */
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
    
    /* returns true if there is at least one cell sel  */
    private boolean isElementsSel(){
        ArrayList<int[]> cellsSel = getSelectedCells();
        return cellsSel.size() > 0;
    }
    
    /* returns true if & only if data are selected */
    private boolean isElementsDataSel(){
        boolean isData = true;
        ArrayList<int[]> cellsSel = getSelectedCells();
        int nb = cellsSel.size();
        for (int i=0; i<nb; i++){
            boolean d = isValueData(cellsSel.get(i)[0], cellsSel.get(i)[1]);
            if (!isValueHeader(cellsSel.get(i)[0], cellsSel.get(i)[1]) && !d){
                isData = false;
                break;
            }
        }
        if(nb==0)
            return false;
        return isData;
    }


    /*returns true if & only if at least data are selected */
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

    /*returns true if only one cell is selected and this cell is data or header */
    private boolean isOnlyOneCellSelected(){
        ArrayList<int[]> cellsSel = getSelectedCells();
        int nb = cellsSel.size();
        if(nb != 1)
            return false;
        return isValueHeader(cellsSel.get(0)[0], cellsSel.get(0)[1]) || isValueData(cellsSel.get(0)[0], cellsSel.get(0)[1]) ;

    }

    /*returns true if in the selection, there is only  cells in the last line or last column */
    private boolean isLastCellSel(){
        boolean lastRow = isLastRowSel();
        boolean lastCol = isLastColSel();
        return ((lastCol && !lastRow) || (lastRow && !lastCol) );
    }

    /*returns true if in the selection, there is cells in the last row*/
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
     /* returns true if in the selection, there is cells in the last column*/
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

    /* returns true if at least one row or col is selected */
    private boolean isALineSel(){
        boolean rowSel = isLineRowSel();
        boolean colSel = isLineColSel();
        return ((colSel && !rowSel) || (rowSel && !colSel) );
        
    }

    /*returns true if at least one row or one column is selected &  at least one of these is type double */
    private boolean isADataLineSel(){
        boolean aLine = false;
        boolean isDouble = false;
        ArrayList<int[]> cellsSel = getSelectedCells();
        for(Iterator<int[]> c = cellsSel.iterator();c.hasNext();){
            int[] cell = c.next();
            boolean h = isValueHeader(cell[0], cell[1]);
            boolean r = isValueNoRow(cell[0], cell[1]);
            if( (h && !r) || (!h && r) )
                aLine = true;
            else if(h && r)
                aLine = false;
            if(h && (getDataset().getDataHeader(cell[1]-1) == null || getDataset().getDataHeader(cell[1]-1).isDouble())){
                isDouble = true;
            }
            if(r && getDataset().hasAtLeastADoubleColumn())
                isDouble = true;
        }
        return aLine&isDouble;
    }

    /*returns true if at least one column from type data is selected  */
    private boolean isADataColSel(){
        ArrayList<int[]> cellsSel = getSelectedCells();
        boolean isDataCols = false;
        for(Iterator<int[]> c = cellsSel.iterator();c.hasNext();){
            int[] cell = c.next();
            boolean h = isValueHeader(cell[0], cell[1]);
            boolean r = isValueNoRow(cell[0], cell[1]);
            boolean op = isValueTitleOperation(cell[0], cell[1]);
            if(h)
                isDataCols = true;
            if (r || op){
                isDataCols = false;
                break;
            }
        }
        return isDataCols;
    }


    /* returns true if an operation -row or col- is selected  */
    private boolean isOperationSelected(){
        ArrayList<int[]> cellsSel = getSelectedCells();
        for(Iterator<int[]> c = cellsSel.iterator();c.hasNext();){
            int[] cell = c.next();
           if (isValueTitleOperation(cell[0], cell[1]) ){
               return true;
           }
        }
        return false;
    }

    /* returns true if one or many columns are selected  */
    private boolean isLineRowSel(){
        int[] selCols = this.getSelectedColumns() ;
        return selCols.length == this.getNbCols()-1 ;
    }
    /* returns true if one or many rows are selected  */
    private boolean isLineColSel(){
        int[] selRows = this.getSelectedRows() ;
        return selRows.length == this.getNbRows() -1;
    }

    /*returns the selected Data*/
    public ArrayList<Data> getSelectedData(){
        return this.tableModel.getSelectedData(getSelectedCells(), false);
    }

    /* returns the selected headers*/
    public ArrayList<DataHeader> getSelectedHeader(){
        return this.tableModel.getSelectedHeader(getSelectedCells());
    }

    /* returns the first header selected , otherwise the first operation (on row), otherwise null */
    public Object getSelectedFirstColumn(){
        ArrayList<int[]> listCellSel = getSelectedCells();
        ArrayList<DataHeader> list = this.tableModel.getSelectedHeader(listCellSel);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        ArrayList<DataOperation> listOp = this.tableModel.getSelectedOperation(listCellSel);
        if (listOp != null){
            int nb = listOp.size();
            for (int i=0; i<nb; i++){
                if (!listOp.get(i).isOnCol())
                    return listOp.get(i);
            }
        }
        return null;
    }

    /* returns the nb of columns  */
    public int getNbCols(){
       return tableModel.getColumnCount() ;
    }
     /* returns the nb of rows */
    public int getNbRows(){
       return tableModel.getRowCount() ;
    }

    /* creation popup menu */
    private DataMenu getPopUpMenu(){
        if (this.popUpMenu == null){
            popUpMenu = new DataMenu(owner, this);
        }
        return this.popUpMenu ;
    }
    
    /* returns true if the sort is possible */
    public boolean canSort(){
        return this.dataset.getNbCol() > 0;
    }
    
    /* returns true if undo available */
    public boolean canUndo(){
        return undoManager.canUndo();
    }
    
    /* returns true if redo possible */
    public boolean canRedo(){
        return undoManager.canRedo();
    }
    
    /* returns true is remove possible */
    public boolean canSuppr(){
        //return isALineSel() ;
        return isAtLeastElementsDataSel() || isOperationSelected();
    }
    
    /* returns true if copy possible */
    public boolean canCopy(){
        return isAtLeastElementsDataSel();
    }
    
    /* returns true if paste possible */
    public boolean canPaste(){
        ArrayList<int[]> selectedCell = getSelectedCells();
        //        boolean canCopyH = copyDs != null && copyDs.getListHeader() != null &&
        //                ((copyDs.getListHeader().size() > 0 && tableModel.getSelectedRowAndCol(selectedCell)[1].size() > 0)
        //                || (copyDs.getListHeader().isEmpty() && tableModel.getSelectedRowAndCol(selectedCell)[1].isEmpty())) ;
        //        boolean canCopyR = copyDs != null && copyDs.getListRow() != null &&
        //                ((copyDs.getListRow().size() > 0 && tableModel.getSelectedRowAndCol(selectedCell)[0].size() > 0)
        //                || (copyDs.getListRow().isEmpty() && tableModel.getSelectedRowAndCol(selectedCell)[0].isEmpty())) ;
        return this.copyDs != null  && selectedCell.size() > 0 ;
    }
    
    /* returns true if cut possible */
    public boolean canCut(){
        return canSuppr() && canCopy() ;
    }

    /*returns true if a row or column can be insert */
    public boolean canInsert(){
        return isALineSel()  ;
    }

    /* returns true if data can be ignored or not */
    public boolean canIgnore(){
        return isElementsDataSel();
    }

    /* returns true if operations possible */
    public boolean canOperations(){
        return isADataLineSel() ;
    }


    public boolean canAlignText(){
        return isADataColSel()  ;
    }

    /* click on ignore data */
    public void ignoreData(){
        owner.setDataIgnored(dataset, true,getSelectedData(), true);
    }
    /* click on not ignore data */
    public void restoreIgnoreData(){
        markSelectedCell();
        owner.setDataIgnored(dataset, false,getSelectedData(), true);
    }

     /* calculating */
    public void calculate(int type){
        boolean isOnCol = false;
        ArrayList<Integer> listNo = new ArrayList();
        ArrayList<int[]> cellsSel = getSelectedCells();
        int nb = cellsSel.size();
        for (int i=0; i<nb; i++){
            if(isValueDoubleHeader(cellsSel.get(i)[0], cellsSel.get(i)[1]) ){
                isOnCol = true;
                listNo.add(cellsSel.get(i)[1]-1);
            }else if( isValueNoRow(cellsSel.get(i)[0], cellsSel.get(i)[1])){
                listNo.add(cellsSel.get(i)[0]-1);
                isOnCol = false;
            }
        }
        markSelectedCell();
        owner.createOperation(dataset, type, isOnCol, listNo, true);
    }
    /* sum */
    public void sum(){
        calculate(DataConstants.OP_SUM);
    }

     /* average */
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

    /* sum if */
    public void sumIf(){
        calculate(DataConstants.OP_SUM_IF);
    }

    /* insert rows/columns */
    public void insert(){
        ArrayList<int[]> listSelCell = getSelectedCells();
        ArrayList v  = tableModel.getSelectedIdForInsert(listSelCell);
        boolean isOnCol = (Boolean)v.get(0);
        int nb=(Integer)v.get(1);
        int idBefore = (Integer)v.get(2);
        owner.insertData(dataset, isOnCol, nb, idBefore, true);
    }

    /* columns selection */
    public void selectCols(int id, int nb){
        int nbR = this.getNbRows();
        changeSelection(0, 1+id, false, false);
        for (int j=0; j<nb; j++){
            for (int i=1; i<nbR-1; i++){
                changeSelection(i, 1+j+id, true, true);
            }
        }
        owner.updateMenuData();
    }

    /* rows selections */
    public void selectRows(int id, int nb){
        int nbC = this.getNbCols();
        changeSelection(1+id, 0, false, false);
        for (int i=0; i<nb; i++){
            for (int j=1; j<nbC-1; j++)
                changeSelection(1+i+id, j, true, true);
        }
        owner.updateMenuData();
    }
    /* remove rows or columns */
    public void delete(){
        ArrayList<int[]> listSelCell = getSelectedCells();
        ArrayList<Integer>[] listRowAndCol = tableModel.getSelectedRowAndCol(listSelCell);
        owner.deleteData(dataset, tableModel.getSelectedDataAlone(listSelCell,listRowAndCol ), tableModel.getSelectedHeader(listSelCell), tableModel.getSelectedOperation(listSelCell), listRowAndCol, true);
    }

    /* update the dataset */
    public void updateDataset(Dataset ds, boolean reload){
        this.dataset = ds ;
        this.tableModel.updateDataset(ds, reload);
        resizeColumn();
        this.revalidate();
        this.repaint();
    }
   
    /* creation of a new operation */
    public void createOperation(Dataset ds, DataOperation operation){
        this.dataset = ds;
        this.tableModel.createOperation(ds, operation);
        resizeColumn();
        selectOldCell();
        revalidate();
        repaint();
    }

    /* retusn true if the selection is a sub-table (entire columns)  */
    public boolean selIsSubData(){
        return isLineColSel();
    }

    
   
   
    /* retusn true if the selected cell is data */
    public boolean isCellSelectedData(){
        ArrayList<int[]> listCellSel = getSelectedCells();
       if (listCellSel.size() != 1)
            return false;
        return isValueData(listCellSel.get(0)[0], listCellSel.get(0)[1]) ;

    }

    /* copy => copyDs + menu update  */
    public ArrayList<int[]> copy(){
        ArrayList<int[]> listSelCell = getSelectedCells();
        copyDs = tableModel.getCopyDataset(listSelCell);
        owner.updateMenuData();
        owner.logCopy(dataset, listSelCell);
        return listSelCell;
    }

    /* resize column */
    public void resizeColumn(){
        //min_column_width = ownerWidth / getNbCols()- 5 ;
        int widthTot = 0;
        for (int j = 0 ; j < this.getNbCols() ; j++){
            int max = 0;
            if(j == getNbCols() - 1){
                max = MIN_WIDTH_COL;
            }else{
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
                        }else if (this.getValueAt(i, j) instanceof String[]){
                            s = ((String[])this.getValueAt(i, j))[0]+" ("+((String[])this.getValueAt(i, j))[1]+")";
                        }
                        taille = fm.stringWidth(s)+5;
                    }
                    max = Math.max(taille, max);
                }
            }
           //max = Math.max(min_column_width, max);
           max = Math.max(MIN_WIDTH_COL, max);

           getColumnModel().getColumn(j).setPreferredWidth(max);
           widthTot += (max);
        }
         int height = (getNbRows())*getRowHeight();
         setSize(widthTot, height);
         setPreferredSize(getSize());
         repaint();

    }

    
    public void setOwnerWidth(int width){
        this.ownerWidth = width;
        resizeColumn();
    }

    /* sort execution*/
    public void executeSort(ElementToSort keySort1, ElementToSort keySort2, ElementToSort keySort3){
        try{
            //get the array to sort (keys concatenation)
            Vector tabToSort = getElementsToSort(dataset, keySort1, keySort2, keySort3);
            if (tabToSort == null)
                return;
            //quicksort on the array
            Vector exchange = getExchange(dataset);
            executeQuickSort(0,tabToSort.size() - 1 ,exchange, tabToSort);
            owner.updateDatasetRow(dataset, exchange, true);
        }catch(Throwable t){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT")+" "+t, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* returns the vector with index of rows  */
    private Vector getExchange(Dataset ds){
        Vector exchange = new Vector();
        for (int i=0; i<ds.getNbRows(); i++){
            exchange.add(i);
        }
        return exchange;
    }

    /**
     * Depending of the colmumn (param), returns the index if this column in the table
     * @return int no of the column
     * @param nom java.lang.String
     */
    private int getIdColWithNameLike(Dataset ds, String name){
        for (int i=0; i<ds.getListDataHeader().length; i++){
            if (ds.getListDataHeader()[i] != null && ds.getListDataHeader()[i].getValue().equals(name)){
                return i;
            }
        }
        ArrayList<DataOperation> listOp = ds.getListOperationOnRows();
        int nb = listOp.size();
        for (int i=0; i<nb; i++){
            if (listOp.get(i).getName().equals(name))
                return i+ds.getNbCol();
        }
        return -1;
    }

    /**
     * get the column to sort -vector O
     * @return Vector
     * @param col int
     */
    private Vector getElementsToSort(Dataset ds, ElementToSort keySort1, ElementToSort keySort2, ElementToSort keySort3){
        //name of the first key to sort
        String name = keySort1.getColumnName().trim() ;
        int col = getIdColWithNameLike(ds, name);
        if (col == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT_KEY")+" "+name,false),owner.getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }
        // vector which contains the keys: to sort
        Vector elementsToSort = new Vector() ;
        // build the first sort key
        elementsToSort = buildKeyForSort(ds, elementsToSort,col,keySort1.getOrder());
        if (elementsToSort == null){
            return null;
        }
        // if 2nd key:
        if (keySort2!=null){
            name = keySort2.getColumnName().trim() ;
            col = getIdColWithNameLike(ds, name);
            if (col == -1){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT_KEY")+" "+name,false),owner.getBundleString("TITLE_DIALOG_ERROR"));
                return null;
            }
            // cbuild the 2nd key
            elementsToSort = buildKeyForSort(ds, elementsToSort,col,keySort2.getOrder());
            if (elementsToSort == null){
                return null;
            }
            // if 3d key:
            if (keySort3!=null){
                name = keySort3.getColumnName().trim() ;
                col = getIdColWithNameLike(ds, name);
                if (col == -1){
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_SORT_KEY")+" "+name,false),owner.getBundleString("TITLE_DIALOG_ERROR"));
                    return null;
                }
                // build the third key :
                elementsToSort = buildKeyForSort(ds, elementsToSort,col,keySort3.getOrder());
                if (elementsToSort == null){
                    return null;
                }
            }
        }
        return elementsToSort;
    }

    /* build keys for sort*/
    private Vector buildKeyForSort(Dataset ds, Vector elementsToSort, int col, int order) {
        int nb = ds.getNbRows() ;
        int nbCol = ds.getNbCol();
        int maxSize = ds.getValueMaxSizeIn(col);
        int maxSize1 = ds.getValueMaxDoubleSizeIn(col)[0];
        int maxSize2 = ds.getValueMaxDoubleSizeIn(col)[1];
        for(int i=0; i<nb; i++){
            String element="";
            boolean isDouble = true;
            // get object
            if(col<nbCol){
                Data o = ds.getData(i, col) ;
                if (o == null)
                    element = "";
                else{
                    element = o.getValue();
                    isDouble = o.isDoubleValue();
                }
            }else{
                Double o = ds.getListOperationResult(ds.getListOperationOnRows().get(col-nbCol)).get(i);
                if(o == null || o.isNaN())
                    element = "";
                else
                    element = Double.toString(o);
            }
            String element1="";
            boolean isNull = element.equals("");
            // maxSize is the max lenght of objects in the column
//            int maxSize = ds.getValueMaxSizeIn(col);
//            if (maxSize == -1){
//                owner.displayError(new CopexReturn ("Erreur fatale : probleme avec la methode getValueMaxSizeIn() !",false),"Construction de la cle de tri");
//                return null;
//            }
            //System.out.println("***ELEMENT : "+element+" ****");
            
            boolean invers = order==0;
            if(isDouble){
                
                // if the string is shorter, complete with blank
           
                boolean neg = false;
                if(element.startsWith("-")){
                    neg = true;
                    element = element.substring(1);
                }
                int lg1 = element.indexOf(".");
                if(lg1 == -1)
                    lg1 = element.length();
                int lg2 = element.length() - lg1 - 1;
                if (lg1 < maxSize1 ){
                    for(int j=lg1; j < maxSize1; j++)
                        element ="0"+element;
                }
                if (lg2 < maxSize2 ){
                    for(int j=lg2; j < maxSize2; j++)
                        element=element+"0";
                }
                if(neg){
                    if(order==1)
                        element="0"+element;
                    else
                        element = "999"+element;
                }else{
                    if (order==1)
                        element = "999"+element;
                    else
                        element = "0"+element;
                }
                invers = (order==0 && !neg) || (order==1 && neg) ;
            }
            
//             descending sort
//             reversed
//             char is between 0 and 255
//            if (order == 0){
            if(invers){
//                for(int k=0;k<maxSize; k++){
                  for(int k=0; k<element.length(); k++){
                    int c = 255 - element.charAt(k);
                    element1+=c;
                  }
            }
            if(isNull && order==1){
                for (int o=0; o<maxSize; o++){
                    element1 += "Z";
                }
            }
            if(isNull && order==0){
                element1 = "";
                for (int o=0; o<maxSize; o++){
                    element1 += "9";
                }
            }
            // ascending sort
            if (element1.equals(""))
                element1=element;
            if (elementsToSort.size() == nb){
                String chaine = (String)(elementsToSort.elementAt(i));
                String el  = chaine.concat(element1);
                elementsToSort.removeElementAt(i);
                elementsToSort.insertElementAt(el,i);
                //System.out.println("=>el : "+el);
            }
            else{
                elementsToSort.addElement(element1);
            //System.out.println("=>element1 : "+element1);
            }
        }
        
        return elementsToSort;
    }

    private void executeQuickSort(int g, int d,Vector exchange, Vector keys) {
        int i,j, idT=-1;
        String v = "",t="";
        Double v1;
        while (g < d) {
            v = (String)keys.elementAt(d);
            //v1 = Double.parseDouble(v);
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
                /* exchange */
                t = ((String)keys.elementAt(i));
                keys.setElementAt(keys.elementAt(j),i);
                Object w = exchange.elementAt(j);
                exchange.setElementAt(exchange.elementAt(i),j);
                exchange.setElementAt(w,i);
                keys.setElementAt(t,j);
            }
            /* exchange */
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



    /* cut : => copy then delete */
    public void cut(){
        owner.logCut(dataset);
        copy();
        delete();
    }

    /* paste */
    public void paste(){
        // copy data
        boolean isOk = owner.paste(copyDs, tableModel.getSelectedCell(getSelectedCells()), true);
        if (isOk){
            owner.updateMenuData();
            owner.setModification();
        }
    }

    public DataTableModel getTableModel() {
        return tableModel;
    }

    /* sort on a column asc or desc*/
   public void sort(boolean asc){
       int order = asc ? 1 : 0;
       ArrayList<int[]> listCellSel = getSelectedCells() ;
       ArrayList<Integer> colSel = tableModel.getSelectedRowAndCol(listCellSel)[1];
       int nb = colSel.size();
       if(nb > 0){
           if (dataset.getDataHeader(colSel.get(0)) != null) {
                String column1 = dataset.getDataHeader(colSel.get(0)).getValue() ;
                ElementToSort keySort1 = new ElementToSort(column1, order);
                ElementToSort keySort2 = null;
                ElementToSort keySort3 = null;
                if( nb >1 ){
                    if (dataset.getDataHeader(colSel.get(1)) != null) {
                        String column2 = dataset.getDataHeader(colSel.get(1)).getValue() ;
                        keySort2 = new ElementToSort(column2, order);
                    }
                    if(nb > 2){
                        if (dataset.getDataHeader(colSel.get(2)) != null) {
                            String column3 = dataset.getDataHeader(colSel.get(2)).getValue() ;
                            keySort3 = new ElementToSort(column3,order);
                        }
                    }
                }
                owner.executeSort(keySort1, keySort2, keySort3);
           }
       }else{
           ArrayList<DataOperation> listOp = tableModel.getSelectedOperation(listCellSel);
           if (listOp != null){
               int nbop = listOp.size();
               for (int i=0; i<nbop; i++){
                   if (!listOp.get(i).isOnCol()){
                       String column1 = listOp.get(i).getName() ;
                       ElementToSort keySort1 = new ElementToSort(column1,order);
                       ElementToSort keySort2 = null;
                       ElementToSort keySort3 = null;
                       owner.executeSort(keySort1, keySort2, keySort3);
                       return;
                   }
               }
           }
       }
   }

   /* undo */
    public void undo(){
        try{
            undoManager.undo();
            owner.updateMenuData();
        }catch(CannotUndoException e){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_UNDO"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* redo */
    public void redo(){
        try{
            undoManager.redo();
            owner.updateMenuData();
        }catch(CannotUndoException e){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_UNDO"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

   /* add in the list undo / redo */
   public void addUndo(DataUndoRedo undo){
       this.undoManager.addEdit(undo);
       owner.updateMenuData();
   }

   /* tool tip text for units */
   public String getToolTipTextUnit(){
       return owner.getBundleString("TOOLTIPTEXT_UNIT");
   }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(this.noColToResize != -1){
            double xColEnd = e.getPoint().getX();
            int newWidth =(int)(xColEnd - xColStart+getColumnModel().getColumn(noColToResize).getPreferredWidth());
            if (xColEnd < xColStart){
               newWidth = (int)(getColumnModel().getColumn(noColToResize).getPreferredWidth() - (xColStart - xColEnd));
            }
            newWidth=Math.max(MIN_WIDTH_COL, newWidth);
            xColStart = xColEnd;
            getColumnModel().getColumn(noColToResize).setPreferredWidth(newWidth);
            resizeLastColumn();
            repaint();
            return;
        }
        if ((resizeTableHeight || resizeTableWidth) && startPoint != null){
            //minimum size
            int minWidth = getNbCols()*MIN_WIDTH_COL;
            int minHeight = 2*getRowHeight();
            int maxHeight = getNbRows() * getRowHeight() ;
            Point endPoint = e.getPoint();
            int newWidth = this.getWidth();
            if(resizeTableWidth){
                if (endPoint.getX() > startPoint.getX())
                    newWidth += endPoint.getX() - startPoint.getX();
                else if (endPoint.getX() < startPoint.getX())
                    newWidth -= startPoint.getX() - endPoint.getX();
                newWidth = Math.max(minWidth, newWidth);
            }
            int newHeight = this.getHeight();
            resizeTableHeight = false;
            if (resizeTableHeight){
                if(endPoint.getY() > startPoint.getY())
                    newHeight += endPoint.getY() - startPoint.getY();
                else if (endPoint.getY() < startPoint.getY())
                    newHeight -= startPoint.getY() - endPoint.getY();
                newHeight = ((int)(newHeight / getRowHeight())) * getRowHeight();
                newHeight = Math.max(minHeight , newHeight);
                newHeight = Math.min(newHeight, maxHeight);
            }
            startPoint = endPoint;
            int n = getNbCols()-1;
            // resize of the last colmun
            resizeLastColumn();
            // resize of the first column
            int w0=getColumnWidth(0);
            getColumnModel().getColumn(0).setPreferredWidth(w0);
            // resize columns
            int w = newWidth-w0-MIN_WIDTH_COL;
            float delta=w/(n-1);
            for (int j=1; j<n; j++){
                getColumnModel().getColumn(j).setPreferredWidth((int)delta);
            }
            int wt = 0;
            for (int j=0; j<getNbCols(); j++){
                wt+= getColumnModel().getColumn(j).getPreferredWidth();
            }
            this.setSize(wt, newHeight);
            this.setPreferredSize(getSize());
            repaint();
            return;
        }

    }

    private void resizeLastColumn(){
        getColumnModel().getColumn(getNbCols()-1).setPreferredWidth(MIN_WIDTH_COL);
    }

    private int getCellWidth(int i,int j){
        FontMetrics fm = getFontMetrics(this.dataTableRenderer.getFont(this, i, j));
        int taille = 0;
        if (this.getValueAt(i, 0) != null){
            String s = "";
            if (this.getValueAt(i, j) instanceof String)
                s = (String)this.getValueAt(i, j) ;
            else if (this.getValueAt(i, j) instanceof Double){
                s = Double.toString((Double)this.getValueAt(i, j)) ;
           }else if (this.getValueAt(i, j) instanceof Integer){
                s = Integer.toString((Integer)this.getValueAt(i, j)) ;
           }else if (this.getValueAt(i, j) instanceof String[]){
                s = ((String[])this.getValueAt(i, j))[0]+" ("+((String[])this.getValueAt(i, j))[1]+")";
           }
           taille = fm.stringWidth(s)+5;
        }
        return taille;
    }

    private int getColumnWidth(int j){
        if(j== getNbCols()-1)
            return MIN_WIDTH_COL;
        int max=0;
        for (int i=0; i<getNbRows(); i++){
            max = Math.max(getCellWidth(i, j), max);
        }
        max = Math.max(MIN_WIDTH_COL, max);
        return max;
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        int row = rowAtPoint(p);
        int col = columnAtPoint(p);
        if (row == 0){
            if(columnAtPoint(new Point((int)p.getX()+DELTA_RESIZE_COL, (int)p.getY())) == col+1 || columnAtPoint(new Point((int)p.getX()-DELTA_RESIZE_COL, (int)p.getY())) == col-1)
                setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
            else
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }else{
            
            if (p.getX() > this.getWidth()- DELTA_RESIZE_COL && p.getY() > this.getHeight() - DELTA_RESIZE_COL){
                setCursor((new Cursor(Cursor.NW_RESIZE_CURSOR)));
            }else if (p.getX() > this.getWidth() - DELTA_RESIZE_COL){
                setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
//            }else if (p.getY() > this.getHeight() - DELTA_RESIZE_COL){
//                setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
            }else
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public int[] getBorders(int row, int col){
        return tableModel.getBorders(row, col);
    }

    public void setCellSelected(int rowIndex, int colIndex){
        changeSelection(rowIndex, colIndex, false, false);
        owner.updateMenuData();
    }

    public boolean isAllSelectionIgnore(){
        return this.tableModel.isAllSelectionIgnore(getSelectedCells());
    }


    /* fix the selected cells */
    public void markSelectedCell(){
       listSelectedCell = new LinkedList();
       int nbRows = getNbRows();
       int nbCols = getNbCols();
       for(int i=0; i<nbRows; i++){
           for(int j=0; j<nbCols; j++){
               if(isCellSelected(i, j)){
                   int[] tab = new int[2];
                   tab[0] = i;
                   tab[1] = j;
                   listSelectedCell.add(tab);
               }
           }
       }
    }

    public void selectOldCell(){
        if(listSelectedCell == null)
            return;
        for(Iterator<int[]> t = listSelectedCell.iterator();t.hasNext();){
            int[] tab = t.next();
            //changeSelection(tab[0], tab[1], true, false);
            if(isValueHeader(tab[0], tab[1])){
                selectCols(tab[1]-1, 1);
            }else if(isValueNoRow(tab[0], tab[1])){
                selectRows(tab[0]-1, 1);
            }
        }
        owner.updateMenuData();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C && canCopy()){
            copy();
        }else if (e.getKeyCode() == KeyEvent.VK_X && canCut()){
            cut();
        }else if (e.getKeyCode() == KeyEvent.VK_V && canPaste()){
            paste();
        }else if (e.getKeyCode() == KeyEvent.VK_DELETE && canSuppr()){
            delete();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isEditAfterOneClick(){
        return !doubleClickOnCell;
    }

    public int getDataAlignment(int idCol){
        if(idCol == 0 || idCol > dataset.getNbCol() )
            return DataConstants.DEFAULT_DATASET_ALIGNMENT;
        return dataset.getDataAlignment(idCol-1);
    }
    
}
