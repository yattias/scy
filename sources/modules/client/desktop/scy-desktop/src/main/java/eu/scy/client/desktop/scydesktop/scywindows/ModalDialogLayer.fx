/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author SikkenJ
 */
def modalDialogLayer = ModalDialogLayer {};
public def layer: Node = modalDialogLayer;

public function addModalDialog(node: Node, center: Boolean): Void {
   modalDialogLayer.addModalDialogNode(node, center);
}

public function addModalDialog(node: Node): Void {
   modalDialogLayer.addModalDialogNode(node, false);
}

public function removeModalDialog(node: Node): Void {
   modalDialogLayer.removeModalDialogNode(node);
}

public class ModalDialogLayer extends CustomNode {

   def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
   def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
   def modalDialogGroup = Group {
         visible: false
      }
   def backgroundBlocker = Rectangle {
         blocksMouse: true
         x: 0, y: 0
         width: 100, height: 100
         fill: Color.color(1.0, 1.0, 1.0, 0.5)
         onKeyPressed: function(e: KeyEvent): Void {
         }
         onKeyReleased: function(e: KeyEvent): Void {
         }
         onKeyTyped: function(e: KeyEvent): Void {
         }
      }
   var centeredNodes: Node[];

   function sceneSizeChanged() {
      if (backgroundBlocker.visible) {
         backgroundBlocker.width = scene.width;
         backgroundBlocker.height = scene.height;
         for (node in centeredNodes) {
            node.layoutX = -node.layoutBounds.minX + scene.width / 2 - node.layoutBounds.width / 2;
            node.layoutY = -node.layoutBounds.minY + scene.height / 2 - node.layoutBounds.height / 2;
         }
      }
   }

   public override function create(): Node {
      modalDialogGroup.content = backgroundBlocker;
      modalDialogGroup
   }

   function addModalDialogNode(node: Node, center: Boolean): Void {
      if (center) {
         insert node into centeredNodes;
         sceneSizeChanged();
      }
      node.visible = true;
      insert node into modalDialogGroup.content;
      modalDialogGroup.visible = true;
   }

   function removeModalDialogNode(node: Node): Void {
      delete node from modalDialogGroup.content;
      delete node from centeredNodes;
      node.visible = false;
      modalDialogGroup.visible = sizeof modalDialogGroup.content > 1;
   }

}
