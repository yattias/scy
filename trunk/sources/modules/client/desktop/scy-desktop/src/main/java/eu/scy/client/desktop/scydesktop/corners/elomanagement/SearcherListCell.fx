/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement;
import javafx.scene.control.ListCell;
import javafx.scene.control.CheckBox;

/**
 * @author svenmaster
 */

public class SearcherListCell extends ListCell{

    public var onSelectionChanged:function():Void;

    public def eloSearcher: EloBasedSearcher = bind(item as EloBasedSearcher);

    def selectionChanged = bind checkBox.selected on replace{
        println("selection changed event");
        onSelectionChanged();
    };


    public def checkBox:CheckBox = node as CheckBox;

    public function isBoxSelected():Boolean{
        return checkBox.selected;
    }

    override public var node = CheckBox {
        text: bind "{eloSearcher.getDisplayId()}";
        }

}
