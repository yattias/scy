/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.desktoputils.multiselectlistview;
import javafx.scene.control.ListCell;
import javafx.scene.control.Skin;
import javafx.util.Sequences;
import com.sun.javafx.scene.control.skin.SkinAdapter;

/**
 * @author SikkenJ
 */

public class MultiSelectListCell extends ListCell {

    def multiSelected = bind Sequences.indexOf((listView as MultiSelectListView).selectedIndexes, index) != -1 on invalidate {
        impl_pseudoClassStateChanged("selected");
    }

    package override function createDefaultSkin():Skin {
        SkinAdapter {
            rootRegion: MultiSelectListCellSkin { }
        }
    }

    override function impl_getPseudoClassState():String[] {
        [
            if (multiSelected) "selected" else null,
            super.impl_getPseudoClassState()
        ]
    }
}
