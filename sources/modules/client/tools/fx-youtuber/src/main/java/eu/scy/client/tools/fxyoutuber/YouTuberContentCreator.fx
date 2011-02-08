/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxyoutuber;
import javafx.scene.Node;
import javafx.util.Properties;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;

/**
 * @author pg
 */

public class YouTuberContentCreator extends ScyToolCreatorFX {

    public var eloFactory:IELOFactory;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository:IRepository;

    init {
        println("youtuber content creator");
    }

    public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow, windowContent: Boolean):Node {
        setWindowProperties(scyWindow);
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false");
        var ytNode:YouTuberNode = YouTuberNode{scyDesktop: scyWindow.windowManager.scyDesktop};
        var youTuberRepositoryWrapper:YouTuberRepositoryWrapper = new YouTuberRepositoryWrapper(ytNode);
        youTuberRepositoryWrapper.setRepository(repository);
        youTuberRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
        youTuberRepositoryWrapper.setEloFactory(eloFactory);
        youTuberRepositoryWrapper.setDocName(scyWindow.title);
        ytNode.setFormAuthorRepositoryWrapper(youTuberRepositoryWrapper);
        return ytNode;
    }

    function setWindowProperties(scyWindow:ScyWindow) {
        scyWindow.minimumWidth = 320;
        scyWindow.minimumHeight = 100;
    }
}
