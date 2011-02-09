/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;

/**
 * @author SikkenJ
 */
public class TitleBarBuddies extends WindowElement {

   public var window: ScyWindow;
   public-init var tooltipManager: TooltipManager;
   def buddySpacing = 5.0;
   def displayBox = HBox {
         spacing: buddySpacing
      }

   public function buddiesChanged(): Void {
      updateBuddies()
   }

   public override function create(): Node {
      updateBuddies();
      displayBox
   }

   function updateBuddies() {
      delete  displayBox.content;
      def authors = window.scyElo.getAuthors();
      if (authors != null) {
         displayBox.content =
            for (author in authors) {
               Buddy {
                  tooltipManager:tooltipManager
                  windowColorScheme: windowColorScheme
                  name: author
               }
            }
      }
   }

}

function run(): Void {
   def scale = 1.0;
   Stage {
      title: "MyApp"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         content: [
            TitleBarBuddies {
               layoutX: 10
               layoutY: 10
            }
            TitleBarBuddies {
               layoutX: 10
               layoutY: 50
            }
         ]
      }
   }
}
