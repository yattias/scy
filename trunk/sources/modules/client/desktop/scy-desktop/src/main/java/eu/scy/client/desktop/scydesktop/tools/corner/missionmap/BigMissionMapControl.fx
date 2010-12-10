/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.MoreInfoWindow;
import eu.scy.client.desktop.scydesktop.scywindows.ModalDialogLayer;

/**
 * @author SikkenJ
 */
public class BigMissionMapControl extends CustomNode {

   public var bigMissionMap: BigMissionMap;
   def missionMapWindow: MoreInfoWindow = MoreInfoWindow {
         title: "big mission map"
         content: bigMissionMap
         closeAction: hideBigMissionMap
      }
   def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
   def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
   def relativeWindowScreenBoder = 0.2;
   var bigMissionMapVisible = false;

   public override function create(): Node {
      Group {
         content: [
            Button {
               rotate: 90
               text: "big"
               action: function() {
                  showBigMissionMap();
               }
            }
         ]
      }
   }

   function sceneSizeChanged() {
      if (bigMissionMapVisible) {
         missionMapWindow.width = (1 - 2 * relativeWindowScreenBoder) * scene.width;
         missionMapWindow.height = (1 - 2 * relativeWindowScreenBoder) * scene.height;
      }
   }

   function showBigMissionMap() {
      bigMissionMapVisible = true;
      sceneSizeChanged();
      ModalDialogLayer.addModalDialog(missionMapWindow,true);
   }

   function hideBigMissionMap(): Void {
      bigMissionMapVisible = false;
      ModalDialogLayer.removeModalDialog(missionMapWindow);
   }

}
