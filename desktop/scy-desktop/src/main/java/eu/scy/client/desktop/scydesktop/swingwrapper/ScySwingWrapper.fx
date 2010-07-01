/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.swingwrapper;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Container;
import javafx.scene.layout.Resizable;
import java.awt.Dimension;
import javax.swing.JComponent;

/**
 * @author SikkenJ
 */

public class ScySwingWrapper extends CustomNode, Resizable {
   var component: JComponent;
   var swingComponent: javafx.ext.swing.SwingComponent;
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};
   public-init var useJfx12Mode = false;

   public override function create(): Node {
      swingComponent = javafx.ext.swing.SwingComponent.wrap(component);
      resizeContent();
      FX.deferAction(resizeContent);
      if (useJfx12Mode){
         Group{
            autoSizeChildren:false
            content:swingComponent
         }
       }
      else{
         swingComponent
      }
   }

   function resizeContent(): Void {
//      if (width == 0 or height == 0) {
//         return;
//      }

      var size = new Dimension(width, height);
      Container.resizeNode(swingComponent, size.width, size.height);
      component.setPreferredSize(size);
      component.setSize(size);
   }

   public override function getMinHeight(): Number {
      Container.getNodeMinHeight(swingComponent);
   }

   public override function getMaxHeight(): Number {
      Container.getNodeMaxHeight(swingComponent);
   }

   public override function getMinWidth(): Number {
      Container.getNodeMinWidth(swingComponent);
   }

   public override function getMaxWidth(): Number {
      Container.getNodeMaxWidth(swingComponent);
   }

   public override function getPrefHeight(number: Number): Number {
      Container.getNodePrefHeight(swingComponent,number);
   }

   public override function getPrefWidth(number: Number): Number {
      Container.getNodePrefWidth(swingComponent,number);
   }

}

public function wrap(component: JComponent, useJfx12Mode:Boolean): CustomNode {
   var scySwingNode = ScySwingWrapper {
         component: component
         useJfx12Mode:useJfx12Mode
      };

   scySwingNode
}

public function wrap(component: JComponent): CustomNode {
   wrap(component,false);
}
