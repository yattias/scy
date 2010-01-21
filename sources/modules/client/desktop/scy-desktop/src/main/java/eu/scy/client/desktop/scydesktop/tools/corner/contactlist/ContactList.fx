package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;
/*
 * ListView.fx
 *
 * Created on 02.12.2009, 13:26:45
 */

import javafx.scene.control.ScrollBar;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.util.Math;

import javafx.scene.shape.Rectangle;

import javafx.scene.layout.HBox;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import com.sun.javafx.scene.control.caspian.ScrollBarSkin;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * @author Sven
 */
public class ContactList extends CustomNode {

    public-init var dragAndDropManager:DragAndDropManager;
    public-init var awarenessService:IAwarenessService;

    public override var clip =
            Rectangle {
                width: bind this.width
                height: bind this.height
                fill: Color.BLUE;
            };
    public def background = Rectangle {
                width: bind this.width;
                height: bind this.height
                fill: Color.TRANSPARENT;
            }
    public var contacts: Contact[] on replace {
                createContactFrames()
            };
    def dummyContact:ContactFrame = ContactFrame {
                contact: Contact {};
                onMouseEntered: function (e: MouseEvent) {
                };
                onMouseExited: function (e: MouseEvent) {
                };
                onMousePressed: function (e: MouseEvent) {
                };
            };

    function createContactFrames(): Void {
        //TODO check if inserting contacts at runtime affects this in a negative way
        //TODO check if contact already exists -> UserID
        //TODO only show online contacts
        delete  contactFrames;
        for (contact in contacts) {
            def contactFrame: ContactFrame = ContactFrame {
                        contact: contact;
                        onMousePressed: function (e: MouseEvent) {
                            contactFrame.expand();
                            unexpandOtherFrames(contactFrame);
                            startDragging(e,contactFrame);
                        };
                    };
            insert contactFrame into contactFrames;
        }
        insert dummyContact into contactFrames;
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
    public var contactFrames: ContactFrame[] on replace {
                def size = sizeof contactFrames;
                if (size > 0) {
                    itemHeight = contactFrames[0].height as Integer;
                    itemWidth = contactFrames[0].width as Integer;
                    height = size * itemHeight as Integer;
                } else {
                    itemHeight = WindowSize.DEFAULT_HEIGHT;
                    itemWidth = WindowSize.DEFAULT_WIDTH;
                }
            };
    //public var twoColumnContent: Node[] = bind calculateMultiColumnContent(2);
    //public var content:Node[] = twoColumnContent;
    public var content: ContactFrame[] = contactFrames;
    public var width = WindowSize.DEFAULT_ITEM_WIDTH;
    public var height = 500;
//    public var listItem : ListItem[];
    public var itemHeight = if ((sizeof contactFrames) > 0) contactFrames[0].height else 10;
    public var itemWidth = WindowSize.DEFAULT_WIDTH;
//    public var selectedListItem : ListItem;
    override var focusTraversable = true;
    var scrollBar: ScrollBar = ScrollBar {
                translateX: bind (this.width - scrollBar.width)
                translateY: 0
                min: 0
                max: bind Math.max((listView.boundsInLocal.height - height), 0)
                vertical: true
                height: bind height;
                blockIncrement: itemHeight
                focusTraversable: false
                blocksMouse: true
                skin: ScrollBarSkin {
                }
            };
    var listView = VBox {
                height: bind height
                width: bind width
                spacing: 0
                translateX: 2
                translateY: bind -(scrollBar.value) + 2
                content: content;
                focusTraversable: false
            };
    override var onMouseWheelMoved = function (e: MouseEvent) {
                scrollBar.adjustValue(e.wheelRotation);
            }

    override var onKeyPressed = function (e) {
                if (e.code == KeyCode.VK_UP) {
                    scrollBar.adjustValue(-1);
                } else if (e.code == KeyCode.VK_DOWN) {
                    scrollBar.adjustValue(1);
                }
            }
//    public var twoColumnContent: Node[] = calculateMultiColumnContent(2) on replace {
//                twoColumnContent = calculateMultiColumnContent(2)
    //          }
    def scrollHelperAtBottom: Rectangle = Rectangle {
                translateX: bind this.translateX;
                translateY: bind this.height - 50;
                height: 50;
                width: bind this.width;
                fill: Color.TRANSPARENT;
                onMouseEntered: function (e: MouseEvent) {
                    scrollBar.adjustValue(1);
                }
            }

    public function calculateMultiColumnContent(columns: Integer): Node[] {
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
        //this.width = contactFrames[0].boundsInLocal.width*2+10;
        return boxes;
    }

    override function create(): Node {

        Group {
            content: [
                Group {
                    content: [background, listView]
                },
                scrollBar
            ]
        }
    }

    init {

    }
}

function run() {
}


