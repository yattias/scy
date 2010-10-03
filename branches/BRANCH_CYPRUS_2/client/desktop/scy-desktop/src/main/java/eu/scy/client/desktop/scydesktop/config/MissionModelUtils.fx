/*
 * MissionModelUtils.fx
 *
 * Created on 13-okt-2009, 12:14:17
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import java.net.URI;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;
import java.lang.String;
import java.util.List;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.Las;

/**
 * @author sikken
 */
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.config.MissionModelUtils");

public class MissionModelUtils {
}

public function retrieveMissionModelFromConfig(config: Config): MissionModelFX {
   var basicMissionMap = config.getBasicMissionMap();
   var missionModel = MissionModelFX {
         id: basicMissionMap.getId();
         name: basicMissionMap.getName();
         loEloUris: createExistingEloUriSequence(basicMissionMap.getLoEloUris(), "mission map", config)
         lasses: createLasSequence(basicMissionMap.getLasses(), config.getBasicMissionAnchors(), config);
      }
   var initialActiveLasId = config.getBasicMissionMap().getInitialLasId();
   if (initialActiveLasId != null) {
      var initialActiveLas: Las;
      for (las in missionModel.lasses) {
         if (las.id == initialActiveLasId) {
            initialActiveLas = las
         }
      }
      if (initialActiveLas != null) {
         missionModel.anchorSelected(initialActiveLas, null);
      } else {
         logger.error("cannot find initial active las with id {initialActiveLasId}");
      }
      }
   missionModel.findPreviousLasLinks();
   return missionModel;
   }

function createExistingEloUriSequence(uriList: List, objectLabel: String, config: Config): URI[] {
   var uris: URI[];
   if (uriList != null) {
      for (object in uriList) {
         var uri = object as URI;
         var metadata = config.getRepository().retrieveMetadata(uri);
         if (metadata != null) {
            insert uri into uris;
            } else {
            logger.error("Could not find ELO with uri {uri}, in {objectLabel}");
            }
      }
   }
   return uris;
   }

function createLasSequence(lasList: List, missionAnchorList: List, config: Config): Las[] {
   var lasIdMap = new HashMap();
   var lasses: Las[];
   if (lasList != null)
      lasses = for (object in lasList) {
         var basicLas = object as BasicLas;
         var las = Las {
               id: basicLas.getId();
               xPos: basicLas.getxPosition();
               yPos: basicLas.getyPosition();
               loEloUris: createExistingEloUriSequence(basicLas.getLoEloUris(), "las {basicLas.getId()}", config)
               toolTip: basicLas.getTooltip();
            }
         if (not lasIdMap.containsKey(las.id)) {
            lasIdMap.put(las.id, las);
         } else {
            logger.error("duplicate las id: {las.id}");
         }

         las
      }
   // fix the next las links
   if (lasList != null)
      for (object in lasList) {
         var basicLas = object as BasicLas;
         var las = lasIdMap.get(basicLas.getId()) as Las;
         for (lasId in basicLas.getNextLasses()) {
            var nextLas = lasIdMap.get(lasId) as Las;
            if (nextLas != null) {
               insert nextLas into las.nextLasses;
               } else {
               logger.error("cannot find nextLas id: {lasId} in las {las.id}");
               }
            }
      }
   // fix the mission anchor links
   var missionAnchorMap = new HashMap();
   var missionAnchors: MissionAnchorFX[];
   if (missionAnchorList != null) {
      missionAnchors = for (object in missionAnchorList) {
         var anchor = object as BasicMissionAnchor;
         var missionAnchor = MissionAnchorFX {
               id: anchor.getId()
               eloUri: anchor.getUri()
               iconType: anchor.getIconType()
               metadata: anchor.getMetadata();
               exists: anchor.getMetadata() != null;
               loEloUris: createExistingEloUriSequence(anchor.getLoEloUris(), "las {anchor.getId()}", config)
            }
         if (not missionAnchorMap.containsKey(missionAnchor.id)) {
            missionAnchorMap.put(missionAnchor.id, missionAnchor);
         } else {
            logger.error("duplicate anchor id: {missionAnchor.id}");
         }
         missionAnchor;
      }

      for (object in missionAnchorList) {
         var anchor = object as BasicMissionAnchor;
         var missionAnchor = missionAnchorMap.get(anchor.getId()) as MissionAnchorFX;
         for (objectId in anchor.getInputMissionAnchorIds()) {
            var inputMissionAnchorId = objectId as String;
            var inputMissionAnchor = missionAnchorMap.get(inputMissionAnchorId) as MissionAnchorFX;
            if (inputMissionAnchor != null) {
               insert inputMissionAnchor into missionAnchor.inputAnchors;
               } else {
               logger.error("cannot find next mission anchor id {inputMissionAnchorId} for mission anchor id {missionAnchor.id}");
               }
            }
         }
   }
   if (lasList != null) {
      for (object in lasList) {
         var basicLas = object as BasicLas;
         var las = lasIdMap.get(basicLas.getId()) as Las;
         las.mainAnchor = missionAnchorMap.get(basicLas.getAnchorEloId()) as MissionAnchorFX;
         las.mainAnchor.las = las;
         if (las.mainAnchor == null) {
            logger.error("cannot find anchor elo id {basicLas.getAnchorEloId()} for las id {las.id}");
            }
         if (basicLas.getIntermediateEloIds() != null) {
            for (anchorId in basicLas.getIntermediateEloIds()) {
               var id = anchorId as String;
               var intermediateAnchor = missionAnchorMap.get(anchorId) as MissionAnchorFX;
               if (intermediateAnchor != null) {
                  insert intermediateAnchor into las.intermediateAnchors;
                  intermediateAnchor.las = las;
                  } else {
                  logger.error("cannot find intermediate elo id {id} for las id {las.id}");
                  }
               }
         }
      }
   }
   return lasses;
   }

function toUriSequence(uris: List): URI[] {
   var uriSequence: URI[];
   if (uris != null) {
      for (uri in uris) {
         insert uri as URI into uriSequence;
      }
   }
   return uriSequence;
}

function toStringSequence(names: List): String[] {
   var nameSequence: String[];
   if (names != null) {
      for (name in names) {
         insert name as String into nameSequence;
      }
   }
   return nameSequence;
}

function toMissionAnchorList(missionAnchors: List, missionAnchorFXMap: HashMap): MissionAnchorFX[] {
   var missionAnchorFXList: MissionAnchorFX[];
   if (missionAnchors != null) {
      for (missionAnchorObject in missionAnchors) {
         var missionAnchor = missionAnchorObject as MissionAnchor;
         var nextMissionAnchorFX = missionAnchorFXMap.get(missionAnchor.getEloUri()) as MissionAnchorFX;
         if (nextMissionAnchorFX != null) {
            insert nextMissionAnchorFX into missionAnchorFXList;
         } else {
            logger.info("can't find next mission anchor with uri: {missionAnchor.getEloUri()}");
         }
      }
   }
   return missionAnchorFXList;
}


