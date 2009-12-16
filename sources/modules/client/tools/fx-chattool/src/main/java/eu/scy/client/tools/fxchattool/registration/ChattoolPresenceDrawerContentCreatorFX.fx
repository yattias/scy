/*
 * ChattoolDrawerContentCreatorFX.fx
 *
 * Created on Nov 17, 2009, 8:23:10 PM
 */

package eu.scy.client.tools.fxchattool.registration;


import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;


import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorFX;

import eu.scy.client.tools.chattool.ChatPresencePanelMain;
import eu.scy.awareness.IAwarenessService;

/**
 * @author jeremyt
 */

public class ChattoolPresenceDrawerContentCreatorFX extends DrawerContentCreatorFX {
    override public function getDrawerContent (eloUri:URI, scyWindow:ScyWindow) : Node {
         return createChatPresenceToolNode(scyWindow);
    }

    public var node:Node;
    public var awarenessService:IAwarenessService;
   //public var metadataTypeManager: IMetadataTypeManager;
   // public var repository:IRepository;

    function createChatPresenceToolNode(scyWindow:ScyWindow):ChatPresenceToolNode{
        var chatPresenceTool = new ChatPresencePanelMain(awarenessService);

        return ChatPresenceToolNode{
            chatPresenceTool:chatPresenceTool;

         }
   }
}
