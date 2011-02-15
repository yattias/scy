/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.tooltips.impl.ColoredTextTooltip;
import eu.scy.client.desktop.scydesktop.awareness.BuddyModel;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;

/**
 * @author SikkenJ
 */
public class Buddy extends CustomNode, TooltipCreator {

   public-init var tooltipManager: TooltipManager;
   public-init var buddyModel: BuddyModel;
   public var windowColorScheme: WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def size = 10.0;
   def circleSize = 4.0;
   def lineWidth = 2.0;
   def color = bind if (OnlineState.ONLINE == buddyModel.onlineState) windowColorScheme.mainColor else windowColorScheme.mainColorLight;

   public override function create(): Node {
      tooltipManager.registerNode(this, this);
      Group {
         content: [
            Polyline {
               points: [0, size, size / 2, size / 2, size, size, 0, size]
               strokeWidth: lineWidth
               stroke: bind color
               fill: bind color
            }
            Circle {
               centerX: size / 2, centerY: circleSize
               radius: circleSize
               strokeWidth: lineWidth
               stroke: bind color
               fill: bind if (buddyModel.isMe) color else Color.WHITE
            }
         ]
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      ColoredTextTooltip {
         content: buddyModel.name
         color: windowColorScheme.mainColor
      }
   }

}

function run(): Void {
   def scale = 3.0;
   Stage {
      title: "Test buddy"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         content: [
            Buddy {
               layoutX: 10
               layoutY: 10
            }
            Buddy {
               layoutX: 10
               layoutY: 50
            }
         ]
      }
   }
}
