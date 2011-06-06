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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Math;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.metalthemes.CustomMetalThemeFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * @author SikkenJ
 */

public class ScySwingWrapper extends CustomNode, Resizable {
   var component: JComponent;
   var swingComponent: javafx.ext.swing.SwingComponent;
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};
   public-init var useJfx12Mode = false;
   public var windowColorScheme: WindowColorScheme on replace { setSwingCustomMetalTheme() };

   var initialClickCatched = false;
   var contentGroup:Group;

   /**
   * workaround for the npe problems with swing content
   * first mouse click on swing content is used to "fix" problem
   * this means that the first click on the swing content is lossed
   * there is also compatible fix in WindowContent, but that is currently not used
   */
   var contentGlassPane:Rectangle = Rectangle {
      blocksMouse:true;
      x: 0, y: 0
      width: 140, height: 90
      fill: Color.TRANSPARENT
//      fill: Color.rgb(92,92,255,0.15)
      onMouseEntered: function( e: MouseEvent ):Void {
         // deleting contentGlassPane from the scene graph fixes the problem with swing content in the scene graph
         delete contentGlassPane from contentGroup.content;
         contentGlassPane = null;
         println("ScySwingWrapper contentGlassPane removed by click");
//         contentGroup.onMousePressed(MouseEvent{
//               node:contentGroup
//            });
      }
   }

   public override function create(): Node {
      swingComponent = javafx.ext.swing.SwingComponent.wrap(component);
      resizeContent();
      FX.deferAction(resizeContent);
//      Timeline {
//         repeatCount: 1
//         keyFrames: [
//            KeyFrame{
//               time:1000ms
//               action:function():Void{
//                  delete contentGlassPane from contentGroup.content;
//                  contentGlassPane = null;
//                  println("ScySwingWrapper contentGlassPane removed automaticly");
//               }
//
//            }
//
//         ];
//      }.play();

      contentGroup = Group{
         autoSizeChildren:not useJfx12Mode
         content:[
            swingComponent,
//            contentGlassPane
         ]
      }
//      if (useJfx12Mode){
//         Group{
//            autoSizeChildren:false
//            content:swingComponent
//         }
//       }
//      else{
//         swingComponent
//      }
   }

   function setSwingCustomMetalTheme(): Void {
      if (windowColorScheme!=null){
         def customMetalTheme = CustomMetalThemeFactory.createCustumMetalTheme(windowColorScheme.getColorScheme());
         MetalLookAndFeel.setCurrentTheme(customMetalTheme);
         UIManager.setLookAndFeel(new MetalLookAndFeel());
         SwingUtilities.updateComponentTreeUI(component);
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
      contentGlassPane.width = size.width;
      contentGlassPane.height = size.height;
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
//      printSizes();
      var preferredHeight = Container.getNodePrefHeight(swingComponent,number);
      Math.max(0,preferredHeight);
   }

   public override function getPrefWidth(number: Number): Number {
      var preferredWidth = Container.getNodePrefWidth(swingComponent,number);
      Math.max(0, preferredWidth);
   }

   function printSizes(){
      println("ScySwingWrapper sizes with JComponent class: {component.getClass()}");
      println("width: {width}, limits: {Container.getNodeMinWidth(swingComponent)} <= {Container.getNodePrefWidth(swingComponent)} <= {Container.getNodeMaxWidth(swingComponent)}");
      println("height: {height}, limits: {Container.getNodeMinHeight(swingComponent)} <= {Container.getNodePrefHeight(swingComponent)} <= {Container.getNodeMaxHeight(swingComponent)}");
      var swingMinimumSize = component.getMinimumSize();
      var swingPreferredSize = component.getPreferredSize();
      var swingMaximumSize = component.getMaximumSize();
      println("width: {component.getWidth()}, limits: {swingMinimumSize.width} <= {swingPreferredSize.width} <= {swingMaximumSize.width}");
      println("height: {component.getHeight()}, limits: {swingMinimumSize.height} <= {swingPreferredSize.height} <= {swingMaximumSize.height}");
   }

}

public function wrap(component: JComponent, useJfx12Mode:Boolean): CustomNode {
   var preferredSize = component.getPreferredSize();
   var scySwingNode = ScySwingWrapper {
         component: component
         width:preferredSize.width
         height:preferredSize.height
         useJfx12Mode:useJfx12Mode
      };

   scySwingNode
}

public function wrap(component: JComponent): CustomNode {
   wrap(component,false);
}
