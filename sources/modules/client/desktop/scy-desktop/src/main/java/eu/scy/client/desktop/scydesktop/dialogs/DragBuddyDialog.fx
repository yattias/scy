/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.dialogs;

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
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.tools.search.ExtendedScyEloDisplayNode;

/**
 * @author sven
 */

public class DragBuddyDialog {
   public var scyDesktop: ScyDesktop;
   public var eloIconName = "";
   public var title = "title";
   public var message = "message";
   def addBuddyTitle = ##"Add buddy as owner";
   public var addBuddyFunction: function(): Void;
   def sendEloTitle = ##"Send the ELO";
   public var sendEloFunction: function(): Void;
   def collaborateTitle = ##"Collaborate";
   public var collaborateFunction: function(): Void;
   def cancelTitle = ##"Cancel";
   public var cancelFunction: function(): Void;
   public-init var collaborative : Boolean = false;
   public-init var scyElo: ScyElo;
   var addBuddyButton : Button;
   var sendEloButton : Button;
   var collaborateButton : Button;
   var cancelButton : Button;

   def spacing = 5.0;
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
       addBuddyButton.disable = true;
       sendEloButton.disable = true;
       collaborateButton.disable = true;
       cancelButton.disable = true;
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
                   addBuddyButton = Button{
                     text: addBuddyTitle
                     action: function():Void{
                        disableButtons();
                        addBuddyFunction();
                        close();
                     }
                   }
                   sendEloButton = Button{
                     text: sendEloTitle
                     action: function():Void{
                        disableButtons();
                        sendEloFunction();
                        close();
                     }
                   }
                  if (collaborative) {
                    collaborateButton = Button{
                     text: collaborateTitle
                     action: function():Void{
                        disableButtons();
                        collaborateFunction();
                        close();
                     }
                   }
                  } else {
                     null
                  }
                   cancelButton = Button{
                     text: cancelTitle
                     action: function():Void{
                        disableButtons();
                        cancelFunction();
                        close();
                     }
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
