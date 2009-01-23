/*
 * DataToolNode.fx
 *
 * Created on 12-jan-2008, 21:46:32
 */

package eu.scy.elobrowser.tool.dataProcessTool;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.dataProcessTool.EloDataToolWrapper;
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
 * @author marjolaine bodin
 */

 // place your code here
public class DataToolNode extends CustomNode {
   var dataToolPanel:DataToolPanel;
   var eloDataToolWrapper:EloDataToolWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public function loadElo(uri:URI){
      eloDataToolWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Process Dataset: {eloDataToolWrapper.getDocName()}";
		var eloUri = eloDataToolWrapper.getEloUri();
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
                              eloDataToolWrapper.newDataProcessAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Load"
                           clickAction:function( e: MouseEvent ):Void {
                              eloDataToolWrapper.loadDataProcessAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Save"
                           clickAction:function( e: MouseEvent ):Void {
                              eloDataToolWrapper.saveDataProcessAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Save as"
                           clickAction:function( e: MouseEvent ):Void {
                              eloDataToolWrapper.saveAsDataProcessAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  SwingComponent.wrap(dataToolPanel)
               ]
            }
         ]
      };
   }
}

	class CommandText extends CustomNode  {
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

	public function createDataToolNode(roolo:Roolo):DataToolNode{
		var dataToolPanel= new DataToolPanel();
		var eloDataToolWrapper= new EloDataToolWrapper(dataToolPanel);
		eloDataToolWrapper.setRepository(roolo.repository);
		eloDataToolWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
		eloDataToolWrapper.setEloFactory(roolo.eloFactory);
		return DataToolNode{
			dataToolPanel:dataToolPanel;
			eloDataToolWrapper:eloDataToolWrapper;
		}
	}

	public function createDataToolWindow(roolo:Roolo):ScyWindow{
		return
		createDataToolWindow(DataToolNode.createDataToolNode(roolo));
	}

	public function createDataToolWindow(dataToolNode:DataToolNode):ScyWindow{
		var dataToolWindow = ScyWindow{
			color:Color.GREEN;
			title:"Data Process Visualization Tool"
			scyContent: dataToolNode
			minimumWidth:320;
			minimumHeight:100;
			//width: 320;
			height: 650;
			cache:true;
      }
		dataToolNode.scyWindow = dataToolWindow;
		dataToolWindow.width = 850;
		return dataToolWindow;
	}

function run(){
   Stage {
		title: "Data Process node test"
		width: 250
		height: 250
		scene: Scene {
			content:DataToolNode{
         }

		}
	}

}
