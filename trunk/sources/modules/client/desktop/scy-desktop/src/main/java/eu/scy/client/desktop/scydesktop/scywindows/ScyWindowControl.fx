/*
 * ScyWindowControl.fx
 *
 * Created on 13-okt-2009, 17:12:09
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionMap;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.elofactory.impl.ScyToolFactory;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryWrapper;
import eu.scy.client.desktop.scydesktop.edges.IEdgesManager;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.elofactory.EloConfigManager;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;


/**
 * ScyWindowControl controls which windows (elos) are shown on the desktop
 *
 * @author sikken
 */

public mixin class ScyWindowControl {
   public var missionModel: MissionModelFX;
   public var missionMap: MissionMap;
   public var windowContentFactory: ScyToolFactory;
   public var windowStyler: WindowStyler;
   public var windowManager: WindowManager;
   public var windowPositioner: WindowPositioner;
   public var edgesManager:IEdgesManager;
   public var stage: Stage;
   public var tbi:ToolBrokerAPI;
   public var tooltipManager:TooltipManager;
   public var bubbleManager: BubbleManager;
   public var dragAndDropManager:DragAndDropManager;
   public var repositoryWrapper:RepositoryWrapper;
   public var eloConfigManager: EloConfigManager;

   protected def metadataTypeManager = tbi.getMetaDataTypeManager();
   protected def extensionManager = tbi.getExtensionManager();
   protected def repository = tbi.getRepository();

   public var setScyContent:function(window: ScyWindow):Void;
   //   public var edgesManager: EdgesManager;

   public abstract function addOtherScyWindow(eloUri:URI): ScyWindow;
   
   public abstract function addOtherScyWindow(eloType:String): ScyWindow;

   public abstract function addOtherCollaborativeScyWindow(eloUri:URI, mucid: String): ScyWindow;

   public abstract function removeOtherScyWindow(eloUri:URI): Void;

   public abstract function removeOtherScyWindow(window:ScyWindow): Void;

   public abstract function makeMainScyWindow(eloUri:URI): Void;

   public abstract function makeMainScyWindow(window: ScyWindow): Void;

   public abstract function newEloSaved(eloUri:URI):Void;

//   public function positionWindows():Void{
//      positionWindows(false);
//   }
//
//   public abstract function positionWindows(onlyNewWindows:Boolean):Void;

}
