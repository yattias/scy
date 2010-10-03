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
import java.io.FileInputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.LayoutInfo;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.ScrollView;
import javafx.scene.control.ScrollBarPolicy;
import javafx.geometry.HPos;
import javafx.scene.paint.Color;

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
                action:function():Void {openFile()}
            },
            Button{
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/table_save.png" } }
                tooltip: Tooltip { text: "save xml file" }
                action:function():Void {saveToFile()}
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
        layoutInfo: LayoutInfo {
            hpos: HPos.RIGHT;
            hfill: false;
            width: 440;
        }
    }
    var descriptionBox:TextBox = TextBox {
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

    var title:String = bind titleBox.text;
    var description:String = bind descriptionBox.text;
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

    function createDoc():Document {
        var factory:DocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        var parser:DocumentBuilder = factory.newDocumentBuilder();
        var doc:Document = parser.newDocument();

        var root:Element = doc.createElement("form");
        root.setAttribute("title", title);
        root.setAttribute("description", description);
        root.setAttribute("version", String.valueOf(version));

        for(i in [0..elements.size()-1]) {
            var elField:Element = doc.createElement("field");
            elField.setAttribute("title", (elements.get(i) as FormElement).caption);
            elField.setAttribute("type", (elements.get(i) as FormElement).typeChoice.selectedItem.toString());
            elField.setAttribute("cardinality", (elements.get(i) as FormElement).cardinality);
            //TODO: data -> base64
            if((elements.get(i) as FormElement).events.count != 0) {
                var items:FormElementEventsItem[] = (elements.get(i) as FormElement).events.getEventItems();
                for(item in items) {
                    var elEvent:Element = doc.createElement("event");
                    elEvent.setAttribute("type", item.typeChoice.selectedItem.toString());
                    elEvent.setAttribute("datatype", item.dataChoice.selectedItem.toString());
                    //TODO: data -> base64
                    elField.appendChild(elEvent);
                }
            }
            root.appendChild(elField);
        }
        doc.appendChild(root);
        return doc;
    }

    public function toXML(filename:String):Void {
        try {
            var doc:Document = createDoc();
            var source:Source = new DOMSource(doc);
            //prepare file output
            var file:File = new File(filename);
            var result:Result = new StreamResult(file);
            var xformer:Transformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            xformer.transform(source, result);
        }
        catch(e:Exception) {

        }
    }

    function toXML():String {
        try {
            var doc:Document = createDoc();
            var source:Source = new DOMSource(doc);
            var stringWriter:StringWriter = new StringWriter();
            var result:Result = new StreamResult(stringWriter);
            var xformer:Transformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            xformer.transform(source, result);
            return stringWriter.toString();
        }
        catch(e:Exception) {

        }
        return null;
    }

    function toFile(filename:String) {
        toXML("{filename}.xml");
    }



    function saveToFile() {
        def jfc:JFileChooser = new JFileChooser();
        jfc.setDialogType(JFileChooser.SAVE_DIALOG);
        var result = jfc.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            toFile(jfc.getSelectedFile().toString());
        }

    }

    function askForReplace():Void {
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

    public function fromString(xml:String):Void {
        askForReplace();
        try {
            def factory:DocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            def builder:DocumentBuilder = factory.newDocumentBuilder();
            var document:Document = builder.parse(new InputSource(new StringReader(xml)));
            document.getDocumentElement().normalize();
            createFAfromXML(document);
        }
        catch(e:Exception) {
            e.printStackTrace();
            println(e);
            JOptionPane.showMessageDialog(null,
                "The XML parsing failed.",
                "Parsing Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    function createFAfromXML(doc:Document):Void {
        //tihs works with a shitload of try n catch :/
        var nl:NodeList;
        nl = doc.getElementsByTagName("form");
        println("#FOOFOOBAR");
        println(nl.getLength());
        println("#foofoobar");
        try {
            for (i in [0..nl.getLength()-1]) {
                titleBox.text = (nl.item(i) as Element).getAttribute("title");
                descriptionBox.text = (nl.item(i) as Element).getAttribute("description");
                version = Integer.parseInt((nl.item(i) as Element).getAttribute("version"));
            }
        }
        catch(e:Exception) {
            println("whoops, error while parsing.")
        }

        nl = doc.getElementsByTagName("field");
        for (i in [0..nl.getLength()-1]) {
            try {
                var fe:FormElement = FormElement {
                    caption: (nl.item(i) as Element).getAttribute("title")
                    cardinality: (nl.item(i) as Element).getAttribute("cardinality")
                    formList: this
                }
                //bitte auslagern in FormElement.fx.. danke! :)
                def type = (nl.item(i) as Element).getAttribute("type");
                def choices:String[] = ["TEXT", "IMAGE", "VOICE", "COUNTER", "NUMBER", "GPS", "DATE", "TIME"];
                var k:Number = 0;
                for(item in choices) {
                    if(item.equals(type)) {
                        fe.typeChoice.select(k);
                    }
                    k++;
                }
                var nlFieldChilds:NodeList = nl.item(i).getChildNodes();
                for (j in [0..nl.getLength()-1]) {
                    try {
                        if (nlFieldChilds.item(j).getNodeType() == Element.ELEMENT_NODE) {
                            var fieldChild:Element = (nlFieldChilds.item(j)as Element);
                            if (fieldChild.getNodeName().equalsIgnoreCase("fielddata")) {
                                var fieldData:String = (nlFieldChilds.item(j) as Element).getAttribute("data");
                                println("data should be storaged now..");
                            }
                            else {
                                if (fieldChild.getNodeName().equalsIgnoreCase("event")) {
                                    /*
                                    var feei:FormElementEventsItem = FormElementEventsItem {
                                        eventsList: fe.events;
                                    }
                                    feei.setDataType((nlFieldChilds.item(j) as Element).getAttribute("datatype"));
                                    feei.setEventType((nlFieldChilds.item(j) as Element).getAttribute("type"));
                                    */
                                    fe.addEventItem((nlFieldChilds.item(j) as Element).getAttribute("type"), (nlFieldChilds.item(j) as Element).getAttribute("datatype"));
                                    //fe.addEventItem(type, datatype);
                                }
                            }

                        }
                    }
                    catch(e:Exception) {
                        println("error while parsing field element");
                    }
                }
                elements.add(fe);
            }
            catch(e:Exception) {
                println("error while parsing form element");
            }

        }
        updateGUIList();
    }

    function openFile():Void {
        def jfc:JFileChooser = new JFileChooser();
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        var result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                fromFile(jfc.getSelectedFile().toString());
            } catch (e:Exception) {
                // H
                JOptionPane.showMessageDialog(null,
                                "The XML structure is broken.\nPlease try another file.",
                                "ERROR",
                                JOptionPane.OK_CANCEL_OPTION);
                e.printStackTrace();
            }
        }
    }

    function fromFile(filename:String):Void {
        var fis:FileInputStream = new FileInputStream(filename);
        var dbf:DocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        var db:DocumentBuilder = dbf.newDocumentBuilder();
        var doc:Document = db.parse(fis);
        doc.getDocumentElement().normalize();
        askForReplace();
        createFAfromXML(doc);
    }

    public function setScyWindow(window:ScyWindow):Void {
        scyWindow = window;
    }



    public function getXML():String {
         try {
            var doc:Document = createDoc();
            var source:Source = new DOMSource(doc);
            //prepare file output
            var writer:StringWriter = new StringWriter();
            var result:Result = new StreamResult(writer);
            var xformer:Transformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            xformer.transform(source, result);
            return writer.toString();
        }
        catch(e:Exception) {
            e.printStackTrace();
        }
        //just in case we got an error
        return "<form>error!</form>";
    }









}
