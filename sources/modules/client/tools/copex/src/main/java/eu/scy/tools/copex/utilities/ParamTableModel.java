package eu.scy.tools.copex.utilities;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;
import org.omg.CORBA.OBJECT_NOT_EXIST;

/**
 *
 * @author Marjolaine
 */
public class ParamTableModel extends AbstractTableModel {

    /*data*/
    private Object[] datas;

    public ParamTableModel(Object[] datas) {
        super();
        this.datas = datas;
    }


    @Override
    public int getRowCount() {
       return 1;
    }

    @Override
    public int getColumnCount() {
        return datas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(datas[columnIndex] instanceof String)
            return (String)datas[columnIndex];
        else if (datas[columnIndex] instanceof JComboBox){
            // cb
            ((JComboBox)datas[columnIndex]).getSelectedItem() ;
        }
        return datas[columnIndex];
    }

    public void addColumns(int nb){
        Object[] d = new Object[this.datas.length+nb];
        for (int i=0; i<this.datas.length; i++){
            d[i] = this.datas[i];
        }
        this.datas = d;
        this.fireTableStructureChanged();
    }

    public void removeColumns(int nb){
        int nbC = this.datas.length-nb ;
        Object[] d = new Object[nbC];
        for (int i=0; i<nbC; i++){
            d[i] = this.datas[i];
        }
        this.datas = d;
        this.fireTableStructureChanged();
    }
}
