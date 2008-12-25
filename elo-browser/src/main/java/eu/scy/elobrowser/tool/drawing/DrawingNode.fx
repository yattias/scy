/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.elobrowser.tool.drawing;

import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.drawing.EloDrawingActionWrapper;
import eu.scy.scywindows.ScyWindow;
import java.awt.Dimension;
import java.net.URI;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author sikkenj
 */

 // place your code here
public class DrawingNode extends CustomNode {
   var whiteboardPanel:WhiteboardPanel;
   var eloDrawingActionWrapper:EloDrawingActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public function loadElo(uri:URI){
      eloDrawingActionWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Drawing: {eloDrawingActionWrapper.getDocName()}";
		var eloUri = eloDrawingActionWrapper.getEloUri();
		if (eloUri != null)
			scyWindow.id = eloUri.toString()
		else
			scyWindow.id = "";
	}

   public override function create(): Node {
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:5;
               spacing:5;
               content:[
                  HBox{
                     translateX:5;
                     spacing:5;
                     content:[
                        CommandText{
                           label:"New"
                           clickAction:function( e: MouseEvent ):Void {
                              eloDrawingActionWrapper.newDrawingAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Load"
                           clickAction:function( e: MouseEvent ):Void {
                              eloDrawingActionWrapper.loadDrawingAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Save"
                           clickAction:function( e: MouseEvent ):Void {
                              eloDrawingActionWrapper.saveDrawingAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Save as"
                           clickAction:function( e: MouseEvent ):Void {
                              eloDrawingActionWrapper.saveAsDrawingAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  SwingComponent.wrap(whiteboardPanel)
               ]
            }
         ]
      };
   }
}

	class CommandText extends CustomNode {
		public var label="label";
		public var clickAction:function(e: MouseEvent):Void;
		var color = Color.color(0.34,0.34,0.34);
		var hoverColor = Color.BLACK;
		var textFont =  Font {
			size: 11}
		var text:Text;

		public override function create(): Node {
			return Group {
				content: [
					Rectangle {
						x: 0,
						y: 0
						width: 55,
						height: 17
						arcHeight:5
						arcWidth:5
						fill: Color.color(0.9,0.9,0.9)
					}
					text = Text{
						translateX:8;
						translateY:12;
						font:textFont
						content: bind label
						fill:color
					}
				]
				onMouseEntered: function( e: MouseEvent ):Void {
					text.fill = hoverColor;
				}
				onMouseExited: function( e: MouseEvent ):Void {
					text.fill = color;
				}
				onMouseClicked: function( e: MouseEvent ):Void {
					if (clickAction != null) clickAction(e);
				}
      };
		}
	}

	public function createDrawingNode(roolo:Roolo):DrawingNode{
		var whiteboardPanel= new WhiteboardPanel();
		whiteboardPanel.setPreferredSize(new Dimension(2000,2000));
		var eloDrawingActionWrapper= new EloDrawingActionWrapper(whiteboardPanel);
		eloDrawingActionWrapper.setRepository(roolo.repository);
		eloDrawingActionWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
		eloDrawingActionWrapper.setEloFactory(roolo.eloFactory);
		return DrawingNode{
			whiteboardPanel:whiteboardPanel;
			eloDrawingActionWrapper:eloDrawingActionWrapper;
		}
	}



	public function createDrawingWindow(roolo:Roolo):ScyWindow{
		return
		createDrawingWindow(DrawingNode.createDrawingNode(roolo));
	}

	public function createDrawingWindow(drawingNode:DrawingNode):ScyWindow{
		var drawingWindow = ScyWindow{
			color:Color.GREEN
			title:"Drawing"
			scyContent: drawingNode
			minimumWidth:320;
			minimumHeigth:100;
			width: 320;
			height: 250;
			cache:true;
      }
		drawingNode.scyWindow = drawingWindow;
		return drawingWindow;
	}

function run(){
   Stage {
		title: "Drawing node test"
		width: 250
		height: 250
		scene: Scene {
			content:DrawingNode{
         }

		}
	}

}
