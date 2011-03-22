/*
 * MissionModelFX.fx
 *
 * Created on 13-okt-2009, 11:48:42
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;

/**
 * @author sikken
 */

public def eloType = "scy/missionmodel";

public class MissionModelFX {
   def logger = Logger.getLogger(this.getClass());

   public var id:String;
   public var name:String;
   public var loEloUris:URI[];
   public var lasses:Las[];
   public-read var activeLas:Las on replace{
         logger.debug("new activeLas {activeLas}");
         updateElo();
   }

   public var elo: IELO;
   public var repository:IRepository;
   public var eloFactory:IELOFactory;

// old props
//   public var anchors:MissionAnchorFX[];
//   public var activeAnchor:MissionAnchorFX on replace{
//         logger.debug("new activeAncher {activeAnchor}");
//         updateElo();
//      };


   var contentChanged = false;

   init{
      findPreviousLasLinks();
      if (activeLas != null){
         if (not activeLas.exists){
            // the active anchor elo does not exist
            logger.error("the specified active las does not have an existing main anchor elo: {activeLas.mainAnchor.eloUri}");
            activeLas = null;
         }
      }
   }

   public function getAllEloUris():URI[]{
      var allEloUris:URI[];
      insert loEloUris into allEloUris;
      for (las in lasses){
         insert las.getAllEloUris() into allEloUris;
      }
      return allEloUris;
   }


   public function anchorSelected(las:Las,anchor:MissionAnchorFX):Void{
      if (las==activeLas){
         if (las.selectedAnchor==anchor){
            // nothing changed
            return;
         }
      }
      else{
         activeLas = las;
      }
      if (anchor==null){
         las.selectedAnchor=las.mainAnchor;
      }
      else{
         las.selectedAnchor=anchor;
      }

      updateElo();
   }



   public function eloUriChanged(oldEloUri:URI, newEloUri:URI){
      logger.info("eloUri changed from {oldEloUri} to {newEloUri}");
      if (oldEloUri==null){
         // it's a new elo, being saved
         if (activeLas!=null){
            insert newEloUri into activeLas.otherEloUris;
         }
         return;
      }
      loEloUris = updateEloUri(loEloUris,oldEloUri,newEloUri);
      for (las in lasses){
         updateLasEloUri(las,oldEloUri,newEloUri)
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

   function updateLasEloUri(las:Las,oldEloUri:URI, newEloUri:URI){
      updateAnchorEloUri(las.mainAnchor,oldEloUri,newEloUri);
      for (intermediateAnchor in las.intermediateAnchors){
         updateAnchorEloUri(intermediateAnchor,oldEloUri,newEloUri);
      }
      las.loEloUris = updateEloUri(las.loEloUris,oldEloUri,newEloUri);
   }

   function updateAnchorEloUri(anchor:MissionAnchorFX,oldEloUri:URI, newEloUri:URI){
      if (anchor.eloUri==oldEloUri){
         anchor.eloUri = newEloUri;
         contentChanged = true;
      }
      anchor.loEloUris = updateEloUri(anchor.loEloUris,oldEloUri,newEloUri);
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

//   public function findPreviousMissionAnchorLinks(){
//      for (anchor in anchors){
//         delete anchor.previousAnchors;
//      }
//      for (anchor in anchors){
//         for (nextAnchor in anchor.nextAnchors){
//            insert anchor into nextAnchor.previousAnchors;
//         }
//      }
//   }

   public function findPreviousLasLinks(){
      for (las in lasses){
         delete las.previousLasses;
      }
      for (las in lasses){
         for (nextlas in las.nextLasses){
            insert las into nextlas.previousLasses;
         }
      }
   }

}