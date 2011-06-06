/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ShowInfoUrl;
import java.net.URL;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.Button;

/**
 * @author SikkenJ
 */
public class TestMoreInfoTool extends CustomNode, ShowInfoUrl {

   def spacing = 5.0;
   def infoUrlDisplay = Label {
         layoutX: 5.0
         layoutY: 5.0
         text: ""
      }

   public override function create(): Node {
      Group {
         content: [
            Rectangle {
               width: 2000
               height: 2000
               fill: Color.YELLOW
            }
            VBox {
               spacing: spacing
               padding: Insets {
                  top: spacing
                  right: spacing
                  bottom: spacing
                  left: spacing
               }
               content: [
                  infoUrlDisplay,
                  Button {
                     text: "test"
                     action: function() {

                     }
                  }
               ]
            }
         ]
      }
   }

   override public function showInfoUrl(url: URL): Void {
      infoUrlDisplay.text = "Info url: {url}"
   }

}
