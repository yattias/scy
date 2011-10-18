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
import eu.scy.client.desktop.desktoputils.XFX;


/**
 * @author adam
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
        
        def chattool = ChatterNode{
            scyWindow: scyWindow
        };

        XFX.runActionInBackground(function(): Void {
            if(eloUri != null) {
                var metadataFirstVersion = repository.retrieveMetadataFirstVersion(eloUri);

                var identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
                var firstVersionELOURI = metadataFirstVersion.getMetadataValueContainer(identifierKey).getValue() as URI;

                var roomId = firstVersionELOURI.toString();

                roomId = StringUtils.remove(roomId, "/");
                roomId = StringUtils.remove(roomId, ".");
                roomId = StringUtils.remove(roomId, ":");
                chatController = chatControllerMap.get(roomId) as ChatController;

                if( chatController == null ) {
                    chatController = new MUCChatController(awarenessService, roomId);
                    chatControllerMap.put(roomId, chatController);
                }

                def presenceListener = PresenceListener {
                    ownershipManager: scyWindow.ownershipManager;
                    chattool: chattool;
                    roomId: roomId;
                };

                chatController.getAwarenessService().addAwarenessPresenceListener(presenceListener);
                chatController.connectToRoom();
                chattool.chatController = chatController;
                chatController.registerChat(chattool);

                var metadata = repository.retrieveMetadata(eloUri);

                def technicalFormat = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT)).getValue() as String;

                if ("scy/chat".equals(technicalFormat)) {
                    var buddies:String[];
                    var authors = (metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR)).getValueList()) as List;
                    if (authors != null) {
                        for (author in authors) {
                            FX.deferAction(function() : Void {
                                if (author instanceof String) {
                                    insert (author as String) into buddies;
                                } else if (author instanceof Contribute) {
                                    insert ((author as Contribute).getVCard()) into buddies;
                                }
                            });
                        }
                    }
                    for (buddy in buddies) {
                        if (not buddy.equals(toolBrokerAPI.getLoginUserName())) {
                            // todo check if user is already in mission
                            chatController.sendInvitation(buddy);
                        }
                    }
                }
            }
        }, "ChatInitializationThread");
        return chattool;
   }
}
