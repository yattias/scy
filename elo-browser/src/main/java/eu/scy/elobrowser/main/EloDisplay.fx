/*
 * EloDisplay.fx
 *
 * Created on 6-okt-2008, 19:43:36
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.EloDisplay;
import eu.scy.elobrowser.tool.drawing.EloDrawingActionWrapper;
import eu.scy.elobrowser.ui.SwingPopupMenu;
import eu.scy.elobrowser.ui.SwingMenuItem;
import java.util.HashMap;
import javafx.ext.swing.SwingButton;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

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
   //System.out.println("finding image for {eType}, {image.url}");
   return image;
	}

public class EloDisplay extends CustomNode {
   public var title = "title";
   public var radius = 20;
   public var eloType = "unknown" on replace {
		updateImage()};
	public var image = unknownEloImage;
   public var strokeColor = Color.BLACK;
   public var fillColor = Color.color(0,0.5,0,0.25);
	public var dragable = true;
   var textFont =  Font {
      size: 12}

	var originalX:Number;
	var originalY:Number;

	function updateImage() {
		image = getEloImage(eloType);
	}

   public override function create(): Node {
 		var menuItems = [
			SwingMenuItem{
				label:"Open"
				enabled:false;
			}
			SwingMenuItem{
				label:"Show elo xml"
				enabled:false;
			}
		];
		var imageView:ImageView;
		var text:Text;
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
            imageView = ImageView{
					translateX:bind -radius / 2
					translateY:bind -radius / 2
               image:bind image
               fitHeight:bind radius
               fitWidth:bind radius
               preserveRatio:true
            }
				text = Text{
               x: 0;
               y: bind radius / 2 + textFont.size;
               textAlignment:TextAlignment.CENTER;
               //wrappingWidth:100
               content: bind title;
               fill: bind strokeColor;
               font: bind textFont;
            }
				SwingPopupMenu{
					items: menuItems;
					translateX:bind -radius / 2
					translateY:bind -radius / 2
					width:1000
					height:bind radius + 2*textFont.size;
					shapes:[imageView,text]
					//fill:Color.color(0.9,0.9,0.9,0.7);
				}
         ]
			onMousePressed: function( e: MouseEvent ):Void {
				originalX = translateX;
				originalY = translateY;
			}
			onMouseDragged: function( e: MouseEvent ):Void {
				if (dragable){
					translateX = originalX + e.dragX;
					translateY = originalY + e.dragY;
				}
			}
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
         content: [
				eloDisplay=EloDisplay{
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
