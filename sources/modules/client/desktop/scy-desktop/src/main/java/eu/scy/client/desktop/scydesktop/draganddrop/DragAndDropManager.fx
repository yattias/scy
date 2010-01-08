/*
 * DragAndDropManager.fx
 *
 * Created on 8-jan-2010, 10:40:17
 */

package eu.scy.client.desktop.scydesktop.draganddrop;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * @author sikken
 */

public mixin class DragAndDropManager {

   public abstract function startDrag(node:Node, object:Object, source:Node, e:MouseEvent):Void;
}
