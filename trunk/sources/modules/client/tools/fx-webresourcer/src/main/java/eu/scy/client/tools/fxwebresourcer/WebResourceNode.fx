
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
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;

import javafx.scene.layout.Resizable;


import eu.scy.client.tools.fxwebresourcer.highlighter.XMLData;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author pg
 */

public class WebResourceNode extends CustomNode, ILoadXML, ScyToolFX, Resizable {
    def logger = Logger.getLogger(this.getClass());
    public var scyWindow: ScyWindow;
    public var toolBrokerAPI:ToolBrokerAPI;
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    def spacing = 5.0;
    public var eloWebResourceActionWrapper:EloWebResourceActionWrapper;
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
        node: bind myContent;
        pannable: false;
        layoutInfo:LayoutInfo {
            height: bind scyWindow.height-100;
            width: bind scyWindow.width - 30;
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
       // finally working!!!11 :)
       //if((myContent.height) <= scrollClipView.height) {
       def offset = vScroll.value;
       if((myContent.layoutBounds.height-400) <= scrollClipView.height) {
           //vScroll.value = 0;
           vScroll.max = 1;
           vScroll.disable = true;
       } else {
           //vScroll.value = 0;
           //vScroll.max = javafx.util.Math.abs(myContent.height - scrollClipView.height);
           vScroll.max = javafx.util.Math.abs(myContent.layoutBounds.height-400 - scrollClipView.height);
           vScroll.disable = false;
       }
    }

    public override function loadXML(input:String):Void {
        myContent = ContentNode {
            webNode: this
        };
        var xmlData:XMLData = new XMLData(input);
        myContent.loadXML(xmlData);
        updateLine();
        updateScrollbars();
    }

    public override function getXML():String {
        return "";
    }

 /**
    * ScyTool methods
    */
    override function initialize(windowContent: Boolean):Void {
       repository = toolBrokerAPI.getRepository();
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
       eloFactory = toolBrokerAPI.getELOFactory();
    }

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
//                  HBox{
//                     translateX:spacing;
//                     spacing:spacing;
//                     content:[
//                        Button {
//                           text: "Open"
//                           action: function() {
//                                eloWebResourceActionWrapper.loadWebAction();
//                           }
//                        }
//                     ]
//                  }
                  hBox
               ]
            }
        };
        return g;
    }

}
