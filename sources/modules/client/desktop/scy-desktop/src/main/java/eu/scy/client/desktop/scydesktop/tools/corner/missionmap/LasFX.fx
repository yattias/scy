/*
 * Las.fx
 *
 * Created on 12-feb-2010, 10:46:49
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;
import javafx.scene.paint.Color;
import java.net.URI;
import eu.scy.common.mission.LasType;

/**
 * @author sikken
 */
public class LasFX {

   public var id = "?";
   public var xPos: Number = 0;
   public var yPos: Number = 0;
   public var loEloUris: URI[];
   public var nextLasses: LasFX[];
   public var previousLasses: LasFX[];
   public var mainAnchor: MissionAnchorFX;
   public var intermediateAnchors: MissionAnchorFX[];
   public var otherEloUris: URI[];
   public var color = Color.LIGHTGRAY;
//   public var iconCharacter = "?";
//   public var title = "?";
   public var toolTip: String;
   public var exists=false;
   public var instructionUri:URI;
   public var lasType: LasType;

   public var selectedAnchor:MissionAnchorFX;

   public override function toString(): String {
      return "id:{id}";
   }

   public function getAllEloUris():URI[]{
      var allEloUris:URI[];
      insert loEloUris into allEloUris;
      insert mainAnchor.getAllEloUris() into allEloUris;
      for (intermediateAnchor in intermediateAnchors){
         insert intermediateAnchor.getAllEloUris() into allEloUris;
      }
      return allEloUris;
   }
}
