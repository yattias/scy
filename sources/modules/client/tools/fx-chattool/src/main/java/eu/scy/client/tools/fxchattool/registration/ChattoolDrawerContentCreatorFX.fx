/*
 * ChattoolDrawerContentCreatorFX.fx
 *
 * Created on Nov 17, 2009, 8:23:10 PM
 */

package eu.scy.client.tools.fxchattool.registration;


import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import eu.scy.client.tools.chattool.ChatPanelMain;

import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorFX;

/**
 * @author jeremyt
 */

public class ChattoolDrawerContentCreatorFX extends DrawerContentCreatorFX {
    override public function getDrawerContent (eloUri:URI, scyWindow:ScyWindow) : Node {
         return createChatToolNode(scyWindow);
    }

    public var node:Node;
   //public var metadataTypeManager: IMetadataTypeManager;
   // public var repository:IRepository;

    function createChatToolNode(scyWindow:ScyWindow):ChatToolNode{
        var chatTool = new ChatPanelMain();

        return ChatToolNode{
            chatTool:chatTool;

         }
   }
}
