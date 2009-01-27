/*
 * NewJavaFXEmpty.fx
 *
 * Created on 23.jan.2009, 14:08:29
 */

package eu.scy.elobrowser.tool.colemo;
import eu.scy.elobrowser.main.Roolo;
import javafx.scene.CustomNode;
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
import eu.scy.colemo.client.ColemoPanel;


public class ColemoNode extends CustomNode {

    //var whiteboardPanel:WhiteboardPanel;
   var colemoPanel : ColemoPanel;
   var colemoActionWrapper:ColemoActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};



	function setScyWindowTitle(){
		if (scyWindow == null)  return;
		scyWindow.title = "Colemo-window";
		var eloUri = null;//colemoActionWrapper.getEloUri();
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
                           label:"New session"
                           clickAction:function( e: MouseEvent ):Void {
                              colemoActionWrapper.connect();
										setScyWindowTitle();
                           }
                        }

                        CommandText{
                           label:"New Concept"
                           clickAction:function( e: MouseEvent ):Void {
                              colemoActionWrapper.createNewConcept();
										setScyWindowTitle();
                           }
                        }

                     ]
                  }
                  SwingComponent.wrap(colemoPanel)
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

	public function createColemoNode(roolo:Roolo):ColemoNode{
		var colemoPanel= new ColemoPanel();

		colemoPanel.setPreferredSize(new Dimension(2000,2000));
		var colemoActionWrapper= new ColemoActionWrapper(colemoPanel);
        
		//colemoActionWrapper.setRepository(roolo.repository);
		//colemoActionWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
		//colemoActionWrapper.setEloFactory(roolo.eloFactory);
		return ColemoNode{
			colemoPanel:colemoPanel;
			colemoActionWrapper:colemoActionWrapper;
		}
	}

	public function createColemoWindow(roolo:Roolo):ScyWindow{
		return  createColemoWindow(ColemoNode.createColemoNode(roolo));
	}

	public function createColemoWindow(colemoNode:ColemoNode):ScyWindow{
        var colemoWindow = ScyWindow{
			color:Color.YELLOWGREEN
			title:"Colemo-app"
			scyContent: colemoNode
			minimumWidth:320;
			minimumHeight:100;
			//width: 320;
			height: 250;
			cache:true;
      }
		colemoNode.scyWindow = colemoWindow;
		colemoWindow.width = 320;
		return colemoWindow;
	}

function run(){
   Stage {
		title: "Drawing node test"
		width: 250
		height: 250
		scene: Scene {
			content:ColemoNode{
         }

		}
	}



}

/**
 * @author Henrik
 */

// place your code here
