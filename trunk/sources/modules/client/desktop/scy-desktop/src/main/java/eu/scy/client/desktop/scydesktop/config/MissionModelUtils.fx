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
import org.apache.log4j.Logger;

/**
 * @author sikken
 */

var logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.config.MissionModelUtils");

public class MissionModelUtils {

}

public function createBasicMissionModelFX(config:Config):MissionModelFX{
   var missionModelFX = MissionModelFX{

   }
   if (config.getBasicMissionAnchorConfigs()!=null){
      var missionAnchorFXMap = new HashMap();
      // create the list of MissionAnchorFX
      for (basicMissionAnchorConfig in config.getBasicMissionAnchorConfigs())
      {
         var missionAnchorFX = MissionAnchorFX{
            eloUri: new URI(basicMissionAnchorConfig.getUri())
            xPos: basicMissionAnchorConfig.getXPosition()
            yPos: basicMissionAnchorConfig.getYPosition()
            relationNames: toStringSequence(basicMissionAnchorConfig.getRelationNames())
         }
         missionAnchorFXMap.put(basicMissionAnchorConfig.getUri(), missionAnchorFX);
         insert missionAnchorFX into missionModelFX.anchors;
      }
      // fill in the sequences of next anchors
      for (basicMissionAnchorConfig in config.getBasicMissionAnchorConfigs())
      {
         var missionAnchorFX = missionAnchorFXMap.get(basicMissionAnchorConfig.getUri()) as MissionAnchorFX;
         if (basicMissionAnchorConfig.getNextMissionAnchorConfigUris()!=null){
            for (uri in basicMissionAnchorConfig.getNextMissionAnchorConfigUris()){
               var nextMissionAnchorFX = missionAnchorFXMap.get(uri) as MissionAnchorFX;
               if (nextMissionAnchorFX != null){
                  insert nextMissionAnchorFX into missionAnchorFX.nextAnchors;
               }
               else
               {
                  logger.info("can't find next mission anchor with uri: {uri}");
               }
            }
         }
      }
   }
   return missionModelFX;
}

function toStringSequence(names:List):String[]{
   var nameSequence:String[];
   if (names!=null){
      for (name in names){
         insert name as String into nameSequence;
      }
   }
   return nameSequence;
}

