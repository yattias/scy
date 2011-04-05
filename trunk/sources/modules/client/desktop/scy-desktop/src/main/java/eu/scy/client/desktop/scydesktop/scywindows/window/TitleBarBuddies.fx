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
import javafx.scene.layout.Stack;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.client.desktop.scydesktop.tooltips.impl.TextTooltip;

/**
 * @author SikkenJ
 */
public class TitleBarBuddies extends TitleBarItemList, TooltipCreator {

   public var window: ScyWindow;
   public-init var tooltipManager: TooltipManager;
   public var ownershipManager: OwnershipManager;
   public var showOneIcon = false;
   public var myName: String;
   def eloIconFactory = EloIconFactory {};

   public function buddiesChanged(): Void {
      updateItems()
   }

   public override function create(): Node {
      displayBox
   }

   override function updateItems() {
      delete  displayBox.content;
      displayBox.content = if (showOneIcon) createCombinedBuddies() else createBuddies();
      itemListChanged();
   }

   function createBuddies(): Node[] {
      for (contact in ownershipManager.getOwners()) {
         Buddy {
            tooltipManager: tooltipManager
            windowColorScheme: windowColorScheme
            contact: contact
         }
      }
   }

   function createCombinedBuddies(): Node[] {
      def contacts = ownershipManager.getOwners();
      if (sizeof contacts == 0) {
         return null;
      } else {
         var buddiesEloIconName: String;
         def isMineTo = ownershipManager.isOwner(myName);
         if (sizeof contacts == 1) {
            if (isMineTo) {
               buddiesEloIconName = "Buddy_me"
            } else {
               buddiesEloIconName = "Buddy_other"
            }
         } else {
            if (isMineTo) {
               buddiesEloIconName = "Buddies_me_and_others"
            } else {
               buddiesEloIconName = "Buddies_others"
            }
         }
         def eloIcon = eloIconFactory.createEloIcon(buddiesEloIconName);
         eloIcon.windowColorScheme.assign(windowColorScheme);
         eloIcon.windowColorScheme.mainColorLight = Color.TRANSPARENT;
         eloIcon.size = itemSize;
         tooltipManager.registerNode(eloIcon, this);
         return Stack {
                    content: [
                       Rectangle {
                          x: 0, y: 0
                          width: mouseOverItemSize, height: mouseOverItemSize
                          fill: Color.TRANSPARENT
                       }
                       eloIcon
                    ]
                 }
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      def contacts = ownershipManager.getOwners();
      var tooltip = "";
      for (contact in contacts) {
         if (indexof contact > 0) {
            tooltip += "\n"
         }
         tooltip += "{contact.name}"
      }
      TextTooltip {
         content: tooltip
         windowColorScheme: windowColorScheme
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
