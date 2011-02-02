/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor;
import javafx.scene.CustomNode;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import eu.scy.client.tools.fxformauthor.datamodel.FormEventDataType;
import eu.scy.client.tools.fxformauthor.datamodel.FormEventType;

/**
 * @author pg
 */

public class FormElementEventsItem extends CustomNode {
    public-init var eventsList:FormElementEvents;

    var typeLabel:Text = Text {
        font: Font { size: 14 }
        content: "Eventtype:"
    }
    var dataTypeLabel:Text = Text {
        font: Font { size: 14 }
        content: "Eventdatatype:"
    }
    public var typeChoice:ChoiceBox = ChoiceBox {
        items: ["ONBEFORE", "ONAFTER"]
    }
    public var dataChoice:ChoiceBox = ChoiceBox {
        items: ["GPS", "TIME", "DATE"]
    }

    def eventRemoveButton:Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/bomb.png" } }
        tooltip: Tooltip { text: "remove event" }
        action:function():Void {
            eventsList.removeEventItem(this);
        }
    }

    var content:HBox = HBox {
        spacing: 15.0;
        padding: Insets { bottom: 2; top: 2; left: 5; right: 5; }
        content: [typeLabel, typeChoice, dataTypeLabel, dataChoice, eventRemoveButton]
    };
    var backgroundRectangle:Rectangle = Rectangle {
        stroke: Color.BLACK;
        fill: Color.TRANSPARENT;
        height: bind content.layoutBounds.height;
        width: bind content.layoutBounds.width;
    }

    override var children = bind [backgroundRectangle, content];

    public function setDataType(type:String) {
        var items = dataChoice.items;
        var i:Number = 0;
        for(item in items) {
            if(item.equals(type)) {
                dataChoice.select(i);
            }
            i++;
        }
    }

    public function setEventType(type:String) {
        var items = typeChoice.items;
        var i:Number = 0;
        for(item in items) {
            if(item.equals(type)) {
                typeChoice.select(i);
            }
            i++;
        }
    }

    public function setDataType(datatype:FormEventDataType):Void {
        dataChoice.select(datatype.ordinal());
    }

    public function setEventType(type:FormEventType):Void {
        typeChoice.select(type.ordinal());
    }


    public function getDataType():FormEventDataType {
        return FormEventDataType.valueOf(dataChoice.selectedItem as String);
        //return (dataChoice.selectedItem as FormEventDataType);
    }

    public function getEventType():FormEventType {
        return FormEventType.valueOf(typeChoice.selectedItem as String);
        //return (typeChoice.selectedItem as FormEventType);
    }







}
