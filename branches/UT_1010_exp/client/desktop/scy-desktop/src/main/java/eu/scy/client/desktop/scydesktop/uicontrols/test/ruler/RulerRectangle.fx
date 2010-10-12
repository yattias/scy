/*
 * RulerRectangle.fx
 *
 * Created on 19-apr-2010, 13:07:38
 */
package eu.scy.client.desktop.scydesktop.uicontrols.test.ruler;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.control.Slider;
import javafx.scene.text.TextOrigin;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Rectangle;

/**
 * @author sikken
 */
public class RulerRectangle extends CustomNode {

   def gridStep = 50;
   def gridColor = Color.GRAY;

   public var xSize = 100.0 on replace {sizeChanged()};
   public var ySize = 100.0 on replace {sizeChanged()};

   public var background:Color = Color.YELLOWGREEN;

   def contentGroup = Group{

   }

   function sizeChanged(){
      delete contentGroup.content;
      contentGroup.content = createContent();
   }

   public override function create(): Node {
      sizeChanged();
      contentGroup
   }

   function createContent():Node{
      var sizeText = Text {
         x: 0, y: 0
         content: getSizeText()
         textAlignment:TextAlignment.CENTER
         textOrigin:TextOrigin.TOP
         layoutX: xSize/2
         layoutY:ySize/2
      }
      sizeText.translateX = -sizeText.layoutBounds.width/2;
      sizeText.translateY = -sizeText.layoutBounds.height/2;
      Group {
            content: [
               Rectangle{
                  x:0
                  y:0
                  width:xSize
                  height:ySize
                  fill:background
               }
               for (x in [0..xSize step gridStep]){
                  Line{
                     startX:x
                     startY:0
                     endX:x
                     endY:ySize
                     stroke:gridColor
                  }
               }
               for (y in [0..ySize step gridStep]){
                  Line{
                     startX:0
                     startY:y
                     endX:xSize
                     endY:y
                     stroke:gridColor
                  }
               }
               PixelRuler {
                  layoutX: 0
                  layoutY: 0
                  pixelRulerOrientation: PixelRulerOrientation.TOP
                  maximum: xSize
               }
               PixelRuler {
                  layoutX: xSize
                  layoutY: 0
                  pixelRulerOrientation: PixelRulerOrientation.RIGHT
                  maximum: ySize
               }
               PixelRuler {
                  layoutX: 0
                  layoutY: ySize
                  pixelRulerOrientation: PixelRulerOrientation.BOTTOM
                  maximum: xSize
               }
               PixelRuler {
                  layoutX: 0
                  layoutY: 0
                  pixelRulerOrientation: PixelRulerOrientation.LEFT
                  maximum: ySize
               }
               sizeText
            ]
         };
   }

   protected function getSizeText():String{
      "{xSize}*{ySize}"
   }


}

function run() {
   var slider:Slider;
   Stage {
      title: "Test RulerRectangle"
      scene: Scene {
         width: 300
         height: 300
         fill:Color.YELLOW
         content: [
            slider = Slider {
               layoutX:10
               layoutY:10
               min: 50
               max: 200
               value:100.123
               vertical: false
            }
            Text{
               x:200
               y:20
               content: bind "{slider.value}"
            }
            RulerRectangle {
               layoutX: 10
               layoutY: 50
               xSize : bind slider.value
               ySize: bind slider.value
            }
         ]
      }
   }
}

