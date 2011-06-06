/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxformauthor;

import javafx.scene.CustomNode;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.control.ScrollBarPolicy;
import javafx.scene.control.ScrollView;
import eu.scy.client.tools.fxformauthor.datamodel.FormEventDataType;
import eu.scy.client.tools.fxformauthor.datamodel.FormEventType;

/**
 * @author pg
 */
public class FormElementEvents extends CustomNode {

    public-init var formList: FormList;
    //controll bar
    def addItemButton: Button = Button {
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/textfield_add.png" } }
                tooltip: Tooltip { text: "add event" }
                action: function(): Void {
                    insert FormElementEventsItem { eventsList: this } into itemList.content;
                }
            }
    def closeButton: Button = Button {
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/application_form_delete.png" } }
                tooltip: Tooltip { text: "close event view" }
                action: function(): Void {
                    formList.hideElementEvents(this);
                }
            }
    def controlBar: HBox = HBox {
                content: [addItemButton, closeButton]
                spacing: 2.0
            }
    //content box:
    var itemList: VBox = VBox {
                spacing: -2.0;
                padding: Insets { bottom: 2; top: 2; left: 5; right: 5; }
                content: [];
            }
    var sv:ScrollView = ScrollView {
        node: itemList;
        style: "-fx-background-color: transparent;"
        vbarPolicy: ScrollBarPolicy.AS_NEEDED;
        hbarPolicy: ScrollBarPolicy.NEVER;
        layoutInfo: LayoutInfo{
            height: bind formList.scyWindow.height-formList.headerHeight-60;
            width: bind itemList.layoutBounds.width+10;
        }
    }
    //central item:
    def content: VBox = VBox {
                content: [controlBar, sv]
            }
    var backgroundRectangle: Rectangle = Rectangle {
                fill: bind formList.backgroundColor;
                height: bind formList.scyWindow.height - formList.headerHeight;
                width: bind formList.scyWindow.width;
            }
    var nodes: Node[] = [backgroundRectangle, content];
    //customnode
    override var children = bind nodes;

    public-read var count = bind itemList.content.size();
    //random
/*
    var player:MediaPlayer = MediaPlayer {
        media: Media {

            source: "http://188.40.38.84/~pg/scy/explosion_glass.mp3"
        }
        repeatCount: MediaPlayer.REPEAT_NONE
        autoPlay: false
    }
*/ 

    postinit {
        this.translateY = formList.headerHeight-20;
        this.layoutInfo = LayoutInfo {
            height: bind formList.scyWindow.height - formList.headerHeight;
            width: bind formList.scyWindow.width;
        }

    }

    //functions
    public function removeEventItem(eventItem: FormElementEventsItem): Void {

        //player.play();
        delete eventItem from itemList.content;
    }

    public function addEventItem(eventItem: FormElementEventsItem): Void {
        insert eventItem into itemList.content;
    }
    public function addEventItem(type:String, datatype:String): Void {
        var feei:FormElementEventsItem = FormElementEventsItem { eventsList: this }
        feei.setEventType(type);
        feei.setDataType(datatype);
        insert feei into itemList.content;
    }

    public function addEventItem(type:FormEventType, datatype:FormEventDataType):Void {
       var feei:FormElementEventsItem = FormElementEventsItem { eventsList: this }
       feei.setEventType(type);
       feei.setDataType(datatype);
       insert feei into itemList.content;
    }


    public function getEventItems():FormElementEventsItem[] {
        return (itemList.content as FormElementEventsItem[]);
    }

}
