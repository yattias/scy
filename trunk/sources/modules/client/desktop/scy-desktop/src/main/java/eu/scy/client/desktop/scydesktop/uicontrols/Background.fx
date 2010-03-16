/*
 * Background.fx
 *
 * Created on 12-mrt-2010, 11:50:58
 */
package eu.scy.client.desktop.scydesktop.uicontrols;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.effect.Effect;
import eu.scy.client.desktop.scydesktop.uicontrols.BackgroundSpecification;
import eu.scy.client.desktop.scydesktop.uicontrols.BackgroundElement;
import eu.scy.client.desktop.scydesktop.art.FxdImageLoader;
import eu.scy.client.desktop.scydesktop.art.ArtSource;
import org.apache.log4j.Logger;

/**
 * @author sikken
 */
public class Background extends CustomNode {
   def logger = Logger.getLogger(this.getClass());

   public-init var sourceName = ArtSource.selectedIconsPackage;
   public-init var fxdImageLoader: FxdImageLoader;
   public var elements:BackgroundElement[];
   public var specification: BackgroundSpecification on replace {
         newSpecification()
      };
   public var width = 2048;
   public var height = 1536;
   public var tileWidth = 1024;
   public var tileHeight = 768;
   public var iconOpacity = 0.1;
   public var iconEffect:Effect;

   def backgroundRect = Rectangle {
         x: 0, y: 0
         width: width, height: height
         fill: Color.WHITE
      }
   def iconsGroup = Group {
      }

   init{
   }


   public override function create(): Node {
      Group {
         content: [
            backgroundRect,
            iconsGroup
         ]
      }
   }

   function newSpecification(): Void {
//      println("newSpecification");
      delete  iconsGroup.content;

      if (fxdImageLoader==null){
         fxdImageLoader = FxdImageLoader {
            sourceName: sourceName;
            backgroundLoading: false;
            loadedAction:newSpecification
         }
      }

      if (specification == null or not fxdImageLoader.loaded) {
         return ;
      }
      backgroundRect.fill = specification.backgroundColor;
      var testNode = fxdImageLoader.getGroup(specification.iconName);
      if (testNode == null) {
         logger.error("cannot find node {specification.iconName} in {fxdImageLoader.sourceUrl}");
         return;
      }
//      println("found group id {specification.iconName}, size {testNode.layoutBounds}, {testNode.visible}");
//      println("nr of elements {sizeof elements}");
      for (element in elements) {
         var icon = fxdImageLoader.getNode(specification.iconName);
         applyElementSpecifation(icon,element,0,0);
         insert icon into iconsGroup.content;
         icon = fxdImageLoader.getNode(specification.iconName);
         applyElementSpecifation(icon,element,tileWidth,0);
         insert icon into iconsGroup.content;
         icon = fxdImageLoader.getNode(specification.iconName);
         applyElementSpecifation(icon,element,0,tileHeight);
         insert icon into iconsGroup.content;
         icon = fxdImageLoader.getNode(specification.iconName);
         applyElementSpecifation(icon,element,tileWidth,tileHeight);
         insert icon into iconsGroup.content;
      }
   }

   function applyElementSpecifation(node : Node, element:BackgroundElement,addX: Number,addY: Number ){
      node.layoutX = element.xPos + addX;
      node.layoutY = element.yPos + addY;
      node.scaleX = element.scale;
      node.scaleY = element.scale;
      node.translateX = 0;
      node.translateY = 0;
      node.visible = true;
      node.opacity = iconOpacity;
      node.effect = iconEffect;
   }


}
