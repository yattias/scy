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
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.Contact;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.tooltips.impl.NodeTooltip;
import javafx.scene.control.Label;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactNameComparator;

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
   def spacing = 5.0;

   public function buddiesChanged(): Void {
      FX.deferAction(updateItems)
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
            eloIcon: getContactEloIcon(contact)
            size: mouseOverItemSize
            tooltipContent: getContactTooltipContent(contact)
         }
      }
   }

   function createCombinedBuddies(): Node[] {
      def contacts = Sequences.sort(ownershipManager.getOwners(), ContactNameComparator {}) as Contact[];
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
         def eloIcon = getEloIcon(buddiesEloIconName);
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

   function getContactEloIcon(contact: Contact): EloIcon {
      def eloIconName = if (contact.onlineState == OnlineState.IS_ME) "buddy_me" else "buddy_other";
      def eloIcon = getEloIcon(eloIconName);
      if (contact.onlineState != OnlineState.IS_ME and contact.onlineState != OnlineState.ONLINE) {
         eloIcon.opacity = 0.2;
      }
      return eloIcon
   }

   function getEloIcon(name: String): EloIcon {
      def eloIcon = eloIconFactory.createEloIcon(name);
      eloIcon.windowColorScheme.assign(windowColorScheme);
      eloIcon.windowColorScheme.mainColorLight = Color.TRANSPARENT;
      eloIcon.size = itemSize;
      return eloIcon;
   }

   function getContactTooltipContent(contact: Contact): Node {
      HBox {
         spacing: spacing
         content: [
            getContactEloIcon(contact),
            Label {
               text: contact.name
            }
         ]
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      def contacts = Sequences.sort(ownershipManager.getOwners(), ContactNameComparator {}) as Contact[];
      NodeTooltip {
         content: VBox {
            spacing: spacing
            content: [
               for (contact in contacts) {
                  getContactTooltipContent(contact)
               }
            ]
         }
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
