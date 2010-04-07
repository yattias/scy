
package eu.scy.client.tools.fxwebresourcer;

import javafx.scene.CustomNode;

import javafx.scene.control.ScrollBar;
import javafx.scene.layout.ClipView;
import javafx.scene.layout.LayoutInfo;

import javafx.scene.layout.HBox;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;




import eu.scy.client.tools.fxwebresourcer.EloWebResourceActionWrapper;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.net.URI;

import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;

import javafx.scene.layout.Resizable;

import java.lang.System;
import java.lang.Thread;

import eu.scy.client.tools.fxwebresourcer.highlighter.XMLData;

/**
 * @author pg
 */

public class WebResourceNode extends CustomNode, ILoadXML, ScyToolFX, Resizable {
    public var scyWindow: ScyWindow on replace {
        setScyWindowTitle();
    };
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    def spacing = 5.0;
    public var eloWebResourceActionWrapper:EloWebResourceActionWrapper;
    //var dataFromXML:HighlighterXMLData;
    var myContent:ContentNode = ContentNode {
            webNode: this
    };
    var vScroll:ScrollBar = ScrollBar {
        min: 0;
        value: 0;
        max: 1;
        vertical: true;
        disable: true;
        layoutInfo:LayoutInfo {
            height: bind scyWindow.height-100;
        }
    }
    var scrollClipView:ClipView = ClipView {
        clipY: bind vScroll.value;
        node: myContent;
        pannable: false;
        layoutInfo:LayoutInfo {
            height: bind scyWindow.height-100;
            width: bind scyWindow.width - 20;
        }
    }
    var hBox:HBox = HBox {
        content: [scrollClipView, vScroll]
    }
    var scyWindowHeightWatch:Number = bind scyWindow.height on replace { 
            updateScrollbars();
    }
    var scyWindowWidthtWatch:Number = bind scyWindow.width on replace { 
            updateScrollbars();
            updateLine();
    }

    public function addQuotation(text:String):Void {
        myContent.addQuotation(text);
    }
    function updateLine():Void {
        myContent.lineWidth = scyWindowWidthtWatch;
    }

    public function updateScrollbars():Void {
       //disables the scrollbars if the content is smaller than the scywindow to prevent some errors
       if((myContent.height) <= scrollClipView.height) {
           vScroll.value = 0;
           vScroll.max = 1;
           vScroll.disable = true;
       } else {
           vScroll.value = 0;
           vScroll.max = javafx.util.Math.abs(myContent.height - scrollClipView.height);
           vScroll.disable = false;
       }
    }

    public override function loadXML(input:String):Void {
        //lets do some bugfixing the chucknorris style.
        /*
        myContent = null;
        myContent = ContentNode {
            webNode: this;
        }
        */
        //remove linebreaks -> JAXB seems to have some trouble with them.
        /*var xml = input.replaceAll("[\r\n]+", "");
        //use JAXB to create an object from xml inupt
        var context:JAXBContext = JAXBContext.newInstance(HighlighterXMLData.class);
        var um:Unmarshaller = context.createUnmarshaller();
        dataFromXML = (um.unmarshal( new StringReader(xml)) as HighlighterXMLData);
        */
        //clear and add new content
        var xmlData:XMLData = new XMLData(input);
        myContent.clearQuotations();
        myContent.setTitle(xmlData.getTitle());
        
        for(bullet in xmlData.getBullets()) {
             myContent.addQuotation(bullet);
        }
        //myContent.setComment(xmlData.getComments().replaceAll("LINEBREAKISAFORBIDDENWORD", "\r\n"));
        myContent.setComment(xmlData.getComments());
        myContent.setSource(xmlData.getSources());
        setScyWindowTitle();
        updateScrollbars();
    }

    public override function getXML():String {
        //dataFromXML.setComments(myContent.comment);
        //return dataFromXML.toString();
        return "";
    }

    function setScyWindowTitle() {
       /* if(scyWindow == null)
            return scyWindow.title = "WebResourceR";
        scyWindow.title = eloWebResourceActionWrapper.getDocName();
        var eloUri = eloWebResourceActionWrapper.getEloUri();
        scyWindow.eloUri = eloUri;*/
        scyWindow.title = "WebResouceR: {myContent.title}";
        /*
        if(eloUri != null)
            scyWindow.id = eloUri.toString()
        else
            scyWindow.id = "";
        */
    }

 /**
    * ScyTool methods
    */
    override function initialize(windowContent: Boolean):Void {

    }

    /**
    * step 6 in sequence of calls -> wiki ScyTool
    * ScyWindow + repository should be fine now -> create eloactionwrapper and be happy!
    */
    override function postInitialize():Void {

            this.eloWebResourceActionWrapper = new EloWebResourceActionWrapper(this);
            eloWebResourceActionWrapper.setRepository(repository);
            eloWebResourceActionWrapper.setMetadataTypeManager(metadataTypeManager);
            eloWebResourceActionWrapper.setEloFactory(eloFactory);
            eloWebResourceActionWrapper.setDocName(scyWindow.title);
    }

    override function newElo():Void {

    }

    override function loadElo(uri:URI):Void {
        eloWebResourceActionWrapper.loadElo(uri);
        setScyWindowTitle();
    }

    override function onGotFocus():Void {

    }

    override function onLostFocus():Void {

    }

    override function onMinimized():Void {

    }

    override function onUnMinimized():Void {

    }

    override function onClosed():Void {

    }

    override function aboutToClose():Boolean {
        return true
    }

    override function setEloSaver(eloSaver:EloSaver):Void {

    }

    override function setMyEloChanged(myEloChanged:MyEloChanged) {

    }



    override function getPrefHeight(width:Number):Number {
        return 500;
    }

    override function getPrefWidth(height:Number):Number {
        return 400;
    }

    

    public override function create():Node {
        var g = Group {
              //blocksMouse: true;
            content: bind
            VBox{
               translateY:spacing;
               spacing:spacing;
               content: bind [
                  HBox{
                     translateX:spacing;
                     spacing:spacing;
                     content:[
                             /*
                        Button {
                            text: "reload";
                            translateX: 500;
                            onMouseReleased:function(e:MouseEvent):Void {
                            }
                        }
*/
                             /*
                        Button {
                           text: "New"
                           action: function() {
                              eloWebResourceActionWrapper.newWebAction();
                            setScyWindowTitle();
                           }
                        }*/
                        Button {
                           text: "Open"
                           action: function() {
                                eloWebResourceActionWrapper.loadWebAction();
                                //setScyWindowTitle();
                           }
                        }
                        /*
                        Button {
                           text: "Save"
                           action: function() {
                              eloWebResourceActionWrapper.saveWebAction();
										setScyWindowTitle();
                           }
                        }*/
                        /*
                        Button {
                           text: "Save copy"
                           action: function() {
                                eloWebResourceActionWrapper.saveAsWebAction();
                                setScyWindowTitle();
                           }
                        }
                        */
                     ]
                  }
                  hBox
               ]
            }
            //bind hBox;
        };
        return g;
    }

}
