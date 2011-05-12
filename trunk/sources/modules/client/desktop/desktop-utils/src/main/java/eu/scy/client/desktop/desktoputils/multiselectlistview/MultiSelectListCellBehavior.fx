/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.desktoputils.multiselectlistview;
import javafx.util.Sequences;
import com.sun.javafx.scene.control.behavior.ListCellBehavior;

/**
 * @author SikkenJ
 */

public class MultiSelectListCellBehavior extends ListCellBehavior {

    override function mouseReleased(e) {
        // Note that list.select will reset selection
        // for out of bounds indexes. So, need to check
        def listCell = skin.control as MultiSelectListCell;
        def listView = listCell.listView as MultiSelectListView;

        // If the mouse event is not contained within this ListCell, then
        // we don't want to react to it.
        if (listCell.contains(e.x, e.y)) {
            if (listCell.index >= sizeof listView.items) return;

            var row = listCell.index;

            if (e.controlDown) {
                if (Sequences.indexOf(listView.selectedIndexes, row) == -1) {
                    insert row into listView.selectedIndexes;
                } else {
                    delete row from listView.selectedIndexes;
                }
            } else if (e.shiftDown) {
                var start = listView.focusedIndex;
                var end = row;
                var range = if (start < end)
                    then [start..end] else [end..start];

                listView.selectedIndexes = range;
            } else {
                delete listView.selectedIndexes;
                insert row into listView.selectedIndexes;
                listView.focus(row);
            }
        }
    }
}
