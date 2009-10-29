/*
 * MissionModelFX.fx
 *
 * Created on 13-okt-2009, 11:48:42
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import org.apache.log4j.Logger;

/**
 * @author sikken
 */

 var logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX");

public class MissionModelFX {
   public var anchors:MissionAnchorFX[];
   public var activeAnchor:MissionAnchorFX on replace{
         logger.info("new activeAncher {activeAnchor}");
      };
}
