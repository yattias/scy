/*
 * TestMultiImageButton.fx
 *
 * Created on 11-mrt-2010, 10:52:28
 */
package eu.scy.client.desktop.scydesktop.uicontrols.test;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.uicontrols.MultiImageButton;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.Timeline;
import eu.scy.client.desktop.desktoputils.log4j.InitLog4JFX;

/**
 * @author sikken
 */
public class TestMultiImageButton extends CustomNode {

   public var imageLocation = "{__DIR__}../images/";

   def testMultiImageButtonDesign = TestMultiImageButtonDesign {
         }
   def multiImageButton = MultiImageButton {
         disable: bind testMultiImageButtonDesign.disabledCheckbox.selected
         turnedOn: bind testMultiImageButtonDesign.turnedOnCheckbox.selected
         imageName: "new"
         action:buttonAction
         layoutX: 220
         layoutY: 100
      }
   def clickedDisplay = testMultiImageButtonDesign.clickedLabel;
   var clickedAnimation = Timeline {
      repeatCount: 1
      keyFrames : [
         at (0s){
            clickedDisplay.opacity => 0
         }
         at (100ms){
            clickedDisplay.opacity => 1.0
         }
         at (1500ms){
            clickedDisplay.opacity => 0.0
         }
      ]
   }

   public override function create(): Node {
      testMultiImageButtonDesign.reloadButton.action = loadButtonImages;
      testMultiImageButtonDesign.imageNameTextBox.text = "new";
      testMultiImageButtonDesign.clickedLabel.opacity = 0;
      loadButtonImages();
      return Group {
            content: [
               ImageView {
                  image: Image {
                     url: "{imageLocation}bckgrnd2l.jpg"
                  }
               }

               testMultiImageButtonDesign.getDesignRootNodes(),
               multiImageButton
            ]
         };
   }

   function loadButtonImages():Void{
      multiImageButton.imageName = "";
      multiImageButton.imageName = testMultiImageButtonDesign.imageNameTextBox.rawText.trim();
      testMultiImageButtonDesign.mouseOutImageView.image = multiImageButton.mouseOutImage;
      testMultiImageButtonDesign.mouseOverImageView.image = multiImageButton.mouseOverImage;
      testMultiImageButtonDesign.pressedOutImageView.image = multiImageButton.pressedMouseOutImage;
      testMultiImageButtonDesign.pressedOverImageView.image = multiImageButton.pressedMouseOverImage;
      testMultiImageButtonDesign.disabledImageView.image = multiImageButton.disabledImage;
   }

   function buttonAction():Void{
      clickedAnimation.playFromStart();
   }


}

function run() {
   InitLog4JFX.initLog4J("/config/log4j.xml");

   Stage {
      title: "Test multi image button"
      scene: Scene {
         width: 300
         height: 280
         content: TestMultiImageButton{
               imageLocation: "file:images/"
            }
      }
   }
}
