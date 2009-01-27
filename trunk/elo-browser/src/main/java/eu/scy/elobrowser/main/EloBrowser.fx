/*
 * EloBrowser.fx
 *
 * Created on 17-dec-2008, 10:06:44
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.EloSpecWidget;
import eu.scy.elobrowser.main.MetadataDisplayMappingWidget;
import eu.scy.elobrowser.main.ResultView;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.dataProcessTool.DataToolNode;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.simquest.SimQuestNode;
import eu.scy.elobrowser.tool.textpad.TextpadNode;
import eu.scy.elobrowser.tool.chat.ChatNode;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import eu.scy.elobrowser.notification.GrowlFX;
import java.lang.System;
import javafx.ext.swing.SwingButton;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 * @author sikkenj
 */


var roolo= Roolo.getRoolo();
var stage:Stage;
var scyDesktop = ScyDesktop.getScyDesktop();

var newGroup = VBox {
    translateX:5
    translateY:5;
    spacing:3;
    content:[
        //      SwingButton{
        //         text: "Drw"
        //         action: function() {
        //            var newWhiteboard = new WhiteboardPanel();
        //				var preferredSize = new Dimension(200,200);
        //				newWhiteboard.setMinimumSize(preferredSize);
        //				newWhiteboard.setMaximumSize(preferredSize);
        //				newWhiteboard.setPreferredSize(preferredSize);
        //            var newWhiteboardNode = SwingComponent.wrap(newWhiteboard);
        //            var drawingWindow = ScyWindow{
        //               color:Color.GREEN
        //               title:"Drawing"
        //               scyContent: newWhiteboardNode
        //               minimumWidth:320;
        //               minimumHeigth:100;
        //               width: 320;
        //               height: 150;
        //               }
        //            scyDesktop.addScyWindow(drawingWindow)
        //         }
        //      }
        SwingButton{
            text: "Drawing"
            action: function() {
				//            var newWhiteboard =
				//            roolo.getSpringBean("drawingTool") as EloDrawingPanel;
                //            var whiteboard = new WhiteboardPanel();
                //            var panel = new JPanel();
                //            panel.setLayout(new GridLayout(1,1,0,0));
                //            panel.add(whiteboard);
                //				var newWhiteboard = new EloDrawingPanel();
                //				newWhiteboard.setRepository(roolo.repository);
                //				newWhiteboard.setMetadataTypeManager(roolo.metadataTypeManager);
                //				newWhiteboard.setEloFactory(roolo.eloFactory);
                //				var preferredSize = new Dimension(2000,2000);
                //            //				newWhiteboard.setMinimumSize(preferredSize);
                //            //				newWhiteboard.setMaximumSize(preferredSize);
                //				newWhiteboard.setPreferredSize(preferredSize);
                //            var newWhiteboardNode = SwingComponent.wrap(newWhiteboard);
                //				var drawingNode = DrawingNode.createDrawingNode(roolo);
                //            var drawingWindow = ScyWindow{
                //               color:Color.GREEN
                //               title:"Drawing"
                //               scyContent: drawingNode
                //               minimumWidth:320;
                //               minimumHeigth:100;
                //               width: 320;
                //               height: 150;
                //					cache:true;
                //               }
                //				drawingNode.scyWindow = drawingWindow;
                var drawingWindow = DrawingNode.createDrawingWindow(roolo);
                scyDesktop.addScyWindow(drawingWindow)
            }
        }
        SwingButton{
            text: "Simulator"
            action: function() {
                var simquestWindow = SimQuestNode.createSimQuestWindow(roolo);
                simquestWindow.allowResize;
                scyDesktop.addScyWindow(simquestWindow)
            }
        },
        SwingButton{
            text: "Textpad"
            action: function() {
                var textpadWindow = TextpadNode.createTextpadWindow(roolo);
                textpadWindow.allowResize = true;
                scyDesktop.addScyWindow(textpadWindow);
                textpadWindow.openWindow(300,100);
            }
        }
        SwingButton{
            text: "ChatPad"
            action: function() {
                var chatWindow = ChatNode.createChatWindow(roolo);
                chatWindow.allowResize = true;
                scyDesktop.addScyWindow(chatWindow);
                chatWindow.openWindow(300,300);
            }
        }
        //         SwingButton{
        //            translateY:25
        //            text: "Water"
        //            action: function() {
        //               var drawingWindow = ScyWindow{
        //                  color:Color.BLUE
        //                  title:"Water"
        //                  scyContent: ImageView{
        //                     image:image1
        //                  }
        //               }
        //               scyDesktopMain.addScyWindow(drawingWindow)
        //            }
        //         }
        //         SwingButton{
        //            translateY:50
        //            text: "Sun"
        //            action: function() {
        //               var drawingWindow = ScyWindow{
        //                  color:Color.RED
        //                  title:"Sun"
        //                  scyContent: ImageView{
        //                     image:image2
        //                  }
        //               }
        //               scyDesktopMain.addScyWindow(drawingWindow)
        //            }
        //         }
        SwingButton{
            text: "Data Process Tool"
            action: function() {
                var dataToolWindow = DataToolNode.createDataToolWindow(roolo);
                //dataToolWindow.allowResize;
                scyDesktop.addScyWindow(dataToolWindow);
                dataToolWindow.openWindow(711,568);
            }
        }
    ]
   }

var newScyWindow= ScyWindow{
    translateX:10;
    translateY:150;
	opacity:0.75;
    title:"New"
    color:Color.BLUEVIOLET
    scyContent:newGroup
    allowClose:false;
    allowResize:false;
    allowMinimize:true;
   };
newScyWindow.openWindow(150, 190);
scyDesktop.addScyWindow(newScyWindow);

var queryEntry1 = EloSpecWidget{
    roolo: roolo;
    title: "Query";
}

var metadataDisplayMappingWidget = MetadataDisplayMappingWidget{
    roolo: roolo;
}

var resultView = ResultView{
    roolo:roolo;
    xSize: bind
        stage.width - 110 as Integer;
    ySize: bind
        stage.height - 130 as Integer;
}

function doSearch(){
    var query1 = queryEntry1.getSearchQuery();
    System.out.println("Query 1: {query1.toString()}");
    var displayEloMappings = roolo.queryToElosDisplay.getDisplayEloMapping(metadataDisplayMappingWidget.getMappingElo(),query1);
   System.out.println("Query 1: {query1.toString()}\nNr of elos {displayEloMappings.size()}");
   resultView.newDisplayEloMappings(displayEloMappings);
}

var searchButton = SwingButton{
    text: "Search"
    action: function()  {
      doSearch();
    }
}

var queryWindow = ScyWindow{
    title:"Query"
    eloType:"Search"
    color:Color.BLUE;
    allowClose:true;
    closeIsHide:true;
    allowResize:false;
    width:270;
    height:160;
    scyContent: Group{
        content:[queryEntry1]
    }
}
queryWindow.openWindow(270, 160);

scyDesktop.addScyWindow(queryWindow);
scyDesktop.hideScyWindow(queryWindow);

var metadataDisplayMappingWindow = ScyWindow{
    title:"Display mapping"
    eloType:"Search"
    color:Color.BLUE;
    allowClose:true;
    closeIsHide:true;
    allowResize:false;
    width:188;
    height:223;
    scyContent: Group{
        content:[metadataDisplayMappingWidget]
    }
}
metadataDisplayMappingWindow.openWindow(188, 223);
scyDesktop.addScyWindow(metadataDisplayMappingWindow);
scyDesktop.hideScyWindow(metadataDisplayMappingWindow);

var eloBrowserControl= ScyWindow{
    translateX:10;
    translateY:10;
    title:"Search"
    eloType:"Search"
    color:Color.BLUE;
    allowClose:false;
    allowResize:false;
    allowMinimize:true;
    width:100;
    height:130;
	opacity:0.75;
    scyContent: VBox{
        translateX:5
        translateY:5;
        spacing:3;
        content:[
            SwingButton {
                text: "Search"
                action: function() {
               doSearch();
                }
            }
            SwingButton {
                text: "Query"
                action: function() {
               scyDesktop.activateScyWindow(queryWindow);
                }
            }
            SwingButton {
                text: "Mapping"
                action: function() {
               scyDesktop.activateScyWindow(metadataDisplayMappingWindow);
                }
            }
        ]
    }
}
eloBrowserControl.openWindow(100, 130);
scyDesktop.addScyWindow(eloBrowserControl);
var loginGroup = SCYLogin {
    mainContent: [
        scyDesktop.desktop,
        resultView
    ]
}

var growl = GrowlFX {
    translateX: bind (stage.width / 2) - 200;
    translateY: bind stage.height - 120;
    opacity: 0;
}


stage = Stage {

	title: "ELO browser"
	width: 1024
	height: 768
	scene: Scene {
        fill: RadialGradient {
            centerX: 1200
            centerY: -700
            radius: 1300
            focusX: 800
            focusY: 600
            proportional: false
            stops: [
                Stop {
                    offset: 0
                    color: Color.web("#bdd1ef")},
                Stop {
                    offset: 1
                    color: Color.web("#ebf3ff")}
            ]
        }
		content: [
			Group{
				content:[
                    loginGroup
                    resultView
					scyDesktop.desktop
                    growl
				]
			}
		]

	}
}
