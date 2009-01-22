/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.elobrowser.tool.simquest;

import sqv.SimQuestViewer;
import eu.scy.elobrowser.main.Roolo;
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
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.lang.System;
import eu.scy.elobrowser.tool.simquest.DataCollector;
import java.awt.BorderLayout;
import utils.FileName;

/**
 * @author sikkenj
 */

 // place your code here
public class SimQuestNode extends CustomNode {
   var simquestPanel:JPanel;
   var eloSimQuestWrapper:EloSimQuestWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public function loadElo(uri:URI){
      eloSimQuestWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Dataset: {eloSimQuestWrapper.getDocName()}";
		var eloUri = eloSimQuestWrapper.getEloUri();
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
                              eloSimQuestWrapper.newDrawingAction();
										setScyWindowTitle();
                           }
                        //}
                        //CommandText{
                        //   label:"Load"
                        //   clickAction:function( e: MouseEvent ):Void {
                        //      eloSimQuestWrapper.loadDrawingAction();
						//				setScyWindowTitle();
                        //   }
                        }
                        CommandText{
                           label:"Save"
                           clickAction:function( e: MouseEvent ):Void {
                              eloSimQuestWrapper.saveDrawingAction();
										setScyWindowTitle();
                           }
                        }
                        CommandText{
                           label:"Save as"
                           clickAction:function( e: MouseEvent ):Void {
                              eloSimQuestWrapper.saveAsDrawingAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  //SwingComponent.wrap(simquestViewer.getUIasJPanel())
                  //SwingComponent.wrap(new JLabel("blabla"))
                  SwingComponent.wrap(simquestPanel)
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

	public function createSimQuestNode(roolo:Roolo):SimQuestNode{
		var simquestViewer = new SimQuestViewer();
        //var fileName = new FileName("E:/netbeans-workspaces/elo-browser/tools/simquestviewer/src/main/resources/balance.sqzx");
		var fileName = new FileName("src/main/java/eu/scy/elobrowser/tool/simquest/balance.sqzx");
        System.out.println("SimQuestNode.createSimQuestNode(). trying to load: {fileName.toURI().getPath().toString()}");
		simquestViewer.setFile(fileName.toURI());
        simquestViewer.createFrame(false);
        simquestViewer.run();

        var dataCollector = new DataCollector(simquestViewer);

        var simquestPanel = new JPanel();
        simquestPanel.setLayout(new BorderLayout());
        
        simquestPanel.add(dataCollector, BorderLayout.SOUTH);

        System.out.println(simquestViewer.getInterfacePanel().getSize());
        System.out.println(simquestViewer.getInterfacePanel().getPreferredSize());

        // TODO: infering correct dimension rather than guessing
        simquestViewer.getInterfacePanel().setMinimumSize(new Dimension(450,450));
        simquestPanel.add(simquestViewer.getInterfacePanel(), BorderLayout.CENTER);

        var eloSimQuestWrapper = new EloSimQuestWrapper(dataCollector);
		eloSimQuestWrapper.setRepository(roolo.repository);
		eloSimQuestWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
		eloSimQuestWrapper.setEloFactory(roolo.eloFactory);
		
        return SimQuestNode{
			simquestPanel:simquestPanel;
			eloSimQuestWrapper:eloSimQuestWrapper;
		}
	}

	public function createSimQuestWindow(roolo:Roolo):ScyWindow{
		return
		createSimQuestWindow(SimQuestNode.createSimQuestNode(roolo));
	}

	public function createSimQuestWindow(simquestNode:SimQuestNode):ScyWindow{
		var simquestWindow = ScyWindow{
            translateX: 10
			color:Color.GREEN
			title:"SimQuest"
			scyContent: simquestNode
			minimumWidth:320;
			minimumHeight:100;
			width: 500;
			height: 680;
			cache:true;
      }
		simquestNode.scyWindow = simquestWindow;
		return simquestWindow;
	}

function run(){
   Stage {
		title: "SimQuest node test"
		width: 250
		height: 250
		scene: Scene {
			content:SimQuestNode{
         }

		}
	}

}
