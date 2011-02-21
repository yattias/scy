/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.art;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
import javafx.util.Sequences;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.control.TextBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.io.File;

/**
 * @author SikkenJ
 */
public class ShowEloIcons extends CustomNode {

   def eloIconFactory = EloIconFactory {};
   def iconSpacingX = 120;
   def maxX = 500;
   def iconSpacingY = 80;
   def spacing = 5.0;
   def windowColorScheme = WindowColorScheme {
         mainColor: Color.rgb(38, 47, 102)
         mainColorLight: Color.rgb(181, 213, 240)
         secondColor: Color.rgb(0, 178, 89)
         secondColorLight: Color.rgb(204, 240, 222)
         thirdColor: Color.rgb(233, 90, 12)
         thirdColorLight: Color.rgb(242, 213, 199)
      }

   public override function create(): Node {
      Group {
         content: [
            generationControl(),
            generateEloIconViews()
         ]
      }
   }

   function generationControl(): Node {
      def contentFileBox = TextBox {
            text: "src\\main\\java\\eu\\scy\\client\\desktop\\scydesktop\\art\\images\\content.fxd"
            columns: 60
            selectOnFocus: true
         }
      def targetDirBox = TextBox {
            text: "src\\main\\java\\eu\\scy\\client\\desktop\\scydesktop\\art\\eloicons"
            columns: 60
            selectOnFocus: true
         }

      HBox {
         spacing: spacing
         content: [
            Button {
               text: "Generate"
               action: function() {
                  def contentFile = new File(contentFileBox.rawText);
                  def targetDir = new File(targetDirBox.rawText);
                  println("contentFile: {contentFile.getAbsolutePath()}");
                  println("targetDir: {targetDir.getAbsolutePath()}");
                  new JavaFxdEloIconLoader(contentFile, targetDir);
               }
            }
            VBox {
               spacing: spacing
               content: [
                  HBox {
                     spacing: spacing
                     content: [
                        Label {
                           text: "FXZ content file:"
                        }
                        contentFileBox
                     ]
                  }
                  HBox {
                     spacing: spacing
                     content: [
                        Label {
                           text: "Target directory:"
                        }
                        targetDirBox
                     ]
                  }
               ]
            }
         ]
      }

   }

   function generateEloIconViews(): Node[] {
      def eloIconNames = Sequences.sort(eloIconFactory.getNames()) as String[];
      var x = 0.0;
      var y = iconSpacingY;
      for (eloIconName in eloIconNames) {
         def display = VBox {
               layoutX: x
               layoutY: y
               spacing: spacing
               content: [
                  HBox {
                     spacing: spacing
                     content: [
                        createEloIcon(eloIconName, false),
                        createEloIcon(eloIconName, true),
                     ]
                  }

                  Label {
                     text: eloIconName
                  }
               ]
            }
         x += iconSpacingX;
         if (x > maxX) {
            x = 0;
            y += iconSpacingY;
         }
         display
      }

   }

   function createEloIcon(eloIconName: String, selected: Boolean): EloIcon {
      def eloIcon = eloIconFactory.createEloIcon(eloIconName);
      eloIcon.windowColorScheme = windowColorScheme;
      eloIcon.selected = selected;
      eloIcon
   }

}

function run() {
   Stage {
      title: "Elo icon overview"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         content: [
            ShowEloIcons {
               layoutX: 10
               layoutY: 10
            }
         ]
      }
   }

}
