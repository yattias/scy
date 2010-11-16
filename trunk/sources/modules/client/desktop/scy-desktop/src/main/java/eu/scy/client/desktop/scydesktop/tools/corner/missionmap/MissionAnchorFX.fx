/*
 * MissionAnchorFX.fx
 *
 * Created on 13-okt-2009, 12:07:33
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.paint.Color;
import java.net.URI;
import roolo.elo.api.IMetadata;
import eu.scy.client.desktop.scydesktop.art.ColorSchemeId;

/**
 * @author sikken
 */
public class MissionAnchorFX {

   public var id:String;
   public var eloUri: URI;
   public var iconType:String;
   public var inputAnchors: MissionAnchorFX[];
   public var relationNames: String[];
//   public var mainAnchor = true;
   public var exists = false;
   public var color = Color.LIGHTGRAY;
   public var iconCharacter = "?";
   public var title = "?";
   public var loEloUris: URI[];
   public var metadata: IMetadata;
   public var las:LasFX;

   public var targetDescriptionUri:URI;
   public var assignmentUri: URI;
   public var resourcesUri: URI;
   public var colorScheme: ColorSchemeId;

// old props
//   public var toolTip: String;
//   public var intermediateEloUris: URI[];
//   public var name = "?";
//   public var xPos: Number = 0;
//   public var yPos: Number = 0;
//   public var nextAnchors: MissionAnchorFX[];
//   public var previousAnchors: MissionAnchorFX[];

   public override function toString(): String {
      return "eloUri:{eloUri}";
   }

   public function getAllEloUris():URI[]{
      var allEloUris:URI[] = eloUri;
      insert loEloUris into allEloUris;
      return allEloUris;
   }
}
