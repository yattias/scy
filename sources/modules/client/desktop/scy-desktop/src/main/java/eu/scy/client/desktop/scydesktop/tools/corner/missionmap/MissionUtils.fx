/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import java.util.HashMap;
import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;

/**
 * @author SikkenJ
 */
public class MissionUtils {

   def lasMap = new HashMap();
   def anchorMap = new HashMap();

   public function createLasFX(las: Las): Void {
      var lasFX = lasMap.get(las) as LasFX;
      if (lasFX == null) {
         lasFX = LasFX {
               missionUtils: this
            }
         lasMap.put(las, lasFX)
      }
   }

   public function getLasFX(las: Las): LasFX {
      var lasFX = lasMap.get(las) as LasFX;
      lasFX.las = las;
      return lasFX
   }

   public function createMissionAnchorFX(missionAnchor: MissionAnchor): Void {
      var missionAnchorFX = anchorMap.get(missionAnchor) as MissionAnchorFX;
      if (missionAnchorFX == null) {
         missionAnchorFX = MissionAnchorFX {
               missionUtils: this
            }
         anchorMap.put(missionAnchor, missionAnchorFX);
      }
   }

   public function getMissionAnchorFX(missionAnchor: MissionAnchor): MissionAnchorFX {
      var missionAnchorFX = anchorMap.get(missionAnchor) as MissionAnchorFX;
      missionAnchorFX.missionAnchor = missionAnchor;
      return missionAnchorFX
   }

   public function getUriSequence(uriList: List): URI[] {
      var uris: URI[];
      if (uriList != null) {
         uris = for (uriObject in uriList) {
               uriObject as URI
            }
      }
      return uris
   }

   public function getUriList(uris: URI[]): List {
      def uriList = new ArrayList();
      for (uri in uris) {
         uriList.add(uri);
      }
      return uriList
   }

   public function getLasFXSequence(lasList: List): LasFX[] {
      var lasses: LasFX[];
      if (lasList != null) {
         lasses = for (lasObject in lasList) {
               getLasFX(lasObject as Las)
            }
      }
      return lasses
   }

   public function getMissionAnchorFXSequence(missionAnchorList: List): MissionAnchorFX[] {
      var anchors: MissionAnchorFX[];
      if (missionAnchorList != null) {
         anchors = for (lasObject in missionAnchorList) {
               getMissionAnchorFX(lasObject as MissionAnchor)
            }
      }
      return anchors
   }

   public function getStringSequence(stringList: List): String[] {
      var strings: String[];
      if (stringList != null) {
         strings = for (stringObject in stringList) {
               stringObject as String
            }
      }
      return strings;
   }

}
