/*
 * ScyWindowControl.fx
 *
 * Created on 13-okt-2009, 17:12:09
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Node;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentFactory;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionMap;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;

/**
 * @author sikken
 */

public mixin class ScyWindowControl {
   public var missionModel: MissionModelFX;
   public var missionMap: MissionMap;
   public var windowContentFactory: WindowContentFactory;
   public var windowStyler: WindowStyler;
   public var scyDesktop: WindowManager;
   public var stage: Stage;
   public var eloInfoControl: EloInfoControl;
//   public var metadataTypeManager: IMetadataTypeManager;
//   public var extensionManager: IExtensionManager;
//   public var repository: IRepository;
   //   public var edgesManager: EdgesManager;
   public var width: Number= 400;
   public var height: Number = 300;

   public var forbiddenNodes: Node[];

   public abstract function addOtherScyWindow(otherWindow:ScyWindow): Void;

   public function positionWindows():Void{
      positionWindows(false);
   }

   public abstract function positionWindows(onlyNewWindows:Boolean):Void;

}
