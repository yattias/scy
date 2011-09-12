/*
 * ChattoolDrawerContentCreatorFX.fx
 *
 * Created on Nov 17, 2009, 8:23:10 PM
 */

package eu.scy.client.tools.fxchattool.registration;


import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import eu.scy.awareness.IAwarenessService;
import eu.scy.chat.controller.ChatController;
import java.util.*;
import eu.scy.chat.controller.*;
import org.apache.commons.lang.StringUtils;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import roolo.elo.metadata.keys.Contribute;


/**
 * @author jeremyt
 */

public class ChattoolDrawerContentCreatorFX extends ScyToolCreatorFX {

   public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow,windowContent:Boolean):Node{
        repository = toolBrokerAPI.getRepository();
        awarenessService = toolBrokerAPI.getAwarenessService();
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
         return createChatToolNode(scyWindow);
    }

    public var awarenessService:IAwarenessService;
    public var chatController:ChatController;
    public var eloId:String;
    public var chatControllerMap:HashMap;
    public var repository:IRepository;
    public var metadataTypeManager: IMetadataTypeManager;
    public var toolBrokerAPI: ToolBrokerAPI;

    function createChatToolNode(scyWindow:ScyWindow) : Node {
        var eloUri:URI = scyWindow.eloUri;
        if(eloUri == null){
            def elo = toolBrokerAPI.getELOFactory().createELO();
            elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId())).setValue("scy/chat");
            def newMetadata = toolBrokerAPI.getRepository().addNewELO(elo);
            eloUri = newMetadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId())).getValue() as URI;
        }

        if(eloUri != null) {
            var metadataFirstVersion = repository.retrieveMetadataFirstVersion(eloUri);

            var identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
            var firstVersionELOURI = metadataFirstVersion.getMetadataValueContainer(identifierKey).getValue() as URI;

            var s = firstVersionELOURI.toString();

            s = StringUtils.remove(s, "/");
            s = StringUtils.remove(s, ".");
            s = StringUtils.remove(s, ":");
            chatController = chatControllerMap.get(s) as ChatController;

            if( chatController == null ) {
                chatController = new MUCChatController(awarenessService, s);
                chatControllerMap.put(s, chatController);
            }
            def chattool = ChatterNode{
                chatController: chatController
                scyWindow: scyWindow
            };
            chatController.registerChat(chattool);

            def presenceListener = PresenceListener {
                ownershipManager: scyWindow.ownershipManager;
            };

            chatController.getAwarenessService().addAwarenessPresenceListener(presenceListener);
            chatController.connectToRoom();

            var metadata = repository.retrieveMetadata(eloUri);

            var buddies:String[];
            var authors = (metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR)).getValueList()) as List;
            if (authors != null) {
                for (author in authors) {
                    if (author instanceof String) {
                        insert (author as String) into buddies;
                    } else if (author instanceof Contribute) {
                        insert ((author as Contribute).getVCard()) into buddies;
                    }
                }
            }
            for (buddy in buddies) {
                if (not buddy.equals(toolBrokerAPI.getLoginUserName())) {
                    // todo check if user is already in mission
                    chatController.sendInvitation(buddy);
                }
            }

            return chattool;
        }
        else {
            return null;
        }
   }
}
