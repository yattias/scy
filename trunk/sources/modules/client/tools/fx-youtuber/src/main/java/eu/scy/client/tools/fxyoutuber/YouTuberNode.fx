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
    var nodes:Node[];
    override var children = bind [nodes, foreground];

    public var windowTitle:String;

    var foreground:Node[];

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


    postinit {
        //insert YouTubeDataEditor{} into nodes;

        /*
        println("asking fer yt url..");
        var test:String = JOptionPane.showInputDialog(
                null,
                null,
                "GIEV YOUTUBE URL",
                JOptionPane.OK_OPTION);
        println(test);
        */
        insert menuBar into nodes;
    }

    function getNewURL() {
        
    }

    public function addDataSet(inputURL:String, title:String, text:String) {
        var url = inputURL;
        //Dataset type
        //url kleinhacken
        if(url.equalsIgnoreCase("")) {
            url = "http://www.youtube.com/watch?v=spn-84Qe9i8";

        }
        var ytid = YouTubeSplitter.split(url);

        println(ytid);
        println(title);
        println(text);

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
