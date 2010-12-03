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
    
    var contentList:VBox = VBox{};

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
        text: "add YT URL"
        action: function():Void {
            showPopup(YouTubeDataEditor{ ytNode: this });
            showPopup(Text { content: "foobar "});
            println("added buttons");
        }

    }


    def menuBar:HBox = HBox {
        content: [
            addURLButton
            ]
    }

    var content:VBox = VBox {
        content: [
            menuBar,
            sv]
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

    }

    override function getXML():String {
        return "yt rockx";
    }

    override function setTitle(title:String):Void {
        
    }



    public function browseElos():Void {
    }

    public function saveElo():Void {
    }



}
