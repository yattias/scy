/*
 * ScyDesktop.fx
 *
 * Created on 26-jun-2009, 12:15:46
 */

package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.WindowManagerImpl;
import javafx.scene.CustomNode;
import javafx.scene.Node;

import eu.scy.client.desktop.scydesktop.missionmap.MissionMap;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModel;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.missionmap.Anchor;
import java.net.URI;

import javafx.scene.Group;

import org.apache.log4j.Logger;

import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreator;
import eu.scy.client.desktop.scydesktop.dummy.DummyEloInfoControl;
import eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler;
import eu.scy.client.desktop.scydesktop.dummy.DummyWindowContentCreator;

import java.lang.IllegalArgumentException;


/**
 * @author sikkenj
 */
var logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.ScyDesktop");

class ScyDesktop extends CustomNode {

   public var missionModel: MissionModel;
   public var eloInfoControl: EloInfoControl;
   public var windowStyler: WindowStyler;
   public var windowContentCreator:WindowContentCreator;

   var windows: WindowManager;

   var scyWindowControl:ScyWindowControl;
   var missionMap: MissionMap;

   function checkProperties(){
      var errors = 0;
      errors += checkIfNull(missionModel,"missionModel");
      errors += checkIfNull(eloInfoControl,"eloInfoControl");
      errors += checkIfNull(windowStyler,"windowStyler");
      errors += checkIfNull(windowContentCreator,"windowContentCreator");
      if (errors>0){
         throw new IllegalArgumentException("One or more properties of ScyDesktop are null");
      }
   }

   function checkIfNull(object:Object,label:String):Integer{
      if (object==null){
         logger.error("ScyDesktop property {label} may not be null");
         return 1;
      }
      return 0;
   }


    function createElements(){
      windows = WindowManagerImpl{
      }
      missionMap = MissionMap{
         missionModel: missionModel
         translateX:40;
         translateY:40;
      }
      scyWindowControl = ScyWindowControl{
          windowContentCreator: windowContentCreator;
          scyDesktop: windows;
          missionModel: missionModel;
          missionMap: missionMap;
          eloInfoControl:eloInfoControl;
          windowStyler:windowStyler;
         //       stage: stage;
         //       forbiddenNodes:[
         //         missionMap,
         //         newWindowButton
         //       ]
          width: bind scene.width;
          height: bind scene.height;
      };
      missionMap.scyWindowControl=scyWindowControl;
    }

    public override function create(): Node {
       logger.info("create");
       checkProperties();
       createElements();
//       return windows.scyWindows;
      Group{
         content:[
            windows.scyWindows,
            missionMap
         ]
      }
    }

 }


function run(){
   InitLog4JFX.initLog4J();
//   InitLog4j.init();
   var anchor0 = Anchor{
       title: "0";
       xPos: -20;
       yPos: 40;
       color: Color.BLUE;
       eloUri: new URI("test://anchor0");
   }
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
   var anchor5 = Anchor{
       title: "5";
       xPos: 100;
       yPos: 40;
       color: Color.ORANGE;
       eloUri: new URI("test://anchor5");
   }
   anchor0.nextAnchors=[anchor1,anchor2,anchor3,anchor4];
   anchor1.nextAnchors=[anchor2,anchor3,anchor4];
   anchor2.nextAnchors=[anchor1,anchor3,anchor4,anchor5];
   anchor3.nextAnchors=[anchor1,anchor2,anchor4];
   anchor4.nextAnchors=[anchor1,anchor2,anchor3,anchor5];

   var missionModel = MissionModel{
       anchors: [anchor0,anchor1,anchor2,anchor3,anchor4,anchor5];
       activeAnchor:anchor0
   }
   var scyDesktop = ScyDesktop{
      missionModel : missionModel;
      eloInfoControl: DummyEloInfoControl{
      };
      windowStyler:DummyWindowStyler{
      };
      windowContentCreator:DummyWindowContentCreator{
      };

   }

   Stage {
      title : "ScyDestop Test"
      scene: Scene {
         width: 400
         height: 300
         content: [
            scyDesktop,
//            MissionMap{
//         missionModel: missionModel
//         translateX:20;
//         translateY:20;
//      }
         ]
      }
   }


}
