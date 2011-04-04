/*
 * DatasyncAttribute.fx
 *
 * Created on 09.02.2010, 11:29:49
 */
package eu.scy.client.desktop.scydesktop.scywindows;

/**
 * @author lars
 */
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import java.lang.Object;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;

/**
 * @author lars
 */
public class DatasyncAttribute extends ScyWindowAttribute {

   public-init var dragAndDropManager: DragAndDropManager;
   public-init var dragObject: Object;
   public-init var tooltipManager: TooltipManager;
   public-read def cableStartX = 3;
   public-read def cableStartY = -12;
   def logger = Logger.getLogger(this.getClass());
   def size = 16.0;
//    def strokeWidth = 2;
//    def defaultTitleColor = Color.WHITE;
//    def defaultContentColor = Color.GRAY;
   def dragScale = 2.0;
//    var titleColor = defaultTitleColor;
//    var contentColor = defaultContentColor;
   var eloIcon: EloIcon;
   def color = bind scyWindow.windowColorScheme.mainColor;

   public override function clone(): DatasyncAttribute {
      DatasyncAttribute {
         scyWindow: scyWindow
         dragAndDropManager: dragAndDropManager
         dragObject: dragObject
         priority: priority
      }
   }

   function mousePressed(e: MouseEvent) {
      println("now dragging should start.....");
      var dragNode = dragIcon();
      //       dragNode.layoutX = layoutX;
      //       dragNode.layoutY = layoutY;
      // correct for the  "layouting" in the drag and drop manager
      //       dragNode.scaleX = dragScale;
      //       dragNode.scaleY = dragScale;
      //       dragNode.translateX = -e.sceneX + e.x;
      //       dragNode.translateY = -e.sceneY + e.y;
      dragAndDropManager.startDrag(dragNode, dragObject, this, e);
   };

   function mouseDragged(e: MouseEvent) { };

   function mouseReleased(e: MouseEvent) { };

   function dragIcon(): Node {
      def icon = eloIcon.clone();
      icon.windowColorScheme.mainColorLight = Color.TRANSPARENT;
      icon.size = 2 * mouseOverItemSize;
      icon
   }

   public override function create(): Node {
      eloIcon = EloIconFactory {}.createEloIcon("data_sync");
      eloIcon.windowColorScheme = scyWindow.windowColorScheme;
      EloIconButton {
         size: itemSize
         mouseOverSize: mouseOverItemSize
         eloIcon: eloIcon.clone()
         tooltipManager: tooltipManager
         tooltip: "drag to connect"
         hideBackground: true
         actionScheme: 1

         onMousePressed: mousePressed;
         onMouseDragged: mouseDragged;
         onMouseReleased: mouseReleased;
      }
   }

}

function run() {

   Stage {
      title: "DatasyncAttribute"
      scene: Scene {
         width: 200
         height: 200
         content: [
            Group {
               translateX: 20;
               translateY: 40;
               content: [
                  Rectangle {
                     x: 0,
                     y: 0
                     width: 100,
                     height: 20
                     fill: Color.GRAY
                  }
                  DatasyncAttribute {
                     translateX: 10;
                     translateY: 0;
                  }
               ]
            }
         ]
      }
   }
}
