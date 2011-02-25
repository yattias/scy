/*
 * TestDynamicTypeBackground.fx
 *
 * Created on 15-mrt-2010, 17:39:24
 */
package eu.scy.client.desktop.scydesktop.uicontrols.test;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.uicontrols.DynamicTypeBackground;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

/**
 * @author sikken
 */
public class TestDynamicTypeBackground extends CustomNode {

   public var dynamicTypeBackground = DynamicTypeBackground {
         }

   public override function create(): Node {
      return Group {
            content: [
               dynamicTypeBackground,
               VBox {
                  spacing:3
                  content:[
                     Label {
//                        text: bind "Type: {dynamicTypeBackground.type}"
                     }
                     Button {
                        text: "None"
                        action: function () {
//                           dynamicTypeBackground.type = "";
                        }
                     }
                     for (type in EloImageInformation.values()) {
                        Button {
                           text: "{type.type}"
                           action: function () {
//                              dynamicTypeBackground.type = type.type;
                           }
                        }
                     }
                  ]
               }
            ]
         };
   }

}

function run(){
InitLog4JFX.initLog4J();

Stage {
	title : "Test DynamicTypeBackground"
	scene: Scene {
		width: 200
		height: 300
		content: [
         TestDynamicTypeBackground{

         }

      ]
	}
}
}
