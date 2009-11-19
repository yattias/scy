/*
 * MissionModelUtils.fx
 *
 * Created on 13-okt-2009, 12:14:17
 */

package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;

import java.net.URI;
import java.util.HashMap;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchor;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;
import java.lang.String;

import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author sikken
 */

def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.config.MissionModelUtils");

public class MissionModelUtils {

}

public function createBasicMissionModelFX(missionAnchorsList:List):MissionModelFX{
   var missionAnchors:MissionAnchor[];
   for (missionAnchor in missionAnchorsList){
      insert missionAnchor as MissionAnchor into missionAnchors;
   }
   var missionModelFX = MissionModelFX{

   }
   if (missionAnchors!=null){
      var missionAnchorFXMap = new HashMap();
      // create the list of MissionAnchorFX
      for (missionAnchor in missionAnchors)
      {
         var missionAnchorFX = MissionAnchorFX{
            eloUri: missionAnchor.getEloUri()
            xPos: missionAnchor.getXPosition()
            yPos: missionAnchor.getYPosition()
            exists:missionAnchor.isExisting();
            relationNames: toStringSequence(missionAnchor.getRelationNames())
         }
         missionAnchorFXMap.put(missionAnchorFX.eloUri, missionAnchorFX);
         insert missionAnchorFX into missionModelFX.anchors;
         for (uriObject in missionAnchor.getHelpEloUris()){
            insert uriObject as URI into missionAnchorFX.helpEloUris;
         }
      }
      // fill in the sequences of next anchors
      for (missionAnchor in missionAnchors)
      {
         var missionAnchorFX = missionAnchorFXMap.get(missionAnchor.getEloUri()) as MissionAnchorFX;
         if (missionAnchor.getNextMissionAnchors()!=null){
            for (nextMissionAnchor in missionAnchor.getNextMissionAnchors()){
               var nextMissionAnchorFX = missionAnchorFXMap.get(nextMissionAnchor.getEloUri()) as MissionAnchorFX;
               if (nextMissionAnchorFX != null){
                  insert nextMissionAnchorFX into missionAnchorFX.nextAnchors;
               }
               else
               {
                  logger.info("can't find next mission anchor with uri: {nextMissionAnchor.getEloUri()}");
               }
            }
         }
         else{
            logger.info("no next anchors for {missionAnchor.getEloUri()}");
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

