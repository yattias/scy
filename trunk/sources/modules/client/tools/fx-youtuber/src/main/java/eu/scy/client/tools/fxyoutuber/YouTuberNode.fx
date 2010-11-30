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
import javax.swing.JOptionPane;

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
    override var children = bind nodes;

    public var windowTitle:String;

    postinit {
        insert YouTubeDataEditor{} into nodes;

        /*
        println("asking fer yt url..");
        var test:String = JOptionPane.showInputDialog(
                null,
                null,
                "GIEV YOUTUBE URL",
                JOptionPane.OK_OPTION);
        println(test);
        */
    }

    function getNewURL() {
        
    }




    override function getPrefWidth(height:Number):Number {
        return 600;
    }

    override function getPrefHeight(width:Number):Number {
        return 400;
    }

    function setScyWindowTitle():Void {
        scyWindow.title = "FormAuthor: {windowTitle}";

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
