/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataEvent;
import eu.scy.client.tools.fxformauthor.datamodel.FormEventDataType;
import eu.scy.client.tools.fxformauthor.viewer.FormViewer;
import eu.scy.client.tools.fxformauthor.viewer.element.gps.MapWrapper;
import javafx.scene.CustomNode;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.transition.FadeTransition;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;

/**
 * @author pg
 */

public abstract class AbstractElementView extends CustomNode, IFormViewElement {
   public var fde:FormDataElement;
    public var viewer:FormViewer;
    public var title:String;

    public-read var width:Number = bind viewer.width-5;
    public-read var height:Number = bind viewer.height-5;
    override var children = bind [backgroundRectangle, contentBox];
    public var dataDisplay:Node[];

    def titleText:Text = Text {
        content: bind title;
    }

    var eventsOpen:Boolean = false;

    var eventNodes:Node[];
    var eventVBox = VBox {}
    var eventView:Node[];
    var eventButton : Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/zoom.png" } }
        tooltip: Tooltip { text: "open events" }
        layoutInfo: LayoutInfo { hpos: HPos.RIGHT }
        action: function():Void {
            //check for elemens
            if(fde.getEvents().size() > 0) {
                if(eventsOpen) {
                    hideEvents();
                    eventButton.tooltip.text = "open Events";
                    eventButton.graphic = ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/zoom.png" } }
                    eventsOpen = false;
                }
                else {
                    showEvents();
                    eventButton.tooltip.text = "close Events";
                    eventButton.graphic = ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/zoom_out.png" } }
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
            eventNodes,

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
        abstractView: this;
    };

    var mapHeightWatch = bind viewer.scyWindow.height on replace { mapWrapper.setSize(mapWidthWatch-10, mapHeightWatch-10); }
    var mapWidthWatch = bind viewer.scyWindow.width on replace { mapWrapper.setSize(mapWidthWatch-10, mapHeightWatch-10); }

    postinit {
        if(fde == null) {
            println("FATAL ERROR: FDE IS NOT SUPPOSED TO BE NULL! :-(");
        }
       // insert mapWrapper into viewer.foreground;
        //loadFormElement();
    }

    public function showEvents():Void {

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
                        } into eventVBox.content;
                    }
                    else {
                        insert Text {
                            content: new String(event.getData());
                        } into eventVBox.content;
                    }
                }
                if(event.getDataType() == FormEventDataType.DATE) {
                    insert Text {
                        content: "DataType: DATE. Type: {event.getType()}"
                    } into eventVBox.content;
                }
                if(event.getDataType() == FormEventDataType.GPS) {
                    insert Text {
                        content: "DataType: GPS. Type: {event.getType()}"
                    } into eventVBox.content;
                    /*
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
                    */
                    if(event.getData() == null) {
                        insert Text {
                            content: "No Data Found =("
                        } into eventVBox.content;
                    }
                    else {
                        var gps:Float[] = GPSTypeParser.getCoords(event.getData());
                        if(sizeof gps == 2) {
                            insert Button {
                                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/map_magnify.png" } }
                                tooltip: Tooltip { text: "open map" }
                                action: function():Void {
                                    showCoordinates(gps[0], gps[1]);
                                }
                            }
                            into eventVBox.content;
                        }

                    }

                }
                if(i < fde.getEvents().size()-1) {
                    insert Separator {} into eventVBox.content;
                }
            }
        }
        insert eventVBox into eventNodes;

    }

    function hideEvents() {
        delete eventNodes;
    }

    public function showCoordinates(x:Number, y:Number):Void {
        mapWrapper.addPosition(x, y, "SCY Position");
        mapWrapper.centerView(x, y);
        mapWrapper.setSize(viewer.scyWindow.width-10, viewer.scyWindow.height-10);
        mapWrapper.showMap();
        //mapWrapper.toFront();
        insert mapWrapper into viewer.foreground;
    }


    public function putImageToFront(img:Image):Void {
        def view:ImageView = ImageView {
            image: img;
            blocksMouse: true;
            fitHeight: bind viewer.scyWindow.height;
            fitWidth: bind viewer.scyWindow.width;
        }
        def closeBT:Button = Button {
            text: "close image";
            translateX: 15;
            translateY: 15;
            action: function():Void {
                var fadeOutTransition:FadeTransition = FadeTransition {
                    duration: 1s;
                    node: imageGroup;
                    byValue: 0.1
                    fromValue: 1.0 toValue: 0.0
                    action: function():Void {
                        println("uuund... ACTION!");
                        delete imageGroup from viewer.foreground;
                    }

                }

                fadeOutTransition.playFromStart();
                /*
                try {
                    Thread.sleep(1000);
                }
                catch(e:Exception) {
                    println("hacky hacks gone wild");
                }
                */


               // while(imageGroup.opacity > 0.0) {
               //      println("foobar")
               // }

                //deleteNaow
                //removeFromForeground(imageGroup);
                //delete imageGroup from viewer.foreground;
                //delete imageGroup from viewer.foreground;

            }
        }
        var imageGroup:Group = Group {
            opacity: 0.0;
            content: [view, closeBT]
        }
        var fadeInTransition = FadeTransition {
            duration: 1s;
            node: imageGroup;
            fromValue: 0.0 toValue: 1.0
        }
        //insert view into viewer.foreground;
        //insert closeBT into viewer.foreground;
        insert imageGroup into viewer.foreground;
        fadeInTransition.play();
    }

    function removeFromForeground(item:Node) {
        delete item from viewer.foreground;
    }

    public function removeMap() {
        removeFromForeground(mapWrapper);
    }



    override abstract public function loadFormElement (fde : FormDataElement);


}
