/*
 * ChattoolDrawerContentCreatorFX.fx
 *
 * Created on Nov 17, 2009, 8:23:10 PM
 */

package eu.scy.client.tools.fxchattool.registration;


import javafx.scene.Node;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;


import eu.scy.client.tools.chattool.ChatPresencePanel;
import eu.scy.awareness.IAwarenessService;
import eu.scy.chat.controller.ChatController;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorFX;

/**
 * @author jeremyt
 */

public class ChattoolPresenceDrawerContentCreatorFX extends DrawerContentCreatorFX {
    override public function getDrawerContent (eloUri:URI, scyWindow:ScyWindow) : Node {
         return createChatPresenceToolNode(scyWindow);
    }

    public var node:Node;
    public var awarenessService:IAwarenessService;
    public var chatController:ChatController;
   
    function createChatPresenceToolNode(scyWindow:ScyWindow):ChatPresenceToolNode{

        //needs to be the same awareness service as the one initized with chatpanelmain
        var chatPresenceTool = new ChatPresencePanel(chatController);

        return ChatPresenceToolNode{
            chatPresenceTool:chatPresenceTool;

         }
   }
}
