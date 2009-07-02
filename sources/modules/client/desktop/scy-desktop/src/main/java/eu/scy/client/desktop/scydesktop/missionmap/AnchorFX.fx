/*
 * Anchor.fx
 *
 * Created on 19-mrt-2009, 10:53:45
 */

package eu.scy.client.desktop.scydesktop.missionmap;

import java.net.URI;
import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

public class AnchorFX {
    public var title = "?";
    public var color = Color.LIGHTGRAY;
    public var xPos:Float = 0;
    public var yPos:Float = 0;
    public var nextAnchors: AnchorFX[];
    public var eloUri: URI;
    public var relationNames:String[];
}
