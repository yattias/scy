/*
 * EloBrowser.fx
 *
 * Created on 17-dec-2008, 10:06:44
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.awareness.contact.OnlineState;
import eu.scy.elobrowser.awareness.contact.WindowSize;
import eu.scy.elobrowser.main.EloSpecWidget;
import eu.scy.elobrowser.main.MetadataDisplayMappingWidget;
import eu.scy.elobrowser.main.ResultView;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.main.SCYLogin;
import eu.scy.elobrowser.notification.GrowlFX;
import eu.scy.elobrowser.tool.chat.ChatNode;
import eu.scy.elobrowser.tool.colemo.*;
import eu.scy.elobrowser.awareness.contact.Contact;
import eu.scy.elobrowser.awareness.contact.ContactFrame;
import eu.scy.elobrowser.awareness.contact.ContactWindow;
import eu.scy.elobrowser.tool.dataProcessTool.DataToolNode;
import eu.scy.elobrowser.tool.displayshelf.*;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.simquest.SimQuestNode;
import eu.scy.elobrowser.tool.textpad.TextpadNode;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
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
        SwingButton{
            text: "Drawing"
            action: function() {
                var drawingWindow = DrawingNode.createDrawingWindow(roolo);
                scyDesktop.addScyWindow(drawingWindow);
                drawingWindow.openWindow(414, 377);
            }
        }
        SwingButton{
            text: "Simulator"
            action: function() {
                var simquestWindow = SimQuestNode.createSimQuestWindow(roolo);
                simquestWindow.allowResize;
                scyDesktop.addScyWindow(simquestWindow);
                //simquestWindow.openWindow(491,673);
            }
        },
        SwingButton{
            text: "Textpad"
            action: function() {
                var textpadWindow = TextpadNode.createTextpadWindow(roolo);
                textpadWindow.allowResize = true;
                scyDesktop.addScyWindow(textpadWindow);
                textpadWindow.openWindow(263,256);
            }
        },
        SwingButton{
            text: "Cool(emo)"
            action: function() {
                var colemoWIndow = ColemoNode.createColemoWindow(roolo);
                colemoWIndow.allowResize = true;
                scyDesktop.addScyWindow(colemoWIndow);
                colemoWIndow.openWindow(600,300);
            }
        }

        SwingButton{
            text: "BuddyList"
            action: function() {
                var chatWindow = ChatNode.createChatWindow(roolo);
                chatWindow.allowResize = true;
                scyDesktop.addScyWindow(chatWindow);
                chatWindow.openWindow(323,289);
            }
        }
        SwingButton{
            text: "Data Process Tool"
            action: function() {
                var dataToolWindow = DataToolNode.createDataToolWindow(roolo);
                scyDesktop.addScyWindow(dataToolWindow);
                dataToolWindow.allowResize = true;
                dataToolWindow.openWindow(300,600);
            }
        }

         SwingButton{
            text: "OSLO picture viewer"
            action: function() {
                var shelfWindow = DisplayShelf.createScyDisplayShelf(roolo);
                scyDesktop.addScyWindow(shelfWindow);
                shelfWindow.allowResize = false;
                shelfWindow.openWindow(600,300);
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
    allowResize:true;
    allowMinimize:true;
   };
newScyWindow.openWindow(150, 230);
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
        stage.width - 200 as Integer;
    ySize: bind
        stage.height - 150 as Integer;
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
 def contactWindow = ContactWindow{
            contacts: bind getContacts();
        };
        
eloBrowserControl.openWindow(100, 130);
scyDesktop.addScyWindow(eloBrowserControl);
insert contactWindow into scyDesktop.desktop.content;
contactWindow.translateX = 5;
contactWindow.translateY = 5;

var loginGroup = SCYLogin {
    mainContent: [
        scyDesktop.desktop,
        contactWindow,
        resultView
    ]
}



var growl = GrowlFX {
    translateX: bind (stage.width / 2) - 200;
    translateY: bind stage.height - 120;
    opacity: 0;
}


 function getContacts():ContactFrame[]{
           def contact1 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission"; 
                imageURL: "img/Manske.jpg";
                name: "Sven Manske";
                onlineState: OnlineState.ONLINE;
                progress: 1.0;
            };

        };

        def contact2 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Another Mission";
                imageURL: "img/Giemza.jpg";
                name: "Adam G";
                onlineState: OnlineState.AWAY;
                progress: 0.1;
            };
            x: 300;

        };

        def contact3 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission";
                imageURL: "img/Weinbrenner.jpg";
                name: "Stefan W.";
                onlineState: OnlineState.ONLINE;
                progress: 0.7;
            };

        };

        def contact4 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission";
                imageURL: "img/Gerling.jpg";
                name: "Philip G.";
                onlineState: OnlineState.ONLINE;
                progress: 0.5;
            };

        };

        def contact5 = ContactFrame{
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission";
                imageURL: "img/Calendar.png";
                name: "Sascha N.";
                onlineState: OnlineState.OFFLINE;
                progress: 0.3;
            };

        };

        return ([contact1,contact5,contact2,contact3,contact4] as ContactFrame[]);
    };


stage = Stage {

	title: "SCY Lab (FX)"
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
                    contactWindow
                    resultView
					scyDesktop.desktop
                    growl
				]
			}
		]

	}
}
