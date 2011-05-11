/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.art.javafx;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;

/**
 * @author SikkenJ
 */
public class NoThumbnailView extends CustomNode {

   public var windowColorScheme:WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def backgroundColor = Color.rgb(0xe9, 0xe9, 0xe9);

   public override function create(): Node {
      Group {
         content: [
            Polygon {
               points: [80.12, 80.12, 0.12, 80.12, 0.12, 0.12, 80.12, 0.12]
               fill: backgroundColor
               stroke: bind windowColorScheme.mainColor
               strokeWidth: 0.25
               strokeLineCap: StrokeLineCap.BUTT
               strokeMiterLimit: 4.0
            },
            SVGPath {
               opacity: 0.33
               fill: bind windowColorScheme.mainColorLight
               stroke: null
               strokeLineCap: StrokeLineCap.BUTT
               content: "M57.01,45.94 L12.47,68.27 C19.63,75.29 29.45,79.62 40.30,79.62 C60.22,79.62 76.71,65.00 79.54,45.94 Z M79.41,33.54 C76.26,14.85 59.95,0.62 40.30,0.62 C29.51,0.62 19.73,4.91 12.58,11.87 L57.15,33.49 Z M5.13,21.84 C2.26,27.31 0.62,33.52 0.62,40.12 C0.62,46.59 2.19,52.68 4.95,58.07 L41.59,39.79 Z "
            }
         ]
      }
   }

}

function run() {
   Stage {
      title: "test no thumnail view"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         content: [
            NoThumbnailView {
               layoutX: 20
               layoutY: 20
//               color: Color.RED
            }
         ]
      }
   }
}
