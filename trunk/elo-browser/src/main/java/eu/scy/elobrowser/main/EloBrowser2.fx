/*
 * EloBrowser2.fx
 *
 * Created on 31-mrt-2009, 17:09:43
 */

package eu.scy.elobrowser.main;

/**
 * @author sikkenj
 */

import java.lang.System;
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
import eu.scy.elobrowser.tbi_hack.ActiveAnchorTransferer;
import eu.scy.elobrowser.tool.chat.ChatNode;
import eu.scy.elobrowser.tool.colemo.*;
import eu.scy.elobrowser.tool.dataProcessTool.DataProcessToolScyWindowContentFactory;
import eu.scy.elobrowser.tool.dataProcessTool.DataToolNode;
import eu.scy.elobrowser.tool.displayshelf.*;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.drawing.DrawingToolScyWindowContentFactory;
import eu.scy.elobrowser.tool.colemo.ColemoScyWindowContentFactory;
import eu.scy.elobrowser.tool.textpad.TextpadScyWindowContentFactory;
import eu.scy.elobrowser.tool.elofactory.DummyScyWindowContentFactory;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentCreator;
import eu.scy.elobrowser.tool.missionmap.Anchor;
import eu.scy.elobrowser.tool.missionmap.MissionMap;
import eu.scy.elobrowser.tool.missionmap.MissionModel;
import eu.scy.elobrowser.tool.pictureviewer.PictureViewerNode;
import eu.scy.elobrowser.tool.simquest.SimQuestNode;
import eu.scy.elobrowser.tool.simquest.SimQuestScyWindowContentFactory;
import eu.scy.elobrowser.tool.textpad.TextpadNode;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import eu.scy.scywindows.ScyWindowControl;
import eu.scy.scywindows.ScyWindowStyler;
import java.net.URI;
import javafx.ext.swing.SwingButton;
import javafx.geometry.Point2D;
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

def annotatesRelationName = "annotates";
def usesRelationName = "uses";

var roolo = Roolo.getRoolo();
var stage: Stage;
var scyDesktop = ScyDesktop.getScyDesktop();

var scyWindowContentCreator = ScyWindowContentCreator{
    scyWindowContentFactories: [
        DummyScyWindowContentFactory{
        }
        DrawingToolScyWindowContentFactory{
            roolo:roolo;
        }
        SimQuestScyWindowContentFactory{
            roolo:roolo;
        }
        DataProcessToolScyWindowContentFactory{
            roolo:roolo;
        }
        ColemoScyWindowContentFactory{
            roolo:roolo;
        }
        TextpadScyWindowContentFactory{
            roolo:roolo;
        }
    ];
};

var scyWindowStyler = ScyWindowStyler{
    roolo:roolo;
}

var scyWindowControl: ScyWindowControl;

var missionAnchor = Anchor{
    title: "M";
    xPos: 140;
    yPos: 40;
    eloUri: new URI("roolo://memory/21/mission.scydraw");
    relationNames: [annotatesRelationName]
}

var backgroundAnchor = Anchor{
    title: "B";
    xPos: 100;
    yPos: 20;
    color: Color.BLUE;
    eloUri: new URI("test://background.jpg");
    relationNames: [annotatesRelationName]
}

var simulationAnchor = Anchor{
    title: "S";
    xPos: 100;
    yPos: 60;
    color: Color.BLUE;
    eloUri: new URI("roolo://memory/20/review_simulation.scysimconfig");
    relationNames: [annotatesRelationName,usesRelationName]
}

var mappingAnchor = Anchor{
    title: "C";
    xPos: 60;
    yPos: 40;
    color: Color.BLUE;
    eloUri: new URI("test://mapping.scymapping");
    relationNames: [annotatesRelationName]
}

var reportAnchor = Anchor{
    title: "R";
    xPos: 20;
    yPos: 40;
    color: Color.BLUE;
    eloUri: new URI("test://report.jpg");
    relationNames: [annotatesRelationName]
}
missionAnchor.nextAnchors = [backgroundAnchor,simulationAnchor];
backgroundAnchor.nextAnchors = [mappingAnchor];
simulationAnchor.nextAnchors = [mappingAnchor];
mappingAnchor.nextAnchors = [reportAnchor];


var anchor1 = Anchor{
    title: "1";
    xPos: 20;
    yPos: 20;
    color: Color.BLUE;
    eloUri: new URI("roolo://memory/4/crappy_balanc.scydraw");
}
var anchor2 = Anchor{
    title: "2";
    xPos: 60;
    yPos: 20;
    color: Color.GREEN;
    eloUri: new URI("test://anchor2.test");
}
var anchor3 = Anchor{
    title: "3";
    xPos: 20;
    yPos: 60;
    color: Color.RED;
    eloUri: new URI("test://anchor3.test");
}
var anchor4 = Anchor{
    title: "4";
    xPos: 60;
    yPos: 60;
    color: Color.ORANGE;
    eloUri: new URI("test://anchor4.test");
}
anchor1.nextAnchors=[anchor2,anchor3,anchor4];
anchor2.nextAnchors=[anchor1,anchor3,anchor4];
anchor3.nextAnchors=[anchor1,anchor2,anchor4];
anchor4.nextAnchors=[anchor1,anchor2,anchor3];

var missionModel = MissionModel{
//    anchors: [anchor1,anchor2,anchor3,anchor4]
    anchors: [missionAnchor,backgroundAnchor,simulationAnchor,mappingAnchor,reportAnchor]
    activeAnchor:missionAnchor;
}
for (anchor in missionModel.anchors){
    anchor.color = scyWindowStyler.getScyColor(anchor.eloUri);
}

var activeAnchorTransferer = ActiveAnchorTransferer{
    roolo:roolo
    activeAnchor: bind missionModel.activeAnchor
}


var missionMap = MissionMap{
    missionModel: missionModel
    translateX:-10;
    translateY:-10;
    //translateX:bind (stage.width-100)/2;
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
                scyWindowControl.addOtherScyWindow(drawingWindow, true);
            }
        },
        SwingButton{
            text: "Simulator"
            action: function() {
                var simquestWindow = SimQuestNode.createSimQuestWindow(roolo);
                simquestWindow.allowResize;
                scyDesktop.addScyWindow(simquestWindow);
                //simquestWindow.openWindow(491,673);
                scyWindowControl.addOtherScyWindow(simquestWindow, true);
            }
        },
        SwingButton{
            text: "Textpad"
            action: function() {
                var textpadWindow = TextpadNode.createTextpadWindow(roolo);
                textpadWindow.allowResize = true;
                scyDesktop.addScyWindow(textpadWindow);
                textpadWindow.openWindow(263,256);
                scyWindowControl.addOtherScyWindow(textpadWindow, true);
            }
        },
        SwingButton{
            text: "SCYMapper"
            action: function() {
                var colemoWIndow = ColemoNode.createColemoWindow(roolo);
                colemoWIndow.allowResize = true;
                scyDesktop.addScyWindow(colemoWIndow);
                colemoWIndow.openWindow(600,300);
                scyWindowControl.addOtherScyWindow(colemoWIndow, true);
            }
        },
        SwingButton{
            text: "BuddyList"
            action: function() {
                var chatWindow = ChatNode.createChatWindow(roolo);
                chatWindow.allowResize = true;
                scyDesktop.addScyWindow(chatWindow);
                chatWindow.openWindow(323,289);
                scyWindowControl.addOtherScyWindow(chatWindow, true);
            }
        },
        SwingButton{
            text: "Data Process Tool"
            action: function() {
                var dataToolWindow = DataToolNode.createDataToolWindow(roolo);
                scyDesktop.addScyWindow(dataToolWindow);
                dataToolWindow.allowResize = true;
                dataToolWindow.openWindow(300,600);
                scyWindowControl.addOtherScyWindow(dataToolWindow, true);
            }
        },
        SwingButton{
            text: "Picture Viewer"
            action: function() {
                var picViewWindow = PictureViewerNode.createPictureViewerWindow(roolo);
                scyDesktop.addScyWindow(picViewWindow);
                picViewWindow.allowResize = true;
                scyWindowControl.addOtherScyWindow(picViewWindow, true);
            }
        },
        SwingButton{
            text: "OSLO picture viewer"
            action: function() {
                var shelfWindow = DisplayShelf.createScyDisplayShelf(roolo);
                scyDesktop.addScyWindow(shelfWindow);
                shelfWindow.allowResize = false;
                shelfWindow.openWindow(600,300);
                scyWindowControl.addOtherScyWindow(shelfWindow, true);
            }
        }
    ]
}

var newScyWindow = ScyWindow{
//    translateX: 300;
    translateX: 30;
    translateY: 300;
	opacity: 0.75;
    title: "New"
    color: Color.BLUEVIOLET
    scyContent: newGroup
    allowClose: false;
    allowResize: true;
    allowMinimize: true;
};
newScyWindow.openWindow(150, 300);
scyDesktop.addScyWindow(newScyWindow);


def contactWindow = ContactWindow{
    scyDesktop:this.scyDesktop;
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
    width:160;
    height:80;
    translateX :bind( (stage.scene.width - missionMapWindow.width) - contactWindow.translateX);
    translateY : bind( (stage.scene.height - missionMapWindow.height) - contactWindow.translateY);
    //TODO insert MissionMap here (as a Node), then fix the size
    content:missionMap
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

//
//def changeOnlineState = SwingButton {
//    translateX:200;
//    translateY:400;
//    text: "change online state";
//    action: function() {
//        System.out.println("sizeof visibleContacts(before): { sizeof contactWindow.visibleContacts}");
//        contactWindow.contacts[2].contact.onlineState = OnlineState.AWAY;
//        contactWindow.contacts[1].contact.onlineState = OnlineState.ONLINE;
//        contactWindow.actualizePositions();
//        contactWindow.frameResize();
//        System.out.println("sizeof visible after lablalba: { sizeof contactWindow.visibleContacts}");
//    }
//
//}
//insert changeOnlineState into scyDesktop.desktop.content;

def changeOnlineState = SwingButton {
    translateX:200;
    translateY:400;
    text: "change online state";
    action: function() {
        System.out.println("sizeof visibleContacts(before): { sizeof contactWindow.visibleContacts}");
        contactWindow.contacts[2].contact.onlineState = OnlineState.AWAY;
        contactWindow.contacts[1].contact.onlineState = OnlineState.ONLINE;
        contactWindow.actualizePositions();
        contactWindow.frameResize();
        System.out.println("sizeof visible after lablalba: { sizeof contactWindow.visibleContacts}");
    }

}
insert changeOnlineState into scyDesktop.desktop.content;
var loginGroup = SCYLogin {
    mainContent: [
        scyDesktop.desktop,
        propertiesWindow,
        searchWindow,
        missionMapWindow,
        contactWindow,
//        changeOnlineState,
       // resultView
        //missionMap
    ]
}



def growl = GrowlFX {
    translateX: bind (stage.width / 2) - 200;
    translateY: bind stage.height - 120;
    opacity: 0;
    //TODO Check this out, if initially false?
//    visible: false;
}

function getContacts():ContactFrame[]{
    def contact1 = ContactFrame{
        size: WindowSize.NORMAL;
        contact: Contact{
            currentMission: "Testmission";
            imageURL: "img/Giemza.png";
            name: "Adam G.";
            onlineState: OnlineState.ONLINE;
            progress: 0.78;
        };
    };

    def contact2 = ContactFrame{
        size: WindowSize.NORMAL;
        contact: Contact{
            currentMission: "Another Mission";
            imageURL: "img/Wouter.png";
            name: "Wouter v. J.";
            onlineState: OnlineState.ONLINE;
            progress: 0.745;
        };
//        x: 300;
    };

    def contact3 = ContactFrame{
        size: WindowSize.NORMAL;
        contact: Contact{
            currentMission: "Testmission";
            imageURL: "img/Lazonder.png";
            name: "Ard L.";
            onlineState: OnlineState.AWAY;
            progress: 0.7;
        };

    };

    def contact4 = ContactFrame{
        size: WindowSize.NORMAL;
        contact: Contact{
            currentMission: "Testmission";
            imageURL: "img/Wasson.png";
            name: "Barbara W.";
            onlineState: OnlineState.ONLINE;
            progress: 0.5;
        };

    };

    def contact5 = ContactFrame{
        size: WindowSize.NORMAL;
        contact: Contact{
            currentMission: "Testmission";
            imageURL: "img/Lejeune.png";
            name: "Anne L.";
            onlineState: OnlineState.ONLINE;
            progress: 0.2;
        };

    };

    def contact6 = ContactFrame {
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission";
                imageURL: "img/Kluge.png";
                name: "Anders K.";
                onlineState: OnlineState.ONLINE;
                progress: 0.01;
        };
    }

    def contact7 = ContactFrame {
            size: WindowSize.NORMAL;
            contact: Contact{
                currentMission: "Testmission";
                imageURL: "img/DHam.png";
                name: "Cedric D.";
                onlineState: OnlineState.ONLINE;
                progress: 0.4;
        };
    }

    return ([contact1,contact2,contact4,contact5, contact6] as ContactFrame[]);
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
                   // missionMap
                    growl,
				]
			}
        ]
	}
}

scyWindowControl = ScyWindowControl{
    scyWindowContentCreator: scyWindowContentCreator;
    scyDesktop: scyDesktop;
    missionModel: missionModel;
    missionMap: missionMap;
    stage: stage;
    scyWindowStyler:scyWindowStyler;
    roolo: roolo;
    edgesManager: edgesManager;
    forbiddenNodes:[
        contactWindow,
        propertiesWindow,
        missionMapWindow,
        searchWindow
    ];
    width: bind stage.scene.width;
    height: bind stage.scene.height;
//    size: bind Point2D{
//        x: stage.scene.width
//        y: stage.scene.height
//        };
};
missionMap.scyWindowControl=scyWindowControl;
activeAnchorTransferer.eloSavedAction = scyWindowControl.newEloSaved;

scyWindowControl.addOtherScyWindow(newScyWindow);
scyWindowControl.positionWindows();

stage;

