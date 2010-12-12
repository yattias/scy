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

/**
 * @author pg
 */

public class YouTuberNode  extends CustomNode, Resizable, ScyToolFX, ILoadXML {
    public-init var youTuberRepositoryWrapper:YouTuberRepositoryWrapper;
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle();
    };
    public var spacing:Number on replace { requestLayout() }
    public var windowTitle:String;


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
        node: contentList;
        style: "-fx-background-color: transparent;"
        vbarPolicy: ScrollBarPolicy.AS_NEEDED;
        hbarPolicy: ScrollBarPolicy.NEVER;
        layoutInfo: LayoutInfo{
            height: bind scyWindow.height-60;
            width: bind scyWindow.width+25;
        }
    }

    var nodes:Node[];
    var foreground:Node[];
    override var children = bind [nodes, foreground];
    

    def addURLButton:Button = Button {
        tooltip: Tooltip {text: "add YouTube Video"}
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/television_add.png" } }
        action: function():Void {
            showPopup(YouTubeDataEditor{ ytNode: this });
            showPopup(Text { content: "foobar "});
            println("added buttons");
        }

    }


    def menuBar:HBox = HBox {
        content: [
            addURLButton,
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
                    set.setYtid("spn-84Qe9i8");
                    set.setTitle("Frittenbude - Bilder mit Katze");
                    set.setText("just great music.");
                    updateDataSet(-1,set);
                    set = new YouTuberDataSet();
                    set.setYtid("lOXA1iM2Bsk");
                    set.setTitle("A Day To Remember - All Signs Point to Lauderdale");
                    updateDataSet(-1, set);
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
        delete contentList.content;

        for(i in [0..dataSets.size()-1]) {
            var item = YouTuberItem {
                dataSetID: i;
                dataSet: (dataSets.get(i) as YouTuberDataSet);
                ytNode: this;
            }
            insert item into contentList.content;

        }

        
    }


    
    public function closePopup(item:Node):Void {
        delete item from foreground;
    }

    public function showPopup(item:Node):Void {
        insert item into foreground;
        println("inserting {item}");
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


    override function loadXML(xml:String):Void {
        this.dataSets = YTDataHandler.createSetFromString(xml);
        refreshGUIList();

    }

    override function getXML():String {
        return YTDataHandler.createXMLDocument(dataSets);
    }

    override function setTitle(title:String):Void {
        
    }



    public function browseElos():Void {
    }

    public function saveElo():Void {
    }


    public function createXML():Void {

    }



}
