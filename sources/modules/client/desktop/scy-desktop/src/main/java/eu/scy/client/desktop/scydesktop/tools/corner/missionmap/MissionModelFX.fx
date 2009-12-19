/*
 * MissionModelFX.fx
 *
 * Created on 13-okt-2009, 11:48:42
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import org.apache.log4j.Logger;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;

/**
 * @author sikken
 */

def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX");
public def eloType = "scy/missionmodel";

public class MissionModelFX {
   public var anchors:MissionAnchorFX[];
   public var activeAnchor:MissionAnchorFX on replace{
         logger.info("new activeAncher {activeAnchor}");
         updateElo();
      };
   public var elo: IELO;
   public var repository:IRepository;
   public var eloFactory:IELOFactory;

   public function eloUriChanged(oldEloUri:URI, newEloUri:URI){
      for (anchor in anchors){
         if (anchor.eloUri==oldEloUri){
            anchor.eloUri==newEloUri;
         }
      }
      updateElo();
   }

   function updateElo():Void{
      if (repository==null or eloFactory==null){
         logger.error("failed to update elo because repository and/or eloFactory is null");
         return;
      }
      if (elo==null){
         logger.error("can't update elo, because elo is null");
         return;
      }
      elo.getContent().setXmlString(MissionModelXml.convertToXml(this));
      var missionModelMetadata = repository.updateELO(elo);
      eloFactory.updateELOWithResult(elo,missionModelMetadata);
   }


}
