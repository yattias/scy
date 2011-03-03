/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop.test;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;

/**
 * @author SikkenJ
 */

public class TestDialogsNode extends CustomNode, ScyToolFX {

   def spacing = 5.0;

   public override function create(): Node {
      VBox {
         spacing: spacing
         content: [
            Label {
               text: "Test Dialog\nnot part of mission"
            }
            Button {
               text: "More assignment"
               action: function() {
                  }
            }
            Button {
               text: "More resources"
               action: function() {
                  }
            }
         ]
      }
       }


}
