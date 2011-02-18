/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor;
import javafx.scene.CustomNode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import eu.scy.client.tools.fxformauthor.datamodel.FormElementDataType;
import eu.scy.client.tools.fxformauthor.datamodel.FormEventDataType;
import eu.scy.client.tools.fxformauthor.datamodel.FormEventType;

/**
 * @author pg
 */

public class FormElement extends CustomNode {
    public-init var formList:FormList;

    var content:HBox = HBox {
        spacing: 2.0;
        padding: Insets { bottom: 2; top: 2; left: 5; right: 5; }
    };
    var backgroundRectangle:Rectangle = Rectangle {
        height: bind content.layoutBounds.height;
        width: bind content.layoutBounds.width;
        fill: formList.backgroundColor;
        stroke: Color.BLACK;
        onMouseReleased:function(e:MouseEvent):Void {
            if(checked) {
                checked = false;
                backgroundRectangle.fill = formList.backgroundColor;
            }
            else {
                checked = true;
                backgroundRectangle.fill = Color.WHITE;
            }
        }
    }

    override var children = bind [backgroundRectangle, content];
    //gui items
    //label:
    def captionLabel:Text = Text {font: Font { size: 14} content: "Caption:"  }
    def typeLabel:Text = Text {font: Font { size: 14} content: "Type" }
    def cardinalityLabel:Text = Text {font: Font { size: 14} content: "Cardinality" }
    //input:
    def captionBox:TextBox = TextBox { 
        columns: 24;
        selectOnFocus: true;
        text: bind caption with inverse
    }
    def cardinalityBox:TextBox = TextBox {  columns: 3; selectOnFocus: true; text: bind cardinality with inverse }
    public var typeChoice:ChoiceBox = ChoiceBox {
        items: ["TEXT", "IMAGE", "VOICE", "COUNTER", "NUMBER", "GPS", "DATE", "TIME"]
    }

    public-init var caption:String = "";
    public-init var cardinality:String = "1";
    var eventButton:Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/note.png" } }
        tooltip: Tooltip { text: "view events" }
        
        action:function():Void {
            formList.showElementEvents(events);
        }

    }
    //other
    public-read var events:FormElementEvents = FormElementEvents {formList:formList}
    
    public-read var checked:Boolean = false;
    
    //create content
    postinit {
        content.content = [captionLabel, captionBox, typeLabel, typeChoice, cardinalityLabel, cardinalityBox/*, eventButton*/];
    }

    public function addEventItem(type:String, datatype:String):Void {
        events.addEventItem(type, datatype);
    }

    public function addEventItem(type:FormEventType, datatype:FormEventDataType):Void {
        events.addEventItem(type, datatype);
    }


    public function setType(type:FormElementDataType):Void {
        this.typeChoice.select(type.ordinal());
    }

    public function getType():FormElementDataType { 
        return FormElementDataType.valueOf(typeChoice.selectedItem as String);
    }

    public function getTitle() {
        return caption;
    }

    public function getCardinality() {
        return cardinality;
    }




}
