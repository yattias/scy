/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxyoutuber;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import java.lang.String;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.scene.control.ScrollBarPolicy;
import javafx.scene.control.ScrollView;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import java.net.URI;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author pg
 */

public class YouTuberNode  extends CustomNode, Resizable, ScyToolFX, ILoadXML, EloSaverCallBack {
    public-init var youTuberRepositoryWrapper:YouTuberRepositoryWrapper;
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var technicalFormatKey: IMetadataKey;
    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle();
    };
    public var spacing:Number on replace { requestLayout() }
    public var windowTitle:String;
    var scyYouTubeRType = "scy/youtuber";
    var elo:IELO;
    var eloUri:String = "n/a";

    var dataSets:ArrayList;
    
    var contentList:VBox = VBox{
        spacing: 5.0;

    };

    public-read var titleFont:Font = Font {
        size: 15.0;
    }


    public-read var textFont:Font = Font {
        size: 12.0;
    }


    var sv:ScrollView = ScrollView {
        node: bind contentList;
        style: "-fx-background-color: transparent;"
        vbarPolicy: ScrollBarPolicy.AS_NEEDED;
        hbarPolicy: ScrollBarPolicy.NEVER;
        layoutInfo: LayoutInfo{
            height: bind scyWindow.height-60;
            width: bind scyWindow.width-15;
        }
    }

    var nodes:Node[];
    var foreground:Node[];
    override var children = bind [nodes, foreground];
    

    def addURLButton:Button = Button {
        tooltip: Tooltip {text: "add YouTube Video"}
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/television_add.png" } }
        action: function():Void {
            showPopup(YouTubeDataEditor{ 
                ytNode: this
                translateX: bind (scyWindow.width/2) - (170);
                translateY: bind (scyWindow.height/2) - (105);
            });
            //showPopup(Text { content: "foobar "});
            //println("added buttons");
        }

    }


    def menuBar:HBox = HBox {
        content: [
            /*
            Text {
                content: "Add YouTube URL";
                font: Font {
                    size: 15.0;
                }

            },
            */
            addURLButton,
            /*
            Button {
                text: "fill with random data"
                action:function():Void {
                    var set = new YouTuberDataSet();
                    set.setYtid("HyAhVrNtlWA");
                    set.setTitle("SCY Videoplayer: A great Tool");
                    set.setText("The Videoplayer is a great player playing Videos. Because we know you like videos, we put the videoplayer playing a video on the youtube player. So you can watch a video while you watch a video!");
                    updateDataSet(-1,set);
                    set = new YouTuberDataSet();
                    set.setYtid("GP0_MNj8f1Q");
                    set.setTitle("Rammstein - Haifisch Video HD");
                    set.setText("Haifisch, is the third single from the album 'Liebe ist für Alle da' For the video 'Haifisch' Rammstein have again worked with Joern Heitmann. The single due out in mid-May, we will know the exact date later.");
                    updateDataSet(-1,set);
                    set = new YouTuberDataSet();
                    set.setYtid("spn-84Qe9i8");
                    set.setTitle("Frittenbude - Bilder mit Katze");
                    set.setText("just great music.");
                    updateDataSet(-1,set);
                    set = new YouTuberDataSet();
                    set.setYtid("lOXA1iM2Bsk");
                    set.setTitle("A Day To Remember - All Signs Point to Lauderdale");
                    updateDataSet(-1, set);
                }
            },
            */
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/world_add.png" } }
                tooltip: Tooltip { text: "browse ELOs" }
                action:function():Void {browseElos()}
            },
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/page_white_world.png" } }
                tooltip: Tooltip { text: "save ELO" }
                action:function():Void {
                    //saveElo();
                    doSaveELO();
                }
            },
            Button {
                graphic: ImageView{ image: Image { url: "{__DIR__}resources/page_world.png" } }
                tooltip: Tooltip { text: "save AS ELO" }
                action:function():Void {
                    //saveElo();
                    doSaveAsELO();
                }
            }
            ]
            spacing: 5.0;
    }

    var content:VBox = VBox {
        content: [
            menuBar,
            sv]
        spacing: 5.0;
    }
   var blocker:Rectangle = Rectangle {
        blocksMouse: true;
        height: bind scyWindow.height;
        width: bind scyWindow.width;
        translateX: 0;
        translateY: 0;
        fill: Color.BLACK;
        opacity: 0.7;
    }

    postinit {
        insert content into nodes;
        dataSets = new ArrayList();
    }

    function getNewURL() {
        
    }

    public function updateDataSet(id:Integer, dataSet:YouTuberDataSet):Void {
        if(id == -1) {
            dataSets.add(dataSet);
        }
        else {
            dataSets.set(id, dataSet);
        }
        //call refresh on GUI
        refreshGUIList();
    }

    public function getDataSet(id:Integer):YouTuberDataSet {
        return (dataSets.get(id) as YouTuberDataSet);
    }



    function refreshGUIList():Void {
        //delete contentList.content;
        contentList.content = [];

        for(i in [0..dataSets.size()-1]) {
            var item = YouTuberItem {
                dataSetID: i;
                dataSet: (dataSets.get(i) as YouTuberDataSet);
                ytNode: this;
            }
            insert item into contentList.content;

        }
        insert Rectangle {
            height: 10;
            width: 10;
            fill: Color.TRANSPARENT;
            stroke: Color.TRANSPARENT
        }
        into contentList.content;
    }

    public function closePopup(item:Node):Void {
        delete item from foreground;
        delete blocker from foreground;
    }

    public function showPopup(item:Node):Void {
        insert blocker into foreground;
        insert item into foreground;
        //println("inserting {item}");
    }


    public function deleteItem(id:Integer):Void {
        dataSets.remove(id);
        refreshGUIList();
    }

    override function getPrefWidth(height:Number):Number {
        return 600;
    }

    override function getPrefHeight(width:Number):Number {
        return 400;
    }

    function setScyWindowTitle():Void {
        scyWindow.title = "YouTubeR: {windowTitle}";

    }


    public function setFormAuthorRepositoryWrapper(wrapper:YouTuberRepositoryWrapper):Void {
        youTuberRepositoryWrapper = wrapper;
    }

    public override function postInitialize(): Void {
        youTuberRepositoryWrapper = new YouTuberRepositoryWrapper(this);
        youTuberRepositoryWrapper.setRepository(repository);
        youTuberRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        youTuberRepositoryWrapper.setEloFactory(eloFactory);
        youTuberRepositoryWrapper.setDocName(scyWindow.title);
    }

    public override function initialize(windowContent: Boolean):Void {
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
    }

    override function loadXML(xml:String):Void {
        this.dataSets = YTDataHandler.createSetFromString(xml);
        refreshGUIList();

    }

    override function getXML():String {
        //println(YTDataHandler.createXMLDocument(dataSets));
        return YTDataHandler.createXMLDocument(dataSets);
    }

    override function setTitle(title:String):Void {
        
    }


    public function browseElos():Void {
        youTuberRepositoryWrapper.loadYTAction();
    }

    public function saveElo():Void { 
        youTuberRepositoryWrapper.saveYTAction();
    }


    public function createXML():Void {
    }

    public override function loadElo(uri:URI) {
        doLoadELO(uri);
    }
 
    function doLoadELO(eloUri:URI) {
        var newElo = repository.retrieveELO(eloUri);
        if(newElo != null) {
            loadXML(newElo.getContent().getXmlString());
            this.eloUri = eloUri.toString();
            //logger.info("youtubeR: elo loaded");
            elo = newElo;
        }
    }
    
    function doSaveELO() {
        eloSaver.eloUpdate(getELO(), this);
        this.eloUri = elo.getUri().toString(); // stolen from filtex, dont know why (:
    }

    function doSaveAsELO() {
        eloSaver.eloSaveAs(getELO(), this);
    }

    function getELO():IELO {
        if(elo == null) {
            elo = eloFactory.createELO();
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyYouTubeRType);
        }
        elo.getContent().setXmlString(getXML());
        return elo;
    }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        eloUri = elo.getUri().toString();
    }

}