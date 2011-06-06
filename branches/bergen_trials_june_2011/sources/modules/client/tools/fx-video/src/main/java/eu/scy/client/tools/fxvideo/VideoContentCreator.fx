/*
 * WebressourceContentCreator.fx
 *
 * Created on 04.09.2009, 12:17:15
 */
package eu.scy.client.tools.fxvideo;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author pg
 */
//public class VideoContentCreator extends WindowContentCreatorFX {
public class VideoContentCreator extends ScyToolCreatorFX {

    public override function createScyToolNode(eloType: String, creatorId: String, scyWindow: ScyWindow, windowContent: Boolean): Node {
        return VideoNode {};
    }

}
