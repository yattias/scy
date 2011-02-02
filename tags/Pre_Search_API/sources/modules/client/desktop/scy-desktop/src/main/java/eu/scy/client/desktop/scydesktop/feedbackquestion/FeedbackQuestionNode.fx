package eu.scy.client.desktop.scydesktop.feedbackquestion;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import javafx.scene.control.Label;
import javafx.scene.control.TextBox;
import javafx.scene.text.Font;

public class FeedbackQuestionNode extends CustomNode, ScyToolFX {

   public var toolBrokerAPI: ToolBrokerAPI;
   def spacing = 5.0;
   var eloUri: URI;
   var scyElo: ScyElo;

   public override function create(): Node {
      VBox {
         spacing: spacing
         content: [
            Label {
               text: "Ask for Feedback"
               font: Font{size:18}
            }
            TextBox {
                text: ""
                columns: 10
                lines: 20
                multiline: true
                selectOnFocus: true
            }
            Button {
               text: "Submit"
               action: function() {
                  javafx.stage.Alert.inform("Specification of the feedback question left drawer is not yet completed. Implementation is done after specification is complete.");
                  }
            }
         ]
      }
       }

   public override function loadElo(eloUri: URI): Void {
      eloUriChanged(eloUri)

       }

   public override function loadedEloChanged(eloUri: URI): Void {
      eloUriChanged(eloUri)
       }

   function eloUriChanged(eloUri: URI) {
      this.eloUri = eloUri;
      if (eloUri != null) {
         scyElo = ScyElo.loadMetadata(eloUri, toolBrokerAPI);
          } else {
         scyElo = null;
          }

       }

}
