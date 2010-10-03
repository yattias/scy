/*
 * DragAndDropManager.fx
 *
 * Created on 8-jan-2010, 10:40:17
 */

package eu.scy.client.desktop.scydesktop.draganddrop;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * This is the interface describing the drag and drop manager.
 *
 * @author sikken
 */

public mixin class DragAndDropManager {

   /**
   * starts a drag and drop action.
   *
   * It is assumed that the drag and drop action is initiated from a onMousePressed handler. You create a new JavaFX node, which will be dragged around.
   *
   * @param node - the JavaFX node which will be dragged around
   * @param object - the object to be dropped some where
   * @param source - the JavaFX source node where the drag starts
   * @param e - the mouseEvent which initiated the drag and drop action
   */
   public abstract function startDrag(node:Node, object:Object, source:Node, e:MouseEvent):Void;

   /**
   * register a node as drop target. The node must implement the DropTarget interface.
   *
   * @param node - the drop target
   */
   public abstract function addDropTaget(node:Node):Void;
}
