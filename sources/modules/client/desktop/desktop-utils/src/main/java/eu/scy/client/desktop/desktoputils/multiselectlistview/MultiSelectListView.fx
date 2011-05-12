/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.desktoputils.multiselectlistview;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import java.lang.String;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author SikkenJ
 */

public class MultiSelectListView extends ListView {

    public-read var selectedItems:Object[];

    public var selectedIndexes:Integer[] on replace {
        selectedItems = for (i in selectedIndexes) items[i];
    }

    override var cellFactory = function():ListCell {
        var label:Label;
        def cell:ListCell = MultiSelectListCell {
            onUpdate: function() {
                def item = cell.item;
                if (item == null) {
                    cell.node = null;
                } else if (item instanceof Node) {
                    cell.node = item as Node;
                } else {
                    if (label == null) {
                        label = Label { }
                    }
                    label.text = if (item instanceof String) then item as String else "{item}";
                    if (cell.node != label) cell.node = label;
                }
            }
        }
    }
}

function run(){
   Stage {
	title: "test MultiSelectListView"
	onClose: function () {  }
	scene: Scene {
		width: 200
		height: 200
		content: [
         MultiSelectListView{
            items:["one", "two", "three"]
         }

      ]
	}
}


}

