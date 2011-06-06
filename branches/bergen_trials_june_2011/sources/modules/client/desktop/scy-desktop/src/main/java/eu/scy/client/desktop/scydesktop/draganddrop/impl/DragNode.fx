/*
 * DragNode.fx
 *
 * Created on 8-jan-2010, 10:45:01
 */
package eu.scy.client.desktop.scydesktop.draganddrop.impl;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;

/**
 * @author sikken
 */
public class DragNode extends CustomNode {

   public-init var node: Node;
   public-init var object:Object;

   def dragOpacity = 0.5;
   def dropOpacity = 0.75;

   public override function create(): Node {
      return Group {
                 opacity:dragOpacity
                 content: [
                    node
                 ]
                 onMousePressed: function (e: MouseEvent): Void {
                    println("dragNode.onMousePressed");
                 }
                 onMouseDragged: function (e: MouseEvent): Void {
                    println("dragNode.onMouseDragged");
                 }
                 onMouseReleased: function (e: MouseEvent): Void {
                    println("dragNode.onMouseReleased");
                    //MouseBlocker.stopMouseBlocking();
                 }
              };
   }



}
