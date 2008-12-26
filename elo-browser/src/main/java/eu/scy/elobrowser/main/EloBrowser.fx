/*
 * EloBrowser.fx
 *
 * Created on 17-dec-2008, 10:06:44
 */

package eu.scy.elobrowser.main;

import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.client.tools.drawing.EloDrawingPanel;
import eu.scy.elobrowser.main.EloSpecWidget;
import eu.scy.elobrowser.main.MetadataDisplayMappingWidget;
import eu.scy.elobrowser.main.ResultView;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import java.awt.Dimension;
import java.lang.System;
import javafx.ext.swing.SwingButton;
import javafx.ext.swing.SwingComponent;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
   };

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

scyDesktop.addScyWindow(queryWindow);
scyDesktop.hideScyWindow(queryWindow);

var metadataDisplayMappingWindow = ScyWindow{
   title:"Display mapping"
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

scyDesktop.addScyWindow(metadataDisplayMappingWindow);
scyDesktop.hideScyWindow(metadataDisplayMappingWindow);

var eloBrowserControl= ScyWindow{
   translateX:10;
   translateY:10;
   title:"Search"
   color:Color.BLUE;
   allowClose:false;
   allowResize:false;
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
scyDesktop.addScyWindow(eloBrowserControl);

stage = Stage {

	title: "ELO browser"
	width: 800
	height: 600
	scene: Scene {
		content: [
			Group{
				content:[
               resultView
					scyDesktop.desktop
				]
			}
		]

	}
}
