/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;


/**
 * popup menu on the right click
 * @author Marjolaine Bodin
 */
public class DataMenu extends JPopupMenu implements ActionListener{
    /* owner */
    private FitexToolPanel owner ;
    /* table */
    private DataTable table;
    
    // item
    private JMenuItem itemInsert;
    private JMenuItem itemDelete;
    private JMenuItem itemCopy;
    private JMenuItem itemPaste;
    private JMenuItem itemCut;
    private JMenu menuSort;
    private JMenuItem itemAsc;
    private JMenuItem itemDes;
    private JMenu menuOperations;
    private JMenuItem itemSum;
    private JMenuItem itemAvg;
    private JMenuItem itemMin;
    private JMenuItem itemMax;
    private JMenuItem itemSumIf;
    private JMenuItem itemIgnored;
    private JMenuItem itemNotIgnored;

    // background color
    private Color bgColorSum;
    private Color bgColorAvg;
    private Color bgColorMin;
    private Color bgColorMax;
    private Color bgColorSumIf;

    public DataMenu(FitexToolPanel owner, DataTable table) {
        super();
        this.owner = owner;
        this.table = table;
        this.bgColorSum = owner.getOperationColor(DataConstants.OP_SUM);
        this.bgColorAvg = owner.getOperationColor(DataConstants.OP_AVERAGE);
        this.bgColorMin = owner.getOperationColor(DataConstants.OP_MIN);
        this.bgColorMax = owner.getOperationColor(DataConstants.OP_MAX);
        this.bgColorSumIf = owner.getOperationColor(DataConstants.OP_SUM_IF);
        init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.itemIgnored))
            ignoreData();
        else if (e.getSource().equals(this.itemNotIgnored))
            restoreIgnoreData();
        else if (e.getSource().equals(this.itemSum))
            sum();
        else if (e.getSource().equals(this.itemAvg))
            avg();
        else if (e.getSource().equals(this.itemMin))
            min();
        else if (e.getSource().equals(this.itemMax))
            max();
        else if (e.getSource().equals(this.itemSumIf))
            sumIf();
        else if (e.getSource().equals(this.itemInsert))
            insert();
        else if (e.getSource().equals(this.itemDelete))
            delete();
        else if (e.getSource().equals(this.itemCopy))
            copy();
        else if (e.getSource().equals(this.itemCut))
            cut();
        else if (e.getSource().equals(this.itemPaste))
            paste();
        else if (e.getSource().equals(this.itemAsc))
            sort(true);
        else if (e.getSource().equals(this.itemDes))
            sort(false);
    }
    
    private void init(){
        this.itemInsert = new JMenuItem(owner.getBundleString("MENU_INSERT"));
        this.itemInsert.addActionListener(this);
        this.itemDelete = new JMenuItem(owner.getBundleString("MENU_DELETE"));
        this.itemDelete.addActionListener(this);
        this.itemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        this.itemCopy = new JMenuItem(owner.getBundleString("MENU_COPY"));
        this.itemCopy.addActionListener(this);
        this.itemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        this.itemPaste = new JMenuItem(owner.getBundleString("MENU_PASTE"));
        this.itemPaste.addActionListener(this);
        this.itemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        this.itemCut = new JMenuItem(owner.getBundleString("MENU_CUT"));
        this.itemCut.addActionListener(this);
        this.itemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        this.itemIgnored = new JMenuItem(owner.getBundleString("MENU_IGNORED"));
        this.itemIgnored.addActionListener(this);
        this.itemNotIgnored = new JMenuItem(owner.getBundleString("MENU_NOT_IGNORED"));
        this.itemNotIgnored.addActionListener(this);
        this.itemSum = new JMenuItem(owner.getBundleString("MENU_SUM"));
        if(bgColorSum != null){
            itemSum.setOpaque(true);
            itemSum.setBackground(bgColorSum);
        }
        this.itemSum.addActionListener(this);
        this.itemAvg = new JMenuItem(owner.getBundleString("MENU_AVERAGE"));
        if(bgColorAvg != null){
            itemAvg.setOpaque(true);
            itemAvg.setBackground(bgColorAvg);
        }
        this.itemAvg.addActionListener(this);
        this.itemMin = new JMenuItem(owner.getBundleString("MENU_MIN"));
        if(bgColorMin != null){
            itemMin.setOpaque(true);
            itemMin.setBackground(bgColorMin);
        }
        this.itemMin.addActionListener(this);
        this.itemMax = new JMenuItem(owner.getBundleString("MENU_MAX"));
        if(bgColorMax != null){
            itemMax.setOpaque(true);
            itemMax.setBackground(bgColorMax);
        }
        this.itemMax.addActionListener(this);
        this.itemSumIf = new JMenuItem(owner.getBundleString("MENU_SUM_IF"));
        if(bgColorSumIf != null){
            itemSumIf.setOpaque(true);
            itemSumIf.setBackground(bgColorSumIf);
        }
        this.itemSumIf.addActionListener(this);
        this.menuOperations = new JMenu(owner.getBundleString("MENU_OPERATION"));
        this.menuOperations.add(this.itemSum);
        this.menuOperations.add(this.itemAvg);
        this.menuOperations.add(this.itemMin);
        this.menuOperations.add(this.itemMax);
        //this.menuOperations.add(this.itemSumIf);
        this.itemAsc = new JMenuItem(owner.getBundleString("MENU_SORT_ASC"));
        this.itemAsc.addActionListener(this);
        this.itemDes = new JMenuItem(owner.getBundleString("MENU_SORT_DES"));
        this.itemDes.addActionListener(this);
        this.menuSort = new JMenu(owner.getBundleString("MENU_SORT"));
        this.menuSort.add(this.itemAsc);
        this.menuSort.add(this.itemDes);
        // menu order
        this.add(itemInsert);
        this.add(itemDelete);
        this.addSeparator();
        this.add(itemCopy);
        this.add(itemPaste);
        this.add(itemCut);
        this.addSeparator();
        this.add(itemIgnored);
        this.add(itemNotIgnored);
        this.addSeparator();
        this.add(menuOperations);
        this.addSeparator();
        this.add(menuSort);
    }

    /* click on ignore data*/
    private void ignoreData(){
       table.ignoreData();
    }
    /*click on no-ignore data */
    private void restoreIgnoreData(){
       table.restoreIgnoreData();
    }

    /* sum */
    public void sum(){
        table.sum();
    }

    /* sum if */
    public void sumIf(){
        table.sumIf();
    }

    /* avg */
    public void avg(){
        table.avg();
    }

    /* min */
    public void min(){
        table.min();
    }

    /* max */
    public void max(){
        table.max();
    }

    /* insert */
    public void insert(){
        table.insert();
    }

    /*delete */
    public void delete(){
        table.delete();
    }

    /* copy */
    public void copy(){
        table.copy();
    }

    /* cut */
    private void cut(){
        table.cut();
    }
    /* paste */
    private void paste(){
        table.paste();
    }

    /* sort ascending, descending  */
    private void sort(boolean asc){
        table.sort(asc);
    }

    /* set the item ignored enabled */
    public void setEnabledItemIgnored(boolean enabled){
        this.itemIgnored.setEnabled(enabled);
    }
    /* set the item no ignored enabled*/
    public void setEnabledItemNotIgnored(boolean enabled){
        this.itemNotIgnored.setEnabled(enabled);
    }

    /* set the item insert enabled */
    public void setEnabledItemInsert(boolean enabled){
        this.itemInsert.setEnabled(enabled);
    }
     /* set the item delete enabled */
    public void setEnabledItemDelete(boolean enabled){
        this.itemDelete.setEnabled(enabled);
    }

    /* set the item operations enabled */
    public void setEnabledItemOperation(boolean enabled){
        this.itemSum.setEnabled(enabled);
        this.itemAvg.setEnabled(enabled);
        this.itemMin.setEnabled(enabled);
        this.itemMax.setEnabled(enabled);
        this.itemSumIf.setEnabled(enabled);

        this.menuOperations.setEnabled(enabled);
    }

    /* set the item sort enabled */
    public void setEnabledItemSort(boolean enabled){
        this.itemAsc.setEnabled(enabled);
        this.itemDes.setEnabled(enabled);
        this.menuSort.setEnabled(enabled);
    }

    /* set the item copy enabled */
    public void setEnabledItemCopy(boolean enabled){
        this.itemCopy.setEnabled(enabled);
    }

    /* set the item paste enabled  */
    public void setEnabledItemPaste(boolean enabled){
        this.itemPaste.setEnabled(enabled);
    }
    
    /* set the item cut enabled */
    public void setEnabledItemCut(boolean enabled){
        this.itemCut.setEnabled(enabled);
    }
}
