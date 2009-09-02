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
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.missionmap.AnchorFX;
import java.net.URI;

import javafx.scene.Group;

import org.apache.log4j.Logger;

import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFXImpl;
import eu.scy.client.desktop.scydesktop.dummy.DummyEloInfoControl;
import eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler;
import eu.scy.client.desktop.scydesktop.corners.Corner;
import eu.scy.client.desktop.scydesktop.corners.TopLeftCorner;
import eu.scy.client.desktop.scydesktop.corners.TopRightCorner;
import eu.scy.client.desktop.scydesktop.corners.BottomRightCorner;
import eu.scy.client.desktop.scydesktop.corners.BottomLeftCorner;

import java.lang.IllegalArgumentException;
import javafx.scene.control.Button;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentFactory;

import eu.scy.client.desktop.scydesktop.config.Config;

import eu.scy.client.desktop.scydesktop.config.SpringConfigFactory;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistryImpl;

import eu.scy.client.desktop.scydesktop.test.SwingSizeTestPanelCreator;


/**
 * @author sikkenj
 */
var logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.ScyDesktop");

public class ScyDesktop extends CustomNode {

   public var config:Config;

   public var missionModelFX: MissionModelFX= MissionModelFX{};
   public var eloInfoControl: EloInfoControl;
   public var windowStyler: WindowStyler;
   public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
   public var newEloCreationRegistry:NewEloCreationRegistry;

   public var topLeftCornerTool: Node on replace{topLeftCorner.content = topLeftCornerTool};
   public var topRightCornerTool: Node on replace{topRightCorner.content = topRightCornerTool};
   public var bottomRightCornerTool: Node; // TODO, still hard coded to missionMap
   public var bottomLeftCornerTool: Node on replace{bottomLeftCorner.content = bottomLeftCornerTool};

   var windows: WindowManager;

   var windowContentFactory: WindowContentFactory;
   var scyWindowControl:ScyWindowControl;
   var missionMap: MissionMap;

   var topLeftCorner:Corner;
   var topRightCorner:Corner;
   var bottomRightCorner:Corner;
   var bottomLeftCorner:Corner;

   init{
      FX.deferAction(initialWindowPositioning);
   }

   function initialWindowPositioning(){
      scyWindowControl.positionWindows();
   }

   function checkProperties(){
      var errors = 0;
      errors += checkIfNull(config,"config");
      errors += checkIfNull(missionModelFX,"missionModel");
      errors += checkIfNull(eloInfoControl,"eloInfoControl");
      errors += checkIfNull(windowStyler,"windowStyler");
      errors += checkIfNull(windowContentCreatorRegistryFX,"windowContentCreatorRegistryFX");
      errors += checkIfNull(newEloCreationRegistry,"newEloCreationRegistry");
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
      windowContentFactory = WindowContentFactory{
         windowContentCreatorRegistryFX:windowContentCreatorRegistryFX;
         config:config;
      }

      missionMap = MissionMap{
         missionModel: missionModelFX
         translateX:40;
         translateY:40;
      }
      missionMap.scyWindowControl=scyWindowControl;
      topLeftCorner = TopLeftCorner{
         content:topLeftCornerTool;
         color:Color.RED;
      }
      topRightCorner = TopRightCorner{
         content:topRightCornerTool;
         color:Color.GREEN;
      }
      bottomRightCorner = BottomRightCorner{
         // TODO, replace with specified tool
         content:missionMap;
         color:Color.BLUE;
      }
      bottomLeftCorner = BottomLeftCorner{
         content:bottomLeftCornerTool;
         color:Color.GRAY;
      }
      scyWindowControl = ScyWindowControl{
          windowContentFactory: windowContentFactory;
          scyDesktop: windows;
          missionModel: missionModelFX;
          missionMap: missionMap;
          eloInfoControl:eloInfoControl;
          windowStyler:windowStyler;
          forbiddenNodes:[
               topLeftCorner,
               topRightCorner,
               bottomRightCorner,
               bottomLeftCorner
            ];
          width: bind scene.width;
          height: bind scene.height;
      };

    }

    public override function create(): Node {
       logger.info("create");
       checkProperties();
       createElements();
      Group{
         content:[
            windows.scyWindows,
            topLeftCorner,
            topRightCorner,
            bottomRightCorner,
            bottomLeftCorner
         ]
      }
    }

    public function addScyWindow(window:ScyWindow){
       if (window.scyContent==null and window.setScyContent==null){
          window.setScyContent=fillNewScyWindow
       }

      windowStyler.style(window);
      windows.addScyWindow(window);
      scyWindowControl.addOtherScyWindow(window);
      scyWindowControl.positionWindows(true);
    }

    function fillNewScyWindow(window: ScyWindow):Void{
      windowContentFactory.fillWindowContent(window);
    }


 }


function run(){
   InitLog4JFX.initLog4J();

   var springConfigFactory = new SpringConfigFactory();
   springConfigFactory.initFromClassPath("config/scyDesktopTestConfig.xml");
   var config = springConfigFactory.getConfig();

//   InitLog4j.init();
   var anchor0 = AnchorFX{
       title: "0";
       xPos: 00;
       yPos: 20;
       color: Color.BLUE;
       eloUri: new URI("test://anchor0.tst");
   }
   var anchor1 = AnchorFX{
       title: "1";
       xPos: 40;
       yPos: 00;
       color: Color.BLUE;
       eloUri: new URI("test://anchor1.tst");
   }
   var anchor2 = AnchorFX{
       title: "2";
       xPos: 80;
       yPos: 00;
       color: Color.GREEN;
       eloUri: new URI("test://anchor2.tst");
   }
   var anchor3 = AnchorFX{
       title: "3";
       xPos: 40;
       yPos: 40;
       color: Color.RED;
       eloUri: new URI("test://anchor3.tst");
   }
   var anchor4 = AnchorFX{
       title: "4";
       xPos: 80;
       yPos: 40;
       color: Color.ORANGE;
       eloUri: new URI("test://anchor4.tst");
   }
   var anchor5 = AnchorFX{
       title: "5";
       xPos: 120;
       yPos: 20;
       color: Color.ORANGE;
       eloUri: new URI("test://anchor5.tst");
   }
   anchor0.nextAnchors=[anchor1,anchor2,anchor3,anchor4];
   anchor1.nextAnchors=[anchor2,anchor3,anchor4];
   anchor2.nextAnchors=[anchor1,anchor3,anchor4,anchor5];
   anchor3.nextAnchors=[anchor1,anchor2,anchor4];
   anchor4.nextAnchors=[anchor1,anchor2,anchor3,anchor5];

   var missionModel = MissionModelFX{
       anchors: [anchor0,anchor1,anchor2,anchor3,anchor4,anchor5];
       activeAnchor:anchor0
   }
   missionModel = MissionModelFX{
       anchors: [];
   }
   var newWindowCounter = 0;
   var newWindowButton:Button = Button {
         text: "New Window"
         action: function() {
            var title = "new_{++newWindowCounter}";
            var window:ScyWindow = StandardScyWindow{
               title:title
               id:"new://{title}"
              allowClose: true;
              allowResize: true;
              allowRotate: true;
              allowMinimize: true;
            }
            scyDesktop.addScyWindow(window);
         }
      }

   var newScyWindowTool = NewScyWindowTool{

   }

   var windowContentCreatorRegistryFX:WindowContentCreatorRegistryFX =WindowContentCreatorRegistryFXImpl{
         };

   windowContentCreatorRegistryFX.registerWindowContentCreator(new SwingSizeTestPanelCreator(), "size");
   var scyDesktop:ScyDesktop = ScyDesktop{
      config:config;
      missionModelFX : missionModel;
      eloInfoControl: DummyEloInfoControl{
      };
      windowStyler:DummyWindowStyler{
      };
      windowContentCreatorRegistryFX:windowContentCreatorRegistryFX;
      newEloCreationRegistry: NewEloCreationRegistryImpl{};
//      topLeftCornerTool:MissionMap{
//         missionModel: missionModel
//      }
//      bottomRightCornerTool:MissionMap{
//         missionModel: missionModel
//      }
//      bottomLeftCornerTool:newWindowButton;
//        bottomLeftCornerTool: newScyWindowTool;
   }

   newScyWindowTool.scyDesktop = scyDesktop;
   scyDesktop.bottomLeftCornerTool= newScyWindowTool;

   scyDesktop.newEloCreationRegistry.registerEloCreation("test","test");
   scyDesktop.newEloCreationRegistry.registerEloCreation("tst","tst");
   scyDesktop.newEloCreationRegistry.registerEloCreation("size","Size test");

   Stage {
      title : "ScyDestop Test"
      scene: Scene {
         width: 400
         height: 300
         content: [
            scyDesktop,
         ]
      }
   }
}
