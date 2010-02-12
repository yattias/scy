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
import java.util.*;
import eu.scy.chat.controller.*;
import org.apache.commons.lang.StringUtils;
import roolo.api.IRepository;
import roolo.api.search.IMetadataQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author jeremyt
 */

public class ChattoolPresenceDrawerContentCreatorFX extends DrawerContentCreatorFX {
    override public function getDrawerContent (eloUri:URI, scyWindow:ScyWindow) : Node {


         return createChatPresenceToolNode(scyWindow,eloUri);
    }

    public var node:Node;
    public var awarenessService:IAwarenessService;
    public var chatController:ChatController;
    public var toolBrokerAPI:ToolBrokerAPI;
    public var eloId:String;
    public var chatControllerMap:HashMap;
    public var repository:IRepository;
    public var metadataTypeManager: IMetadataTypeManager;
   
    function createChatPresenceToolNode(scyWindow:ScyWindow,eloUri:URI):ChatPresenceToolNode{

        var chatPresenceTool;

        
        var metadataFirstVersion = repository.retrieveMetadataFirstVersion(eloUri);

        var identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
        var firstVersionELOURI = metadataFirstVersion.getMetadataValueContainer(identifierKey).getValue() as URI;
         
        var s = firstVersionELOURI.toString();


        s = StringUtils.remove(s, "/");
        s = StringUtils.remove(s, ".");
        s = StringUtils.remove(s, ":");

        println("new string presence drawer: {s}" );
        var controller = chatControllerMap.get(s) as ChatController;

        if( controller != null ) {
            chatPresenceTool = new ChatPresencePanel(controller);
        } else {
            var chatController = new MUCChatController(awarenessService, s);
            chatControllerMap.put(s, chatController);
            chatPresenceTool = new ChatPresencePanel(chatController);
        }
        
        return ChatPresenceToolNode{
            chatPresenceTool:chatPresenceTool;
            toolBrokerAPI:toolBrokerAPI;
         };
   }
}
