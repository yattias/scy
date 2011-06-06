/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.desktoputils.multiselectlistview;
import com.sun.javafx.scene.control.skin.ListCellSkin;

/**
 * @author SikkenJ
 */

public class MultiSelectListCellSkin extends ListCellSkin {
    override var behavior = MultiSelectListCellBehavior { }

    postinit {
        // This fixes an issue where the mouseReleased function is called twice.
        overlay.onMouseReleased = null;
    }
}
