/*
 * SwingScyDesktop.fx
 *
 * Created on 2-jul-2009, 14:01:11
 */

package eu.scy.client.desktop.scydesktop;




import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreator;
import eu.scy.client.desktop.scydesktop.elofactory.SwingContentCreator;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelFX;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.dummy.DummyEloInfoControl;
import eu.scy.client.desktop.scydesktop.dummy.DummyWindowContentCreator;
import eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler;

import eu.scy.client.desktop.scydesktop.missionmap.MissionModelCreator;
import java.lang.Class;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

/**
 * @author sikkenj
 */

def titleOption = "title";
def missionModelCreatorClassNameOptionOption = "missionModelCreatorClassName";
def swingComponentClassNameOption = "swingComponentClassName";

InitLog4JFX.initLog4J();

var title = "Swing SCY desktop";
var missionModel:MissionModelFX;
var windowContentCreator:WindowContentCreator = DummyWindowContentCreator{};

function parseParamenters(args: String[]): Void{
   var i=0;
   while (i<args.size()){
      var lcArg = args[i].toLowerCase();
      if (lcArg.startsWith('-')){
         var option = lcArg.substring(1);
         if (option==titleOption.toLowerCase()){
            title = args[++i];
            println("{titleOption}: {title}");
         }
         else if (option==missionModelCreatorClassNameOptionOption.toLowerCase()){
            var className = args[++i];
            missionModel = createMissionModel(className);
            println("{missionModelCreatorClassNameOptionOption}: {className}");
         }
         else if (option==swingComponentClassNameOption.toLowerCase()){
            var swingClassName = args[++i];
            windowContentCreator = SwingContentCreator{
               swingClassName: swingClassName;
            }
           println("{swingComponentClassNameOption}: {swingClassName}");
         }
         else{
            println("Unknown option: {option}");
         }
      }
      else{
         println("ignored parameter: {args[i]}");
      }

      i++;
   }
}

function createMissionModel(missionModelCreatorClassName:String):MissionModelFX{
   var clas = Class.forName(missionModelCreatorClassName);
   var missionModelCreator =clas.newInstance() as MissionModelCreator;
   var missionModel = missionModelCreator.createMissionModel();
   return MissionModelFX.createMissionModelFX(missionModel);
}

parseParamenters(FX.getArguments());

var scyDesktop = ScyDesktop{
   missionModelFX : missionModel;
   eloInfoControl: DummyEloInfoControl{
   };
   windowStyler:DummyWindowStyler{
   };
   windowContentCreator:windowContentCreator;
//      topLeftCornerTool:MissionMap{
//         missionModel: missionModel
//      }
//      bottomRightCornerTool:MissionMap{
//         missionModel: missionModel
//      }
//   bottomLeftCornerTool:newWindowButton;
}


Stage {
    title: title
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}