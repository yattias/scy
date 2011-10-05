/*
 * ContactTooltipCreator.fx
 *
 * Created on 11.02.2010, 16:24:13
 */
package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import eu.scy.client.desktop.scydesktop.tooltips.impl.NodeTooltip;

/**
 * @author svenmaster
 */
public class ContactTooltipCreator extends TooltipCreator {

   public var windowColorScheme: WindowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

   override public function createTooltipNode(sourceNode: Node): Node {
      if (sourceNode instanceof ContactFrame) {
         def contactFrame: ContactFrame = sourceNode as ContactFrame;
         def contact = contactFrame.contact;
         return NodeTooltip{
            content:createTooltipContent(contact)
            windowColorScheme: windowColorScheme
         }
      } else {
         return null;
      }
   }

   function createTooltipContent(contact: Contact): Node {
      VBox {
         spacing: 5.0;
         content: [
            Text {
               font: Font.font("", FontWeight.BOLD, FontPosture.REGULAR, 16)
               x: 0, y: 0;
               content: contact.name;
            }
            Text {
               x: 0, y: 0;
               content: "{contact.onlineState}"
            }
            Text {
               x: 0, y: 0;
               content: ##"Drag me over a window to start collaboration!"
            }
         ]
      }
   }

}
