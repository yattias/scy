/*
 * Las.fx
 *
 * Created on 12-feb-2010, 10:46:49
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;
import javafx.scene.paint.Color;
import java.net.URI;

/**
 * @author sikken
 */
public class Las {

   public var id = "?";
   public var xPos: Number = 0;
   public var yPos: Number = 0;
   public var loEloUris: URI[];
   public var nextLasses: Las[];
   public var previousLasses: Las[];
   public var mainAnchor: MissionAnchorFX;
   public var intermediateAnchors: MissionAnchorFX[];
   public var otherEloUris: URI[];
   public var color = Color.LIGHTGRAY;
   public var iconCharacter = "?";
//   public var title = "?";
   public var toolTip: String;
   public var exists=false;

   public var selectedAnchor:MissionAnchorFX;

   public override function toString(): String {
      return "id:{id}";
   }

}
