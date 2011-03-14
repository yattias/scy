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

/**
 * @author Sven
 */
public class ContactList extends CustomNode {

    public-init var dragAndDropManager:DragAndDropManager;
    public var tooltipManager: TooltipManager;
    public-init var scyDesktop:ScyDesktop;
    public-init var showOfflineContacts:Boolean = true;
    public-init var columns: Integer;
    public-init var stateIndicatorOpacity:Boolean = true;

    def contactTooltipCreator = ContactTooltipCreator{
   }

    var awarenessServiceWrapper:AwarenessServiceWrapper;

    public var toolBrokerAPI;
    public var awarenessService:IAwarenessService;

    public var contacts: Contact[] on replace {
                createContactFrames();
                this.content = calculateMultiColumnContent(columns);
            };

    function createContactFrames(): Void {
        //TODO check if contact already exists -> UserID
        delete  contactFrames;
        for (contact in contacts) {
            def contactFrame: ContactFrame = ContactFrame {
                        contact: contact;
                        onMousePressed: function (e: MouseEvent) {
                            //contactFrame.expand();
                            unexpandOtherFrames(contactFrame);
                            startDragging(e,contactFrame);
                        };
                    };
                    //set indicators of online state by opacity (online, away, offline)
                    if (stateIndicatorOpacity){
                        contactFrame.opacity = if (contact.onlineState==OnlineState.AWAY) 0.6 else (if (contact.onlineState==OnlineState.OFFLINE) 0.3 else 1)
                    }
            tooltipManager.registerNode(contactFrame, contactTooltipCreator);
            insert contactFrame into contactFrames;
        }
    }

    function startDragging(e:MouseEvent, dragObject:ContactFrame){
        var dragNode = Group{
	content:[
                ContactFrame{
                        contact: dragObject.contact
                        }
            ]
        }
        dragAndDropManager.startDrag(dragNode, dragObject,this,e);
    }


    function unexpandOtherFrames(contactFrame: ContactFrame): Void {
        for (frame in contactFrames[node | not contactFrame.equals(node)]) {
            frame.reduce();
            }

    }
    public var contactFrames: ContactFrame[];
    public var content:Node[] = calculateMultiColumnContent(columns);
    public var width = WindowSize.DEFAULT_ITEM_WIDTH * columns;
    public var height = 250;
    public var itemHeight = if ((sizeof contactFrames) > 0) contactFrames[0].boundsInParent.height else 10;
    public var itemWidth = WindowSize.DEFAULT_WIDTH;
    override var focusTraversable = true;
   

    //organize by column
    public function calculateMultiColumnContent(columns: Integer): Node {
        delete content;
        if (sizeof contactFrames >0){
        var count = 0;
        var box: HBox = HBox{};
        for (i in [1..columns]){
            insert VBox {content: []} into box.content;
        }

        def contactsPerColumn : Integer = Math.ceil((sizeof contactFrames as Double) / columns as Double) as Integer;

        while (sizeof contactFrames>0){
             //insert into column:
            def node:ContactFrame = contactFrames[0];
            def columnIndex = count/ contactsPerColumn;
            //get the vbox
            def vbox: VBox = box.content[columnIndex] as VBox;
            delete node from contactFrames;
            insert node into vbox.content;
            count++;
        }

        //setting width of the scrollbar dependent on width of contactFrames
        if(sizeof contactFrames>0){
            this.width = (contactFrames[0].boundsInLocal.width*columns+10) as Integer;
            
        }
        return box;
        } else {
            return Group{content:[]};
        }

    }

    //organize by line
    public function calculateMultiColumnContent2(columns: Integer): Node[] {
        var count = 0;
        var boxes: HBox[] = [];

        for (node in contactFrames) {
            if (count mod columns == 0) {
                insert HBox {content: []} into boxes;
            }
            delete node from contactFrames;
            insert node into (boxes[(count / columns)].content);
            count++;
        }

        //setting width of the scrollbar dependent on width of contactFrames
        if(sizeof contactFrames>0){
            this.width = (contactFrames[0].boundsInLocal.width*columns+10) as Integer;
        }
        return boxes;
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
        awarenessServiceWrapper = AwarenessServiceWrapper{
          contactlist:this;
        };
    }
}

function run() {
}


