/*
 * ChattoolDrawerContentCreator.fx
 *
 * Created on Sep 30, 2009, 9:43:18 AM
 */

package eu.scy.client.tools.fxchattool.registration;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ProgressNode;

/**
 * @author jeremyt
 */

public class ChattoolDrawerContentProgressCreatorFX extends DrawerContentCreatorFX {
    
    public override function getDrawerContent(eloUri:URI, scyWindow:ScyWindow):Node{
        return createPresenceNode(scyWindow);
    }

    function createPresenceNode(scyWindow:ScyWindow):ProgressNode {
        return ProgressNode {
            scyWindow: scyWindow;
        }
    }
}
