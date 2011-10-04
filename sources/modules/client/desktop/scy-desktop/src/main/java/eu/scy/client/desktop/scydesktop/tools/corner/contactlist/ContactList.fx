package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;
/*
 * ListView.fx
 *
 * Created on 02.12.2009, 13:26:45
 */

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.util.Math;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.search.IQueryComponent;
import roolo.search.AndQuery;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataKey;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import java.lang.System;
import roolo.elo.metadata.keys.Contribute;
import roolo.search.ISearchResult;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.lang.String;

/**
 * @author Sven
 */
public class ContactList extends CustomNode {

    public-init var dragAndDropManager: DragAndDropManager;
    public var tooltipManager: TooltipManager;
    public-init var scyDesktop: ScyDesktop;
    public-init var showOfflineContacts: Boolean = true;
    public-init var columns: Integer;
    public-init var stateIndicatorOpacity: Boolean = true;
    public var filter: Boolean = false on replace{createContactFrames()};
    public var filterId: String on replace{createContactFrames()};
    def chatManager: ChatManager = ChatManager {};
    def contactTooltipCreator = ContactTooltipCreator {
            }
    var awarenessServiceWrapper: AwarenessServiceWrapper;
    public var toolBrokerAPI: ToolBrokerAPI;
    public var awarenessService: IAwarenessService;
    public var repository: IRepository;
    var technicalTypeKey: IMetadataKey;
    var authorKey: IMetadataKey;
    var titleKey: IMetadataKey;
    var uriKey: IMetadataKey;
    public var contacts: Contact[] on replace {
                createContactFrames();
            };

    function createContactFrames(): Void {
        //TODO check if contact already exists -> UserID
        var contactFrames: ContactFrame[];
        for (contact in contacts) {
            if((not filter ) or (filter and  filterId.equalsIgnoreCase(contact.awarenessUser.getStatus()) )) {

                def contactFrame: ContactFrame = ContactFrame {
                            contact: contact;
                            onMousePressed: function(e: MouseEvent) {
                                startDragging(e, contactFrame);
                            };
                            onMouseClicked: function(e: MouseEvent) {
                                if (e.clickCount >= 2) {
                                    openChat(contact.name);
                                }
                            }
                        };
                //set indicators of online state by opacity (online, away, offline)
                if (stateIndicatorOpacity) {
                    contactFrame.opacity = if (contact.onlineState == OnlineState.AWAY) 0.6 else (if (contact.onlineState == OnlineState.OFFLINE) 0.3 else 1)
                }
                tooltipManager.registerNode(contactFrame, contactTooltipCreator);
                insert contactFrame into contactFrames;
            }
        }
        calculateMultiColumnContent(columns, contactFrames);
    }

    public function openChat(contact: String): Void {
        var chatWindow: ScyWindow = null;
        // first check if we have this URI already registered then the chat is open and we ignore
        var eloUri: URI = chatManager.getChatMappingForUser(contact);
        if (eloUri != null) {
            chatWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(eloUri);
            if (chatWindow != null) {
                if (chatWindow.isClosed) {
                    chatWindow.open();
                }
                return;
            }
        } else {
            // if not try to find the ELO from roolo
            def technicalTypeQuery: IQueryComponent = new MetadataQueryComponent(technicalTypeKey, "scy/chat");
            def firstAuthorQuery: IQueryComponent = new MetadataQueryComponent(authorKey, toolBrokerAPI.getLoginUserName());
            def secondAuthorQuery: IQueryComponent = new MetadataQueryComponent(authorKey, contact);

            def andQuery = new AndQuery(technicalTypeQuery, firstAuthorQuery, secondAuthorQuery);
            var searchResultList = toolBrokerAPI.getRepository().search(new Query(andQuery));

            for (searchResultObject in searchResultList) {
                eloUri = (searchResultObject as ISearchResult).getUri();
            }
        }
        // if still not found we create a new chat ELO
        if (eloUri == null) {
            def elo = toolBrokerAPI.getELOFactory().createELO();
            elo.getMetadata().getMetadataValueContainer(technicalTypeKey).setValue("scy/chat");
            def timestamp = System.currentTimeMillis();
            elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute(toolBrokerAPI.getLoginUserName(), timestamp));
            elo.getMetadata().getMetadataValueContainer(authorKey).addValue(new Contribute(contact, timestamp));
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue("Chat");
            def content = toolBrokerAPI.getELOFactory().createContent();
            // we do not need to set any "meaningful" content (yet) as the chat room id is derived from the ELO URI
            // however if content is null then EloSearchNode.loadElo throuws an exception (NPE)
            content.setXmlString("<chat />");
            elo.setContent(content);
            def newMetadata = toolBrokerAPI.getRepository().addNewELO(elo);
            eloUri = newMetadata.getMetadataValueContainer(uriKey).getValue() as URI;
        }

        // now register for next event
        chatManager.addChatMapping(contact, eloUri);

        chatWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(eloUri);
        if (chatWindow == null) {
            chatWindow = scyDesktop.scyWindowControl.addOtherScyWindow(eloUri);
            chatWindow.openWindow(100, 100, 250, 300);
        }
    }

    function startDragging(e: MouseEvent, dragObject: ContactFrame) {
        var dragNode = Group {
                    content: [
                        ContactFrame {
                            contact: dragObject.contact
                        }
                    ]
                }
        dragAndDropManager.startDrag(dragNode, dragObject, this, e);
    }

    public var content: Node[];
    public var width = WindowSize.DEFAULT_ITEM_WIDTH * columns;
    public var height = 250;
    public var itemWidth = WindowSize.DEFAULT_WIDTH;
    override var focusTraversable = true;

    //organize by column
    function calculateMultiColumnContent(columns: Integer,  contactFrames: ContactFrame[]): Void {
        var itemHeight = if ((sizeof contactFrames) > 0) contactFrames[0].boundsInParent.height else 10;
        delete  content;
        if (sizeof contactFrames > 0) {
            var count = 0;
            var box: HBox = HBox {};
            for (i in [1..columns]) {
                insert VBox { content: [] } into box.content;
            }

            def contactsPerColumn: Integer = Math.ceil((sizeof contactFrames as Double) / columns as Double) as Integer;

            for(contactFrame in contactFrames){
                //insert into column:
                def columnIndex = count / contactsPerColumn;
                //get the vbox
                def vbox: VBox = box.content[columnIndex] as VBox;
                insert contactFrame into vbox.content;
                count++;
            }

            //setting width of the scrollbar dependent on width of contactFrames
            if (sizeof contactFrames > 0) {
                this.width = (contactFrames[0].boundsInLocal.width * columns + 10) as Integer;

            }
            insert box into content;
        } else {
            def group = Group { content: [] };
            insert group into content;
        }
    }

    override function create(): Node {
        Group {
            content: [
                Group {
                    content: bind content
                }
            ]
        }
    }

    init {
        toolBrokerAPI = scyDesktop.config.getToolBrokerAPI();
        awarenessService = toolBrokerAPI.getAwarenessService();
        repository = toolBrokerAPI.getRepository();

        technicalTypeKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
        authorKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
        titleKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE);
        uriKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);

        awarenessServiceWrapper = AwarenessServiceWrapper {
                    contactlist: this;
                };
//        createContactFrames();
    }

}

function run() {
}


