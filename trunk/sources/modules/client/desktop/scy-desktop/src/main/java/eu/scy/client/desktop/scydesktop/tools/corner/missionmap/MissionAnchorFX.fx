/*
 * MissionAnchorFX.fx
 *
 * Created on 13-okt-2009, 12:07:33
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.paint.Color;
import java.net.URI;

/**
 * @author sikken
 */

public class MissionAnchorFX {
    public var eloUri: URI;
    public var xPos:Number = 0;
    public var yPos:Number = 0;
    public var nextAnchors: MissionAnchorFX[];
    public var relationNames:String[];
    public var exists = false;
    public var color = Color.LIGHTGRAY;
    public var iconCharacter = "?";
    public var title = "?";
    public var helpEloUris:URI[];

    public override function toString():String{
       return "eloUri:{eloUri}";
    }

}
