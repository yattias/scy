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
            name: missionAnchor.getName()
            xPos: missionAnchor.getXPosition()
            yPos: missionAnchor.getYPosition()
            exists:missionAnchor.isExisting();
            relationNames: toStringSequence(missionAnchor.getRelationNames())
            metadata:missionAnchor.getMetadata()
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
         missionAnchorFX.nextAnchors = toMissionAnchorList(missionAnchor.getNextMissionAnchors(),missionAnchorFXMap);
         missionAnchorFX.inputAnchors = toMissionAnchorList(missionAnchor.getInputMissionAnchors(),missionAnchorFXMap);
      }
      missionModelFX.findPreviousMissionAnchorLinks();
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

function toMissionAnchorList(missionAnchors: List,missionAnchorFXMap: HashMap):MissionAnchorFX[]{
   var missionAnchorFXList:MissionAnchorFX[];
   if (missionAnchors!=null){
      for (missionAnchorObject in missionAnchors){
         var missionAnchor = missionAnchorObject as MissionAnchor;
         var nextMissionAnchorFX = missionAnchorFXMap.get(missionAnchor.getEloUri()) as MissionAnchorFX;
         if (nextMissionAnchorFX != null){
            insert nextMissionAnchorFX into missionAnchorFXList;
         }
         else
         {
            logger.info("can't find next mission anchor with uri: {missionAnchor.getEloUri()}");
         }
      }
   }
   return missionAnchorFXList;
}


