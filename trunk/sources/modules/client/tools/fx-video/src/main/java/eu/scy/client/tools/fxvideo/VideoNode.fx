
package eu.scy.client.tools.fxvideo;

import javafx.scene.CustomNode;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;


import javafx.scene.Group;
import javafx.scene.Node;
import java.net.URI; 

import com.sun.javafx.mediabox.*;

import javafx.scene.control.Button;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javafx.scene.layout.HBox;

import eu.scy.client.desktop.scydesktop.scywindows.window.WindowChangesListener;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

import javafx.scene.layout.Resizable;

import java.lang.System;



/**
 * @author pg
 *
 *  README README README README README
 *
 *  if the videos do not play you should check if you got all the required codecs installed
 *      http://codecs.com/FFDShow_download.htm
 */

public class VideoNode extends CustomNode, ILoadXML, WindowChangesListener, ScyTool, Resizable {

    public var scyWindow: ScyWindow on replace {
        setScyWindowTitle();
    };
    def spacing = 5.0;
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var eloVideoActionWrapper:EloVideoActionWrapper;

    var media:String;
    var title:String;
    var subtitle:String;
    var mediaBox:MediaBox =  MediaBox {
                mediaSource: media
                mediaTitle: title;
                mediaDescription: subtitle;
                showMediaInfo: true
                translateX: 25
                translateY: 25
                //binding width and height kills every simple piece of performance
                width: 320
                //width: bind scyWindow.width - 50;
                height: 240
                //height: bind scyWindow.height - 50;
                autoPlay: false
                mediaControlBarHeight: 25
    }

    function setScyWindowTitle() {
        scyWindow.title = "Video: {this.title}";
    }
    var dataFromXML:XMLVideoData;
    
    override function loadXML(input:String):Void {
        var xml = input.replaceAll("[\r\n]+", "");
        //use JAXB to create an object from xml inupt
        var context:JAXBContext = JAXBContext.newInstance(XMLVideoData.class);
        var um:Unmarshaller = context.createUnmarshaller();
        dataFromXML = (um.unmarshal( new StringReader(xml)) as XMLVideoData);
        //delete contentNode;
        //System.out.println(dataFromXML.getURI());
        //System.out.println(dataFromXML.getTitle());
        //System.out.println(dataFromXML.getSubtitle());
        mediaBox.mediaSource = dataFromXML.getURI();
        this.title = dataFromXML.getTitle();
        mediaBox.mediaTitle = dataFromXML.getTitle();
        mediaBox.mediaDescription = dataFromXML.getSubtitle();

        setScyWindowTitle();
        //mediaBox.mediaSource = media;
        /*
        mediaBox =  MediaBox {
                mediaSource: media
                mediaTitle: dataFromXML.getTitle()
                mediaDescription: dataFromXML.getSubtitle();
                showMediaInfo: true
                translateX: 25
                translateY: 25
                width: 320
                height: 240
                autoPlay: false
                mediaControlBarHeight: 25
                
        } */
    }

    /**
    *
    * resizes the mediabox to the current scywindow size
    */
    function setSizeToCurrent():Void {
        mediaBox.width = scyWindow.width-50;
        mediaBox.height = scyWindow.height-75;
    }

    override function resizeStarted():Void {
    }

    override function resizeFinished():Void {
            this.setSizeToCurrent();
    }

    override function draggingStarted():Void {
    }

    override function draggingFinished():Void {
    }
     /**
    * ScyTool methods
    */
    override function initialize():Void {

    }

    /**
    * step 6 in sequence of calls -> wiki ScyTool
    * ScyWindow + repository should be fine now -> create eloactionwrapper and be happy!
    */
    override function postInitialize():Void {
            this.eloVideoActionWrapper = new EloVideoActionWrapper(this);
            eloVideoActionWrapper.setRepository(repository);
            eloVideoActionWrapper.setMetadataTypeManager(metadataTypeManager);
            eloVideoActionWrapper.setEloFactory(eloFactory);
            eloVideoActionWrapper.setDocName(scyWindow.title);

    }

    override function newElo():Void {

    }

    override function loadElo(uri:URI):Void {
        eloVideoActionWrapper.loadElo(uri);
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

    override function getPrefHeight(width:Number) {
        return 450;
    }

    override function getPrefWidth(height: Number):Number {
        return 550;
    }

    public override function create():Node {

        scyWindow.addChangesListener(this);
        var g = Group {
              content: bind
              [
                      HBox {
                          content: [
                              Button {
                                        text: "load video";
                                        action:  function() {
                                            eloVideoActionWrapper.loadVideoAction();
                                        }
                               },
                               Button {
                                        text: "resize video";
                                        action: function() {
                                            setSizeToCurrent();
                                        }

                               }

                        ]
                       },
                       mediaBox
              ]
        };
        return g;
    }

}
