/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;
import javafx.scene.text.Text;
import org.apache.commons.codec.binary.Base64;
import javafx.scene.paint.Color;

/**
 * @author pg
 */

public class ElementViewText extends  IFormViewElement, AbstractElementView {
    /*
    public-init var fde:FormDataElement;
    public-init var viewer:FormViewer;
    public-read var title:String;

    var width:Number = bind viewer.width;
    override var children = bind [backgroundRectangle, contentBox];
    var dataDisplay:Node[];

    def titleText:Text = Text {
        content: bind title;
    }

    var eventsOpen:Boolean = false;

    var eventNodes:Node[];
    var eventVBox = VBox {}
    var eventView:Node[];
    var eventButton : Button = Button {
        text: "V"
        layoutInfo: LayoutInfo { hpos: HPos.RIGHT }
        action: function():Void {
            //check for elemens
            if(fde.getEvents().size() > 0) {
                if(eventsOpen) {
                    hideEvents();
                    eventButton.text = "V";
                    eventsOpen = false;
                }
                else {
                    showEvents();
                    eventButton.text = "^";
                    eventsOpen = true;
                }
            }
        }
    }
    var contentBox:VBox = VBox {
        content: bind [
            titleText,
            Separator{
                },
            dataDisplay,
            eventButton,
            eventNodes

            ]
        padding: Insets { bottom: 2; top: 2; left: 5; right: 5; }
        layoutInfo: LayoutInfo { width: bind width-10; }
    }

    var backgroundRectangle:Rectangle = Rectangle {
        height: bind contentBox.layoutBounds.height;
        width: bind contentBox.layoutBounds.width;
        stroke: Color.BLACK;
        fill: Color.TRANSPARENT;
    }
    var mapWrapper:MapWrapper = MapWrapper{
    };

    var mapHeightWatch = bind viewer.scyWindow.height on replace { mapWrapper.setSize(mapWidthWatch-10, mapHeightWatch-10); }
    var mapWidthWatch = bind viewer.scyWindow.width on replace { mapWrapper.setSize(mapWidthWatch-10, mapHeightWatch-10); }

    postinit {
        if(fde == null) {
            println("FATAL ERROR: FDE IS NOT SUPPOSED TO BE NULL! :-(");
        }
        loadFormElement();
    }

    public function showEvents():Void {
        //1: create everything, calculate bounds

        if(sizeof eventVBox.content == 0) {
            for(i in [0..fde.getEvents().size()-1]) {
                var event:FormDataEvent = fde.getEvents().get(i);
                if(event.getDataType() == FormEventDataType.TIME) {
                    insert Text {
                        content: "DataType: TIME. Type: {event.getType()}"
                    }
                    into eventVBox.content;
                    if(event.getData() == null) {
                        insert Text {
                            content: "No Data Found =("
                        }
                        into eventVBox.content;
                    }
                    else {
                        insert Text {
                            content: new String(event.getData());
                        }
                        into eventVBox.content;
                    }
                }
                if(event.getDataType() == FormEventDataType.DATE) {
                    insert Text {
                        content: "DataType: DATE. Type: {event.getType()}"
                    }
                    into eventVBox.content;
                }
                if(event.getDataType() == FormEventDataType.GPS) {
                    insert Text {
                        content: "DataType: GPS. Type: {event.getType()}"
                    }
                    into eventVBox.content;
                    insert mapWrapper into viewer.foreground;
                    insert Button {
                        text: "show map"
                        action: function():Void {
                            mapWrapper.addPosition(51.427783,6.800172, "UDE SCY Headquarters");
                            mapWrapper.centerView(51.427783,6.800172);
                            mapWrapper.setSize(viewer.scyWindow.width-10, viewer.scyWindow.height-10);
                            mapWrapper.showMap();
                        }
                    }
                    into eventVBox.content;


                }
                if(i < fde.getEvents().size()-1) {
                    insert Separator {} into eventVBox.content;
                }
            }
        }
        insert eventVBox into eventNodes;

        
        

        //2: increase backgroundRectangle height -> transition, keyframes, foobar
        // => dont increase backgroundRectangle, simply put something in the background that will rise;
        //3: display content
        //4: ????
        //5: PROFIT
        
    }

    function hideEvents() {
        delete eventNodes;
    }

*/
    postinit {
        //loadFormElement();
    }
    override override public function loadFormElement (fde : FormDataElement) : Void {
        if(fde != null) {
            this.fde = fde;
            loadFormElement();
        }
    }


    override function loadFormElement():Void {
        title = "Text: {fde.getTitle()}";
        //display data.. 
        if(fde.getUsedCardinality() > 0) {
            for(i in [0..fde.getUsedCardinality()-1]) {
                println("load form element.. {i}");
                var data:Byte[] = fde.getStoredData(i);
                if(data != null) {
                    var text = new String(Base64.decodeBase64(data));
                    itemList.add(Text {
                                    content: new String(data);
                                    wrappingWidth: bind width-20;
                                    font: bind defaultTextFont; 
                                });
                    //into dataDisplay;
                    println("data != null.. {text}");
                }
                else {
                    itemList.add(Text { content: "No Data Found. Entry #{i}"; }); // into dataDisplay;
                }
            }
        }
        if(itemList.size() == 0) {
            itemList.add(Text { font: defaultErrorFont; fill: Color.RED; content: "No Data Found." }); // into dataDisplay;
        }

    }


}