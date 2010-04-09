/*
 * ModalDialogNode.fx
 *
 * Created on 28-feb-2010, 14:08:37
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import javafx.scene.Node;

/**
 * @author sikken
 */

public mixin class ModalDialogNode {

   public var modalDialogBox: ModalDialogBox;

   public abstract function getContentNodes():Node[];

}
