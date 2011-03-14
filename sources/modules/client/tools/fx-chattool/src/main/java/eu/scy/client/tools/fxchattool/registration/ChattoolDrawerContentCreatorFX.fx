/*
 * ChattoolDrawerContentCreatorFX.fx
 *
 * Created on Nov 17, 2009, 8:23:10 PM
 */

package eu.scy.client.tools.fxchattool.registration;


import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import eu.scy.client.tools.chattool.ChatPanel;

import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorFX;
import eu.scy.awareness.IAwarenessService;
import eu.scy.chat.controller.ChatController;
import java.util.*;
import eu.scy.chat.controller.*;
import org.apache.commons.lang.StringUtils;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.toolbrokerapi.ToolBrokerAPI;


/**
 * @author jeremyt
 */

public class ChattoolDrawerContentCreatorFX extends DrawerContentCreatorFX {

    override public function getDrawerContent (eloUri:URI, scyWindow:ScyWindow) : Node {
        repository = toolBrokerAPI.getRepository();
        awarenessService = toolBrokerAPI.getAwarenessService();
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
         return createChatToolNode(scyWindow,eloUri);
    }

    public var node:Node;
    public var awarenessService:IAwarenessService;
    public var chatController:ChatController;
    public var eloId:String;
    public var chatControllerMap:HashMap;
    public var repository:IRepository;
    public var metadataTypeManager: IMetadataTypeManager;
    public var toolBrokerAPI: ToolBrokerAPI;

    function createChatToolNode(scyWindow:ScyWindow,eloUri:URI):ChatToolNode {
        
        if(eloUri != null) {
            var metadataFirstVersion = repository.retrieveMetadataFirstVersion(eloUri);

            var identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
            var firstVersionELOURI = metadataFirstVersion.getMetadataValueContainer(identifierKey).getValue() as URI;

            var s = firstVersionELOURI.toString();

            s = StringUtils.remove(s, "/");
            s = StringUtils.remove(s, ".");
            s = StringUtils.remove(s, ":");
            var chatTool;
            var controller = chatControllerMap.get(s) as ChatController;

            if( controller != null ) {
                chatTool = new ChatPanel(controller);
            } else {
                var chatController = new MUCChatController(awarenessService, s);
                chatControllerMap.put(s, chatController);
                chatTool = new ChatPanel(chatController);
            }

            //or go random
            //String token = Long.toString(Math.abs(r.nextLong()), 36);


            return ChatToolNode{
                chatTool:chatTool;
            }
        }
        else {
            return null;
        }



        
   }
}
