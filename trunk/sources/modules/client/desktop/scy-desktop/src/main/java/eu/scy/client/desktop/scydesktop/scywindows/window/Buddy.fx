/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.Cursor;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.Contact;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.layout.Stack;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.tooltips.impl.NodeTooltip;

/**
 * @author SikkenJ
 */
public class Buddy extends CustomNode, TooltipCreator {

   public-init var tooltipManager: TooltipManager;
   public-init var contact: Contact;
   public var eloIcon: EloIcon;
   public var tooltipContent: Node;
   public var windowColorScheme: WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   public var size = 10.0;

   public override function create(): Node {
      tooltipManager.registerNode(this, this);
      def node: Group = Group {
                 content: Stack {
                    content: [
                       Rectangle {
                          x: 0, y: 0
                          width: size, height: size
                          fill: Color.TRANSPARENT
                       }
                       eloIcon
                    ]
                 }
              }
      if (contact.onlineState == OnlineState.PENDING) {
         createProgressIndicator(node)
      }
      return node;
   }

   function createProgressIndicator(node: Group): Void {
      def progress = ProgressIndicator {
                 cursor: Cursor.WAIT
                 translateX: 0
                 translateY: -2
                 scaleX: 0.4
                 scaleY: 0.4
                 visible: bind (contact.onlineState == OnlineState.PENDING)
              }
      insert progress into node.content;
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      NodeTooltip {
         content: tooltipContent
         windowColorScheme: windowColorScheme
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
