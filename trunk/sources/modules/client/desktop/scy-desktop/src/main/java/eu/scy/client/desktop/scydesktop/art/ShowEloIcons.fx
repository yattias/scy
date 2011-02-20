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

/**
 * @author SikkenJ
 */
public class ShowEloIcons extends CustomNode {

   def eloIconFactory = EloIconFactory {};
   def iconSpacingX = 100;
   def maxX = 500;
   def iconSpacingY = 70;
   def windowColorScheme = WindowColorScheme{
      mainColor:Color.rgb(38, 47, 102)
      mainColorLight:Color.rgb(181, 213, 240)
      secondColor:Color.rgb(0,178,89)
      secondColorLight:Color.rgb(204,240,222)
      thirdColor:Color.rgb(233,90,12)
      thirdColorLight:Color.rgb(242,213,199)
   }

   public override function create(): Node {
      Group {
         content: [
            generateEloIconViews()
         ]
      }
   }

   function generateEloIconViews(): Node[] {
      def eloIconNames = Sequences.sort(eloIconFactory.getNames()) as String[];
      var x = 0.0;
      var y = 0.0;
      for (eloIconName in eloIconNames) {
         def display = VBox {
               layoutX: x
               layoutY: y
               content: [
                  createEloIcon(eloIconName),
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

   function createEloIcon(eloIconName:String):EloIcon{
      def eloIcon = eloIconFactory.createEloIcon(eloIconName);
      eloIcon.windowColorScheme = windowColorScheme;
      eloIcon
   }


}

function run() {
   Stage {
      title: "MyApp"
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
