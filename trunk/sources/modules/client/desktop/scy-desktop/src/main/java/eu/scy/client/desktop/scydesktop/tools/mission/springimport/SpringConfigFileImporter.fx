/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.config.BasicLas;
import eu.scy.client.desktop.scydesktop.config.BasicMissionAnchor;
import eu.scy.client.desktop.scydesktop.config.MissionAnchor;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.Las;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import java.lang.IllegalArgumentException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelXml;

/**
 * @author SikkenJ
 */
// place your code here
public class SpringConfigFileImporter {

   def logger = Logger.getLogger(this.getClass());
   public-init var file: String;
   public-init var repository: IRepository;
   public-read var missionMapXml: String;
   var missionConfigInput: MissionConfigInput;
   def missionConfigInputBeanName = "missionConfigInput";

   init {
      missionConfigInput = readSpringConfig();
      def missionModel = retrieveMissionModelFromConfig();
      missionMapXml = MissionModelXml.convertToXml(missionModel);
   }

   function readSpringConfig(): MissionConfigInput {
      def context = new FileSystemXmlApplicationContext(file);
      if (context == null) {
         throw new IllegalArgumentException("failed to load context from file system: {file}");
      }
      var basicMissionConfigInput = context.getBean(missionConfigInputBeanName) as BasicMissionConfigInput;
      basicMissionConfigInput.parseEloConfigs(repository);
      basicMissionConfigInput;
   }

   function retrieveMissionModelFromConfig(): MissionModelFX {
      var basicMissionMap = missionConfigInput.getBasicMissionMap();
      var missionModel = MissionModelFX {
            id: basicMissionMap.getId();
            name: basicMissionMap.getName();
            loEloUris: createExistingEloUriSequence(basicMissionMap.getLoEloUris(), "mission map")
            lasses: createLasSequence(basicMissionMap.getLasses(), missionConfigInput.getBasicMissionAnchors());
         }
      var initialActiveLasId = missionConfigInput.getBasicMissionMap().getInitialLasId();
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

   function createExistingEloUriSequence(uriList: List, objectLabel: String): URI[] {
      var uris: URI[];
      if (uriList != null) {
         for (object in uriList) {
            var uri = object as URI;
            var metadata = repository.retrieveMetadata(uri);
            if (metadata != null) {
               insert uri into uris;
            } else {
               logger.error("Could not find ELO with uri {uri}, in {objectLabel}");
            }
         }
      }
      return uris;
   }

   function createLasSequence(lasList: List, missionAnchorList: List): Las[] {
      var lasIdMap = new HashMap();
      var lasses: Las[];
      if (lasList != null)
         lasses = for (object in lasList) {
               var basicLas = object as BasicLas;
               var las = Las {
                     id: basicLas.getId();
                     xPos: basicLas.getxPosition();
                     yPos: basicLas.getyPosition();
                     loEloUris: createExistingEloUriSequence(basicLas.getLoEloUris(), "las {basicLas.getId()}")
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
                     loEloUris: createExistingEloUriSequence(anchor.getLoEloUris(), "las {anchor.getId()}")
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

}
