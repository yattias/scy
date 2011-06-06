/*
 * TestBackground.fx
 *
 * Created on 12-mrt-2010, 12:14:21
 */

package eu.scy.client.desktop.scydesktop.uicontrols.test;

import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.uicontrols.Background;
import eu.scy.client.desktop.scydesktop.uicontrols.BackgroundSpecification;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.uicontrols.BackgroundElement;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.effect.GaussianBlur;
import eu.scy.client.desktop.desktoputils.art.EloImageInformation;
import eu.scy.client.desktop.desktoputils.log4j.InitLog4JFX;

/**
 * @author sikken
 */

InitLog4JFX.initLog4J();

var elements = [
      BackgroundElement{
         scale:3
         xPos:80
         yPos:70
      }
      BackgroundElement{
         scale:4
         xPos:40
         yPos:380
      }
      BackgroundElement{
         scale:2.5
         xPos:100
         yPos:690
      }
      BackgroundElement{
         scale:2.5
         xPos:260
         yPos:260
      }
      BackgroundElement{
         scale:4
         xPos:390
         yPos:480
      }
      BackgroundElement{
         scale:2.5
         xPos:550
         yPos:650
      }
      BackgroundElement{
         scale:3
         xPos:490
         yPos:110
      }
      BackgroundElement{
         scale:3
         xPos:680
         yPos:440
      }
      BackgroundElement{
         scale:2.5
         xPos:750
         yPos:210
      }
      BackgroundElement{
         scale:3
         xPos:900
         yPos:140
      }
      BackgroundElement{
         scale:4
         xPos:890
         yPos:690
      }
];

var background:Background;

def backgroundGray = Color.web("#EAEAEA");
def defaultIconName = EloImageInformation.generalLogo.iconName;

def timeStep = 2s;

Timeline {
	repeatCount: Timeline.INDEFINITE
	keyFrames : [
      for (technicalType in EloImageInformation.values()){
         KeyFrame{
            time: indexof technicalType*timeStep
            action: function():Void{
               background.specification = BackgroundSpecification{
                  backgroundColor:backgroundGray;
                  iconName:if (technicalType.iconName!=null) technicalType.iconName else defaultIconName
               }
            }
         }
      }
	]
}.play();


var backgroundSpecification1 = BackgroundSpecification{
   backgroundColor:Color.GRAY;
   iconName:"logo_scy"
}
var backgroundSpecification = BackgroundSpecification{
   backgroundColor:Color.GRAY;
   iconName:"logo_scy"
}
var opcitySlider:Slider;
var blurSlider:Slider;


Stage {
	title : "Test background"
	scene: Scene {
		width: 200
		height: 200
		content: [
         background = Background{
            //sourceUrl:"testBackground.fxz"
            specification:backgroundSpecification1
            elements: elements
            iconOpacity:bind opcitySlider.value
            iconEffect:GaussianBlur {
               radius: bind blurSlider.value
            }
         }
         opcitySlider = Slider {
            layoutX:20
            layoutY:20
            min: 0
            max: 0.5
            value:0.1
            vertical: false
         }
         Text {
            font : Font {
               size: 24
            }
            x: 200, y: 30
            content: bind "{opcitySlider.value}"
         }
         blurSlider = Slider {
            layoutX:20
            layoutY:50
            min: 0
            max: 5
            value:1
            vertical: false
         }
         Text {
            font : Font {
               size: 24
            }
            x: 200, y: 70
            content: bind "{blurSlider.value}"
         }


      ]
	}
}
