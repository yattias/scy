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
import org.apache.log4j.Logger;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.javafx.LogoEloIcon;

/**
 * @author sikken
 */
public class Background extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public var elements: BackgroundElement[];
   public var specification: BackgroundSpecification on replace {
              newSpecification()
           };
   public var width = 2048;
   public var height = 1536;
   public var tileWidth = 1024;
   public var tileHeight = 768;
   public var iconOpacity = 0.1;
   public var iconEffect: Effect;
   def logoWindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def defaultEloIcon = LogoEloIcon {
              windowColorScheme: logoWindowColorScheme
           };
   def backgroundRect = Rectangle {
              x: 0, y: 0
              width: width, height: height
              fill: Color.WHITE
           }
   def iconsGroup = Group {
           }

   init {
   }

   public override function create(): Node {
      newSpecification();
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

      if (specification == null) {
         return;
      }
      var useEloIcon = specification.eloIcon;
      if (useEloIcon==null){
         useEloIcon = defaultEloIcon;
      }
//      backgroundRect.fill = specification.backgroundColor;
      backgroundRect.fill = useEloIcon.windowColorScheme.backgroundColor;

      for (element in elements) {
         var icon = useEloIcon.clone();
         applyElementSpecifation(icon, element, 0, 0);
         insert icon into iconsGroup.content;
         icon = useEloIcon.clone();
         applyElementSpecifation(icon, element, tileWidth, 0);
         insert icon into iconsGroup.content;
         icon = useEloIcon.clone();
         applyElementSpecifation(icon, element, 0, tileHeight);
         insert icon into iconsGroup.content;
         icon = useEloIcon.clone();
         applyElementSpecifation(icon, element, tileWidth, tileHeight);
         insert icon into iconsGroup.content;
      }
   }

   function applyElementSpecifation(node: Node, element: BackgroundElement, addX: Number, addY: Number) {
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
