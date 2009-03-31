/*
 * EloBrowser2.fx
 *
 * Created on 31-mrt-2009, 17:09:43
 */

package eu.scy.elobrowser.main;

/**
 * @author sikkenj
 */

import eu.scy.elobrowser.awareness.contact.Contact;
import eu.scy.elobrowser.awareness.contact.ContactFrame;
import eu.scy.elobrowser.awareness.contact.ContactWindow;
import eu.scy.elobrowser.awareness.contact.OnlineState;
import eu.scy.elobrowser.awareness.contact.WindowSize;
import eu.scy.elobrowser.main.EdgesManager;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.main.SCYLogin;
import eu.scy.elobrowser.missionmap.MissionMapWindow;
import eu.scy.elobrowser.notification.GrowlFX;
import eu.scy.elobrowser.properties.PropertiesWindow;
import eu.scy.elobrowser.search.SearchWindow;
import eu.scy.elobrowser.tool.chat.ChatNode;
import eu.scy.elobrowser.tool.colemo.*;
import eu.scy.elobrowser.tool.dataProcessTool.DataToolNode;
import eu.scy.elobrowser.tool.displayshelf.*;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.elofactory.DummyScyWindowContentFactory;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentCreator;
import eu.scy.elobrowser.tool.missionmap.Anchor;
import eu.scy.elobrowser.tool.missionmap.MissionMap;
import eu.scy.elobrowser.tool.missionmap.MissionModel;
import eu.scy.elobrowser.tool.simquest.SimQuestNode;
import eu.scy.elobrowser.tool.textpad.TextpadNode;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import eu.scy.scywindows.ScyWindowControl;
import java.net.URI;
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

//def stageWidth = 1024;
//def stageHeight = 768;

var roolo = Roolo.getRoolo();
var stage: Stage;
var scyDesktop = ScyDesktop.getScyDesktop();

var scyWindowContentCreator = ScyWindowContentCreator{
    scyWindowContentFactories: [
        DummyScyWindowContentFactory{
        }
    ];
};



var anchor1 = Anchor{
    title: "1";
    xPos: 20;
    yPos: 20;
    color: Color.BLUE;
    eloUri: new URI("test://anchor1");
}
var anchor2 = Anchor{
    title: "2";
    xPos: 60;
    yPos: 20;
    color: Color.GREEN;
    eloUri: new URI("test://anchor2");
}
var anchor3 = Anchor{
    title: "3";
    xPos: 20;
    yPos: 60;
    color: Color.RED;
    eloUri: new URI("test://anchor3");
}
var anchor4 = Anchor{
    title: "4";
    xPos: 60;
    yPos: 60;
    color: Color.ORANGE;
    eloUri: new URI("test://anchor4");
}
anchor1.nextAnchors=[anchor2,anchor3,anchor4];
anchor2.nextAnchors=[anchor1,anchor3,anchor4];
anchor3.nextAnchors=[anchor1,anchor2,anchor4];
anchor4.nextAnchors=[anchor1,anchor2,anchor3];

var missionModel = MissionModel{
    anchors: [anchor1,anchor2,anchor3,anchor4]
}

var missionMap = MissionMap{
    missionModel: missionModel
    translateX:bind (stage.width-100)/2;
}


var newGroup = VBox {
    translateX: 5
    translateY: 5;
    spacing: 3;
    content: [
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
            text: "SCYMapper"
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

var newScyWindow = ScyWindow{
//    translateX: 300;
    translateX: (stage.width-100)/2;
    translateY: 300;
	opacity: 0.75;
    title: "New"
    color: Color.BLUEVIOLET
    scyContent: newGroup
    allowClose: false;
    allowResize: true;
    allowMinimize: true;
};
newScyWindow.openWindow(150, 230);
scyDesktop.addScyWindow(newScyWindow);


def contactWindow = ContactWindow{
    contacts: bind getContacts();
};

def propertiesWindow: PropertiesWindow = PropertiesWindow{
    width:200;
    height:60;
    translateX :bind( (stage.scene.width - propertiesWindow.width) - contactWindow.translateX);
    translateY : bind contactWindow.translateY;
    //TODO insert content
//    content:
}

def missionMapWindow: MissionMapWindow = MissionMapWindow{
    width:200;
    height:60;
    translateX :bind( (stage.scene.width - missionMapWindow.width) - contactWindow.translateX);
    translateY : bind( (stage.scene.height - searchWindow.height) - contactWindow.translateY);
    //TODO insert MissionMap here (as a Node), then fix the size
//    content:
}

def searchWindow: SearchWindow = SearchWindow{
    width:200;
    height:60;
    translateX :bind(contactWindow.translateX);
    translateY : bind( (stage.scene.height - searchWindow.height) - contactWindow.translateY);
    //TODO insert content
//    content:
}


//eloBrowserControl.openWindow(100, 130);
//scyDesktop.addScyWindow(eloBrowserControl);
insert contactWindow into scyDesktop.desktop.content;
insert searchWindow into scyDesktop.desktop.content;
insert propertiesWindow into scyDesktop.desktop.content;
insert missionMapWindow into scyDesktop.desktop.content;
contactWindow.translateX = 5;
contactWindow.translateY = 5;

var loginGroup = SCYLogin {
    mainContent: [
        scyDesktop.desktop,
        propertiesWindow,
        searchWindow,
        missionMapWindow,
        contactWindow,
       // resultView
        missionMap
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
//        x: 300;
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

def edgesManager: EdgesManager = EdgesManager {
};


stage = Stage {

	title: "SCY Lab (FX)"
	width: 1024;
	height: 768;
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
                    color: Color.web("#bdd1ef")
                },
                Stop {
                    offset: 1
                    color: Color.web("#ebf3ff")
                }
            ]
        }
		content: [
			Group{
				content: [
                    edgesManager
                    loginGroup
                    propertiesWindow
                    searchWindow
                    missionMapWindow
                    contactWindow
                    //resultView
					scyDesktop.desktop
                    missionMap
                    growl
				]
			}
        ]
	}
}

var scyWindowControl = ScyWindowControl{
    scyWindowContentCreator: scyWindowContentCreator;
    scyDesktop: scyDesktop;
    missionModel: missionModel;
    missionMap: missionMap;
    stage: stage;
};
missionMap.scyWindowControl=scyWindowControl;

scyWindowControl.addOtherScyWindow(newScyWindow);

stage;

