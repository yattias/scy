/*
 * EloDisplay.fx
 *
 * Created on 6-okt-2008, 19:43:36
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.tool.drawing.EloDrawingActionWrapper;
import java.lang.System;
import java.util.HashMap;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.ext.swing.SwingButton;

/**
* @author sikken
 */

 // place your code here

var eloImages:HashMap;
var unknownEloImage:Image = Image {
   url: "{__DIR__}images/unknownElo.png"
   };

function initImages(){
   eloImages = new HashMap();
   var plainEloImage = Image {
      url: "{__DIR__}images/plainElo.png"
      }
   var drawingEloImage = Image {
      url: "{__DIR__}images/drawingElo.png"
      }
   eloImages.put(EloDrawingActionWrapper.scyDrawType,drawingEloImage);
}

function getEloImage(eType:String){
   if (eloImages == null) initImages();
   var image:Image =
   eloImages.get(eType) as Image;
   if (image == null){
      image = unknownEloImage;
   }
   System.out.println("finding image for {eType}, {image.url}");
   return image;
}

public class EloDisplay extends CustomNode {
   public var title = "title";
   public var radius = 20;
   public var eloType = "unknown" on replace {updateImage()};
	public var image = unknownEloImage;
   public var strokeColor = Color.BLACK;
   public var fillColor = Color.color(0,0.5,0,0.25);
   var textFont =  Font {
      size: 12}

	function updateImage()
	{
		 image = getEloImage(eloType);
	}

   public override function create(): Node {
      return Group
      {
         content: [
//            Circle {
//               centerX: 0;
//               centerY: 0;
//               radius: bind radius/2;
//               stroke:bind strokeColor;
//               fill: bind fillColor;
//            },
            ImageView{
                translateX:bind -radius/2
                translateY:bind -radius/2
               image:bind image
               fitHeight:bind radius
               fitWidth:bind radius
               preserveRatio:true
            }
               Text
	    {
               x: 0;
               y: bind radius/2 + textFont.size;
               textAlignment:TextAlignment.CENTER;
               //wrappingWidth:100
               content: bind title;
               fill: bind strokeColor;
               font: bind textFont;
            }
         ]
      };
   }
}


function run(){
 var eloDisplay:EloDisplay;
   Stage {
      title: "EloDisplay test"
      scene: Scene {
         width: 200
         height: 200
         content: [ eloDisplay=EloDisplay{
               translateX:50;
               translateY:50;
               radius:30
               title:"testing"
               //eloType:"??"
            }
            SwingButton {
               text: "change"
               action: function() {
                  eloDisplay.eloType="scy/drawing";
                  eloDisplay.radius = 48;
               }
            }
         ]
      }
}
}
