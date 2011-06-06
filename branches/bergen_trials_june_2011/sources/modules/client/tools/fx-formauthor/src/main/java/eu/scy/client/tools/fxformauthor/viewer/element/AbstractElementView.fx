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
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import eu.scy.client.tools.fxformauthor.datamodel.FormEventType;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Button;

/**
 * @author pg
 */

public abstract class AbstractElementView extends CustomNode, IFormViewElement {
   public var fde:FormDataElement;
    public var viewer:FormViewer;
    public var title:String;

    public-read var width:Number = bind viewer.width-5;
    public-read var height:Number = bind viewer.height-5;
    public-read var defaultTextFont:Font = Font {
        size: 15;
    }

    public-read var defaultErrorFont:Font = Font {
        size: 15;
        
    }


    override var children = bind [backgroundRectangle, numberBox, buttonBox, contentBox];
    public var dataDisplay:Node[];

    public var itemList:ArrayList = new ArrayList();

    def titleText:Text = Text {
        font: Font { size: 14 }
        content: bind title;
    }

    var eventsOpen:Boolean = false;

    var eventNodes:Node[];
    var eventVBox = VBox {}
    var eventView:Node[];
    var gps:Boolean = false;
    //public var currentPosition = 1;
    var eventButton : Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/zoom.png" } }
        tooltip: Tooltip { text: "open events" }
        layoutInfo: LayoutInfo { hpos: HPos.RIGHT }
        action: function():Void {
            //check for elemens
            if(fde.getEvents().size() > 0) {
                if(eventsOpen) {
                    hideEvents();
                    /*
                    if(gps) {
                        this.translateX = 0;
                    }
                    */

                    eventButton.tooltip.text = "open Events";
                    eventButton.graphic = ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/zoom.png" } }
                    eventsOpen = false;
                }
                else {
                    showEvents();
                    eventButton.tooltip.text = "close Events";
                    eventButton.graphic = ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/zoom_out.png" } }
                    eventsOpen = true;
                    /*
                    if(gps) {
                        this.translateX = -2;
                    }*/
                }
            }
        }
    }
    /*
    var contentBox:VBox = VBox {
        content: bind [
            titleText,
            Separator{
                opacity: 0.0;
                },
            //dataDisplay,
            HBox {
                content: bind [itemToDisplay]
                translateX: 45;
                width: bind width-100;
            },
            //eventButton,
            //eventNodes
            eventBar 
            ]
        padding: Insets { bottom: 2; top: 2; left: 5; right: 5; }
        layoutInfo: LayoutInfo { width: bind width-10;} 
        translateY: 10;
    }*/
    var contentBox:HBox = HBox {
        content: bind [
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/resultset_previous.png" } }
                opacity: 0.0;
                layoutInfo: LayoutInfo {
                    width: 26;
                    height: 20;
                }
            },
            VBox {
                content: [
                    titleText,
                    Separator{
                        opacity: 0.0;
                    },
                    //dataDisplay,
                    HBox {
                        content: bind [itemToDisplay]
                        translateX: 45;
                        width: bind width-100;
                    },
                    //eventButton,
                    //eventNodes
                    eventBar
                    ]
            },
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/resultset_previous.png" } }
                opacity: 0.0;
                layoutInfo: LayoutInfo {
                    width: 26;
                    height: 20;
                }
            }
            ]
        padding: Insets { bottom: 2; top: 2; left: 5; right: 5; }
        layoutInfo: LayoutInfo { width: bind width-10;}
        translateY: 10;
    }

    var eventBar:HBox = HBox{
            translateY: 10;

        };

    var backgroundRectangle:Rectangle = Rectangle {
        height: bind contentBox.layoutBounds.height+30;
        width: bind contentBox.layoutBounds.width;
        stroke: Color.BLACK;
        fill: Color.TRANSPARENT;
    }

    // UI improvements start here :3 <- KATZE


    var currentItem:Integer = -1;
    var maxItem:Integer = -1;
    var itemToDisplay:Node;
    
    var numberRectangle = Rectangle {
        translateX: backgroundRectangle.x + 20;
        translateY: backgroundRectangle.y - 10;
        height: 20;
        width: 35;
        fill: Color.WHITE;
        stroke: Color.BLACK;
        arcWidth: 8;
        arcHeight: 8;
    }

    var numberText:Text = Text {
        content: bind "{currentItem+1} / {fde.getUsedCardinality()}";
        translateX: numberRectangle.translateX + 5;
        translateY: numberRectangle.translateY + 12;
    }

    var numberBox:Node[] = [numberRectangle, numberText];

    var prevBT:Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/resultset_previous.png" } }
        tooltip: Tooltip { text: "previous item" }
        translateX: bind backgroundRectangle.x-13;//-15;
        translateY: bind backgroundRectangle.y+(backgroundRectangle.layoutBounds.height/2)-15;
        action:function():Void {
            showPrevious();
        }
        layoutInfo: LayoutInfo {
            width: 26;
            height: 20;
        }
    }
    
    var nextBT:Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/resultset_next.png" } }
        tooltip: Tooltip { text: "next item" }
        translateX: bind backgroundRectangle.x + backgroundRectangle.layoutBounds.width - 42+28;//+28;
        translateY: bind backgroundRectangle.y + (backgroundRectangle.layoutBounds.height/2)-15;
        action:function():Void {
            showNext();
        }
        layoutInfo: LayoutInfo {
            width: 26;
            height: 20;
        }
        
    }

    var buttonBox:Node[] = [prevBT, nextBT];

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
        loadFormElement();
        setUItoStart();
    }

    public function showEvents():Void {

        if(sizeof eventVBox.content == 0) {
            insert Separator {} into eventVBox.content;
            var beforeBox:VBox = VBox{ spacing: 2.0; content: []};
            var beforeHBox:HBox = HBox{ spacing: 5.0; content: [Text{content: "onBefore: " }, beforeBox]};
            var afterBox:VBox = VBox{ spacing: 2.0; content: []};
            var afterHBox:HBox = HBox{ spacing: 5.0; content: [Text{content: "onAfter: " }, beforeBox]};
            var isAfter:Boolean = false;
            var isBefore:Boolean = false;
            for(i in [0..fde.getEvents().size()-1]) {
                var event:FormDataEvent = fde.getEvents().get(i);
                var dataList:HBox = HBox{};
                if(event.getDataType() == FormEventDataType.TIME) {
                    println("time <:");
                    println(new String(event.getData()));
                    insert Text {
                        content: "Time: "
                    }
                    into dataList.content;
                    if(event.getData() == null) {
                        insert Text {
                            content: "No Data Found =("
                        } into dataList.content;

                    }
                    else {
                        insert Text {
                            content: new String(event.getData());
                        } into dataList.content;
                    }
                }
                if(event.getDataType() == FormEventDataType.DATE) {
                    insert Text {
                        content: "Date: "
                    } into dataList.content;
                    if(not (event.getData() == null)) {
                        insert Text {
                            content: new String(event.getData());
                        } into dataList.content;
                    }
                }
                if(event.getDataType() == FormEventDataType.GPS) {
                    insert Text {
                        content: "GPS: "
                    } into dataList.content;
                    if(event.getData() == null) {
                        insert Text {
                            content: "No Data Found =("
                        } into dataList.content;
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
                            into dataList.content;
                        }
                    }
                }
                if(event.getType() == FormEventType.ONAFTER) {
                    insert dataList into afterBox.content;
                    isAfter = true;
                }
                else {
                    insert dataList into  beforeBox.content;
                    isBefore = true;
                }
            }
            if(isBefore) {
                insert beforeBox into beforeHBox.content;
                insert beforeHBox into eventVBox.content;
            }
            if(isAfter) {
                insert afterBox into afterHBox.content;
                insert afterHBox into eventVBox.content;
            }

        }
        insert eventVBox into eventNodes;
    }

    function hideEvents():Void {
        delete eventNodes;
    }

    var gpsBefore:Byte[] = null;
    var gpsAfter:Byte[] = null;
    var dateBefore:Byte[] = null;
    var dateAfter:Byte[] = null;
    var timeBefore:Byte[] = null;
    var timeAfter:Byte[] = null;
    
    function loadEventBar():Void {
        var eventsBefore:HashMap = new HashMap();
        var eventsAfter:HashMap = new HashMap();
        //vorher: daten in hashmap werfen
        for(i in [0..fde.getEvents().size()-1]) {
            var event:FormDataEvent = fde.getEvents().get(i);
            if(event.getDataType() == FormEventDataType.TIME) {
                if(event.getType() == FormEventType.ONBEFORE) {
                    timeBefore = event.getData();
                }
                else {
                    timeAfter = event.getData();
                }
            }
            if(event.getDataType() == FormEventDataType.DATE) {
                if(event.getType() == FormEventType.ONBEFORE) {
                    dateBefore = event.getData();
                }
                else {
                    dateAfter = event.getData();
                }
            }
            if(event.getDataType() == FormEventDataType.GPS) {
                if(event.getType() == FormEventType.ONBEFORE) {
                    gpsBefore = event.getData();
                }
                else {
                    gpsAfter = event.getData();
                }
            }
         }

        var beforeBar:HBox = HBox {spacing: 2.0; content: [ Text{ font: defaultTextFont; content: "EventBefore: "} ]};
        var afterBar:HBox = HBox{spacing: 2.0; content: [Text{ font:defaultTextFont; content: "EventAfter: "} ]};
        //var timeGraphic = ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/clock.png" } }
        //var dateGraphic = ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/calendar.png" } }
        //var gpsGraphic = ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/map_magnify.png" } }
        var funkyBT:Button; 

        // TIME
        //before:
        funkyBT = Button {
            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/clock.png" } };
        }
        if(timeBefore == null) {
            funkyBT.tooltip = Tooltip{ text: "Time: no data." }
            funkyBT.disable = true;
        }
        else {
            funkyBT.tooltip = Tooltip{ text: "Time: {new String(timeBefore)}" }
        }
        insert funkyBT into beforeBar.content;
        //after:
        funkyBT = Button {
            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/clock.png" } };
        }
        if(timeAfter == null) {
            funkyBT.tooltip = Tooltip{ text: "Time: no data." }
            funkyBT.disable = true;
        }
        else {
            funkyBT.tooltip = Tooltip{ text: "Time: {new String(timeAfter)}" }
        }
        insert funkyBT into afterBar.content;
        //DATE
        //before
        funkyBT = Button {
            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/calendar.png" } };
        }
        if(dateBefore == null) {
            funkyBT.tooltip = Tooltip{ text: "Date: no data." }
            funkyBT.disable = true;
        }
        else {
            funkyBT.tooltip = Tooltip{ text: "Date: {new String(dateBefore)}" }
        }
        insert funkyBT into beforeBar.content;
        //after
        funkyBT = Button {
            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/calendar.png" } };
        }
        if(dateAfter == null) {
            funkyBT.tooltip = Tooltip{ text: "Date: no data." }
            funkyBT.disable = true;
        }
        else {
            funkyBT.tooltip = Tooltip{ text: "Date: {new String(dateAfter)}" }
        }
        insert funkyBT into afterBar.content;
        //GPS
        //before
        funkyBT = Button {
            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/map_magnify.png" } };
        }
        if(gpsBefore == null) {
            funkyBT.tooltip = Tooltip{ text: "GPS: no data." }
            funkyBT.disable = true;
        }
        else {
            var gps:Float[] = GPSTypeParser.getCoords(gpsBefore);
            funkyBT = Button {
                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/map_magnify.png" } };
                tooltip: Tooltip{ text: "GPS: click to open map." }
                action: function():Void {
                            showCoordinates(gps[0], gps[1]);
                }
            }
        }
        insert funkyBT into beforeBar.content;
        //after:
        funkyBT = Button {
            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/map_magnify.png" } };
        }
        if(gpsAfter == null) {
            funkyBT.tooltip = Tooltip{ text: "GPS: no data." }
            funkyBT.disable = true;
        }
        else {
            var gps:Float[] = GPSTypeParser.getCoords(gpsAfter);
            funkyBT = Button {
                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/map_magnify.png" } };
                tooltip: Tooltip{ text: "GPS: click to open map." }
                action: function():Void {
                            showCoordinates(gps[0], gps[1]);
                }
            }
        }
        insert funkyBT into afterBar.content;

        insert beforeBar into eventBar.content;
        insert afterBar into eventBar.content;
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
                    duration: 0.2s;
                    node: imageGroup;
                    byValue: 0.1
                    fromValue: 1.0 toValue: 0.0
                    action: function():Void {
                        delete imageGroup from viewer.foreground;
                    }

                }

                fadeOutTransition.playFromStart();
            }
        }
        var imageGroup:Group = Group {
            opacity: 0.0;
            content: [view, closeBT]
        }
        var fadeInTransition = FadeTransition {
            duration: 0.2s;
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

    /**
    * This might be a stupid method name, but this nice function will
    * set the gui to the 'start' mode, eg. display the first content element and set bounds for
    * NEXT and PREV buttons
    */
    function setUItoStart():Void {
        if(fde.getUsedCardinality() == 0) {
            currentItem = -1;
        }
        else {
            currentItem = 0; //we will display 1 -> calculate currentItem-1 to get the position in dataDisplay[]
        }
        maxItem = itemList.size()-1;
        //itemToDisplay = dataDisplay[0];
        if(itemList.size() > 0) {
            itemToDisplay = (itemList.get(0) as Node);
        }

        println("sizeof dataDisplay: {sizeof dataDisplay}");
        println("itemList.size(): {itemList.size()}");


        loadEventBar();
    }

    function showNext():Void {
        if(currentItem < maxItem) {
            currentItem++;
            itemToDisplay = (itemList.get(currentItem) as Node); //yeah, that could be one line above..
        }
    }

    function showPrevious():Void {
        if(currentItem > 0) {
            currentItem--;
            itemToDisplay = (itemList.get(currentItem) as Node);
        }

    }



    abstract public function loadFormElement():Void;
    
    override abstract public function loadFormElement (fde : FormDataElement);


}
