/*
 * ChattoolDrawerContentCreator.fx
 *
 * Created on Sep 30, 2009, 9:43:18 AM
 */

package eu.scy.client.tools.fxchattool.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ContentCreator;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author jeremyt
 */

public class ChattoolDrawerContentCreatorFX extends ContentCreator {
    
     public function getDrawerContent(eloUri:URI, scyWindow:ScyWindow):Node{
        return createChatNode(scyWindow);
    }

    function createChatNode(scyWindow:ScyWindow):ChatNode {
        return ChatNode{
            scyWindow: scyWindow;
        }
    }
}
