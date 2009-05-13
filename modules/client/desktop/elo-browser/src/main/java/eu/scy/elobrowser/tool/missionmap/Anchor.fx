/*
 * Anchor.fx
 *
 * Created on 19-mrt-2009, 10:53:45
 */

package eu.scy.elobrowser.tool.missionmap;

import eu.scy.elobrowser.tool.missionmap.Anchor;
import java.net.URI;
import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

public class Anchor {
    public var title = "?";
    public var color = Color.LIGHTGRAY;
    public var xPos = 0;
    public var yPos = 0;
    public var nextAnchors: Anchor[];
    public var eloUri: URI;
    public var relationNames:String[];
}
