/*
 * TestLanguageSelector.fx
 *
 * Created on 18-mrt-2010, 10:05:39
 */

package eu.scy.client.desktop.scydesktop.uicontrols.test;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.uicontrols.LanguageSelector;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author sikken
 */

public class TestLanguageSelector extends CustomNode {

   def languageSelector = LanguageSelector{
      languages:["nl","en","et","fr","el"]
   }

   def spacing = 5.0;

   public override function create(): Node {
      return VBox {
         spacing:spacing
         content: [
            HBox{
               spacing:spacing
               content:[
                  languageSelector,
                  Label {
                     text: bind languageSelector.language
                  }
               ]
            }
            Label {
               text: bind languageSelector.language
            }
         ]
      };
   }
}

function run(){
Stage {
	title : "Test language selector"
	scene: Scene {
		width: 300
		height: 200
		content: [
         TestLanguageSelector{

         }

      ]
	}
}

}
