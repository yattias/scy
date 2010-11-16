/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TextBox;
import java.util.ArrayList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import java.lang.Exception;
import javax.swing.JOptionPane;
import java.io.StringReader;
import org.xml.sax.InputSource;
import org.w3c.dom.NodeList;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import javax.swing.JFileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.LayoutInfo;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.ScrollView;
import javafx.scene.control.ScrollBarPolicy;
import javafx.geometry.HPos;
import javafx.scene.paint.Color;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataModel;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;
import eu.scy.client.tools.fxformauthor.datamodel.DataHandler;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataEvent;

/**
 * @author pg
 */

public class FormList extends CustomNode {
    public-init var scyWindow:ScyWindow;
    public-init var formNode:FormAuthorNode;
    var nodes:Node[];
    override var children = bind [nodes];
    public-read var backgroundColor:Color = bind scyWindow.windowColorScheme.backgroundColor;
    public-read var mainColor:Color = bind scyWindow.windowColorScheme.mainColor;
    //Button-Bar
    def addButton:Button = Button { 
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/textfield_add.png" } }
        tooltip: Tooltip { text: "add form field" }
        action: function():Void {
            elements.add(FormElement{formList: this});
            updateGUIList();
        }
    }
    def deleteButton:Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/textfield_delete.png" } }
        tooltip: Tooltip { text: "delete selected fields" }
        action: function():Void {
            checkDeletes();
            updateGUIList();
        }
    }
    def upButton:Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/arrow_up.png" } }
        tooltip: Tooltip { text: "move selected fields up" }
        action: function():Void {
            checkMoveUp();
            updateGUIList();
        }
    }
    def downButton:Button = Button {
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/arrow_down.png" } }
        tooltip: Tooltip { text: "move selected fields down" }
        action: function():Void {
            checkMoveDown();
            updateGUIList();
        }
    }
    def xml:String = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><form description=\"\" title=\"\" version=\"0\"><field cardinality=\"0\" title=\"name\" type=\"TEXT\"><event datatype=\"TIME\" type=\"ONBEFORE\"/><event datatype=\"TIME\" type=\"ONBEFORE\"/><event datatype=\"TIME\" type=\"ONBEFORE\"/></field><field cardinality=\"0\" title=\"foto\" type=\"IMAGE\"/><field cardinality=\"0\" title=\"koordinaten\" type=\"GPS\"/><field cardinality=\"0\" title=\"zeit vorher\" type=\"TIME\"><event datatype=\"TIME\" type=\"ONBEFORE\"/></field><field cardinality=\"0\" title=\"zeit nachher\" type=\"TIME\"><event datatype=\"TIME\" type=\"ONAFTER\"/></field></form>";
    def buttonBox:HBox = HBox { content: [addButton, deleteButton, upButton, downButton,
            Button{
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/folder_table.png" } }
                tooltip: Tooltip { text: "open xml file" }
                action:function():Void {
                    loadFDM(DataHandler.getInstance().loadFromFile());
                }
            },
            Button{
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/table_save.png" } }
                tooltip: Tooltip { text: "save xml file" }
                action:function():Void {
                    //saveToFile()
                    DataHandler.getInstance().saveToFile(createFDM());
                }
            },
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/world_add.png" } }
                tooltip: Tooltip { text: "browse ELOs" }
                action:function():Void {formNode.browseElos()}
            },
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/page_white_world.png" } }
                tooltip: Tooltip { text: "save ELO" }
                action:function():Void {formNode.saveElo()}
            },
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/application_view_gallery.png" } }
                tooltip: Tooltip { text: "switch to FormViewer" }
                action:function():Void {formNode.loadViewer()}
            }

        ]
        spacing: 2.0;
    }

    //form data bar
    def titleLabel:Text = Text { font: Font {size: 14} content: "Title:"}
    def descriptionLabel:Text = Text { font: Font {size: 14} content: "Description:"}
    var titleBox:TextBox = TextBox {
        columns: 10;
        selectOnFocus: true;
        text: bind title with inverse;
        layoutInfo: LayoutInfo {
            hpos: HPos.RIGHT;
            hfill: false;
            width: 440;
        }
    }
    var descriptionBox:TextBox = TextBox {
        text: bind description with inverse;
        layoutInfo: LayoutInfo {
            hpos: HPos.RIGHT;
            hfill: false;
            width: 440;
        }
        multiline: true
    }
    def formDataBox:VBox = VBox{
        spacing: 2.0;
        content: [
            HBox {
                content: [titleLabel, titleBox]
                spacing: 2.0
                layoutInfo: LayoutInfo {
                    hfill: false;
                    minWidth: 520 
                    width: 520
                    maxWidth: 520
                }
            },
            HBox {
                content: [descriptionLabel, descriptionBox]
                spacing: 2.0
                layoutInfo: LayoutInfo {
                    minWidth: 520
                    width: 520
                    maxWidth: 520
                }
            }
        ]
    }
    //formelementbox
    var formElementBox:VBox = VBox {
        spacing: -2.0;
        content: []
    }
    public-read var headerHeight:Number = bind formDataBox.layoutBounds.height+buttonBox.layoutBounds.height;

    var sv:ScrollView = ScrollView {
        node: formElementBox;
        style: "-fx-background-color: transparent;"
        vbarPolicy: ScrollBarPolicy.AS_NEEDED;
        hbarPolicy: ScrollBarPolicy.NEVER;
        layoutInfo: LayoutInfo{
            height: bind scyWindow.height-headerHeight-60;
            width: bind scyWindow.width+25;
        }
    }

    var content:VBox = VBox {
        spacing: 5.0;
        layoutInfo: LayoutInfo {minWidth: 520 width: 520}
        content: bind [formDataBox, buttonBox, sv]
    }

    //content array
    var elements:ArrayList = new ArrayList();

    var title:String = "";
    var description:String = "";
    var version:Integer = 0;
    postinit {
        insert content into nodes;
    }

    public function showElementEvents(eventNode:FormElementEvents):Void {
        insert eventNode into nodes;
        eventNode.blocksMouse = true;
    }

    public function hideElementEvents(eventNode:FormElementEvents):Void {
        delete eventNode from nodes;
        eventNode.blocksMouse = false;
    }

    public function clearContent():Void {
        elements.clear();
        updateGUIList();
    }

    function updateGUIList():Void {
        var formElements:FormElement[];
        formElementBox.content = [];
        for(i in [0..elements.size()-1]) {
            insert (elements.get(i) as FormElement) into formElements;
        }
        formElementBox.content = formElements;
    }

    function moveUp(element:FormElement):Void {
        def oldIndex = elements.indexOf(element);
        if(oldIndex > 0) {
            elements.remove(element);
            elements.add(oldIndex - 1, element);
        }
    }

    function moveDown(element:FormElement):Void {
        def oldIndex = elements.indexOf(element);
        if(oldIndex < elements.size()-1) {
            elements.remove(element);
            elements.add(oldIndex + 1, element);
        }
    }

    function checkMoveUp():Void {
        for(i in [0..elements.size()-1]) {
            if((elements.get(i) as FormElement).checked == true) {
                //move up
                moveUp((elements.get(i) as FormElement));
            }
        }
    }

    function checkMoveDown():Void {
        var i = elements.size()-1;
        while(i >= 0) {
            if((elements.get(i) as FormElement).checked == true) {
                //move down
                moveDown((elements.get(i) as FormElement));
            }
            i--;
        }
    }

    function checkDeletes():Void {
        var i = elements.size()-1;
        while(i >= 0) {
            if((elements.get(i) as FormElement).checked == true) {
                //delete
                elements.remove(elements.get(i));
            }
            i--;
        }
    }

    function askForReplace():Void {
        if(DataHandler.getInstance().getLastFDM() != null) {
            var options = ["Append content", "Delete current form"];
            var result:Number = JOptionPane.showOptionDialog(
                    null,
                    "Do you want to append the ELO content to the current form?",
                    "Replace Form?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    (options as Object[]),
                    (options[0] as Object));
            if(result == 1.0) {
                clearContent();
            }
        }
    }

    public function createFromString(xml:String):Void {
        var fdm = DataHandler.getInstance().loadFromString(xml);
        loadFDM(fdm);
    }

    public function getXMLString():String {
        return DataHandler.getInstance().saveToString(createFDM());
    }

    public function setScyWindow(window:ScyWindow):Void {
        scyWindow = window;
    }

    public function loadFDM(fdm:FormDataModel):Void {
        if(fdm == null) {
                JOptionPane.showMessageDialog(null,
                                "An error occured: the FormDataModel is null.\n Please contact your local SCY partner.",
                                "ERROR",
                                JOptionPane.OK_CANCEL_OPTION);
            return;
        }
        askForReplace();
        titleBox.text = fdm.getTitle();
        descriptionBox.text = fdm.getDescription();
        version = fdm.getVersion();
        //b4: ask user to delete / append content
        for(i in [0..(fdm.getElements().size()-1)]) {
            def fde:FormDataElement = (fdm.getElements().get(i) as FormDataElement);
            var fe:FormElement = FormElement {
                formList: this;
                caption: fde.getTitle();
                cardinality: fde.getCardinality().toString();
            }
            fe.setType(fde.getType());
            for(j in [0..(fde.getEvents().size()-1)]) {
                fe.addEventItem(
                    fde.getEvents().get(j).getType(),
                    fde.getEvents().get(j).getDataType()
                );
            }
            elements.add(fe);
        }
        updateGUIList();
    }

    public function createFDM():FormDataModel {
        var fdm:FormDataModel = new FormDataModel();
        fdm.setTitle(title);
        fdm.setDescription(description);
        fdm.setVersion(version); //TODO: version does not work
        for(i in [0..elements.size()-1]) {
            var cardinality;
            if((elements.get(i) as FormElement).getCardinality().equals("")){
                cardinality = 0;
            }
            else {
                cardinality = Integer.parseInt((elements.get(i) as FormElement).getCardinality());
            }


            var fel = new FormDataElement(
                    (elements.get(i) as FormElement).caption,
                    (elements.get(i) as FormElement).getType(),
                    cardinality
            );
            if((elements.get(i) as FormElement).events.count != 0) {
                var items:FormElementEventsItem[] = (elements.get(i) as FormElement).events.getEventItems();
                for(item in items) {
                   var fev:FormDataEvent = new FormDataEvent(
                            item.getDataType(),
                            item.getEventType()
                   );
                   fel.addEvent(fev);
                }
            }
            fdm.addElement(fel);
        }
        return fdm;
    }

}
