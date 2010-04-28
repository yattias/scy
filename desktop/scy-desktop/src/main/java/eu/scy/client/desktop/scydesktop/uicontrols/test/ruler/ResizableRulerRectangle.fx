/*
 * ResizableRulerRectangle.fx
 *
 * Created on 19-apr-2010, 14:09:21
 */

package eu.scy.client.desktop.scydesktop.uicontrols.test.ruler;
import javafx.scene.layout.Resizable;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author sikken
 */

public class ResizableRulerRectangle extends RulerRectangle,Resizable {

   public var minimumWidth = 10;
   public var preferredWidth = width;
   public var maximumWidth = 1000;
   public var minimumHeight = 10;
   public var preferredHeight = height;
   public var maximumHeight = 1000;

   public override var width on replace{xSize = width; preferredWidth = width}
   public override var height on replace{ySize = height; preferredHeight = height}

    override public function getPrefWidth (arg0 : Number) : Number {
        preferredWidth
    }

    override public function getPrefHeight (arg0 : Number) : Number {
        preferredHeight
    }

    override public function getMinWidth() : Number {
        minimumWidth
    }

    override public function getMinHeight() : Number {
        minimumHeight
    }

    override public function getMaxWidth() : Number {
        maximumWidth
    }

    override public function getMaxHeight() : Number {
        maximumHeight
    }

   protected override function getSizeText():String{
      "min:{minimumWidth}*{minimumHeight}\n"
      "pre:{preferredWidth}*{preferredHeight}\n"
      "max:{maximumWidth}*{maximumHeight}\n"
//      "real:{xSize}*{ySize}\n"
      "lay:{width}*{height}"
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
               value:100.1
               vertical: false
            }
            Text{
               x:200
               y:20
               content: bind "{slider.value}"
            }
            ResizableRulerRectangle {
               layoutX: 10
               layoutY: 50
               width : bind slider.value
               height: bind slider.value
            }
         ]
      }
   }
}

