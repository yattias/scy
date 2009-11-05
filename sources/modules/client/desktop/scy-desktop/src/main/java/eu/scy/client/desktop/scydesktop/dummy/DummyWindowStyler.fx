/*
 * DummyWindowStyler.fx
 *
 * Created on 30-jun-2009, 14:33:55
 */

package eu.scy.client.desktop.scydesktop.dummy;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;

import org.apache.log4j.Logger;

/**
 * @author sikkenj
 */
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler");

public class DummyWindowStyler extends WindowStyler {

   public def drawingType = "scy/drawing";
   public def datasetType = "scy/dataset";
   public def simulationConfigType = "scy/simconfig";
   public def datasetProcessingType = "scy/pds";
   public def imageType = "scy/image";
   public def textType = "scy/text";
   public def mappingType = "scy/mapping";
   public def meloType = "scy/melo";
   public def urlType = "scy/url";

   public def scyGreen = Color.web("#8db800");
   public def scyPurple = Color.web("#7243db");
   public def scyOrange = Color.web("#ff5400");
   public def scyPink = Color.web("#fb06a2");
   public def scyBlue = Color.web("#0042f1");
   public def scyMagenta = Color.web("#0ea7bf");

   public override function getScyColor(type:String):Color{
      var scyColor = scyMagenta;
      if (type == drawingType)
         scyColor = scyGreen
      else if (type == datasetType)
         scyColor = scyPurple
      else if (type == simulationConfigType)
         scyColor = scyPink
      else if (type == mappingType)
         scyColor = scyBlue
      else if (type == imageType)
         scyColor = scyOrange
      else if (type == meloType)
         scyColor = scyOrange
      else if (type == textType)
         scyColor = scyBlue
      else if (type == urlType)
         scyColor = scyOrange;
      return scyColor;
   }

   public override function getScyIconCharacter(type:String):String{
      var iconChar = "?";
      if (type == drawingType)
         iconChar = "D"
      else if (type == datasetType)
         iconChar = "V"
      else if (type == simulationConfigType)
         iconChar = "S"
      else if (type == datasetProcessingType)
         iconChar = "P"
      else if (type == textType)
         iconChar = "T"
      else if (type == imageType)
         iconChar = "I"
      else if (type == meloType)
         iconChar = "I"
      else if (type == mappingType)
         iconChar = "M"
      else if (type == urlType)
         iconChar = "W";
      return iconChar;
   }
}


function run(){

   Stage {
      title: "color test"
      scene: Scene {
         width: 200
         height: 200
         content: [
            Rectangle {
               x: 10,
               y: 10
               width: 140,
               height: 90
               fill: Color.web("8db800")
            }

         ]
      }
   }
}
