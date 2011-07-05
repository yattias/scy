/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.common.scyelo.ScyElo;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ExtendedScyEloDisplayNode;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import java.net.URI;

/**
 * @author SikkenJ
 */

public class CollaborationMessageDialogBox {
   public var scyDesktop: ScyDesktop;
   public var eloUri: URI;
   public var eloIconName = "";
   public var title = "title";
   public var message = "message";
   public var yesTitle = ##"Yes";
   public var yesFunction: function(): Void;
   public var noTitle = ##"No";
   public var noFunction: function(): Void;
   var yesButton : Button;
   var noButton : Button;

   def spacing = 5.0;
   def scyElo: ScyElo = if (eloUri==null) null else ScyElo.loadMetadata(eloUri, scyDesktop.missionRunConfigs.tbi);
   def windowStyler = scyDesktop.windowStyler;
   def newEloCreationRegistry = scyDesktop.newEloCreationRegistry;
   var modalDialogBox: ModalDialogBox;

   init{
      createDialogBox()
   }

   public function close():Void{
      modalDialogBox.close();
   }

   function createDialogBox() {
      def windowColorScheme = windowStyler.getWindowColorScheme(scyElo);
      def eloIcon = windowStyler.getScyEloIcon(eloIconName);
      eloIcon.windowColorScheme = windowColorScheme;
      eloIcon.selected = true;
      modalDialogBox = ModalDialogBox {
                 title: title
                 eloIcon: eloIcon
                 windowColorScheme: windowColorScheme
                 content: createCollaborationMessage()
              }
   }

   function disableButtons() {
       yesButton.disable = true;
       noButton.disable = true;
   }

   function createCollaborationMessage(): Node {
      def eloIcon = windowStyler.getScyEloIcon(scyElo);
      def moreEloInfo = ExtendedScyEloDisplayNode {
                 newEloCreationRegistry: newEloCreationRegistry
                 scyElo: scyElo
                 eloIcon: eloIcon
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
            HBox{
               content:[
                     Rectangle {
                        x: 0, y: 0
                        width: 15, height: 1
                        fill: Color.TRANSPARENT
                     }
                     Label {
                        text: message
                        font: Font.font("Verdana", FontWeight.BOLD, 11)
                     }
                  ]
            }

            Rectangle {
               x: 0, y: 0
               width: 1, height: 1
               fill: Color.TRANSPARENT
            }
            moreEloInfo,
            HBox {
               spacing: spacing
               hpos: HPos.RIGHT
               content: [
                  yesButton = Button {
                     text: yesTitle
                     action: function():Void{
                        disableButtons();
                        yesFunction();
                        close();
                     }
                     
                  }
                  if (noFunction != null) {
                     noButton = Button {
                        text: noTitle
                        action: function():Void{
                            disableButtons();
                            noFunction();
                            close();
                         }
                     }
                  } else {
                     null
                  }
               ]
            }
            Rectangle {
               x: 0, y: 0
               width: 1, height: 1
               fill: Color.TRANSPARENT
            }
         ]
      }
   }


}
