/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer;
import javafx.scene.CustomNode;
import javafx.scene.text.Text;
import java.util.ArrayList;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataModel;
import javax.swing.JOptionPane;
import eu.scy.client.tools.fxformauthor.viewer.element.IFormViewElement;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;
import eu.scy.client.tools.fxformauthor.datamodel.FormElementDataType;
import eu.scy.client.tools.fxformauthor.viewer.element.ElementViewText;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.tools.fxformauthor.FormAuthorRepositoryWrapper;
import eu.scy.client.tools.fxformauthor.ILoadXML;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBarPolicy;
import javafx.scene.control.ScrollView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import eu.scy.client.tools.fxformauthor.datamodel.DataHandler;
import eu.scy.client.tools.fxformauthor.viewer.element.ElementViewImage;
import eu.scy.client.tools.fxformauthor.viewer.element.AbstractElementView;
import eu.scy.client.tools.fxformauthor.FormAuthorNode;
import eu.scy.client.tools.fxformauthor.viewer.element.ElementViewCounter;
import eu.scy.client.tools.fxformauthor.viewer.element.ElementViewDate;
import eu.scy.client.tools.fxformauthor.viewer.element.ElementViewNumber;
import eu.scy.client.tools.fxformauthor.viewer.element.ElementViewGPS;
import javafx.scene.text.Font;
import eu.scy.client.tools.fxformauthor.viewer.element.ElementViewAudio;

/**
 * @author pg
 */

public class FormViewer extends CustomNode, Resizable, ILoadXML, ScyToolFX {
    var nodes:Node[];
    public var foreground:Node[];
    override var children = bind [nodes, foreground];
    public-read var backgroundColor:Color = bind scyWindow.windowColorScheme.backgroundColor;
    public-read var mainColor:Color = bind scyWindow.windowColorScheme.mainColor;

    public-init var title:String;
    public-init var description:String;
    public-init var formAuthorRepositoryWrapper;
    public-init var formNode:FormAuthorNode; 
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var scyWindow:ScyWindow on replace {
        //formList.setScyWindow(scyWindow);
    };
    var elements:ArrayList = new ArrayList();
    //title
    var titleText:Text = Text {
        translateX: 5;
        font: Font { size: 16 }
        content: bind title;
    }

    var descriptionText:Text = Text {
        translateX: 5
        font: Font { size: 14 }
        content: bind description;
    }

    var controlls:HBox = HBox {
        content: [
            Button{
                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-7)}resources/folder_table.png" } }
                tooltip: Tooltip { text: "open xml file" }
                action:function():Void {
                    loadFDM(DataHandler.getInstance().loadFromFile());
                }
            },
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-7)}resources/world_add.png" } }
                tooltip: Tooltip { text: "browse ELOs" }
                action:function():Void {
                    formNode.browseElos();
                    loadFDM(DataHandler.getInstance().getLastFDM())
                }
            },
            Button{
                graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-7)}resources/application_form.png" } }
                tooltip: Tooltip { text: "open in FormAuthor" }
                action:function():Void {
                    formNode.loadAuthor();
                }
            }
        ];
        spacing: 2.0;
    }

    var header:VBox = VBox {
        content: [controlls, titleText, descriptionText]
    }

    var contentBox:VBox = VBox {
        spacing: 5.0;
        content: []
    }

    var sv:ScrollView = ScrollView {
        style: "-fx-background-color: transparent;"
        node: contentBox;
        vbarPolicy: ScrollBarPolicy.AS_NEEDED;
        hbarPolicy: ScrollBarPolicy.NEVER;
        layoutInfo: LayoutInfo{
            height: bind scyWindow.height-header.layoutBounds.height-60;
            width: bind scyWindow.width-12;
        }
    }


    var content:VBox = VBox {
        content: [header, sv]
    }

    postinit {
        insert content into nodes;
    }



    //description
    //nothing(?)
    
    //list of items /w value


    public function addElement(e:IFormViewElement) {
        elements.add(e);
    }

    public function loadView():Void {
        //set title, description, version
        //initialize subnodes
    }

    public function loadFDM(fdm:FormDataModel):Void {
        if(fdm == null) {
                JOptionPane.showMessageDialog(null,
                                "An error occured: the FormDataModel is null.\n Please contact your local SCY partner.",
                                "ERROR",
                                JOptionPane.OK_CANCEL_OPTION);
            return;
        }
        //clear content list
        var viewElements:Node[];
        elements.clear();
        title = fdm.getTitle();
        description = fdm.getDescription();
        for(i in [0..(fdm.getElements().size()-1)]) {
            //store elements
            def fde:FormDataElement = fdm.getElements().get(i);
            var element:AbstractElementView;
            if(fde.getType() == FormElementDataType.TEXT) {
                element = ElementViewText {
                    viewer: this;
                    fde: fde;
                }
            }
            if(fde.getType() == FormElementDataType.IMAGE) {
                element = ElementViewImage {
                    viewer: this;
                    fde: fde;
                }
            }
            if(fde.getType() == FormElementDataType.VOICE) {
                element = ElementViewAudio {
                    viewer: this;
                    fde: fde;
                }

            }
            if(fde.getType() == FormElementDataType.COUNTER) {
                element = ElementViewCounter {
                    viewer: this;
                    fde: fde;
                }
            }
            if(fde.getType() == FormElementDataType.NUMBER) {
                element = ElementViewNumber {
                    viewer: this;
                    fde: fde;
                }
            }
            if(fde.getType() == FormElementDataType.GPS) {
                element = ElementViewGPS {
                    viewer: this;
                    fde: fde;
                }
            }
            if(fde.getType() == FormElementDataType.DATE) {
                element = ElementViewDate {
                    viewer: this;
                    fde: fde;
                }
            }
            if(fde.getType() == FormElementDataType.TIME) {
                element = ElementViewDate {
                    viewer: this;
                    fde: fde;
                }
            }
            if(element != null) {
                elements.add(element);
                insert element into viewElements;
            }

        }
        contentBox.content = [];
        contentBox.content = viewElements;
    }

    public function updateGUI():Void {
        contentBox.content = [];
        var contentNodes:Node[];
        contentBox.content = contentNodes;
        println("whoops. i shud repaint the gui :)");
        println(elements.size());
    }


    override function getPrefWidth(height:Number):Number {
        return 600;
    }

    override function getPrefHeight(width:Number):Number {
        return 400;
    }

    public function setFormAuthorRepositoryWrapper(wrapper:FormAuthorRepositoryWrapper):Void {
        formAuthorRepositoryWrapper = wrapper;
    }

    override function loadXML(xml:String):Void {
        //formList.createFromString(xml);
    }

    override function getXML():String {
        return "";
        //return formList.getXMLString();
    }

    public var windowTitle:String;

    override function setTitle(title:String):Void {
        windowTitle = title;
        this.title = title;
    }

    public override function postInitialize(): Void {
        formAuthorRepositoryWrapper = new FormAuthorRepositoryWrapper(this);
        formAuthorRepositoryWrapper.setRepository(repository);
        formAuthorRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        formAuthorRepositoryWrapper.setEloFactory(eloFactory);
        formAuthorRepositoryWrapper.setDocName(scyWindow.title);
    }

    public function browseElos():Void {
        formAuthorRepositoryWrapper.loadFormAction();
    }

    public function saveElo():Void {
        formAuthorRepositoryWrapper.saveFormAction();
    }

}
