
package eu.scy.client.tools.fxvideo;

import javafx.scene.CustomNode;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;


import javafx.scene.Group;
import javafx.scene.Node;
import java.net.URI; 

import com.sun.javafx.mediabox.*;


import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import eu.scy.client.desktop.scydesktop.scywindows.window.WindowChangesListener;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.common.scyi18n.UriLocalizer;

import javafx.scene.layout.Resizable;

import eu.scy.toolbrokerapi.ToolBrokerAPI;



/**
 * @author pg
 *
 *  README README README README README
 *
 *  if the videos do not play you should check if you got all the required codecs installed
 *      http://codecs.com/FFDShow_download.htm
 */

public class VideoNode extends CustomNode, ILoadXML, WindowChangesListener, ScyToolFX, Resizable {

    public var scyWindow: ScyWindow on replace {
        setScyWindowTitle();
    };
    def spacing = 5.0;

    public var toolBrokerAPI:ToolBrokerAPI;
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var eloVideoActionWrapper:EloVideoActionWrapper;

    def uriLocalizer = new UriLocalizer();

    var media:String = "http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_2956241001_big-buck-bunny-640x360.flv";
    var title:String;
    var subtitle:String;
    var mediaBox:MediaBox =  MediaBox {
                mediaSource: media
                mediaTitle: title;
                mediaDescription: subtitle;
                showMediaInfo: true
                translateX: 5
                translateY: 5
                //binding width and height kills every simple piece of performance
                //width: 320
                width: bind scyWindow.width - 25;
                //height: 240
                height: bind scyWindow.height - 50;
                autoPlay: false
                mediaControlBarHeight: 25
                preserveRatio: true
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
        var realVideoUri = uriLocalizer.localizeUriWithChecking(dataFromXML.getURI());
        println("playing video from: {realVideoUri}");
        mediaBox.mediaSource = realVideoUri;
        this.title = dataFromXML.getTitle();
        mediaBox.mediaTitle = dataFromXML.getTitle();
        mediaBox.mediaDescription = dataFromXML.getSubtitle();
        setScyWindowTitle();
        //this will resize the scyWindow to the current media size - tho it might be buggy with streaming media etc.
        /*
        var testMedia = javafx.scene.media.Media {
                source: dataFromXML.getURI();
        } */
        //mediaBox.height = 640;
        //mediaBox.width = 360;
        //this.setSizeToCurrent();
        //kinda ugly to create another media object, mediaBox offers no access to the media file.
        //scyWindow.height = testMedia.height+34;
        //mediaBox.height = testMedia.height;
        //scyWindow.width = testMedia.width+13;
        //mediaBox.width = testMedia.width;
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
    override function initialize(windowContent:Boolean):Void {
       repository = toolBrokerAPI.getRepository();
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
       eloFactory = toolBrokerAPI.getELOFactory();
    }

    /**
    * step 6 in sequence of calls -> wiki ScyTool
    * ScyWindow + repository should be fine now -> create eloactionwrapper and be happy!
    */
    override function postInitialize():Void {
            this.eloVideoActionWrapper = new EloVideoActionWrapper(this);
            repository = toolBrokerAPI.getRepository();
            metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
            eloFactory = toolBrokerAPI.getELOFactory();
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
                      /*
                      HBox {
                          content: [
                              Button {
                                        text: "load video";
                                        action:  function() {
                                            //eloVideoActionWrapper.loadVideoAction();
                                            //        scyWindow.height = 500;
                                            //scyWindow.width = 150;
                                            var testMedia =
                                                javafx.scene.media.Media {
                                                        source: "http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_11810873001_09c01923-00.flv";
                                                 }

                                            mediaBox.mediaSource = "http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_11810873001_09c01923-00.flv";
                                            scyWindow.height = testMedia.height;
                                            mediaBox.height = testMedia.height;
                                            scyWindow.width = testMedia.width;
                                            mediaBox.width = testMedia.width;
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
                       */
                       mediaBox              ]
        };
        return g;
    }

}
