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

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.elofactory.impl.ScyToolFactory;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryWrapper;
import eu.scy.client.desktop.scydesktop.edges.IEdgesManager;


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
   public var eloInfoControl: EloInfoControl;
   public var metadataTypeManager: IMetadataTypeManager;
   public var extensionManager: IExtensionManager;
   public var repository: IRepository;
   public var tooltipManager:TooltipManager;
   public var dragAndDropManager:DragAndDropManager;
   public var repositoryWrapper:RepositoryWrapper;


   public var setScyContent:function(window: ScyWindow):Void;
   //   public var edgesManager: EdgesManager;

   public abstract function addOtherScyWindow(eloUri:URI): ScyWindow;
   public abstract function addOtherScyWindow(eloType:String): ScyWindow;

   public abstract function makeMainScyWindow(eloUri:URI): Void;

   public abstract function newEloSaved(eloUri:URI):Void;

//   public function positionWindows():Void{
//      positionWindows(false);
//   }
//
//   public abstract function positionWindows(onlyNewWindows:Boolean):Void;

}
