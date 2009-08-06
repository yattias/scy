/*
 * TableComponent.fx
 *
 * Created on Jul 31, 2009, 3:23:44 PM
 */

package chatter;

/**
 * @author jeremyt
 */

// fileName filenameX.fx
// package  packageX

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javafx.ext.swing.SwingComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseListener;

//class TableColumn{
package class TableColumn{
    public var text: String;
}

//class TableCell{
package class TableCell{
    public var text: String;
}
//class TableRow{
package class TableRow{
    public var cells: TableCell[];
}
//class SwingTable extends SwingComponent{
package class TableComponent extends SwingComponent{

    public var table: JTable;
    var model: DefaultTableModel;

    public var selection: Integer;


    public var columns: TableColumn[] on replace{
        model = new DefaultTableModel(for(column in columns) column.text, 0);
        table.setModel(model);
    };

    public var rows: TableRow[] on replace oldValue[lo..hi] = newVals{
        for(index in [hi..lo step -1]){
            model.removeRow(index);
        }

        for(row in newVals){
            model.addRow(for(cell in row.cells) cell.text);
        }
    };



    public override function createJComponent(){
        table = new JTable();
        model =
        table.getModel() as DefaultTableModel;

        return new JScrollPane(table);
    }



}
