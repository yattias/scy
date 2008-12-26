/*
 * EloDisplay.fx
 *
 * Created on 6-okt-2008, 19:43:36
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.EloDisplay;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.drawing.EloDrawingActionWrapper;
import eu.scy.elobrowser.ui.SwingMenuItem;
import eu.scy.elobrowser.ui.SwingPopupMenu;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import java.util.HashMap;
import javafx.ext.swing.SwingButton;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import roolo.elo.api.IELO;

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
	public var roolo:Roolo;
   public var title = "title";
   public var radius = 20;
   public var eloType = "unknown" on replace {
		updateImage()};
	public var elo:IELO;
	public var image = unknownEloImage;
   public var textColor = Color.BLACK;
	public var textBackgroundColor = Color.TRANSPARENT;
   public var fillColor = Color.color(0,0.5,0,0.25);
	public var dragable = true;
   var textFont =  Font {
      size: 12}

	var originalX:Number;
	var originalY:Number;

	var openMenuItem = SwingMenuItem{
		label:"Open"
		enabled:false;
		action:openElo;
			}

	function updateImage() {
		image = getEloImage(eloType);
		openMenuItem.enabled = EloDrawingActionWrapper.scyDrawType == eloType;
	}

	function openElo(){
		if (EloDrawingActionWrapper.scyDrawType == eloType){
			var eloWindow = ScyDesktop.getScyDesktop().findScyWindow(elo.getUri().toString());
			if (eloWindow == null){
				var drawingNode = DrawingNode.createDrawingNode(roolo);
				eloWindow = DrawingNode.createDrawingWindow(drawingNode);
				ScyDesktop.getScyDesktop().addScyWindow(eloWindow);
				drawingNode.loadElo(elo.getUri());
			}
			else {
			ScyDesktop.getScyDesktop().activateScyWindow(eloWindow);
			}
		}
	}

	function openEloXml(){
		var eloXmlWindowId = "{elo.getUri().toString()}.xml";
		var eloXmlWindow = ScyDesktop.getScyDesktop().findScyWindow(eloXmlWindowId);
		if (eloXmlWindow == null){
			var textArea = new JTextArea();
			textArea.setEditable(false);
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			var scrollPane = new JScrollPane(textArea);
			var textNode = SwingComponent.wrap(scrollPane);
			eloXmlWindow = ScyWindow{
				id: eloXmlWindowId
				color:Color.color(0.8,0,0)
				title:"XML: {elo.getUri()}"
				scyContent: textNode
				visible:true
				//width:150
				height:150
			}
			eloXmlWindow.width = 300;
			ScyDesktop.getScyDesktop().addScyWindow(eloXmlWindow);
			textArea.setText(elo.getXml());
		}
		else {
			ScyDesktop.getScyDesktop().activateScyWindow(eloXmlWindow);
		}
	}

   public override function create(): Node {
 		var menuItems = [
			openMenuItem,
			SwingMenuItem{
				label:"Show elo xml"
				action:openEloXml
			}
		];
		var imageView:ImageView;
		var titleText = Text{
			content: bind title;
			fill: bind textColor;
			font: bind textFont;
            };
		// place a rect behind the text, to better catch mouse actions on the text,
		// the text does catch mouse actions always
		var titleTextRect:Rectangle = Rectangle {
			x: bind titleText.boundsInLocal.minX,
			y: bind titleText.boundsInLocal.minY
			width: bind titleText.boundsInLocal.width,
			height: bind titleText.boundsInLocal.height
			fill: bind textBackgroundColor
				};
		var titleGroup:Group;
		var displayGroup = Group
      {
			blocksMouse:true;
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
				titleGroup = Group{
					translateX:bind -titleText .boundsInLocal.width / 2;
					translateY:bind radius / 2 + titleText.boundsInLocal.height;
					content:[titleTextRect,titleText]
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
			onMouseClicked: function( e: MouseEvent ):Void {
				if (e.clickCount == 2) openElo();
			}
      };
		return Group{
			content:[
				displayGroup
				SwingPopupMenu{
					items: menuItems;
					translateX: bind displayGroup.boundsInLocal.minX,
					translateY: bind displayGroup.boundsInLocal.minY
					width: bind displayGroup.boundsInLocal.width,
					height: bind displayGroup.boundsInLocal.height
					shapes:[imageView,titleGroup]
					//fill:Color.color(0.9,0.9,0.9,0.7);
				}
			]
		}
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
						eloDisplay.title = "new title, but very long"
               }
            }
         ]
      }
	}
}
