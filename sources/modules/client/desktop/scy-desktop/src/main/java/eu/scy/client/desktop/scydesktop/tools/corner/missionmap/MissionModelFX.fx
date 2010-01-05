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
import java.util.HashMap;

/**
 * @author sikken
 */

public def eloType = "scy/missionmodel";

public class MissionModelFX {
   def logger = Logger.getLogger(this.getClass());

   public var anchors:MissionAnchorFX[];
   public var activeAnchor:MissionAnchorFX on replace{
         logger.debug("new activeAncher {activeAnchor}");
         updateElo();
      };
   public var elo: IELO;
   public var repository:IRepository;
   public var eloFactory:IELOFactory;

   var contentChanged = false;

   init{
      findPreviousMissionAnchorLinks();
   }


   public function eloUriChanged(oldEloUri:URI, newEloUri:URI){
      //logger.info("eloUri changed from {oldEloUri} to {newEloUri}");
      for (anchor in anchors){
         if (anchor.eloUri==oldEloUri){
            anchor.eloUri=newEloUri;
            contentChanged = true;
            // it is not likely that an elo is also a help or support elo
            // but it might be, so check other eloUri
//            updateElo();
//            return;
         }
      }
      for (anchor in anchors){
         anchor.helpEloUris = updateEloUri(anchor.helpEloUris,oldEloUri,newEloUri);
         anchor.supportEloUris = updateEloUri(anchor.supportEloUris,oldEloUri,newEloUri);
      }
      if (contentChanged){
          updateElo();
      }
    }

   function updateEloUri(uris:URI[], oldEloUri:URI, newEloUri:URI):URI[]{
      var updatedUris:URI[];
      for (uri in uris){
         if (uri==oldEloUri){
            insert newEloUri into updatedUris;
            contentChanged = true;
         }
         else{
            insert uri into updatedUris;
         }
      }
      return updatedUris;
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
      contentChanged = false;
   }

   public function findPreviousMissionAnchorLinks(){
      for (anchor in anchors){
         delete anchor.previousAnchors;
      }
      for (anchor in anchors){
         for (nextAnchor in anchor.nextAnchors){
            insert anchor into nextAnchor.previousAnchors;
         }
      }
   }

}
