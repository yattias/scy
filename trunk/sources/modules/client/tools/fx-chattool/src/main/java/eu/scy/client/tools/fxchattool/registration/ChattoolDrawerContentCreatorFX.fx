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
import org.apache.log4j.Logger;
import java.util.*;
import eu.scy.chat.controller.*;
import org.apache.commons.lang.StringUtils;

/**
 * @author jeremyt
 */

public class ChattoolDrawerContentCreatorFX extends DrawerContentCreatorFX {

    override public function getDrawerContent (eloUri:URI, scyWindow:ScyWindow) : Node {
         println("ChattoolDrawerContentCreatorFX ELOURI: {eloUri}");
         println("ChattoolDrawerContentCreatorFX ELO ID: {eloId}");
         return createChatToolNode(scyWindow,eloUri);
    }

    public var node:Node;
    public var awarenessService:IAwarenessService;
    public var chatController:ChatController;
    public var eloId:String;
    public var chatControllerMap:HashMap;

   //public var metadataTypeManager: IMetadataTypeManager;
   // public var repository:IRepository;

    function createChatToolNode(scyWindow:ScyWindow,eloUri:URI):ChatToolNode {

        println("ELO ID nooooode {eloUri.toString()}");

        var s = eloUri.toString();

        s = StringUtils.remove(s, "/");
        s = StringUtils.remove(s, ".");
        s = StringUtils.remove(s, ":");
        println("new string chat tool drawer: {s}" );
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
        

        return ChatToolNode{chatTool:chatTool;}
   }
}
