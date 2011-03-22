/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.owner.OwnershipManager;

/**
 * @author SikkenJ
 */
public class TitleBarBuddies extends TitleBarItemList {

   public var window: ScyWindow;
   public-init var tooltipManager: TooltipManager;
   public var ownershipManager: OwnershipManager on replace {
              ownershipManager.titleBarBuddies = this;
           };

   public function buddiesChanged(): Void {
      updateItems()
   }

   public override function create(): Node {
      displayBox
   }

   override function updateItems() {
      delete displayBox.content;
      displayBox.content =
              for (contact in ownershipManager.getOwners()) {
                 Buddy {
                    tooltipManager: tooltipManager
                    windowColorScheme: windowColorScheme
                    contact: contact
                 }
              }
      itemListChanged();
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
